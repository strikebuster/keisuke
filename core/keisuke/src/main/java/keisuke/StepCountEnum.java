package keisuke;

/**
 * Enumerator for StepCounter Result
 */
public enum StepCountEnum {

	/** Path of a source file */
	FILE_PATH(0),

	/** Extension of a source file */
	FILE_EXTENSION(1),

	/** Category which is written with the rule in a source file */
	FILE_CATEGORY(2),

	/** Number of executable steps in a source file */
	EXEC_STEPS(3),

	/** Number of blanc steps in a source file */
	BLANC_STEPS(4),

	/** Number of comment steps in a source file */
	COMMENT_STEPS(5),

	/** Number of whole steps in a source file */
	SUM_STEPS(6);

	// カラム順番 0..N
	private int index;

	/**
	 * コンストラクタ
	 * @param idx カラム順番を示す整数値
	 */
	StepCountEnum(final int idx) {
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
