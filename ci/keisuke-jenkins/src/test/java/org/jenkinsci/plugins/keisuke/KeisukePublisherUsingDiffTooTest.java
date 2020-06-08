package org.jenkinsci.plugins.keisuke;

import static keisuke.util.TestUtil.nameOfSystemOS;
import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jenkinsci.plugins.keisuke.setup.ProjectMaker;
import org.junit.Before;
import org.junit.Test;

import hudson.model.FreeStyleBuild;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * countingMode is diff_too.
 */
public class KeisukePublisherUsingDiffTooTest extends AbstractProjectTest {

	@Override @Before
	public void setUp() throws Exception {
		this.setProjectMaker(new ProjectMaker(this.jenkinsRule, CountingModeEnum.BOTH_STEP_AND_DIFF));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingDiffToo ## countJavaUsingCsvFormatAsOneUnit ##");
		URL[] expected = {
				this.getClass().getResource("stepCount_java_unit.csv"),
				this.getClass().getResource("diffCount_java_unit.csv")};
		String[] outfile = {
				"test/out/stepCountDiffToo_java_unit.csv",
				"test/out/diffCount_java_unit.csv"};
		File[] actual = new File[2];
		Map<String, BuildResult> map = null;
		try {
			this.setProject(this.projectMaker().createJobToCountJavaDiffToo(
					"JavaCsvJob", outfile[0], "csv", true, outfile[1], "csv"));
			FreeStyleBuild build = this.jenkinsRule.buildAndAssertSuccess(this.project());
			List<StepCountBuildAction> actions = build.getActions(StepCountBuildAction.class);
			StepCountBuildAction action = actions.get(actions.size() - 1);
			map = action.getStepsMap();
			//System.out.println("[TEST] Workspace is " + this.workspace().getAbsolutePath());
			for (int i = 0; i < 2; i++) {
				actual[i] = new File(this.workspace(), outfile[i]);
				//System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}

		assertThat(map.size(), is(1));
		for (Entry<String, BuildResult> entry : map.entrySet()) {
			//System.out.println("[TEST] unit : " + entry.getKey());
			//System.out.println("[TEST] steps :\n" + entry.getValue().debug());
			BuildResult result = entry.getValue();
			assertThat(result, is(notNullValue()));
			assertThat(result.getFileSteps(), is(allOf(notNullValue(), not(empty()))));
			assertThat(result.getDiffResult(), is(notNullValue()));
		}
		for (int i = 0; i < 2; i++) {
			System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			System.out.println(rawContentOf(actual[i]));
			assertThat(rawContentOf(actual[i]), is(equalTo(textContentOf(expected[i]))));
		}
	}

	@Test
	public void countJavaUsingCsvFormatAndCustomRuleAsOneUnit() {
		System.out.println("## KeisukePublisherUsingDiffToo ## "
				+ "countJavaUsingCsvFormatAndCustomRuleAsOneUnit ##");

		URL[] expected = {
				this.getClass().getResource("stepCount_java_rule_unit.csv"),
				this.getClass().getResource("diffCount_java_rule_unit.csv")};
		String[] outfile = {
				"test/out/stepCountDiffToo_java_rule_unit.csv",
				"test/out/diffCount_java_rule_unit.csv"};
		File[] actual = new File[2];

		try {
			this.setProject(this.projectMaker().createJobToCountJavaUsingCustomRuleDiffToo(
					"JavaCsvRuleDiffJob", outfile[0], "csv", true, outfile[1], "csv"));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			for (int i = 0; i < 2; i++) {
				actual[i] = new File(this.workspace(), outfile[i]);
				//System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}

		for (int i = 0; i < 2; i++) {
			System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			System.out.println(rawContentOf(actual[i]));
			assertThat(rawContentOf(actual[i]), is(equalTo(textContentOf(expected[i]))));
		}
	}

	@Test
	public void countJavaUsingTextFormatAndSjisUsingJsonFormat() {
		System.out.println("## KeisukePublisherUsingDiffToo ## "
				+ "countJavaUsingTextFormatAndSjisUsingJsonFormat ##");

		URL[][] expected = {
			{this.getClass().getResource("stepCount_java_unit.txt"),
				null},
			{this.getClass().getResource("stepCountNoBase_sjis_unit.json"),
				this.getClass().getResource("diffCountNoBase_sjis_unit.csv")}
		};
		if (nameOfSystemOS().startsWith("Windows")) {
			// encoding="MS932"
			expected[0][1] = this.getClass().getResource("diffCount_java_unit_win.xml");
		} else {
			// encoding="UTF-8"
			expected[0][1] = this.getClass().getResource("diffCount_java_unit.xml");
		}

		String outdir = "test/out";
		String[][] outfile = {
			{outdir + "/stepCount_java_unit1.txt",
				outdir + "/diffCount_java_unit1.xml"},
			{outdir + "/stepCount_sjis_unit2.json",
				outdir + "/diffCount_sjis_unit2.csv"}
		};

		File[][] actual = new File[2][2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0][0], "text").setBaseDirInclusion(true)
					.setDiffOutputSetting(new OutputSettingForDiff(outfile[0][1], "xml")),
				new OutputSetting(outfile[1][0], "json").setBaseDirInclusion(false)
					.setDiffOutputSetting(new OutputSettingForDiff(outfile[1][1], "csv"))};

		try {
			this.setProject(this.projectMaker().createJobToCountJavaAndSjis(
					"DiffTooJavaTextXmlSjisJsonCsvJob", outSettingArray));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			for (int i = 0; i < actual.length; i++) {
				for (int j = 0; j < actual[i].length; j++) {
					actual[i][j] = new File(this.workspace(), outfile[i][j]);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}

		for (int i = 0; i < actual.length; i++) {
			for (int j = 0; j < actual[i].length; j++) {
				System.out.println("[TEST] outfile[" + i + "][" + j + "] = "
						+ actual[i][j].getAbsolutePath());
				System.out.println(rawContentOf(actual[i][j]));
				assertThat(rawContentOf(actual[i][j]), is(equalTo(textContentOf(expected[i][j]))));
			}
		}
	}
}
