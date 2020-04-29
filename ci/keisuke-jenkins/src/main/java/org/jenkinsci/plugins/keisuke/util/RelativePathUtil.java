package org.jenkinsci.plugins.keisuke.util;

import static keisuke.count.util.FileNameUtil.isAbsolutePath;

import java.io.File;

/**
 * Utility for relative path
 */
public final class RelativePathUtil {

	private RelativePathUtil() { }

	/**
	 * Divides a file path string into two strings as an array.
	 * first string element is a directory path and second is a file name.
	 * @param path file path string.
	 * @return strings array.
	 */
	public static String[] dividePath(final String path) {
		if (path == null || path.isEmpty()) {
			return null;
		}
		String dirPath;
		String fileName;
    	int pos = path.lastIndexOf('/');
    	if (pos < 0) {
    		dirPath = "";
    		fileName = path;
    	} else if (pos == 0) {
    		dirPath = "";
    		fileName = path.substring(1);
    	} else {
    		dirPath = path.substring(0, pos);
    		fileName = path.substring(pos + 1);
    	}
    	return new String[] {dirPath, fileName};
	}

	/**
	 * Gets File instance of a directory.
	 * @param current current directory.
	 * @param path directory path string.
	 * @return File instance of a directory.
	 */
	public static File getDirectoryOf(final File current, final String path) {
		if (current == null || !current.isDirectory()) {
			return null;
		}
		File targetDir = null;
		//System.out.println("[DEBUG] path:" + path);
		if (path.equals(".")) {
			targetDir = current;
		} else if (isAbsolutePath(path)) {
			targetDir = new File(path);
		} else {
			targetDir = new File(current, path);
		}
		//System.out.println("[DEBUG] directory:" + targetDir.getAbsolutePath());
		return targetDir;
	}
}
