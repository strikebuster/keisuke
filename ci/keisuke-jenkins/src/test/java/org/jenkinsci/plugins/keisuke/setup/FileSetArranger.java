package org.jenkinsci.plugins.keisuke.setup;

import static org.jenkinsci.plugins.keisuke.setup.TestDataConstant.JAVA_SRC_ROOT;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.InputSetting;

/**
 * Arranging inputSetting to be set up when countingMode is file_set.
 */
class FileSetArranger extends InputSettingArranger {

	FileSetArranger() {
		super(CountingModeEnum.ONLY_STEP_USING_FILE_SET);
	}

	/**
	 * Creates InputSetting instance to count java test data,
	 * then its top directory is workspace one.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountJavaAtWorkspace() {
		return super.createInputSettingToCountJavaAtWorkspace()
				.setItemsOfFileSet(JAVA_SRC_ROOT + "/**/*.java", "");
	}

	/**
	 * Creates InputSetting instance to count java test data,
	 * then its top directory is out of workspace.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountJavaAtAbsoluteDir(final String srcdir) {
		return super.createInputSettingToCountJavaAtAbsoluteDir(srcdir)
				.setItemsOfFileSet(JAVA_SRC_ROOT + "/**/*.java", "");
	}

	/**
	 * Creates InputSetting instance to count java test data,
	 * then it uses a customized language rule.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountJavaUsingCustomRule(final String srcdir, final String xml) {
		return super.createInputSettingToCountJavaUsingCustomRule(srcdir, xml)
				.setItemsOfFileSet("**/*.java", "");
	}

	/**
	 * Creates InputSetting instance to count sjis test data,
	 * then it uses a specified encoding.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountSjisWithEncoding(final String srcdir, final String encoding) {
		return super.createInputSettingToCountSjisWithEncoding(srcdir, encoding)
				.setItemsOfFileSet("", "");
	}
}
