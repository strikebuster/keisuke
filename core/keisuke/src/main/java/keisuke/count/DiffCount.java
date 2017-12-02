package keisuke.count;

import keisuke.count.diff.DiffCountProc;

/**
 * コマンドラインから引数で指定した2つのディレクトリ配下のファイルの
 * 差分行数をカウントする
 */
public final class DiffCount {

	private DiffCount() { }

	/**
	 *コマンドライン起動用メソッド
	 * @param args コマンドライン引数配列
	 */
	public static void main(final String[] args) {
		//if (args == null || args.length == 0) {
		//	System.exit(0);
		//}
		new DiffCountProc().main(args);
	}

}
