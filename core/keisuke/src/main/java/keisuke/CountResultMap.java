package keisuke;

import java.util.Set;
import java.util.Map.Entry;

/**
 * Interface of count result map object for keisuke commands.
 */
public interface CountResultMap {

	/**
	 * 言語に対する集計結果を返す
	 * @param key 言語名
	 * @return keyに対する集計結果
	 */
	CountResult get(String key);

	/**
	 * 言語とその集計結果を登録する
	 * @param key 言語名
	 * @param result keyに対する集計結果
	 */
	void put(String key, CountResult result);

	/**
	 * 指定する言語が登録されているかチェック
	 * @param key 言語名
	 * @return keyが含まれるならtrue
	 */
	boolean containsKey(String key);

	/**
	 * 集計結果内容をSetにして返す
	 * @return 集計結果エントリのSet
	 */
	Set<Entry<String, CountResult>> entrySet();

}
