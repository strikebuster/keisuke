package keisuke.count.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * ファイル名に関するユーティリティメソッドを提供します。
 */
public final class FileNameUtil {

	private FileNameUtil() { }

	/**
	 * ファイルを無視するかどうかを判定します。
	 *
	 * @param file ファイル
	 * @return 無視する場合true、無視しない場合false
	 */
	public static boolean checkToBeIgnored(final File file) {
		String name = file.getName();
		if (name.equals(".svn")) {
			return true;
		}
		if (name.equals(".git")) {
			return true;
		}
		if (name.equals(".hg")) {
			return true;
		}
		if (name.equals(".bzr")) {
			return true;
		}
		if (name.equals("vssver.scc")) {
			return true;
		}
		if (name.equals("CVS")) {
			return true;
		}
		if (name.equals("SCCS")) {
			return true;
		}
		return false;
	}

	/**
	 * ファイル名の絶対パスを基点ディレクトリ名からの相対パスにして返す
	 * 相対パスに基点ディレクトリは含まれない
	 *
	 * @param fullPath ファイル名の絶対パス
	 * @param baseName 基点ディレクトリ名
	 * @return ファイル名の相対パス
	 */
	public static String getRelativePath(final String fullPath, final String baseName) {
		if (fullPath == null) {
			return "";
		}
		String keyword = getStringToSearchBase(baseName);
		if (keyword == null) {
			return fullPath;
		}
		int pos = fullPath.indexOf(keyword);
		if (pos < 0) {
			return fullPath;
		}
		return fullPath.substring(pos + keyword.length());
	}

	/**
	 * ファイル名の絶対パスから基点ディレクトリ名以下の部分パスを返す
	 * 部分パスには基点ディレクトリ名を含む
	 *
	 * @param fullPath ファイル名の絶対パス
	 * @param baseName 基点ディレクトリ名
	 * @return ファイル名の相対パス
	 */
	public static String getSubPathFromBottomOfBase(final String fullPath, final String baseName) {
		if (fullPath == null) {
			return "";
		}
		String keyword = getStringToSearchBase(baseName);
		if (keyword == null) {
			return fullPath;
		}
		int pos = fullPath.lastIndexOf(keyword);
		if (pos < 0) {
			return fullPath;
		}
		int pos2 = keyword.substring(0, keyword.length() - 1).lastIndexOf('/');
		return fullPath.substring(pos + pos2);
	}

	/**
	 * ファイル名の絶対パスから基点ディレクトリ名以下の部分パスを返す
	 * 部分パスには基点ディレクトリ名を含む
	 *
	 * @param fullPath ファイル名の絶対パス
	 * @param baseName 基点ディレクトリ名
	 * @return ファイル名の相対パス
	 */
	public static String getSubPathFromTopOfBase(final String fullPath, final String baseName) {
		if (fullPath == null) {
			return "";
		}
		String keyword = getStringToSearchBase(baseName);
		if (keyword == null) {
			return fullPath;
		}
		int pos = fullPath.lastIndexOf(keyword);
		if (pos < 0) {
			return fullPath;
		}
		return fullPath.substring(pos);
	}

	private static String getStringToSearchBase(final String baseName) {
		if (baseName == null || baseName.isEmpty()) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if (baseName.charAt(0) != '/') {
			sb.append('/');
		}
		sb.append(baseName).append('/');
		return sb.toString();
	}

	/* ファイル名比較用の変数と初期化処理 */
	private static boolean ignoreCase;
	static {
		if ((new File("a")).compareTo(new File("A")) == 0) {
			ignoreCase = true;
		} else {
			ignoreCase = false;
		}
	}

	/**
	 * OSの違いによる大文字小文字の扱いに対応したファイル名比較結果を返す
	 * @param name1 ファイル名文字列
	 * @param name2 ファイル名文字列
	 * @return name1が大きければ正の整数、小さければ負の整数、同じなら0
	 */
	public static int compareInOsOrder(final String name1, final String name2) {
		if (ignoreCase) {
			return name1.compareToIgnoreCase(name2);
		} else {
			return name1.compareTo(name2);
		}
	}

	/**
	 * コード順の文字列比較によるファイル名比較結果を返す
	 * @param name1 ファイル名文字列
	 * @param name2 ファイル名文字列
	 * @return name1が大きければ正の整数、小さければ負の整数、同じなら0
	 */
	public static int compareInCodeOrder(final String name1, final String name2) {
		return name1.compareTo(name2);
	}

	/**
	 * OSの違いによる大文字小文字の扱いに対応したファイル名比較結果を返す
	 * @param name1 ファイル名文字列
	 * @param name2 ファイル名文字列
	 * @return name1が大きければ正の整数、小さければ負の整数、同じなら0
	 */
	public static int compare(final String name1, final String name2) {
		return compareInOsOrder(name1, name2);
	}

	/**
	 * ファイル配列に対しOSの違いによる大文字小文字の扱いに対応したソートを
	 * 実行した結果の配列を返す
	 * @param filearray ファイル配列
	 * @return ソート後のファイル配列
	 */
	public static File[] sortInOsOrder(final File[] filearray) {
		return sort(filearray, true);
	}

	/**
	 * ファイル配列に対しファイル名のコード順のソートを実行した結果の配列を返す
	 * @param filearray ファイル配列
	 * @return ソート後のファイル配列
	 */
	public static File[] sortInCodeOrder(final File[] filearray) {
		return sort(filearray, false);
	}

	/**
	 * ファイル配列に対しソートを実行した結果の配列を返す
	 * OSの違いによる大文字小文字の扱いに対応したソート方法と通常のコード順の
	 * ソートを指定する
	 * @param filearray ファイル配列
	 * @param dependingOs OS依存のファイル名ソートならtrue
	 * @return ソート後のファイル配列
	 */
	public static File[] sort(final File[] filearray, final boolean dependingOs) {
		if (filearray == null) {
			return null;
		}
		if (dependingOs) {
			Arrays.sort(filearray, new Comparator<File>() {
				public int compare(final File o1, final File o2) {
					return FileNameUtil.compare(o1.getName(), o2.getName());
				}
			});
		} else {
			Arrays.sort(filearray);
		}
		return filearray;
	}
}
