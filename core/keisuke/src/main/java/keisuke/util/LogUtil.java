package keisuke.util;

/**
 * 処理のロギングをするユーティリティ
 */
public final class LogUtil {

	private LogUtil() { }

	public static void errorLog(final String msg) {
		System.err.print("!![ERROR] ");
		System.err.println(msg);
	}

	public static void warningLog(final String msg) {
		System.err.print("![WARN] ");
		System.err.println(msg);
	}

	public static void debugLog(final String msg) {
		System.out.print("[DEBUG] ");
		System.out.println(msg);
	}
}
