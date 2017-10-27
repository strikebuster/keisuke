package keisuke.count.language;

import keisuke.count.language.parse.ParseSourceCode;
import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.LiteralString;
import keisuke.count.syntax.ScriptBlock;
import keisuke.util.LogUtil;

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
				LogUtil.warningLog("miss literal mark[" + start + "] in line=" + line);
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			ParseSourceCode psc = this.peekStatusAsScriptletCode();
			if (psc == null) {
				// ありえないパスだが
				LogUtil.warningLog("illegal status, not IN_SCRIPTLET in line=" + line);
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			String sbstart = psc.scriptBlock().getStartString();
			int pos2 = line.indexOf(sbstart);
			if (pos2 >= 0 && (pos2 < pos && pos2 + sbstart.length() > pos)) {
				// %記法でなく "<%" の一部
				//LogUtil.debugLog("Exclude % Case : " + line);
				return true;
			}
		}
		return super.checkExcludeLiteralStringStart(line, literal);
	}
}
