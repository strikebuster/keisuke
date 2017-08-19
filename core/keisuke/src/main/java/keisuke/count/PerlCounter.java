package keisuke.count;

/**
 * Perl用ステップカウンタ
 */
public class PerlCounter extends LabelHereDocScriptCounter {

	// 複数行コメントの終了が空行の場合のフラグ(Perl用)
	private boolean blancEndOfAreaCommentFlag = false;

	/**
	 * コンストラクター
	 *
	 */
	public PerlCounter() {
		super();
		this.addLineComment(new LineComment("#"));
		this.addLiteralString(new LiteralString("\"", "\"", "\\\""));
		this.addLiteralString(new LiteralString("'", "'", "\\'"));
		this.addLiteralString(new LabelHereDoc("<<@?@", "@?@")); // @?@は特別な意味あり
		this.addAreaComment(new AreaComment("^=pod ", "^=cut "));
		this.addAreaComment(new AreaComment("^=begin ", "^=end "));
		this.addAreaComment(new AreaComment("^=for ", ""));
		this.addAreaComment(new AreaComment("^=cut ", ""));
		this.setFileType("Perl");
	}

	/** 空行かどうかをチェック */
	@Override
	protected boolean checkBlancLine(final String line) {
		if (this.blancEndOfAreaCommentFlag && line.isEmpty()) {
			// 複数行コメントブロックの終了を示す空行
			AreaComment area = (AreaComment) this.peekStatusAsCommentRule().subject();
			area.subtractNest();
			if (area.checkNestQuit()) {
				this.blancEndOfAreaCommentFlag = false;
				this.popStatusAsCommentRule();
			}
			return true;
		}
		String trimedLine = line.trim();
		if (trimedLine.equals("")) {
			return true;
		}
		return false;
	}

	/** 複数行コメントが終了しているかチェックし、空文字列を返す */
	@Override
	protected String removeAreaCommentUntilEnd(
			final ProgramLangRule lang, final String line, final boolean head) {
		AreaComment area = (AreaComment) this.peekStatusAsCommentRule().subject();
		if (area == null) {
			// 空行終了をした直後の行で呼ばれた、コメント外部の処理を呼び出し
			if (line.length() > 0) {
				return this.removeCommentFromLeft(lang, line, 0);
			}
		}
		String end = area.getEndString();
		if (end == null || end.isEmpty()) {
			String start = area.getStartString();
			if (start.equals("=cut")) {
				// =cutは終了行なので開始として見つけた場合はこの行で終了
				area.subtractNest();
				this.popStatusAsCommentRule();
				return "";
			} else {
				// =forの場合空行までの段落がコメント扱い
				this.blancEndOfAreaCommentFlag = true;
				return "";
			}
		}
		// endが空ではない
		int pos = area.searchEndingMark(line, head, lang.isCaseInsense());
		if (pos >= 0) {
			// コメント終了があった
			this.popStatusAsCommentRule();
			// コメント終了記号の右側のコードの解析処理は不要
		}
		return "";
	}
}
