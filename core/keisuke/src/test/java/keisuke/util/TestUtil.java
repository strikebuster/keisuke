package keisuke.util;

import static keisuke.util.StringUtil.ORIGINAL_ENCODING;
import static keisuke.util.StringUtil.SYSTEM_ENCODING;

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

	protected TestUtil() { }

	/**
	 * Mapの内容を文字列に出力する.
	 * Mapのkey毎に改行するが、改行コードは'\n'固定.
	 * keyとvalueの間は'\t'で区切る.
	 * @param map 表示したいMapインスタンス
	 * @return Mapの内容を表示する文字列
	 */
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

	/**
	 * keisukeのリソースファイルの内容を文字列に格納して返す.
	 * ファイルのエンコードは"UTF-8"固定.
	 * @param url リソースファイルのURL
	 * @return ファイルの内容の文字列
	 */
	public static String contentOf(final URL url) {
		return contentOf(new File(url.getFile()), ORIGINAL_ENCODING, 0);
	}

	/**
	 * テスト実行した環境で作成される出力ファイルの内容を文字列に格納して返す.
	 * ファイルのエンコードは環境依存.
	 * @param file 実行により出力されたファイル
	 * @return ファイルの内容の文字列
	 */
	public static String contentOf(final File file) {
		return contentOf(file, SYSTEM_ENCODING, 0);
	}

	/**
	 * テスト実行した環境で作成される出力ファイルの内容から先頭のN行を取り除いて文字列に格納して返す.
	 * 取り除く行数を指定する.
	 * ファイルのエンコードは環境依存.
	 * @param file 実行により出力されたファイル
	 * @param ignorelines 取り除く先頭の行数
	 * @return ファイルの内容の文字列
	 */
	public static String contentOf(final File file, final int ignorelines) {
		return contentOf(file, SYSTEM_ENCODING, ignorelines);
	}

	/**
	 * テスト実行した環境で作成される出力ファイルの内容を文字列に格納して返す.
	 * ファイルのエンコードは出力ファイル形式に依存するので指定する.
	 * @param file 実行により出力されたファイル
	 * @param encoding 出力ファイル用のエンコード名
	 * @return ファイルの内容の文字列
	 */
	public static String contentOf(final File file, final String encoding) {
		return contentOf(file, encoding, 0);
	}

	/**
	 * テスト実行した環境で作成される出力ファイルの内容から先頭のN行を取り除いて文字列に格納して返す.
	 * 取り除く行数を指定する.
	 * ファイルのエンコードは出力ファイル形式に依存するので指定する.
	 * @param file 実行により出力されたファイル
	 * @param encoding 出力ファイル用のエンコード名
	 * @param ignorelines 取り除く先頭の行数
	 * @return ファイルの内容の文字列
	 */
	public static String contentOf(final File file, final String encoding, final int ignorelines) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encoding));
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
				sb.append(line).append('\n');
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

	public static String nameOfSystemOS() {
		return System.getProperty("os.name");
	}

	public static boolean removeFile(final String filename) {
		if (filename == null || filename.isEmpty()) {
			return false;
		}
		File file = new File(filename);
		if (!file.exists() || !file.isFile()) {
			return false;
		}
		return file.delete();
	}
}
