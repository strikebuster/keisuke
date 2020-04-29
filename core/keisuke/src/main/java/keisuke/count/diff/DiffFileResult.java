package keisuke.count.diff;

import java.io.Serializable;

import keisuke.DiffStatusEnum;

/**
 * ファイルの差分変更結果を示すオブジェクトです。
 */
public class DiffFileResult extends AbstractDiffResultForCount implements Serializable {
	private static final long serialVersionUID = 1L; // since ver.2.0.0

	private String sourceType = null;
	private String sourceCategory = "";

	public DiffFileResult() { }

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param name ノード名
	 * @param status 差分変更ステータス値
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 */
	public DiffFileResult(final String name, final DiffStatusEnum status,
			final DiffFolderResult parent) {
		super(name, status, parent);
		this.setIsFile(true);
		//this.setSourceType("UNDEF");
	}

	/**
	 * カテゴリを返す
	 * @return カテゴリ名
	 */
	public String sourceCategory() {
		return sourceCategory;
	}

	/**
	 * カテゴリを設定する
	 * @param category カテゴリ名
	 */
	public void setSourceCategory(final String category) {
		if (category == null) {
			this.sourceCategory = "";
		} else {
			this.sourceCategory = category;
		}
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

	/**
	 * 計測対象のソースコードが未対応であればtrueを返す
	 * @return 未対応ならtrue
	 */
	@Override
	public boolean isUnsupported() {
		return (this.sourceType == null);
	}
}
