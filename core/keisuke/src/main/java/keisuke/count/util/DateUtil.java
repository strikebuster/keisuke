package keisuke.count.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日付に関するユーティリティメソッドを提供します。
 */
public final class DateUtil {

	private DateUtil() { }

	/**
	 * 日付を文字列に整形して返す
	 * 整形フォーマットは yyyy/MM/dd HH:mm:ss
	 * @param date 日付インスタンス
	 * @return 整形された日付文字列
	 */
	public static String formatDate(final Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return format.format(date);
	}
}
