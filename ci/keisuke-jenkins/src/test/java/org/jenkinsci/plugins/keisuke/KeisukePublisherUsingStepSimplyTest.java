package org.jenkinsci.plugins.keisuke;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import hudson.model.FreeStyleBuild;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * countingMode is step_simply.
 */
public class KeisukePublisherUsingStepSimplyTest extends AbstractProjectTest {

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimply ## countJavaUsingCsvFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCount_java_unit.csv");
		String outfile = "test/out/stepCount_java_unit.csv";
		Map<String, BuildResult> map = null;
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJava("JavaCsvJob", outfile, "csv", true));
			FreeStyleBuild build = this.jenkinsRule.buildAndAssertSuccess(this.project());
			List<StepCountBuildAction> actions = build.getActions(StepCountBuildAction.class);
			StepCountBuildAction action = actions.get(actions.size() - 1);
			map = action.getStepsMap();
			//System.out.println("[TEST] Workspace is " + this.workspace().getAbsolutePath());
			actual = new File(this.workspace(), outfile);
			//System.out.println("[TEST] outfile = " + actual.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}

		for (Entry<String, BuildResult> entry : map.entrySet()) {
			//System.out.println("[TEST] unit : " + entry.getKey());
			//System.out.println("[TEST] steps :\n" + entry.getValue().debug());
			BuildResult result = entry.getValue();
			assertThat(result, notNullValue());
			assertThat(result.getFileSteps(), allOf(notNullValue(), not(empty())));
			assertThat(result.getDiffResult(), nullValue());
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingTextFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimply ## countJavaUsingTextFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCount_java_unit.txt");
		String outfile = "test/out/stepCount_java_unit.txt";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJava("JavaTextJob", outfile, "text", true));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countSjisUsingJsonFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimply ## countSjisUsingJsonFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCount_sjis_unit.json");
		String outfile = "test/out/stepCount_sjis_unit.json";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountSjis("SjisJsonJob", outfile, "json", true));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsWorkspace() {
		System.out.println("## KeisukePublisherUsingStepSimply ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsWorkspace ##");
		URL expected = this.getClass().getResource("stepCount_java_unit.csv");
		URL expected2 = this.getClass().getResource("stepCountFileSet_java_unit_on_ws.csv");
		String outfile = "test/out/stepCount_java_unit_on_ws.csv";
		File actual = null;
		try {
			this.setProject(this.projectMaker().createJobToCountJavaOnWorkspace(
					"JavaCsvOnWsJob", outfile, "csv", true));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), stringContainsInOrder(
				Arrays.asList(textContentOf(expected).split(System.getProperty("line.separator")))));
		assertThat(rawContentOf(actual), stringContainsInOrder(
				Arrays.asList(textContentOf(expected2).split(System.getProperty("line.separator")))));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath() {
		System.out.println("## KeisukePublisherUsingStepSimply ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath ##");
		URL expected = this.getClass().getResource("stepCount_java_unit.csv");
		URL expected2 = this.getClass().getResource("stepCountFileSet_java_unit_on_abs.csv");
		String outfile = "test/out/stepCountSimply_java_unit_on_abs.csv";
		File actual = null;
		try {
			this.setProject(this.projectMaker().createJobToCountJavaOnAbsolutePath("JavaCsvOnAbsJob",
					outfile, "csv", true));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), stringContainsInOrder(
				Arrays.asList(textContentOf(expected).split(System.getProperty("line.separator")))));
		assertThat(rawContentOf(actual), stringContainsInOrder(
				Arrays.asList(textContentOf(expected2).split(System.getProperty("line.separator")))));
	}

	@Test
	public void countJavaUsingCsvFormatAndCustomRuleAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimply ## "
				+ "countJavaUsingCsvFormatAndCustomRuleAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCount_java_rule_unit.csv");
		String outfile = "test/out/stepCount_java_rule_unit.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJavaUsingCustomRule("JavaCsvRuleJob",
					outfile, "csv", true));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingTextFormatAndSjisUsingJsonFormat() {
		System.out.println("## KeisukePublisherUsingStepSimply ## "
				+ "countJavaUsingTextFormatAndSjisUsingJsonFormat ##");
		URL[] expected = {
				this.getClass().getResource("stepCount_java_unit.txt"),
				this.getClass().getResource("stepCount_sjis_unit.json")};
		String outdir = "test/out";
		String[] outprefix = {"stepCount_java_unit1", "stepCount_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".txt",
				outdir + "/" + outprefix[1] + ".json"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "text").setBaseDirInclusion(true),
				new OutputSetting(outfile[1], "json").setBaseDirInclusion(true)};

		try {
			this.setProject(this.projectMaker().createJobToCountJavaAndSjis("JavaTextSjisJsonJob",
					outSettingArray));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			for (int i = 0; i < actual.length; i++) {
				actual[i] = new File(this.workspace(), outfile[i]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		for (int i = 0; i < actual.length; i++) {
			System.out.println(rawContentOf(actual[i]));
			assertThat(rawContentOf(actual[i]), equalTo(textContentOf(expected[i])));
		}
	}
}
