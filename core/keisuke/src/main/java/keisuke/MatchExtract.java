package keisuke;

/**
 * Command for extracting the matching files from result of StepCount.
 * @author strikebuster
 *
 */
public final class MatchExtract {

	private MatchExtract() { }

	public static void main(final String[] args) {
		IfMainProc proc = new MatchMainProc();
		proc.main(args);
	}
}
