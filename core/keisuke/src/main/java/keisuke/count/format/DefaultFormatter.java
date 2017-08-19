package keisuke.count.format;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.Util;
import keisuke.count.SCCommonDefine;

/**
 * ステップ計測結果をデフォルトのテキスト形式にフォーマットする。
 */
public class DefaultFormatter extends AbstractFormatter {

	private static final int PATH_MIN_LEN = 40;
	private static final int TYPE_MAX_LEN = 12;
	private static final int CATEGORY_MAX_LEN = 20;
	private static final int EXEC_MAX_LEN = 6;
	private static final int BLANC_MAX_LEN = 6;
	private static final int COMMENT_MAX_LEN = 6;
	private static final int SUM_MAX_LEN = 6;

	private static final char UTF8_LATIN1_MAX = 0x00FF;
	private static final char UTF8_HALF_LETTER_MIN = 0xFF61;
	private static final char UTF8_HALF_LETTER_MAX = 0xFFDC;
	private static final char UTF8_HALF_SYMBOL_MIN = 0xFFE8;
	private static final char UTF8_HALF_SYMBOL_MAX = 0xFFEE;

	/** {@inheritDoc} */
	public byte[] format(final CountResult[] results) {

		long sumStep    = 0;
		long sumComment = 0;
		long sumNone    = 0;

		int maxFileLength = maxLengthOfFileNameIn(results);
		// ヘッダをフォーマット
		StringBuffer sb = new StringBuffer();
		//sb.append(fillOrCut("ファイル", maxFileLength));
		//sb.append("種類  カテゴリ            実行  空行  ｺﾒﾝﾄ  合計  ");
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_PATH), maxFileLength));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_TYPE), TYPE_MAX_LEN));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_CATEGORY), CATEGORY_MAX_LEN));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_EXEC), EXEC_MAX_LEN));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_BLANC), BLANC_MAX_LEN));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_COMMENT), COMMENT_MAX_LEN));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_SUM), SUM_MAX_LEN));
		sb.append("\n");
		//sb.append(makeHyphen(maxFileLength));
		//sb.append("--------------------------------------------------");
		sb.append(makeHyphen(maxFileLength + TYPE_MAX_LEN + CATEGORY_MAX_LEN
				+ EXEC_MAX_LEN + BLANC_MAX_LEN + COMMENT_MAX_LEN + SUM_MAX_LEN));
		sb.append("\n");
		// １行ずつ処理を行う
		for (int i = 0; i < results.length; i++) {
			CountResult result = results[i];
			// 未対応のカウント結果をフォーマット
			if (result.getFileType() == null) {
				sb.append(fillOrCut(result.getFileName(), maxFileLength));
				//sb.append("未対応");
				sb.append(getMessageText(SCCommonDefine.MSG_COUNT_FMT_UNDEF));
				sb.append("\n");
			// 正常にカウントされた結果をフォーマット
			} else {
//				String fileName = result.getFileName();
//				String fileType = result.getFileType();
				String step     = String.valueOf(result.getStep());
				String non      = String.valueOf(result.getNon());
				String comment  = String.valueOf(result.getComment());
				String sum      = String.valueOf(result.getStep() + result.getNon()
									+ result.getComment());

				sb.append(fillOrCut(result.getFileName(), maxFileLength));
				sb.append(fillOrCut(result.getFileType(), TYPE_MAX_LEN));
				sb.append(fillOrCut(result.getCategory(), CATEGORY_MAX_LEN));
				sb.append(fillLeftOrCut(step, EXEC_MAX_LEN));
				sb.append(fillLeftOrCut(non, BLANC_MAX_LEN));
				sb.append(fillLeftOrCut(comment, COMMENT_MAX_LEN));
				sb.append(fillLeftOrCut(sum, SUM_MAX_LEN));
				sb.append("\n");

				sumStep    += result.getStep();
				sumComment += result.getComment();
				sumNone    += result.getNon();
			}
		}
		// 合計行をフォーマット
		//sb.append(makeHyphen(maxFileLength));
		//sb.append("--------------------------------------------------");
		sb.append(makeHyphen(maxFileLength + TYPE_MAX_LEN + CATEGORY_MAX_LEN
				+ EXEC_MAX_LEN + BLANC_MAX_LEN + COMMENT_MAX_LEN + SUM_MAX_LEN));
		sb.append("\n");
		//sb.append(fillOrCut("合計", maxFileLength));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_TOTAL), maxFileLength));
		sb.append(makeSpace(TYPE_MAX_LEN));
		sb.append(makeSpace(CATEGORY_MAX_LEN));
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
	protected static int maxLengthOfFileNameIn(final CountResult[] results) {
		int fileLength = PATH_MIN_LEN;
		if (results == null || results.length == 0) {
			return fileLength;
		}
		for (CountResult result : results) {
			String fileName = result.getFileName();

			if (fileName != null) {
				int len = getDisplayWidth(fileName);
				if (fileLength < len) {
					fileLength = len;
				}
			}
		}
		return fileLength;
	}

	/**
	 * テキストの表示幅を計算して返す
	 * @param text 表示対象の文字列
	 * @return 表示に必要な幅の半角文字数
	 */
	protected static int getDisplayWidth(final String text) {
		if (text == null || text.isEmpty()) {
			return 0;
		}

		int len = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (isHalfWidthChar(c)) {
				len += 1;
			} else {
				len += 2;
			}
		}
		return len;
	}

	private static boolean isHalfWidthChar(final char ch) {
		if (ch <= UTF8_LATIN1_MAX) {
			// ASCII(LATIN)とLATIN1補助
			return true;
		} else if (ch >= UTF8_HALF_LETTER_MIN && ch <= UTF8_HALF_LETTER_MAX) {
			// 半角カタカナ、ハングル文字
			return true;
		} else if (ch >= UTF8_HALF_SYMBOL_MIN && ch <= UTF8_HALF_SYMBOL_MAX) {
			// 半角記号
			return true;
		}
		return false;
	}

	/**
	 * 指定された長さの半角スペースを作成します
	 * @param width 半角スペースの数を指定
	 * @return 半角スペース文字列
	 */
	protected static String makeSpace(final int width) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < width; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 指定された長さの半角ハイフンを作成します
	 * @param width ハイフンの数を指定
	 * @return 半角ハイフン文字列
	 */
	protected static String makeHyphen(final int width) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < width; i++) {
			sb.append('-');
		}
		return sb.toString();
	}

	/**
	 * 文字列が指定の長さ未満であれば右側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 * @param str 整形元の文字列
	 * @param width 整形する長さ
	 * @return 指定された長さに整形した文字列
	 */
	protected static String fillOrCut(final String str, final int width) {
		int length = getDisplayWidth(str);
		if (length == width) {
			return str;
		} else if (length < width) {
			return str + makeSpace(width - length);
		} else {
			return Util.substring(str, width);
		}
	}

	/**
	 * 文字列が指定の長さ未満であれば左側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 * @param str 整形元の文字列
	 * @param width 整形する長さ
	 * @return 指定された長さに整形した文字列
	 */
	protected static String fillLeftOrCut(final String str, final int width) {
		int length = Util.getByteLength(str);
		if (length == width) {
			return str;
		} else if (length < width) {
			return makeSpace(width - length) + str;
		} else {
			return Util.substring(str, width);
		}
	}
}
