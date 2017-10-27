package keisuke.report;

/**
 * Interface of count result for keisuke commands.
 */
public interface CountResultForReport {

	/**
	 * 渡されたCountResultの値を自分に足しこむ
	 * @param result 別のCountResultインスタンス
	 */
	void add(CountResultForReport result);

	/**
	 * 集計キーの値を返す
	 * @param key 集計キー
	 * @return 集計キーに対する集計値
	 */
	long getValue(String key);
}
