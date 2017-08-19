package keisuke;

/**
 * Class of attributes for a column of reporting
 * @author strikebuster
 *
 */
class ReportColumn {
	private String title = "";
	private int index = -1;

	/**
	 * レポートカラム属性のコンストラクタ
	 * @param str カラムタイトル文字列
	 * @param idx カラム順番
	 */
	protected ReportColumn(final String str, final int idx) {
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
	protected String getTitle() {
		return this.title;
	}

	/**
	 * カラム順番を返す
	 * @return カラム順番
	 */
	protected int getIndex() {
		return this.index;
	}
}
