package keisuke.count.diff;


/**
 * ファイルの変更状況を示すオブジェクトです。
 * keisuke: パッケージの変更とtype変数追加
 *	type変数はComparatorで利用
 *	親クラスのインスタンス変数のゲッターはFolderも共通にして親で定義に変更
 *  ステータス文言のproperties定義対応のためコンストラクタ引数追加
 */
public class DiffFileResult extends AbstractDiffResult {


	private String	fileType;
	private String	category;


	public DiffFileResult(DiffFolderResult parent, DiffStatusText dst) {
		super(parent, dst);
		this.type = super.TYPE_FILE; 
		setFileType("UNDEF");
	}

	public DiffFileResult(String name, DiffStatus status,
			DiffFolderResult parent, DiffStatusText dst) {
		super(name, status, parent, dst);
		this.type = super.TYPE_FILE; 
		setFileType("UNDEF");
	}


	public String getCategory() {
		if (category == null) {
			return "";
		}
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/*
	@Override
	public int getAddCount() {
		return this.addCount;
	}
	
	public void setAddCount(int addcnt) {
		this.addCount = addcnt;
	}

	@Override
	public int getDelCount() {
		return this.delCount;
	}
	
	public void setDelCount(int delcnt) {
		this.delCount = delcnt;
	}
	*/

	@Override
	public String render(int nest) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(getName());
		sb.append("[").append(getStatus()).append("]");
		sb.append(" +").append(getAddCount());
		sb.append(" -").append(getDelCount());
		sb.append("\n"); // keisuke: 呼び出し元でなく自身で改行追加
		return sb.toString();
	}
}
