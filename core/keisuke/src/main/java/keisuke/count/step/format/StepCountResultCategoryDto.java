package keisuke.count.step.format;

/**
 * jXLSを利用するテンプレートへデータを渡すDTOクラス
 * jp.sf.amateras.stepcounter.CategoryStepDtoとの互換性を持つ
 */
public class StepCountResultCategoryDto implements CategoryStepDtoCompatible {

	private String sourceCategory = null;
	private long execSteps = 0;
	private long blancSteps = 0;
	private long commentSteps = 0;

	public StepCountResultCategoryDto() { }

	/** {@inheritDoc} */
	public String getCategory() {
		return this.sourceCategory;
	}

	/** {@inheritDoc} */
	public long getStep() {
		return this.execSteps;
	}

	/**
	 * 計測対象のソースコードのコード行数を返す
	 * @return コード行数
	 */
	public long getCode() {
		return this.execSteps;
	}

	/** {@inheritDoc} */
	public long getNone() {
		return this.blancSteps;
	}

	/**
	 * 計測対象のソースコードの空白行数を返す
	 * @return 空白行数
	 */
	public long getBlank() {
		return this.blancSteps;
	}

	/** {@inheritDoc} */
	public long getComment() {
		return this.commentSteps;
	}

	/**
	 * 計測対象のソースコードの全行数を返す
	 * @return 全行数
	 */
	public long getSum() {
		return (this.execSteps + this.blancSteps + this.commentSteps);
	}

	/**
	 * カテゴリを設定する
	 * @param category カテゴリ文字列
	 */
	protected void setCategory(final String category) {
		this.sourceCategory = category;
	}

	/**
	 * 保持する3種類のステップ数に指定されたステップ数をそれぞれ足し込む
	 * @param exec 実行ステップ数
	 * @param blanc 空白ステップ数
	 * @param comment コメントステップ数
	 */
	protected void addResultSteps(final long exec, final long blanc, final long comment) {
		this.execSteps += exec;
		this.blancSteps += blanc;
		this.commentSteps += comment;
	}

}
