package keisuke.count.diff;

/**
 * ファイル、ディレクトリの差分変更ステータス
 * ステータスの表示文言はDiffStatusTextクラスで変換する
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
