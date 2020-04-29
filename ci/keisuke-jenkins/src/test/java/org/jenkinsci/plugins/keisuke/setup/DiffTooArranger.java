package org.jenkinsci.plugins.keisuke.setup;

import static keisuke.count.util.FileNameUtil.isAbsolutePath;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.InputSetting;
import org.jenkinsci.plugins.keisuke.util.PathUtil;

/**
 * Arranging inputSetting to be set up when countingMode is diff_too.
 */
class DiffTooArranger extends InputSettingArranger {

	private String absdir = PathUtil.getAbsoluteDataPath();

	DiffTooArranger() {
		super(CountingModeEnum.BOTH_STEP_AND_DIFF);
	}

	private String getOldDirecoryComparedWith(final String srcdir) {
		if (srcdir == null || srcdir.isEmpty()) {
			return srcdir;
		}
		if (srcdir.equals(".")) {
			return this.absdir;
		}
		String olddir = null;
		if (isAbsolutePath(srcdir)) {
			// old & new are both in the absolute dir
			olddir = srcdir.replaceFirst("/root1", "/root2");
		} else {
			// old is in the absolute dir & new is in workspace
			//olddir = this.absdir + "/" + srcdir.replaceFirst("/root1", "/root2");
			olddir = srcdir.replaceFirst("/root1", "/root2");
		}
		return olddir;
	}

	/**
	 * Creates InputSetting instance to count java test data,
	 * then its top directory is workspace one.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountJava() {
		InputSetting setting = super.createInputSettingToCountJava();
		String srcdir = setting.getSourceDirectory();
		return setting.setItemsOfDiffToo(this.getOldDirecoryComparedWith(srcdir));
	}

	/**
	 * Creates InputSetting instance to count java test data,
	 * then its top directory is workspace one.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountJavaAtWorkspace() {
		return super.createInputSettingToCountJavaAtWorkspace()
				.setItemsOfDiffToo(this.getOldDirecoryComparedWith("."));
	}

	/**
	 * Creates InputSetting instance to count java test data,
	 * then it uses a customized language rule.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountJavaUsingCustomRule(final String srcdir, final String xml) {
		return super.createInputSettingToCountJavaUsingCustomRule(srcdir, xml)
				.setItemsOfDiffToo(this.getOldDirecoryComparedWith(srcdir));
	}

	/**
	 * Creates InputSetting instance to count sjis test data,
	 * then it uses a specified encoding.
	 * @return  InputSetting instance
	 */
	@Override
	protected InputSetting createInputSettingToCountSjisWithEncoding(final String srcdir, final String encoding) {
		return super.createInputSettingToCountSjisWithEncoding(srcdir, encoding)
				.setItemsOfDiffToo(this.getOldDirecoryComparedWith(srcdir));
	}
}
