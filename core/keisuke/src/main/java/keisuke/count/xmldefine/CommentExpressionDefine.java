package keisuke.count.xmldefine;

/**
 * 言語ルール定義で関数言語のコメント式定義を保持するクラス
 */
public class CommentExpressionDefine {
	private String start = null;

	/**
	 * デフォルトコンストラクター
	 */
	protected CommentExpressionDefine() { }

	/**
	 * コメント式開始記号を設定する
	 * @param startingMark コメント式開始記号
	 */
	protected void setStart(final String startingMark) {
		this.start = startingMark;
	}

	/**
	 * コメント式開始記号を返す
	 * @return コメント式開始記号
	 */
	public String getStart() {
		return this.start;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getStart());
		return sb.toString();
	}
}
