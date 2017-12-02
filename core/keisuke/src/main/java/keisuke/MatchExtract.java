package keisuke;

import keisuke.report.procedure.MatchMainProc;

/**
 * Command for extracting the matching files from result of StepCount.
 */
public final class MatchExtract {

	private MatchExtract() { }

	public static void main(final String[] args) {
		new MatchMainProc().main(args);
	}
}
