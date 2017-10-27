package keisuke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import keisuke.report.ReportColumn;

/**
 * Util Class for Test of keisuke.* .
 */
public class TestUtil {
	protected static final String DEFAULT_ENCODING = System.getProperty("file.encoding");

	protected TestUtil() { }

	public static String contentOf(final Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<?, ?> entry : map.entrySet()) {
			Object keyObj = entry.getKey();
			Object valObj = entry.getValue();
			String key = "";
			if (keyObj instanceof String) {
				key = (String) keyObj;
			} else {
				key = keyObj.toString();
			}
			if (valObj == null) {
				sb.append(key + "\tnull\n");
			} else if (valObj instanceof String) {
				String str = (String) valObj;
				sb.append(key + "\t" + str + "\n");
			} else if (valObj instanceof ReportColumn) {
				ReportColumn repcol = (ReportColumn) valObj;
				int idx = repcol.getIndex();
				String title = repcol.getTitle();
				sb.append(key + "\t" + idx + "," + title + "\n");
			}
		}
		return sb.toString();
	}

	public static String contentOf(final URL url) {
		return contentOf(new File(url.getFile()));
	}

	public static String contentOf(final File file) {
		return contentOf(file, 0);
	}

	public static String contentOf(final File file, final int ignorelines) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), DEFAULT_ENCODING));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int ignoredlines = 0;
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				if (ignoredlines < ignorelines) {
					ignoredlines++;
					System.out.println("[TEST] ignore : " + line);
					continue;
				}
				sb.append(line).append("\n");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
