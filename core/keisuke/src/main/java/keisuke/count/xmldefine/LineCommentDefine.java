package keisuke.count.xmldefine;

public class LineCommentDefine {

	private String start = null;
	private boolean needHead = false;
	private boolean needBlanc = false;
	private String lineDelimiter = null;
	private String escape = null;
	
	public LineCommentDefine() {}
	
	public void setStart(String str) {
		this.start = str;
	}
	
	public void setNeedHeadTrue() {
		this.needHead = true;
	}
	
	public void setNeedBlancTrue() {
		this.needBlanc = true;
	}
	
	public void setLineDelimiter(String str) {
		this.lineDelimiter = str;
	}
	
	public void setEscape(String str) {
		this.escape = str;
	}
	
	public String getStartDefineString() {
		StringBuilder sb = new StringBuilder();
		if (this.needHead) {
			sb.append("^");
			if (this.lineDelimiter != null && this.lineDelimiter.length() > 0) {
				sb.append(this.lineDelimiter);
				sb.append("^");
			}
		}
		sb.append(this.start);
		if (this.needBlanc) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public String getEscapeString() {
		return this.escape;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.start);
		sb.append(",");
		sb.append(this.needHead);
		sb.append(",");
		sb.append(this.needBlanc);
		sb.append(",");
		sb.append(this.lineDelimiter);
		sb.append(",");
		sb.append(this.escape);
		return sb.toString();
	}
}
