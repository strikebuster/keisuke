package keisuke.count.xmldefine;

/**
 * 言語ルール定義で単一行コメント定義を保持するクラス
 */
public class LineCommentDefine {

	private String start = null;
	private boolean needHead = false;
	private boolean needBlanc = false;
	private String lineDelimiter = null;
	private String escape = null;

	/**
	 * デフォルトコンストラクター
	 */
	protected LineCommentDefine() { }

	/**
	 * コメント開始記号を設定する
	 * @param startingMark コメント開始記号
	 */
	protected void setStart(final String startingMark) {
		this.start = startingMark;
	}

	/**
	 * 開始記号が行の先頭のみ有効であると設定する
	 * このメソッドが呼びだされるまで場所は任意で有効が
	 * デフォルト値として設定されている
	 */
	protected void setNeedHeadTrue() {
		this.needHead = true;
	}

	/**
	 * 開始記号の直後が空白のみ有効であると設定する
	 * このメソッドが呼びだされるまで直後は任意で有効が
	 * デフォルト値として設定されている
	 */
	protected void setNeedBlancTrue() {
		this.needBlanc = true;
	}

	/**
	 * 改行コード以外に行区切り記号が定義されている言語で
	 * 行区切り記号の直後が、行の先頭と見なされる場合には
	 * 行区切り記号を設定する。
	 * @param delimiterMark 行区切り記号
	 */
	protected void setLineDelimiter(final String delimiterMark) {
		this.lineDelimiter = delimiterMark;
	}

	/**
	 * コメント開始記号と同じ記号を含む別の意味の表記がある場合の
	 * 表記を設定する
	 * @param escapeNotation 開始記号文字のエスケープ表記
	 */
	protected void setEscape(final String escapeNotation) {
		this.escape = escapeNotation;
	}

	/**
	 * コメント開始記号の定義用表記を返す
	 * 古いバージョンで、先頭のみ有効、直後空白のみ有効、行区切り有効
	 * を開始記号の定義表記で記述していたことに由来する
	 * 定義はXMLとなりそれぞれ別個の属性として定義するが、実行系の
	 * モジュールのAPIが古い定義表記のために必要
	 * @return コメント開始記号の定義表記
	 */
	public String getStartDefineString() {
		StringBuilder sb = new StringBuilder();
		if (this.needHead) {
			sb.append("^");
			if (this.lineDelimiter != null && this.lineDelimiter.length() > 0) {
				sb.append(this.lineDelimiter);
				sb.append("^");
			}
		}
		sb.append(this.start);
		if (this.needBlanc) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * コメント開始記号と同じ記号を含む別の意味の表記がある場合の
	 * 表記を返す
	 * @return 開始記号文字のエスケープ表記
	 */
	public String getEscapeString() {
		return this.escape;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.start);
		sb.append(",");
		sb.append(this.needHead);
		sb.append(",");
		sb.append(this.needBlanc);
		sb.append(",");
		sb.append(this.lineDelimiter);
		sb.append(",");
		sb.append(this.escape);
		return sb.toString();
	}
}
