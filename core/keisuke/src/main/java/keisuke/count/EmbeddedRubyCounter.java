package keisuke.count;

/**
 * Embedded Ruby用ステップカウンタ
 */
public class EmbeddedRubyCounter extends RubyCounter {

	/**
	 * コンストラクター
	 */
	public EmbeddedRubyCounter() {
		super(true);
		this.addAreaComment(new AreaComment("<%--", "--%>"));
		this.addScriptBlock(new ScriptBlock("<%", "%>"));
		this.setFileType("EmbeddedRuby");
	}

	/* "%"が%記法ではないケースのチェックメソッド */
	@Override
	protected boolean checkExcludeLiteralStringStart(final String line, final LiteralString literal) {
		String start = literal.getStartString();
		if (start.equals("%")) {
			int pos = line.indexOf(start);
			if (pos < 0) {
				// ありえないパスだが
				System.out.println("![WARN] miss literal mark[" + start + "] in line=" + line);
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			ParseSourceCode psc = this.peekStatusAsScriptletCode();
			if (psc == null) {
				// ありえないパスだが
				System.out.println("![WARN] illegal status, not IN_SCRIPTLET in line=" + line);
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			String sbstart = psc.scriptBlock().getStartString();
			int pos2 = line.indexOf(sbstart);
			if (pos2 >= 0 && (pos2 < pos && pos2 + sbstart.length() > pos)) {
				// %記法でなく "<%" の一部
				//System.out.println("[DEBUG] Exclude % Case : " + line);
				return true;
			}
		}
		return super.checkExcludeLiteralStringStart(line, literal);
	}
}
