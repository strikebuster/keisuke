package keisuke;

import keisuke.report.procedure.CountMainProc;

/**
 * Command for amounting the result of StepCount
 */
public final class CountReport {

	private CountReport() { }

	public static void main(final String[] args) {
		new CountMainProc().main(args);
	}
}
