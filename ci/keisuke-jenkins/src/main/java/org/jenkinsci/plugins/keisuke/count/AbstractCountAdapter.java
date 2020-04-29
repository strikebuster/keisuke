package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.PrintStream;

import org.jenkinsci.plugins.keisuke.BuildResult;
import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.InputSetting;
import org.jenkinsci.plugins.keisuke.OutputSetting;

import keisuke.count.AbstractCountMainProc;
import keisuke.count.PathStyleEnum;
import keisuke.count.SortOrderEnum;

/**
 * Base class for adapter to call keisuke.count.step.StepCountProc or
 * StepCountProceduralFunc or keisuke.count.diff.DiffCountProc.
 * sort option is specified as fixed value "on".
 */
public abstract class AbstractCountAdapter {

	private AbstractCountMainProc countProc = null;
	private CountingModeEnum countMode = CountingModeEnum.ONLY_STEP_SIMPLY;
	private InputSetting inputSettingProp;
	private OutputSetting outputSettingProp;
	private File workspaceDir = null;
	private File baseDir = null;
	private PathStyleEnum pathStyle = PathStyleEnum.SUB;
	private SortOrderEnum sortOrder = SortOrderEnum.ON;
	private transient PrintStream logger;

	/**
	 * constructor.
	 * @param unit CountingUnit instance.
	 * @param workspace jenkins job workspace.
	 * @param base directory of source code files.
	 * @param including path style is base, or sub.
	 * @param stream log stream.
	 */
	protected AbstractCountAdapter(final CountingUnit unit, final File workspace,
			final File base, final BaseDirIncludingSwitch including, final PrintStream stream) {
		this.inputSettingProp = unit.getInputSetting();
		this.outputSettingProp = unit.getOutputSetting();
		this.workspaceDir = workspace;
		this.baseDir = base;
		if (including != null && including.isIncluding()) {
			this.pathStyle = PathStyleEnum.BASE;
		}
		this.logger = stream;
	}

	/**
	 * Gets InputSetting instance.
	 * @return InputSetting instance.
	 */
	protected InputSetting inputSetting() {
		return this.inputSettingProp;
	}

	/**
	 * Gets OutputSetting instance.
	 * @return OutputSetting instance.
	 */
	protected OutputSetting outputSetting() {
		return this.outputSettingProp;
	}

	/**
	 * Gets workspace directory.
	 * @return File instance.
	 */
	protected File workspace() {
		return this.workspaceDir;
	}

	/**
	 * Gets base directory of source code files.
	 * @return File instance.
	 */
	protected File baseDirectory() {
		return this.baseDir;
	}

	/**
	 * Gets path style.
	 * @return PathStyleEnum instance.
	 */
	protected PathStyleEnum pathStyle() {
		return this.pathStyle;
	}

	/**
	 * Gets logger.
	 * @return PrintStream instance.
	 */
	protected PrintStream logger() {
		return this.logger;
	}

	/**
	 * Sets common properties of InputSetting about CountProc.
	 */
	protected void initCommonProps() {
		this.setSortOrder();
		this.setEncoding(this.inputSetting().getEncoding());
		String xmlname = this.inputSetting().getXmlPath();
		if (xmlname != null && !xmlname.isEmpty()) {
			this.setXml(xmlname);
		}
		this.countMode = this.inputSettingProp.getCountingModeEnum();
	}

	/**
	 * Gets CountingModeEnum instance.
	 * @return CountingModeEnum instance.
	 */
	protected CountingModeEnum coutingMode() {
		return this.countMode;
	}

	/**
	 * Gets an instance of subclass is a AbstractCountMainProc.
	 * @return AbstractCountMainProc instance.
	 */
	protected AbstractCountMainProc countProc() {
		return this.countProc;
	}

	/**
	 * Sets an instance of subclass is a AbstractCountMainProc.
	 * @param proc AbstractCountMainProc instance.
	 */
	protected void setCountProc(final AbstractCountMainProc proc) {
		this.countProc = proc;
	}

	/**
	 * Sets a XML file name of language defined.
	 * @param filename file name
	 */
	public void setXml(final String filename) {
		this.countProc.setXmlFileName(filename);
	}

	/**
	 * Sets source files encoding.
	 * @param encoding encoding.
	 */
	public void setEncoding(final String encoding) {
		this.countProc.setSourceEncoding(encoding);
	}

	private void setSortOrder() {
		this.countProc.setSortOrder(this.sortOrder.value());
	}

	/**
	 * Writes counted results into output file.
	 * @param result counted results.
	 */
	abstract void writeResult(BuildResult result);
}
