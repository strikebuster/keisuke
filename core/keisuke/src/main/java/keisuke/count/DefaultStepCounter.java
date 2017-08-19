package keisuke.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.StepCounter;
import jp.sf.amateras.stepcounter.diffcount.DiffSource;


/**
 * 言語毎のコメント記号やリテラル記号を設定して解析できる標準の
 * 汎用ステップカウンタ
 */
public class DefaultStepCounter extends ProgramLangRule implements StepCounter, Cutter {

	protected static final Pattern CATEGORY_PATTERN = Pattern.compile("\\[\\[(.*?)\\]\\]");
	protected static final Pattern IGNORE_PATTERN = Pattern.compile("\\[\\[IGNORE\\]\\]");
	protected static final int TABWIDTH = 8;

	// 処理対象とするFileのType（Factoryで指定）
	private String fileType = "UNDEF";

	// XHTML内のScriptlet言語
	private boolean scriptletFlag = false;
	private List<ProgramLangRule> scriptLangs = null;
	// Scriptletの開始・終了記号
	private List<ScriptBlock> scriptBlocks = null;

	// 処理中のPROGRAM/Script言語
	private ProgramLangRule currentLang = null;
	// 解析状態スタック
	private Deque<ParseSourceCode> codeStack = null;
	private Deque<ParseSyntaxRule> ruleStack = null;

	// count or cut 指定引数の定数定義
	protected static final int OPE_COUNT = 0;
	protected static final int OPE_CUT = 1;

	// 対象ソースファイルが無視対象か指定する定数定義
	protected static final boolean IGNORE_YES = true;
	protected static final boolean IGNORE_NON = false;

	// 処理対象の文字列が行頭を含むか指定する定数定義
	protected static final boolean LINE_HEAD = true;
	protected static final boolean LINE_NO_HEAD = false;

	// 有効行を取り出す際にリテラル以外の有効行文字が含まれるか示すフラグ
	protected static final boolean HAVING_VALID_STATEMENT = true;
	protected static final boolean HAVING_ONLY_LITERAL = false;

	/**
	 * 通常のPROGRAM言語のコンストラクタ
	 */
	public DefaultStepCounter() {
		this.setCurrentLang((ProgramLangRule) this);
		this.ruleStack = new ArrayDeque<ParseSyntaxRule>();
		this.pushNewStatusAsProgramRule();
	}

	/**
	 * Scriptlet言語用のコンストラクタ
	 * @param sflag Scriptlet言語の場合にtrueを指定する
	 */
	public DefaultStepCounter(final boolean sflag) {
		this.setScriptletProgram(sflag);
		this.ruleStack = new ArrayDeque<ParseSyntaxRule>();
		if (sflag) { // Scriptlet言語
			this.scriptLangs = new ArrayList<ProgramLangRule>();
			this.scriptBlocks = new ArrayList<ScriptBlock>();
			this.codeStack = new ArrayDeque<ParseSourceCode>();
			this.codeStack.push(new ParseSourceCode(SourceCodeStatus.EXTERNAL_LITERAL));
		} else { // 通常のPROGRAM言語
			this.setCurrentLang((ProgramLangRule) this);
			this.pushNewStatusAsProgramRule();
		}
	}

	/**
	 * 別ファイルの計測前にインスタンス変数をリセットする
	 */
	protected void reset() {
		if (this.isScriptletProgram()) {
			this.setCurrentLang(null);
			this.codeStack = new ArrayDeque<ParseSourceCode>();
			this.codeStack.push(new ParseSourceCode(SourceCodeStatus.EXTERNAL_LITERAL));
		}
		this.ruleStack = new ArrayDeque<ParseSyntaxRule>();
		this.pushNewStatusAsProgramRule();
	}

	/**
	 * ルールの定義流用のために他のインスタンスの設定をコピーする
	 * ただし、SkipPatternはコピー対象外。
	 * @param counter 流用するコピー元のDefaultStepCounterインスタンス
	 */
	protected void copyFrom(final DefaultStepCounter counter) {
		if (counter == null) {
			return;
		}
		this.setCaseInsense(counter.isCaseInsense());
		this.setUsingIndentBlock(counter.isUsingIndentBlock());
		/* SkipPatternは言語個別に定義することとし流用しない
		if (counter.skipPatterns != null) {
			this.skipPatterns = new ArrayList<Pattern>();
			for (Pattern ptn : counter.skipPatterns) {
				this.skipPatterns.add(ptn);
			}
		}
		*/
		if (counter.lineComments() != null) {
		//this.lineComments = new ArrayList<LineComment>();
			for (LineComment lc : counter.lineComments()) {
				this.addLineComment(lc);
				//System.out.println("[DEBUG] copy lineComment " + lc);
			}
		}
		if (counter.areaComments() != null) {
			//this.areaComments = new ArrayList<AreaComment>();
			for (AreaComment ac : counter.areaComments()) {
				this.addAreaComment(ac);
				//System.out.println("[DEBUG] copy areaComment " + ac);
			}
		}
		if (counter.literalStrings() != null) {
			//this.literalStrings = new ArrayList<LiteralString>();
			for (LiteralString ls : counter.literalStrings()) {
				this.addLiteralString(ls);
				//System.out.println("[DEBUG] copy literalString " + ls);
			}
		}
	}

	/**
	 * ファイルの種類を設定します
	 * @param type ファイル種類
	 */
	protected void setFileType(final String type) {
		this.fileType = type;
		if (!this.isScriptletProgram()) {
			this.setLanguageName(fileType);
		}
	}

	/**
	 * ファイルの種類を取得します
	 * @return ファイル種類
	 */
	public String getFileType() {
		return this.fileType;
	}

	/**
	 * このプログラムがScriptlet形式であるか設定する
	 * @param bool Scriptletプログラムならtrueを指定
	 */
	protected void setScriptletProgram(final boolean bool) {
		this.scriptletFlag = bool;
	}

	/**
	 * このプログラムがScriptlet形式であるかチェックする
	 * @return Scriptletプログラムならtrueを指定
	 */
	protected boolean isScriptletProgram() {
		return this.scriptletFlag;
	}

