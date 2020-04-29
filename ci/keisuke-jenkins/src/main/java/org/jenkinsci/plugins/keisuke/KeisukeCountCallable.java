package org.jenkinsci.plugins.keisuke;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.jenkinsci.plugins.keisuke.count.BaseDirIncludingSwitch;
import org.jenkinsci.plugins.keisuke.count.DiffCountAdapter;
import org.jenkinsci.plugins.keisuke.count.ResultConvertUtil;
import org.jenkinsci.plugins.keisuke.count.StepCountAdapter;
import org.jenkinsci.plugins.keisuke.count.StepCountResultForPublish;
import org.jenkinsci.plugins.keisuke.util.RelativePathUtil;
import org.jenkinsci.remoting.RoleChecker;

import hudson.FilePath.FileCallable;
import hudson.remoting.VirtualChannel;
import keisuke.StepCountResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.util.LogUtil;

/**
 * Class for calling keisuke.count.
 * invoked by jenkins build step
 *
 */
public class KeisukeCountCallable implements FileCallable<BuildResult> {
	private static final long serialVersionUID = 3L; // since ver.2.0.0

	private CountingUnit countingUnit;
	private File workspaceDir = null;
	private File baseDir = null;
	private BaseDirIncludingSwitch baseDirSwitch = null;
	private transient PrintStream logger;
	private transient StepCountAdapter stepCounter = null;
	private transient DiffCountAdapter diffCounter = null;

	/**
	 * constructor
	 * @param unit an instance of CountingUnit
	 * @param stream logging stream
	 */
	public KeisukeCountCallable(final CountingUnit unit, final PrintStream stream) {
		this.countingUnit = unit;
		this.logger = stream;
		this.logger.println("[keisuke] unitName <" + this.inputSetting().getUnitName() + ">");
		this.logger.println("[keisuke] sourceDir <" + this.inputSetting().getSourceDirectory() + ">");
		this.logger.println("[keisuke] encoding <" + this.inputSetting().getEncoding() + ">");
		this.logger.println("[keisuke] xmlPath <" + this.inputSetting().getXmlPath() + ">");
		this.logger.println("[keisuke] countingMode <" + this.inputSetting().getCountingMode() + ">");
		if (CountingModeEnum.ONLY_STEP_USING_FILE_SET.equals(this.inputSetting().getCountingModeEnum())) {
			this.logger.println("[keisuke] includes <" + this.inputSetting().getIncludePattern() + ">");
			this.logger.println("[keisuke] excludes <" + this.inputSetting().getExcludePattern() + ">");
		} else if (CountingModeEnum.BOTH_STEP_AND_DIFF.equals(this.inputSetting().getCountingModeEnum())) {
			this.logger.println("[keisuke] oldSourceDir <"
					+ this.inputSetting().getOldSourceDirectory() + ">");
		}
		boolean inclusion = false;
		if (unit.isOutputEnabled()) {
			inclusion = unit.getOutputSetting().isBaseDirInclusion();
		}
		this.baseDirSwitch = new BaseDirIncludingSwitch(inclusion);
	}

	private InputSetting inputSetting() {
		return this.countingUnit.getInputSetting();
	}

	private OutputSetting outputSetting() {
		return this.countingUnit.getOutputSetting();
	}

	private PrintStream logger() {
		if (this.logger == null) {
			return System.out;
		}
		return this.logger;
	}

