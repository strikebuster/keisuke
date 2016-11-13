package keisuke.count.xmldefine;

public abstract class AbstractBlockDefine {

	private String start = null;
	private String end = null;
	
	public void setStart(String str) {
		this.start = str;
	}
	
	public String getStart() {
		return this.start;
	}
	
	public void setEnd(String str) {
		this.end = str;
	}
	
	public String getEnd() {
		return this.end;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getStart());
		sb.append(",");
		sb.append(this.getEnd());
		return sb.toString();
	}
}
