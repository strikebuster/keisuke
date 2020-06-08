package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.lengthOfSummaryDiffLabelsForJobMain;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.lengthOfSummaryStepLabelsForJobMain;
import static org.jenkinsci.plugins.keisuke.util.BuildResultCountTestUtil.summaryLabelForJobMainOf;

import org.jenkinsci.plugins.keisuke.uihelper.ProjectMainPageUI;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

import hudson.model.FreeStyleBuild;

/**
 * Testing StepCountProjectAction view in project main page.
 */
public class StepCountProjectActionTest extends AbstractActionTest {

	private ProjectMainPageUI mainUI = null;

	@Test
	public void showMainResultUsingFileSet() {
		System.out.println("## StepCountProjectActionTest ## showMainResultUsingFileSet ##");
		int buildTimes = 2;
		this.prepareProjectToCountJavaThenDoingBuildsBy(buildTimes);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		HtmlDivision main = this.mainUI.findDivOfMainPanel();
		assertThat(main, is(not(nullValue())));
		String mainXpath = main.getCanonicalXPath();
		System.out.println("[TEST] main-panel division xpath:" + mainXpath);

		String h1Xpath = mainXpath + "/h1";
		HtmlHeading1 head1 = (HtmlHeading1) this.mainUI.getPage().getFirstByXPath(h1Xpath);
		System.out.println("[TEST] heading1 content:" + head1.getTextContent());
		assertThat(head1.getTextContent(), containsString(this.projName()));

		HtmlAnchor anchor = this.mainUI.findAnchorOfKeisukeStepCountInMain(mainXpath);
		String caption = anchor.getTextContent();
		System.out.println("[TEST] caption anchor content:" + caption);
		assertThat(caption, is(equalTo(Messages.steps())));
		for (int i = 1; i < lengthOfSummaryStepLabelsForJobMain(); i++) {
			HtmlTableDataCell labelCell = this.mainUI.findTdataOfStepLabelFrom(anchor, i);
			HtmlTableDataCell valueCell = this.mainUI.findTdataOfStepValueFrom(anchor, i);
			System.out.println("[TEST] step Data Label:" + labelCell.getTextContent());
			System.out.println("[TEST] step Data Value:" + valueCell.getTextContent());
			String label = labelCell.getTextContent();
			assertThat(label, startsWith(summaryLabelForJobMainOf(i)));
		}
		// URL link to result page
		System.out.println("[TEST] page title(before):" + this.mainUI.getPage().getTitleText());
		String path = "job/" + this.projName() + "/" + anchor.getHrefAttribute();
		System.out.println("[TEST] anchor goes to:" + path);
		HtmlPage anotherPage = this.mainUI.openResultPage(path);
		if (anotherPage == null) {
			// 404 not found about jquery
			return;
		}
		System.out.println("[TEST] === go to the anchor's result page.");
		System.out.println("[TEST] page title(after):" + anotherPage.getTitleText());
		String pageTitle = anotherPage.getTitleText();
		assertThat(pageTitle, is(allOf(startsWith("Keisuke"), containsString("Build #"))));
	}

	@Test
	public void showMainResultUsingStepSimply() {
		System.out.println("## StepCountProjectActionTest ## showMainResultUsingStepSimply ##");
		int buildTimes = 2;
		this.prepareProjectToCountJavaUsingStepSimplyThenDoingBuildsBy(buildTimes);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		HtmlDivision main = this.mainUI.findDivOfMainPanel();
		assertThat(main, is(not(nullValue())));
		String mainXpath = main.getCanonicalXPath();
		System.out.println("[TEST] main-panel division xpath:" + mainXpath);

		String h1Xpath = mainXpath + "/h1";
		HtmlHeading1 head1 = (HtmlHeading1) this.mainUI.getPage().getFirstByXPath(h1Xpath);
		System.out.println("[TEST] heading1 content:" + head1.getTextContent());
		assertThat(head1.getTextContent(), containsString(this.projName()));

		HtmlAnchor anchor = this.mainUI.findAnchorOfKeisukeStepCountInMain(mainXpath);
		String caption = anchor.getTextContent();
		System.out.println("[TEST] caption anchor content:" + caption);
		assertThat(caption, is(equalTo(Messages.steps())));
		for (int i = 1; i < lengthOfSummaryStepLabelsForJobMain(); i++) {
			HtmlTableDataCell labelCell = this.mainUI.findTdataOfStepLabelFrom(anchor, i);
			HtmlTableDataCell valueCell = this.mainUI.findTdataOfStepValueFrom(anchor, i);
			System.out.println("[TEST] step Data Label:" + labelCell.getTextContent());
			System.out.println("[TEST] step Data Value:" + valueCell.getTextContent());
			String label = labelCell.getTextContent();
			assertThat(label, startsWith(summaryLabelForJobMainOf(i)));
		}
		// URL link to result page
		System.out.println("[TEST] page title(before):" + this.mainUI.getPage().getTitleText());
		String path = "job/" + this.projName() + "/" + anchor.getHrefAttribute();
		System.out.println("[TEST] anchor goes to:" + path);
		HtmlPage anotherPage = this.mainUI.openResultPage(path);
		if (anotherPage == null) {
			// 404 not found about jquery
			return;
		}
		System.out.println("[TEST] === go to the anchor's result page.");
		System.out.println("[TEST] page title(after):" + anotherPage.getTitleText());
		String pageTitle = anotherPage.getTitleText();
		assertThat(pageTitle, is(allOf(startsWith("Keisuke"), containsString("Build #"))));
	}

