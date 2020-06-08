package keisuke.swing.diffcount;

/**
 * Swing画面DiffCountテスト用ユーティリティ
 */
final class DiffCountTestUtil {

	//private static final int IDX_NAME = 0;
	private static final int IDX_STATUS = 1;
	private static final int IDX_ADDED = 2;
	private static final int IDX_DELETED = 3;

	private DiffCountTestUtil() { }

	/**
	 * 元のテキストから指定された行数を先頭から取り除いたテキストの部分文字列を返す
	 * @param text テキスト
	 * @param ignoreLines 無視する先頭行数
	 * @return 指定された行数を先頭から除去したテキスト文字列
	 */
	static String actualTextOf(final String text, final int ignoreLines) {
		if (text == null) {
			return null;
		}
		if (ignoreLines <= 0) {
			return text;
		}
		String value = text;
		for (int i = 0; i < ignoreLines; i++) {
			int p = value.indexOf('\n');
			if (p < 0 || value.length() - p < 2) {
				return "";
			}
			value = value.substring(p + 1);
		}
		return value;
	}

	/**
	 * 表形式のDiffCount結果データを２次元配列に格納した引数を解析して
	 * 結果全体の変更ステータスを導出して返す
	 * @param table ２次元配列データ
	 * @return 結果全体の変更ステータス文字列
	 */
	static String deriveTotalStatusFrom(final String[][] table) {
		if (table == null || table.length == 0) {
			return "";
		}
		String unchanged = "UNCHANGED";
		for (int i = 0; i < table.length; i++) {
			String[] row = table[i];
			if (row.length > IDX_STATUS) {
				if (row[IDX_STATUS].equals("MODIFIED") || row[IDX_STATUS].equals("ADDED")
						|| row[IDX_STATUS].equals("DROPED")) {
					return "MODIFIED";
				} else if (row[IDX_STATUS].equals("変更") || row[IDX_STATUS].equals("新規")
						|| row[IDX_STATUS].equals("削除")) {
					return "変更";
				}
				if (unchanged.equals("UNCHANGED") && row[IDX_STATUS].equals("変更なし")) {
					unchanged = "変更なし";
				}
			}
		}
		return unchanged;
	}

	/**
	 * 表形式のDiffCount結果データを２次元配列に格納した引数を解析して
	 * 結果全体の追加ステップ数を導出して返す
	 * @param table ２次元配列データ
	 * @return 結果全体の追加ステップ数
	 */
	static long deriveTotalAddedStepsFrom(final String[][] table) {
		if (table == null || table.length == 0) {
			return 0;
		}
		long sum = 0;
		for (int i = 0; i < table.length; i++) {
			String[] row = table[i];
			if (row.length > IDX_ADDED) {
				sum += Long.parseLong(row[IDX_ADDED]);
			}
		}
		return sum;
	}

	/**
	 * 表形式のDiffCount結果データを２次元配列に格納した引数を解析して
	 * 結果全体の削除ステップ数を導出して返す
	 * @param table ２次元配列データ
	 * @return 結果全体の削除ステップ数
	 */
	static long deriveTotalDeletedStepsFrom(final String[][] table) {
		if (table == null || table.length == 0) {
			return 0;
		}
		long sum = 0;
		for (int i = 0; i < table.length; i++) {
			String[] row = table[i];
			if (row.length > IDX_DELETED) {
				sum += Long.parseLong(row[IDX_DELETED]);
			}
		}
		return sum;
	}
}
