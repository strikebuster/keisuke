package keisuke.count.diff;


/**
 * ファイルの差分変更結果を示すオブジェクトです。
 */
public class DiffFileResult extends AbstractDiffResult {

	private String	fileType;
	private String	category;

	/**
	 * 親フォルダーを指定するコンストラクタ
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffFileResult(final DiffFolderResult parent, final DiffStatusText diffStatusText) {
		super(parent, diffStatusText);
		this.setNodeType(TYPE_FILE);
		this.setFileType("UNDEF");
	}

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param name ノード名
	 * @param status 差分変更ステータス値
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffFileResult(final String name, final DiffStatus status,
			final DiffFolderResult parent, final DiffStatusText diffStatusText) {
		super(name, status, parent, diffStatusText);
		this.setNodeType(TYPE_FILE);
		this.setFileType("UNDEF");
	}

	/**
	 * カテゴリを返す
	 * @return カテゴリ名
	 */
	public String getCategory() {
		if (category == null) {
			return "";
		}
		return category;
	}

	/**
	 * カテゴリを設定する
	 * @param categorytext カテゴリ名
	 */
	public void setCategory(final String categorytext) {
		this.category = categorytext;
	}

	/**
	 * ソースファイルタイプを返す
	 * @return ソースファイルタイプ
	 */
	public String getFileType() {
		return this.fileType;
	}

	/**
	 * ソースファイルタイプを設定する
	 * @param filetype ソースファイルタイプ
	 */
	public void setFileType(final String filetype) {
		this.fileType = filetype;
	}

	/** {@inheritDoc} */
	@Override
	public String render(final int nest) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(this.getName());
		sb.append("[").append(this.getStatus()).append("]");
		sb.append(" +").append(this.getAddCount());
		sb.append(" -").append(this.getDelCount());
		sb.append("\n");
		return sb.toString();
	}
}
