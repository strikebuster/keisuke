package keisuke;

import java.util.Map;

/**
 * Interface of message map object for keisuke commands.
 */
public interface MessageMap {

	/**
	 * メッセージ定義の項目キーに対するメッセージを返す
	 * @param key メッセージ定義項目キー
	 * @return keyに対するメッセージ文字列
	 */
	String get(String key);

	/**
	 * メッセージ定義内容を保持する
	 * @param map メッセージ定義内容を保持するMap
	 */
	void setMap(Map<String, String> map);

	/**
	 * DEBUG用 定義内容の表示
	 */
	void debug();
}
