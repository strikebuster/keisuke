package keisuke;

import keisuke.report.property.MessageDefine;
import static keisuke.report.property.MessageConstant.*;

/**
 * DiffStatusTextインタフェースを実装するクラス
 * DiffStatusEnumの差分変更ステータスに対応する表示文言を配列で保持する
 */
public class DiffStatusTextsImpl implements DiffStatusTexts {

	private static final int DIFF_STATUS_NUM = DiffStatusEnum.values().length;
	private String[] statusTexts = new String[DIFF_STATUS_NUM];
	private MessageMap msgmap;

	/**
	 * コンストラクター
	 * デフォルトのメッセージ定義を使用すて表示文言を設定する
	 */
	public DiffStatusTextsImpl() {
		this.msgmap = new MessageDefine(MSG_DIFF_STATUS_PREFIX).getMessageMap();
		this.setTexts();
	}

	/**
	 * メッセージ定義を指定するコンストラクター
	 * 指定されたメッセージ定義から表示文言を設定する
	 * @param msgdef メッセージ定義保持インスタンス
	 */
	public DiffStatusTextsImpl(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.msgmap = msgdef.getMessageMap();
		this.setTexts();
	}

	/**
	 * メッセージ定義解析結果を保持するMapを指定するコンストラクター
	 * @param map メッセージ定義Mapインスタンス
	 */
	public DiffStatusTextsImpl(final MessageMap map) {
		this.msgmap = map;
		this.setTexts();
	}

	private void setTexts() {
		this.statusTexts[DiffStatusEnum.ADDED.index()] = this.msgmap.get(MSG_DIFF_STATUS_ADD);
		this.statusTexts[DiffStatusEnum.MODIFIED.index()] = this.msgmap.get(MSG_DIFF_STATUS_MODIFY);
		this.statusTexts[DiffStatusEnum.DROPED.index()] = this.msgmap.get(MSG_DIFF_STATUS_DROP);
		this.statusTexts[DiffStatusEnum.UNCHANGED.index()] = this.msgmap.get(MSG_DIFF_STATUS_UNCHANGE);
		this.statusTexts[DiffStatusEnum.UNSUPPORTED.index()] = this.msgmap.get(MSG_DIFF_STATUS_UNSUPPORT);
	}

	/** {@inheritDoc} */
	public String getTextOf(final DiffStatusEnum status) {
		if (status == null) {
			return "";
		}
		switch (status) {
		case ADDED:
			return this.statusTexts[DiffStatusEnum.ADDED.index()];
		case MODIFIED:
			return this.statusTexts[DiffStatusEnum.MODIFIED.index()];
		case DROPED:
			return this.statusTexts[DiffStatusEnum.DROPED.index()];
		case UNCHANGED:
			return this.statusTexts[DiffStatusEnum.UNCHANGED.index()];
		case UNSUPPORTED:
			return this.statusTexts[DiffStatusEnum.UNSUPPORTED.index()];
		default:
			return "";
		}
	}

	/** {@inheritDoc} */
	public DiffStatusEnum whichDiffStatusIs(final String text) {
		if (text == null) {
			return null;
		}
		for (int i = 0; i < this.statusTexts.length; i++) {
			if (text.equals(this.statusTexts[i])) {
				return DiffStatusEnum.values()[i];
			}
		}
		return null;
	}

}
