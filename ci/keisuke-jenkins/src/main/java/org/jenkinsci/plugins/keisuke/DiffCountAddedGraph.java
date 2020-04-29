package org.jenkinsci.plugins.keisuke;

import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import hudson.model.Run;
import hudson.util.DataSetBuilder;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

/**
 * Graph for diff counting added steps results of StepCountBuildAction.
 */
class DiffCountAddedGraph extends AbstractDiffCountGraph {

	DiffCountAddedGraph(final StepCountBuildAction obj, final Calendar timestamp,
			final int width, final int height, final String relPath) {
		super(obj, timestamp, width, height, relPath);
	}

	@Override
	protected String stepsAxisLabel() {
		return "Added Steps";
	}

	@Override
	protected DataSetBuilder<String, NumberOnlyBuildLabel> createDataSetOfDiff() {
		return this.createDataSetOfDiffAdded();
	}

	private DataSetBuilder<String, NumberOnlyBuildLabel> createDataSetOfDiffAdded() {
		DataSetBuilder<String, NumberOnlyBuildLabel> dsb =
				new DataSetBuilder<String, NumberOnlyBuildLabel>();

		for (StepCountBuildAction action = this.buildAction();
				action != null; action = action.getPreviousBuildAction()) {

			NumberOnlyBuildLabel label =
					new NumberOnlyBuildLabel((Run<?, ?>) action.getOwnerBuild());
			Map<String, BuildResult> map = action.getStepsMap();
			for (Entry<String, BuildResult> entry : map.entrySet()) {
				String unit = entry.getKey();
				BuildResult result = entry.getValue();
				if (result.isDiffExist()) {
					dsb.add(result.getAddedSum(), unit, label);
				}
			}
		}
		return dsb;
	}

	@Override
	protected AbstractDiffCountGraphRenderer createRenderer() {
		return new DiffCountAddedGraphRenderer(this.relativePath());
	}
}
