package keisuke.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.StepCounter;
import jp.sf.amateras.stepcounter.diffcount.DiffSource;


/** 
 * カスタマイズして使用できる標準のステップカウンタです
 * keisuke: 解析機能強化を反映し、全面的に内部改修
 */
public class DefaultStepCounter extends ProgramLangRule implements StepCounter, Cutter {

	protected static Pattern CATEGORY_PATTERN = Pattern.compile("\\[\\[(.*?)\\]\\]");
	protected static Pattern IGNORE_PATTERN = Pattern.compile("\\[\\[IGNORE\\]\\]");

	// 処理対象とするFileのType（Factoryで指定）
	private String fileType = "UNDEF";
	
	// XHTML内のScriptlet言語
	protected boolean scriptletFlag = false;
	protected List<ProgramLangRule> scriptLangs = null;
	
	// Scriptletの開始・終了記号
	protected List<ScriptBlock> scriptBlocks = null;
	// Scriptletに入ったときの対象記号
	protected ScriptBlock onScriptBlock = null;
	
	// 処理中のPROGRAM/Script言語
	protected ProgramLangRule currentLang = null;
	// 複数行コメント領域に入ったときの対象記号
	protected AreaComment onAreaComment = null;
	// リテラル文字列に入ったときの対象記号
	protected LiteralString onLiteralString = null;
	// 関数型言語のコメント式に入った時の対象記号
	protected CommentExpr onCommentExpr = null;
		
	// count or cut 指定引数の定数定義
	protected static int OPE_COUNT = 0;
	protected static int OPE_CUT = 1;
	
	// 処理対象の文字列が行頭を含むか指定する定数定義
	protected static boolean LINE_HEAD = true;
	protected static boolean LINE_NO_HEAD = false;
 
	/** 通常のPROGRAM言語のコンストラクタ */
	public DefaultStepCounter() {
		this.currentLang = (ProgramLangRule)this;
	}
	
	/** Scriptlet言語のコンストラクタ */
	public DefaultStepCounter(boolean sflag) {
		this.scriptletFlag = sflag;
		if (sflag) { // Scriptlet言語
			this.scriptLangs = new ArrayList<ProgramLangRule>();
			this.scriptBlocks = new ArrayList<ScriptBlock>();
		} else { // 通常のPROGRAM言語
			this.currentLang = (ProgramLangRule)this;
		}
	}
	
	/** 別ファイルの計測前にリセット */
	public void reset() {
		if (this.scriptletFlag) {
			this.currentLang = null;
		}
		this.onScriptBlock = null;
		this.onAreaComment = null;
		this.onLiteralString = null;
		this.onCommentExpr = null;
	}
	
	/** ルールの定義流用のためにコピーインスタンス作成 */
	public void copyFrom(DefaultStepCounter counter) {
		if (counter == null) {
			return;
		}
		this.caseInsense = counter.caseInsense;
		this.indentOpt = counter.indentOpt;
		/* SkipPatternは言語個別に定義することとし流用しない
		if (counter.skipPatterns != null) {
			this.skipPatterns = new ArrayList<Pattern>();
			for (Pattern ptn : counter.skipPatterns) {
				this.skipPatterns.add(ptn);
			}
		}
		*/
		if (counter.lineComments != null) {
			this.lineComments = new ArrayList<LineComment>();
			for (LineComment lc : counter.lineComments) {
				this.lineComments.add(lc);
				//System.out.println("[DEBUG] copy lineComment " + lc);
			}
		}
		if (counter.areaComments != null) {
			this.areaComments = new ArrayList<AreaComment>();
			for (AreaComment ac : counter.areaComments) {
				this.areaComments.add(ac);
				//System.out.println("[DEBUG] copy areaComment " + ac);
			}
		}
		if (counter.literalStrings != null) {
			this.literalStrings = new ArrayList<LiteralString>();
			for (LiteralString ls : counter.literalStrings) {
				this.literalStrings.add(ls);
				//System.out.println("[DEBUG] copy literalString " + ls);
			}
		}
	}
	
