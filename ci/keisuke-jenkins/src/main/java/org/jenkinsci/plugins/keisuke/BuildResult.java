package org.jenkinsci.plugins.keisuke;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.count.DiffCountResultForPublish;
import org.jenkinsci.plugins.keisuke.count.DiffFolderResultForPublish;
import org.jenkinsci.plugins.keisuke.count.StepCountResultForPublish;

/**
 * Class aggregate the count result of each files in the target unit.
 *
 */
public class BuildResult implements Serializable {

	private static final long serialVersionUID = 4L; // since ver.2.0.0

	// list of file counting result about the target unit.
	private List<StepCountResultForPublish> fileStepList = new ArrayList<StepCountResultForPublish>();
	// list of file counting result about the target unit.
	private List<DiffCountResultForPublish> fileDiffList = new ArrayList<DiffCountResultForPublish>();
	// diff counting result about the target unit.
	private DiffFolderResultForPublish diffResult = null;
	// list of error messages
	private List<String> errorMessages = new ArrayList<String>();

	// sum of code lines about all files in the unit.
	private long codeSum = 0;
	// sum of comment lines about all files in the unit.
	private long commentSum = 0;
	// sum of blank lines about all files in the unit.
	private long blankSum = 0;
	// sum of all lines about all files in the unit.
	private long stepSum = 0;
	// counting number of all files in the unit.
	private long countedFiles = 0;
	// counting number of all uncounted files in the unit.
	private long uncountedFiles = 0;

	// sum of added diff lines about all files in the unit.
	private long addedSum = 0;
	// sum of deleted diff lines about all files in the unit.
	private long deletedSum = 0;
	// counting number of all changed files in the unit.
	private long changedFiles = 0;
	// counting number of all no changed files in the unit.
	private long unchangedFiles = 0;
	// counting number of all unsupported files in the unit.
	private long unsupportedFiles = 0;

	// saving inputSetting value at this build
	private InputSetting inputSetting = null;

	public BuildResult() { }

	/**
	 * Gets error messages during counting steps.
	 * @return error messages
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Adds an error message during counting steps.
	 * @param errorMessage error messages
	 */
	public void addErrorMessage(final String errorMessage) {
		this.errorMessages.add(errorMessage);
	}

	/**
	 * Gets FileStep list which show result of counting steps.
	 * @return  FileStep list
	 */
	public List<StepCountResultForPublish> getFileSteps() {
		return fileStepList;
	}

	/**
	 * Sets FileStep list which show result of counting steps all files in the unit.
	 * @param steps list of StepCount results.
	 */
	protected void setFileSteps(final List<StepCountResultForPublish> steps) {
		this.fileStepList = steps;
		this.codeSum = 0;
		this.commentSum = 0;
		this.blankSum = 0;
		this.stepSum = 0;
		this.countedFiles = 0;
		this.uncountedFiles = 0;
		for (StepCountResultForPublish fileStep : steps) {
			this.codeSum += fileStep.getCodes();
			this.commentSum += fileStep.getComments();
			this.blankSum += fileStep.getBlanks();
			this.stepSum += fileStep.getSum();
			if (fileStep.isUnsupported()) {
				this.uncountedFiles++;
			} else {
				this.countedFiles++;
			}
		}
	}

	/**
	 * Adds FileStep which has result of counting steps about a file
	 * to list of FileSteps.
	 * @param fileStep result of counting
	 */
	@SuppressWarnings("unused")
	private void addFileStep(final StepCountResultForPublish fileStep) {
		this.fileStepList.add(fileStep);
		this.codeSum += fileStep.getCodes();
		this.commentSum += fileStep.getComments();
		this.blankSum += fileStep.getBlanks();
		this.stepSum += fileStep.getSum();
		if (fileStep.isUnsupported()) {
			this.uncountedFiles++;
		} else {
			this.countedFiles++;
		}
	}

	/**
	 * Gets sum of runnable codes line steps.
	 * @return line steps
	 */
	public long getCodesSum() {
		return this.codeSum;
	}

