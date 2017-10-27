package keisuke.count;

/**
 * ソースコードファイルからコメントを切り落とした有効なコード行と
 * 空白行からなるソースを保持する
 */
public class NakedSourceCode {

	private String source = null;
	private String sourceCategory = null;
	private boolean toBeIgnored = false;

	/**
	 * コンストラクタ
	 * @param code 有効行のみのソースコード文字列
	 * @param category カテゴリ指定文字列
	 * @param ignore 計測対象から除外されるか指定するフラグ
	 */
	public NakedSourceCode(final String code, final String category, final boolean ignore) {
		this.source = code;
		this.sourceCategory = category;
		this.toBeIgnored = ignore;
	}

	/**
	 * 保持するソースコードを返す
	 * @return ソースコード文字列
	 */
	public String code() {
		return this.source;
	}

	/**
	 * 計測定義でカテゴリ指定されているソースであればカテゴリ指定文字列を返す
	 * @return カテゴリ指定文字列
	 */
	public String category() {
		return this.sourceCategory;
	}

	/**
	 * 計測定義で無視する指定に該当するソースか判定する
	 * @return 無視するならtrueを返す
	 */
	public boolean isIgnored() {
		return this.toBeIgnored;
	}
}