	/**
	 * Scriptletブロック定義を追加します
	 * @param block Scriptletブロック定義
	 */
	protected void addScriptBlock(final ScriptBlock block) {
		if (this.isScriptletProgram()) {
			this.scriptBlocks.add(block);
		} else {
			throw new RuntimeException("Deny addScriptBlock().");
		}
	}

	/**
	 * このScriptlet言語用のSciptletブロック定義記号の配列を返す
	 * @return Scriptlet定義記号の配列
	 */
	protected List<ScriptBlock> scriptBlocks() {
		return this.scriptBlocks;
	}

	/**
	 * Script言語を追加します
	 * @param lang Script言語定義
	 */
	protected void addScriptLang(final ProgramLangRule lang) {
		if (this.isScriptletProgram()) {
			this.scriptLangs.add(lang);
		} else {
			throw new RuntimeException("Deny addScriptLang().");
		}
	}

	/**
	 * このScriptlet言語用のScipt言語定義の配列を返す
	 * @return Scipt言語定義の配列
	 */
	protected List<ProgramLangRule> scriptLangs() {
		return this.scriptLangs;
	}

	/**
	 * Script言語名を指定して定義中の該当する言語定義を取得します
	 * @param langType Script言語名
	 * @return プログラム言語定義
	 */
	protected ProgramLangRule getScriptLang(final String langType) {
		if (langType == null) {
			return null;
		}
		if (this.isScriptletProgram()) {
			for (int i = 0; i < this.scriptLangs.size(); i++) {
				ProgramLangRule lang = this.scriptLangs.get(i);
				if (lang.languageName().toUpperCase().equals(langType.toUpperCase())) {
					return lang;
				}
			}
			return null;
		} else {
			throw new RuntimeException("Deny getScriptLang().");
		}
	}

	/**
	 * 処理する言語定義インスタンスを設定する
	 * @param lang 言語定義インスタンス
	 */
	protected void setCurrentLang(final ProgramLangRule lang) {
		this.currentLang = lang;
	}

	/**
	 * 処理中の言語定義インスタンスを返す
	 * @return 処理中の言語定義インスタンス
	 */
	protected ProgramLangRule currentLang() {
		return this.currentLang;
	}

	/* インナークラス */
	/**
	 * 行数カウントと有効行抽出カットの結果格納クラス
	 */
	protected static class CountAndCutSource {
		private int operationFlag;
		private CountResult countResult = null;
		private long executableStep = 0;
		private long commentStep = 0;
		private long blancStep = 0;
		private DiffSource diffSource = null;
		private StringBuilder buildingSource = null;

		/**
		 * ステップカウントまたはコメント除去ソース抽出の操作を指定するコンストラクター
		 * @param ope OPE_COUNTまたはOPE_CUTを指定
		 */
		protected CountAndCutSource(final int ope) {
			this.operationFlag = ope;
			this.buildingSource = new StringBuilder();
		}

		/**
		 * 実行ステップ数に１加える
		 * OPE_CUT処理時にはコメントを除いたソースに引数の文字列を追記する
		 * @param line ソースの実行ステップ文字列
		 */
		protected void appendExecutableStep(final String line) {
			this.increaseExecutableStep();
			if (this.operationFlag == OPE_CUT) {
				if (line == null || line.isEmpty()) {
					return;
				}
				this.buildingSource.append(line);
			}
			/* DEBUG *
			if (getFileType().equals("Haskell")) {
				System.out.print("[DEBUG] type=" + getFileType() + " cut:" + line);
			}
			/* */
		}

		/**
		 * 実行ステップ数に１加える
		 */
		private void increaseExecutableStep() {
			this.executableStep++;
		}

		/**
		 * コメントステップ数に１加える
		 */
		protected void increaseCommentStep() {
			this.commentStep++;
		}

		/**
		 * 空白ステップ数に１加える
		 */
		protected void increaseBlancStep() {
			this.blancStep++;
		}

		/**
		 * ステップカウント結果を持つCountResultインスタンスをセットする
		 * @param cresult CountResultインスタンス
		 */
		protected void setCountResult(final CountResult cresult) {
			this.countResult = cresult;
		}

		/**
		 * 保持しているステップカウント結果を元にCountResultインスタンスを作成して保持する
		 * @param file ソースのFileインスタンス
		 * @param type ソースのファイルタイプ
		 * @param category ソースのカテゴリ
		 */
		protected void createCountResult(final File file, final String type, final String category) {
			CountResult cresult = new CountResult(file, file.getName(), type, category,
					this.executableStep, this.blancStep, this.commentStep);
			this.setCountResult(cresult);
		}

		/**
		 * 保持しているステップカウント結果を返す
		 * @return ステップカウント結果
		 */
		protected CountResult getCountResult() {
			return this.countResult;
		}

		/**
		 * コメントを除いたソースを持つDiffSourceインスタンスをセットする
		 * @param dsource DiffSourceインスタンス
		 */
		protected void setDiffSource(final DiffSource dsource) {
			this.diffSource = dsource;
		}

		/**
		 * 保持しているコメントを除いたソースを元にDiffSourceインスタンスを作成して保持する
		 * @param category ソースのカテゴリ
		 */
		protected void createDiffSource(final String category) {
			DiffSource ds = new DiffSource(this.buildingSource.toString(), IGNORE_NON, category);
			this.setDiffSource(ds);
		}

		/**
		 * 保持しているコメントを除いたソースを返す
		 * @return コメントを除いたソース
		 */
		protected DiffSource getDiffSource() {
			return this.diffSource;
		}

		/**
		 * 保持しているステップカウント結果またはコメントを除いたソースを元に処理フラグによって
		 * 結果となるCountResultインスタンスまたはDiffSourceインスタンスを作成して保持する
		 * @param file ソースのFileインスタンス
		 * @param type ソースのファイルタイプ
		 * @param category ソースのカテゴリ
		 */
		protected void createResult(final File file, final String type, final String category) {
			if (this.operationFlag == OPE_COUNT) {
				this.createCountResult(file, type, category);
			} else if (this.operationFlag == OPE_CUT) {
				this.createDiffSource(category);
			}
		}

