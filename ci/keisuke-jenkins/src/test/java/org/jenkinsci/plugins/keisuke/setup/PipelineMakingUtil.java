package org.jenkinsci.plugins.keisuke.setup;

//import static keisuke.util.TestUtil.copy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.DisplaySetting;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Utility class for testing Jenkins pipeline workflow job
 */
public final class PipelineMakingUtil {

	/*
	private static final String DATA_ZIP_ORIGIN = PrepareDataSCM.DATA_ZIP_PATH;
	private static final String ZIP_NAME = "data4ut.zip";
	private static final String BUILD_XML_ORIGIN = "test/archive/build.xml";
	private static final String BUILD_XML_FOR_ABSOLUTEPATH = "test/archive/build_abs.xml";
	private static final String XML_NAME = "build.xml";
	*/

	private PipelineMakingUtil() { }

	private static WorkflowJob createWorkflowJob(final JenkinsRule jenkinsRule, final String name,
			final String script) throws IOException {
		WorkflowJob job = jenkinsRule.createProject(WorkflowJob.class, name);
		job.setDefinition(new CpsFlowDefinition(script, true));
		return job;
	}

	public static WorkflowJob createJobToCount(final JenkinsRule jenkinsRule, final String name,
			final DisplaySetting displaySetting, final List<CountingUnit> countingUnits)
			throws IOException {
		String script = createJobScript(jenkinsRule, name, displaySetting, countingUnits, "");
		System.out.println("[TEST] script:" + script);
		return createWorkflowJob(jenkinsRule, name, script);
	}

	public static WorkflowJob createJobToCountForAbsolutePath(final JenkinsRule jenkinsRule, final String name,
			final DisplaySetting displaySetting, final List<CountingUnit> countingUnits, final String dest)
			throws IOException {
		String script = createJobScript(jenkinsRule, name, displaySetting, countingUnits, dest);
		System.out.println("[TEST] script:" + script);
		return createWorkflowJob(jenkinsRule, name, script);
	}

	private static String createJobScript(final JenkinsRule jenkinsRule, final String name,
			final DisplaySetting displaySetting, final List<CountingUnit> countingUnits,
			final String dest) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (CountingUnit unit : countingUnits) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(unit.toScript());
		}
		String unitConfig = sb.toString();
		sb = new StringBuilder();
		sb
			.append("node {\n")
			.append("  stage('Preparation') {\n")
			//.append("    if (isUnix()) {\n")
			//.append("      echo 'build on UNIX'\n")
			//.append("      sh 'ant build'\n")
			//.append("    } else {\n")
			//.append("      echo 'build on Windows'\n")
			//.append("      bat 'ant build'\n")
			//.append("    }\n")
			.append("    checkout([$class: 'PrepareDataSCM',\n")
			.append("      destination: '").append(dest).append("'])\n")
			.append("  }\n")
			.append("  stage('StepCount') {\n")
			.append("    step([$class: 'KeisukePublisher',\n")
			.append("      countingUnits: [")
			.append(unitConfig).append("],\n");
		if (displaySetting != null) {
			sb
			.append("      displaySetting: " + displaySetting.toScript() + "\n");
		}
		sb
			.append("    ])\n")
			.append("  }\n")
			.append("}");
		String script = sb.toString();
		return script;
	}

	/*
	public static void prepareTestData(final JenkinsRule jenkinsRule, final WorkflowJob wfJob)
			throws IOException, InterruptedException {
		File wsDir = workspace(jenkinsRule, wfJob);
		wsDir.mkdirs();
		File zipSrc = new File(DATA_ZIP_ORIGIN);
		File zipDest = new File(wsDir, ZIP_NAME);
		copy(zipSrc, zipDest);
		//System.out.println("copy to " + zipDest.getAbsolutePath());
		File xmlSrc = new File(BUILD_XML_ORIGIN);
		File xmlDest = new File(wsDir, XML_NAME);
		copy(xmlSrc, xmlDest);
		//System.out.println("copy to " + xmlDest.getAbsolutePath());
	}

	public static void prepareTestDataForAbsolutePath(final JenkinsRule jenkinsRule, final WorkflowJob wfJob)
			throws IOException, InterruptedException {
		File wsDir = workspace(jenkinsRule, wfJob);
		wsDir.mkdirs();
		File zipSrc = new File(DATA_ZIP_ORIGIN);
		File zipDest = new File(wsDir, ZIP_NAME);
		copy(zipSrc, zipDest);
		//System.out.println("copy to " + zipDest.getAbsolutePath());
		File xmlSrc = new File(BUILD_XML_FOR_ABSOLUTEPATH);
		File xmlDest = new File(wsDir, XML_NAME);
		copy(xmlSrc, xmlDest);
		//System.out.println("copy to " + xmlDest.getAbsolutePath());
	}
	*/

	public static File workspace(final JenkinsRule jenkinsRule, final WorkflowJob wfJob)
			throws InterruptedException, IOException {
		return new File(jenkinsRule.jenkins.getWorkspaceFor(wfJob).toURI());
	}
}
