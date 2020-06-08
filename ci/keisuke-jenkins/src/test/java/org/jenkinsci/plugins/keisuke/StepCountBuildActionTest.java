package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.net.URL;

import org.jenkinsci.plugins.keisuke.uihelper.BuildResultPageUI;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;

import hudson.model.FreeStyleBuild;

/**
 * Testing StepCountBuildAction view in build result page.
 */
public class StepCountBuildActionTest extends AbstractActionTest {

	private BuildResultPageUI resultUI = null;
	private HtmlDivision mainDiv = null;

	private void validateFirstHeading() {
		HtmlHeading1 allHead1 = this.resultUI.findHeadingOfAllSummary();
		System.out.println("[TEST] 1st heading1 content:" + allHead1.getTextContent());
		assertThat(allHead1.getTextContent(), is(not(emptyOrNullString())));
	}

	private void validateTotalSummaryTable(final int[] counts, final int[] uncounts) {
		this.resultUI.assertStepSummaryTableLabel();
		this.resultUI.assertStepSummaryTableValue(counts);
		this.resultUI.assertStepSummaryTableUncount(uncounts);
	}

	private void validateTotalSummaryTable(final int[] counts, final int[] uncounts,
			final int[] changes, final int[] unchanges, final int[] unsupports) {
		this.resultUI.assertStepSummaryTableLabel();
		this.resultUI.assertStepSummaryTableValue(counts);
		this.resultUI.assertStepSummaryTableUncount(uncounts);
		this.resultUI.assertDiffSummaryTableLabel();
		this.resultUI.assertDiffSummaryTableValue(changes);
		this.resultUI.assertDiffSummaryTableUnchange(unchanges);
		this.resultUI.assertDiffSummaryTableUnsupport(unsupports);
	}

	private void validateSecondHeading() {
		HtmlHeading1 eachHead1 = this.resultUI.findHeadingOfEachSummary();
		System.out.println("[TEST] 2nd heading1 content:" + eachHead1.getTextContent());
		assertThat(eachHead1.getTextContent(), is(not(emptyOrNullString())));
	}

	private void validateUnitTab(final String[] units) {
		this.resultUI.assertCountingUnitListContent(units);
	}

	private void validateUnitSummaryTable(final HtmlDivision unitDiv, final int[] expected,
			final int[] uncounted) {
		// 見出し
		HtmlHeading3 allHead3 = this.resultUI.findHeadingOfUnitSummary(unitDiv);
		System.out.println("[TEST] 1st heading3 content:" + allHead3.getTextContent());
		assertThat(allHead3.getTextContent(), is(not(emptyOrNullString())));
		this.resultUI.assertStepSummaryTableLabel(unitDiv);
		this.resultUI.assertStepSummaryTableValue(unitDiv, expected);
		this.resultUI.assertStepSummaryTableUncount(unitDiv, uncounted);
	}

	private void validateUnitSummaryTable(final HtmlDivision unitDiv, final int[] expected,
			final int[] uncounted, final int[] changes, final int[] unchanges, final int[] unsupports) {
		// 見出し
		HtmlHeading3 allHead3 = this.resultUI.findHeadingOfUnitSummary(unitDiv);
		System.out.println("[TEST] 1st heading3 content:" + allHead3.getTextContent());
		assertThat(allHead3.getTextContent(), is(not(emptyOrNullString())));
		this.resultUI.assertStepSummaryTableLabel(unitDiv);
		this.resultUI.assertStepSummaryTableValue(unitDiv, expected);
		this.resultUI.assertStepSummaryTableUncount(unitDiv, uncounted);
		this.resultUI.assertDiffSummaryTableLabel(unitDiv);
		this.resultUI.assertDiffSummaryTableValue(unitDiv, changes);
		this.resultUI.assertDiffSummaryTableUnchange(unitDiv, unchanges);
		this.resultUI.assertDiffSummaryTableUnsupport(unitDiv, unsupports);
	}