		/**
		 * 解析対象ソースが無視対象の場合の結果を作成して保持する
		 * @param category ソースのカテゴリ
		 */
		protected void createIgnoredResult(final String category) {
			if (this.operationFlag == OPE_COUNT) {
				this.setCountResult(null);
			} else if (this.operationFlag == OPE_CUT) {
				DiffSource ds = new DiffSource(null, IGNORE_YES, category);
				this.setDiffSource(ds);
			}
		}

		/**
		 * 別のインスタンスのカウント変数をコピーする
		 * CountResultインスタンスとDiffSourceインスタンスはコピーせずに
		 * nullに初期化する
		 * @param another コピー元のインスタンス
		 */
		protected void copyFrom(final CountAndCutSource another) {
			if (another == null) {
				this.reset();
				return;
			}
			this.countResult = null;
			this.executableStep = another.executableStep;
			this.commentStep = another.commentStep;
			this.blancStep = another.blancStep;
			this.diffSource = null;
			this.buildingSource = new StringBuilder(another.buildingSource);
		}

		/**
		 * インスタンス変数を初期化する
		 */
		protected void reset() {
			this.countResult = null;
			this.executableStep = 0;
			this.commentStep = 0;
			this.blancStep = 0;
			this.diffSource = null;
			this.buildingSource = new StringBuilder();
		}
	}

