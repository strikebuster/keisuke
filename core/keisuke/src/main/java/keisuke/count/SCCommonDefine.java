package keisuke.count;

/**
 * Final class to define constants of keisuke.count.*.
 * オプション引数やプロパティキーの文字列定義
 */

public final class SCCommonDefine {

	private SCCommonDefine() { }

	// Procedure Type
	public static final String STEPCOUNTPROC = "StepCount";
	public static final String DIFFCOUNTPROC = "DiffCount";
	// Common Option
	public static final String OPT_HELP = "help";
	public static final String OPT_ENCODE = "encoding";
	public static final String OPT_OUTPUT = "output";
	public static final String OPT_FORMAT = "format";
	public static final String OPT_SHOWDIR = "showDirectory";
	public static final String OPT_XML = "xml";

	public static final String OPTVAL_TEXT = "text";
	public static final String OPTVAL_CSV = "csv";
	public static final String OPTVAL_EXCEL = "excel";
	public static final String OPTVAL_XML = "xml";
	public static final String OPTVAL_JSON = "json";
	public static final String OPTVAL_HTML = "html";

	// Message Property Key
	/*
	public static final String MSG_COUNT_SUBJECT_HEAD = "count.subject.head";
	public static final String MSG_COUNT_SUBJECT_UNSUPPORT = "count.subject.unsupport";
	public static final String MSG_DIFF_SUBJECT_ADD = "diff.subject.add";
	public static final String MSG_DIFF_SUBJECT_MODIFY = "diff.subject.modify";
	public static final String MSG_DIFF_SUBJECT_DROP = "diff.subject.drop";
	public static final String MSG_DIFF_SUBJECT_UNCHANGE = "diff.subject.unchange";
	public static final String MSG_DIFF_SUBJECT_UNSUPPORT = "diff.subject.unsupport";
	*/
	public static final String MSG_COUNT_FMT_PATH = "count.format.path";
	public static final String MSG_COUNT_FMT_TYPE = "count.format.type";
	public static final String MSG_COUNT_FMT_CATEGORY = "count.format.category";
	public static final String MSG_COUNT_FMT_EXEC = "count.format.execution";
	public static final String MSG_COUNT_FMT_BLANC = "count.format.blanc";
	public static final String MSG_COUNT_FMT_COMMENT = "count.format.comment";
	public static final String MSG_COUNT_FMT_SUM = "count.format.sum";
	public static final String MSG_COUNT_FMT_TOTAL = "count.format.total";
	public static final String MSG_COUNT_FMT_UNDEF = "count.format.undef";
	public static final String MSG_DIFF_RND_TIME = "diff.render.time";
	public static final String MSG_DIFF_RND_PATH = "diff.render.path";
	public static final String MSG_DIFF_RND_STATUS = "diff.render.status";
	public static final String MSG_DIFF_RND_INCREASE = "diff.render.increase";
	public static final String MSG_DIFF_RND_DECREASE = "diff.render.decrease";
	public static final String MSG_DIFF_RND_TITLE = "diff.render.html.title";
	public static final String MSG_DIFF_RND_EXPAND = "diff.render.html.expand";
	public static final String MSG_DIFF_RND_HIDE = "diff.render.html.hide";

}
