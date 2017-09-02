package keisuke.report.property;

/**
 * Defined constants of keisuke.report.property.*.
 * メッセージプロパティーのキーの文字列定義
 */
public final class MessageConstant {

	private MessageConstant() { }

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

	// Key Prefix which means the category of keys
	public static final String MSG_COUNT_SUBJECT_PREFIX = "count.subject.";
	public static final String MSG_DIFF_SUBJECT_PREFIX = "diff.subject.";
	public static final String MSG_DIFF_STATUS_PREFIX = "diff.status.";

}
