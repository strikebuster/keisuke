package keisuke.count.diff.renderer;

import keisuke.count.diff.DiffFileResult;

/**
 * jXLSを利用するテンプレートへデータを渡すDTOクラス
 * jp.sf.amateras.stepcounter.DiffFileResultとの互換性を持つ
 */
public class DiffCountResultDto extends DiffFileResult implements DiffResultCompatible {

	public DiffCountResultDto(final DiffFileResult result) {
		super(result.nodeName(), result.status(), result.getParent(), result.getDiffStatusLabel());
		this.setFilePath(result.pathFromTop());
		this.setSourceType(result.sourceType());
		this.setSourceCategory(result.sourceCategory());
		this.setSteps(result.addedSteps(), result.deletedSteps());
	}

	/** {@inheritDoc} */
	public String getPath() {
		return this.filePath();
	}

	/** {@inheritDoc} */
	public String getFileType() {
		return this.sourceType();
	}

	/** {@inheritDoc} */
	public String getStatus() {
		return super.getStatusLabel();
	}

	/** {@inheritDoc} */
	public String getCategory() {
		return this.sourceCategory();
	}

	/** {@inheritDoc} */
	public long getAddCount() {
		return this.addedSteps();
	}

	/** {@inheritDoc} */
	public long getDelCount() {
		return this.deletedSteps();
	}

}
