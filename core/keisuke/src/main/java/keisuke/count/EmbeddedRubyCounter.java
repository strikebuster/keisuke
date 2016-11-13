package keisuke.count;

import java.util.ArrayList;


public class EmbeddedRubyCounter extends RubyCounter {

	/**
	 * コンストラクター
	 *
	 */
	public EmbeddedRubyCounter() {
		super();
		this.scriptletFlag = true;
		this.scriptLangs = new ArrayList<ProgramLangRule>();
		this.scriptBlocks = new ArrayList<ScriptBlock>();
		addAreaComment(new AreaComment("<%--","--%>"));
		addScriptBlock(new ScriptBlock("<%", "%>"));
		setFileType("EmbeddedRuby");
	}
	
	/** "%"が%記法ではないケースのチェックメソッド */
	@Override
	protected boolean checkExcludeLiteralStringStart(String line, LiteralString literal) {
		String start = literal.getStartString();
		if (start.equals("%")) {
			int pos = line.indexOf(start);
			if (pos < 0) {
				// ありえないパスだが
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			String sbstart = this.onScriptBlock.getStartString();
			int pos2 = line.indexOf(sbstart);
			if (pos2 >= 0 && (pos2 < pos && pos2+sbstart.length() > pos)) {
				// %記法でなく "<%" の一部
				//System.out.println("[DEBUG] Exclude % Case : " + line);
				return true;
			}
		}
		return super.checkExcludeLiteralStringStart(line, literal);
	}
}
