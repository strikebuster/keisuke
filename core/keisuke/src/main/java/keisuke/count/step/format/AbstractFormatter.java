package keisuke.count.step.format;

import static keisuke.count.step.format.FormatConstant.*;

import keisuke.MessageMap;
import keisuke.StepCountResult;
import keisuke.count.step.Formatter;
import keisuke.report.property.MessageDefine;

/**
 * ステップ計測結果を出力形式に整形する
 */
public abstract class AbstractFormatter implements Formatter {

	private MessageMap messageMap;

	/**
	 * コンストラクター
	 * メッセージ定義の設定を行う
	 */
	public AbstractFormatter() {
		this.messageMap = new MessageDefine(MSG_COUNT_FMT_PREFIX).getMessageMap();
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
		return this.messageMap.get(key);
	}

	/**
	 * ステップ計測結果を出力形式に整形したバイト配列を返す
	 * @param results ステップ計測結果の配列
	 * @return 出力形式に整形されたバイト配列
	 */
	public abstract byte[] format(StepCountResult[] results);
}
