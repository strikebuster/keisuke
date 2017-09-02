package keisuke.util;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Map<String, String>形式で設定を保持するクラスの基底クラス
 */
public abstract class ConfigMap {

	private Map<String, String> configMap = null;

	protected ConfigMap() { }

	/**
	 * 設定定義の項目キーに対する設定値を返す
	 * @param key 設定定義項目キー
	 * @return keyに対する設定値文字列
	 */
	public String get(final String key) {
		return this.configMap.get(key);
	}

	/**
	 * 定義内容を保持する
	 * @param map 定義内容を保持するMap
	 */
	public void setMap(final Map<String, String> map) {
		this.configMap = map;
	}

	/**
	 * 定義内容を保持するMapを返す
	 * @return map 定義内容を保持するMap
	 */
	public Map<String, String> getMap() {
		return this.configMap;
	}

	/**
	 * DEBUG用 定義内容の表示
	 */
	public void debug() {
		for (Entry<String, String> entry : configMap.entrySet()) {
			String key = entry.getKey();
			String data = entry.getValue();
			System.out.println("[DEBUG] " + key + ": " + data);
		}
	}
}
