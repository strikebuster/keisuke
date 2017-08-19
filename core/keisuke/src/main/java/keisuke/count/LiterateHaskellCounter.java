package keisuke.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 	Literate Haskell用のステップカウンタ
 */
public class LiterateHaskellCounter extends DefaultStepCounter {

	private static final String BIRDMARK = ">";

	/**
	 * コンストラクター
	 *
	 */
	public LiterateHaskellCounter() {
		super(true);
		this.addLineComment(new LineComment("--"));
		this.addAreaComment(new AreaComment("{-", "-}", AreaComment.ALLOW_NEST));
		this.addLiteralString(new LiteralString("\"", "\""));
		this.addScriptBlock(new ScriptBlock("\\begin{code}", "\\end{code}"));
		this.setUsingIndentBlock(true);
		this.setFileType("Haskell");
		this.setCurrentLang((ProgramLangRule) this);
	}

	/**
	 * カウントまたは有効行以外をカットします。
	 * @param ope ステップカウントかコメント除去かの指定値
	 * @param file カウント対象のファイル
	 * @param charset ファイルのエンコード
	 * @return 引数opeで指定した処理の結果を保持するインスタンス
	 * @throws IOException ファイル読み取りで異常があれば発行
	 */
	@Override
	protected CountAndCutSource countOrCut(
			final int ope, final File file, final String charset) throws IOException {

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
			boolean birdmode = false;

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
					// リテラルかBIRDモードのコードか判定
					if (line.startsWith(BIRDMARK)) {
						// BIRDモードのソースコード
						birdmode = true;
						this.setCurrentLang((ProgramLangRule) this);
						this.pushNewStatusAsScriptletCodeWith(
								new BirdMode(), this.currentLang());
						if (!this.isInSomeRuleOfProgram()) { // 最初のBirdモード
							this.pushNewStatusAsProgramRule();
						}
						// インデント保持、空白行チェックのやり直し
						focusedLine = line.substring(BIRDMARK.length());
						if (this.currentLang().isUsingIndentBlock()) {
							indent = this.saveIndent(focusedLine); // インデント保持
						}
						focusedLine = focusedLine.trim();
						//System.out.println("[DEBUG] :[" + indent + "]" + focusedLine);
						if (this.checkBlancLine(focusedLine)) {
							// 空白行またはスキップ対象行なので飛ばす
							ccsObj.increaseBlancStep();
						}
					} else {
						// BIRDモードではない
						focusedLine = this.searchScriptStart(focusedLine).trim();
						if (focusedLine.length() > 0) {
							// 有効行が残っている
							ccsObj.appendExecutableStep(indent + focusedLine + "\n");
						} else {
							// ここに来ることはないはず
							System.out.println("![WARN] illegal status in line=" + line);
						}
						continue;
					}
				}

				// Programコード(Scriptlet)の内部
				if (this.isInProgramRule()) {
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
					focusedLine = this.searchLiteralStringEnd(this.currentLang(), focusedLine)
							.trim();
					if (focusedLine.length() > 0) {
						// 有効行が残っている
						ccsObj.appendExecutableStep(indent + focusedLine + "\n");
					} else {
						// リテラル内の空行(ここは到達不能なはず）
						System.out.println("![WARN] blanc in literal. file=" + file.getName()
								+ " line=" + line);
						ccsObj.increaseBlancStep();
					}
				} else {
					// Scriptlet言語でないのにプログラム外部？？
					System.out.println("![WARN] Unknown the status of this line. : file="
							+ file.getPath() + " line=" + line);
					ccsObj.appendExecutableStep(indent + focusedLine + "\n");
				}
				// BIRDモードの場合の行後処理
				if (birdmode) {
					birdmode = false;
					this.popStatusAsScriptletCode();
					if (this.isInProgramRule()) { // コメントやリテラル処理中ではない
						this.popStatusAsProgramRule();
					}
				}
			}
		} finally {
			reader.close();
		}
		ccsObj.createResult(file, this.getFileType(), category);
		return ccsObj;
	}

	private static class BirdMode extends ScriptBlock {
		protected BirdMode() {
			super(">", "\n");
		}
	}
}
