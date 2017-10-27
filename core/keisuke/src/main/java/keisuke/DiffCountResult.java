package keisuke;

/**
 * ソースコードファイルの差分ステップ数計測結果の基本クラス
 */
public class DiffCountResult {

	private String nodeName = "";
	private String filePath = "";
	private boolean isFile = false;
	private DiffStatusEnum diffStatus = null;
	private long addedSteps = 0;
	private long deletedSteps = 0;

	public DiffCountResult() { }

	/**
	 * DiffCount結果の対象ノードがファイルかチェックする
	 * @return ファイルならtrue
	 */
	public boolean isFile() {
		return this.isFile;
	}

	/**
	 * DiffCount結果の対象ノードがファイルであるか真偽値を設定する
	 * @param bool ファイルであるか示す真偽値
	 */
	protected void setIsFile(final boolean bool) {
		this.isFile = bool;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの名称を返す
	 * @return ノード名
	 */
	public String nodeName() {
		return this.nodeName;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの名称を設定する
	 * @param name ノード名
	 */
	protected void setNodeName(final String name) {
		this.nodeName = name;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリのパス名を返す
	 * @return パス名
	 */
	public String filePath() {
		return this.filePath;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリのパス名を設定する
	 * @param path パス名
	 */
	public void setFilePath(final String path) {
		this.filePath = path;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの変更ステータスを返す
	 * @return 変更ステータス
	 */
	public DiffStatusEnum status() {
		return this.diffStatus;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの変更ステータスを設定する
	 * @param status 変更ステータス
	 */
	public void setDiffStatus(final DiffStatusEnum status) {
		this.diffStatus = status;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの追加行数を返す
	 * @return diff追加行数(正の整数)
	 */
	public long addedSteps() {
		return this.addedSteps;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの削除行数を返す
	 * @return diff削除行数(正の整数）
	 */
	public long deletedSteps() {
		return this.deletedSteps;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの数値一式を設定する
	 * @param add diff追加行数(正の整数)
	 * @param del diff削除行数(正の整数）
	 */
	public void setSteps(final long add, final long del) {
		this.addedSteps = add;
		this.deletedSteps = del;
	}

	/**
	 * Diff差分計測結果のステータスがUNCHANGEDかチェックする
	 * @return UNCHANGEDならtrue
	 */
	public boolean isUnchanged() {
		if (this.diffStatus.equals(DiffStatusEnum.UNCHANGED)) {
			return true;
		}
		return false;
	}

	/**
	 * Diff差分計測結果のステータスがUNSUPPORTEDかチェックする
	 * @return UNSUPPORTEDならtrue
	 */
	public boolean isUnsupported() {
		if (this.diffStatus.equals(DiffStatusEnum.UNSUPPORTED)) {
			return true;
		}
		return false;
	}
}
