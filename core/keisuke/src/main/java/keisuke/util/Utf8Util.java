package keisuke.util;

/**
 * UTF8コードに関するユーティリティ
 */
public final class Utf8Util {

	private static final char UTF8_LATIN1_MAX = 0x00FF;
	private static final char UTF8_HALF_LETTER_MIN = 0xFF61;
	private static final char UTF8_HALF_LETTER_MAX = 0xFFDC;
	private static final char UTF8_HALF_SYMBOL_MIN = 0xFFE8;
	private static final char UTF8_HALF_SYMBOL_MAX = 0xFFEE;

	private Utf8Util() { }

	/**
	 * 指定された文字が半角文字であるかチェックする
	 * @param ch 文字
	 * @return 半角文字であればtrue
	 */
	public static boolean isHalfWidthChar(final char ch) {
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
}
