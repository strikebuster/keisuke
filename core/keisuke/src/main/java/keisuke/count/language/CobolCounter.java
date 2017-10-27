package keisuke.count.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import keisuke.count.syntax.LineComment;
import keisuke.count.syntax.LiteralString;
import keisuke.util.LogUtil;

/**
 * 	Cobol用のステップカウンタ
 */
public class CobolCounter extends GeneralStepCounter {

	// 固定形式用定義
	private static final int INDICATOR_COLUMN = 7;
	// 固定形式コメント（7カラム目に特定文字）
	private List<String> fixLineComments = Arrays.asList("*", "/", "D", "$");
	// MicroFocus独自の固定形式コメント（1カラム目に*）
	private List<String> fixMFLineComments = Arrays.asList("*");

	// 自由形式用定義
	// 自由形式コメント
	private List<String> freeLineComments = Arrays.asList("*>", "^>>D ");
	// MicroFocus独自の自由形式コメント（左端の1文字に特定文字）
	private List<String> freeMFLineComments = Arrays.asList("*", "/", "D ", "$");

	// 共通定義
	// COBOLの文字列リテラル引用符
	private LiteralString cobolLiteralString = new LiteralString("'", "'", "''");
	// IBM独自の文字列リテラル引用符
	private LiteralString ibmCobolLiteralString = new LiteralString("\"", "\"", "\"\"");
	// 形式判定で利用するCOBOL文
	private static final String COBOLFIXSTYLE = ">>SOURCE FORMAT IS FIXED";
	private static final String COBOLFREESTYLE = ">>SOURCE FORMAT IS FREE";
	private static final String COBOLFIRSTTOKEN = "IDENTIFICATION";

	// 指定された形式を保持
	private CobolStyle formatStyle;

	/**
	 * コンストラクター
	 */
	public CobolCounter() {
		super();
		this.setFileType("COBOL");
		for (String ss : freeLineComments) {
			this.addLineComment(new LineComment(ss));
		}
		this.addLiteralString(cobolLiteralString);
		this.addLiteralString(ibmCobolLiteralString);
		this.setCaseInsense(true);
		this.setUnknownStyle();
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
	public OperationResult countOrCut(final OperationType ope, final File file,
			final String charset) throws IOException {

		String charSetName = charset;
		if (charSetName == null) {
			// キャラクタセット無指定の場合は
			// プラットフォームデフォルトキャラクタセットを指定する。
			LogUtil.warningLog("charset is null");
			charSetName = Charset.defaultCharset().name();
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), charSetName));

		String category = "";
		// リターン値格納変数3種類
		// 正式なカウンタ変数
		OperationResult opeResult = new OperationResult(ope, this.getFileType());
		// 仮の固定形式カウンタ変数
		OperationResult opeResultFixed = new OperationResult(ope, this.getFileType());
		// 仮の自由形式カウンタ変数
		OperationResult opeResultFree = new OperationResult(ope, this.getFileType());
		// リターン値変数格納配列
		OperationResult[] opeResultArray = {opeResult, opeResultFixed, opeResultFree};

