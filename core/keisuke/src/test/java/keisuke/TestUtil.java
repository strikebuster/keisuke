package keisuke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

import org.junit.Assert;
import static org.hamcrest.CoreMatchers.*;

public class TestUtil {
	public static String encode	= System.getProperty("file.encoding");

	protected TestUtil() {}

	
	public static void assertEquals(String expected, String actual) {
		//System.out.println("#EXPECTED#[" + expected + "]##");
		//System.out.println("#ACTUAL  #[" + actual + "]##");
		//Assert.assertThat(expected, is(actual));
		Assert.assertEquals(expected, actual);
	}
	
	public static void assertEquals(byte[] expected, byte[] actual) {
		//System.out.println("#EXPECTED#[**Binary**]##");
		//System.out.println("#ACTUAL  #[**Binary**]##");
		//Assert.assertThat(expected, is(actual));
		Assert.assertEquals(expected, actual);
	}

	public static void assertEquals(URL expectedFileUrl, String actual) {
		String expected = transformToString(expectedFileUrl);
		TestUtil.assertEquals(expected, actual);
	}
	
	public static void assertEquals(URL expectedFileUrl, byte[] actual) {
		String expected = transformToString(expectedFileUrl);
		TestUtil.assertEquals(expected.getBytes(), actual);
	}
	
	public static void assertEquals(URL expectedFileUrl, Map<?, ?> actualMap) {
		String expected = transformToString(expectedFileUrl);
		String actual = transformToString(actualMap);
		TestUtil.assertEquals(expected, actual);
	}
	
	public static void assertEquals(URL expectedFileUrl, File actualFile) {
		String expected = transformToString(expectedFileUrl);
		String actual = transformToString(actualFile);
		TestUtil.assertEquals(expected, actual);
	}
	
	public static void assertEquals(URL expectedFileUrl, URL actualFileUrl) {
		String expected = transformToString(expectedFileUrl);
		String actual = transformToString(actualFileUrl);
		TestUtil.assertEquals(expected, actual);
	}
	
	public static String transformToString(Map<?, ?> map) {
		if (map == null) return "";
		if (map.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		for ( Object key : map.keySet() ) {
			Object obj = map.get( key );
			if (obj == null) {
				sb.append(key + "\tnull\n");
			} else if (obj instanceof String) {
				String str = (String)obj;
				sb.append(key + "\t" + str + "\n");
			} else if (obj instanceof ReportColumn) {
				ReportColumn repcol = (ReportColumn)obj;
				int idx = repcol.getIndex();
				String title = repcol.getTitle();
				sb.append(key + "\t" + idx + "," + title + "\n");
			}
		}
		return sb.toString();
	}
		
	public static String transformToString(URL url) {
		return transformToString(new File(url.getFile()));
	}
	
	public static String transformToString(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encode));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}

		StringBuilder sb = new StringBuilder();
		try {
			while (reader.ready()) {
				String line = reader.readLine();
				sb.append(line).append("\n");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (reader != null) try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
		}
		return sb.toString();
	}
	
}