	@Test
	public void showMainResultUsingDiffToo() {
		System.out.println("## StepCountProjectActionTest ## showMainResultUsingDiffToo ##");
		int buildTimes = 2;
		this.prepareProjectToCountJavaUsingDiffTooThenDoingBuildsBy(buildTimes);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		HtmlDivision main = this.mainUI.findDivOfMainPanel();
		assertThat(main, is(not(nullValue())));
		String mainXpath = main.getCanonicalXPath();
		System.out.println("[TEST] main-panel division xpath:" + mainXpath);

		String h1Xpath = mainXpath + "/h1";
		HtmlHeading1 head1 = (HtmlHeading1) this.mainUI.getPage().getFirstByXPath(h1Xpath);
		System.out.println("[TEST] heading1 content:" + head1.getTextContent());
		assertThat(head1.getTextContent(), containsString(this.projName()));

		HtmlAnchor anchor = this.mainUI.findAnchorOfKeisukeStepCountInMain(mainXpath);
		String caption = anchor.getTextContent();
		System.out.println("[TEST] caption anchor content:" + caption);
		assertThat(caption, is(equalTo(Messages.steps())));
		for (int i = 1; i < lengthOfSummaryStepLabelsForJobMain(); i++) {
			HtmlTableDataCell labelCell = this.mainUI.findTdataOfStepLabelFrom(anchor, i);
			HtmlTableDataCell valueCell = this.mainUI.findTdataOfStepValueFrom(anchor, i);
			System.out.println("[TEST] step Data Label:" + labelCell.getTextContent());
			System.out.println("[TEST] step Data Value:" + valueCell.getTextContent());
			String label = labelCell.getTextContent();
			assertThat(label, startsWith(summaryLabelForJobMainOf(i)));
		}
		for (int j = 1; j < lengthOfSummaryDiffLabelsForJobMain(); j++) {
			int i = lengthOfSummaryStepLabelsForJobMain() - 1 + j;
			HtmlTableDataCell labelCell = this.mainUI.findTdataOfStepLabelFrom(anchor, i);
			HtmlTableDataCell valueCell = this.mainUI.findTdataOfStepValueFrom(anchor, i);
			System.out.println("[TEST] diff Data Label:" + labelCell.getTextContent());
			System.out.println("[TEST] diff Data Value:" + valueCell.getTextContent());
			String label = labelCell.getTextContent();
			assertThat(label, startsWith(summaryLabelForJobMainOf(i)));
		}
		// URL link to result page
		System.out.println("[TEST] page title(before):" + this.mainUI.getPage().getTitleText());
		String path = "job/" + this.projName() + "/" + anchor.getHrefAttribute();
		System.out.println("[TEST] anchor goes to:" + path);
		HtmlPage anotherPage = this.mainUI.openResultPage(path);
		if (anotherPage == null) {
			// 404 not found about jquery
			return;
		}
		System.out.println("[TEST] === go to the anchor's result page.");
		System.out.println("[TEST] page title(after):" + anotherPage.getTitleText());
		String pageTitle = anotherPage.getTitleText();
		assertThat(pageTitle, is(allOf(startsWith("Keisuke"), containsString("Build #"))));
	}

