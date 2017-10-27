package keisuke;

/**
 * ソースコードファイルのステップ数計測結果の基本クラス
 */
public class StepCountResult {

	private String filePath = "";
	private String sourceType = "";
	private String sourceCategory = "";
	private long execSteps = 0;
	private long blancSteps = 0;
	private long commentSteps = 0;

	public StepCountResult() { }

	public StepCountResult(final String path, final String type, final String category,
			final long exec, final long blanc, final long comment) {

		this.setFilePath(path);
		this.setSourceType(type);
		this.setSourceCategory(category);
		this.setSteps(exec, blanc, comment);
	}

	/**
	 * 計測対象のファイルパスを返す
	 * @return ファイルパス
	 */
	public String filePath() {
		return this.filePath;
	}

	/**
	 * 計測対象のファイルパスを設定する
	 * @param path ファイルパス
	 */
	public void setFilePath(final String path) {
		this.filePath = path;
	}

	/**
	 * 計測対象のファイルのソースコードタイプを返す
	 * @return ソースコードタイプ
	 */
	public String sourceType() {
		return this.sourceType;
	}

	/**
	 * 計測対象のファイルのソースコードタイプを設定する
	 * @param type ソースコードタイプ
	 */
	public void setSourceType(final String type) {
		this.sourceType = type;
	}

	/**
	 * 計測対象のファイルに指定されたカテゴリ文字列を返す
	 * @return カテゴリ文字列
	 */
	public String sourceCategory() {
		return this.sourceCategory;
	}

	/**
	 * 計測対象のファイルに指定されたカテゴリ文字列を設定する
	 * @param category カテゴリ文字列
	 */
	public 	void setSourceCategory(final String category) {
		this.sourceCategory = category;
	}

	/**
	 * 計測対象のソースコードの実行ステップ数を返す
	 * @return 実行スッテプ数
	 */
	public long execSteps() {
		return this.execSteps;
	}

	/**
	 * 計測対象のソースコードの空白ステップ数を返す
	 * @return 空白ステップ数
	 */
	public long blancSteps() {
		return this.blancSteps;
	}

	/**
	 * 計測対象のソースコードのコメントステップ数を返す
	 * @return コメントステップ数
	 */
	public long commentSteps() {
		return this.commentSteps;
	}

	/**
	 * 計測対象のソースコードの実行/空白/コメントステップ数を設定する
	 * @param exec 実行スッテプ数
	 * @param blanc 空白ステップ数
	 * @param comment コメントステップ数
	 */
	public void setSteps(final long exec, final long blanc, final long comment) {
		this.execSteps = exec;
		this.blancSteps = blanc;
		this.commentSteps = comment;
	}

	/**
	 * 計測対象のソースコードの全ステップ数を返す
	 * @return 全ステップ数
	 */
	public long sumSteps() {
		return this.execSteps + this.blancSteps + this.commentSteps;
	}
}
