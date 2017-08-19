package keisuke;

/**
 * Account element for result of StepCount.
 * @author strikebuster
 *
 */
class CountElement implements ICountElement {

	private String classify = "";
	private int execSteps = 0;
	private int blancSteps = 0;
	private int commentSteps = 0;
	private int sumSteps = 0;
	private int files = 0;

	/**
	 * StepCount結果を集計分類ための分類キーで初期化するコンストラクタ
	 * @param name 集計分類のキー名
	 */
	protected CountElement(final String name) {
		this.classify = name;
	}

	/**
	 * StepCount結果の対象ソースファイルの値一式を設定するコンストラクタ
	 * @param name 集計分類のキー名
	 * @param exe 実行行数
	 * @param blanc 空白行数
	 * @param comment コメント行数
	 * @param sum 合計行数
	 */
	protected CountElement(final String name, final int exe, final int blanc, final int comment, final int sum) {
		this.classify = name;
		this.execSteps = exe;
		this.blancSteps = blanc;
		this.commentSteps = comment;
		this.sumSteps = sum;
		this.files = 1;
	}

	/** {@inheritDoc} */
	public void add(final ICountElement ice) {
		if (ice == null || !(ice instanceof CountElement)) {
			return;
		}
		CountElement ce = (CountElement) ice;
		if (!this.classify.equals(ce.classify)) {
			return;
		}
		this.execSteps += ce.execSteps;
		this.blancSteps += ce.blancSteps;
		this.commentSteps += ce.commentSteps;
		this.sumSteps += ce.sumSteps;
		this.files += ce.files;
	}

	/** {@inheritDoc} */
	public int getValue(final String key) {
		if (key == null) {
			return -1;
		}
		int val;
		if (key.equals(CommonDefine.CP_EXECSTEP)) {
			val = this.execSteps;
		} else if (key.equals(CommonDefine.CP_BLANCSTEP)) {
			val = this.blancSteps;
		} else if (key.equals(CommonDefine.CP_COMMENTSTEP)) {
			val = this.commentSteps;
		} else if (key.equals(CommonDefine.CP_SUMSTEP)) {
			val = this.sumSteps;
		} else if (key.equals(CommonDefine.CP_FILENUM)) {
			val = this.files;
		} else {
			val = -1;
		}
		return val;
	}
}
