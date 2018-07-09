package keisuke.count.diff.renderer;

import keisuke.MessageMap;
import keisuke.count.Formatter;
import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;

import keisuke.DiffStatusEnum;
import keisuke.DiffStatusLabels;
import keisuke.DiffStatusLabelsImpl;

/**
 * 差分計測結果の出力整形をするI/Fを実装する抽象基底クラス
 */
public abstract class AbstractRenderer implements Formatter<DiffFolderResult> {

	private MessageMap messageMap = null;
	private DiffStatusLabels diffStatusLabels = null;

	protected AbstractRenderer() {	}

	/**
	 * メッセージ定義インスタンスを設定する
	 * @param msgdef メッセージ定義インスタンス
	 */
	protected void setMessageMap(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.messageMap = msgdef.getMessageMap();
		this.diffStatusLabels = new DiffStatusLabelsImpl(msgdef);
	}

	/**
	 * DiffStatusLabelsインスタンスを返す
	 * @return DiffStatusLabelsインスタンス
	 */
	protected DiffStatusLabels diffStatusLabels() {
		return this.diffStatusLabels;
	}

	/**
	 * 差分計測結果の出力形式で使用する定形文言を返す
	 * @param key 定形文言の種類を示すキー
	 * @return 表示用定形文言
	 */
	protected String getMessageText(final String key) {
		if (key == null) {
			return "";
		}
		return this.messageMap.get(key);
	}

	/**
	 * 変更ステータスに対するメッセージ定義の文言を返す
	 * @param status 変更ステータス
	 * @return メッセージ定義の文言
	 */
	protected String getStatusLabelOf(final DiffStatusEnum status) {
		return this.diffStatusLabels.getLabelOf(status);
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * 引数は差分計測結果
	 */
	public abstract byte[] format(DiffFolderResult result);

	/** {@inheritDoc} */
	public abstract boolean isText();

	/** {@inheritDoc} */
	public abstract String textEncoding();
}
