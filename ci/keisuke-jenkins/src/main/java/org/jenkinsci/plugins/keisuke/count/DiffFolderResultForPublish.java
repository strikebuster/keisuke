package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.Serializable;

import org.jenkinsci.plugins.keisuke.util.RelativePathUtil;

import keisuke.count.PathStyleEnum;
import keisuke.count.diff.DiffFolderResult;

/**
 * DiffCount result for a directory which has different line steps numbers of source code files under itself.
 * this is serializable.
 */
public class DiffFolderResultForPublish extends DiffFolderResult implements Serializable {

	private static final long serialVersionUID = 2L; // since ver.2.0.0

	private PathStyleEnum style = PathStyleEnum.SUB;
	private String parentDirPath = "";

	public DiffFolderResultForPublish(final DiffFolderResult result, final File baseDir,
			final BaseDirIncludingSwitch including) {
		super(result.nodeName(), result.status(), result.getParent());
		super.setFilePath(result.filePath());
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
