package org.jenkinsci.plugins.keisuke;

import java.util.Map;

import org.jfree.data.category.CategoryDataset;

import hudson.util.StackedAreaRenderer2;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

/**
 * Renderer for StepCountGraph.
 */
class StepCountGraphRenderer extends StackedAreaRenderer2 {
	private static final long serialVersionUID = 0L;  // since ver.2.0.0

	private String ancestorPath; // subpath about this project's URL

	StepCountGraphRenderer(final String path) {
		super();
		this.ancestorPath = path;
	}

	@Override
	public String generateURL(final CategoryDataset dataset, final int row, final int column) {
		NumberOnlyBuildLabel label = (NumberOnlyBuildLabel) dataset.getColumnKey(column);
		String unit = (String) dataset.getRowKey(row);
		StringBuilder sb = new StringBuilder();
		sb.append(StepCountProjectAction.getResultUrlOf(this.ancestorPath, label.getRun()))
			.append("#").append(unit);
		return sb.toString();
	}

	@Override
	public String generateToolTip(final CategoryDataset dataset, final int row, final int column) {
		String unit = (String) dataset.getRowKey(row);
		NumberOnlyBuildLabel label = (NumberOnlyBuildLabel) dataset.getColumnKey(column);
		StepCountBuildAction action = label.getRun().getAction(StepCountBuildAction.class);
		Map<String, BuildResult> map = action.getStepsMap();
		BuildResult result = map.get(unit);
		if (result == null) {
			return Messages.unknown();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(unit).append("]")
			.append(" ").append(Messages.build()).append(" ")
			.append(label.getRun().getDisplayName())
			.append(" ").append(Messages.sum()).append(":")
			.append(result.getStepsSum())
			.append(" ").append(Messages.codes()).append(":")
			.append(result.getCodesSum())
			.append(" ").append(Messages.comments()).append(":")
			.append(result.getCommentsSum())
			.append(" ").append(Messages.blanks()).append(":")
			.append(result.getBlanksSum());
		return sb.toString();

	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof StepCountGraphRenderer)) {
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
