package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.Serializable;

import org.jenkinsci.plugins.keisuke.util.RelativePathUtil;

import keisuke.count.PathStyleEnum;
import keisuke.count.diff.DiffFileResult;

/**
 * DiffCount result for a file which has different line steps numbers of a source code file.
 * this is serializable.
 */
public class DiffFileResultForPublish extends DiffFileResult implements Serializable {

	private static final long serialVersionUID = 2L; // since ver.2.0.0

	private PathStyleEnum style = PathStyleEnum.SUB;
	private String parentDirPath = "";

	public DiffFileResultForPublish(final DiffFileResult result, final File baseDir,
			final BaseDirIncludingSwitch including) {
		super(result.nodeName(), result.status(), result.getParent());
		super.setFilePath(result.filePath());
		super.setSourceCategory(result.sourceCategory());
		super.setSourceType(result.sourceType());
		super.setSteps(result.addedSteps(), result.deletedSteps());
		if (including != null && including.isIncluding()) {
			this.style = PathStyleEnum.BASE;
		}
		String[] divPath = RelativePathUtil.dividePath(this.filePath());
    	if (divPath != null) {
    		this.parentDirPath = divPath[0];
    	}
	}

	/**
     * Gets parent directory path of a file.
     * @return parent directory path.
     */
    public String getDirectoryPath() {
    	return this.parentDirPath;
    }

    /**
     * Gets file path string.
     * it starts sub directory of workspace.
     * cf. original DiffCountResult's filePath() starts base directory(=workspace).
     * @return file path
     */
    @Override
    public String filePath() {
    	return super.filePath(this.style);
    }
}
