package keisuke;

import keisuke.report.procedure.DiffMainProc;

/**
 * Command for amounting the result of DiffCount
 */
public final class DiffReport {

	private DiffReport() { }

	public static void main(final String[] args) {
		new DiffMainProc().main(args);
	}
}
