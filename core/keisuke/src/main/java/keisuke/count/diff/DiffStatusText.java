package keisuke.count.diff;

import keisuke.MessageMap;
import keisuke.report.property.MessageDefine;
import static keisuke.report.property.MessageConstant.*;

/**
 * DiffStatusの差分変更ステータスに対応する表示文言を設定するクラス
 */
public class DiffStatusText {
	private String none;
	private String added;
	private String modified;
	private String removed;
	private String unsupported;

	private MessageMap msgmap;

	/**
	 * コンストラクター
	 * デフォルトのメッセージ定義を使用すて表示文言を設定する
	 */
	public DiffStatusText() {
		this.msgmap = new MessageDefine("diff.status.").getMessageMap();
		this.setTexts();
	}

	/**
	 * メッセージ定義を指定するコンストラクター
	 * 指定されたメッセージ定義から表示文言を設定する
	 * @param msgdef メッセージ定義保持インスタンス
	 */
	public DiffStatusText(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.msgmap = msgdef.getMessageMap();
		this.setTexts();
	}

	private void setTexts() {
		this.none = this.msgmap.get(MSG_DIFF_STATUS_UNCHANGE);
		this.added = this.msgmap.get(MSG_DIFF_STATUS_ADD);
		this.modified = this.msgmap.get(MSG_DIFF_STATUS_MODIFY);
		this.removed = this.msgmap.get(MSG_DIFF_STATUS_DROP);
		this.unsupported = this.msgmap.get(MSG_DIFF_STATUS_UNSUPPORT);
	}

	/**
	 * DiffStatusに対応する表示文言を返す
	 * @param status 差分変更ステータス
	 * @return ステータスに対応する表示文言
	 */
	public String getText(final DiffStatus status) {
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
		default:
			return "";
		}
	}

}
