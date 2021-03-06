package keisuke.count.diff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import keisuke.DiffStatusEnum;
import keisuke.util.LogUtil;

/**
 * ディレクトリの差分変更結果を示すオブジェクトです。
 */
public class DiffFolderResult extends AbstractDiffResultForCount implements Serializable {
	private static final long serialVersionUID = 1L; // since ver.2.0.0

	/*
	 * インスタンス変数のゲッターはOverride
	 * ３つのインスタンス変数毎に子供を走査していたのを１度走査し３つの変数値をセットするロジックに変更
	 */
	private List<AbstractDiffResultForCount> children = new ArrayList<AbstractDiffResultForCount>();
	private boolean evaluated = false;

	public DiffFolderResult() { }

	/**
	 * 自ノードの名称と差分変更ステータス、親フォルダーを指定するコンストラクタ
	 * @param name ノード名
	 * @param status 差分変更ステータス値
	 * @param parent 親フォルダーの差分計測結果インスタンス
	 */
	public DiffFolderResult(final String name, final DiffStatusEnum status,
			final DiffFolderResult parent) {
		super(name, status, parent);
		this.setIsFile(false);
	}

	/**
	 * 子ノードリストに追加する
	 * @param child 追加する子ノードの差分計測結果インスタンス
	 */
	public void addChild(final AbstractDiffResultForCount child) {
		if (child != null) {
			this.children.add(child);
		}
	}

	/**
	 * 子ノードリストを返す
	 * @return 子ノード差分計測結果のリスト
	 */
	public List<AbstractDiffResultForCount> getChildren() {
		return this.children;
	}

	/**
	 * 子ノードリストをノードタイプ＞ノード名でソートしたリストを返す
	 * @return ソート済みの子ノード差分計測結果のリスト
	 */
	public List<AbstractDiffResultForCount> getSortedChildren() {
		List<AbstractDiffResultForCount> list = new ArrayList<AbstractDiffResultForCount>(this.getChildren());
		Collections.sort(list);
		return list;
	}

	/**
	 * 子要素をスキャンして下位の変更ステータスと差分行数を評価して
	 * 自身の変更ステータスと差分行数を設定する
	 */
	private void evaluateChildren() {
		if (this.evaluated) {
			return;
		}
		int addcnt = 0;
		int delcnt = 0;
		for (AbstractDiffResultForCount obj : getChildren()) {
			addcnt += obj.addedSteps();
			delcnt += obj.deletedSteps();
		}
		this.setSteps(addcnt, delcnt);
		DiffStatusEnum stat = super.status();
		if (stat == null) {
			LogUtil.warningLog("Null value is unexpected for diff status.");
		}
		if (stat == DiffStatusEnum.MODIFIED && addcnt == 0 && delcnt == 0) {
			this.setDiffStatus(DiffStatusEnum.UNCHANGED);
		}
		this.evaluated = true;
	}

	/** {@inheritDoc} */
	@Override
	public DiffStatusEnum status() {
		this.evaluateChildren();
		return super.status();
	}

	/** {@inheritDoc} */
	@Override
	public long addedSteps() {
		this.evaluateChildren();
		return super.addedSteps();
	}

	/** {@inheritDoc} */
	@Override
	public long deletedSteps() {
		this.evaluateChildren();
		return super.deletedSteps();
	}

}
