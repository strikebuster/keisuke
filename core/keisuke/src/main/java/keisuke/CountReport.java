package keisuke;

/**
 * Command for amounting the result of StepCount
 *
 */
public final class CountReport {

	private CountReport() { }

	public static void main(final String[] args) {
		IfMainProc proc = new CountMainProc();
		proc.main(args);
	}
}
