package org.jenkinsci.plugins.keisuke;

import java.awt.Color;
//import java.io.PrintStream;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import hudson.model.Run;
import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import hudson.util.ShiftedCategoryAxis;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

/**
 * Graph for step counting results of StepCountBuildAction.
 */
class StepCountGraph extends Graph {

	private static final String BUILD_AXIS_LABEL = null;
	private static final String STEP_AXIS_LABEL = "Steps";
	private static final double MARGIN_SIZE = 5.0;
	private StepCountBuildAction buildAction; // last build result on this graph
	private String relativePath; // subpath of this project's URL
	private DisplayStepKindEnum displayStepKind = null;
	//private transient PrintStream syslogger = System.out;

	StepCountGraph(final StepCountBuildAction obj, final Calendar timestamp,
			final int width, final int height, final String relPath) {
		super(timestamp, width, height);
		this.buildAction = obj;
		this.relativePath = relPath;
		this.displayStepKind = obj.getDisplayStepKindEnum();
	}

	@Override
	protected JFreeChart createGraph() {
		CategoryDataset dataSet = this.createDataSetOfSteps().build();
		JFreeChart chart = ChartFactory.createLineChart(
				null, // title
				null, // X axis label
				STEP_AXIS_LABEL, // Y axis label
				dataSet, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);
		// グラフの設定
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRenderer(new StepCountGraphRenderer(this.relativePath));
		plot.setInsets(new RectangleInsets(0, 0, 0, MARGIN_SIZE));
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(null);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		// X軸：ビルドの設定
		CategoryAxis buildAxis = new ShiftedCategoryAxis(BUILD_AXIS_LABEL);
		buildAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		buildAxis.setLowerMargin(0.0);
		buildAxis.setUpperMargin(0.0);
		buildAxis.setCategoryMargin(0.0);
		plot.setDomainAxis(buildAxis);
		// Y軸：ステップ数の設定
		NumberAxis stepAxis = (NumberAxis) plot.getRangeAxis();
		stepAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		stepAxis.setAutoRange(true);

		// 背景色
		chart.setBackgroundPaint(Color.GRAY);
		// 凡例
		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		return chart;
	}

	private DataSetBuilder<String, NumberOnlyBuildLabel> createDataSetOfSteps() {
		DataSetBuilder<String, NumberOnlyBuildLabel> dsb =
				new DataSetBuilder<String, NumberOnlyBuildLabel>();
		for (StepCountBuildAction action = this.buildAction;
				action != null; action = action.getPreviousBuildAction()) {
			NumberOnlyBuildLabel label =
					new NumberOnlyBuildLabel((Run<?, ?>) action.getOwnerBuild());
			Map<String, BuildResult> map = action.getStepsMap();
			for (Entry<String, BuildResult> entry : map.entrySet()) {
				dsb.add(this.getStepsForDisplayStepKind(entry.getValue()), entry.getKey(), label);
			}
		}
		return dsb;
	}

	private long getStepsForDisplayStepKind(final BuildResult result) {
		if (this.displayStepKind == null || this.displayStepKind.isCodeOnly()) {
			return result.getCodesSum();
		} else if (this.displayStepKind.isWithComment()) {
			return result.getCodesSum() + result.getCommentsSum();
		} else {
			return result.getStepsSum();
		}
	}

}
