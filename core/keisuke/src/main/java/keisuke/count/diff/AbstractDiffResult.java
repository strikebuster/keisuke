package keisuke.count.diff;

/**
 * ファイル、ディレクトリの変更情報を示すオブジェクトの抽象基底クラスです。
 * keisuke: パッケージの変更とrender()をpublicに変更
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

	protected int type = 0;
	protected final int TYPE_FOLDER = 1;
	protected final int TYPE_FILE = 2;

	public AbstractDiffResult(DiffFolderResult parent, DiffStatusText dst) {
		this.dstext = dst;
		this.parent = parent;
	}

	public AbstractDiffResult(String name, DiffStatus status,
			DiffFolderResult parent, DiffStatusText dst) {
		this.dstext = dst;
		this.parent = parent;
		setName(name);
		setDiffStatus(status);
	}

	public DiffFolderResult getParent() {
		return this.parent;
	}

	public DiffStatus getDiffStatus() {
		return this.diffstatus;
	}

	public void setDiffStatus(DiffStatus status) {
		this.diffstatus = status;
		this.status = dstext.getText(status);
	}
	
	public String getStatus() {
		return this.status;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAddCount() {
		return this.addCount;
	}

	public void setAddCount(int addcnt) {
		this.addCount = addcnt;
	}

	public int getDelCount() {
		return this.delCount;
	}

	public void setDelCount(int delcnt) {
		this.delCount = delcnt;
	}
	
	public int compareTo(AbstractDiffResult adr) throws NullPointerException{
		if (adr == null) {
			throw new NullPointerException();
		}
		if (this.type != adr.type) {
			return (this.type - adr.type);
		}
		return (this.name).compareTo(adr.name);
	}

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

	protected abstract String render(int nest);

	@Override
	public String toString() {
		return render(0);
	}


	// TODO 親から出したほうがよいですねぇ
	public String getClassName() {
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
