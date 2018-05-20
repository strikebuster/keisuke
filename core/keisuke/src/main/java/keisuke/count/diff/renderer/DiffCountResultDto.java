package keisuke.count.diff.renderer;

import keisuke.DiffStatusLabels;
import keisuke.count.diff.DiffFileResult;

/**
 * jXLSを利用するテンプレートへデータを渡すDTOクラス
 * jp.sf.amateras.stepcounter.diffcount.object.DiffFileResultとの互換性を持つ
 */
public class DiffCountResultDto extends DiffFileResult implements DiffResultCompatible {

	private String statusText = null;

	public DiffCountResultDto(final DiffFileResult result, final DiffStatusLabels statusLabels) {
		super(result.nodeName(), result.status(), result.getParent());
		this.setFilePath(result.pathFromTop());
		this.setSourceType(result.sourceType());
		this.setSourceCategory(result.sourceCategory());
		this.setSteps(result.addedSteps(), result.deletedSteps());
		this.statusText = statusLabels.getLabelOf(result.status());
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
		return this.statusText;
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
