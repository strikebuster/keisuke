package keisuke.swing;

/**
 * 計測結果の表示形式の列挙型
 */
enum DisplayStyleEnum {

	/** 出力フォーマット形式 */
	FORMAT("selected format"),

	/** テーブル形式 */
	TABLE("table");

	private String label = null;

	DisplayStyleEnum(final String text) {
		this.label = text;
	}

	String getValue() {
		return this.label;
	}
}
