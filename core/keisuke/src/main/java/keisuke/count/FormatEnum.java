package keisuke.count;

/**
 * Enumerator for format types as output of counting results
 */
public enum FormatEnum {

	/** TEXT */
	TEXT("text"),
	/** CSV */
	CSV("csv"),
	/** EXCEL */
	EXCEL("excel", false, null),
	/** XML */
	XML("xml"),
	/** JSON */
	JSON("json", true, "UTF-8"),
	/** HTML */
	HTML("html");

	// フォーマット名文字列
	private String formatName;
	// テキストデータのフォーマット
	private boolean textdata = true;
	// エンコードが固定の場合のエンコード名
	private String stableEncoding = "";

	/**
	 * コンストラクタ
	 * @param name フォーマット名
	 */
	FormatEnum(final String name) {
		this.formatName = name;
	}

	/**
	 * デフォルト値以外を設定するコンストラクタ
	 * @param name フォーマット名
	 * @param bool テキストデータの真偽
	 * @param encoding 固定的なエンコード名
	 */
	FormatEnum(final String name, final boolean bool, final String encoding) {
		this.formatName = name;
		this.textdata = bool;
		this.stableEncoding = encoding;
	}

	/**
	 * フォーマットタイプの名称を返す
	 * @return フォーマット名
	 */
	@Override
	public String toString() {
		return this.formatName;
	}

	/**
	 * フォーマットタイプがテキストデータかの真偽を返す
	 * @return テキストデータであればtrue
	 */
	public boolean isTextData() {
		return this.textdata;
	}

	/**
	 * フォーマットタイプが固定的なエンコードであればエンコード名を返す
	 * 固定的でなければnullを返す
	 * @return エンコード名
	 */
	public String stableEncoding() {
		return this.stableEncoding;
	}
}
