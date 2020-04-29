package keisuke;

/**
 * Enumerator for DiffCount Result
 */
public enum DiffCountEnum {

	/** Path of a source file */
	FILE_PATH(0),

	/** Type of a source file */
	SOURCE_TYPE(1),

	/** Category which is written with the rule in a source file */
	SOURCE_CATEGORY(2),

	/** Number of executable steps in a source file */
	DIFF_STATUS(3),

	/** Number of blanc steps in a source file */
	ADDED_STEPS(4),

	/** Number of comment steps in a source file */
	DELETED_STEPS(5);

	// カラム順番 0..N
	private int index;

	/**
	 * コンストラクタ
	 * @param idx カラム順番を示す整数値
	 */
	DiffCountEnum(final int idx) {
		this.index = idx;
	}

	/**
	 * カラム順番を返す
	 * @return 順番（先頭が０で始まる）
	 */
	public int index() {
		return this.index;
	}
}
