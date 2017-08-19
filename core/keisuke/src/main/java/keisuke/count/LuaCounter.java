package keisuke.count;

/**
 * LUA用ステップカウンタ（N段長括弧対応）
 */
public class LuaCounter extends DefaultStepCounter {

	/**
	 * コンストラクター
	 *
	 */
	public LuaCounter() {
		super();
		this.addLineComment(new LineComment("--"));
		this.addAreaComment(new AreaComment("--[[", "]]"));
		this.addAreaComment(new AreaComment("--[=@+@[", "]=@+@]")); // @+@は特別な意味あり
		this.addLiteralString(new LiteralString("\"", "\"", "\\\""));
		this.addLiteralString(new LiteralString("'", "'", "\\'"));
		this.addLiteralString(new LiteralString("[[", "]]"));
		this.addLiteralString(new LiteralString("[=@+@[", "]=@+@]")); // @+@は特別な意味あり
		this.setFileType("Lua");
	}

	/* 複数行ブロックコメントの開始から行末までの処理をする */
	@Override
	public String dealAreaCommentStart(
			final ProgramLangRule lang, final String line, final AreaComment area) {
		if (area.checkLongBracket()) {
			// N段長括弧コメント
			int pos = area.validateLongBracketStart(line);
			if (pos < 0) {
				// 開始記号に一致しなかった
				// --行コメントとして扱う
				//System.out.println("[DEBUG] Not LongBracket, but LineComment : " + line);
				this.popStatusAsCommentRule();
				area.subtractNest();
				return "";
			} else {
				// 開始記号より右のコメントの処理
				return this.removeAreaCommentUntilEnd(lang, line.substring(pos), LINE_NO_HEAD);
			}
		} else {
			// 通常の複数行コメント
			return super.dealAreaCommentStart(lang, line, area);
		}
	}

	/* リテラル文字列の開始から行末までの処理をする */
	@Override
	public String dealLiteralStringStart(
			final ProgramLangRule lang, final String line, final LiteralString literal) {
		StringBuilder sb = new StringBuilder();
		if (literal.checkLongBracket()) {
			// N段長括弧リテラル処理
			int pos = literal.validateLongBracketStart(line);
			if (pos < 0) {
				// 開始記号に一致しなかった
				//System.out.println("[DEBUG] Not LongBracket : " + line);
				this.popStatusAsLiteralRule();
				pos = 1;
				sb.append(line.substring(0, pos));
				sb.append(this.removeCommentFromLeft(lang, line.substring(pos), 1));
			} else {
				// 開始記号より右のリテラルの処理
				sb.append(line.substring(0, pos));
				sb.append(this.searchLiteralStringEnd(lang, line.substring(pos)));
			}
			return sb.toString();
		} else {
			// 通常の引用符リテラル
			return super.dealLiteralStringStart(lang, line, literal);
		}
	}
}
