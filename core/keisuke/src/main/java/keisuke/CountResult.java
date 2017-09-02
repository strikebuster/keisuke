package keisuke;

/**
 * Interface of count result for keisuke commands.
 */
public interface CountResult {

	/**
	 * 渡されたCountResultの値を自分に足しこむ
	 * @param result 別のCountResultインスタンス
	 */
	void add(CountResult result);

	/**
	 * 集計キーの値を返す
	 * @param key 集計キー
	 * @return 集計キーに対する集計値
	 */
	int getValue(String key);
}
