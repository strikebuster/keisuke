package keisuke.count.option;

import keisuke.count.FormatEnum;

/**
 * Defined constants of keisuke.count.option.*
 * オプション引数の文字列定義
 */
public final class CountOptionConstant {

	private CountOptionConstant() { }

	// StepCount and/or DiffCount Option
	public static final String OPT_HELP = "help";
	public static final String OPT_ENCODE = "encoding";
	public static final String OPT_OUTPUT = "output";
	public static final String OPT_FORMAT = "format";
	public static final String OPT_SHOWDIR = "showDirectory";
	public static final String OPT_SORT = "sort";
	public static final String OPT_XML = "xml";

	public static final String OPTVAL_TEXT = FormatEnum.TEXT.toString();
	public static final String OPTVAL_CSV = FormatEnum.CSV.toString();
	public static final String OPTVAL_EXCEL = FormatEnum.EXCEL.toString();
	public static final String OPTVAL_XML = FormatEnum.XML.toString();
	public static final String OPTVAL_JSON = FormatEnum.JSON.toString();
	public static final String OPTVAL_HTML = FormatEnum.HTML.toString();

	public static final String OPTVAL_SORT_ON = "on";
	public static final String OPTVAL_SORT_OS = "os";
	public static final String OPTVAL_SORT_OFF = "off";

	// Match Proc Option
	public static final String ARG_NEWDIR = "newDir";
	public static final String ARG_OLDDIR = "oldDir";
}
