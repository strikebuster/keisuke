package keisuke;

import java.util.Map;

/**
 * Interface of argument map object for keisuke commands.
 */
public interface ArgumentMap {

	/**
	 * コマンド引数設定のオプション項目名に対する引数値を返す
	 * @param key オプション項目名
	 * @return keyに対する引数値
	 */
	String get(String key);

	/**
	 * コマンド引数設定内容を保持する
	 * @param map コマンド引数設定内容を保持するMap
	 */
	void setMap(Map<String, String> map);

	/**
	 * コマンド引数設定内容を保持するMapを返す
	 * @return コマンド引数設定内容を保持するMap
	 */
	Map<String, String> getMap();

	/**
	 * DEBUG用 定義内容の表示
	 */
	void debug();
}