	/**
	 * invoked by build action's perform method.
	 * @param workspace File instance for workspace
	 * @param channel VirtualChannel
	 * @return BuildResult
	 */
	@Override
	public BuildResult invoke(final File workspace, final VirtualChannel channel) {
		this.workspaceDir = workspace;
		this.baseDir = this.getBaseDir();
		BuildResult result = null;
		CountingModeEnum mode = this.inputSetting().getCountingModeEnum();
		//this.logger().println("[keisuke] counting mode is " + mode.getValue());
		try {
			if (mode.isFileSet()) { // file_set
				result = this.countStepsInFileSet();
			} else if (mode.isDiffToo()) { // diff_too
				result = this.countStepsAndDiffs();
			} else { // step_simply
				result = this.countStepsInDir();
			}

			//this.logger().println("[DEBUG] outputEnabled = " + this.countingUnit.isOutputEnabled());
			if (this.countingUnit.isOutputEnabled()) {
				this.stepCounter.writeResult(result);
				//this.logger().println("[DEBUG] outputSetting.isDiffEnabled = "
				//		+ this.outputSetting().isDiffEnabled());
				if (this.outputSetting().isDiffEnabled() && this.diffCounter != null) {
					this.diffCounter.writeResult(result);
				}
			}
		} catch (Exception e) {
			this.logger().println("[keisuke] catch exception during invoke: "
						+ LogUtil.getMessage(e));
			throw e;
		}
		return result;
	}

	/**
	 * Check about security
	 * @param checker RoleChecker
	 * @throws SecurityException signal to violation of security
	 */
	@Override
	public void checkRoles(final RoleChecker checker) throws SecurityException {
		// 自動生成されたメソッド・スタブ
		return;
	}

	/**
	 * Gets File instance of sourceDirectory.
	 * @return File instance of sourceDirectory.
	 */
	private File getBaseDir() {
		return RelativePathUtil.getDirectoryOf(this.workspaceDir, this.inputSetting().getSourceDirectory());
	}

	private BuildResult countStepsInDir() {
		BuildResult result = new BuildResult();
		try {
			List<StepCountResultForPublish> list = ResultConvertUtil.convertToListOfFileStepFrom(
				this.getStepCountingResult(), this.inputSetting().getUnitName(), this.baseDirSwitch);
			result.setFileSteps(list);
		} catch (IOException e) {
			result.addErrorMessage(LogUtil.getMessage(e));
		}
		return result;
	}

	private BuildResult countStepsInFileSet() {
		BuildResult result = new BuildResult();
    	try {
    		List<StepCountResultForPublish> list = ResultConvertUtil.convertToListOfFileStepFrom(
    				this.getStepCountingResultAboutFileSet(), this.inputSetting().getUnitName(),
    				this.baseDirSwitch);
		    result.setFileSteps(list);
    	} catch (IllegalArgumentException | IOException e) {
			result.addErrorMessage(LogUtil.getMessage(e));
		}
    	return result;
	}

	private BuildResult countStepsAndDiffs() {
		BuildResult result = this.countStepsInDir();
		try {
			DiffFolderResult diffResult = this.getDiffCountingResult();
			result.setDiffResult(ResultConvertUtil.convertToDiffResultForPublishFrom(diffResult,
					this.baseDir, this.inputSetting().getUnitName(), this.baseDirSwitch));
			result.setDiffSteps(ResultConvertUtil.convertToListOfDiffStepFrom(diffResult,
					this.baseDir, this.inputSetting().getUnitName(), this.baseDirSwitch));
		} catch (IOException e) {
			result.addErrorMessage(LogUtil.getMessage(e));
		}
		return result;
	}

	private StepCountResult[] getStepCountingResult() throws IOException {
    	this.stepCounter = new StepCountAdapter(this.countingUnit, this.workspaceDir, this.baseDir,
    					this.baseDirSwitch, this.logger());
    	StepCountResult[] resultArray = this.stepCounter.getCountingResultAboutDir();
    	return resultArray;
	}

	private List<StepCountResult> getStepCountingResultAboutFileSet() throws IllegalArgumentException, IOException {
		this.stepCounter = new StepCountAdapter(this.countingUnit, this.workspaceDir, this.baseDir,
    					this.baseDirSwitch, this.logger());
    	List<StepCountResult> resultList = this.stepCounter.getCountingResultAboutFileSet();
    	return resultList;
    }

	private DiffFolderResult getDiffCountingResult() throws IOException {
		this.diffCounter = new DiffCountAdapter(this.countingUnit, this.workspaceDir, this.baseDir,
						this.baseDirSwitch, this.logger());
    	DiffFolderResult result = this.diffCounter.getCountingResult();
    	return result;
	}

}
