package keisuke.count.format;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.format.ResultFormatter;
import keisuke.MessageDefine;

/**
 * ステップ計測結果を出力形式に整形する
 */
public abstract class AbstractFormatter implements ResultFormatter {

	private MessageDefine msgdef = null;

	/**
	 * コンストラクター
	 * メッセージ定義の設定を行う
	 */
	public AbstractFormatter() {
		this.msgdef = new MessageDefine("count.format.");
	}

	/**
	 * ステップ計測結果の出力形式で使用する定形文言を返す
	 * @param key 定形文言の種類を示すキー
	 * @return 表示用定形文言
	 */
	public String getMessageText(final String key) {
		if (key == null) {
			return "";
		}
		return this.msgdef.getMessage(key);
	}

	/**
	 * ステップ計測結果を出力形式に整形したバイト配列を返す
	 * @param results ステップ計測結果の配列
	 * @return 出力形式に整形されたバイト配列
	 */
	public abstract byte[] format(CountResult[] results);
}
