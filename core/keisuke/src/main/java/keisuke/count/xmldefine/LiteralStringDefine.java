package keisuke.count.xmldefine;

public class LiteralStringDefine extends AbstractBlockDefine {

	private String escape = null;
	
	public LiteralStringDefine() { }
	
	public void setEscape(String str) {
		this.escape = str;
	}
	
	public String getEscape() {
		return this.escape;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getStart());
		sb.append(",");
		sb.append(this.getEnd());
		sb.append(",");
		sb.append(this.getEscape());
		return sb.toString();
	}
}