	private HtmlMap validateMapAboutJava(final HtmlImage image, final int times) {
		String[] expectedCategories = {"java"};
		return this.mainUI.verifyMap(image, times, expectedCategories);
	}

	private HtmlMap validateMapAboutJavaAndSjis(final HtmlImage image, final int times) {
		String[] expectedCategories = {"java", "commentS"};
		return this.mainUI.verifyMap(image, times, expectedCategories);
	}

	@Test
	public void showFloatingGraph() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraph ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJava(graph, buildTimes);
		// Diffグラフがないことを確認
		this.mainUI.assertThatDiffCountGraphsDoNotExist();
	}

	@Test
	public void showFloatingGraphAboutJavaAndSjis() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraphAboutJavaAndSjis ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaAndSjisThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJavaAndSjis(graph, buildTimes);
		// Diffグラフがないことを確認
		this.mainUI.assertThatDiffCountGraphsDoNotExist();
	}

	@Test
	public void showFloatingGraphWhenOneBuild() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraphWhenOneBuild ##");
		int buildTimes = 1;
		FreeStyleBuild build = this.prepareProjectToCountJavaThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJava(graph, buildTimes);
		// Diffグラフがないことを確認
		this.mainUI.assertThatDiffCountGraphsDoNotExist();
	}

	@Test
	public void showFloatingGraphWhenNoBuild() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraphWhenNoBuild ##");
		int buildTimes = 0;
		FreeStyleBuild build = this.prepareProjectToCountJavaThenDoingBuildsBy(buildTimes);
		assertThat(build, is(nullValue()));
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, null);
		// グラフのマップ
		this.validateMapAboutJava(graph, buildTimes);
		// Diffグラフがないことを確認
		this.mainUI.assertThatDiffCountGraphsDoNotExist();
	}

	@Test
	public void showFloatingGraphUsingStepSimply() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraphUsingStepSimply ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaUsingStepSimplyThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJava(graph, buildTimes);
		// Diffグラフがないことを確認
		this.mainUI.assertThatDiffCountGraphsDoNotExist();
	}

	@Test
	public void showFloatingGraphAboutJavaAndSjisUsingStepSimply() {
		System.out.println("## StepCountProjectActionTest ## "
				+ "showFloatingGraphAboutJavaAndSjisUsingStepSimply ##");
		int buildTimes = 2;
		FreeStyleBuild build =
				this.prepareProjectToCountJavaAndSjisUsingStepSimplyThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJavaAndSjis(graph, buildTimes);
		// Diffグラフがないことを確認
		this.mainUI.assertThatDiffCountGraphsDoNotExist();
	}

	private HtmlMap validateMapForDiffCountAboutJava(final HtmlImage image, final int times) {
		String[] expectedCategories = {"java"};
		return this.mainUI.verifyMapForDiffCount(image, times, expectedCategories);
	}

	private HtmlMap validateMapForDiffCountAboutJavaAndSjis(final HtmlImage image, final int times) {
		String[] expectedCategories = {"java", "commentS"};
		return this.mainUI.verifyMapForDiffCount(image, times, expectedCategories);
	}

	@Test
	public void showFloatingGraphUsingDiffToo() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraphUsingDiffToo ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaUsingDiffTooThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJava(graph, buildTimes);
		// Diff added グラフ
		graph = this.mainUI.verifyFloatingGraphForDiffAdded(buildTimes);
		this.validateMapForDiffCountAboutJava(graph, buildTimes);
		// Diff deleted グラフ
		graph = this.mainUI.verifyFloatingGraphForDiffDeleted(buildTimes);
		this.validateMapForDiffCountAboutJava(graph, buildTimes);
	}

	@Test
	public void showFloatingGraphAboutJavaAndSjisUsingDiffToo() {
		System.out.println("## StepCountProjectActionTest ## showFloatingGraphAboutJavaAndSjisUsingDiffToo ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaAndSjisUsingDiffTooThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		this.validateMapAboutJavaAndSjis(graph, buildTimes);
		// Diff added グラフ
		graph = this.mainUI.verifyFloatingGraphForDiffAdded(buildTimes);
		this.validateMapForDiffCountAboutJavaAndSjis(graph, buildTimes);
		// Diff deleted グラフ
		graph = this.mainUI.verifyFloatingGraphForDiffDeleted(buildTimes);
		this.validateMapForDiffCountAboutJavaAndSjis(graph, buildTimes);
	}
}
