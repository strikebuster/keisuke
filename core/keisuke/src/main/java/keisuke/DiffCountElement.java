package keisuke;

/**
 * Account element for result of DiffCount.
 * @author strikebuster
 *
 */
class DiffCountElement implements ICountElement {

	private String classify = "";
	private int addSteps = 0;
	private int deleteSteps = 0;
	private int sumSteps = 0;
	private int files = 0;

	/**
	 * DiffCount結果を集計分類ための分類キーで初期化するコンストラクタ
	 * @param name 集計分類のキー名
	 */
	protected DiffCountElement(final String name) {
		this.classify = name;
		this.addSteps = 0;
		this.deleteSteps = 0;
		this.sumSteps = 0;
		this.files = 0;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの値一式を設定するコンストラクタ
	 * @param name 集計分類のキー名
	 * @param add diff追加行数
	 * @param del diff削除行数
	 */
	protected DiffCountElement(final String name, final int add, final int del) {
		this.classify = name;
		this.addSteps = add;
		this.deleteSteps = del;
		this.sumSteps = add - del;
		this.files = 1;
	}

	/** {@inheritDoc} */
	public void add(final ICountElement ice) {
		if (ice == null || !(ice instanceof DiffCountElement)) {
			return;
		}
		DiffCountElement de = (DiffCountElement) ice;
		if (!this.classify.equals(de.classify)) {
			return;
		}
		this.addSteps += de.addSteps;
		this.deleteSteps += de.deleteSteps;
		this.sumSteps += de.sumSteps;
		this.files += de.files;
	}

	/** {@inheritDoc} */
	public int getValue(final String key) {
		if (key == null) {
			return -1;
		}
		int val;
		if (key.equals(CommonDefine.DP_ADDSTEP)) {
			val = this.addSteps;
		} else if (key.equals(CommonDefine.DP_DELSTEP)) {
			val = this.deleteSteps;
		} else if (key.equals(CommonDefine.DP_SUMSTEP)) {
			val = this.sumSteps;
		} else if (key.equals(CommonDefine.DP_FILENUM)) {
			val = this.files;
		} else {
			val = -1;
		}
		return val;
	}
}
