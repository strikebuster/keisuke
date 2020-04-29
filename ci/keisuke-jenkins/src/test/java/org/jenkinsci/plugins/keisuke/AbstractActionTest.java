package org.jenkinsci.plugins.keisuke;

import static org.junit.Assert.fail;

import org.jenkinsci.plugins.keisuke.setup.ProjectMaker;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.jvnet.hudson.test.BuildWatcher;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;

/**
 * Preparing procedure that creates FreeStyleProject and execute FreeStyleBuild.
 */
public abstract class AbstractActionTest implements JenkinsUITester {

	private JenkinsRule.WebClient webClient = null;
	private FreeStyleProject project = null;
	private String projName = null;
	private ProjectMaker projMaker = null;

	// JenkinsUITester methods.
	/**
	 * Gets JenkinsRule instance.
	 * @return JenkinsRule instance.
	 */
	public JenkinsRule jenkinsRule() {
		return this.jenkinsRule;
	}

	/**
	 * Gets JenkinsRule.WebClient instance.
	 * @return JenkinsRule.WebClient instance.
	 */
	public JenkinsRule.WebClient webClient() {
		return this.webClient;
	}

	// Test rule methods.
	@Before
	public void setUp() throws Exception {
		this.webClient = this.jenkinsRule.createWebClient();
		//this.projMaker = new ProjectMaker(this.jenkinsRule, CountingModeEnum.ONLY_STEP_USING_FILE_SET);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();

	@ClassRule
	public static BuildWatcher buildWatch = new BuildWatcher();

	// common methods for this action result page test.
	/**
	 * Prepares to open page of ProjectAction or BuildAction.
	 * create project to count java source codes using ONLY_STEP_USING_FILE_SET.
	 * execute builds some times.
	 * @param times build running times.
	 * @return last build instance.
	 */
	protected FreeStyleBuild prepareProjectToCountJavaThenDoingBuildsBy(final int times) {
		return this.prepareProjectToCountJava("JavaJob", CountingModeEnum.ONLY_STEP_USING_FILE_SET, times);
	}

	/**
	 * Prepares to open page of ProjectAction or BuildAction.
	 * create project to count java source codes using counting mode ONLY_STEP_SIMPLY.
	 * execute builds some times.
	 * @param times build running times.
	 * @return last build instance.
	 */
	protected FreeStyleBuild prepareProjectToCountJavaUsingStepSimplyThenDoingBuildsBy(final int times) {
		return this.prepareProjectToCountJava("JavaJobSimply", CountingModeEnum.ONLY_STEP_SIMPLY, times);
	}

	/**
	 * Prepares to open page of ProjectAction or BuildAction.
	 * create project to count java source codes using counting mode BOTH_STEP_AND_DIFF.
	 * execute builds some times.
	 * @param times build running times.
	 * @return last build instance.
	 */
	protected FreeStyleBuild prepareProjectToCountJavaUsingDiffTooThenDoingBuildsBy(final int times) {
		return this.prepareProjectToCountJava("JavaJobDiff", CountingModeEnum.BOTH_STEP_AND_DIFF, times);
	}

	private FreeStyleBuild prepareProjectToCountJava(final String name, final CountingModeEnum mode,
			final int times) {
		this.projName = name;
		FreeStyleBuild build = null;
		this.projMaker = new ProjectMaker(this.jenkinsRule, mode);
		try {
			this.project = this.projMaker.createJobToCountJava(this.projName, null, null, false);
			for (int i = 0; i < times; i++) {
				build = this.jenkinsRule.buildAndAssertSuccess(this.project);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println("[TEST] === show project page after " + Integer.toString(times)
				+ " times builds done.");
		//this.mainUI = new ProjectMainUI(this.webClient, this.projName);
		return build;
	}

	/**
	 * Prepares to open page of ProjectAction or BuildAction.
	 * create project to count java and sjis source codes.
	 * execute builds some times.
	 * @param times build running times.
	 * @return last build instance.
	 */
	protected FreeStyleBuild prepareProjectToCountJavaAndSjisThenDoingBuildsBy(final int times) {
		return this.prepareProjectToCountJavaAndSjis("JavaSjisJob",
				CountingModeEnum.ONLY_STEP_USING_FILE_SET, times);
	}

	/**
	 * Prepares to open page of ProjectAction or BuildAction.
	 * create project to count java and sjis source codes using counting mode ONLY_STEP_SIMPLY.
	 * execute builds some times.
	 * @param times build running times.
	 * @return last build instance.
	 */
	protected FreeStyleBuild prepareProjectToCountJavaAndSjisUsingStepSimplyThenDoingBuildsBy(final int times) {
		return this.prepareProjectToCountJavaAndSjis("JavaJobSimply", CountingModeEnum.ONLY_STEP_SIMPLY, times);
	}

	/**
	 * Prepares to open page of ProjectAction or BuildAction.
	 * create project to count java and sjis source codes using counting mode BOTH_STEP_AND_DIFF.
	 * execute builds some times.
	 * @param times build running times.
	 * @return last build instance.
	 */
	protected FreeStyleBuild prepareProjectToCountJavaAndSjisUsingDiffTooThenDoingBuildsBy(final int times) {
		return this.prepareProjectToCountJavaAndSjis("JavaJobDiff", CountingModeEnum.BOTH_STEP_AND_DIFF, times);
	}

	private FreeStyleBuild prepareProjectToCountJavaAndSjis(final String name, final CountingModeEnum mode,
			final int times) {
		this.projName = name;
		FreeStyleBuild build = null;
		this.projMaker = new ProjectMaker(this.jenkinsRule, mode);
		try {
			this.project = this.projMaker.createJobToCountJavaAndSjis(this.projName, null);
			for (int i = 0; i < times; i++) {
				build = this.jenkinsRule.buildAndAssertSuccess(this.project);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		System.out.println("[TEST] === show project page after " + Integer.toString(times)
				+ " times builds done.");
		return build;
	}

	/**
	 * Gets FreeStyleProject instance.
	 * @return FreeStyleProject instance.
	 */
	protected FreeStyleProject project() {
		return this.project;
	}

	/**
	 * Gets name of this FreeStyleProject instance.
	 * @return name of project.
	 */
	protected String projName() {
		return this.projName;
	}

}
