package keisuke.count;

import keisuke.MainProcedure;

/**
 * コマンドラインから引数で指定したファイルやディレクトリ配下のファイルの
 * ソースコード行数をカウントする
 */
public final class StepCount {

	private StepCount() { }

	/**
	 *コマンドライン起動用メソッド
	 * @param args コマンドライン引数配列
	 */
	public static void main(final String[] args) {
		//if (args == null || args.length == 0) {
		//	System.exit(0);
		//}
		MainProcedure proc = (MainProcedure) new StepCountProc();
		proc.main(args);
	}

}
