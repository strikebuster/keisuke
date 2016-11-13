package keisuke.count.xmldefine;

public class BlockCommentDefine extends AbstractBlockDefine {

	private boolean nest = false;
	
	public BlockCommentDefine() { }
	
	public void setNestTrue() {
		this.nest = true;
	}
	
	public boolean getNest() {
		return this.nest;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getStart());
		sb.append(",");
		sb.append(this.getEnd());
		sb.append(",");
		sb.append(this.getNest());
		return sb.toString();
	}
}
