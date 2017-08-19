package keisuke.count.diff.renderer;

import keisuke.MessageDefine;
import keisuke.count.diff.DiffFolderResult;

/**
 * 差分計測結果の出力整形をするI/Fを実装する抽象基底クラス
 */
public abstract class AbstractRenderer implements Renderer {

	private MessageDefine msgdef;

	public AbstractRenderer() {	}

	/**
	 * メッセージ定義インスタンスを返す
	 * @return メッセージ定義インスタンス
	 */
	protected MessageDefine messageDefine() {
		return this.msgdef;
	}

	/**
	 * メッセージ定義インスタンスを設定する
	 * @param messageDefine メッセージ定義インスタンス
	 */
	public void setMessageDefine(final MessageDefine messageDefine) {
		this.msgdef = messageDefine;
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
		if (this.msgdef == null) {
			this.msgdef = new MessageDefine("diff.render.");
		}
		return this.msgdef.getMessage(key);
	}

	/** {@inheritDoc} */
	public abstract byte[] render(DiffFolderResult result);

}
