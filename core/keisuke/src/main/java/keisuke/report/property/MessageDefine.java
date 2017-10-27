package keisuke.report.property;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import keisuke.MessageMap;
import keisuke.util.LogUtil;

/**
 * Messages to be defined for keisuke command.
 * It uses java.util.ResourceBundle for adjusting to location.
 * "keisuke/message[_xx].properties" will be loaded.
 */
public class MessageDefine {

	private Map<String, String> messageMap = null;

	/**
	 * プレフィックスが一致するメッセージ定義を取り込むコンストラクタ
	 * @param prefix keisukeの各コマンド用の定義プレフィックス名
	 */
	public MessageDefine(final String prefix) {
		this.messageMap = new HashMap<String, String>();
		String[] prefixes = {prefix};
		this.loadMessageMap(prefixes);
	}

	/**
	 * 配列中のプレフィックスが一致するメッセージ定義を取り込むコンストラクタ
	 * @param prefixes keisukeコマンド用の定義プレフィックス名配列
	 */
	public MessageDefine(final String[] prefixes) {
		this.messageMap = new HashMap<String, String>();
		for (int i = 0; i < prefixes.length; i++) {
			this.loadMessageMap(prefixes);
		}
	}

	private void loadMessageMap(final String[] prefixes) {
		ResourceBundle bundle = null;
		String key = null;
		int prefixlen = prefixes.length;
        try {
        	//LogUtil.debugLog(("Locale = " + Locale.getDefault());
            bundle = ResourceBundle.getBundle("keisuke/message");
        } catch (MissingResourceException ex) {
        	throw new RuntimeException("!! Not found message[_xx].properties");
        }
        try {
        	Enumeration<?> en = bundle.getKeys();
    		while (en.hasMoreElements()) {
    			key = (String) en.nextElement();
    			for (int i = 0; i < prefixlen; i++) {
	    			if (key.startsWith(prefixes[i])) {
	    				String msg = bundle.getString(key);
	        			this.messageMap.put(key, msg);
	    			}
    			}
        	}
        } catch (MissingResourceException e) {
        	LogUtil.errorLog("Not found key(" + key + ") in message[_xx].properties");
        	throw new RuntimeException(e);
        }
	}

	/**
	 * 設定したメッセージ定義マップを返す
	 * @return メッセージ定義マップ
	 */
	public MessageMap getMessageMap() {
		return new MessageMapImpl(this.messageMap);
	}

}
