package keisuke.count.diff.renderer;

import keisuke.MessageMap;
import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;

/**
 * 差分計測結果の出力整形をするI/Fを実装する抽象基底クラス
 */
public abstract class AbstractRenderer implements Renderer {

	private MessageMap msgmap = null;

	public AbstractRenderer() {	}

	/**
	 * メッセージ定義インスタンスを返す
	 * @return メッセージ定義インスタンス
	 */
	protected MessageMap messageMap() {
		return this.msgmap;
	}

	/**
	 * メッセージ定義インスタンスを設定する
	 * @param msgdef メッセージ定義インスタンス
	 */
	public void setMessageMap(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.msgmap = msgdef.getMessageMap();
	}

	/**
	 * 差分計測結果の出力形式で使用する定形文言を返す
	 * @param key 定形文言の種類を示すキー
	 * @return 表示用定形文言
	 */
	public String getMessageText(final String key) {
		if (key == null) {
			return "";
		}
		if (this.msgmap == null) {
			this.msgmap = new MessageDefine("diff.render.").getMessageMap();
		}
		return this.msgmap.get(key);
	}

	/** {@inheritDoc} */
	public abstract byte[] render(DiffFolderResult result);

}
