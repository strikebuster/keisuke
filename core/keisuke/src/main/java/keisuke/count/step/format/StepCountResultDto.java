package keisuke.count.step.format;

import keisuke.StepCountResult;

/**
 * jXLSを利用するテンプレートへデータを渡すDTOクラス
 * jp.sf.amateras.stepcounter.CountResultとの互換性を持つ
 */
public class StepCountResultDto extends StepCountResult implements CountResultCompatible {
	private static final long serialVersionUID = 1L; // since ver.2.0.1

	public StepCountResultDto(final StepCountResult result) {
		if (result != null) {
			this.setFilePath(result.filePath());
			this.setSourceType(result.sourceType());
			this.setSourceCategory(result.sourceCategory());
			this.setSteps(result.execSteps(), result.blancSteps(), result.commentSteps());
		}
	}

	/** {@inheritDoc} */
	public String getFileName() {
		return this.filePath();
	}

	/** {@inheritDoc} */
	public String getFileType() {
		return this.sourceType();
	}

	/** {@inheritDoc} */
	public String getCategory() {
		return this.sourceCategory();
	}

	/** {@inheritDoc} */
	public long getStep() {
		return this.execSteps();
	}

	/**
	 * 計測対象のソースコードのコード行数を返す
	 * @return コード行数
	 */
	public long getCode() {
		return this.execSteps();
	}

	/** {@inheritDoc} */
	public long getNon() {
		return this.blancSteps();
	}

	/**
	 * 計測対象のソースコードの空白行数を返す
	 * @return 空白行数
	 */
	public long getBlank() {
		return this.blancSteps();
	}

	/** {@inheritDoc} */
	public long getComment() {
		return this.commentSteps();
	}

	/**
	 * 計測対象のソースコードの全行数を返す
	 * @return 全行数
	 */
	public long getSum() {
		return (this.execSteps() + this.blancSteps() + this.commentSteps());
	}
}
