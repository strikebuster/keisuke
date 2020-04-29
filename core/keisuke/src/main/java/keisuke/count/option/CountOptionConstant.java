package keisuke.count.option;

import keisuke.count.FormatEnum;
import keisuke.count.PathStyleEnum;
import keisuke.count.SortOrderEnum;

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
	public static final String OPT_PATH = "path";
	public static final String OPT_SORT = "sort";
	public static final String OPT_XML = "xml";

	public static final String OPTVAL_TEXT = FormatEnum.TEXT.value();
	public static final String OPTVAL_CSV = FormatEnum.CSV.value();
	public static final String OPTVAL_EXCEL = FormatEnum.EXCEL.value();
	public static final String OPTVAL_XML = FormatEnum.XML.value();
	public static final String OPTVAL_JSON = FormatEnum.JSON.value();
	public static final String OPTVAL_HTML = FormatEnum.HTML.value();

	public static final String OPTVAL_PATH_BASE = PathStyleEnum.BASE.value();
	public static final String OPTVAL_PATH_SUB = PathStyleEnum.SUB.value();
	public static final String OPTVAL_PATH_NO = PathStyleEnum.NO.value();
	// this value is not allowed as argument option value.
	public static final String OPTVAL_PATH_SHOWDIR = PathStyleEnum.SHOWDIR.value();

	public static final String OPTVAL_SORT_ON = SortOrderEnum.ON.value();
	public static final String OPTVAL_SORT_OS = SortOrderEnum.OS.value();
	public static final String OPTVAL_SORT_OFF = SortOrderEnum.OFF.value();
	public static final String OPTVAL_SORT_NODE = SortOrderEnum.NODE.value();

	// DiffCount Argument
	public static final String ARG_NEWDIR = "newDir";
	public static final String ARG_OLDDIR = "oldDir";
}
