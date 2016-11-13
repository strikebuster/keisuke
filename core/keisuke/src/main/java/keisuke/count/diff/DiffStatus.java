package keisuke.count.diff;

/**
 * ファイル、ディレクトリの変更状況を示す列挙型です。
 *
 * keisuke: パッケージ変更と列挙子のtoString()オーバーライドをやめて
 * 出力文言はDiffStatusTextクラスで変換するように変更
 */

public enum DiffStatus {

	//変更なしを示すステータスです。
	NONE,

	 //追加を示すステータスです。
	ADDED,

	 //変更を示すステータスです。
	MODIFIED,

	 //削除を示すステータスです。
	REMOVED,

	 //サポート対象外を示すステータスです。
	UNSUPPORTED;
}
