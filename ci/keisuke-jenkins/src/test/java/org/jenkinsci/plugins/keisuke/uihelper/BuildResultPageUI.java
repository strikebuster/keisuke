package org.jenkinsci.plugins.keisuke.uihelper;

import static keisuke.util.TestUtil.contentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.contentWithoutTypeAndCategoryOf;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.convertToTableArrayFromDiffTableBody;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.convertToTableArrayFromStepTableBody;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.detailDiffLabelOf;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.detailStepLabelOf;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.lengthOfDetailDiffLabels;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.lengthOfDetailStepLabels;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.lengthOfSummaryDiffLabels;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.lengthOfSummaryStepLabels;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.rowLabelOfChanged;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.rowLabelOfCounted;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.rowLabelOfUnchanged;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.rowLabelOfUncounted;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.rowLabelOfUnsupported;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.summaryDiffLabelOf;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.summaryStepLabelOf;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfValueTrFromLabelTr;

import java.net.URL;

import org.jvnet.hudson.test.JenkinsRule;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;

/**
 * UI utility for jenkins build result page about KeisukePublisher.
 */
public class BuildResultPageUI extends AbstractReportPageUI {

	private HtmlDivision mainDiv = null;

	/**
	 * Constructs an instance.
	 * @param client JenkinsRule.WebClient for jenkins web ui.
	 * @param page HtmlPage of jenkins web ui.
	 */
	public BuildResultPageUI(final JenkinsRule.WebClient client, final HtmlPage page) {
		super(client, page);
	}
	/**
	 * Constructs an instance.
	 * @param client JenkinsRule.WebClient for jenkins web ui.
	 * @param jobName name of jenkins job.
	 */
	public BuildResultPageUI(final JenkinsRule.WebClient client, final String jobName) {
		super(client, jobName);
	}

	/** {@inheritDoc}	 */
	@Override
	protected String getPageUrlPath(final String jobName) {
		return "job/" + jobName + "/keisuke";
	}

	/**
	 * Open build result of KeisuePublisher page.
	 * @param jobName job name.
	 */
	public void openBuildResultPage(final String jobName) {
		this.openPage(jobName);
	}

	/** {@inheritDoc}	 */
	@Override
	public HtmlDivision findDivOfMainPanel() {
		this.mainDiv = super.findDivOfMainPanel();
		return this.mainDiv;
	}

	/**
	 * Finds &lt;h1&gt; for sum all counting units.
	 * @return HtmlHeading1 instance.
	 */
	public HtmlHeading1 findHeadingOfAllSummary() {
		String h1Xpath = this.mainDiv.getCanonicalXPath() + "/div[1]/h1";
		return (HtmlHeading1) this.getPage().getFirstByXPath(h1Xpath);
	}

	private HtmlTableRow findTrowOfStepSummaryTableLabel() {
		String trXpath = this.mainDiv.getCanonicalXPath() + "/table/tbody[1]/tr";
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitStepSummaryTableLabel(final HtmlDivision unitDiv) {
		String trXpath = unitDiv.getCanonicalXPath() + "/table[1]/tbody[1]/tr";
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	/**
	 * Asserts that TableRow's value matches the labels of summary steps table.
	 * This TableRow is summary table in result page.
	 */
	public void assertStepSummaryTableLabel() {
		this.assertStepSummaryTableLabel(null);
	}

	/**
	 * Asserts that TableRow's value matches the labels of summary steps table.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 */
	public void assertStepSummaryTableLabel(final HtmlDivision unitDiv) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfStepSummaryTableLabel();
		} else {
			trow = this.findTrowOfUnitStepSummaryTableLabel(unitDiv);
		}
		System.out.println("[TEST] Summary Step Label Row Xpath:" + trow.getCanonicalXPath());
		for (int i = 1; i < lengthOfSummaryStepLabels(); i++) {
			String tdXpath = trow.getCanonicalXPath() + "/td[" + i + "]";
			HtmlTableDataCell labelCell = this.getPage().getFirstByXPath(tdXpath);
			System.out.println("[TEST] step Data Label:" + labelCell.getTextContent());
			String label = labelCell.getTextContent();
			assertThat(label, startsWith(summaryStepLabelOf(i)));
		}
	}

	private static final int ROW_INDEX_FOR_UNCOUNTED = 2;

