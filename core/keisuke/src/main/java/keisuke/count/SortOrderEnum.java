package keisuke.count;

/**
 * Enumerator for sort orders about paths of counted source files.
 */
public enum SortOrderEnum {

	/** enable to sort as code order */
	ON("on"),
	/** enable to sort as os file order */
	OS("os"),
	/** disable to sort */
	OFF("off"),
	/** enable to sort as node order node type and code order */
	NODE("node");

	// ソート順タイプ文字列値
	private String sortType;

	/**
	 * コンストラクタ
	 * @param type ソート順タイプ名
	 */
	SortOrderEnum(final String type) {
		this.sortType = type;
	}

	/**
	 * ソート順タイプの値を返す
	 * @return ソート順タイプ名
	 */
	public String value() {
		return this.sortType;
	}
}