	/**
	 * Gets sum of comment line steps.
	 * @return line steps
	 */
	public long getCommentsSum() {
		return this.commentSum;
	}

	/**
	 * Gets sum of blank line steps.
	 * @return line steps
	 */
	public long getBlanksSum() {
		return this.blankSum;
	}

	/**
	 * Gets sum of all line steps.
	 * @return line steps
	 */
	public long getStepsSum() {
		return this.stepSum;
	}

	/**
	 * Gets number of counted files.
	 * @return files number.
	 */
	public long getCountedFiles() {
		return this.countedFiles;
	}

	/**
	 * Gets number of uncounted files.
	 * @return files number.
	 */
	public long getUncountedFiles() {
		return this.uncountedFiles;
	}

	/**
	 * Gets DiffStep list which show result of counting diff steps.
	 * @return  DiffStep list
	 */
	public List<DiffCountResultForPublish> getDiffSteps() {
		return fileDiffList;
	}

	/**
	 * Sets DiffStep list which show result of counting diff steps all files in the unit.
	 * @param diffs list of DiffCount results.
	 */
	protected void setDiffSteps(final List<DiffCountResultForPublish> diffs) {
		this.fileDiffList = diffs;
		this.addedSum = 0;
		this.deletedSum = 0;
		this.changedFiles = 0;
		this.unchangedFiles = 0;
		for (DiffCountResultForPublish diffStep : diffs) {
			this.addedSum += diffStep.getAdded();
			this.deletedSum += diffStep.getDeleted();
			if (diffStep.isUnsupported()) {
				this.unsupportedFiles++;
			} else if (diffStep.isUnchanged()) {
				this.unchangedFiles++;
			} else {
				this.changedFiles++;
			}
		}
	}

	/**
	 * Gets sum of added diff line steps.
	 * @return line steps
	 */
	public long getAddedSum() {
		return this.addedSum;
	}

	/**
	 * Gets sum of deleted diff line steps.
	 * @return line steps
	 */
	public long getDeletedSum() {
		return this.deletedSum;
	}

	/**
	 * Gets number of changed files.
	 * @return files number.
	 */
	public long getChangedFiles() {
		return this.changedFiles;
	}

	/**
	 * Gets number of unchanged files.
	 * @return files number.
	 */
	public long getUnchangedFiles() {
		return this.unchangedFiles;
	}

	/**
	 * Gets number of unsupported files.
	 * @return files number.
	 */
	public long getUnsupportedFiles() {
		return this.unsupportedFiles;
	}

	/**
	 * Gets DiffFolderResult which is root folder of counting files.
	 * @return DiffFolderResult
	 */
	public DiffFolderResultForPublish getDiffResult() {
		return this.diffResult;
	}

	/**
	 * Sets DiffFolderResult which is root folder of counting files.
	 * @param result DiffFolderResult
	 */
	protected void setDiffResult(final DiffFolderResultForPublish result) {
		this.diffResult = result;
	}

	/**
	 * Asks if that diff result exists.
	 * @return true if diff result extsts.
	 */
	public boolean isDiffExist() {
		return (this.diffResult != null);
	}

	/**
	 * Gets InputSetting value which is used at this build.
	 * @return instance of InputSetting.
	 */
	public InputSetting getInputSetting() {
		return this.inputSetting;
	}

	/**
	 * Sets InputSetting value which is used at this build.
	 * @param setting instance of InputSetting.
	 */
	public void setInputSetting(final InputSetting setting) {
		this.inputSetting = setting;
	}

	/** Gets debug message
	 * @return debug message
	 */
	protected String debug() {
		StringBuffer sb = new StringBuffer();
		for (StepCountResultForPublish step : this.fileStepList) {
			sb.append(step.toString());
		}
		for (DiffCountResultForPublish diff : this.fileDiffList) {
			sb.append(diff.toString());
		}
		for (String msg : this.errorMessages) {
			sb.append(msg).append('\n');
		}
		return sb.toString();
	}
}
