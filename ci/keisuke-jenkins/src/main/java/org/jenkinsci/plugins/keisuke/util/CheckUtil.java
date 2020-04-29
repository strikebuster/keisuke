package org.jenkinsci.plugins.keisuke.util;

/**
 * Utility for validation checking for form values.
 */
public final class CheckUtil {

	private CheckUtil() { }

	/**
	 * Checks that value is acceptable as category name.
	 * @param value string as category	name.
	 * @return if value is acceptable, return true.
	 */
	public static boolean checkStringForCategory(final String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		for (char ch : value.toCharArray()) {
			if (!checkCharForCategory(ch)) {
				return false;
			}
		}
		return true;
	}

	private static boolean checkCharForCategory(final char ch) {
		if (ch >= '0' && ch <= '9') {
			return true;
		} else if (ch >= 'A' && ch <= 'Z') {
			return true;
		} else if (ch >= 'a' && ch <= 'z') {
			return true;
		} else if (ch == '-' || ch == '_') {
			return true;
		}
		return false;
	}
}
