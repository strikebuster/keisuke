package keisuke.report.property;

import java.util.Map;

import keisuke.MessageMap;
import keisuke.util.ConfigMap;

/**
 * MessageMapインタフェースを実装するクラス
 * Map<String, String>形式でメッセージ定義を保持する
 */
public class MessageMapImpl extends ConfigMap implements MessageMap {

	protected MessageMapImpl(final Map<String, String> map) {
		super();
		this.setMap(map);
	}
}
