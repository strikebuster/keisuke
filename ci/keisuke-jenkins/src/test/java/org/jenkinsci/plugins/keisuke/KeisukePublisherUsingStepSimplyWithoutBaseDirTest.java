package org.jenkinsci.plugins.keisuke;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.junit.Test;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * countingMode is step_simply.
 * baseDirInclusion is false.
 */
public class KeisukePublisherUsingStepSimplyWithoutBaseDirTest extends AbstractProjectTest {

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountNoBase_java_unit.csv");
		String outfile = "test/out/stepCountNoBase_java_unit.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJava("JavaCsvJob", outfile, "csv", false));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			//System.out.println("[TEST] Workspace is " + this.workspace().getAbsolutePath());
			actual = new File(this.workspace(), outfile);
			//System.out.println("[TEST] outfile = " + actual.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingTextFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countJavaUsingTextFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountNoBase_java_unit.txt");
		String outfile = "test/out/stepCountNoBase_java_unit.txt";
		File actual = null;

		try {
			this.setProject(this.projectMaker()
					.createJobToCountJava("JavaTextJob", outfile, "text", false));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countSjisUsingJsonFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countSjisUsingJsonFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountNoBase_sjis_unit.json");
		String outfile = "test/out/stepCountNoBase_sjis_unit.json";
		File actual = null;

		try {
			this.setProject(this.projectMaker()
					.createJobToCountSjis("SjisJsonJob", outfile, "json", false));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsWorkspace() {
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsWorkspace ##");
		URL expected = this.getClass().getResource("stepCount_java_unit.csv");
		URL expected2 = this.getClass().getResource("stepCountFileSetNoBase_java_unit_on_ws.csv");
		String outfile = "test/out/stepCountNoBase_java_unit_on_ws.csv";
		File actual = null;
		try {
			this.setProject(this.projectMaker().createJobToCountJavaOnWorkspace(
					"JavaCsvOnWsJob", outfile, "csv", false));
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
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath ##");
		URL expected = this.getClass().getResource("stepCount_java_unit.csv");
		URL expected2 = this.getClass().getResource("stepCountFileSetNoBase_java_unit_on_ws.csv");
		String outfile = "test/out/stepCountSimplyNoBase_java_unit_on_abs.csv";
		File actual = null;
		try {
			this.setProject(this.projectMaker().createJobToCountJavaOnAbsolutePath("JavaCsvOnAbsJob",
					outfile, "csv", false));
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
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAndCustomRuleAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountNoBase_java_rule_unit.csv");
		String outfile = "test/out/stepCountNoBase_java_rule_unit.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJavaUsingCustomRule("JavaCsvRuleJob",
					outfile, "csv", false));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingTextFormatAndSjisUsingJsonFormat() {
		System.out.println("## KeisukePublisherUsingStepSimplyWithoutBaseDir ## "
				+ "countJavaUsingTextFormatAndSjisUsingJsonFormat ##");
		URL[] expected = {
				this.getClass().getResource("stepCountNoBase_java_unit.txt"),
				this.getClass().getResource("stepCountNoBase_sjis_unit.json")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountNoBase_java_unit1", "stepCountNoBase_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".txt",
				outdir + "/" + outprefix[1] + ".json"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "text").setBaseDirInclusion(false),
				new OutputSetting(outfile[1], "json").setBaseDirInclusion(false)};

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
			assertThat(rawContentOf(actual[i]), is(equalTo(textContentOf(expected[i]))));
		}
	}
}
