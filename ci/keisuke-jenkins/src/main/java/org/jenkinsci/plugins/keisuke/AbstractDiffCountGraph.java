package org.jenkinsci.plugins.keisuke;

import java.awt.Color;
import java.util.Calendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;


import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

/**
 * Abstract class as a Graph for diff counting added steps results of StepCountBuildAction.
 */
abstract class AbstractDiffCountGraph extends Graph {

	private static final double MARGIN_SIZE = 5.0;
	private static final double CATEGORY_MARGIN_SIZE = 0.2;
	private StepCountBuildAction buildAction; // last build result on this graph
	private String relativePath; // subpath of this project's URL

	AbstractDiffCountGraph(final StepCountBuildAction obj, final Calendar timestamp,
			final int width, final int height, final String relPath) {
		super(timestamp, width, height);
		this.buildAction = obj;
		this.relativePath = relPath;
	}

	/**
	 * Gets this build action.
	 * @return instance of StepCountBuildAction.
	 */
	protected StepCountBuildAction buildAction() {
		return this.buildAction;
	}

	/**
	 * Gets this relative path.
	 * @return url relative path.
	 */
	protected String relativePath() {
		return this.relativePath;
	}

	/**
	 * Creates graph for diff result.
	 * @return instance of JFreeChart.
	 */
	protected JFreeChart createGraph() {
		JFreeChart chart = ChartFactory.createStackedBarChart(
				null, // title
				null, // X axis label
				this.stepsAxisLabel(), // Y axis label
				this.createDataSetOfDiff().build(), // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);
		// グラフの設定
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRenderer(this.createRenderer());
		plot.setInsets(new RectangleInsets(0, 0, 0, MARGIN_SIZE)); // top,left,bottom,right
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlinePaint(null);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		// X軸：ビルドの設定
		CategoryAxis buildAxis = new CategoryAxis();
		//buildAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		buildAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		buildAxis.setLowerMargin(0.0);
		buildAxis.setUpperMargin(0.0);
		buildAxis.setCategoryMargin(CATEGORY_MARGIN_SIZE);
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

	/**
	 * Gets steps-axis label.
	 * @return label.
	 */
	abstract String stepsAxisLabel();

	/**
	 * Creates dataset for graph.
	 * @return instance of DataSetBuilder.
	 */
	abstract DataSetBuilder<String, NumberOnlyBuildLabel> createDataSetOfDiff();

	/**
	 * Creates renderer.
	 * @return instance of AbstractDiffCountGraphRenderer.
	 */
	abstract AbstractDiffCountGraphRenderer createRenderer();
}
