package keisuke.count.format;

import keisuke.StepCountResult;
import keisuke.util.StringUtil;

import static keisuke.count.format.FormatConstant.*;

/**
 * ステップ計測結果をデフォルトのテキスト形式にフォーマットする。
 */
public class TextFormatter extends AbstractFormatter {

	private static final int PATH_MIN_LEN = 40;
	private static final int TYPE_MAX_LEN = 12;
	private static final int CATEGORY_MAX_LEN = 20;
	private static final int EXEC_MAX_LEN = 6;
	private static final int BLANC_MAX_LEN = 6;
	private static final int COMMENT_MAX_LEN = 6;
	private static final int SUM_MAX_LEN = 6;

	/** {@inheritDoc} */
	public byte[] format(final StepCountResult[] results) {

		long sumStep    = 0;
		long sumComment = 0;
		long sumNone    = 0;

		int maxFileLength = maxLengthOfFileNameIn(results);
		// ヘッダをフォーマット
		StringBuffer sb = new StringBuffer();
		//sb.append(fillOrCut("ファイル", maxFileLength));
		//sb.append("種類  カテゴリ            実行  空行  ｺﾒﾝﾄ  合計  ");
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_PATH), maxFileLength));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_TYPE), TYPE_MAX_LEN));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_CATEGORY), CATEGORY_MAX_LEN));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_EXEC), EXEC_MAX_LEN));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_BLANC), BLANC_MAX_LEN));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_COMMENT), COMMENT_MAX_LEN));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_SUM), SUM_MAX_LEN));
		sb.append("\n");
		//sb.append(makeHyphen(maxFileLength));
		//sb.append("--------------------------------------------------");
		sb.append(getHyphens(maxFileLength + TYPE_MAX_LEN + CATEGORY_MAX_LEN
				+ EXEC_MAX_LEN + BLANC_MAX_LEN + COMMENT_MAX_LEN + SUM_MAX_LEN));
		sb.append("\n");
		// １行ずつ処理を行う
		for (int i = 0; i < results.length; i++) {
			StepCountResult result = results[i];
			// 未対応のカウント結果をフォーマット
			if (result.sourceType() == null) {
				sb.append(fillOrCut(result.filePath(), maxFileLength));
				//sb.append("未対応");
				sb.append(this.getMessageText(MSG_COUNT_FMT_UNDEF));
				sb.append("\n");
			// 正常にカウントされた結果をフォーマット
			} else {
				//String fileName = result.getFileName();
				//String fileType = result.getFileType();
				String step     = String.valueOf(result.execSteps());
				String non      = String.valueOf(result.blancSteps());
				String comment  = String.valueOf(result.commentSteps());
				String sum      = String.valueOf(result.sumSteps());

				sb.append(fillOrCut(result.filePath(), maxFileLength));
				sb.append(fillOrCut(result.sourceType(), TYPE_MAX_LEN));
				sb.append(fillOrCut(result.sourceCategory(), CATEGORY_MAX_LEN));
				sb.append(fillLeftOrCut(step, EXEC_MAX_LEN));
				sb.append(fillLeftOrCut(non, BLANC_MAX_LEN));
				sb.append(fillLeftOrCut(comment, COMMENT_MAX_LEN));
				sb.append(fillLeftOrCut(sum, SUM_MAX_LEN));
				sb.append("\n");

				sumStep    += result.execSteps();
				sumComment += result.commentSteps();
				sumNone    += result.blancSteps();
			}
		}
		// 合計行をフォーマット
		//sb.append(makeHyphen(maxFileLength));
		//sb.append("--------------------------------------------------");
		sb.append(getHyphens(maxFileLength + TYPE_MAX_LEN + CATEGORY_MAX_LEN
				+ EXEC_MAX_LEN + BLANC_MAX_LEN + COMMENT_MAX_LEN + SUM_MAX_LEN));
		sb.append("\n");
		//sb.append(fillOrCut("合計", maxFileLength));
		sb.append(fillOrCut(this.getMessageText(MSG_COUNT_FMT_TOTAL), maxFileLength));
		sb.append(getSpaces(TYPE_MAX_LEN));
		sb.append(getSpaces(CATEGORY_MAX_LEN));
		sb.append(fillLeftOrCut(String.valueOf(sumStep), EXEC_MAX_LEN));
		sb.append(fillLeftOrCut(String.valueOf(sumNone), BLANC_MAX_LEN));
		sb.append(fillLeftOrCut(String.valueOf(sumComment), COMMENT_MAX_LEN));
		sb.append(fillLeftOrCut(String.valueOf(sumStep + sumNone + sumComment), SUM_MAX_LEN));
		sb.append("\n");

		return sb.toString().getBytes();
	}

	/**
	 * 計測結果中のファイル名の最大長を取得します（最小40）
	 * @param results ステップ計測結果の配列
	 * @return ファイル名の最大長分の半角文字数
	 */
	protected static int maxLengthOfFileNameIn(final StepCountResult[] results) {
		int fileLength = PATH_MIN_LEN;
		if (results == null || results.length == 0) {
			return fileLength;
		}
		for (StepCountResult result : results) {
			String fileName = result.filePath();

			if (fileName != null) {
				int len = StringUtil.getDisplayWidth(fileName);
				if (fileLength < len) {
					fileLength = len;
				}
			}
		}
		return fileLength;
	}

	/**
	 * 指定された長さの半角スペースを作成します
	 * @param width 半角スペースの数を指定
	 * @return 半角スペース文字列
	 */
	protected static String getSpaces(final int width) {
		return StringUtil.fillWithChars(' ', width);
	}

	/**
	 * 指定された長さの半角ハイフンを作成します
	 * @param width ハイフンの数を指定
	 * @return 半角ハイフン文字列
	 */
	protected static String getHyphens(final int width) {
		return StringUtil.fillWithChars('-', width);
	}

	/**
	 * 文字列が指定の長さ未満であれば右側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 * @param str 整形元の文字列
	 * @param width 整形する長さ
	 * @return 指定された長さに整形した文字列
	 */
	protected static String fillOrCut(final String str, final int width) {
		return StringUtil.shapeIntoFixWidth(str, width);
	}

	/**
	 * 文字列が指定の長さ未満であれば左側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 * @param str 整形元の文字列
	 * @param width 整形する長さ
	 * @return 指定された長さに整形した文字列
	 */
	protected static String fillLeftOrCut(final String str, final int width) {
		return StringUtil.shapeIntoFixWidthRightAlign(str, width);
	}

}