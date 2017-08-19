package keisuke.count.xmldefine;

/**
 * 言語ルール定義で文字列リテラル定義を保持するクラス
 */
public class LiteralStringDefine extends AbstractBlockDefine {

	private String escape = null;

	/**
	 * デフォルトコンストラクター
	 */
	protected LiteralStringDefine() { }

	/**
	 * リテラル中に終了記号と同じ文字を記述する際のエスケープ表記を設定する
	 * @param escapeNotation 終了記号文字のエスケープ表記
	 */
	protected void setEscape(final String escapeNotation) {
		this.escape = escapeNotation;
	}

	/**
	 * リテラル中に終了記号と同じ文字を記述する際のエスケープ表記を返す
	 * @return 終了記号文字のエスケープ表記
	 */
	public String getEscape() {
		return this.escape;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(",");
		sb.append(this.getEscape());
		return sb.toString();
	}
}
