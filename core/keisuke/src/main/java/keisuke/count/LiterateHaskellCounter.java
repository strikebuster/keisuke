package keisuke.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.diffcount.DiffSource;

/**
 * keisuke:追加クラス
 * 	Literate Haskell用のステップカウンタ
 */
public class LiterateHaskellCounter extends DefaultStepCounter {

		private static String BIRDMARK = ">";
		
	/**
	 * コンストラクター
	 *
	 */
	public LiterateHaskellCounter() {
		scriptletFlag = true;
		scriptLangs = new ArrayList<ProgramLangRule>();
		scriptBlocks = new ArrayList<ScriptBlock>();
		addLineComment(new LineComment("--"));
		addAreaComment(new AreaComment("{-","-}",AreaComment.ALLOW_NEST));
		addLiteralString(new LiteralString("\"", "\""));
		addScriptBlock(new ScriptBlock("\\begin{code}", "\\end{code}"));
		setIndentOptTrue();
		setFileType("Haskell");
	}
	
	@Override
	public CountAndCutSource countOrCut(int ope, File file, String charset) throws IOException {
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
			boolean programFlag = false;
			boolean birdmode = false;
	
			//System.out.println("[DEBUG] ### Processing file=" + file.getPath() + " ###"); 
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

				if (programFlag && currentLang != null && currentLang.indentOpt){
					indent = saveIndent(line); // インデント保持
				} else {
					indent = "";
				}
				String focusedLine = line.trim();
				if (checkBlancLine(focusedLine) || checkSkipLine(currentLang, focusedLine)){
					// 空白行またはスキップ対象行なので飛ばす
					non++;
					continue;
				}
				birdmode = false;
				if (programFlag == false && scriptletFlag) {
					// Scriptletの外部
					// リテラルかBIRDモードのコードか判定
					if (line.startsWith(BIRDMARK)) {
						// BIRDモードのソースコード
						birdmode = true;
						programFlag = true;
						currentLang = (ProgramLangRule)this;
						// インデント保持、空白行チェックのやり直し
						focusedLine = line.substring(BIRDMARK.length());
						if (currentLang.indentOpt){
							indent = saveIndent(focusedLine); // インデント保持
						}
						focusedLine = focusedLine.trim();
						//System.out.println("[DEBUG] :["+indent+"]"+focusedLine);
						if (checkBlancLine(focusedLine)) {
							// 空白行またはスキップ対象行なので飛ばす
							non++;
							continue;
						}
					}
				}
				if (areaFlag == false && literalFlag == false && programFlag == true) {
					// 有効行の処理
					focusedLine = removeComments(focusedLine).trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							if (birdmode) {
								sb.append(BIRDMARK);
							}
							sb.append(indent + focusedLine + "\n");
						}
						//System.out.println("[DEBUG] !"+focusedLine);
					} else {
						// 取り除かれたコメントが含まれていた
						comment++;
					}
					if ( onScriptBlock == null && scriptletFlag ) {
						programFlag = false;
					} else if ( onAreaComment != null ) {
						areaFlag = true;
					} else if ( onLiteralString != null ) {
						literalFlag = true;
					}
				} else if (areaFlag) {
					// 複数行コメントの内部
					focusedLine = removeAreaCommentUntillEnd(currentLang, focusedLine, LINE_HEAD).trim();
					if ( onAreaComment == null ) {
						areaFlag = false;
					}
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							if (birdmode) {
								sb.append(BIRDMARK);
							}
							sb.append(indent + focusedLine + "\n");
						}
						//System.out.println("[DEBUG] @"+focusedLine);
					} else {
						// 取り除かれたコメントが含まれていた
						comment++;
					}
					if ( onScriptBlock == null && scriptletFlag ) {
						programFlag = false;
					} else if ( onAreaComment != null ) {
						areaFlag = true;
					} else if ( onLiteralString != null ) {
						literalFlag = true;
					}
				} else if (literalFlag) {
					// リテラル文字列の内部
					focusedLine = searchLiteralStringEnd(currentLang, focusedLine).trim();
					if ( onLiteralString == null ) {
						literalFlag = false;
					}
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							if (birdmode) {
								sb.append(BIRDMARK);
							}
							sb.append(indent + focusedLine + "\n");
						}
						//System.out.println("[DEBUG] $"+focusedLine);
					} else {
						// リテラル内の空行(ここは到達不能なはず）
						non++;
					}
					if ( onScriptBlock == null && scriptletFlag ) {
						programFlag = false;
					} else if ( onAreaComment != null ) {
						areaFlag = true;
					} else if ( onLiteralString != null ) {
						literalFlag = true;
					}					
				} else if (programFlag == false && scriptletFlag) {
					// Scriptletの外部、BIRDモードでもない
					focusedLine = searchScriptStart(focusedLine).trim();
					if ( onScriptBlock != null && scriptletFlag ) {
						programFlag = true;
					}
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						step++;
						if (ope == OPE_CUT) {
							sb.append(indent + focusedLine + "\n");
						}
						//System.out.print("[DEBUG] &"+focusedLine+"\n");
					}
					if ( onScriptBlock == null && scriptletFlag) {
						programFlag = false;
					} else if ( onAreaComment != null ) {
						areaFlag = true;
					} else if ( onLiteralString != null ) {
						literalFlag = true;
					}
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
}
