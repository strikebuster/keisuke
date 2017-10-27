package keisuke.count.diff;

import keisuke.DiffStatusEnum;
import keisuke.DiffStatusLabels;

/**
 * ファイルの差分変更結果を示すオブジェクトです。
 */
public class DiffFileResult extends AbstractDiffResultForCount {

	private String sourceType = "";
	private String sourceCategory = "";

	/**
	 * 親フォルダーを指定するコンストラクタ
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 * @param statusLabels 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffFileResult(final DiffFolderResult parent, final DiffStatusLabels statusLabels) {
		super(parent, statusLabels);
		this.setIsFile(true);
		this.setSourceType("UNDEF");
	}

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param name ノード名
	 * @param status 差分変更ステータス値
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 * @param statusLabels 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffFileResult(final String name, final DiffStatusEnum status,
			final DiffFolderResult parent, final DiffStatusLabels statusLabels) {
		super(name, status, parent, statusLabels);
		this.setIsFile(true);
		this.setSourceType("UNDEF");
	}

	/**
	 * カテゴリを返す
	 * @return カテゴリ名
	 */
	public String sourceCategory() {
		if (sourceCategory == null) {
			return "";
		}
		return sourceCategory;
	}

	/**
	 * カテゴリを設定する
	 * @param category カテゴリ名
	 */
	public void setSourceCategory(final String category) {
		this.sourceCategory = category;
	}

	/**
	 * ソースファイルタイプを返す
	 * @return ソースファイルタイプ
	 */
	public String sourceType() {
		return this.sourceType;
	}

	/**
	 * ソースファイルタイプを設定する
	 * @param filetype ソースファイルタイプ
	 */
	public void setSourceType(final String filetype) {
		this.sourceType = filetype;
	}

}
