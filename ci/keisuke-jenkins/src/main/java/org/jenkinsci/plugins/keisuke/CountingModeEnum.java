package org.jenkinsci.plugins.keisuke;

/**
 * Enumerator for Counting mode
 */
public enum CountingModeEnum {

	/** Counting only step simply */
	ONLY_STEP_SIMPLY("step_simply"),
	/** Counting only step using file set */
	ONLY_STEP_USING_FILE_SET("file_set"),
	/** Counting both step and diff from old version. */
	BOTH_STEP_AND_DIFF("diff_too");

	// 文字列
	private String modeName = "";

	CountingModeEnum(final String name) {
		this.modeName = name;
	}

	/**
	 * Gets string for this counting mode.
	 * return string for this counting mode.
	 */
	@Override
	public String toString() {
		return this.modeName;
	}

	/**
	 * Gets name of this counting mode.
	 * @return name of this counting mode.
	 */
	public String getValue() {
		return this.modeName;
	}

	/**
	 * Is this ONLY_STEP_SIMPLY.
	 * @return if this is ONLY_STEP_SIMPLY, returns true
	 */
	public boolean isOnlyStepSimply() {
		if (this == ONLY_STEP_SIMPLY) {
			return true;
		}
		return false;
	}

	/**
	 * Is this step_simply.
	 * @return if this is step_simply, returns true
	 */
	public boolean isStepSimply() {
		return isOnlyStepSimply();
	}

	/**
	 * Is this ONLY_STEP_USING_FILE_SET.
	 * @return if this is ONLY_STEP_USING_FILE_SET, returns true
	 */
	public boolean isOnlyStepUsingFileSet() {
		if (this == ONLY_STEP_USING_FILE_SET) {
			return true;
		}
		return false;
	}

	/**
	 * Is this file_set.
	 * @return if this is file_set, returns true
	 */
	public boolean isFileSet() {
		return isOnlyStepUsingFileSet();
	}

	/**
	 * Is this BOTH_STEP_AND_DIFF.
	 * @return if this is BOTH_STEP_AND_DIFF, returns true
	 */
	public boolean isBothStepAndDiff() {
		if (this == BOTH_STEP_AND_DIFF) {
			return true;
		}
		return false;
	}

	/**
	 * Is this diff_too.
	 * @return if this is diff_too, returns true
	 */
	public boolean isDiffToo() {
		return isBothStepAndDiff();
	}

}