	/** ファイルの種類を設定します */
	public void setFileType(String fileType){
		this.fileType = fileType;
		if (this.scriptletFlag == false) {
			setLangType(fileType);
		}
	}

	/** ファイルの種類を取得します */
	public String getFileType(){
		return this.fileType;
	}
	
	/** Scriptlet文字列を追加します */
	public void addScriptBlock(ScriptBlock area){
		if (this.scriptletFlag) {
			this.scriptBlocks.add(area);
		} else {
			throw new RuntimeException("Deny addScriptBlock().");
		}
	}
	
	/** Scriptを追加します */
	public void addScriptLang(ProgramLangRule lang){
		if (this.scriptletFlag) {
			this.scriptLangs.add(lang);
		} else {
			throw new RuntimeException("Deny addScriptLang().");
		}
	}
	
	/** Scriptを取得します */
	protected ProgramLangRule getScriptLang(String langType){
		if (langType == null) {
			return null;
		}
		if (this.scriptletFlag) {
			for (int i=0;i<this.scriptLangs.size();i++) {
				ProgramLangRule lang = this.scriptLangs.get(i);
				if (lang.getLangType().toUpperCase().equals(langType.toUpperCase())) {
					return lang;
				}
			}
			return null;
		} else {
			throw new RuntimeException("Deny getScriptLang().");
		}
	}
	
	/* インナークラス */
	/** 行数カウントと有効行抽出カットの結果格納クラス */
	protected class CountAndCutSource {
		private CountResult countResult = null;
		private DiffSource diffSource = null;
		
		protected void setCountResult(CountResult cresult) {
			countResult = cresult;
		}
		
		protected CountResult getCountResult() {
			return countResult;
		}
		
		protected void setDiffSource(DiffSource dsource) {
			diffSource = dsource;
		}
		
		protected DiffSource getDiffSource() {
			return diffSource;
		}
	}
	