	private void validateUnitDetailTable(final HtmlDivision unitDiv, final String expected) {
		// 見出し
		HtmlHeading3 eachHead3 = this.resultUI.findHeadingOfUnitDetail(unitDiv);
		System.out.println("[TEST] 2nd heading3 content:" + eachHead3.getTextContent());
		assertThat(eachHead3.getTextContent(), is(not(emptyOrNullString())));
		// ラベル行
		this.resultUI.assertStepDetailTableLabel(unitDiv);
		// データ
		URL expectedData = this.getClass().getResource(expected);
		this.resultUI.assertStepDetailTableValue(unitDiv, expectedData);
	}

	private void validateUnitDetailTable(final HtmlDivision unitDiv, final String expected,
			final String expectedDiff) {
		// 見出し
		HtmlHeading3 eachHead3 = this.resultUI.findHeadingOfUnitDetail(unitDiv);
		System.out.println("[TEST] 2nd heading3 content:" + eachHead3.getTextContent());
		assertThat(eachHead3.getTextContent(), is(not(emptyOrNullString())));
		// ラベル行
		this.resultUI.assertStepDetailTableLabel(unitDiv);
		// データ
		URL expectedData = this.getClass().getResource(expected);
		this.resultUI.assertStepDetailTableValue(unitDiv, expectedData);
		// ラベル行
		this.resultUI.assertDiffDetailTableLabel(unitDiv);
		// データ
		expectedData = this.getClass().getResource(expectedDiff);
		this.resultUI.assertDiffDetailTableValue(unitDiv, expectedData);
	}

	private static final int[] EXPECTED_STEPS_OF_ALL = {8, 237, 141, 53, 43};
	private static final int[] EXPECTED_UNCOUNT_OF_ALL = {0, -1, -1, -1, -1};
	private static final String[] EXPECTED_UNITS = {"java", "commentS"};
	private static final int[][] EXPECTED_STEPS_OF_UNITS = {{4, 177, 114, 30, 33}, {4, 60, 27, 23, 10}};
	private static final int[][] EXPECTED_UNCOUNT_OF_UNITS = {{0, -1, -1, -1, -1}, {0, -1, -1, -1, -1}};
	private static final String[] EXPECTED_DATA_FILES = {
			"stepCountFileSetNoBase_java_unit.csv",
			"stepCountNoBase_sjis_unit.csv"};

