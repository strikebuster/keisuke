package keisuke.count.diff.renderer;

import keisuke.MessageMap;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.diff.Renderer;
import keisuke.report.property.MessageDefine;
import static keisuke.count.diff.renderer.RendererConstant.*;

/**
 * 差分計測結果の出力整形をするI/Fを実装する抽象基底クラス
 */
public abstract class AbstractRenderer implements Renderer {

	private MessageMap messageMap = null;

	protected AbstractRenderer() {	}

	/**
	 * メッセージ定義インスタンスを返す
	 * @return メッセージ定義インスタンス
	 */
	protected MessageMap messageMap() {
		return this.messageMap;
	}

	/**
	 * メッセージ定義インスタンスを設定する
	 * @param msgdef メッセージ定義インスタンス
	 */
	public void setMessageMap(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.messageMap = msgdef.getMessageMap();
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
		if (this.messageMap == null) {
			this.messageMap = new MessageDefine(MSG_DIFF_RND_PREFIX).getMessageMap();
		}
		return this.messageMap.get(key);
	}

	/** {@inheritDoc} */
	public abstract byte[] render(DiffFolderResult result);

}
