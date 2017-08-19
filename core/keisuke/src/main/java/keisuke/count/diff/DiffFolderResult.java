package keisuke.count.diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ディレクトリの差分変更結果を示すオブジェクトです。
 */
public class DiffFolderResult extends AbstractDiffResult {

	/*
	 * インスタンス変数のゲッターはOverride
	 * ３つのインスタンス変数毎に子供を走査していたのを１度走査し３つの変数値をセットするロジックに変更
	 */
	private List<AbstractDiffResult> children = new ArrayList<AbstractDiffResult>();
	private boolean evaluated = false;

	/**
	 * 親フォルダーを指定するコンストラクタ
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffFolderResult(final DiffFolderResult parent, final DiffStatusText diffStatusText) {
		super(parent, diffStatusText);
		this.setNodeType(TYPE_FOLDER);
	}

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param name ノード名
	 * @param status 差分変更ステータス値
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffFolderResult(final String name, final DiffStatus status,
			final DiffFolderResult parent, final DiffStatusText diffStatusText) {
		super(name, status, parent, diffStatusText);
		this.setNodeType(TYPE_FOLDER);
		this.setDiffStatus(status);
	}

	/**
	 * 子ノードリストに追加する
	 * @param child 追加する子ノードの差分計測結果インスタンス
	 */
	public void addChild(final AbstractDiffResult child) {
		if (child != null) {
			this.children.add(child);
		}
	}

	/**
	 * 子ノードリストを返す
	 * @return 子ノード差分計測結果のリスト
	 */
	public List<AbstractDiffResult> getChildren() {
		return this.children;
	}

	/**
	 * 子ノードリストをノードタイプ＞ノード名でソートしたリストを返す
	 * @return ソート済みの子ノード差分計測結果のリスト
	 */
	public List<AbstractDiffResult> getSortedChildren() {
		List<AbstractDiffResult> list = new ArrayList<AbstractDiffResult>(this.getChildren());
		Collections.sort(list);
		return list;
	}

	// 子要素のスキャンを１度だけにするために追加
	private void evaluateChildren() {
		if (this.evaluated) {
			return;
		}
		int addcnt = 0;
		int delcnt = 0;
		for (AbstractDiffResult obj : getChildren()) {
			addcnt += obj.getAddCount();
			delcnt += obj.getDelCount();
		}
		this.setAddCount(addcnt);
		this.setDelCount(delcnt);
		DiffStatus stat = super.getDiffStatus();
		if (stat == null) {
			// rootの場合に仮で設定
			this.setDiffStatus(DiffStatus.MODIFIED);
		}
		if (stat == DiffStatus.MODIFIED && addcnt == 0 && delcnt == 0) {
			this.setDiffStatus(DiffStatus.NONE);
		}
		this.evaluated = true;
	}

	/** {@inheritDoc} */
	@Override
	public DiffStatus getDiffStatus() {
		this.evaluateChildren();
		return super.getDiffStatus();
	}

	/** {@inheritDoc} */
	@Override
	public int getAddCount() {
		this.evaluateChildren();
		return super.getAddCount();
	}

	/** {@inheritDoc} */
	@Override
	public int getDelCount() {
		this.evaluateChildren();
		return super.getDelCount();
	}

	/** {@inheritDoc} */
	@Override
	public String render(final int nest) {
		this.evaluateChildren(); // 子供を評価して自身の値を確定する
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(this.getName()).append("/");
		sb.append("[").append(super.getStatus()).append("]");
		sb.append(" +").append(super.getAddCount());
		sb.append(" -").append(super.getDelCount()).append("\n");

		for (AbstractDiffResult obj : this.getSortedChildren()) {
			sb.append(obj.render(nest + 1));
		}

		return sb.toString();
	}

}
