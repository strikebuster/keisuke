package keisuke.count;

/**
 * Enumerator for format types as output of counting results
 */
public enum FormatEnum {

	/** TEXT */
	TEXT("text", "Text", "txt"),
	/** CSV */
	CSV("csv", "CSV", "csv"),
	/** EXCEL */
	EXCEL("excel", "Excel", "xls", false, null),
	/** XML */
	XML("xml", "XML", "xml"),
	/** JSON */
	JSON("json", "JSON", "json", true, "UTF-8"),
	/** HTML */
	HTML("html", "HTML", "html");

	// フォーマット名文字列値
	private String formatName;
	// フォーマット名ラベル
	private String formatLabel;
	// ファイル拡張子
	private String fileExtension;
	// テキストデータのフォーマット
	private boolean textdata = true;
	// エンコードが固定の場合のエンコード名
	private String stableEncoding = "";

	/**
	 * コンストラクタ
	 * @param name フォーマット名
	 * @param label 表示ラベル
	 * @param extension ファイル拡張子
	 */
	FormatEnum(final String name, final String label, final String extension) {
		this.formatName = name;
		this.formatLabel = label;
		this.fileExtension = extension;
	}

	/**
	 * デフォルト値以外を設定するコンストラクタ
	 * @param name フォーマット名
	 * @param label 表示ラベル
	 * @param extension ファイル拡張子
	 * @param bool テキストデータの真偽
	 * @param encoding 固定的なエンコード名
	 */
	FormatEnum(final String name, final String label, final String extension,
			final boolean bool, final String encoding) {
		this.formatName = name;
		this.formatLabel = label;
		this.fileExtension = extension;
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
	 * フォーマットタイプの値を返す
	 * @return フォーマット名
	 */
	public String value() {
		return this.formatName;
	}

	/**
	 * フォーマットタイプの表示ラベルを返す
	 * @return フォーマット表示ラベル
	 */
	public String label() {
		return this.formatLabel;
	}

	/**
	 * フォーマットタイプのファイル拡張子を返す
	 * @return ファイル拡張子
	 */
	public String fileExtension() {
		return this.fileExtension;
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
