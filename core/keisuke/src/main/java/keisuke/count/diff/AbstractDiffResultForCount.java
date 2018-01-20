package keisuke.count.diff;

import keisuke.DiffCountResult;
import keisuke.DiffStatusEnum;

/**
 * ファイル、ディレクトリの変更情報を示すオブジェクトの抽象基底クラスです。
 * 	インスタンス変数のゲッターはサブクラスで共通にして親で定義に変更
 *  ステータス文言のproperties定義対応のためコンストラクタ引数追加
 */
public abstract class AbstractDiffResultForCount extends DiffCountResult
		implements Comparable<AbstractDiffResultForCount> {

	private DiffFolderResult	parent;

	/**
	 * 親フォルダーを指定するコンストラクタ
	 * @param parentResult 	親フォルダーの差分計測結果インスタンス
	 */
	public AbstractDiffResultForCount(final DiffFolderResult parentResult) {
		this.parent = parentResult;
	}

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param nodename ノード名
	 * @param diffStatus 差分変更ステータス値
	 * @param parentResult 	親フォルダーの差分計測結果インスタンス
	 */
	public AbstractDiffResultForCount(final String nodename, final DiffStatusEnum diffStatus,
			final DiffFolderResult parentResult) {
		super();
		this.parent = parentResult;
		this.setNodeName(nodename);
		this.setDiffStatus(diffStatus);
	}

	/**
	 * 親フォルダーの差分計測結果を返す
	 * @return 親フォルダーの差分計測結果インスタンス
	 */
	public DiffFolderResult getParent() {
		return this.parent;
	}

	/**
	 * 自分と引数で指定された別のインスタンスとの大小を比較して結果をintの正負で返す
	 * フォルダーよりファイルが大きいと判定する
	 * フォルダー/ファイル同士であれば名称の文字列比較で判定する
	 * @param another 比較対象のインスタンス
	 * @return 自分が大きい場合は正の整数、小さい場合は負の整数
	 * @throws NullPointerException anotherがnullの場合に発行
	 */
	public int compareTo(final AbstractDiffResultForCount another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException();
		}
		if (this.isFile() != another.isFile()) {
			if (this.isFile()) {
				return 1;
			} else {
				return -1;
			}
		}
		return (this.nodeName()).compareTo(another.nodeName());
	}

	/**
	 * 自ノードの計測ルートからのパス名を返す
	 * @return パス名
	 */
	public String pathFromTop() {
		DiffFolderResult parentDir = this.parent;
		StringBuilder sb = new StringBuilder();
		while (parentDir != null) {
			sb.insert(0, "/");
			sb.insert(0, parentDir.nodeName());
			parentDir = parentDir.getParent();
		}
		sb.append(this.nodeName());
		return sb.toString();
	}

	/**
	 * ノードに対し一意になる内部識別名称を返す
	 * @return 内部識別名称
	 */
	public String hashCodeName() {
		AbstractDiffResultForCount obj = this;
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
