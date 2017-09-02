package keisuke;

import keisuke.report.procedure.MainProcFactory;

/**
 * Command for amounting the result of DiffCount
 */
public final class DiffReport {

	private DiffReport() { }

	public static void main(final String[] args) {
		//MainProcedure proc = new DiffMainProc();
		MainProcedure proc = MainProcFactory.create(ProcedureType.DIFF_PROC);
		proc.main(args);
	}
}
