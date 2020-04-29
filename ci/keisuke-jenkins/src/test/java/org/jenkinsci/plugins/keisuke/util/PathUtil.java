package org.jenkinsci.plugins.keisuke.util;

import static keisuke.util.TestUtil.nameOfSystemOS;

/**
 * file path utility for test.
 */
public final class PathUtil {
	public static final String ABS_DIR_WINDOWS = "c:/windows/temp/keisuke-jenkins-test";
	public static final String ABS_DIR_UNIX = "/tmp/keisuke-jenkins-test";

	private PathUtil() { }

	public static String getExistingFilePath() {
		String path = null;
		if (nameOfSystemOS().startsWith("Windows")) {
			String winRoot = System.getenv("SystemRoot");
			if (winRoot == null) {
				winRoot = "C:/Windows";
			} else {
				winRoot = winRoot.replace("\\", "/");
			}
			path = winRoot + "/System32/drivers/etc/hosts";
		} else {
			path = "/etc/hosts";
		}
		return path;
	}

	public static String getAbsoluteDataPath() {
		String path = null;
		if (nameOfSystemOS().startsWith("Windows")) {
			path = ABS_DIR_WINDOWS;
		} else {
			path = ABS_DIR_UNIX;
		}
		return path;
	}

}
