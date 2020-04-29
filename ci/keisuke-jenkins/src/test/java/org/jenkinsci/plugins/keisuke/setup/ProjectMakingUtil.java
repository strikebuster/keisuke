package org.jenkinsci.plugins.keisuke.setup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.DisplaySetting;
import org.jenkinsci.plugins.keisuke.KeisukePublisher;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.FreeStyleProject;

/**
 * Utility to create Jenkins freestyle projects for testing.
 */
public final class ProjectMakingUtil {

	private ProjectMakingUtil() { }

	public static FreeStyleProject createProjectJob(final JenkinsRule jenkins, final String name)
			throws IOException {
		return jenkins.createFreeStyleProject(name);
	}

	public static FreeStyleProject createProjectJobWithKeisukePublisher(
			final JenkinsRule jenkinsRule, final String name, final DisplaySetting displaySetting,
			final List<CountingUnit> countingUnits) throws IOException {
		FreeStyleProject proj = createProjectJob(jenkinsRule, name);
		KeisukePublisher publisher = new KeisukePublisher(countingUnits);
		publisher.setDisplaySetting(displaySetting);
		proj.getPublishersList().add(publisher);
		return proj;
	}

	static FreeStyleProject createProjectJobAndSetScm(final JenkinsRule jenkinsRule,
			final String name, final DisplaySetting displaySetting,
			final List<CountingUnit> countingUnits) throws IOException, InterruptedException {

		FreeStyleProject proj = createProjectJobWithKeisukePublisher(
				jenkinsRule, name, displaySetting, countingUnits);
		return prepareTestData(proj);
	}

	static FreeStyleProject createProjectJobAndSetScmForAbsolutePath(final JenkinsRule jenkinsRule,
			final String name, final DisplaySetting displaySetting,
			final List<CountingUnit> countingUnits, final String dest)
			throws IOException, InterruptedException {

		FreeStyleProject proj = createProjectJobWithKeisukePublisher(
				jenkinsRule, name, displaySetting, countingUnits);
		return prepareTestData(proj, dest);
	}

	private static FreeStyleProject prepareTestData(final FreeStyleProject project)
			throws IOException, MalformedURLException {
		project.setScm(new PrepareDataSCM());
		return project;
	}

	private static FreeStyleProject prepareTestData(final FreeStyleProject project,
			final String dest) throws IOException, MalformedURLException {
		project.setScm(new PrepareDataSCM().setDestination(dest));
		return project;
	}
}
