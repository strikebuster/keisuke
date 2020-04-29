package org.jenkinsci.plugins.keisuke.setup;

import static org.jenkinsci.plugins.keisuke.setup.ProjectMakingUtil.createProjectJobAndSetScm;
import static org.jenkinsci.plugins.keisuke.setup.ProjectMakingUtil.createProjectJobAndSetScmForAbsolutePath;
import static org.jenkinsci.plugins.keisuke.setup.ProjectMakingUtil.createProjectJobWithKeisukePublisher;
import static org.jenkinsci.plugins.keisuke.setup.TestDataConstant.CUSTOM_LANGUAGE_XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.DisplaySetting;
import org.jenkinsci.plugins.keisuke.OutputSetting;
import org.jenkinsci.plugins.keisuke.OutputSettingForDiff;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.FreeStyleProject;

/**
 * Creator Jenkins freestyle projects for testing.
 */
public class ProjectMaker extends AbstractJobMaker {

	public ProjectMaker(final JenkinsRule jenkins, final CountingModeEnum mode) {
		super(jenkins, mode);
	}

	/**
	 * Creates a FreeStyleProject which is configured to count dummy.
	 * @param name project name.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountDummy(final String name)
			throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountDummy());
		countingUnits.add(unit);
		return createProjectJobWithKeisukePublisher(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a FreeStyleProject which is configured to count java test data.
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJava(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountJava());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a FreeStyleProject which is configured to count java in ".".
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJavaOnWorkspace(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountJavaAtWorkspace());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a FreeStyleProject which is configured to count java in absolute path.
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJavaOnAbsolutePath(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException, InterruptedException {

		// expect that no java source exists at srcPath
		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger()
				.createInputSettingToCountJavaAtAbsoluteDir(this.absoluteSourceDirectory()));
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScmForAbsolutePath(this.jenkinsRule(),
				name, displaySetting, countingUnits, this.absoluteSourceDirectory());
	}

	/**
	 * Creates jenkins FreeStyleProject instance.
	 * its inputSetting configures to count java test data with customized rule.
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJavaUsingCustomRule(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(
				this.arranger().createInputSettingToCountJavaUsingCustomRule(CUSTOM_LANGUAGE_XML));
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates jenkins FreeStyleProject instance.
	 * its inputSetting configures to count sjis test data.
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountSjis(final String name, final String opath,
			final String format, final boolean baseInclusion) throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountWin31j());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates jenkins FreeStyleProject instance.
	 * its inputSettings configure to count java and sjis test data.
	 * its outputSettings are given by arguments.
	 * @param name project name.
	 * @param outArray array of outputSettings.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJavaAndSjis(final String name, final OutputSetting[] outArray)
			throws IOException, InterruptedException {

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
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a FreeStyleProject which is configured to count java test data.
	 * counting mode is diff_too.
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format type.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @param opath2 diff output file path.
	 * @param format2 diff output format type.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJavaDiffToo(final String name, final String opath,
			final String format, final boolean baseInclusion, final String opath2, final String format2)
			throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(this.arranger().createInputSettingToCountJava());
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion)
					.setDiffOutputSetting(new OutputSettingForDiff(opath2, format2)));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}

	/**
	 * Creates a FreeStyleProject which is configured to count java test data.
	 * counting mode is diff_too.
	 * its inputSetting configures to count java test data with customized rule.
	 * @param name project name.
	 * @param opath output file path.
	 * @param format output format.
	 * @param baseInclusion if file paths start with base directory, then true.
	 * @param opath2 diff output file path.
	 * @param format2 diff output format type.
	 * @return FreeStyleProject instance.
	 * @throws IOException signal of error.
	 * @throws InterruptedException signal of error.
	 */
	public FreeStyleProject createJobToCountJavaUsingCustomRuleDiffToo(final String name, final String opath,
			final String format, final boolean baseInclusion, final String opath2, final String format2)
			throws IOException, InterruptedException {

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		CountingUnit unit = new CountingUnit(
				this.arranger().createInputSettingToCountJavaUsingCustomRule(CUSTOM_LANGUAGE_XML));
		if (opath != null) {
			unit.setOutputSetting(new OutputSetting(opath, format).setBaseDirInclusion(baseInclusion)
					.setDiffOutputSetting(new OutputSettingForDiff(opath2, format2)));
		}
		countingUnits.add(unit);
		return createProjectJobAndSetScm(this.jenkinsRule(), name, displaySetting, countingUnits);
	}
}