	/**
	 * 行数をカウントします
	 * @param file カウント対象のファイル
	 * @param charset ファイルのエンコード
	 * @return 有効行・空白行・コメント行のカウント結果
	 * @throws IOException ファイル読み取りで異常があれば発行
	 */
	public CountResult count(final File file, final String charset) throws IOException {
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
	 * @return 有効行のみにカットしたソース
	 * @throws IOException ファイル読み取りで異常があれば発行
	 */
	public DiffSource cut(final File file, final String charset) throws IOException {
		CountAndCutSource ccs = countOrCut(OPE_CUT, file, charset);
		if (ccs == null) {
			return null;
			}
		return ccs.getDiffSource();
	}

	/**
	 * カウントまたは有効行以外をカットします。
	 * @param ope ステップカウントかコメント除去かの指定値
	 * @param file カウント対象のファイル
	 * @param charset ファイルのエンコード
	 * @return 引数opeで指定した処理の結果を保持するインスタンス
	 * @throws IOException ファイル読み取りで異常があれば発行
	 */
	protected CountAndCutSource countOrCut(final int ope, final File file, final String charset)
			throws IOException {
		if (file == null || !file.isFile()) {
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
		CountAndCutSource ccsObj = new CountAndCutSource(ope);

		try {
			String line = null;
			String indent = "";

			//System.out.println("[DEBUG] ### Processing file=" + file.getPath() + " ###");
			while ((line = reader.readLine()) != null) {
				if (category.length() == 0) {
					String str = this.findCategory(line);
					if (str != null) {
						category = str;
					}
				}
				if (IGNORE_PATTERN.matcher(line).find()) {
					ccsObj.createIgnoredResult(category);
					return ccsObj;
				}

				if ((!this.isScriptletProgram() || this.isInScriptletCode()) && this.isInProgramRule()
						&& this.currentLang().isUsingIndentBlock()) {
					indent = this.saveIndent(line); // インデント保持
				} else {
					indent = "";
				}
				String focusedLine = line.trim();
				if (this.checkBlancLine(focusedLine)
						|| this.checkSkipLine(this.currentLang(), focusedLine)) {
					// 空白行またはスキップ対象行なので飛ばす
					ccsObj.increaseBlancStep();
					continue;
				}

				// Scriptlet言語の外部リテラルか確認
				if (this.isScriptletProgram() && !this.isInScriptletCode()) {
					// Scriptletの外部
					focusedLine = this.searchScriptStart(focusedLine).trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						ccsObj.appendExecutableStep(indent + focusedLine + "\n");
					} else {
						// 取り除かれたScriptletコメントのみの行
						//System.out.println("[DEBUG] Got Blanc in External Literal : "+line);
						ccsObj.increaseCommentStep();
					}

				// Programコード(Scriptlet)の内部
				} else if (this.isInProgramRule() || this.isInCommentExpressionRule()) {
					// 有効行の処理
					focusedLine = this.removeComments(focusedLine).trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						ccsObj.appendExecutableStep(indent + focusedLine + "\n");
					} else {
						// 取り除かれたコメントが含まれていた
						ccsObj.increaseCommentStep();
					}
				} else if (this.isInCommentRule()) {
					// 複数行コメントの内部
					focusedLine = this.removeAreaCommentUntilEnd(
							this.currentLang(), focusedLine, LINE_HEAD).trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						ccsObj.appendExecutableStep(indent + focusedLine + "\n");
					} else {
						// 取り除かれたコメントが含まれていた
						ccsObj.increaseCommentStep();
					}
				} else if (this.isInLiteralRule()) {
					// リテラル文字列の内部
					focusedLine = this.searchLiteralStringEnd(
							this.currentLang(), focusedLine).trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						ccsObj.appendExecutableStep(indent + focusedLine + "\n");
					} else {
						// リテラル内の空行
						if (this.isInsideOfCommentExpressionRule()) {
							// コメント式の中
							ccsObj.increaseCommentStep();
						} else {
							// 通常はありえない
							ccsObj.increaseBlancStep();
							System.out.println(
									"![WARN] Got Blanc in Internal Literal : file="
									+ file.getPath() + " line=" + line);
						}
					}
				} else {
					// Scriptlet言語でないのにプログラム外部？？
					System.out.println("![WARN] Unknown the status of this line. : file="
							+ file.getPath() + " line=" + line);
					ccsObj.appendExecutableStep(indent + focusedLine + "\n");
				}
			}
		} finally {
			reader.close();
		}
		ccsObj.createResult(file, this.getFileType(), category);
		return ccsObj;
	}

	enum SourceCodeStatus {
		EXTERNAL_LITERAL,
		IN_SCRIPTLET
	}

	protected static class ParseSourceCode {
		private SourceCodeStatus status;
		private ScriptBlock scriptBlock;
		private ProgramLangRule language;

		protected ParseSourceCode(final SourceCodeStatus stat) {
			if (stat == null) {
				throw new RuntimeException("ScriptletStatus does not allow null.");
			}
			this.status = stat;
			this.scriptBlock = null;
			this.language = null;
		}

		protected ParseSourceCode(final SourceCodeStatus stat,
				final ScriptBlock block, final ProgramLangRule lang) {
			if (stat == null) {
				throw new RuntimeException("ScriptletStatus does not allow null.");
			}
			this.status = stat;
			if (block == null) {
				throw new RuntimeException("ScriptBlock does not allow null.");
			}
			this.scriptBlock = block;
			if (lang == null) {
				throw new RuntimeException("ProgramLangRule does not allow null.");
			}
			this.language = lang;
		}

		/**
		 * 保持するSourceCodeStatusの値を返す
		 * @return SourceCodeStatusの値
		 */
		protected SourceCodeStatus status() {
			return this.status;
		}

		/**
		 * 保持するScriptBlock要素を返す
		 * @return ScriptBlock要素
		 */
		protected ScriptBlock scriptBlock() {
			return this.scriptBlock;
		}

		/**
		 * 保持するプログラ言語ルール定義を返す
		 * @return プログラ言語ルール定義
		 */
		protected ProgramLangRule language() {
			return this.language;
		}
	}

	enum SyntaxStatus {
		IN_PROGRAM,
		IN_LITERAL,
		IN_COMMENT,
		IN_COMMENT_EXPR
	}

	protected static class ParseSyntaxRule {
		private SyntaxStatus status;
		private AbstractBlock subject;

		protected ParseSyntaxRule(final SyntaxStatus stat) {
			if (stat == null) {
				throw new RuntimeException("SyntaxStatus does not allow null.");
			}
			this.status = stat;
			this.subject = null;
		}

		protected ParseSyntaxRule(final SyntaxStatus stat, final AbstractBlock obj) {
			if (stat == null) {
				throw new RuntimeException("SyntaxStatus does not allow null.");
			}
			this.status = stat;
			if (obj == null) {
				throw new RuntimeException("Object does not allow null.");
			}
			this.subject = obj;
		}

		/**
		 * 保持するSyntaxStatusの値を返す
		 * @return SyntaxStatusの値
		 */
		protected SyntaxStatus status() {
			return this.status;
		}

		/**
		 * 保持する解析ルール要素を返す
		 * @return 解析ルール要素
		 */
		protected AbstractBlock subject() {
			return this.subject;
		}
	}

	/**
	 * Scriptlet内部の処理中かチェック
	 * @return Scriptlet処理中ならtrue
	 */
	protected boolean isInScriptletCode() {
		if (this.isScriptletProgram()) {
			SourceCodeStatus stat = this.codeStack.peek().status();
			if (stat == SourceCodeStatus.EXTERNAL_LITERAL) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Scriptlet解析スタックから先頭のIN_SCRIPTLETの要素を返す
	 * @return ParseSourceCodeインスタンス
	 */
	protected ParseSourceCode peekStatusAsScriptletCode() {
		if (this.isInScriptletCode()) {
			return this.codeStack.peek();
		}
		System.out.println("![WARN] peek failed, because it is not IN_SCRIPTLET.");
		return null;
	}

	/**
	 * Scriptlet解析スタックから先頭のIN_SCRIPTLETの要素を除く
	 */
	protected void popStatusAsScriptletCode() {
		if (this.isInScriptletCode()) {
			this.codeStack.pop();
			return;
		}
		System.out.println("![WARN] pop failed, because it is not IN_SCRIPTLET.");
	}

	/**
	 * Scriptlet解析スタックにScriptlet開始を積む
	 * @param block Scriptlet記号定義インスタンス
	 * @param lang プログラム言語ルール定義インスタンス
	 */
	protected void pushNewStatusAsScriptletCodeWith(final ScriptBlock block, final ProgramLangRule lang) {
		this.codeStack.push(new ParseSourceCode(SourceCodeStatus.IN_SCRIPTLET, block, lang));
	}

	/**
	 * プログラムコードの通常行を処理中かチェック
	 * リテラルやコメントの処理中ならfalseになる
	 * @return 通常行処理中ならtrue
	 */
	protected boolean isInProgramRule() {
		SyntaxStatus ss = this.ruleStack.peek().status();
		if (ss == SyntaxStatus.IN_PROGRAM) {
			return true;
		}
		return false;
	}

	/**
	 * 言語ルールスタックから先頭のIN_PROGRAMの要素を返す
	 * @return ParseSyntaxRuleインスタンス
	 */
	protected ParseSyntaxRule peekStatusAsProgramRule() {
		if (this.isInProgramRule()) {
			return this.ruleStack.peek();
		}
		System.out.println("![WARN] peek failed, because it is not IN_PROGRAM, but "
				+ this.getSyntaxStatusOfPeekingStack());
		return null;
	}

	/**
	 * 言語ルールスタックから先頭のIN_PROGRAMの要素を除く
	 */
	protected void popStatusAsProgramRule() {
		if (this.isInProgramRule()) {
			this.ruleStack.pop();
			return;
		}
		System.out.println("![WARN] pop failed, because it is not IN_PROGRAM, but "
				+ this.getSyntaxStatusOfPeekingStack());
	}

	/**
	 * 言語ルールにプログラムコード開始を積む
	 */
	protected void pushNewStatusAsProgramRule() {
		this.ruleStack.push(new ParseSyntaxRule(SyntaxStatus.IN_PROGRAM));
	}

	/**
	 * プログラムコードのリテラルを処理中かチェック
	 * @return リテラル処理中ならtrue
	 */
	protected boolean isInLiteralRule() {
		SyntaxStatus ss = this.ruleStack.peek().status();
		if (ss == SyntaxStatus.IN_LITERAL) {
			return true;
		}
		return false;
	}

	/**
	 * 言語ルールスタックから先頭のIN_LITERALの要素を返す
	 * @return ParseSyntaxRuleインスタンス
	 */
	protected ParseSyntaxRule peekStatusAsLiteralRule() {
		if (this.isInLiteralRule()) {
			return this.ruleStack.peek();
		}
		System.out.println("![WARN] peek failed, because it is not IN_LITERAL, but "
				+ this.getSyntaxStatusOfPeekingStack());
		return null;
	}

	/**
	 * 言語ルールスタックから先頭のIN_LITERALの要素を除く
	 */
	protected void popStatusAsLiteralRule() {
		if (this.isInLiteralRule()) {
			this.ruleStack.pop();
			return;
		}
		System.out.println("![WARN] pop failed, because it is not IN_LITERAL, but "
				+ this.getSyntaxStatusOfPeekingStack());
	}

	/**
	 * 言語ルールスタックにリテラル文字列開始を積む
	 * @param literal リテラル文字列定義インスタンス
	 */
	protected void pushNewStatusAsLiteralRuleWith(final LiteralString literal) {
		this.ruleStack.push(new ParseSyntaxRule(SyntaxStatus.IN_LITERAL, literal));
	}

	/**
	 * プログラムコードのコメントを処理中かチェック
	 * @return コメント処理中ならtrue
	 */
	protected boolean isInCommentRule() {
		SyntaxStatus ss = this.ruleStack.peek().status();
		if (ss == SyntaxStatus.IN_COMMENT) {
			return true;
		}
		return false;
	}

	/**
	 * 言語ルールスタックから先頭のIN_COMMENTの要素を返す
	 * @return ParseSyntaxRuleインスタンス
	 */
	protected ParseSyntaxRule peekStatusAsCommentRule() {
		if (this.isInCommentRule()) {
			return this.ruleStack.peek();
		}
		System.out.println("![WARN] peek failed, because it is not IN_COMMENT, but "
				+ this.getSyntaxStatusOfPeekingStack());
		return null;
	}

	/**
	 * 言語ルールスタックから先頭のIN_COMMENTの要素を除く
	 */
	protected void popStatusAsCommentRule() {
		if (this.isInCommentRule()) {
			this.ruleStack.pop();
			return;
		}
		System.out.println("![WARN] pop failed, because it is not IN_COMMENT, but "
				+ this.getSyntaxStatusOfPeekingStack());
	}

	/**
	 * プログラム解析スタックにブロックコメント開始を積む
	 * @param comment ブロックコメント定義インスタンス
	 */
	protected void pushNewStatusAsCommentRuleWith(final AreaComment comment) {
		this.ruleStack.push(new ParseSyntaxRule(SyntaxStatus.IN_COMMENT, comment));
	}

	/**
	 * 関数型プログラムコードのコメント式を処理中かチェック
	 * @return コメント式処理中ならtrue
	 */
	protected boolean isInCommentExpressionRule() {
		SyntaxStatus ss = this.ruleStack.peek().status();
		if (ss == SyntaxStatus.IN_COMMENT_EXPR) {
			return true;
		}
		return false;
	}

	/**
	 * 関数型プログラムコードのコメント式を処理中かチェック
	 * コメント式内にリテラルやコメントもあるのでスタックの
	 * 先頭だけでなく中にある場合もチェックする
	 * @return コメント式処理中ならtrue
	 */
	protected boolean isInsideOfCommentExpressionRule() {
		Iterator<ParseSyntaxRule> it = this.ruleStack.iterator();
		while (it.hasNext()) {
			SyntaxStatus ss = it.next().status();
			if (ss == SyntaxStatus.IN_COMMENT_EXPR) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 言語ルールスタックから先頭のIN_COMMENT_EXPRの要素を返す
	 * @return ParseSyntaxRuleインスタンス
	 */
	protected ParseSyntaxRule peekStatusAsCommentExpressionRule() {
		if (this.isInCommentExpressionRule()) {
			return this.ruleStack.peek();
		}
		System.out.println("![WARN] peek failed, because it is not IN_COMMENT_EXPR, but "
				+ this.getSyntaxStatusOfPeekingStack());
		return null;
	}

	/**
	 * 言語ルールスタックから先頭のIN_COMMENT_EXPRの要素を除く
	 */
	protected void popStatusAsCommentExpressionRule() {
		if (this.isInCommentExpressionRule()) {
			this.ruleStack.pop();
			return;
		}
		System.out.println("![WARN] pop failed, because it is not IN_COMMENT_EXPR, but "
				+ this.getSyntaxStatusOfPeekingStack());
	}

	/**
	 * プログラム解析スタックにコメント式開始を積む
	 * @param expr コメント式定義インスタンス
	 */
	protected void pushNewStatusAsCommentExpressionRuleWith(final CommentExpr expr) {
		this.ruleStack.push(new ParseSyntaxRule(SyntaxStatus.IN_COMMENT_EXPR, expr));
	}

	/**
	 * 言語ルールを処理中かチェック
	 * @return 処理中ならtrue
	 */
	protected boolean isInSomeRuleOfProgram() {
		ParseSyntaxRule obj = this.ruleStack.peek();
		if (obj != null) {
			return true;
		}
		// Not in ProgramCode.
		return false;
	}

	/**
	 * 言語ルールスタックから先頭の要素を返す
	 * @return ParseSyntaxRuleインスタンス
	 */
	protected ParseSyntaxRule peekStatusAsSomeRule() {
		ParseSyntaxRule obj = this.ruleStack.peek();
		if (obj != null) {
			return obj;
		}
		System.out.println("![WARN] peek failed, because stack is empty.");
		return null;
	}

	/**
	 * 言語ルールスタックの先頭要素のSyntaxStatusを返す
	 * @return SyntaxStatus値
	 */
	private SyntaxStatus getSyntaxStatusOfPeekingStack() {
		ParseSyntaxRule obj = this.peekStatusAsSomeRule();
		if (obj == null) {
			return null;
		}
		return obj.status();
	}

	/**
	 * カテゴリ指定にマッチすればカテゴリ名を返す
	 * @param line 解析対象のソース１行（行頭から行末まで）の文字列
	 * @return カテゴリ指定があればカテゴリ名を返す。なければnull。
	 */
	protected String findCategory(final String line) {
		Matcher matcher = CATEGORY_PATTERN.matcher(line);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * 空行かどうかをチェック
	 * 空白文字（スペースやタブ）以外の文字がない行は空行
	 * @param line 解析対象のソース１行（行頭から行末まで）の文字列
	 * @return 空行であればtrue
	 */
	protected boolean checkBlancLine(final String line) {
		String trimedLine = line.trim();
		if (trimedLine.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * スキップパターンにマッチするかチェック
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line 解析対象のソース１行（行頭から行末まで）の文字列
	 * @return スキップパターンにマッチすればtrue
	 */
	protected boolean checkSkipLine(final ProgramLangRule lang, final String line) {
		if (lang == null) {
			return false;
		}
		for (int i = 0; i < lang.skipPatterns().size(); i++) {
			if (lang.skipPatterns().get(i).matcher(line).find()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 行からコメントを除去する処理
	 * @param line 解析対象のソース１行（行頭から行末まで）の文字列
	 * @return lineからコメントを取り除いた文字列
	 */
	protected String removeComments(final String line) {
		return this.removeCommentFromLeft(this.currentLang(), line, 0);
	}

	/* インナークラス */
	/* 解析対象記号の対象行内での位置、長さ、記号などを保持する */
	private static class ParseInfo {
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


		private void setLineComment(final int pos, final int len, final LineComment linecomm) {
			this.lineComment.position = pos;
			this.lineComment.length = len;
			if (linecomm != null) {
				this.lineComment.element = (Object) linecomm;
			}
		}

		private void setAreaComment(final int pos, final int len, final AreaComment area) {
			this.areaComment.position = pos;
			this.areaComment.length = len;
			if (area != null) {
				this.areaComment.element = (Object) area;
			}
		}

		private void setLiteralString(final int pos, final int len, final LiteralString literal) {
			this.literalString.position = pos;
			this.literalString.length = len;
			if (literal != null) {
				this.literalString.element = (Object) literal;
			}
		}

		private void setScriptBlock(final int pos, final int len, final ScriptBlock script) {
			this.scriptBlock.position = pos;
			this.scriptBlock.length = len;
			if (script != null) {
				this.scriptBlock.element = (Object) script;
			}
		}

		/* 最左の解析対象を返す */
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
				//System.out.println("[DEBUG] Find LineComment start["+lcPosMin+"]:" + ss);
				return this.lineComment;
			} else if (acPosMin >= 0) {
				//String ss = ((AreaComment)(this.areaComment.element)).getStartString();
				//System.out.println("[DEBUG] Find AreaComment start["+acPosMin+"]:" + ss);
				return this.areaComment;
			} else if (lsPosMin >= 0) {
				//String ss = ((LiteralString)(this.literalString.element)).getStartString();
				//System.out.println("[DEBUG] Find LiteralString start["+lsPosMin+"]:" + ss);
				return this.literalString;
			} else if (sbPosMin >= 0) {
				//String ss = ((ScriptBlock)(this.scriptBlock.element)).getEndString();
				//System.out.println("[DEBUG] Find ScriptBlock end["+sbPosMin+"]:" + ss);
				return this.scriptBlock;
			} else {
				return null;
			}
		}
	}

	/**
	 * 最左に現れる解析対象記号を探す
	 * @param rule 現在の解析対象のプログラミング言語インスタンス
	 * @param line これから解析する行末までを含む文字列
	 * @param head lineが行頭を含んでいればtrue
	 * @return 解析結果インスタンス
	 */
	protected ParseInfo searchMostLeftTarget(
			final ProgramLangRule rule, final String line, final boolean head) {
		// コメント記述用
		int lcidx = -1;
		int acidx = -1;
		int lcPosMin = -1;
		int acPosMin = -1;
		int lclen = 0;
		int aclen = 0;
		// リテラル文字列記述用
		int lsidx = -1;
		int lsPosMin = -1;
		int lslen = 0;
		// Script記述用
		int sbPosMin = -1;
		int sblen = 0;

		// 単一行コメント記号をチェック
		for (int i = 0; i < rule.lineComments().size(); i++) {
			LineComment linecomm = rule.lineComments().get(i);
			int pos = linecomm.searchStartingMark(line, head, rule.isCaseInsense());
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
		for (int i = 0; i < rule.areaComments().size(); i++) {
			AreaComment area = rule.areaComments().get(i);
			int pos = area.searchStartingMark(line, head, rule.isCaseInsense());
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
		for (int i = 0; i < rule.literalStrings().size(); i++) {
			LiteralString literal = rule.literalStrings().get(i);
			int pos = literal.searchStartingMark(line);
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
		ScriptBlock block = null;
		if (this.isInScriptletCode()) {
			block = this.codeStack.peek().scriptBlock();
			int pos = block.searchEndingMark(line);
			if (pos >= 0) {
				String end = block.getEndString();
				sbPosMin = pos;
				sblen = end.length();
			}
		}
		// 各種類ごとの探索結果の保管と最左対象の決定
		EachLeftTarget each = new EachLeftTarget();
		if (lcPosMin >= 0) {
			each.setLineComment(lcPosMin, lclen, rule.lineComments().get(lcidx));
		}
		if (acPosMin >= 0) {
			each.setAreaComment(acPosMin, aclen, rule.areaComments().get(acidx));
		}
		if (lsPosMin >= 0) {
			each.setLiteralString(lsPosMin, lslen, rule.literalStrings().get(lsidx));
		}
		if (sbPosMin >= 0) {
			each.setScriptBlock(sbPosMin, sblen, block);
		}
		return each.getMostLeftTarget();
	}

	/**
	 * 先に現れるコメント記号から除去処理を再帰的に繰り返す
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line 解析対象のソース１行の処理未済の行末までの部分文字列
	 * @param recursion 解析対象１行に対して再帰的な呼び出し回数（０なら初めて）
	 * @return lineから最も左にあるコメントを取り除いた文字列
	 */
	protected String removeCommentFromLeft(
			final ProgramLangRule lang, final String line, final int recursion) {
		if (line == null || line.isEmpty()) {
			return "";
		}

		// 行の最左の解析対象記号を探す
		boolean head;
		if (recursion == 0) {
			head = true;
		} else {
			head = false;
		}
		ParseInfo pi = this.searchMostLeftTarget(lang, line, head);

		// 最左に選ばれたものを処理する
		if (pi == null) {
			// 解析対象のコメント記号・リテラル記号・スクリプト終了記号はなし
			return this.dealValidCode(lang, line);

		} else if (pi.element instanceof LineComment) {
			// 最左は単一行コメント記号、行末までコメント
			//System.out.println("[DEBUG] Find (Line Comment):"
			//		+ ((LineComment) pi.element).getStartString());
			return this.dealValidCode(lang, line.substring(0, pi.position));

		} else if (pi.element instanceof AreaComment) {
			// 最左は複数行コメント記号
			AreaComment area = (AreaComment) pi.element;
			// 複数行コメント開始
			//System.out.println("Find (Area Comment):" + area.getStartString());
			StringBuilder sb = new StringBuilder();
			if (pi.position > 0) {
				// コメントの左側は有効行なので返却対象
				sb.append(this.dealValidCode(lang, line.substring(0, pi.position)));
			}
			// コメント開始記号の左端から右の処理
			this.pushNewStatusAsCommentRuleWith(area);
			sb.append(this.dealAreaCommentStart(lang, line.substring(pi.position), area));
			return sb.toString();

		} else if (pi.element instanceof LiteralString) {
			// 最左はリテラル文字列記号
			LiteralString literal = (LiteralString) pi.element;
			StringBuilder sb = new StringBuilder();
			// Rubyのヒアドキュメント開始記号に除外ケースあり、dealLiteralStringStart()
			// の中では判定できないため、ここでチェック
			if (this.checkExcludeLiteralStringStart(line, literal)) {
				// 除外ケース
				String start = literal.getStartString();
				int pos = pi.position + start.length();
				sb.append(line.substring(0, pos));
				sb.append(this.removeCommentFromLeft(lang, line.substring(pos), recursion + 1));
				return sb.toString();
			}
			// リテラル文字列開始
			// １つ前に処理中のHereDocがないかを確認する
			ParseSyntaxRule rule = this.peekStatusAsSomeRule();
			if (rule != null) {
				AbstractBlock obj = rule.subject();
				if (obj != null && obj instanceof LabelHereDoc) {
					// Rubyなど１行でHereDocを複数記述したケース
					// 最後のラベルだけの１つのHereDocとみなして処理する
					this.popStatusAsLiteralRule();
				}
			}
			//System.out.println("[DEBUG] Find (Literal String):" + literal.getStartString());
			// 開始記号含まない左側の有効行を処理
			sb.append(this.dealValidCode(lang, line.substring(0, pi.position)));
			// 開始記号含む右のリテラルの処理
			this.pushNewStatusAsLiteralRuleWith(literal);
			sb.append(this.dealLiteralStringStart(lang, line.substring(pi.position), literal));
			return sb.toString();

		} else if (pi.element instanceof ScriptBlock) {
			// 最左はScript終了記号
			ScriptBlock block = (ScriptBlock) pi.element;
			//System.out.println("[DEBUG] Find (Script End):" + block.getEndString());
			StringBuilder sb = new StringBuilder();
			// 終了記号を含まない有効行を処理
			sb.append(this.dealValidCode(lang, line.substring(0, pi.position)));
			// 終了記号を含む右側の外部リテラルを処理
			this.setCurrentLang(null);
			this.popStatusAsScriptletCode();
			this.popStatusAsProgramRule();
			sb.append(this.dealScriptBlockEnd(lang, line.substring(pi.position), block));
			return sb.toString();

		} else {
			// ここには到達しないはず
			System.out.println("![WARN] Unknown ParseInfo. : " + line);
			return this.dealValidCode(lang, line);
		}
	}

	/**
	 * 複数行ブロックコメントの開始から行末までの処理をする
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line コメントの開始記号から行末までを含む文字列
	 * @param area 対象のブロックコメント定義インスタンス
	 * @return lineから先頭にあるブロックコメント範囲を取り除いた文字列
	 */
	protected String dealAreaCommentStart(
			final ProgramLangRule lang, final String line, final AreaComment area) {
		area.addNest();
		// 開始記号末尾の位置
		String start = area.getStartString();
		int pos = start.length();
		// 開始記号より右のコメントの処理
		return this.removeAreaCommentUntilEnd(lang, line.substring(pos), LINE_NO_HEAD);
	}

	/**
	 * リテラル文字列の開始から行末までの処理をする
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line リテラルの開始記号から行末までを含む文字列
	 * @param literal 対象のリテラル文字列定義インスタンス
	 * @return lineからリテラル文字列を取り除いた文字列
	 */
	protected String dealLiteralStringStart(
			final ProgramLangRule lang, final String line, final LiteralString literal) {
		// 通常の引用符リテラル
		String start = literal.getStartString();
		StringBuilder sb = new StringBuilder();
		// 開始記号を処理
		int pos = start.length();
		sb.append(this.dealValidCode(lang, line.substring(0, pos), HAVING_ONLY_LITERAL));
		// 右側を処理
		sb.append(this.searchLiteralStringEnd(lang, line.substring(pos)));
		return sb.toString();
	}

	/**
	 * Scriptlet終了記号の開始から行末までの処理をする
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line Scriptlet終了記号から行末までを含む文字列
	 * @param block 対象のScriptletブロック定義インスタンス
	 * @return lineからScriptletブロック外部を取り除いた文字列
	 */
	protected String dealScriptBlockEnd(
			final ProgramLangRule lang, final  String line, final ScriptBlock block) {
		// 通常の引用符リテラル
		String end = block.getEndString();
		StringBuilder sb = new StringBuilder();
		// 開始記号を処理
		int pos = end.length();
		sb.append(this.dealValidCode(lang, line.substring(0, pos), HAVING_ONLY_LITERAL));
		// 右側を処理
		sb.append(this.searchScriptStart(line.substring(pos)));
		return sb.toString();
	}

	/**
	 * 複数行コメントが終了しているかチェックし、コメント部分除き有効な文字列を返す
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line 先頭がコメント内部で行末までを含む文字列
	 * @param head lineが行頭を含んでいればtrue
	 * @return lineが対象コメント外部のコードを含んでいればコードからコメントを除いた文字列
	 */
	protected String removeAreaCommentUntilEnd(
			final ProgramLangRule lang, final String line, final boolean head) {
		AreaComment area = (AreaComment) this.peekStatusAsCommentRule().subject();
		int pos = area.searchEndingMark(line, head, lang.isCaseInsense());
		if (pos >= 0) {
			// コメント終了があった
			this.popStatusAsCommentRule();
			// コメント終了記号の右側のコードの解析処理
			if (pos < line.length()) {
				return this.removeCommentFromLeft(lang, line.substring(pos), 1);
			}
		}
		return "";
	}

	/**
	 * リテラル文字列が終了しているかチェックし、有効な文字列を返す
	 * @param lang 現在の解析対象のプログラミング言語インスタンス
	 * @param line 先頭がリテラル内部で行末までを含む文字列
	 * @return lineがリテラル外部のコードを含んでいればコードからコメントを除いた文字列
	 */
	protected String searchLiteralStringEnd(final ProgramLangRule lang, final String line) {
		LiteralString literal = (LiteralString) this.peekStatusAsLiteralRule().subject();
		int pos = literal.searchEndingMark(line);
		if (pos >= 0) {
			// リテラル終了
			//System.out.println("[DEBUG] literal end: " + literal.getEndString());
			this.popStatusAsLiteralRule();
			// リテラルの内容を返す
			StringBuilder sb = new StringBuilder();
			sb.append(this.dealValidCode(lang, line.substring(0, pos), HAVING_ONLY_LITERAL));
			// リテラル終了後の右側のコードを解析して返す
			if (pos < line.length()) {
				sb.append(this.removeCommentFromLeft(lang, line.substring(pos), 1));
			}
			//dealProgramCode処理後の内容なのでそのまま返す
			return sb.toString();
		}
		// 文字列終了していないので、全てリテラル
		return this.dealValidCode(lang, line, HAVING_ONLY_LITERAL);
	}

	/**
	 * Scriptlet開始記号が含まれるかどうかをチェックし、有効な文字列を返す
	 * @param line 先頭がScriptlet外部で行末までを含む文字列
	 * @return lineにScriptletが含まれていればその内部からコメントを除いた文字列
	 */
	protected String searchScriptStart(final String line) {
		int asidx = -1;
		int asPosMin = -1;
		int aslen = 0;

		// Script記号をチェック
		for (int i = 0; i < this.scriptBlocks().size(); i++) {
			ScriptBlock block = this.scriptBlocks().get(i);
			int pos = block.searchStartingMark(line);
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
		ScriptBlock block = this.scriptBlocks().get(asidx);
		this.setCurrentLang((ProgramLangRule) this);
		this.pushNewStatusAsScriptletCodeWith(block, this.currentLang());
		this.pushNewStatusAsProgramRule();
		String target = null;
		// Script記号の左側はScript外部なのでそのまま返却
		// Script記号自体はScript内部として再度解析対象にする。理由："<%"に対する"<%--"
		if (asPosMin > 0) {
			sb.append(line.substring(0, asPosMin));
			target = line.substring(asPosMin);
		} else {
			target = line;
		}
		sb.append(this.removeCommentFromLeft(this.currentLang(), target, 1));
		return sb.toString();
	}

	/**
	 * ソースコードの有効行部分を処理する
	 * @param lang 処理中のプログラム言語ルール　　　
	 * @param line 処理対象行でコメントは抜かれたソースコードのみの文字列
	 * @return 引数lineからコメント式範囲を取り除いたソースコード文字列
	 */
	protected String dealValidCode(final ProgramLangRule lang, final String line) {
		// lineの内容はコメントのないソースコードの一部
		return  dealValidCode(lang, line, HAVING_VALID_STATEMENT);
	}

	/**
	 * FuctionalLang(Scheme, Clojure)でOverrideする有効行処理メソッド
	 * @param lang 処理中のプログラム言語ルール　　　
	 * @param line 処理対象行でコメントは抜かれたソースコードのみの文字列
	 * @param validCodeFlag 有効行コードが含まれるか示すフラグ（falseは文字列リテラルのみの場合）
	 * @return 引数lineからコメント式範囲を取り除いたソースコード文字列
	 */
	protected String dealValidCode(final ProgramLangRule lang, final String line, final boolean validCodeFlag) {
		// lineの内容はコメントのないソースコードの一部
		// 通常言語はvalidCodeFlagの値によらずそのまま全てを返す
		return line;
	}

	/**
	 * リテラル文字列の開始記号に一致するが、直前の内容で除外が必要なケースを
	 * 判定する
	 *  RubyでOverrideするチェックメソッド
	 *  @param line 処理対象の行文字列
	 *  @param literal 開始記号を保持するLiteralStringのインスタンス
	 *  @return 判定結果のbool値
	 */
	protected boolean checkExcludeLiteralStringStart(final String line, final LiteralString literal) {
		//　通常は除外必要なケースないので固定でfalse
		return false;
	}

	/**
	 * テキスト１行分の文字列の左端のインデントに含まれるタブのインデント分を
	 * スペースに置換した文字列に変換する。
	 * タブのインデントは半角8文字毎の位置に揃えられるものと定義する
	 * @param line テキスト１行の文字列
	 * @return 引数lineの左端のインデント中のタブをスペースに置換した文字列
	 */
	protected String saveIndent(final String line) {
		if (line == null) {
	        return null;
	    }
	    char[] val = line.toCharArray();
	    int idx = 0;
	    int ind = 0;
	    int len = val.length;

	    while (idx < len) {
	        if (val[idx] == ' ') {
	        	ind++;
	        	idx++;
	        } else if (val[idx] == '\t') {
	        	int tabpad = ind % TABWIDTH;
	        	ind += TABWIDTH - tabpad;
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

