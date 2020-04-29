package keisuke.count;

/**
 * Enumerator for path styles as path notation about counted source files.
 */
public enum PathStyleEnum {

	/** base directory */
	BASE("base", true),
	/** sub directory of base */
	SUB("sub", true),
	/** ver.1 compatible, base directory starting with '/' */
	SHOWDIR("showDirectroy", true),
	/** only file name without directory */
	NO("no", false);

	// スタイル名文字列値
	private String styleName;
	// ディレクトリ名を含むスタイルか
	private boolean hasDirectoryPath = false;

	/**
	 * コンストラクタ
	 * @param name スタイル名
	 * @param hasDirectory 表記パスにディレクトリを含むかの真偽
	 */
	PathStyleEnum(final String name, final boolean hasDirectory) {
		this.styleName = name;
		this.hasDirectoryPath = hasDirectory;
	}

	/**
	 * パススタイルの値を返す
	 * @return スタイル名
	 */
	public String value() {
		return this.styleName;
	}

	/**
	 * パススタイルがディレクトリを含むかの真偽を返す
	 * @return ディレクトリパスを含めばtrue
	 */
	public boolean hasDirectory() {
		return this.hasDirectoryPath;
	}
}
