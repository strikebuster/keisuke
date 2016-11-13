package keisuke;

public interface ICountElement {

	/** ICountElemの値を足しこむ */
	public void add(ICountElement ce);
	
	/** 集計キーの値を返す */
	public int getValue(String str);
}
