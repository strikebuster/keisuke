package keisuke.count.diff;

/**
 * ファイル、ディレクトリの変更情報を示すオブジェクトの抽象基底クラスです。
 * 	インスタンス変数のゲッターはサブクラスで共通にして親で定義に変更
 *  ステータス文言のproperties定義対応のためコンストラクタ引数追加
 */
public abstract class AbstractDiffResult implements Comparable<AbstractDiffResult> {

	private DiffStatusText		dstext;
	private DiffFolderResult	parent;
	private DiffStatus			diffstatus;
	private String				status;
	private String				name;

	private int		addCount;
	private int		delCount;

	private int type = 0;
	protected static final int TYPE_FOLDER = 1;
	protected static final int TYPE_FILE = 2;

	/**
	 * 親フォルダーを指定するコンストラクタ
	 * @param parentResult 	親フォルダーの差分計測結果インスタンス
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public AbstractDiffResult(final DiffFolderResult parentResult, final DiffStatusText diffStatusText) {
		this.dstext = diffStatusText;
		this.parent = parentResult;
	}

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param nodename ノード名
	 * @param diffStatus 差分変更ステータス値
	 * @param parentResult 	親フォルダーの差分計測結果インスタンス
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public AbstractDiffResult(final String nodename, final DiffStatus diffStatus,
			final DiffFolderResult parentResult, final DiffStatusText diffStatusText) {
		this.dstext = diffStatusText;
		this.parent = parentResult;
		this.setName(nodename);
		this.setDiffStatus(diffStatus);
	}

	/**
	 * このノードのタイプ（ファイルかディレクトリ）をint値で返す
	 * @return ファイルなら２、ディレクトリなら１
	 */
	protected int nodeType() {
		return this.type;
	}

	/**
	 * このノードのタイプを設定する
	 * @param nodetype ノードのタイプ値
	 */
	protected void setNodeType(final int nodetype) {
		this.type = nodetype;
	}

	/**
	 * 親フォルダーの差分計測結果を返す
	 * @return 親フォルダーの差分計測結果インスタンス
	 */
	public DiffFolderResult getParent() {
		return this.parent;
	}

	/**
	 * 差分変更ステータスを返す
	 * @return 差分変更ステータス値
	 */
	public DiffStatus getDiffStatus() {
		return this.diffstatus;
	}

	/**
	 * 差分変更ステータスと対応する表示文言を設定する
	 * @param diffStatus 差分変更ステータス値
	 */
	public void setDiffStatus(final DiffStatus diffStatus) {
		this.diffstatus = diffStatus;
		this.status = dstext.getText(diffStatus);
	}

	/**
	 * 差分変更ステータスの表示文言を返す
	 * @return 差分変更ステータスを示す表示文言
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * ノード（ファイルまたはディレクトリ）の名称を返す
	 * @return ノード名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ノード名を設定する
	 * @param nodename ノード名
	 */
	public void setName(final String nodename) {
		this.name = nodename;
	}

	/**
	 * 差分計測結果の増分行数を返す
	 * @return 増分行数
	 */
	public int getAddCount() {
		return this.addCount;
	}

	/**
	 * 差分計測結果の増分行数を設定する
	 * @param addcnt 増分行数
	 */
	public void setAddCount(final int addcnt) {
		this.addCount = addcnt;
	}

	/**
	 * 差分計測結果の削減行数を返す
	 * @return 削減行数
	 */
	public int getDelCount() {
		return this.delCount;
	}

	/**
	 * 差分計測結果の削減行数を設定する
	 * @param delcnt 削減行数
	 */
	public void setDelCount(final int delcnt) {
		this.delCount = delcnt;
	}

	/**
	 * 自分と引数で指定された別のインスタンスとの大小を比較して結果をintの正負で返す
	 * @param another 比較対象のインスタンス
	 * @return 自分が大きい場合は正の整数、小さい場合は負の整数
	 * @throws NullPointerException anotherがnullの場合に発行
	 */
	public int compareTo(final AbstractDiffResult another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException();
		}
		if (this.type != another.type) {
			return (this.type - another.type);
		}
		return (this.name).compareTo(another.name);
	}

	/**
	 * 自ノードの計測ルートからのパス名を返す
	 * @return パス名
	 */
	public String getPath() {
		DiffFolderResult tempParent = this.parent;
		StringBuilder pathBuilder = new StringBuilder();
		while (tempParent != null) {
			pathBuilder.insert(0, "/");
			pathBuilder.insert(0, tempParent.getName());

			tempParent = tempParent.getParent();
		}
		pathBuilder.append(this.name);

		return pathBuilder.toString();
	}

	/**
	 * 差分計測結果を表示用テキストに整形する
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected abstract String render(int nest);

	/**
	 * 差分計測結果を表示用テキストを返す
	 * @return 表示用テキスト
	 */
	@Override
	public String toString() {
		return render(0);
	}

	/**
	 * ノードに対し一意になる内部識別名称を返す
	 * @return 内部識別名称
	 */
	public String hashCodeName() {
		AbstractDiffResult obj = this;

		StringBuilder sb = new StringBuilder();
		sb.append(obj.hashCode());

		while (true) {
			obj = obj.getParent();
			if (obj != null) {
				sb.insert(0, obj.hashCode() + "_");
			} else {
				break;
			}
		}

		return sb.toString();
	}

}
