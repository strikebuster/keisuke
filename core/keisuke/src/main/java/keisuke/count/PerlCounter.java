package keisuke.count;

/**
 * keisuke:追加クラス
 * Perl用ステップカウンタ
 */
public class PerlCounter extends LabelHereDocScriptCounter {
	
	// 複数行コメントの終了が空行の場合のフラグ(Perl用)
	private boolean blancAreaCommentEnd = false;
	
	/**
	 * コンストラクター
	 *
	 */
	public PerlCounter() {
		super();
		addLineComment(new LineComment("#"));
		addLiteralString(new LiteralString("\"", "\"", "\\\""));
		addLiteralString(new LiteralString("'", "'", "\\'"));
		addLiteralString(new LabelHereDoc("<<@?@", "@?@")); // @?@は特別な意味あり
		addAreaComment(new AreaComment("^=pod ","^=cut "));
		addAreaComment(new AreaComment("^=begin ","^=end "));
		addAreaComment(new AreaComment("^=for ",""));
		addAreaComment(new AreaComment("^=cut ",""));
		setFileType("Perl");
	}
	
	/** 空行かどうかをチェック */
	@Override
	protected boolean checkBlancLine(String line){
		if (blancAreaCommentEnd && line.length() == 0) {
			// 複数行コメントブロックの終了を示す空行
			AreaComment area = onAreaComment;
			area.subtractNest();
			if (area.checkNestQuit()) {
				onAreaComment = null;
				blancAreaCommentEnd = false;
			}
			return true;
		}
		String trimedLine = line.trim();
		if(trimedLine.equals("")){
			return true;
		}
		return false;
	}
	
	/** 複数行コメントが終了しているかチェックし、空文字列を返す */
	@Override
	protected String removeAreaCommentUntillEnd(ProgramLangRule pr, String line, boolean head){
		AreaComment area = onAreaComment;
		if ( area == null ) {
			// 空行終了をした直後の行で呼ばれた、コメント外部の処理を呼び出し
			if (line.length() > 0) {
				return removeCommentFromLeft(pr, line, 0);
			}
		}
		String end = area.getEndString();
		if (end.length() == 0) {
			String start = area.getStartString();
			if (start.equals("=cut")) {
				// =cutは終了行なので開始として見つけた場合はこの行で終了
				area.subtractNest();
				onAreaComment = null;
				return "";
			} else {
				// =forの場合空行までの段落がコメント扱い
				blancAreaCommentEnd = true;
				return ""; 
			}
		}
		// endが空ではない
		int pos = area.searchEnd(line, head, pr.caseInsense);
		if (pos >= 0) {
			// コメント終了があった
			onAreaComment = null;
			// コメント終了記号の右側のコードの解析処理は不要		
		}
		return "";
	}

}
