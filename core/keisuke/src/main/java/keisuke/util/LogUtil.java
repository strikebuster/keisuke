package keisuke.util;

/**
 * 処理のロギングをするユーティリティ
 */
public final class LogUtil {

	private LogUtil() { }

	/**
	 * 引数の文字列をエラー情報としてSystem.errに出力する
	 * @param msg text for printing.
	 */
	public static void errorLog(final String msg) {
		System.err.print("!![ERROR] ");
		System.err.println(msg);
	}

	/**
	 * 引数の文字列を警告情報としてSystem.errに出力する
	 * @param msg text for printing.
	 */
	public static void warningLog(final String msg) {
		System.err.print("![WARN] ");
		System.err.println(msg);
	}

	/**
	 * 引数の文字列をデバッグ情報としてSystem.outに出力する
	 * @param msg text for printing.
	 */
	public static void debugLog(final String msg) {
		System.out.print("[DEBUG] ");
		System.out.println(msg);
	}

	/**
	 * ExceptionのgetMessage()を返す。
	 * getMessage()がnullの場合はtoString()を返す。
	 * @param ex Exception
	 * @return text about the exception
	 */
	public static String getMessage(final Exception ex) {
    	if (ex.getMessage() == null) {
    		return ex.toString();
    	}
        return ex.getMessage();
    }
}
