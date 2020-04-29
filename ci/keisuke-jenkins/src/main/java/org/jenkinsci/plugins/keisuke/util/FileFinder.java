package org.jenkinsci.plugins.keisuke.util;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 * Scans the workspace and finds files specified by include pattern
 * and exclude pattern.
 */
public class FileFinder {

	private File baseDir;
	private String includePattern;
	private String excludePattern;

	/**
	 * Creates a new instance of {@link FileFinder}.
	 *
	 * @param base
	 *            the base directory for finding
	 * @param include
	 *            the ant file pattern to include for finding
	 * @param exclude
	 *            the ant file pattern to exclude for finding
	 */
	public FileFinder(final File base, final String include, final String exclude) {
		this.baseDir = base;
		this.includePattern = include;
		this.excludePattern = exclude;
	}

	/**
	 * Returns an array with the filenames of the specified file pattern that
	 * have been found in the base directory.
	 *
	 * @return the filenames of all found files.
	 * @throws IllegalArgumentException signal of failure in FileSet operations.
	 */
	public String[] getFoundedFileNames() throws IllegalArgumentException {
		try {
			FileSet fileSet = new FileSet();
			fileSet.setProject(new Project());
			fileSet.setDir(this.baseDir);
			fileSet.setIncludes(this.includePattern);
			fileSet.setExcludes(this.excludePattern);
			return fileSet.getDirectoryScanner(fileSet.getProject()).getIncludedFiles();
		} catch (BuildException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
