package keisuke;

public final class CommonDefine {
	// Procedure Type
	public final static String COUNTPROC = "CountReport";
	public final static String DIFFPROC = "DiffReport";
	public final static String MATCHPROC = "MatchExtract";
	// Common Option
	public final static String OPT_PROP = "properties";
	public final static String OPT_CLASS = "classify";
	public final static String OPT_XML = "xml";
	public final static String OPTVAL_EXTENSION = "extension";
	public final static String OPTVAL_LANGUAGE = "language";
	public final static String OPTVAL_LANGGROUP = "group";
	public final static String OPTVAL_FW = "fw:";
	// Property Key
	public final static String CP_LANG = "count.language";
	public final static String CP_EXECSTEP = "count.executableSteps";
	public final static String CP_BLANCSTEP = "count.blancSteps";
	public final static String CP_COMMENTSTEP = "count.commentSteps";
	public final static String CP_SUMSTEP = "count.sumSteps";
	public final static String CP_FILENUM = "count.files";
	public final static String DP_LANG = "diff.language";
	public final static String DP_ADDSTEP = "diff.addSteps";
	public final static String DP_DELSTEP = "diff.deleteSteps";
	public final static String DP_SUMSTEP = "diff.sumSteps";
	public final static String DP_FILENUM = "diff.files";	
	// Message Property Key
	public final static String MSG_COUNT_SUBJECT_HEAD = "count.subject.head";
	public final static String MSG_COUNT_SUBJECT_UNSUPPORT = "count.subject.unsupport";
	public final static String MSG_DIFF_SUBJECT_ADD = "diff.subject.add";
	public final static String MSG_DIFF_SUBJECT_MODIFY = "diff.subject.modify";
	public final static String MSG_DIFF_SUBJECT_DROP = "diff.subject.drop";
	public final static String MSG_DIFF_SUBJECT_UNCHANGE = "diff.subject.unchange";
	public final static String MSG_DIFF_SUBJECT_UNSUPPORT = "diff.subject.unsupport";
	public final static String MSG_DIFF_STATUS_ADD = "diff.status.add";
	public final static String MSG_DIFF_STATUS_MODIFY = "diff.status.modify";
	public final static String MSG_DIFF_STATUS_DROP = "diff.status.drop";
	public final static String MSG_DIFF_STATUS_UNCHANGE = "diff.status.unchange";
	public final static String MSG_DIFF_STATUS_UNSUPPORT = "diff.status.unsupport";
	// XML Node Name for Language
	public final static String XML_NODE_LANG = "Language";
	public final static String XML_NODE_EXT = "FileExtension";
	// XML Node Name for Framework
	public final static String XML_NODE_FW = "Framework";
	public final static String XML_NODE_SPECIFIC = "SpecificType";
	public final static String XML_NODE_PATTERN = "PathPattern";
	// XML Attr Name
	public final static String XML_ATTR_NAME = "name";
	public final static String XML_ATTR_GROUP = "group";
}