	private HtmlTableRow findTrowOfStepSummaryTableValue() {
		HtmlTableRow trow = this.findTrowOfStepSummaryTableLabel();
		String trXpath = getXPathOfValueTrFromLabelTr(trow);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitStepSummaryTableValue(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitStepSummaryTableLabel(unitDiv);
		String trXpath = getXPathOfValueTrFromLabelTr(trow);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfStepSummaryTableUncount() {
		HtmlTableRow trow = this.findTrowOfStepSummaryTableLabel();
		String trXpath = getXPathOfValueTrFromLabelTr(trow, ROW_INDEX_FOR_UNCOUNTED);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitStepSummaryTableUncount(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitStepSummaryTableLabel(unitDiv);
		String trXpath = getXPathOfValueTrFromLabelTr(trow, ROW_INDEX_FOR_UNCOUNTED);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private void assertTableRowValues(final HtmlTableRow trow, final String expectedLabel, final int[] expected) {
		// status label
		String tdXpath = trow.getCanonicalXPath() + "/td[1]";
		HtmlTableDataCell valueCell = (HtmlTableDataCell) this.getPage().getFirstByXPath(tdXpath);
		String label = valueCell.getTextContent();
		System.out.println("[TEST] table Row Label:" + label);
		assertThat(label, is(equalTo(expectedLabel)));
		// data values
		for (int i = 0; i < expected.length; i++) {
			tdXpath = trow.getCanonicalXPath() + "/td[" + Integer.toString(i + 2) + "]";
			valueCell = (HtmlTableDataCell) this.getPage().getFirstByXPath(tdXpath);
			System.out.println("[TEST] table Data Value:" + valueCell.getTextContent());
			int value = parseIntOrHyphen(valueCell.getTextContent());
			assertThat(value, is(equalTo(expected[i])));
		}
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of summary steps.
	 * This TableRow is summary table in result page.
	 * @param expected array of expected numbers.
	 */
	public void assertStepSummaryTableValue(final int[] expected) {
		this.assertStepSummaryTableValue(null, expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of summary steps.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 * @param expected array of expected numbers.
	 */
	public void assertStepSummaryTableValue(final HtmlDivision unitDiv, final int[] expected) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfStepSummaryTableValue();
		} else {
			trow = this.findTrowOfUnitStepSummaryTableValue(unitDiv);
		}
		System.out.println("[TEST] Value Row Xpath:" + trow.getCanonicalXPath());
		// # of Array {N/A,Status,File,Total,Code,Comment,Blank}
		// # of values {File,Total,Code,Comment,Blank}
		this.assertTableRowValues(trow, rowLabelOfCounted(), expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of uncounted files.
	 * This TableRow is summary table in result page.
	 * @param expected array of expected numbers.
	 */
	public void assertStepSummaryTableUncount(final int[] expected) {
		this.assertStepSummaryTableUncount(null, expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of uncounted files.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 * @param expected array of expected numbers.
	 */
	public void assertStepSummaryTableUncount(final HtmlDivision unitDiv, final int[] expected) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfStepSummaryTableUncount();
		} else {
			trow = this.findTrowOfUnitStepSummaryTableUncount(unitDiv);
		}
		System.out.println("[TEST] Value Row Xpath:" + trow.getCanonicalXPath());
		// # of Array {N/A,Status,File,Total,Code,Comment,Blank}
		// # of values {File,Total,Code,Comment,Blank}
		this.assertTableRowValues(trow, rowLabelOfUncounted(), expected);
	}

	private HtmlTableRow findTrowOfDiffSummaryTableLabel() {
		String trXpath = this.mainDiv.getCanonicalXPath() + "/table[2]/tbody[1]/tr";
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitDiffSummaryTableLabel(final HtmlDivision unitDiv) {
		String trXpath = unitDiv.getCanonicalXPath() + "/table[3]/tbody[1]/tr";
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	/**
	 * Asserts that TableRow's value matches the labels of summary diffs table.
	 * This TableRow is summary table in result page.
	 */
	public void assertDiffSummaryTableLabel() {
		this.assertDiffSummaryTableLabel(null);
	}

	/**
	 * Asserts that TableRow's value matches the labels of summary diffs table.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 */
	public void assertDiffSummaryTableLabel(final HtmlDivision unitDiv) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfDiffSummaryTableLabel();
		} else {
			trow = this.findTrowOfUnitDiffSummaryTableLabel(unitDiv);
		}
		System.out.println("[TEST] Summary Diff Label Row Xpath:" + trow.getCanonicalXPath());
		for (int i = 1; i < lengthOfSummaryDiffLabels(); i++) {
			String tdXpath = trow.getCanonicalXPath() + "/td[" + i + "]";
			HtmlTableDataCell labelCell = this.getPage().getFirstByXPath(tdXpath);
			System.out.println("[TEST] diff Data Label:" + labelCell.getTextContent());
			String label = labelCell.getTextContent();
			assertThat(label, startsWith(summaryDiffLabelOf(i)));
		}
	}

	private static final int ROW_INDEX_FOR_UNCHANGED = 2;
	private static final int ROW_INDEX_FOR_UNSUPPORTED = 3;

	private HtmlTableRow findTrowOfDiffSummaryTableValue() {
		HtmlTableRow trow = this.findTrowOfDiffSummaryTableLabel();
		String trXpath = getXPathOfValueTrFromLabelTr(trow);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitDiffSummaryTableValue(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitDiffSummaryTableLabel(unitDiv);
		String trXpath = getXPathOfValueTrFromLabelTr(trow);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfDiffSummaryTableUnchange() {
		HtmlTableRow trow = this.findTrowOfDiffSummaryTableLabel();
		String trXpath = getXPathOfValueTrFromLabelTr(trow, ROW_INDEX_FOR_UNCHANGED);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitDiffSummaryTableUnchange(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitDiffSummaryTableLabel(unitDiv);
		String trXpath = getXPathOfValueTrFromLabelTr(trow, ROW_INDEX_FOR_UNCHANGED);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfDiffSummaryTableUnsupport() {
		HtmlTableRow trow = this.findTrowOfDiffSummaryTableLabel();
		String trXpath = getXPathOfValueTrFromLabelTr(trow, ROW_INDEX_FOR_UNSUPPORTED);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	private HtmlTableRow findTrowOfUnitDiffSummaryTableUnsupport(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitDiffSummaryTableLabel(unitDiv);
		String trXpath = getXPathOfValueTrFromLabelTr(trow, ROW_INDEX_FOR_UNSUPPORTED);
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of summary diffs.
	 * This TableRow is summary table in result page.
	 * @param expected array of expected numbers.
	 */
	public void assertDiffSummaryTableValue(final int[] expected) {
		this.assertDiffSummaryTableValue(null, expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of summary diffs.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 * @param expected array of expected numbers.
	 */
	public void assertDiffSummaryTableValue(final HtmlDivision unitDiv, final int[] expected) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfDiffSummaryTableValue();
		} else {
			trow = this.findTrowOfUnitDiffSummaryTableValue(unitDiv);
		}
		System.out.println("[TEST] Value Row Xpath:" + trow.getCanonicalXPath());
		// # of Array {N/A,Status,File,Added,Deleted}
		// # of values {File,Added,Deleted}
		this.assertTableRowValues(trow, rowLabelOfChanged(), expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of unchanged files.
	 * This TableRow is summary table in result page.
	 * @param expected array of expected numbers.
	 */
	public void assertDiffSummaryTableUnchange(final int[] expected) {
		this.assertDiffSummaryTableUnchange(null, expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of unchanged files.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 * @param expected array of expected numbers.
	 */
	public void assertDiffSummaryTableUnchange(final HtmlDivision unitDiv, final int[] expected) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfDiffSummaryTableUnchange();
		} else {
			trow = this.findTrowOfUnitDiffSummaryTableUnchange(unitDiv);
		}
		System.out.println("[TEST] Value Row Xpath:" + trow.getCanonicalXPath());
		// # of Array {N/A,Status,File,Added,Deleted}
		// # of values {File,Added,Deleted}
		this.assertTableRowValues(trow, rowLabelOfUnchanged(), expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of unsupported files.
	 * This TableRow is summary table in result page.
	 * @param expected array of expected numbers.
	 */
	public void assertDiffSummaryTableUnsupport(final int[] expected) {
		this.assertDiffSummaryTableUnsupport(null, expected);
	}

	/**
	 * Asserts that TableRow's value matches the expected numbers of unsupported files.
	 * This TableRow is summary table in result page.
	 * unitDiv is a countingUnit division.
	 * If unitDiv is null, all countingUnits summary table.
	 * @param unitDiv division of countingUnit.
	 * @param expected array of expected numbers.
	 */
	public void assertDiffSummaryTableUnsupport(final HtmlDivision unitDiv, final int[] expected) {
		HtmlTableRow trow = null;
		if (unitDiv == null) {
			trow = this.findTrowOfDiffSummaryTableUnsupport();
		} else {
			trow = this.findTrowOfUnitDiffSummaryTableUnsupport(unitDiv);
		}
		System.out.println("[TEST] Value Row Xpath:" + trow.getCanonicalXPath());
		// # of Array {N/A,Status,File,Added,Deleted}
		// # of values {File,Added,Deleted}
		this.assertTableRowValues(trow, rowLabelOfUnsupported(), expected);
	}

	private static final int ERROR_RETURN_VALUE = -9;

	private static int parseIntOrHyphen(final String s) {
		if (s == null || s.isEmpty()) {
			return ERROR_RETURN_VALUE;
		}
		if (s.equals("-")) { // Hyphen
			return -1;
		}
		return Integer.parseInt(s);
	}

	/**
	 * Finds &lt;h1&gt; for each counting unit.
	 * @return HtmlHeading1 instance.
	 */
	public HtmlHeading1 findHeadingOfEachSummary() {
		String h1Xpath2 = this.mainDiv.getCanonicalXPath() + "/div[2]/h1";
		return (HtmlHeading1) this.getPage().getFirstByXPath(h1Xpath2);
	}

	private HtmlUnorderedList findUlistOfCountingUnits() {
		String ulXpath = this.mainDiv.getCanonicalXPath() + "/div[3]/ul";
		return (HtmlUnorderedList) this.getPage().getFirstByXPath(ulXpath);
	}

	/**
	 * Asserts that UnorderedList's value matches the expected countingUnits.
	 * This UnorderedList means countingUnits tab in result page.
	 * @param expected array of category name.
	 */
	public void assertCountingUnitListContent(final String[] expected) {
		HtmlUnorderedList ul = this.findUlistOfCountingUnits();
		int categoriesNum = expected.length;
		for (int i = 1; i <= categoriesNum; i++) {
			String aXpath = ul.getCanonicalXPath() + "/li[" + i + "]/a";
			HtmlAnchor anchor = (HtmlAnchor) this.getPage().getFirstByXPath(aXpath);
			System.out.println("[TEST] li[" + i + "] anchor content:" + anchor.getTextContent());
			assertThat(anchor.getTextContent(), is(equalTo(expected[i - 1])));
		}
	}

	/**
	 * Finds &lt;div&gt; for N-th counting unit result.
	 * @param index N-th number - 1.
	 * @return HtmlDivision instance.
	 */
	public HtmlDivision findDivOfEachCountingUnit(final int index) {
		String unitDivXpath = this.mainDiv.getCanonicalXPath() + "/div[3]/div[" + index + "]";
		return (HtmlDivision) this.getPage().getFirstByXPath(unitDivXpath);
	}

	/**
	 * Finds &lt;h3&gt; for summary of counting unit.
	 * @param unitDiv division for countingUnit.
	 * @return HtmlHeading3 instance.
	 */
	public HtmlHeading3 findHeadingOfUnitSummary(final HtmlDivision unitDiv) {
		String h3Xpath = unitDiv.getCanonicalXPath() + "/div[1]/h3";
		return (HtmlHeading3) this.getPage().getFirstByXPath(h3Xpath);
	}

	/**
	 * Finds &lt;h3&gt; for detail of counting unit.
	 * @param unitDiv division for countingUnit.
	 * @return HtmlHeading3 instance.
	 */
	public HtmlHeading3 findHeadingOfUnitDetail(final HtmlDivision unitDiv) {
		String h3Xpath = unitDiv.getCanonicalXPath() + "/div[2]/h3";
		return (HtmlHeading3) this.getPage().getFirstByXPath(h3Xpath);
	}

	private HtmlTableRow findTrowOfUnitStepDetailTableLabel(final HtmlDivision unitDiv) {
		String trXpath = unitDiv.getCanonicalXPath() + "/table[2]/tbody[1]/tr";
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	/**
	 * Asserts that TableRow's value matches the labels of detail steps table.
	 * @param unitDiv division for countingUnit.
	 */
	public void assertStepDetailTableLabel(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitStepDetailTableLabel(unitDiv);
		System.out.println("[TEST] Detail Step Label Row Xpath:" + trow.getCanonicalXPath());
		for (int i = 0; i < lengthOfDetailStepLabels(); i++) {
			String tdXpath = trow.getCanonicalXPath() + "/td[" + Integer.toString(i + 1) + "]";
			HtmlTableDataCell labelCell = this.getPage().getFirstByXPath(tdXpath);
			System.out.println("[TEST] step Data Label:" + labelCell.getTextContent());
			if (i > 0) {
				String label = labelCell.getTextContent();
				assertThat(label, startsWith(detailStepLabelOf(i)));
			}
		}
	}

	private HtmlTableBody findTbodyOfUnitStepDetailTable(final HtmlDivision unitDiv) {
		String tbodyXpath = unitDiv.getCanonicalXPath() + "/table[2]/tbody[2]";
		return (HtmlTableBody) this.getPage().getFirstByXPath(tbodyXpath);
	}

	/**
	 * Asserts that TableRowData's value matches the expected numbers of detail steps.
	 * @param unitDiv division for countingUnit.
	 * @param expected file name which has expected values.
	 */
	public void assertStepDetailTableValue(final HtmlDivision unitDiv, final URL expected) {
		HtmlTableBody tableBody = this.findTbodyOfUnitStepDetailTable(unitDiv);
		System.out.println("[TEST] Data tBody Xpath:" + tableBody.getCanonicalXPath());
		String[][] actualData = convertToTableArrayFromStepTableBody(this.getPage(), tableBody);
		System.out.println("[TEST] tBody contents:\n" + contentOf(actualData));
		//System.out.println("[TEST] expected data file:" + expected.getPath());
		assertThat(contentOf(actualData), is(equalTo(contentWithoutTypeAndCategoryOf(expected))));
	}

	private HtmlTableRow findTrowOfUnitDiffDetailTableLabel(final HtmlDivision unitDiv) {
		String trXpath = unitDiv.getCanonicalXPath() + "/table[4]/tbody[1]/tr";
		return (HtmlTableRow) this.getPage().getFirstByXPath(trXpath);
	}

	/**
	 * Asserts that TableRow's value matches the labels of detail diffs table.
	 * @param unitDiv division for countingUnit.
	 */
	public void assertDiffDetailTableLabel(final HtmlDivision unitDiv) {
		HtmlTableRow trow = this.findTrowOfUnitDiffDetailTableLabel(unitDiv);
		System.out.println("[TEST] Detail Diff Label Row Xpath:" + trow.getCanonicalXPath());
		for (int i = 0; i < lengthOfDetailDiffLabels(); i++) {
			String tdXpath = trow.getCanonicalXPath() + "/td[" + Integer.toString(i + 1) + "]";
			HtmlTableDataCell labelCell = this.getPage().getFirstByXPath(tdXpath);
			System.out.println("[TEST] diff Data Label:" + labelCell.getTextContent());
			if (i > 0) {
				String label = labelCell.getTextContent();
				assertThat(label, startsWith(detailDiffLabelOf(i)));
			}
		}
	}

	private HtmlTableBody findTbodyOfUnitDiffDetailTable(final HtmlDivision unitDiv) {
		String tbodyXpath = unitDiv.getCanonicalXPath() + "/table[4]/tbody[2]";
		return (HtmlTableBody) this.getPage().getFirstByXPath(tbodyXpath);
	}

	/**
	 * Asserts that TableRowData's value matches the expected numbers of detail diffs.
	 * @param unitDiv division for countingUnit.
	 * @param expected file name which has expected values.
	 */
	public void assertDiffDetailTableValue(final HtmlDivision unitDiv, final URL expected) {
		HtmlTableBody tableBody = this.findTbodyOfUnitDiffDetailTable(unitDiv);
		System.out.println("[TEST] Data tBody Xpath:" + tableBody.getCanonicalXPath());
		String[][] actualData = convertToTableArrayFromDiffTableBody(this.getPage(), tableBody);
		System.out.println("[TEST] tBody contents:\n" + contentOf(actualData));
		//System.out.println("[TEST] expected data file:" + expected.getPath());
		assertThat(contentOf(actualData), is(equalTo(contentWithoutTypeAndCategoryOf(expected))));
	}
}
