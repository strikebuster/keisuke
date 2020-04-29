package org.jenkinsci.plugins.keisuke.setup;

import java.io.File;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.util.PathUtil;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * abstract class for creator Jenkins job for testing.
 */
class AbstractJobMaker {

	private static  String absSourceDir = PathUtil.getAbsoluteDataPath();
	static {
		new File(absSourceDir).mkdirs();
	}
	private JenkinsRule jenkinsRule = null;
	private InputSettingArranger arranger = null;

	AbstractJobMaker(final JenkinsRule jenkins, final CountingModeEnum mode) {
		this.jenkinsRule = jenkins;
		this.setCountingMode(mode);
	}

	/**
	 * Gets an absolute path of source directory to test.
	 * @return absolute path.
	 */
	protected String absoluteSourceDirectory() {
		return absSourceDir;
	}

	/**
	 * Gets JenkinsRule instance.
	 * @return JenkinsRule instance.
	 */
	protected JenkinsRule jenkinsRule() {
		return this.jenkinsRule;
	}

	/**
	 * Sets CountingMode.
	 * @param mode CountingModeEnum instance.
	 */
	private void setCountingMode(final CountingModeEnum mode) {
		this.arranger = createArranger(mode);
	}

	/**
	 * Gets InputSettingArranger instance.
	 * @return InputSettingArranger instance.
	 */
	protected InputSettingArranger arranger() {
		return this.arranger;
	}

	/**
	 * Creates InputSettingArranger instance depends on CountingModeEnum.
	 * @param mode CountingModeEnum instance.
	 * @return InputSettingArranger instance.
	 */
	private static InputSettingArranger createArranger(final CountingModeEnum mode) {
		if (mode.isFileSet()) {
			return new FileSetArranger();
		} else if (mode.isDiffToo()) {
			return new DiffTooArranger();
		} else {
			return new StepSimplyArranger();
		}
	}

}
