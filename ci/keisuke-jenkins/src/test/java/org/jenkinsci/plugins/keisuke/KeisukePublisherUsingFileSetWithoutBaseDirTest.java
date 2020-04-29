package org.jenkinsci.plugins.keisuke;

import static keisuke.count.CountTestUtil.binaryContentOf;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.jenkinsci.plugins.keisuke.setup.ProjectMaker;
import org.junit.Before;
import org.junit.Test;

import hudson.model.FreeStyleBuild;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * countingMode is file_set.
 * baseDirInclusion is false.
 */
public class KeisukePublisherUsingFileSetWithoutBaseDirTest extends AbstractProjectTest {

	@Override @Before
	public void setUp() throws Exception {
		this.setProjectMaker(new ProjectMaker(this.jenkinsRule, CountingModeEnum.ONLY_STEP_USING_FILE_SET));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_unit.csv");
		String outfile = "test/out/stepCountFileSetNoBase_java_unit.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJava("JavaCsvJob", outfile, "csv", false));
			FreeStyleBuild build = this.jenkinsRule.buildAndAssertSuccess(this.project());
			//System.out.println("[TEST] Workspace is " + this.workspace().getAbsolutePath());
			actual = new File(this.workspace(), outfile);
			//System.out.println("[TEST] outfile = " + actual.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingTextFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaUsingTextFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_unit.txt");
		String outfile = "test/out/stepCountFileSetNoBase_java_unit.txt";
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
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countSjisUsingJsonFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countSjisUsingJsonFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountNoBase_sjis_unit.json");
		String outfile = "test/out/stepCountFileSetNoBase_sjis_unit.json";
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
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countSjisUsingXmlFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countSjisUsingXmlFormatAsOneUnit ##");
		URL expected = null;
		if (nameOfSystemOS().startsWith("Windows")) {
			// encoding="MS932"
			expected = this.getClass().getResource("stepCountNoBase_sjis_unit_win.xml");
		} else {
			// encoding="UTF-8"
			expected = this.getClass().getResource("stepCountNoBase_sjis_unit.xml");
		}
		String outfile = "test/out/stepCountFileSetNoBase_sjis_unit.xml";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountSjis("SjisXmlJob", outfile, "xml", false));
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
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsWorkspace ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_unit_on_ws.csv");
		String outfile = "test/out/stepCountFileSetNoBase_java_unit_on_ws.csv";
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
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnitWhenBaseDirIsAbsolutePath ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_unit_on_ws.csv");
		String outfile = "test/out/stepCountFileSetNoBase_java_unit_on_abs.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJavaOnAbsolutePath(
					"JavaCsvOnAbsJob", outfile, "csv", false));
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
	public void countJavaUsingCsvFormatAndCustomRuleAsOneUnit() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAndCustomRuleAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_rule_unit.csv");
		String outfile = "test/out/stepCountFileSetNoBase_java_rule_unit.csv";
		File actual = null;

		try {
			this.setProject(this.projectMaker().createJobToCountJavaUsingCustomRule(
					"JavaCsvRuleJob", outfile, "csv", false));
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
	public void countJavaAndSjisUsingCsvFormatAsTwoUnits() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaAndSjisUsingCsvFormatAsTwoUnits ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSetNoBase_java_unit.csv"),
				this.getClass().getResource("stepCountNoBase_sjis_unit.csv")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountFileSetNoBase_java_unit1", "stepCountFileSetNoBase_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".csv",
				outdir + "/" + outprefix[1] + ".csv"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "csv").setBaseDirInclusion(false),
				new OutputSetting(outfile[1], "csv").setBaseDirInclusion(false)};

		try {
			this.setProject(this.projectMaker().createJobToCountJavaAndSjis(
					"JavaSjisCsvJob", outSettingArray));
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

	@Test
	public void countJavaAndSjisUsingExcelFormatAsTwoUnits() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaAndSjisUsingExcelFormatAsTwoUnits ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSetNoBase_java_unit.xls"),
				this.getClass().getResource("stepCountNoBase_sjis_unit.xls")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountFileSetNoBase_java_unit1", "stepCountFileSetNoBase_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".xls",
				outdir + "/" + outprefix[1] + ".xls"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "excel").setBaseDirInclusion(false),
				new OutputSetting(outfile[1], "excel").setBaseDirInclusion(false)};

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
		for (int i = 0; i < actual.length; i++) {
			assertThat(binaryContentOf(actual[i]), equalTo(binaryContentOf(expected[i])));
		}
	}

	@Test
	public void countJavaUsingTextFormatAndSjisUsingJsonFormat() {
		System.out.println("## KeisukePublisherUsingFileSetWithoutBaseDir ## "
				+ "countJavaUsingTextFormatAndSjisUsingJsonFormat ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSetNoBase_java_unit.txt"),
				this.getClass().getResource("stepCountNoBase_sjis_unit.json")};
		String outdir = "test/out";
		String[] outprefix = {"stepCountFileSetNoBase_java_unit1", "stepCountFileSetNoBase_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".txt",
				outdir + "/" + outprefix[1] + ".json"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "text").setBaseDirInclusion(false),
				new OutputSetting(outfile[1], "json").setBaseDirInclusion(false)};

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
			assertThat(rawContentOf(actual[i]), equalTo(textContentOf(expected[i])));
		}
	}

}
