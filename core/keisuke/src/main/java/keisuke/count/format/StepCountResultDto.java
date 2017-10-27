package keisuke.count.format;

import keisuke.StepCountResult;

/**
 * jXLSを利用するテンプレートへデータを渡すDTOクラス
 * jp.sf.amateras.stepcounter.CountResultとの互換性を持つ
 */
public class StepCountResultDto extends StepCountResult implements CountResultCompatible {

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

	/** {@inheritDoc} */
	public long getNon() {
		return this.blancSteps();
	}

	/** {@inheritDoc} */
	public long getComment() {
		return this.commentSteps();
	}
}
