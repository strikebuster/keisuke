package keisuke.count.diff.renderer;

import keisuke.DiffStatusEnum;
import keisuke.DiffStatusLabels;
import keisuke.count.PathStyleEnum;
import keisuke.count.diff.DiffFileResult;

/**
 * jXLSを利用するテンプレートへデータを渡すDTOクラス
 * jp.sf.amateras.stepcounter.diffcount.object.DiffFileResultとの互換性を持つ
 */
public class DiffCountResultDto extends DiffFileResult implements DiffResultCompatible {
	private static final long serialVersionUID = 1L; // since ver.2.0.1

	private String statusText = null;
	private PathStyleEnum pathStyle;

	public DiffCountResultDto(final DiffFileResult result, final DiffStatusLabels statusLabels,
			final PathStyleEnum style) {
		super(result.nodeName(), result.status(), result.getParent());
		this.setSourceType(result.sourceType());
		this.setSourceCategory(result.sourceCategory());
		this.setSteps(result.addedSteps(), result.deletedSteps());
		if (result.isUnsupported()) {
			this.statusText = statusLabels.getLabelOf(DiffStatusEnum.UNSUPPORTED);
		} else {
			this.statusText = statusLabels.getLabelOf(result.status());
		}
		this.pathStyle = style;
	}

	/** {@inheritDoc} */
	public String getPath() {
		return this.filePath(this.pathStyle);
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
