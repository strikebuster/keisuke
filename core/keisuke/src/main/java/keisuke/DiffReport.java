package keisuke;

/**
 * Command for amounting the result of DiffCount
 *
 */
public final class DiffReport {

	private DiffReport() { }

	public static void main(final String[] args) {
		IfMainProc proc = new DiffMainProc();
		proc.main(args);
	}
}
