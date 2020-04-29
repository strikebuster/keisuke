package org.jenkinsci.plugins.keisuke;

import static keisuke.count.CountTestUtil.binaryContentOf;
import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
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

import org.jenkinsci.plugins.keisuke.setup.PipelineJobMaker;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing KeisukePublisher in Pipeline Workflow Job, using FileSet mode.
 */
public class KeisukePublisherPipelineUsingFileSetTest extends AbstractPipelineTest {

	@Override @Before
	public void setUp() throws Exception {
		this.setPipelineJobMaker(
				new PipelineJobMaker(this.jenkinsRule, CountingModeEnum.ONLY_STEP_USING_FILE_SET));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherPipelineUsingFileSet ## countJavaUsingCsvFormatAsOneUnit ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_unit.csv");
		String outfile = "test/out/wfSCFileSet_java_unit.csv";
		Map<String, BuildResult> map = null;
		File actual = null;

		try {
			this.setWorkflowJob(this.pipelineJobMaker()
					.createJobToCountJava("JavaCsvWfJob", outfile, "csv", true));
			//prepareTestData(this.jenkinsRule, this.workflowJob());
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());

			List<StepCountBuildAction> actions = run.getActions(StepCountBuildAction.class);
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
			assertThat(result, notNullValue());
			assertThat(result.getFileSteps(), allOf(notNullValue(), not(empty())));
			assertThat(result.getDiffResult(), nullValue());
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingCsvFormatWhenBaseDirIsWorkspaceWithoutBaseDir() {
		System.out.println("## KeisukePublisherPipelineUsingFileSet ## "
				+ "countJavaUsingCsvFormatWhenBaseDirIsWorkspaceWithoutBaseDir ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_unit_on_ws.csv");
		String outfile = "test/out/wfSCFileSetNoBase_java_unit_on_ws.csv";
		File actual = null;

		try {
			this.setWorkflowJob(this.pipelineJobMaker()
					.createJobToCountJavaOnWorkspace("JavaCsvOnWsWfJob", outfile, "csv", false));
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingCsvFormatWhenBaseDirIsAbsolutePath() {
		System.out.println("## KeisukePublisherPipelineUsingFileSet ## "
				+ "countJavaUsingCsvFormatWhenBaseDirIsAbsolutePath ##");
		URL expected = this.getClass().getResource("stepCountFileSet_java_unit_on_abs.csv");
		String outfile = "test/out/wfSCFileSet_java_unit_on_abs.csv";
		File actual = null;
		try {
			this.setWorkflowJob(this.pipelineJobMaker()
					.createJobToCountJavaOnAbsolutePath("JavaCsvOnAbsWfJob", outfile, "csv", true));
			//prepareTestDataForAbsolutePath(this.jenkinsRule, this.workflowJob());
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
			actual = new File(this.workspace(), outfile);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual), equalTo(textContentOf(expected)));
	}

	@Test
	public void countJavaUsingCsvFormatAndCustomRuleWithoutBaseDir() {
		System.out.println("## KeisukePublisherPipelineUsingFileSet ## "
				+ "countJavaUsingCsvFormatAndCustomRuleWithoutBaseDir ##");
		URL expected = this.getClass().getResource("stepCountFileSetNoBase_java_rule_unit.csv");
		String outfile = "test/out/wfSCFileSetNoBase_java_rule_unit.csv";
		File actual = null;

		try {
			this.setWorkflowJob(this.pipelineJobMaker().createJobToCountJavaUsingCustomRule(
					"JavaCsvRuleWfJob", outfile, "csv", false));
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
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
	public void countJavaAndSjisUsingExcelFormatAsTwoUnits() {
		System.out.println("## KeisukePublisherPipelineUsingFileSet ## "
				+ "countJavaAndSjisUsingExcelFormatAsTwoUnits ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSet_java_unit.xls"),
				this.getClass().getResource("stepCount_sjis_unit.xls")};
		String outdir = "test/out";
		String[] outprefix = {"wfSCFileSet_java_unit1", "wfSCFileSet_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".xls",
				outdir + "/" + outprefix[1] + ".xls"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "excel"),
				new OutputSetting(outfile[1], "excel")};

		try {
			this.setWorkflowJob(this.pipelineJobMaker()
					.createJobToCountJavaAndSjis("JavaSjisExcelWfJob", outSettingArray));
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
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
	public void countJavaUsingTextFormatAndSjisUsingJsonFormatWithoutBaseDir() {
		System.out.println("## KeisukePublisherPipelineUsingFileSet ## "
				+ "countJavaUsingTextFormatAndSjisUsingJsonFormatWithoutBaseDir ##");
		URL[] expected = {
				this.getClass().getResource("stepCountFileSetNoBase_java_unit.txt"),
				this.getClass().getResource("stepCountNoBase_sjis_unit.json")};
		String outdir = "test/out";
		String[] outprefix = {"wfSCFileSet_java_unit1", "wfSCFileSet_sjis_unit2"};
		String[] outfile = {
				outdir + "/" + outprefix[0] + ".txt",
				outdir + "/" + outprefix[1] + ".json"};
		File[] actual = new File[2];
		OutputSetting[] outSettingArray = {
				new OutputSetting(outfile[0], "text").setBaseDirInclusion(false),
				new OutputSetting(outfile[1], "json").setBaseDirInclusion(false)};

		try {
			this.setWorkflowJob(this.pipelineJobMaker()
					.createJobToCountJavaAndSjis("JavaTextSjisJsonWfJob", outSettingArray));
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
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
