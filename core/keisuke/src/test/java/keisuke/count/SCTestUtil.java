package keisuke.count;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import keisuke.util.TestUtil;

/**
 * Util Class for Test of keisuke.count.* .
 * @author strikebuster
 *
 */
public class SCTestUtil extends TestUtil {

	public static String htmlToRemoveMutableIdFrom(final String htmlString) {
		String content = htmlString;
		Pattern p = Pattern.compile(":switchDir\\('[0-9_]+'\\)", Pattern.DOTALL);
		Matcher m = p.matcher(content);
		m.find();
		content = m.replaceAll(":switchDir()");
		Pattern p2 = Pattern.compile("class=\\\"[0-9_]+\\\"", Pattern.DOTALL);
		Matcher m2 = p2.matcher(content);
		m2.find();
		content = m2.replaceAll("class=\"\"");
		return content;
	}

	public static int withoutHeadLines(final int lineNum) {
		return lineNum;
	}

	public static byte[] binaryContentOf(final URL url) {
		return binaryContentOf(new File(url.getFile()));
	}

	public static byte[] binaryContentOf(final File file) {
		FileInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
		byte[] content;
		try {
			out = new ByteArrayOutputStream();
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			content = out.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) in.close();
				if (out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

}
