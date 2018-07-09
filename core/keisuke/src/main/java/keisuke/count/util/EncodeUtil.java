package keisuke.count.util;

/**
 * 文字コードに関するユーティリティメソッドを提供します。
 */
public final class EncodeUtil {

	private static final char ASCII_MIN = 0x0000;
	private static final char ASCII_PRINTABLE_MIN = 0x0020;
	//private static final char ASCII_PRINTABLE_MAX = 0x007E;
	private static final char ASCII_MAX = 0x007F;
	private static final int[] JSON_ESCAPE_CHARS = new int[ASCII_MAX + 1];
	private static final int[] XML_ESCAPE_CHARS = new int[ASCII_MAX + 1];

	static {
		// 制御文字はコード値表記
		for (int i = ASCII_MIN; i < ASCII_PRINTABLE_MIN; i++) {
			JSON_ESCAPE_CHARS[i] = -1;
			XML_ESCAPE_CHARS[i] = -1;
		}
		// 以下の制御文字(DEL)はコード値表記
		JSON_ESCAPE_CHARS[ASCII_MAX] = -1;
		XML_ESCAPE_CHARS[ASCII_MAX] = -1;

		// 以下の制御文字は\エスケープ表記
		JSON_ESCAPE_CHARS['\b'] = 'b';
		JSON_ESCAPE_CHARS['\t'] = 't';
		JSON_ESCAPE_CHARS['\n'] = 'n';
		JSON_ESCAPE_CHARS['\f'] = 'f';
		JSON_ESCAPE_CHARS['\r'] = 'r';
		// 以下の文字は\エスケープ表記
		JSON_ESCAPE_CHARS['"'] = '"';
		JSON_ESCAPE_CHARS['/'] = '/';
		JSON_ESCAPE_CHARS['\\'] = '\\';
		//ESCAPE_CHARS['<'] = -2;
		//ESCAPE_CHARS['>'] = -2;

		// 以下の文字をXMLエスケープ
		XML_ESCAPE_CHARS['<'] = '<';
		XML_ESCAPE_CHARS['>'] = '>';
		XML_ESCAPE_CHARS['&'] = '&';
		XML_ESCAPE_CHARS['"'] = '"';
	}

	private EncodeUtil() { }

	/**
	 * 文字列をUnicodeエスケープした文字列を返す
	 * @param str 文字列
	 * @return Unicodeエスケープした文字列
	 */
	public static String unicodeEscape(final String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int c = ch;
			if (c <= ASCII_MAX) {
				int x = JSON_ESCAPE_CHARS[c];
				if (x > 0) {
					sb.append('\\').append((char) x);
				} else if (x == -1) {
					sb.append(unicodeEscape(ch));
				} else {
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * 1文字をUnicodeエスケープした文字列を返す
	 * @param ch 文字
	 * @return Unicodeエスケープした文字列
	 */
	private static String unicodeEscape(final char ch) {
		int c = ch;
		StringBuffer sb = new StringBuffer("\\u");
		sb.append(String.format("%4x", c).toUpperCase());
		return sb.toString();
	}

	/**
	 * 文字列をXmlエスケープした文字列を返す
	 * @param str 文字列
	 * @return Xmlエスケープした文字列
	 */
	public static String xmlEscape(final String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int c = ch;
			if (c <= ASCII_MAX) {
				switch (XML_ESCAPE_CHARS[c]) {
				case -1:
					sb.append("&#").append(Integer.toString(c)).append(";");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				default:
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * 文字列をCSVの値としてエスケープした文字列を返す
	 * @param str 文字列
	 * @return CSVの値としてエスケープした文字列
	 */
	public static String csvEscape(final String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}
		// 文字列にカンマかダブルクォートがなければそのまま返す
		int idxComma = str.indexOf('\"');
		int idxWquot = str.indexOf(',');
		if (idxComma < 0 && idxWquot < 0) {
			return str;
		}
		StringBuffer sb = new StringBuffer('\"');
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == '\"') {
				sb.append("\"\"");
			} else {
				sb.append(ch);
			}
		}
		sb.append('\"');
		return sb.toString();
	}
}
