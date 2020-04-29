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
 *
 */
public class CountTestUtil extends TestUtil {

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

	public static byte[] excelContentOf(final URL url) {
		return excelContentOf(new File(url.getFile()));
	}

	private static final byte[] END_OF_EXCEL_CONTENT = {(byte) 0xFF,
			0x52, 0x00, 0x6F, 0x00, 0x6F, 0x00, 0x74, 0x00,
			0x20, 0x00, 0x45, 0x00, 0x6E, 0x00, 0x74, 0x00,
			0x72, 0x00, 0x79, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x16, 0x00, 0x05, 0x01, (byte) 0xFF
			};
	private static final int SIZE_OF_END_OF_EXCEL_CONTENT = 70;

	public static byte[] excelContentOf(final File file) {
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
			int idx = 0;
			out = new ByteArrayOutputStream();
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
				if (c == END_OF_EXCEL_CONTENT[idx]) {
					idx++;
					if (idx > SIZE_OF_END_OF_EXCEL_CONTENT) {
						// content ends. the rest data is ignored.
						break;
					}
				} else {
					idx = 0;
				}
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

	/**
	 * CSV形式の文字列を表形式の２次元配列に変換する
	 * @param csv CSV形式の文字列
	 * @return ２次元配列データ
	 */
	public static String[][] convertToTableArrayFrom(final String csv) {
		if (csv == null || csv.length() == 0) {
			return null;
		}
		String[][] table = null;
		int valueSize = 0;
		String[] lines = csv.split("\r\n?|\n");
		for (int i = 0; i < lines.length; i++) {
			String[] values = lines[i].split(",");
			if (i == 0) {
				valueSize = values.length;
				table = new String[lines.length][valueSize];
			}
			for (int j = 0; j < values.length; j++) {
				if (j < valueSize) {
					table[i][j] = values[j];
				}
			}
		}
		return table;
	}
}
