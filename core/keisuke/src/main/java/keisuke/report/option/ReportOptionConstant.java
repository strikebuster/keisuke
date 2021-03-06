package keisuke.report.option;

import keisuke.count.FormatEnum;
import keisuke.count.PathStyleEnum;
import keisuke.report.classify.ClassifierConstant;

/**
 * Defined constants of keisuke.report.option.*
 * オプション引数の文字列定義
 */
public final class ReportOptionConstant {

	private ReportOptionConstant() { }

	// Common Option = Count Proc Option
	public static final String OPT_PROP = "properties";
	public static final String OPT_CLASS = "classify";
	public static final String OPT_XML = "xml";
	public static final String OPT_IN = "input";
	public static final String OPT_OUT = "output";
	public static final String OPTVAL_EXTENSION = ClassifierConstant.CLASSIFY_EXTENSION;
	public static final String OPTVAL_LANGUAGE = ClassifierConstant.CLASSIFY_LANGUAGE;
	public static final String OPTVAL_LANGGROUP = ClassifierConstant.CLASSIFY_LANGGROUP;
	public static final String OPTVAL_FW = ClassifierConstant.CLASSIFY_FW;
	public static final String ARG_INPUT = "infile";

	// Diff Proc Option
	public static final String OPT_AOUT = "aout";
	public static final String OPT_MOUT = "mout";
	public static final String OPT_FORMAT = "format";
	public static final String OPT_UNCHANGE = "unchange";
	public static final String OPTVAL_CSV = FormatEnum.CSV.value(); // for format
	public static final String OPTVAL_TEXT = FormatEnum.TEXT.value(); // for format
	public static final String OPTVAL_TOTAL = "total"; // for unchange
	public static final String OPTVAL_DETAIL = "detail"; // for unchange

	// Match Proc Option
	public static final String ARG_MASTER = "mafile";
	public static final String ARG_TRANSACTION = "trfile";
	public static final String ARG_OUTPUT = "outfile";
	public static final String OPT_PATH = "path";
	public static final String OPTVAL_BASE = PathStyleEnum.BASE.value(); // for path
	public static final String OPTVAL_SUB = PathStyleEnum.SUB.value(); // for path

}
