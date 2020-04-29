package org.jenkinsci.plugins.keisuke;

import java.io.File;
import java.io.IOException;

import org.jenkinsci.plugins.keisuke.setup.ProjectMaker;
import org.junit.Before;
import org.junit.ClassRule;
import org.jvnet.hudson.test.BuildWatcher;

import hudson.model.FreeStyleProject;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * countingMode is step_simply
 */
public abstract class AbstractProjectTest extends AbstractJobTest {

	private FreeStyleProject project = null;
	private ProjectMaker projMaker = null;

	@Override @Before
	public void setUp() throws Exception {
		this.setProjectMaker(new ProjectMaker(this.jenkinsRule, CountingModeEnum.ONLY_STEP_SIMPLY));
	}

	@ClassRule
	public static BuildWatcher buildWatch = new BuildWatcher();

	/**
	 * Gets ProjectMaker instance.
	 * @return ProjectMaker instance.
	 */
	protected ProjectMaker projectMaker() {
		return this.projMaker;
	}

	/**
	 * Sets ProjectMaker instance.
	 * @param maker ProjectMaker instance.
	 */
	protected void setProjectMaker(final ProjectMaker maker) {
		this.projMaker = maker;
	}

	/**
	 * Gets testing FreeStyleProject.
	 * @return FreeStyleProject
	 */
	protected FreeStyleProject project() {
		return this.project;
	}

	/**
	 * Sets FreeStyleProject to be tested.
	 * @param proj FreeStyleProject
	 */
	protected void setProject(final FreeStyleProject proj) {
		this.project = proj;
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
		return new File(this.jenkinsRule.jenkins.getWorkspaceFor(this.project).toURI());
	}
}
