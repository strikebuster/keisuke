package keisuke.report.property;

/**
 * Defined constants of keisuke.report.property.*.
 * プロパティーのキーの文字列定義
 */
public final class PropertyConstant {

	private PropertyConstant() { }

	// Property Key
	public static final String CP_LANG = "count.language";
	public static final String CP_EXECSTEP = "count.executableSteps";
	public static final String CP_BLANCSTEP = "count.blancSteps";
	public static final String CP_COMMENTSTEP = "count.commentSteps";
	public static final String CP_SUMSTEP = "count.sumSteps";
	public static final String CP_FILENUM = "count.files";
	public static final String DP_LANG = "diff.language";
	public static final String DP_ADDSTEP = "diff.addSteps";
	public static final String DP_DELSTEP = "diff.deleteSteps";
	public static final String DP_SUMSTEP = "diff.sumSteps";
	public static final String DP_FILENUM = "diff.files";

	// Key Prefix which means the category of keys
	public static final String CP_PREFIX = "count.";
	public static final String DP_PREFIX = "diff.";

}
