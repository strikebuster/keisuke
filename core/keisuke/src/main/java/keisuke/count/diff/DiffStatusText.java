package keisuke.count.diff;

import keisuke.CommonDefine;
import keisuke.MessageDefine;

public class DiffStatusText {
	private String none;
	private String added;
	private String modified;
	private String removed;
	private String unsupported;
	
	private MessageDefine msgdef;
	
	public DiffStatusText() {
		this.msgdef = new MessageDefine("diff.status.");
		setTexts();
	}
	
	public DiffStatusText(MessageDefine md) {
		this.msgdef = md;
		setTexts();
	}
	
	private void setTexts() {
		this.none = this.msgdef.getMessage(CommonDefine.MSG_DIFF_STATUS_UNCHANGE);
		this.added = this.msgdef.getMessage(CommonDefine.MSG_DIFF_STATUS_ADD);
		this.modified = this.msgdef.getMessage(CommonDefine.MSG_DIFF_STATUS_MODIFY);
		this.removed = this.msgdef.getMessage(CommonDefine.MSG_DIFF_STATUS_DROP);
		this.unsupported = this.msgdef.getMessage(CommonDefine.MSG_DIFF_STATUS_UNSUPPORT);
	}
	
	public String getText(DiffStatus status) {
		if (status == null) {
			return "";
		}
		switch (status) {
		case NONE:
			return this.none;
		case ADDED:
			return this.added;
		case MODIFIED:
			return this.modified;
		case REMOVED:
			return this.removed;
		case UNSUPPORTED:
			return this.unsupported;
		}
		return "";
	}
	
}
