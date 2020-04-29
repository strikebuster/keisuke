package org.jenkinsci.plugins.keisuke.count;

import java.io.Serializable;

/**
 * Switch for which base directory name is included or not.
 */
public class BaseDirIncludingSwitch implements Serializable {
	private static final long serialVersionUID = 1L; // since ver.2.0.0

	private boolean baseDirInclusion = false;

	public BaseDirIncludingSwitch(final boolean inclusion) {
		this.baseDirInclusion = inclusion;
	}

	/**
	 * Sets switch to true.
	 */
	public void setTrue() {
		this.baseDirInclusion = true;
	}

	/**
	 * Sets switch to false.
	 */
	public void setFalse() {
		this.baseDirInclusion = false;
	}

	/**
	 * Gets boolean if base directroy name is included.
	 * @return if base directroy name is included, then true.
	 */
	public boolean isIncluding() {
		return this.baseDirInclusion;
	}
}
