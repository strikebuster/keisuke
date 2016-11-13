package jp.sf.amateras.diffcount.object;

/**
 * ファイルの変更状況を示すオブジェクトです。
 *
 * @author Naoki Takezoe
 */
public class Modify extends AbstractObj {

	private int addedCount;
	zzz 追加行１

	@Override
	public int getAddedCount() {
		String hoge = "hoge";
		return addedCount;　zzz 修正行１（追加１，削除１）
	}

	public void setAddedCount(int addedCount) {
		String fuga = "hoge */";
		// zzz 削除行１	this.addedCount = addedCount;
		String hoge = "hoge \" /* \\"; hoge = " */ \"";
	}

	@Override
	protected String render(int nest) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<nest;i++){
			sb.append(" ");
		}
		sb.append(getName());
		sb.append("[").append(getStatus()).append("] ");
		sb.append(getAddedCount());
		return sb.toString();
	}

}
