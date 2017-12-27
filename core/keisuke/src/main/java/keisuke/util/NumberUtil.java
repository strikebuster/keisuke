package keisuke.util;

/**
 * 数値に関する処理
 */
public final class NumberUtil {

	private NumberUtil() { }

	/**
	 * Java1.6までのLong.parseLongでは+文字で始まる正の数値を
	 * 変換できないので、+を除いてparseLongして返す
	 * @param number 整数値を表す文字列
	 * @return long型の数値
	 * @throws NumberFormatException 文字列が数値表現でない場合に発行
	 */
	public static long parseLong(final String number) throws NumberFormatException {
		rejectNullOrEmpty(number);
		if (number.charAt(0) == '+') {
			// can not parse '+' sign character within Java1.6 or less
			return Long.parseLong(number.substring(1));
		} else {
			return Long.parseLong(number);
		}
	}

	/**
	 * Java1.6までのInteger.parseIntでは+文字で始まる正の数値を
	 * 変換できないので、+を除いてparseIntして返す
	 * @param number 整数値を表す文字列
	 * @return int型の数値
	 * @throws NumberFormatException 文字列が数値表現でない場合に発行
	 */
	public static int parseInt(final String number) throws NumberFormatException {
		rejectNullOrEmpty(number);
		if (number.charAt(0) == '+') {
			// can not parse '+' sign character within Java1.6 or less
			return Integer.parseInt(number.substring(1));
		} else {
			return Integer.parseInt(number);
		}
	}

	/**
	 * Java1.6までのShort.parseShortでは+文字で始まる正の数値を
	 * 変換できないので、+を除いてparseShortして返す
	 * @param number 整数値を表す文字列
	 * @return short型の数値
	 * @throws NumberFormatException 文字列が数値表現でない場合に発行
	 */
	public static short parseShort(final String number) throws NumberFormatException {
		rejectNullOrEmpty(number);
		if (number.charAt(0) == '+') {
			// can not parse '+' sign character within Java1.6 or less
			return Short.parseShort(number.substring(1));
		} else {
			return Short.parseShort(number);
		}
	}

	private static void rejectNullOrEmpty(final String arg) throws NumberFormatException {
		if (arg == null) {
			throw new NumberFormatException("argument is null.");
		} else if (arg.isEmpty()) {
			throw new NumberFormatException("argument is empty.");
		}
	}
}
