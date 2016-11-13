package keisuke;

public class ReportColumn {
	private String title = "";
	private int index = -1;
	
	public ReportColumn(String str, int idx) {
		setTitle(str);
		setIndex(idx);
	}
	
	public void setTitle(String str) {
		this.title = str;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setIndex(int idx) {
		this.index = idx;
	}
	
	public int getIndex() {
		return this.index;
	}
}
