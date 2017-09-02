package keisuke;

/**
 * Class of attributes for a column of reporting
 */
public class ReportColumn {
	private String title = ""; // レポートのカラムタイトル
	private int index = -1; // レポートのカラム位置の順番

	/**
	 * レポートカラム属性のコンストラクタ
	 * @param str カラムタイトル文字列
	 * @param idx カラム順番
	 */
	public ReportColumn(final String str, final int idx) {
		if (str != null) {
			this.title = str;
		}
		if (idx >= 0) {
			this.index = idx;
		}
	}

	/**
	 * カラムタイトル文字列を返す
	 * @return カラムタイトル文字列
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * カラム順番を返す
	 * @return カラム順番
	 */
	public int getIndex() {
		return this.index;
	}
}
