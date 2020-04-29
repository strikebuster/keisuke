package org.jenkinsci.plugins.keisuke.util;

import java.io.UnsupportedEncodingException;

/**
 * Utility for encoding name
 */
public final class EncodeUtil {

	private EncodeUtil() { }

	/**
	 * Checks that encoding name is supported.
	 * @param encoding name of encoding.
	 * @return if encoding name is supported, return true.
	 */
	public static boolean isSupportedEncoding(final String encoding) {
		if (encoding == null || encoding.isEmpty()) {
			return false;
		}
		try {
			String str = "dummy";
			str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		return true;
	}
}
