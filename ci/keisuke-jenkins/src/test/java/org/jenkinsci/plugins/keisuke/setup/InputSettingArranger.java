package org.jenkinsci.plugins.keisuke.setup;

import static org.jenkinsci.plugins.keisuke.setup.TestDataConstant.JAVA_SRC_ROOT;
import static org.jenkinsci.plugins.keisuke.setup.TestDataConstant.SJIS_SRC_ROOT;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.InputSetting;

/**
 * base class for creating inputSetting to be set up.
 */
abstract class InputSettingArranger {

	private CountingModeEnum countingMode = CountingModeEnum.ONLY_STEP_SIMPLY;

	protected InputSettingArranger(final CountingModeEnum mode) {
		this.countingMode = mode;
	}

	protected InputSettingArranger(final String mode) {
		if (CountingModeEnum.ONLY_STEP_USING_FILE_SET.getValue().equals(mode)) {
			this.countingMode = CountingModeEnum.ONLY_STEP_USING_FILE_SET;
		} else if (CountingModeEnum.BOTH_STEP_AND_DIFF.getValue().equals(mode)) {
			this.countingMode = CountingModeEnum.BOTH_STEP_AND_DIFF;
		}
	}

	/**
	 * Creates InputSetting which is specified to count dummy.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountDummy() {
		return new InputSetting("dummy", "test/data", "UTF-8", "", this.countingMode.getValue());
	}

	/**
	 * Creates InputSetting which is specified to count java test data.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountJava() {
		return this.createInputSettingToCountJavaAtDirectory(JAVA_SRC_ROOT);
	}

	/**
	 * Creates InputSetting which is specified to count java test data in ".".
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountJavaAtWorkspace() {
		return this.createInputSettingToCountJavaAtDirectory(".");
	}

	/**
	 * Creates InputSetting which is specified to count java test data out of workspace.
	 * @param srcdir source directory path.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountJavaAtAbsoluteDir(final String srcdir) {
		return this.createInputSettingToCountJavaAtDirectory(srcdir);
	}

	/**
	 * Creates InputSetting which is specified to count srcdir.
	 * @param srcdir source directory path.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountJavaAtDirectory(final String srcdir) {
		return this.createInputSettingToCountJavaUsingCustomRule(srcdir, "");
	}

	/**
	 * Creates InputSetting which is specified to count srcdir.
	 * @param srcdir source directory path.
	 * @param xml xml file path.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountJavaUsingCustomRule(final String srcdir, final String xml) {
		return new InputSetting("java", srcdir, "UTF-8", xml, this.countingMode.getValue());
	}

	/**
	 * Creates InputSetting which is specified to count java test data.
	 * @param xml xml file path.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountJavaUsingCustomRule(final String xml) {
		return this.createInputSettingToCountJavaUsingCustomRule(JAVA_SRC_ROOT, xml);
	}

	/**
	 * Creates InputSetting which is specified to count sjis test data.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountSjis() {
		return this.createInputSettingToCountSjisWithEncoding("Shift_JIS");
	}

	/**
	 * Creates InputSetting which is specified to count "Windows-31J" test data.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountWin31j() {
		return this.createInputSettingToCountSjisWithEncoding("Windows-31J");
	}

	/**
	 * Creates InputSetting which is specified to count srcdir.
	 * @param srcdir source directory path.
	 * @param encoding source code encoding.
	 * @return InputSetting instance.
	 */
	protected InputSetting createInputSettingToCountSjisWithEncoding(final String srcdir, final String encoding) {
		return new InputSetting("commentS", srcdir, encoding, "", this.countingMode.getValue());
	}

	/**
	 * Creates InputSetting which is specified to count sjis test data.
	 * @param encoding source code encoding.
	 * @return InputSetting instance.
	 */
	protected  InputSetting createInputSettingToCountSjisWithEncoding(final String encoding) {
		return this.createInputSettingToCountSjisWithEncoding(SJIS_SRC_ROOT, encoding);
	}
}
