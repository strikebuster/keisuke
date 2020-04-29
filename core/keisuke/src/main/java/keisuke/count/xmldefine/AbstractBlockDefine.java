package keisuke.count.xmldefine;

/**
 * 言語ルール定義での開始記号と終了記号からなるブロックを
 * 定義する要素の抽象基底クラス
 */
public abstract class AbstractBlockDefine {

	private String start = null;
	private String end = null;

	/**
	 * デフォルトコンストラクター
	 */
	protected AbstractBlockDefine() { }

	/**
	 * ブロック開始記号を設定する
	 * @param startingMark ブロック開始記号
	 */
	protected void setStart(final String startingMark) {
		this.start = startingMark;
	}

	/**
	 * ブロック開始記号を返す
	 * @return ブロック開始記号
	 */
	public String getStart() {
		return this.start;
	}

	/**
	 * ブロック終了記号を設定する
	 * @param endingMark ブロック終了記号
	 */
	protected void setEnd(final String endingMark) {
		this.end = endingMark;
	}

	/**
	 * ブロック終了記号を返す
	 * @return ブロック終了記号
	 */
	public String getEnd() {
		return this.end;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getStart());
		sb.append(",");
		sb.append(this.getEnd());
		return sb.toString();
	}
}
