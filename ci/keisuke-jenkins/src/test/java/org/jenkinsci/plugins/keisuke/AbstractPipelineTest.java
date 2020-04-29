package org.jenkinsci.plugins.keisuke;

import java.io.File;
import java.io.IOException;

import org.jenkinsci.plugins.keisuke.setup.PipelineJobMaker;
import org.jenkinsci.plugins.keisuke.setup.PipelineMakingUtil;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Before;

/**
 * Testing KeisukePublisher in Pipeline Workflow Job,
 * countingMode is step_simply
 */
public class AbstractPipelineTest extends AbstractJobTest {

	private WorkflowJob wfJob = null;
	private PipelineJobMaker wfJobMaker = null;

	@Override @Before
	public void setUp() throws Exception {
		this.setPipelineJobMaker(new PipelineJobMaker(this.jenkinsRule, CountingModeEnum.ONLY_STEP_SIMPLY));
	}

	/**
	 * Gets PipelineJobMaker instance.
	 * @return PipelineJobMaker instance.
	 */
	protected PipelineJobMaker pipelineJobMaker() {
		return this.wfJobMaker;
	}

	/**
	 * Sets PipelineJobMaker instance.
	 * @param maker PipelineJobMaker instance.
	 */
	protected void setPipelineJobMaker(final PipelineJobMaker maker) {
		this.wfJobMaker = maker;
	}

	/**
	 * Gets testing WorkFlowJob.
	 * @return WorkFlowJob.
	 */
	protected WorkflowJob workflowJob() {
		return this.wfJob;
	}

	/**
	 * Sets WorkFlowJob to be tested.
	 * @param job WorkFlowJob
	 */
	protected void setWorkflowJob(final WorkflowJob job) {
		this.wfJob = job;
	}

	/**
	 * Gets workspace root directory.
	 * @return File instance of workspace.
	 * @throws InterruptedException signal of error.
	 * @throws IOException signal of error.
	 */
	@Override
	protected File workspace()
			throws InterruptedException, IOException {
		return PipelineMakingUtil.workspace(this.jenkinsRule, this.wfJob);
	}

}
