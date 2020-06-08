package org.jenkinsci.plugins.keisuke;

import static keisuke.count.CountTestUtil.binaryContentOf;
import static keisuke.count.CountTestUtil.rawContentOf;
import static keisuke.count.CountTestUtil.textContentOf;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
 * countingMode is file_set.
 */
public class KeisukePublisherUsingFileSetTest extends AbstractProjectTest {

	@Override @Before
	public void setUp() throws Exception {
		this.setProjectMaker(new ProjectMaker(this.jenkinsRule, CountingModeEnum.ONLY_STEP_USING_FILE_SET));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSet ## countJavaUsingCsvFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_unit.csv");
		String outfile = "test/out/stepCountFileSet_java_unit.csv";
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

		assertThat(map.size(), is(1));
		for (Entry<String, BuildResult> entry : map.entrySet()) {
			//System.out.println("[TEST] unit : " + entry.getKey());
			//System.out.println("[TEST] steps :\n" + entry.getValue().debug());
			BuildResult result = entry.getValue();
			assertThat(result, is(notNullValue()));
			assertThat(result.getFileSteps(), is(allOf(notNullValue(), not(empty()))));
			assertThat(result.getDiffResult(), is(nullValue()));
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingTextFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSet ## countJavaUsingTextFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_unit.txt");
		String outfile = "test/out/stepCountFileSet_java_unit.txt";
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
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countSjisUsingJsonFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSet ## countSjisUsingJsonFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCount_sjis_unit.json");
		String outfile = "test/out/stepCountFileSet_sjis_unit.json";
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
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countSjisUsingXmlFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSet ## countSjisUsingXmlFormatAsOneUnit ##");
		URL expected = null;
		if (nameOfSystemOS().startsWith("Windows")) {
			// encoding="MS932"
			expected = this.getClass().getResource("stepCount_sjis_unit_win.xml");
		} else {
			// encoding="UTF-8"
			expected = this.getClass().getResource("stepCount_sjis_unit.xml");
		}
		String outfile = "test/out/stepCountFileSet_sjis_unit.xml";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountSjis("SjisXmlJob", outfile, "xml", true));
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
		System.out.println("## KeisukePublisherUsingFileSet ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsWorkspace ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_unit_on_ws.csv");
		String outfile = "test/out/stepCountFileSet_java_unit_on_ws.csv";
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
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath() {
		System.out.println("## KeisukePublisherUsingFileSet ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_unit_on_abs.csv");
		String outfile = "test/out/stepCountFileSet_java_unit_on_abs.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJavaOnAbsolutePath(
					"JavaCsvOnAbsJob", outfile, "csv", true));
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
	public void countJavaUsingCsvFormatAndCustomRuleAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSet ## "
				+ "countJavaUsingCsvFormatAndCustomRuleAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_rule_unit.csv");
		String outfile = "test/out/stepCountFileSet_java_rule_unit.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJavaUsingCustomRule(
					"JavaCsvRuleJob", outfile, "csv", true));
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
	public void countJavaAndSjisUsingCsvFormatAsTwoUnits() {
		System.out.println("## KeisukePublisherUsingFileSet ## countJavaAndSjisUsingCsvFormatAsTwoUnits ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSet_java_unit.csv"),
				this.getClass().getResource("stepCount_sjis_unit.csv")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountFileSet_java_unit1", "stepCountFileSet_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".csv",
				outdir + "/" + outprefix[1] + ".csv"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "csv").setBaseDirInclusion(true),
				new OutputSetting(outfile[1], "csv").setBaseDirInclusion(true)};

		try {
			this.setProject(this.projectMaker().createJobToCountJavaAndSjis("JavaSjisCsvJob",
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

	@Test
	public void countJavaAndSjisUsingExcelFormatAsTwoUnits() {
		System.out.println("## KeisukePublisherUsingFileSet ## countJavaAndSjisUsingExcelFormatAsTwoUnits ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSet_java_unit.xls"),
				this.getClass().getResource("stepCount_sjis_unit.xls")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountFileSet_java_unit1", "stepCountFileSet_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".xls",
				outdir + "/" + outprefix[1] + ".xls"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "excel").setBaseDirInclusion(true),
				new OutputSetting(outfile[1], "excel").setBaseDirInclusion(true)};

		try {
			this.setProject(this.projectMaker().createJobToCountJavaAndSjis(
					"JavaSjisExcelJob", outSettingArray));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			for (int i = 0; i < actual.length; i++) {
				actual[i] = new File(this.workspace(), outfile[i]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		/*
		System.out.println("[TEST PAUSE] 30 sec pausing.");
		try {
			Thread.sleep(30 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		for (int i = 0; i < actual.length; i++) {
			assertThat(binaryContentOf(actual[i]), is(equalTo(binaryContentOf(expected[i]))));
		}
	}

	@Test
	public void countJavaUsingTextFormatAndSjisUsingJsonFormat() {
		System.out.println("## KeisukePublisherUsingFileSet ## "
				+ "countJavaUsingTextFormatAndSjisUsingJsonFormat ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSet_java_unit.txt"),
				this.getClass().getResource("stepCount_sjis_unit.json")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountFileSet_java_unit1", "stepCountFileSet_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".txt",
				outdir + "/" + outprefix[1] + ".json"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "text").setBaseDirInclusion(true),
				new OutputSetting(outfile[1], "json").setBaseDirInclusion(true)};

		try {
			this.setProject(this.projectMaker().createJobToCountJavaAndSjis(
					"JavaTextSjisJsonJob", outSettingArray));
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
