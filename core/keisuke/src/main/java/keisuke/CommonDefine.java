package keisuke;

/**
 * Defined constants of keisuke.*.
 * オプション引数やプロパティキーの文字列定義
 * @author strikebuster
 *
 */
public final class CommonDefine {

	private CommonDefine() { }

	// Procedure Type
	public static final String COUNTPROC = "CountReport";
	public static final String DIFFPROC = "DiffReport";
	public static final String MATCHPROC = "MatchExtract";
	// Common Option
	public static final String OPT_PROP = "properties";
	public static final String OPT_CLASS = "classify";
	public static final String OPT_XML = "xml";
	public static final String OPTVAL_EXTENSION = "extension";
	public static final String OPTVAL_LANGUAGE = "language";
	public static final String OPTVAL_LANGGROUP = "group";
	public static final String OPTVAL_FW = "fw:";
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
	// Message Property Key
	public static final String MSG_COUNT_SUBJECT_HEAD = "count.subject.head";
	public static final String MSG_COUNT_SUBJECT_UNSUPPORT = "count.subject.unsupport";
	public static final String MSG_DIFF_SUBJECT_ADD = "diff.subject.add";
	public static final String MSG_DIFF_SUBJECT_MODIFY = "diff.subject.modify";
	public static final String MSG_DIFF_SUBJECT_DROP = "diff.subject.drop";
	public static final String MSG_DIFF_SUBJECT_UNCHANGE = "diff.subject.unchange";
	public static final String MSG_DIFF_SUBJECT_UNSUPPORT = "diff.subject.unsupport";
	public static final String MSG_DIFF_STATUS_ADD = "diff.status.add";
	public static final String MSG_DIFF_STATUS_MODIFY = "diff.status.modify";
	public static final String MSG_DIFF_STATUS_DROP = "diff.status.drop";
	public static final String MSG_DIFF_STATUS_UNCHANGE = "diff.status.unchange";
	public static final String MSG_DIFF_STATUS_UNSUPPORT = "diff.status.unsupport";
	// XML Node Name for Language
	public static final String XML_NODE_LANG = "Language";
	public static final String XML_NODE_EXT = "FileExtension";
	// XML Node Name for Framework
	public static final String XML_NODE_FW = "Framework";
	public static final String XML_NODE_SPECIFIC = "SpecificType";
	public static final String XML_NODE_PATTERN = "PathPattern";
	// XML Attr Name
	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_GROUP = "group";
}
