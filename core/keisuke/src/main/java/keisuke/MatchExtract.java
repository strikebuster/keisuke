package keisuke;

import keisuke.report.ProcedureType;
import keisuke.report.procedure.MainProcFactory;

/**
 * Command for extracting the matching files from result of StepCount.
 */
public final class MatchExtract {

	private MatchExtract() { }

	public static void main(final String[] args) {
		//MainProcedure proc = new MatchMainProc();
		MainProcedure proc = MainProcFactory.create(ProcedureType.MATCH_PROC);
		proc.main(args);
	}
}
