package org.jenkinsci.plugins.keisuke;

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

import org.jenkinsci.plugins.keisuke.setup.PipelineJobMaker;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing KeisukePublisher in Pipeline Workflow Job, using DiffToo mode.
 */
public class KeisukePublisherPipelineUsingDiffTooTest extends AbstractPipelineTest {

	@Override @Before
	public void setUp() throws Exception {
		this.setPipelineJobMaker(
				new PipelineJobMaker(this.jenkinsRule, CountingModeEnum.BOTH_STEP_AND_DIFF));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherPipelineUsingDiffToo ## countJavaUsingCsvFormatAsOneUnit ##");

		URL[] expected = {
				this.getClass().getResource("stepCount_java_unit.csv"),
				this.getClass().getResource("diffCount_java_unit.csv")};
		String[] outfile = {
				"test/out/wfSC_java_unit.csv",
				"test/out/wfDC_java_unit.csv"};
		String[] format = {"csv", "csv"};
		Map<String, BuildResult> map = null;
		File[] actual = new File[2];

		try {
			this.setWorkflowJob(this.pipelineJobMaker()
					.createJobToCountJavaUsingDiffToo("JavaCsvWfJob", outfile, format, true));
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
			List<StepCountBuildAction> actions = run.getActions(StepCountBuildAction.class);
			StepCountBuildAction action = actions.get(actions.size() - 1);
			map = action.getStepsMap();
			//System.out.println("[TEST] Workspace is " + this.workspace().getAbsolutePath());
			for (int i = 0; i < 2; i++) {
				actual[i] = new File(this.workspace(), outfile[i]);
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
			assertThat(result, notNullValue());
			assertThat(result.getFileSteps(), allOf(notNullValue(), not(empty())));
			assertThat(result.getDiffResult(), notNullValue());
		}
		for (int i = 0; i < 2; i++) {
			System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			System.out.println(rawContentOf(actual[i]));
			assertThat(rawContentOf(actual[i]), equalTo(textContentOf(expected[i])));
		}
	}
}
