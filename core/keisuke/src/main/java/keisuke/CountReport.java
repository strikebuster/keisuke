package keisuke;

import keisuke.report.ProcedureType;
import keisuke.report.procedure.MainProcFactory;

/**
 * Command for amounting the result of StepCount
 */
public final class CountReport {

	private CountReport() { }

	public static void main(final String[] args) {
		//MainProcedure proc = new CountMainProc();
		MainProcedure proc = MainProcFactory.create(ProcedureType.COUNT_PROC);
		proc.main(args);
	}
}