		try {
			String line     = null;

			//LogUtil.debugLog("### Processing file=" + file.getPath() + " ###");
			while ((line = reader.readLine()) != null) {
				if (category.length() == 0) {
					String str = this.findCategory(line);
					if (str != null) {
						category = str;
					}
				}
				if (IGNORE_PATTERN.matcher(line).find()) {
					opeResult.createIgnoredResult(category);
					return opeResult;
				}
				// styleが判定できるまで両方の形式でカウントし、判定後はいずれかのみでカウント
				if (!this.isFreeStyle()) {
					// 自由形式ではない固定形式と想定したカウント
					this.dealLineAsFixedStyle(line, opeResultArray);
					if (this.isFreeStyle()) { // 自由形式への切り替え宣言あり
						continue;
					}
				}
				if (!this.isFixedStyle()) {
					// 固定形式ではない自由形式と想定したカウント
					this.dealLineAsFreeStyle(line, opeResultArray);
					if (this.isFixedStyle()) { // 固定形式への切り替え宣言あり
						continue;
					}
				}
			} // end of while
		} finally {
			reader.close();
		}
		if (this.isUnknownStyle()) {
			// 形式不明のまま終了した場合（登録集ファイルなど）固定形式とみなす
			opeResult.copyFrom(opeResultFixed);
		}
		opeResult.createResult(file, this.getFileType(), category);
		return opeResult;
	}

	/* 固定形式と想定したカウント */
	private void dealLineAsFixedStyle(final String line, final OperationResult[] ccsArray) {
		OperationResult ccsObj = ccsArray[0];
		OperationResult ccsObjFixed = ccsArray[1];
		OperationResult ccsObjFree = ccsArray[2];

		String rtrimedLine = rtrim(line); // 右端だけトリム
		String indicatorArea = " "; // 標識領域の初期化
		String abAreaLine = ""; // A領域＋B領域の初期化
		int lineLength = rtrimedLine.length();
		if (lineLength > 0 && this.checkFixMFLineComment(rtrimedLine)) {
			// MF独自形式のコメント行
			if (this.isUnknownStyle()) {
				ccsObjFixed.increaseCommentStep();
			} else {
				ccsObj.increaseCommentStep();
			}
			//LogUtil.debugLog("MF_Comm#[" + rtrimedLine + "]");
		} else if (lineLength > INDICATOR_COLUMN - 1) {
			indicatorArea =  rtrimedLine
					.substring(INDICATOR_COLUMN - 1, INDICATOR_COLUMN).toUpperCase();
			if (lineLength > INDICATOR_COLUMN) {
				abAreaLine = rtrimedLine
						.substring(INDICATOR_COLUMN, lineLength).trim();
			}
			if (checkFixLineComment(indicatorArea)) {
				// コメント行
				if (this.isUnknownStyle()) {
					ccsObjFixed.increaseCommentStep();
				} else {
					ccsObj.increaseCommentStep();
				}
				//LogUtil.debugLog("Comment#[" + rtrimedLine + "]");
			} else {
				if (this.isUnknownStyle()) {
					ccsObjFixed.appendExecutableStep(abAreaLine + "\n");
				} else {
					ccsObj.appendExecutableStep(abAreaLine + "\n");
				}
				//LogUtil.debugLog("Exec___#[" + rtrimedLine + "]");
				// style未定なら確認する
				if (this.isUnknownStyle() && checkFirstProgramStep(abAreaLine)) {
					this.setFixedStyle();
					// 仮カウントしていた固定形式のカウンタ値を採用
					ccsObj.copyFrom(ccsObjFixed);
					// 仮カウントしていた自由形式のカウンタをリセット
					ccsObjFree.reset();
					return;
				} else if (checkFormatChangeToFree(abAreaLine)) {
					this.setFreeStyle();
					return;
				}
			}
		} else {
			if (this.isUnknownStyle()) {
				ccsObjFixed.increaseBlancStep();
			} else {
				ccsObj.increaseBlancStep();
			}
			//LogUtil.debugLog("Blanc__#[" + rtrimedLine + "]");
		}
	}

	/* 自由形式と想定したカウント */
	private void dealLineAsFreeStyle(final String line, final OperationResult[] ccsArray) {
		OperationResult ccsObj = ccsArray[0];
		OperationResult ccsObjFixed = ccsArray[1];
		OperationResult ccsObjFree = ccsArray[2];

		// MicroFocus独自記法のコメント行かチェック
		if (this.checkFreeMFLineComment(line)) {
			// MicroFocus独自記法のコメント行
			if (this.isUnknownStyle()) {
				ccsObjFree.increaseCommentStep();
			} else {
				ccsObj.increaseCommentStep();
			}
			//LogUtil.debugLog("MF_Comm![" + line + "]");
		} else {
			//　ANSI記法のコメント行かチェック
			String trimedLine = line.trim(); // 両端トリム
			if (this.checkBlancLine(trimedLine)) {
				// 空白行なので飛ばす
				if (this.isUnknownStyle()) {
					ccsObjFree.increaseBlancStep();
				} else {
					ccsObj.increaseBlancStep();
				}
				//LogUtil.debugLog("Blanc__![" + trimedLine + "]");
			} else {
				// 空白行ではない
				String focusedLine = this.removeFreeLineComment(trimedLine).trim();
				if (focusedLine.length() > 0) {
					// 有効行が残っている
					if (this.isUnknownStyle()) {
						ccsObjFree.appendExecutableStep(focusedLine + "\n");
					} else {
						ccsObj.appendExecutableStep(focusedLine + "\n");
					}
					//LogUtil.debugLog("Exec___![" + focusedLine + "]");
					// style未定なら確認する
					if (this.isUnknownStyle() && checkFirstProgramStep(focusedLine)) {
						this.setFreeStyle();
						// 仮カウントしていた自由形式のカウンタ値を採用
						ccsObj.copyFrom(ccsObjFree);
						// 仮カウントしていた固定形式のカウンタをリセット
						ccsObjFixed.reset();
						return;
					} else if (checkFormatChangeToFix(focusedLine)) {
						this.setFixedStyle();
						return;
					}
				} else {
					// コメントが含まれていた
					if (this.isUnknownStyle()) {
						ccsObjFree.increaseCommentStep();
					} else {
						ccsObj.increaseCommentStep();
					}
					//LogUtil.debugLog("Comment![" + trimedLine + "]");
				}
			}
		}
	}

	/* 固定形式　コメント行チェック */
	private boolean checkFixLineComment(final String indicator) {
		return this.fixLineComments.contains(indicator);
	}

	/* 固定形式　MicroFocus独自コメント行チェック */
	private boolean checkFixMFLineComment(final String line) {
		for (String ss : this.fixMFLineComments) {
			int lineLength = line.length();
			int sslen = ss.length();
			if (lineLength > ss.length()) {
				String head = line.substring(0, sslen);
				if (head.equals(ss)) {
					// コメント行にマッチした
					return true;
				}
			}
		}
		return false;
	}

	/* 自由形式　MicroFocus独自コメント行チェック */
	private boolean checkFreeMFLineComment(final String line) {
		for (String ss : this.freeMFLineComments) {
			int lineLength = line.length();
			int sslen = ss.length();
			if (lineLength > ss.length()) {
				// ">>D "に対し大文字化とタブはスペースに置換して比較
				String head = line.substring(0, sslen).toUpperCase().replace('\t', ' ');
				if (head.equals(ss)) {
					// コメント行にマッチした
					return true;
				}
			}
		}
		return false;
	}

	/* 自由形式　コメント除去した行文字列を抽出 */
	private String removeFreeLineComment(final String line) {
		return this.removeComments(line);
	}

	/* COBOL先頭行の判定 */
	private static boolean checkFirstProgramStep(final String line) {
		return line.toUpperCase().startsWith(COBOLFIRSTTOKEN);
	}

	/* COBOL自由形式→固定形式命令のチェック */
	private static boolean checkFormatChangeToFix(final String line) {
		return line.toUpperCase().startsWith(COBOLFIXSTYLE);
	}

	/* COBOL固定形式→自由形式命令のチェック */
	private static boolean checkFormatChangeToFree(final String line) {
		return line.toUpperCase().trim().startsWith(COBOLFREESTYLE);
	}

	/**
	 * 右端のみトリム
	 * @param str 文字列
	 * @return 右端のみトリムした文字列
	 */
	private static String rtrim(final String str) {
		if (str == null) {
	        return null;
	    }
	    char[] val = str.toCharArray();
	    int len = val.length;
	    while ((0 < len) && (val[len - 1] <= ' ')) {
	        len--;
	    }
	    if (len < val.length) {
	    	return str.substring(0, len);
	    } else {
	    	return str;
	    }
	}

	protected enum CobolStyle {
		FIXED_STYLE,
		FREE_STYLE,
		UNKNOWN_STYLE
	}

	private boolean isFixedStyle() {
		return (this.formatStyle == CobolStyle.FIXED_STYLE);
	}

	private boolean isFreeStyle() {
		return (this.formatStyle == CobolStyle.FREE_STYLE);
	}

	private boolean isUnknownStyle() {
		return (this.formatStyle == CobolStyle.UNKNOWN_STYLE);
	}

	private void setFixedStyle() {
		this.formatStyle = CobolStyle.FIXED_STYLE;
	}

	private void setFreeStyle() {
		this.formatStyle = CobolStyle.FREE_STYLE;
	}

	private void setUnknownStyle() {
		this.formatStyle = CobolStyle.UNKNOWN_STYLE;
	}
}
