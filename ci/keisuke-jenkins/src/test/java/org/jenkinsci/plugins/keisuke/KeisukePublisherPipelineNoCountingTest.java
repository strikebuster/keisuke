package org.jenkinsci.plugins.keisuke;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Test;

/**
 * Testing KeisukePublisher in Pipeline Workflow Job,
 * before step counting.
 */
public class KeisukePublisherPipelineNoCountingTest extends AbstractPipelineTest {

	@Test
	public void configureDefaultSettingAboutOtherThanInputSetting() {
		System.out.println("## KeisukePublisherPipelineNoCounting ## "
				+ "configureDefaultSettingAboutOtherThanInputSetting ##");
		Map<String, BuildResult> map = null;

		try {
			this.setWorkflowJob(this.pipelineJobMaker().createJobToCountDummy());
			WorkflowRun run = this.jenkinsRule.buildAndAssertSuccess(this.workflowJob());
			List<StepCountBuildAction> actions = run.getActions(StepCountBuildAction.class);
			StepCountBuildAction action = actions.get(actions.size() - 1);
			map = action.getStepsMap();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		// it's OK to reach here
		for (Entry<String, BuildResult> entry : map.entrySet()) {
			System.out.println("[TEST] unit : " + entry.getKey());
			System.out.println("[TEST] steps :\n" + entry.getValue().debug());
		}
	}
}
