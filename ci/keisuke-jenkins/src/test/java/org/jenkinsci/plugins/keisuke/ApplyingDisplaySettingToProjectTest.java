package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

import java.util.List;

import org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI;
import org.jenkinsci.plugins.keisuke.uihelper.DisplaySettingConfigUI;
import org.jenkinsci.plugins.keisuke.uihelper.ProjectConfigUI;
import org.jenkinsci.plugins.keisuke.uihelper.ProjectMainPageUI;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;

import hudson.model.FreeStyleBuild;

/**
 * Testing for changing DisplaySetting is applied to the project.
 */
public class ApplyingDisplaySettingToProjectTest extends AbstractActionTest
	implements ProjectConfigUITester {

	// for ProjectConfigUITester
	private ProjectConfigUI projectUI = null;
	// for Display setting
	private DisplaySettingConfigUI displayUI = null;

	private ProjectMainPageUI mainUI = null;

	// ProjectConfigUITester methods.
	/**
	 * Gets HtmlPage of /jenkins/job/project/config
	 * @return HtmlPage instance.
	 */
	@Override
	public HtmlPage configPage() {
		return this.projectUI.configPage();
	}

	/**
	 * Gets HtmlForm in the HtmlPage of /jenkins/job/project/config
	 * @return HtmlForm instance.
	 */
	@Override
	public HtmlForm configForm() {
		return this.projectUI.configForm();
	}

	/**
	 * Gets HtmlTestOperation instance for the config page of freestyle project
	 * which uses KeisukePublisher.
	 * @return HtmlTestOperation instance.
	 */
	@Override
	public ConfigHtmlUI configHtmlUI() {
		return this.projectUI.configHtmlUI();
	}

	// common methods for this test.
	private int[] verifyMapAreaAboutJavaAndSjis(final HtmlImage image, final int times) {
		String[] expectedUnits = {"java", "commentS"};
		return this.mainUI.verifyMapAndGetPositionY(image, times, expectedUnits, true);
	}

	private DisplayStepKindEnum configureDisplayStepKindBeingAllSteps(
			final DisplayStepKindEnum beforeKind, final DisplayStepKindEnum afterKind) {

		// 設定画面の操作準備
		this.projectUI = new ProjectConfigUI(this, this.project()); // this for JenkinsUITester
		this.projectUI.openProjectConfigPage();
		this.displayUI = new DisplaySettingConfigUI(this); // this for ProjectConfigUITester
		List<HtmlRadioButtonInput> kindRadioList = this.displayUI.getRadiosOfDisplayStepKind();
		// 変更前はbeforeKind
		int checkedIdx = 0;
		String checkedValue = kindRadioList.get(checkedIdx).getAttribute("value");
		String expectedBefore = beforeKind.getValue();
		assertThat(checkedValue, is(equalTo(expectedBefore)));
		// afterKindで指定されたRadioを選択
		String expectedAfter = afterKind.getValue();
		this.displayUI.setDisplayStepKind(afterKind);
		this.displayUI.verifyRadioSelected(expectedAfter);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		try {
			this.jenkinsRule.submit(this.projectUI.configForm());
			this.projectUI.configHtmlUI().waitLongForBackgroundProcess();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
		return afterKind;
	}

	// Test case methods.
	@Test
	public void applyDisplayStepKind() {
		System.out.println("## ApplyingDisplaySettingToProjectTest ## applyDisplayStepKind ##");
		int buildTimes = 2;
		FreeStyleBuild build = this.prepareProjectToCountJavaAndSjisThenDoingBuildsBy(buildTimes);
		assertThat(build, is(not(nullValue())));
		StepCountBuildAction action = build.getAction(StepCountBuildAction.class);
		this.mainUI = new ProjectMainPageUI(this.webClient(), this.projName());
		// 右フローティンググラフ
		HtmlImage graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		int[] beforePosY = this.verifyMapAreaAboutJavaAndSjis(graph, buildTimes);

		// DisplaySettingの変更
		System.out.println("[TEST] === change the display setting.");
		DisplayStepKindEnum expectedKindEnum
			= this.configureDisplayStepKindBeingAllSteps(action.getDisplayStepKindEnum(),
					DisplayStepKindEnum.CODE_AND_COMMENT_AND_BLANK);

		// 再表示しても表示は変更前のまま
		System.out.println("[TEST] === show project page after changing the display setting.");
		this.mainUI.openProjectPage(this.projName());
		// 右フローティンググラフ
		graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, action.getDisplayStepKindEnum());
		// グラフのマップ
		int[] afterPosY = this.verifyMapAreaAboutJavaAndSjis(graph, buildTimes);

		for (int i = 0; i < afterPosY.length; i++) {
			int posA = afterPosY[i];
			int posB = beforePosY[i];
			System.out.println("[TEST] unit[" + Integer.toString(i + 1)
					+ "]:posY(before)=" + Integer.toString(posB));
			System.out.println("[TEST] unit[" + Integer.toString(i + 1)
					+ "]:posY(after )=" + Integer.toString(posA));
			assertThat(posA, is(equalTo(posB)));
		}

		// 新たなビルドから変更後の設定が反映
		System.out.println("[TEST] === 3rd build going.");
		try {
			build = this.jenkinsRule.buildAndAssertSuccess(this.project());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		buildTimes++;
		action = build.getAction(StepCountBuildAction.class);
		assertThat(action.getDisplayStepKindEnum(), is(equalTo(expectedKindEnum)));
		System.out.println("[TEST] === show project page after 3rd build done.");
		this.mainUI.openProjectPage(this.projName());
		// 右フローティンググラフ
		graph = this.mainUI.verifyFloatingGraphForStep(buildTimes, expectedKindEnum);
		// グラフのマップ
		afterPosY = this.verifyMapAreaAboutJavaAndSjis(graph, buildTimes);

		//System.out.println("[TEST] --");
		for (int i = 0; i < afterPosY.length; i++) {
			int posA = afterPosY[i];
			int posB = beforePosY[i];
			System.out.println("[TEST] unit[" + Integer.toString(i + 1)
					+ "]:posY(before)=" + Integer.toString(posB));
			System.out.println("[TEST] unit[" + Integer.toString(i + 1)
					+ "]:posY(after )=" + Integer.toString(posA));
			if (i == afterPosY.length - 1) {
				assertThat(posA, is(equalTo(posB)));
			} else {
				assertThat(posA, is(not(equalTo(posB))));
			}
		}
	}

}
