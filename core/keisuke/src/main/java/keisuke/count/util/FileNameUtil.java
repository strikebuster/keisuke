package keisuke.count.util;

import java.io.File;

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
		if (name.equals("CVS")) {
			return true;
		}
		if (name.equals(".svn")) {
			return true;
		}
		if (name.equals(".hg")) {
			return true;
		}
		if (name.equals(".git")) {
			return true;
		}
		return false;
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
	public static int compare(final String name1, final String name2) {
		if (ignoreCase) {
			return name1.compareToIgnoreCase(name2);
		} else {
			return name1.compareTo(name2);
		}
	}
}
