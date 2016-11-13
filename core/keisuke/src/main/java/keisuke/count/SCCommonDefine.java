package keisuke.count;

/**
 * keisuke:追加クラス
 * オプション引数やプロパティキーの文字列定義
 */
public final class SCCommonDefine {
	// Procedure Type
	public final static String STEPCOUNTPROC = "StepCount";
	public final static String DIFFCOUNTPROC = "DiffCount";
	// Common Option
	public final static String OPT_HELP = "help";
	public final static String OPT_ENCODE = "encoding";
	public final static String OPT_OUTPUT = "output";
	public final static String OPT_FORMAT = "format";
	public final static String OPT_SHOWDIR = "showDirectory";
	public final static String OPT_XML = "xml";
	
	public final static String OPTVAL_TEXT = "text";
	public final static String OPTVAL_CSV = "csv";
	public final static String OPTVAL_EXCEL = "excel";
	public final static String OPTVAL_XML = "xml";
	public final static String OPTVAL_JSON = "json";
	public final static String OPTVAL_HTML = "html";

	// Message Property Key
	/*
	public final static String MSG_COUNT_SUBJECT_HEAD = "count.subject.head";
	public final static String MSG_COUNT_SUBJECT_UNSUPPORT = "count.subject.unsupport";
	public final static String MSG_DIFF_SUBJECT_ADD = "diff.subject.add";
	public final static String MSG_DIFF_SUBJECT_MODIFY = "diff.subject.modify";
	public final static String MSG_DIFF_SUBJECT_DROP = "diff.subject.drop";
	public final static String MSG_DIFF_SUBJECT_UNCHANGE = "diff.subject.unchange";
	public final static String MSG_DIFF_SUBJECT_UNSUPPORT = "diff.subject.unsupport";
	*/
	public final static String MSG_COUNT_FMT_PATH = "count.format.path";
	public final static String MSG_COUNT_FMT_TYPE = "count.format.type";
	public final static String MSG_COUNT_FMT_CATEGORY = "count.format.category";
	public final static String MSG_COUNT_FMT_EXEC = "count.format.execution";
	public final static String MSG_COUNT_FMT_BLANC = "count.format.blanc";
	public final static String MSG_COUNT_FMT_COMMENT = "count.format.comment";
	public final static String MSG_COUNT_FMT_SUM = "count.format.sum";
	public final static String MSG_COUNT_FMT_TOTAL = "count.format.total";
	public final static String MSG_COUNT_FMT_UNDEF = "count.format.undef";
	public final static String MSG_DIFF_RND_TIME = "diff.render.time";
	public final static String MSG_DIFF_RND_PATH = "diff.render.path";
	public final static String MSG_DIFF_RND_STATUS = "diff.render.status";
	public final static String MSG_DIFF_RND_INCREASE = "diff.render.increase";
	public final static String MSG_DIFF_RND_DECREASE = "diff.render.decrease";
	public final static String MSG_DIFF_RND_TITLE = "diff.render.html.title";
	public final static String MSG_DIFF_RND_EXPAND = "diff.render.html.expand";
	public final static String MSG_DIFF_RND_HIDE = "diff.render.html.hide";

}
