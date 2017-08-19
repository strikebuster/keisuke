package keisuke;

/**
 * Interface of account result for keisuke commands.
 * @author strikebuster
 *
 */
interface ICountElement {

	/**
	 * 渡されたAccountElementの値を自分に足しこむ
	 * @param ce 他のICount
	 */
	void add(ICountElement ce);

	/**
	 * 集計キーの値を返す
	 * @param str 集計キー
	 * @return 集計キーに対する集計値
	 */
	int getValue(String str);
}
