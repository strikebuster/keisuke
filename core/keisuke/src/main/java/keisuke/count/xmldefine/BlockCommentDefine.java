package keisuke.count.xmldefine;

/**
 * 言語ルール定義でブロックコメント定義を保持するクラス
 */
public class BlockCommentDefine extends AbstractBlockDefine {

	private boolean nest = false;

	/**
	 * デフォルトコンストラクター
	 */
	protected BlockCommentDefine() { }

	/**
	 * ブロック定義がネスト可能であると設定する
	 * このメソッドが呼びだされるまでネスト可能ではない定義が
	 * デフォルト値として設定されている
	 */
	protected void setNestTrue() {
		this.nest = true;
	}

	/**
	 * ネスト可能であるかを返す
	 * @return 可能ならばtrue、そうでなければfalse
	 */
	public boolean getNest() {
		return this.nest;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append(",");
		sb.append(this.getNest());
		return sb.toString();
	}
}
