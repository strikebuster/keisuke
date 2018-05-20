package keisuke.util;

import java.util.Arrays;
import java.util.List;

/**
 * 文字列に関する処理
 */
public final class StringUtil {

	/** the line separator for this OS */
    public static final String LINE_SEP = System.getProperty("line.separator");
    // tab aligns the end of itself on 8 characters width
	static final int TABWIDTH = 8;

	private StringUtil() { }

	/**
	 * 引数で渡した文字列のバイト長を返します。
	 * @param str 文字列
	 * @return バイト長
	 */
	public static int getByteLength(final String str) {
		return str.getBytes().length;
	}

	/**
	 * 文字列の表示幅（カラム数）を返す
	 * @param str 表示対象の文字列
	 * @return 表示に必要な幅の半角文字数
	 */
	public static int getDisplayWidth(final String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}

		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Utf8Util.isHalfWidthChar(c)) {
				len += 1;
			} else {
				len += 2;
			}
		}
		return len;
	}

	/**
	 * 文字列の先頭からを指定表示幅の文字列を切り出す。
	 * カタカナの判定は正しく行うことができない。
	 *
	 * @param str 文字列
	 * @param width 表示幅カラム数
	 * @return 切り出した先頭文字列
	 */
	public static String getLeftInWidth(final String str, final int width) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		int len = 0;
		int idx = 0;
		while (idx < str.length()) {
			char c = str.charAt(idx);
			if (Utf8Util.isHalfWidthChar(c)) {
				len += 1;
			} else {
				len += 2;
			}
			if (len == width) {
				return str.substring(0, idx + 1);
			} else if (len > width) {
				return str.substring(0, idx) + " ";
			}
			idx++;
		}
		return str.substring(0, idx);
	}

	/**
	 * 文字列が指定の長さ未満であれば右側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 * @param str 整形元の文字列
	 * @param width 整形する長さ
	 * @return 指定された長さに整形した文字列
	 */
	public static String shapeIntoFixWidth(final String str, final int width) {
		int length = StringUtil.getDisplayWidth(str);
		if (length == width) {
			return str;
		} else if (length < width) {
			return str + fillWithChars(' ', width - length);
		} else {
			String left = StringUtil.getLeftInWidth(str, width);
			int leftLen = StringUtil.getDisplayWidth(left);
			if (leftLen < width) {
				return left + fillWithChars(' ', width - leftLen);
			} else {
				return left;
			}
		}
	}

	/**
	 * 文字列が指定の長さ未満であれば左側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 * @param str 整形元の文字列
	 * @param width 整形する長さ
	 * @return 指定された長さに整形した文字列
	 */
	public static String shapeIntoFixWidthRightAlign(final String str, final int width) {
		int length = StringUtil.getDisplayWidth(str);
		if (length == width) {
			return str;
		} else if (length < width) {
			return fillWithChars(' ', width - length) + str;
		} else {
			String left = StringUtil.getLeftInWidth(str, width);
			int leftLen = StringUtil.getDisplayWidth(left);
			if (leftLen < width) {
				return left + fillWithChars(' ', width - leftLen);
			} else {
				return left;
			}
		}
	}

	/**
	 * 指定された文字を指定された文字数分繰り返す文字列を返す
	 * @param ch 文字
	 * @param width 半数を指定
	 * @return 文字列
	 */
	public static String fillWithChars(final char ch, final int width) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < width; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * 文字列の左端のインデントに含まれるタブのインデント分を
	 * スペースに置換したインデント部分の文字列を返す
	 * タブのインデントは半角8文字毎の位置に揃えられるものと定義する
	 * @param line テキスト１行の文字列
	 * @return 引数lineの左端のインデント部分のみをタブがあればスペースに置換した文字列
	 */
	public static String getIndentConvertedSpaces(final String line) {
		if (line == null) {
	        return null;
	    }
	    char[] val = line.toCharArray();
	    int idx = 0;
	    int ind = 0;
	    int len = val.length;

	    while (idx < len) {
	        if (val[idx] == ' ') {
	        	ind++;
	        	idx++;
	        } else if (val[idx] == '\t') {
	        	int tabpad = ind % TABWIDTH;
	        	ind += TABWIDTH - tabpad;
	        	idx++;
	        } else {
	        	idx = len;
	        }
	    }

	    StringBuffer sb = new StringBuffer();
	    while (ind > 0) {
	    	sb.append(" ");
	    	ind--;
	    }
	    return sb.toString();
	}

	/**
	 * 改行を含む文字列を改行毎に分割して配列に格納して返す。
	 * @param text 文字列
	 * @return 改行毎に分割された文字列の配列
	 */
	public static String[] splitArrayOfLinesFrom(final String text) {
		String[] array = text.split("\r\n?|\n");
		if (array.length == 1 && array[0].isEmpty()) {
			return new String[0];
		}
		return array;
	}

	/**
	 * 改行を含む文字列を改行毎に分割してListに格納して返す。
	 * @param text 文字列
	 * @return 改行毎に分割された文字列のList
	 */
	public static List<String> splitListOfLinesFrom(final String text) {
		return Arrays.asList(splitArrayOfLinesFrom(text));
	}

	/**
	 * 文字列中にOS依存の改行コードを"\n"に変換する
	 * OS依存改行コードとして"\r\n"および"\r"が対象
	 * @param text 文字列
	 * @return 改行コードを"\n"に変換した文字列
	 */
	public static String normalizeLineSeparator(final String text) {
		if (text == null) {
			return null;
		}
		return text.replaceAll("\r\n?", "\n");
	}
}
