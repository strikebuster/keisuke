package org.jenkinsci.plugins.keisuke.setup;

import static org.jenkinsci.plugins.keisuke.setup.PipelineMakingUtil.createJobToCount;
import static org.jenkinsci.plugins.keisuke.setup.PipelineMakingUtil.createJobToCountForAbsolutePath;
import static org.jenkinsci.plugins.keisuke.setup.TestDataConstant.CUSTOM_LANGUAGE_XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.DisplaySetting;
import org.jenkinsci.plugins.keisuke.OutputSetting;
import org.jenkinsci.plugins.keisuke.OutputSettingForDiff;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Creator Jenkins pipeline workflow job for testing.
 */
public class PipelineJobMaker extends AbstractJobMaker {

	public PipelineJobMaker(final JenkinsRule jenkins, final CountingModeEnum mode) {
		super(jenkins, mode);
	}

	/**
	 * Creates a WorkflowJob which is configured to count dummy.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountDummy() throws IOException {
		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		countingUnits.add(new CountingUnit(this.arranger()
				.createInputSettingToCountDummy()));
		return createJobToCount(this.jenkinsRule(), "DummyJob", displaySetting, countingUnits);
	}

	/**
	 * Creates a WorkflowJob which is configured to count java test data.
	 * @param name job name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountJava(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountJava());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createJobToCount(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a WorkflowJob which is configured to count java in ".".
	 * @param name job name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountJavaOnWorkspace(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountJavaAtWorkspace());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createJobToCount(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a WorkflowJob which is configured to count java in absolute path.
	 * @param name job name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountJavaOnAbsolutePath(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger()
				.createInputSettingToCountJavaAtAbsoluteDir(this.absoluteSourceDirectory()));
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createJobToCountForAbsolutePath(this.jenkinsRule(), name,
				displaySetting, countingUnits, this.absoluteSourceDirectory());
	}

	/**
	 * Creates jenkins WorkflowJob instance.
	 * its inputSetting configures to count java test data with customized rule.
	 * @param name job name.
	 * @param opath output file path.
	 * @param format output format.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountJavaUsingCustomRule(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(
				this.arranger().createInputSettingToCountJavaUsingCustomRule(CUSTOM_LANGUAGE_XML));
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createJobToCount(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a WorkflowJob which is configured to count java test data.
	 * @param name job name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountJavaUsingDiffToo(final String name, final String[] opath,
			final String[] format, final boolean baseInclusion) throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountJava());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath[0], format[0])
					.setBaseDirInclusion(baseInclusion)
					.setDiffOutputSetting(new OutputSettingForDiff(opath[1], format[1])));
		}
		countingUnits.add(unit);
		return createJobToCount(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates jenkins WorkflowJob instance.
	 * its inputSetting configures to count sjis test data.
	 * @param name job name.
	 * @param opath output file path.
	 * @param format output format.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountSjis(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountWin31j());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createJobToCount(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates jenkins WorkflowJob instance.
	 * its inputSettings configure to count java and sjis test data.
	 * its outputSettings are given by arguments.
	 * @param name job name.
	 * @param outArray array of outputSettings.
	 * @return WorkflowJob instance.
	 * @throws IOException signal of error.
	 */
	public WorkflowJob createJobToCountJavaAndSjis(final String name, final OutputSetting[] outArray)
			throws IOException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit[] units = {
				new CountingUnit(this.arranger().createInputSettingToCountJava()),
				new CountingUnit(this.arranger().createInputSettingToCountSjis())};
		for (int i = 0; i < units.length; i++) {
			if (outArray != null && outArray[i] != null) {
				units[i].setOutputSetting(outArray[i]);
			}
			countingUnits.add(units[i]);
		}
		return createJobToCount(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

}
