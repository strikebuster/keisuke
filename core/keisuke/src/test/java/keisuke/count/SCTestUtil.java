package keisuke.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import static org.hamcrest.CoreMatchers.*;

import jp.sf.amateras.stepcounter.Util;
import keisuke.TestUtil;

public class SCTestUtil extends TestUtil {

	protected SCTestUtil() {}
	
	public static void assertEqualHtmls(String expected, String actual) {
		//System.out.println("#EXPECTED#[" + expected + "]##");
		//System.out.println("#ACTUAL  #[" + actual + "]##");
		String expstr = expected;
		Pattern p = Pattern.compile(":switchDir\\('[0-9_]+'\\)", Pattern.DOTALL);
		Matcher m = p.matcher(expstr);
		m.find();
		expstr = m.replaceAll(":switchDir()");
		Pattern p2 = Pattern.compile("class=\\\"[0-9_]+\\\"", Pattern.DOTALL);
		Matcher m2 = p2.matcher(expstr);
		m2.find();
		expstr = m2.replaceAll("class=\"\"");
		
		String actstr = actual;
		m = p.matcher(actstr);
		m.find();
		actstr = m.replaceAll(":switchDir()");
		m2 = p2.matcher(actstr);
		m2.find();
		actstr = m2.replaceAll("class=\"\"");
		
		//System.out.println("#EXPECTEDHTML#[" + expstr + "]##");
		//System.out.println("#ACTUALHTML  #[" + actstr + "]##");
		Assert.assertEquals(expstr, actstr);

	}
	
	public static void assertEqualHtmls(URL expectedFileUrl, File actualFile, int ignorelines) {
		String expected = transformToString(expectedFileUrl);
		String actual = transformToString(actualFile, ignorelines);
		SCTestUtil.assertEqualHtmls(expected, actual);
	}
	
	public static void assertEquals(URL expectedFileUrl, File actualFile, int ignorelines) {
		String expected = transformToString(expectedFileUrl);
		String actual = transformToString(actualFile, ignorelines);
		TestUtil.assertEquals(expected, actual);
	}
	
	public static String transformToString(File file, int ignorelines) {
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
			//if (sb.length() > 0) {
			//	sb.deleteCharAt(sb.lastIndexOf("\n"));
			//}
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (reader != null) try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
		}
		return sb.toString();
	}
}
