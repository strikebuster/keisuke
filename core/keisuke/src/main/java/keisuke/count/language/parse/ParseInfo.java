package keisuke.count.language.parse;

/**
 * ソースコード解析中に解析対象記号の対象行内での位置、長さ、記号などを保持する
 */
public class ParseInfo {
	private int position = -1;
	private int length = 0;
	private Object element = null;

	public ParseInfo() { }

	/**
	 * 解析対象記号の行内での文字位置を設定する
	 * @param pos 文字位置
	 */
	protected void setPosition(final int pos) {
		this.position = pos;
	}

	/**
	 * 解析対象記号の行内での文字位置を返す
	 * @return 文字位置
	 */
	protected int position() {
		return this.position;
	}

	/**
	 * 解析対象記号の行内での文字列長を設定する
	 * @param len 文字列長
	 */
	protected void setLength(final int len) {
		this.length = len;
	}

	/**
	 * 解析対象記号の行内での文字列長を返す
	 * @return 文字列長
	 */
	protected int length() {
		return this.length;
	}

	/**
	 * 解析対象記号のインスタンスを設定する
	 * @param obj 解析対象インスタンス
	 */
	protected void setElement(final Object obj) {
		this.element = obj;
	}

	/**
	 * 解析対象記号のインスタンスを返す
	 * @return 解析対象インスタンス
	 */
	protected Object element() {
		return this.element;
	}

}
