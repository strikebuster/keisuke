package org.jenkinsci.plugins.keisuke;

import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;

import hudson.model.Run;
import hudson.util.DataSetBuilder;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

/**
 * Graph for diff counting deleted steps results of StepCountBuildAction.
 */
class DiffCountDeletedGraph extends AbstractDiffCountGraph {

	private boolean buildAxisVisible = true;

	DiffCountDeletedGraph(final StepCountBuildAction obj, final Calendar timestamp,
			final int width, final int height, final String relPath, final boolean visible) {
		super(obj, timestamp, width, height, relPath);
		this.buildAxisVisible = visible;
	}

	@Override
	protected JFreeChart createGraph() {
		return this.reconfigure(super.createGraph());
	}

	private JFreeChart reconfigure(final JFreeChart chart) {
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT); // Domain(X)軸を上に表示
		NumberAxis stepAxis = (NumberAxis) plot.getRangeAxis();
		stepAxis.setInverted(true); // Range(Y)軸を逆順に表示
		CategoryAxis buildAxis = plot.getDomainAxis();
		buildAxis.setVisible(this.buildAxisVisible); // Domain(X)軸のラベルを表示切替
		return chart;
	}

	@Override
	protected String stepsAxisLabel() {
		return "Deleted Steps";
	}

	@Override
	protected DataSetBuilder<String, NumberOnlyBuildLabel> createDataSetOfDiff() {
		return this.createDataSetOfDiffDeleted();
	}

	private DataSetBuilder<String, NumberOnlyBuildLabel> createDataSetOfDiffDeleted() {
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
					dsb.add(result.getDeletedSum(), unit, label);
				}
			}
		}
		return dsb;
	}

	@Override
	protected AbstractDiffCountGraphRenderer createRenderer() {
		return new DiffCountDeletedGraphRenderer(this.relativePath());
	}
}
