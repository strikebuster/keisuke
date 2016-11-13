package keisuke.count.xmldefine;

public class CommentExpressionDefine {
	private String start = null;
	
	public void setStart(String str) {
		this.start = str;
	}
	
	public String getStart() {
		return this.start;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getStart());
		return sb.toString();
	}
}
