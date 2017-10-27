package keisuke.report;

/**
 * StepCount/DiffCountの計測結果ファイルの行を解析していて
 * 正しく解析できない行であった場合に投げられるException
 */
public class IllegalFormattedLineException extends Exception {

	private static final long serialVersionUID = 0L;

	public IllegalFormattedLineException() { }

	public IllegalFormattedLineException(final String message) {
		super(message);
	}

	public IllegalFormattedLineException(final Throwable cause) {
		super(cause);
	}

	public IllegalFormattedLineException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