	@Test
	public void showDetailResultUsingFileSet() {
		System.out.println("## StepCountBuildActionTest ## showDetailResultUsingFileSet ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaAndSjisThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		this.resultUI = new BuildResultPageUI(this.webClient(), this.projName());
		this.resultUI.openBuildResultPage(this.projName());
		if (this.resultUI.getPage() == null) {
			// 404 not found about jquery
			return;
		}

		this.mainDiv = this.resultUI.findDivOfMainPanel();
		assertThat(this.mainDiv, is(not(nullValue())));

		this.validateFirstHeading();
		this.validateTotalSummaryTable(EXPECTED_STEPS_OF_ALL, EXPECTED_UNCOUNT_OF_ALL);
		this.validateSecondHeading();
		this.validateUnitTab(EXPECTED_UNITS);
		// 各カテゴリの結果
		for (int i = 0; i < EXPECTED_UNITS.length; i++) {
			int idx = i + 1;
			// タブ
			HtmlDivision unitDiv = this.resultUI.findDivOfEachCountingUnit(idx);
			System.out.println("[TEST] countingUnit division id:" + unitDiv.getId());
			assertThat(unitDiv.getId(), is(equalTo(EXPECTED_UNITS[i])));
			// 全ファイル
			this.validateUnitSummaryTable(unitDiv, EXPECTED_STEPS_OF_UNITS[i],
					EXPECTED_UNCOUNT_OF_UNITS[i]);
			// ファイルごと
			this.validateUnitDetailTable(unitDiv, EXPECTED_DATA_FILES[i]);
		}
	}

	private static final int[] DIFFTOO_EXPECTED_STEPS_OF_ALL = {10, 299, 192, 53, 54};
	private static final int[] DIFFTOO_EXPECTED_UNCOUNT_OF_ALL = {1, -1, -1, -1, -1};
	private static final int[] DIFFTOO_EXPECTED_DIFFS_OF_ALL = {13, 59, 48};
	private static final int[] DIFFTOO_EXPECTED_UNCHANGE_OF_ALL = {2, 0, 0};
	private static final int[] DIFFTOO_EXPECTED_UNSUPPORT_OF_ALL = {1, -1, -1};
	private static final String[] DIFFTOO_EXPECTED_UNITS = {"java", "commentS"};
	private static final int[][] DIFFTOO_EXPECTED_STEPS_OF_UNITS = {{6, 239, 165, 30, 44}, {4, 60, 27, 23, 10}};
	private static final int[][] DIFFTOO_EXPECTED_UNCOUNT_OF_UNITS = {{1, -1, -1, -1, -1}, {0, -1, -1, -1, -1}};
	private static final int[][] DIFFTOO_EXPECTED_DIFFS_OF_UNITS = {{5, 43, 6}, {8, 16, 42}};
	private static final int[][] DIFFTOO_EXPECTED_UNCHANGE_OF_UNITS = {{2, 0, 0}, {0, 0, 0}};
	private static final int[][] DIFFTOO_EXPECTED_UNSUPPORT_OF_UNITS = {{1, -1, -1}, {0, -1, -1}};

	private static final String[][] DIFFTOO_EXPECTED_DATA_FILES = {
			{"stepCountNoBase_java_unit.csv", "diffCountNoBase_java_unit.csv"},
			{"stepCountNoBase_sjis_unit.csv", "diffCountNoBase_sjis_unit.csv"}
	};

	@Test
	public void showDetailResultUsingDiffToo() {
		System.out.println("## StepCountBuildActionTest ## showDetailResultUsingDiffToo ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaAndSjisUsingDiffTooThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		this.resultUI = new BuildResultPageUI(this.webClient(), this.projName());
		this.resultUI.openBuildResultPage(this.projName());
		if (this.resultUI.getPage() == null) {
			// 404 not found about jquery
			return;
		}

		this.mainDiv = this.resultUI.findDivOfMainPanel();
		assertThat(this.mainDiv, is(not(nullValue())));

		this.validateFirstHeading();
		this.validateTotalSummaryTable(DIFFTOO_EXPECTED_STEPS_OF_ALL, DIFFTOO_EXPECTED_UNCOUNT_OF_ALL,
				DIFFTOO_EXPECTED_DIFFS_OF_ALL, DIFFTOO_EXPECTED_UNCHANGE_OF_ALL,
				DIFFTOO_EXPECTED_UNSUPPORT_OF_ALL);
		this.validateSecondHeading();
		this.validateUnitTab(DIFFTOO_EXPECTED_UNITS);
		// 各カテゴリの結果
		for (int i = 0; i < DIFFTOO_EXPECTED_UNITS.length; i++) {
			int idx = i + 1;
			// タブ
			HtmlDivision unitDiv = this.resultUI.findDivOfEachCountingUnit(idx);
			System.out.println("[TEST] countingUnit division id:" + unitDiv.getId());
			assertThat(unitDiv.getId(), equalTo(DIFFTOO_EXPECTED_UNITS[i]));
			// 全ファイル
			this.validateUnitSummaryTable(unitDiv, DIFFTOO_EXPECTED_STEPS_OF_UNITS[i],
					DIFFTOO_EXPECTED_UNCOUNT_OF_UNITS[i], DIFFTOO_EXPECTED_DIFFS_OF_UNITS[i],
					DIFFTOO_EXPECTED_UNCHANGE_OF_UNITS[i], DIFFTOO_EXPECTED_UNSUPPORT_OF_UNITS[i]);
			// ファイルごと
			this.validateUnitDetailTable(unitDiv,
					DIFFTOO_EXPECTED_DATA_FILES[i][0], DIFFTOO_EXPECTED_DATA_FILES[i][1]);
		}
	}
}
