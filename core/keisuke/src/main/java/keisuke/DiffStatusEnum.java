package keisuke;

/**
 * Enumerator for DiffCounter Status
 */
public enum DiffStatusEnum {

	/** 新規追加ファイルのグループ */
	ADDED(0),

	/** 修正ファイルのグループ */
	MODIFIED(1),

	/** 廃止ファイルのグループ */
	DROPED(2),

	/** 変更なしファイルのグループ */
	UNCHANGED(3),

	/** 対象外ファイルのグループ */
	UNSUPPORTED(4);

	// カラム順番 0..N
	private int index;

	/**
	 * コンストラクタ
	 * @param idx カラム順番を示す整数値
	 */
	DiffStatusEnum(final int idx) {
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
