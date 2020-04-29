package org.jenkinsci.plugins.keisuke;

import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

import hudson.util.ChartUtil.NumberOnlyBuildLabel;

/**
 * Abstract class as a Renderer for DiffCountGraph.
 */
abstract class AbstractDiffCountGraphRenderer extends StackedBarRenderer
	implements CategoryToolTipGenerator, CategoryURLGenerator {

	private static final long serialVersionUID = 0L;  // since ver.2.0.0
	private String diffLabel = "";
	private String ancestorPath; // subpath about this project's URL

	AbstractDiffCountGraphRenderer(final String path) {
		super();
		this.initDiffKind();
		this.ancestorPath = path;
		this.setItemMargin(0.0);
		this.setBaseItemURLGenerator(this);
		this.setBaseToolTipGenerator(this);
	}

	/**
	 * initialize as implemented class.
	 */
	abstract void initDiffKind();

	/**
	 * Sets label used tool tips.
	 * which is this "added" or "deleted".
	 * @param label label used tool tips
	 */
	protected void setDiffLabel(final String label) {
		this.diffLabel = label;
	}

	/**
	 * Gets URL path for build.
	 * @param dataset data of diff count result.
	 * @param row counting unit index.
	 * @param column build index.
	 * @return URL path for build result.
	 */
	@Override
	public String generateURL(final CategoryDataset dataset, final int row, final int column) {
		NumberOnlyBuildLabel label = (NumberOnlyBuildLabel) dataset.getColumnKey(column);
		String unit = (String) dataset.getRowKey(row);
		StringBuilder sb = new StringBuilder();
		sb.append(StepCountProjectAction.getResultUrlOf(this.ancestorPath, label.getRun()))
			.append("#").append(unit);
		return sb.toString();
	}

	/**
	 * Gets URL path for build.
	 * @param dataset data of diff count added result.
	 * @param row counting unit index.
	 * @param column build index.
	 * @return URL path for build result.
	 */
	@Override
	public String generateToolTip(final CategoryDataset dataset, final int row, final int column) {
		String unit = (String) dataset.getRowKey(row);
		NumberOnlyBuildLabel label = (NumberOnlyBuildLabel) dataset.getColumnKey(column);
		long steps = dataset.getValue(row, column).longValue();
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(unit).append("]");
		sb.append(" ").append(Messages.build()).append(" ")
				.append(label.toString());
		sb.append(" ").append(this.diffLabel).append(":").append(steps);
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof AbstractDiffCountGraphRenderer)) {
			return false;
		}
		if (this.ancestorPath.hashCode() == obj.hashCode()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.ancestorPath == null) {
			return 0;
		}
		return this.ancestorPath.hashCode();
	}
}
