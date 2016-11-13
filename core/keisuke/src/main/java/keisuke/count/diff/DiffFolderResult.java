package keisuke.count.diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ディレクトリの変更情報を示すオブジェクトです。
 * keisuke: パッケージを変更しバグ修正とロジック変更
 *	インスタンス変数のゲッターはFileと共通にして親で定義に変更
 *	３つのインスタンス変数毎に子供を走査していたのを１度走査し３つの変数値をセットするロジックに変更
 *  ステータス文言のproperties定義対応のためコンストラクタ引数追加
 */
public class DiffFolderResult extends AbstractDiffResult {

	/*
	 * インスタンス変数のゲッターはOverride
	 * ３つのインスタンス変数毎に子供を走査していたのを１度走査し３つの変数値をセットするロジックに変更
	 */
	private List<AbstractDiffResult> children = new ArrayList<AbstractDiffResult>();
	private boolean evaluated = false;


	public DiffFolderResult(DiffFolderResult parent, DiffStatusText dst) {
		super(parent, dst);
		this.type = super.TYPE_FOLDER;
	}

	public DiffFolderResult(String name, DiffStatus status,
			DiffFolderResult parent, DiffStatusText dst) {
		super(name, status, parent, dst);
		this.type = super.TYPE_FOLDER;
		setDiffStatus(status);
	}
	
	public void addChild(AbstractDiffResult child) {
		if(child != null){
			this.children.add(child);
		}
	}

	public List<AbstractDiffResult> getChildren() {
		return this.children;
	}

	public List<AbstractDiffResult> getSortedChildren() {
		List<AbstractDiffResult> list = new ArrayList<AbstractDiffResult>(getChildren());
		Collections.sort(list);
		return list;
	}
	
	/*
	@Override
	public DiffStatus getStatus() {
		DiffStatus status = super.getStatus();
		if (status == DiffStatus.REMOVED || status == DiffStatus.ADDED) {
			return status;
		}

		for (AbstractDiffResult obj : getChildren()) {
			DiffStatus childStatus = obj.getStatus();
			if (childStatus != DiffStatus.NONE && childStatus != DiffStatus.UNSUPPORTED) {
				return DiffStatus.MODIFIED;
			}
		}

		return DiffStatus.NONE;
	}
	*/
	
	// keisuke: 子要素のスキャンを１度だけにするために追加
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
		setAddCount(addcnt);
		setDelCount(delcnt);
		DiffStatus stat = super.getDiffStatus();
		if (stat == null) {
			// rootの場合に仮で設定
			setDiffStatus(DiffStatus.MODIFIED);
		}
		if (stat == DiffStatus.MODIFIED && addcnt == 0 && delcnt == 0) {
			setDiffStatus(DiffStatus.NONE);
		}
		this.evaluated = true;
	}
		
	@Override
	public DiffStatus getDiffStatus() {
		evaluateChildren();
		return super.getDiffStatus();
	}
	
	@Override
	public int getAddCount() {
		evaluateChildren();
		return super.getAddCount();
	}

	@Override
	public int getDelCount() {
		evaluateChildren();
		return super.getDelCount();
	}

	@Override
	public String render(int nest) {
		evaluateChildren(); // keisuke: 子供を評価して自身の値を確定する
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(getName()).append("/");
		sb.append("[").append(super.getStatus()).append("]");
		sb.append(" +").append(super.getAddCount());
		sb.append(" -").append(super.getDelCount()).append("\n");

		for (AbstractDiffResult obj : getSortedChildren()) {
			sb.append(obj.render(nest + 1)); 
		}

		return sb.toString();
	}
	
}