	/**
	 * 行数をカウントします
	 * @param file カウント対象のファイル
	 * @param charset ファイルのエンコード
	 * @return CountResult 有効行・空白行・コメント行のカウント結果
	 */
	public CountResult count(File file, String charset) throws IOException {
		CountAndCutSource ccs = countOrCut(OPE_COUNT, file, charset);
		if (ccs == null) {
			return null;
		}
		return ccs.getCountResult();
	}
	/**
	 *  有効行以外をカットします
	 * @param file カウント対象のファイル
	 * @param charset ファイルのエンコード
	 * @return DiffSource 有効行のみにカットしたソース
	 */
	public DiffSource cut(File file, String charset) throws IOException {
		CountAndCutSource ccs = countOrCut(OPE_CUT, file, charset);
		if (ccs == null) {
			return null;
			}
		return ccs.getDiffSource();
	}
	/** カウントまたは有効行以外をカットします。 */
	protected CountAndCutSource countOrCut(int ope, File file, String charset) throws IOException {
		if (file == null || ! file.isFile()) {
			return null;
		}
		String charSetName = charset;
		if (charSetName == null) {
			// キャラクタセット無指定の場合は
			// プラットフォームデフォルトキャラクタセットを指定します。
			charSetName = Charset.defaultCharset().name();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), charSetName));
		
		String category = "";
		boolean ignore = false;
		long step    = 0;
		long non     = 0;
		long comment = 0;
		CountAndCutSource ccsObj = new CountAndCutSource();
		StringBuilder sb = new StringBuilder();

		try {
			String line = null;
			String indent = "";
			boolean areaFlag = false;
			boolean literalFlag = false;
			boolean commentFlag = false;
			boolean programFlag;

			//System.out.println("[DEBUG] ### Processing file=" + file.getPath() + " ###"); 
			if (this.scriptletFlag) { // Scriptlet言語
				programFlag = false;
			} else { // 通常のProgram
				programFlag = true;
				this.currentLang = (ProgramLangRule)this;
			}
			while((line = reader.readLine()) != null){
				if(category.length() == 0){
					Matcher matcher = CATEGORY_PATTERN.matcher(line);
					if(matcher.find()){
						category = matcher.group(1);
					}
				}
				if(IGNORE_PATTERN.matcher(line).find()){
					ignore = true;
					if (ope == OPE_CUT) {
						DiffSource ds = new DiffSource(null, ignore, category);
						ccsObj.setDiffSource(ds);
					}
					return ccsObj;
				}

				if (programFlag && this.currentLang != null && this.currentLang.indentOpt){
					indent = saveIndent(line); // インデント保持
				}
				String focusedLine = line.trim();
				if (checkBlancLine(focusedLine) || checkSkipLine(this.currentLang, focusedLine)){
					// 空白行またはスキップ対象行なので飛ばす
					non++;
					continue;
				}
				if (areaFlag == false && literalFlag == false && programFlag == true) {
					// 有効行の処理
					focusedLine = removeComments(focusedLine).trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							sb.append(indent + focusedLine + "\n");
						}
						/* DEBUG *
						if (fileType == "EmbeddedRuby") {
							System.out.println("[DEBUG] !"+focusedLine);
						}
						/* */
					} else {
						// 取り除かれたコメントが含まれていた
						comment++;
					}
				} else if (areaFlag) {
					// 複数行コメントの内部
					focusedLine = removeAreaCommentUntillEnd(this.currentLang, focusedLine, LINE_HEAD).trim();
					if ( this.onAreaComment == null ) {
						areaFlag = false;
					}
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							sb.append(indent + focusedLine + "\n");
						}
						/* DEBUG *
						if (fileType == "EmbeddedRuby") {
							System.out.println("[DEBUG] @"+focusedLine);
						}
						/* */
					} else {
						// 取り除かれたコメントが含まれていた
						comment++;
					}
				} else if (literalFlag) {
					// リテラル文字列の内部
					focusedLine = searchLiteralStringEnd(this.currentLang, focusedLine).trim();
					if ( this.onLiteralString == null ) {
						literalFlag = false;
					}
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							sb.append(indent + focusedLine + "\n");
						}
						/* DEBUG *
						if (fileType == "EmbeddedRuby") {
							System.out.println("[DEBUG] $"+focusedLine);
						}
						/* */
					} else {
						// リテラル内の空行	
						if (commentFlag) {
							// コメント式の中
							comment++;
						} else {
							// 通常はありえない
							non++;
							System.out.println("![WARN] Got Blanc in Internal Literal : file="
									+ file.getPath() + " line=[" + line + "]");
						}
					}		
				} else if (programFlag == false && this.scriptletFlag) {
					// Scriptletの外部
					focusedLine = searchScriptStart(focusedLine).trim();
					if ( this.onScriptBlock != null && this.scriptletFlag ) {
						programFlag = true;
					}
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							sb.append(indent + focusedLine + "\n");
						}
						/* DEBUG *
						if (fileType == "EmbeddedRuby") {
							System.out.print("[DEBUG] &"+focusedLine+"\n");
						}
						/* */
					} else {
						// 取り除かれたScriptletコメントのみの行
						//System.out.println("[DEBUG] Got Blanc in External Literal : "+line);
						comment++;
					}
				} else {
					// Scriptlet言語でないのにプログラム外部？？
					System.out.println("![WARN] Unknown the status of this line. : file="
							+ file.getPath() + " line=[" + line +"]");
					step++;
					if (ope == OPE_CUT) {
						sb.append(indent + focusedLine + "\n");
					}
				}
				// 行処理後のステータス確認
				if ( this.onScriptBlock == null && this.scriptletFlag) {
					programFlag = false;
				} else if ( this.onAreaComment != null ) {
					areaFlag = true;
				} else if ( this.onLiteralString != null ) {
					literalFlag = true;
				}
				if ( this.onCommentExpr != null ) {
					commentFlag = true;
				} else {
					commentFlag = false;
				}
			}
		} finally {
			reader.close();
		}
		if (ope == OPE_COUNT) {
			CountResult cs = new CountResult(file, file.getName(), getFileType(), category, step, non, comment);
			ccsObj.setCountResult(cs);
		} else {
			String str = sb.toString();
			DiffSource ds = new DiffSource(str, ignore, category);
			ccsObj.setDiffSource(ds);
		}
		return ccsObj;
	}

	/** 空行かどうかをチェック */
	protected boolean checkBlancLine(String line){
		String trimedLine = line.trim();
		if(trimedLine.equals("")){
			return true;
		}
		return false;
	}
	
	/** スキップパターンにマッチするかチェック */
	protected boolean checkSkipLine(ProgramLangRule pr, String line){
		if (pr == null) {
			return false;
		}
		for(int i=0;i<pr.skipPatterns.size();i++){
			if(pr.skipPatterns.get(i).matcher(line).find()){
				return true;
			}
		}
		return false;
	}
	
	/** 行からコメントを除去する処理 */
	protected String removeComments(String line) {
		return removeCommentFromLeft(currentLang, line, 0);
	}
	
	/* インナークラス */
	/* 解析対象記号の対象行内での位置、長さ、記号などを保持する */
	private class ParseInfo {
		private int position = -1;
		private int length = 0;
		private Object element = null;
	}
	
	/* インナークラス */
	/* 対象行内にある解析種類毎の最左の情報を保持する */
	private class EachLeftTarget {
		private ParseInfo lineComment = new ParseInfo();
		private ParseInfo areaComment = new ParseInfo();
		private ParseInfo literalString = new ParseInfo();
		private ParseInfo scriptBlock = new ParseInfo();

		
		private void setLineComment(int pos, int len, LineComment linecomm) {
			this.lineComment.position = pos;
			this.lineComment.length = len;
			if (linecomm != null) {
				this.lineComment.element = (Object)linecomm;
			}
		}
		
		private void setAreaComment(int pos, int len, AreaComment area) {
			this.areaComment.position = pos;
			this.areaComment.length = len;
			if (area != null) {
				this.areaComment.element = (Object)area;
			}
		}
		
		private void setLiteralString(int pos, int len, LiteralString literal) {
			this.literalString.position = pos;
			this.literalString.length = len;
			if (literal != null) {
				this.literalString.element = (Object)literal;
			}
		}
		
		private void setScriptBlock(int pos, int len, ScriptBlock script) {
			this.scriptBlock.position = pos;
			this.scriptBlock.length = len;
			if (script != null) {
				this.scriptBlock.element = (Object)script;
			}
		}
		
		/** 最左の解析対象を返す */
		private ParseInfo getMostLeftTarget() {
			// 最左のコメントを決定
			int tmpPosMin = -1;
			int tmplen = 0;
			// 行コメントで最左
			int lcPosMin = this.lineComment.position;
			int lclen = this.lineComment.length;
			// 暫定最左を行コメント
			tmpPosMin = lcPosMin;
			tmplen = lclen;
			
			// 行コメント vs. ブロックコメント
			// ブロックコメントで最左
			int acPosMin = this.areaComment.position;
			int aclen = this.areaComment.length;
			if (tmpPosMin < 0) {
				tmpPosMin = acPosMin;
				tmplen = aclen;
			} else {
				if (acPosMin >=  0) {
					if (acPosMin < tmpPosMin || (acPosMin == tmpPosMin && aclen > tmplen)) {
						// 最左は複数行コメント記号
						lcPosMin = -1;
						tmpPosMin = acPosMin;
						tmplen = aclen;		
					} else {
						// 最左は単一行コメント記号
						acPosMin = -1;
					}
				}
			}
			
			// コメントとリテラルのどっちが左か決定
			int lsPosMin = this.literalString.position;
			int lslen = this.literalString.length;
			// vs. 文字列リテラル
			if (tmpPosMin < 0) {
				tmpPosMin = lsPosMin;
				tmplen = lslen;
			} else {
				if (lsPosMin >= 0) {
					if (lsPosMin < tmpPosMin || (lsPosMin == tmpPosMin && lslen > tmplen)) {
						// 最左はリテラル記号
						lcPosMin = -1;
						acPosMin = -1;
						tmpPosMin = lsPosMin;
						tmplen = lslen;
					} else {
						// 最左はコメント記号
						lsPosMin = -1;
					}
				}
			}
			// コメント・リテラルの最左とScriptのどっちが左か決定
			int sbPosMin = this.scriptBlock.position;
			int sblen = this.scriptBlock.length;
			// vs. Scriptlet
			//System.out.println("[DEBUG] vs.ScriptBlock: sbPosMin="+sbPosMin+", sblen="+sblen
			//		+ ", elseone: tmpPosMin="+tmpPosMin+", tmplen="+tmplen);
			if (tmpPosMin < 0) {
				tmpPosMin = sbPosMin;
				tmplen = sblen;
			} else {
				if (sbPosMin >= 0) {
					if (sbPosMin < tmpPosMin || (sbPosMin == tmpPosMin && sblen >= tmplen)) {
						// 最左はScript記号
						lcPosMin = -1;
						acPosMin = -1;
						lsPosMin = -1;
						tmpPosMin = sbPosMin;
						tmplen = sblen;
					} else {
						// 最左はコメント or リテラル記号
						sbPosMin = -1;
					}
				}
			}
			if (lcPosMin >= 0) {
				//String ss = (String)(this.lineComment.element);
				//System.out.println("[DEBUG] Find LineComment start["+lcPosMin+"]:"+ss);
				return this.lineComment;
			} else if (acPosMin >= 0) {
				//String ss = ((AreaComment)(this.areaComment.element)).getStartString();
				//System.out.println("[DEBUG] Find AreaComment start["+acPosMin+"]:"+ss);
				return this.areaComment;
			} else if (lsPosMin >= 0) {
				//String ss = ((LiteralString)(this.literalString.element)).getStartString();
				//System.out.println("[DEBUG] Find LiteralString start["+lsPosMin+"]:"+ss);
				return this.literalString;
			} else if (sbPosMin >= 0) {
				//String ss = ((ScriptBlock)(this.scriptBlock.element)).getEndString();
				//System.out.println("[DEBUG] Find ScriptBlock end["+sbPosMin+"]:"+ss);
				return this.scriptBlock;
			} else {
				return null;
			}
		}
	}
	

	/** 最左に現れる解析対象記号を探す */
	protected ParseInfo searchMostLeftTarget(ProgramLangRule pr, String line, boolean head) {
		// コメント記述用
		int lcidx = -1;
		int acidx = -1;
		int lcPosArray[] = new int[pr.lineComments.size()];
		int acPosArray[] = new int[pr.areaComments.size()];
		int lcPosMin = -1;
		int acPosMin = -1;
		int lclen = 0;
		int aclen = 0;
		// リテラル文字列記述用
		int lsidx = -1;
		int lsPosArray[] = new int[pr.literalStrings.size()];
		int lsPosMin = -1;
		int lslen = 0;
		// Script記述用
		int sbPosMin = -1;
		int sblen = 0;
		
		// 単一行コメント記号をチェック
		for(int i=0;i<pr.lineComments.size();i++){
			LineComment linecomm = pr.lineComments.get(i);
			int pos = linecomm.searchStart(line, head, pr.caseInsense);
			lcPosArray[i] = pos;
			if (pos >= 0) {
				String start = linecomm.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (lcPosMin < 0 || pos < lcPosMin 
						|| (pos == lcPosMin && start.length() > lclen)) {
					// 最左の単一行コメント記号
					lcPosMin = pos;
					lcidx = i;
					lclen = start.length();
				}
			}
		}
		// 複数行コメント記号をチェック
		for(int i=0;i<pr.areaComments.size();i++){	
			AreaComment area = pr.areaComments.get(i);
			int pos = area.searchStart(line, head, pr.caseInsense);
			acPosArray[i] = pos;	
			if (pos >= 0) {
				String start = area.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (acPosMin < 0 || pos < acPosMin
						|| (pos == acPosMin && start.length() > aclen)) {
					// 最左の複数行コメント記号
					acPosMin = pos;
					acidx = i;
					aclen = start.length();
				}
			}
		}
		// リテラル文字列記号をチェック
		for(int i=0;i<pr.literalStrings.size();i++){
			LiteralString literal = pr.literalStrings.get(i);	
			int pos = literal.searchStart(line);
			lsPosArray[i] = pos;
			if (pos >= 0) {
				String start = literal.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (lsPosMin < 0 || pos < lsPosMin
						|| (pos == lsPosMin && start.length() > lslen)) {
					// 最左のリテラル記号
					lsPosMin = pos;
					lsidx = i;
					lslen = start.length();
				}
			}
		}
		// Script終了記号をチェック
		if (this.onScriptBlock != null) {
			ScriptBlock block = this.onScriptBlock;
			int pos = block.searchEnd(line);		
			if (pos >= 0) {
				String end = block.getEndString();
				sbPosMin = pos;
				sblen = end.length();
			}
		}
		// 各種類ごとの探索結果の保管と最左対象の決定
		EachLeftTarget each = new EachLeftTarget();
		if (lcPosMin >= 0) {
			each.setLineComment(lcPosMin, lclen, pr.lineComments.get(lcidx));
		}
		if (acPosMin >= 0) {
			each.setAreaComment(acPosMin, aclen, pr.areaComments.get(acidx));
		}
		if (lsPosMin >= 0) {
			each.setLiteralString(lsPosMin, lslen, pr.literalStrings.get(lsidx));
		}
		if (sbPosMin >= 0) {
			each.setScriptBlock(sbPosMin, sblen, this.onScriptBlock);
		}
		return each.getMostLeftTarget();
	}
	
	/** 先に現れるコメント記号から除去処理を再帰的に繰り返す */
	protected String removeCommentFromLeft(ProgramLangRule pr, String line, int recursion) {
		if (line == null || line.isEmpty()) {
			return "";
		}
		
		// 行の最左の解析対象記号を探す
		boolean head = recursion == 0 ? true : false;
		ParseInfo pi = searchMostLeftTarget(pr, line, head);
		
		// 最左に選ばれたものを処理する
		if (pi == null) {
			// 解析対象のコメント記号・リテラル記号・スクリプト終了記号はなし
			return dealProgramCode(pr, line);
		
		} else if (pi.element instanceof LineComment) {
			// 最左は単一行コメント記号、行末までコメント
			//System.out.println("[DEBUG] Find (Line Comment):" + ((LineComment)pi.element).getStartString());
			return dealProgramCode(pr, line.substring(0, pi.position));
			
		} else if (pi.element instanceof AreaComment) {
			// 最左は複数行コメント記号
			AreaComment area = (AreaComment) pi.element;
			// 複数行コメント開始
			this.onAreaComment = area;
			//System.out.println("Find (Area Comment):" + area.getStartString());
			StringBuilder sb = new StringBuilder();
			if ( pi.position > 0) {
				// コメントの左側は有効行なので返却対象
				sb.append(dealProgramCode(pr, line.substring(0, pi.position)));
			}
			// コメント開始記号の左端から右の処理
			sb.append(dealAreaCommentStart(pr, line.substring(pi.position), area));
			return sb.toString();
			
		} else if (pi.element instanceof LiteralString) {
			// 最左はリテラル文字列記号
			LiteralString literal = (LiteralString)pi.element;
			StringBuffer sb = new StringBuffer();
			// Rubyのヒアドキュメント開始記号に除外ケースあり、dealLiteralStringStart()
			// の中では判定できないため、ここでチェック
			if (checkExcludeLiteralStringStart(line, literal)) {
				// 除外ケース
				String start = literal.getStartString();
				int pos = pi.position + start.length();
				sb.append(line.substring(0, pos));
				sb.append(removeCommentFromLeft(pr, line.substring(pos), recursion+1));
				return sb.toString();
			}
			// リテラル文字列開始
			this.onLiteralString = literal;
			//System.out.println("[DEBUG] Find (Literal String):" + literal.getStartString());			
			// 開始記号含まない左側の有効行を処理
			sb.append(dealProgramCode(pr, line.substring(0, pi.position)));
			// 開始記号含む右のリテラルの処理
			sb.append(dealLiteralStringStart(pr, line.substring(pi.position), literal));
			return sb.toString();
			
		} else if (pi.element instanceof ScriptBlock) {
			// 最左はScript終了記号
			ScriptBlock block = (ScriptBlock)pi.element;
			this.onScriptBlock = null;
			this.currentLang = null;
			//System.out.println("[DEBUG] Find (Script End):" + block.getEndString());
			StringBuffer sb = new StringBuffer();	
			// 終了記号を含まない有効行を処理
			sb.append(dealProgramCode(pr, line.substring(0, pi.position)));
			// 終了記号を含む右側の外部リテラルを処理
			sb.append(dealScriptBlockEnd(pr, line.substring(pi.position), block));
			return sb.toString();
			
		} else {
			// ここには到達しないはず
			System.out.println("![WARN] Unknown ParseInfo. : "+line);
			return dealProgramCode(pr, line);
		}
	}
	
	/** 複数行ブロックコメントの開始から行末までの処理をする */
	protected String dealAreaCommentStart(ProgramLangRule pr, String line, AreaComment area) {
		area.addNest();
		// 開始記号末尾の位置
		String start = area.getStartString();
		int pos = start.length();
		// 開始記号より右のコメントの処理
		return removeAreaCommentUntillEnd(pr, line.substring(pos), LINE_NO_HEAD);
	}
	
	/** リテラル文字列の開始から行末までの処理をする */
	protected String dealLiteralStringStart(ProgramLangRule pr, String line, LiteralString literal) {
		// 通常の引用符リテラル
		String start = literal.getStartString();
		StringBuilder sb = new StringBuilder();
		// 開始記号を処理
		int pos = start.length();
		sb.append(dealProgramCode(pr, line.substring(0, pos), false));
		// 右側を処理
		sb.append(searchLiteralStringEnd(pr, line.substring(pos)));
		return sb.toString();
	}
	
	/** Scriptlet終了記号の開始から行末までの処理をする */
	protected String dealScriptBlockEnd(ProgramLangRule pr, String line, ScriptBlock block) {
		// 通常の引用符リテラル
		String end = block.getEndString();
		StringBuilder sb = new StringBuilder();
		// 開始記号を処理
		int pos = end.length();
		sb.append(dealProgramCode(pr, line.substring(0, pos), false));
		// 右側を処理
		sb.append(searchScriptStart(line.substring(pos)));
		return sb.toString();
	}
	
	/** Script開始記号が含まれるかどうかをチェックし、有効な文字列を返す */
	protected String searchScriptStart(String line) {
		int asidx = -1;
		int asPosArray[] = new int[this.scriptBlocks.size()];
		int asPosMin = -1;
		int aslen = 0;

		// Script記号をチェック
		for(int i=0;i<this.scriptBlocks.size();i++){
			ScriptBlock block = this.scriptBlocks.get(i);
			int pos = block.searchStart(line);
			asPosArray[i] = pos;
			if (pos >= 0) {
				String start = block.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (asPosMin < 0 || pos < asPosMin
						|| (pos == asPosMin && start.length() > aslen)) {
					// 最左の複数行コメント記号
					asPosMin = pos;
					asidx = i;
					aslen = start.length();
				}
			}
		}
		if (asPosMin < 0) {
			// Script外部のリテラル行
			return line;
		}
		// Script記号を見つけた
		StringBuilder sb = new StringBuilder();
		ScriptBlock block = this.scriptBlocks.get(asidx);
		this.onScriptBlock = block;
		this.currentLang = (ProgramLangRule)this;
		String target = null;
		// Script記号の左側はScript外部なのでそのまま返却
		// Script記号自体はScript内部として再度解析対象にする。理由："<%"に対する"<%--"
		if (asPosMin > 0) {
			sb.append(line.substring(0, asPosMin));
			target = line.substring(asPosMin);
		} else {
			target = line;
		} 
		sb.append(removeCommentFromLeft(this.currentLang, target, 1));
		return sb.toString();
	}
	

	/** 複数行コメントが終了しているかチェックし、コメント部分除き有効な文字列を返す */
	protected String removeAreaCommentUntillEnd(ProgramLangRule pr, String line, boolean head){
		AreaComment area = this.onAreaComment;
		if ( area == null ) {
			return line;
		}
		int pos = area.searchEnd(line, head, pr.caseInsense);
		if (pos >= 0) {
			// コメント終了があった
			this.onAreaComment = null;
			// コメント終了記号の右側のコードの解析処理		
			if ( pos < line.length() ) {
				return removeCommentFromLeft(pr, line.substring(pos), 1);
			}
		}
		return "";
	}

	/** リテラル文字列が終了しているかチェックし、有効な文字列を返す */
	protected String searchLiteralStringEnd(ProgramLangRule pr, String line){
		LiteralString literal = this.onLiteralString;
		int pos = literal.searchEnd(line);
		if ( pos >= 0) {
			// リテラル終了
			this.onLiteralString = null;
			// リテラルの内容を返す
			StringBuffer sb = new StringBuffer();
			sb.append(dealProgramCode(pr, line.substring(0, pos), false));
			// リテラル終了後の右側のコードを解析して返す
			if ( pos < line.length()) {
				sb.append(removeCommentFromLeft(pr, line.substring(pos), 1));
			}
			//dealProgramCode処理後の内容なのでそのまま返す
			return sb.toString();
		}
		// 文字列終了していないので、全てリテラル
		return dealProgramCode(pr, line, false);
	}
	
	/** ソースコードの有効行部分を処理する */
	protected String dealProgramCode(ProgramLangRule pr, String line) {
		// lineの内容はコメントのないソースコードの一部
		// 通常言語はそのまま全てを返す
		return dealProgramCode(pr, line, true);
	}
	
	/**
	 * FuctionalLang(Scheme, Clojure)でOverrideする有効行処理メソッド
	 * @param pr	処理中のプログラム言語ルール　　　
	 * @param line	処理対象行でコメントは抜かれたソースコードのみの文字列
	 * @param codeFlag	プログラムコードが含まれるか示すフラグ（falseは文字列リテラルのみの場合）
	 * @return
	 */
	protected String dealProgramCode(ProgramLangRule pr, String line, boolean codeFlag) {
		// lineの内容はコメントのないソースコードの一部
		// 通常言語はcodeFlagの値によらずそのまま全てを返す
		//return line;
		
		// lineの内容はコメントの抜かれたソースコードのみからなる文字列
		CommentExpr expr = this.onCommentExpr;
		if ( expr == null ) {
			// コメント式ではないのですべて出力
			return line;
		} else if (codeFlag == false) {
			// コメント式でかつリテラルのみ
			return "";
		}
		// コメント式でコードがある行なので解析必要
		int pos = expr.searchEnd(line);
		if (pos < 0) {
			// コメント式の中
			return "";
		}
		// コメント式が終了
		onCommentExpr = null;
		if ( pos < line.length() ) {
			// 終了記号の右側の処理
			// コメント式の対象範囲外なので、dealProgramCode()を被せない
			return line.substring(pos);
		}
		return "";
	}
	
	/**
	 * リテラル文字列の開始記号に一致するが、直前の内容で除外が必要なケースを
	 * 判定する
	 *  RubyでOverrideするチェックメソッド 
	 *  @param line 処理対象の行文字列
	 *  @param literal 開始記号を保持するLiteralStringのインスタンス
	 *  @return 判定結果のbool値
	 */
	protected boolean checkExcludeLiteralStringStart(String line, LiteralString literal) {
		//　通常は除外必要なケースないので固定でfalse
		return false;
	}
	
	/** 左端のインデントを取得、タブは８文字インデント処理 */
	protected String saveIndent(String str) {
		if (str == null) {
	        return null;
	    }
	    
	    char[] val = str.toCharArray();
	    int idx = 0;
	    int ind = 0;
	    int len = val.length;
	    
	    while (idx < len) {
	        if (val[idx] == ' ') {
	        	ind++;
	        	idx++;
	        } else if (val[idx] == '\t') {
	        	int tabpad = ind % 8;
	        	ind += 8 - tabpad;
	        	idx++;
	        } else {
	        	idx = len;
	        }
	    }
	    
	    StringBuilder sb = new StringBuilder();
	    while (ind > 0) {
	    	sb.append(" ");
	    	ind--;
	    }
	    return sb.toString();
	}
}

