package keisuke.count.util;

import java.io.File;
import java.io.IOException;
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

	/* ファイルパス処理用の変数と初期化処理 */
	private static String[] fsRootPaths;
	private static File[] fsRootDirs = File.listRoots();
	static {
		fsRootPaths = new String[fsRootDirs.length];
		for (int i = 0; i < fsRootDirs.length; i++) {
			try {
				fsRootPaths[i] = fsRootDirs[i].getCanonicalPath().replace('\\', '/');
			} catch (IOException ex) {
				fsRootPaths[i] = "";
			}
		}
	}

	/**
	 * ファイル名の絶対パスを基点ディレクトリ名からの相対パスにして返す
	 * 相対パスに基点ディレクトリは含まれない
	 *
	 * @param fullPath ファイル名の絶対パス
	 * @param basePath 基点ディレクトリの絶対パス
	 * @return ファイル名の相対パス
	 */
	public static String getRelativePath(final String fullPath, final String basePath) {
		String noGoodValue = checkPathArgs(fullPath, basePath);
		if (noGoodValue != null) {
			return noGoodValue;
		}
		if (isRootDirectory(basePath)) {
			// 基点がルートディレクトリなので処理が特別
			return fullPath.substring(basePath.length());
		} else if (fullPath.charAt(basePath.length()) == '/') {
			return fullPath.substring(basePath.length() + 1);
		} else {
			// 正常ではないのでファイルの絶対パスを返す
			return fullPath;
		}
	}

	/**
	 * ファイル名の絶対パスから基点ディレクトリ名以下の部分パスを返す
	 * 部分パスには基点ディレクトリ名を含む
	 *
	 * @param fullPath ファイル名の絶対パス
	 * @param basePath 基点ディレクトリの絶対パス
	 * @return ファイル名の相対パス
	 */
	public static String getSubPathFromBottomOfBase(final String fullPath, final String basePath) {
		String noGoodValue = checkPathArgs(fullPath, basePath);
		if (noGoodValue != null) {
			return noGoodValue;
		}
		if (isRootDirectory(basePath)) {
			// 基点がルートディレクトリなので処理が特別
			return fullPath.substring(basePath.length() - 1);
		} else if (fullPath.charAt(basePath.length()) == '/') {
			StringBuffer sb = new StringBuffer();
			int pos2 = basePath.lastIndexOf('/');
			if (pos2 >= 0) {
				sb.append(basePath.substring(pos2));
			}
			return sb.append(fullPath.substring(basePath.length())).toString();
		} else {
			// 正常ではないのでファイルの絶対パスを返す
			return fullPath;
		}
	}

	/**
	 * ファイル名の絶対パスから基点ディレクトリ名以下の部分パスを返す
	 * 部分パスには基点ディレクトリ名全てを含む
	 *
	 * @param fullPath ファイル名の絶対パス
	 * @param basePath 基点ディレクトリの絶対パス
	 * @param currentPath カレントディレクトリの絶対パス
	 * @return ファイル名の相対パス
	 */
	public static String getSubPathFromTopOfBase(final String fullPath, final String basePath,
			final String currentPath) {
		String noGoodValue = checkPathArgs(fullPath, basePath);
		if (noGoodValue != null) {
			return noGoodValue;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(getRelativeForefatherPath(basePath, currentPath));
		if (isRootDirectory(basePath)) {
			// 基点がルートディレクトリなので処理が特別
			sb.append(fullPath.substring(basePath.length()));
		} else if (fullPath.charAt(basePath.length()) == '/') {
			sb.append(fullPath.substring(basePath.length() + 1));
		} else {
			// 正常ではないのでファイルの絶対パスを返す
			return fullPath;
		}
		return sb.toString();
	}

	private static String checkPathArgs(final String fullPath, final String basePath) {
		if (fullPath == null || fullPath.isEmpty()) {
			return "";
		}
		if (basePath == null || basePath.isEmpty()) {
			return fullPath;
		}
		int pos = fullPath.indexOf(basePath);
		// 正常な値の組み合わせでない場合はファイルの絶対パスを返す
		if (pos != 0) {
			return fullPath;
		} else if (fullPath.length() == basePath.length()) {
			return fullPath;
		}
		// 正常な場合にはnullを返す
		return null;
	}

	private static boolean isRootDirectory(final String path) {
		if (path == null || path.isEmpty()) {
			return false;
		}
		for (int i = 0; i < fsRootPaths.length; i++) {
			if (path.equals(fsRootPaths[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * カレントディレクトリから対象の基点ディレクトリの相対パスを返す
	 * @param targetPath 対象の基点ディレクトリの絶対パス文字列
	 * @param currentPath カレントディレクトリの絶対パス文字列
	 * @return カレントディレクトリからの基点ディレクトリの相対パス文字列
	 */
	private static String getRelativeForefatherPath(final String targetPath, final String currentPath) {
		if (targetPath == null || targetPath.isEmpty()) {
			return "";
		}
		if (currentPath == null || currentPath.isEmpty()) {
			return targetPath + "/";
		}
		if (isRootDirectory(currentPath)) {
			if (targetPath.startsWith(currentPath)) {
				return targetPath.substring(currentPath.length()) + "/";
			} else {
				// Windowsでドライブが異なる場合はこちら
				return targetPath + "/";
			}
		}
		if (isRootDirectory(targetPath)) {
			if (!currentPath.startsWith(targetPath)) {
				// Windowsでドライブが異なる場合
				return targetPath + "/";
			}
		}
		String[] targetDirs = targetPath.split("/");
		if (targetDirs.length == 0) {
			// targetPath == "/"
			targetDirs = new String[1];
			targetDirs[0] = "";
		}
		/*
		System.out.print("target: ");
		for (int i = 0; i < targetDirs.length; i++) {
			System.out.print("[" + targetDirs[i] + "]");
		}
		System.out.println(" len=" + targetDirs.length);
		*/

		String[] currentDirs = currentPath.split("/");
		/*
		System.out.print("current: ");
		for (int i = 0; i < currentDirs.length; i++) {
			System.out.print("[" + currentDirs[i] + "]");
		}
		System.out.println(" len=" + currentDirs.length);
		*/

		int idx = -1;
		int limit = currentDirs.length;
		if (limit > targetDirs.length) {
			limit = targetDirs.length;
		}
		for (int i = 0; i < limit; i++) {
			if (!currentDirs[i].equals(targetDirs[i])) {
				idx = i;
				break;
			}
		}
		if (idx == -1) {
			if (currentDirs.length < targetDirs.length) {
				// targetPathはcurrentPathの下位
				idx = currentDirs.length;
			} else {
				// targetPathはcurrentPathの上位か同一
				idx = targetDirs.length;
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = idx; i < currentDirs.length; i++) {
			sb.append("../");
		}
		for (int i = idx; i < targetDirs.length; i++) {
			sb.append(targetDirs[i]).append('/');
		}
		//System.out.println("relative: " + sb.toString());
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
