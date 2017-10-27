package keisuke.count.language.parse;

import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.LineComment;
import keisuke.count.syntax.LiteralString;
import keisuke.count.syntax.ScriptBlock;
import keisuke.count.syntax.SyntaxRules;

/**
 * ソースコード解析の対象行において解析対象の探索処理
 */
public class ParseSourceLine {

	private ParseInfo info;
	private ScriptBlock block;
	private SyntaxRules rule;
	private String line;
	private boolean head = false;

	/**
	 * 解析対象を内部に保持する
	 * scriptletプログラム出ない場合はblockにはnullを指定する
	 * @param scriptBlock 現在の解析対象のプログラムコードのScriptletブロックインスタンス
	 * @param langRule 現在の解析対象のプログラミング言語インスタンス
	 * @param target これから解析する行末までを含む文字列
	 * @param headFlag targetが行頭を含んでいればtrue
	 */
	public ParseSourceLine(final ScriptBlock scriptBlock, final SyntaxRules langRule,
			final String target, final boolean headFlag) {
		this.block = scriptBlock;
		this.rule = langRule;
		this.line = target;
		this.head = headFlag;
	}

	/**
	 * 最左に現れる解析対象記号を探索して結果を内部に保持する
	 */
	public void searchMostLeftTarget() {
		// コメント記述用
		int lcidx = -1;
		int acidx = -1;
		int lcPosMin = -1;
		int acPosMin = -1;
		int lclen = 0;
		int aclen = 0;
		// リテラル文字列記述用
		int lsidx = -1;
		int lsPosMin = -1;
		int lslen = 0;
		// Script記述用
		int sbPosMin = -1;
		int sblen = 0;

		// 単一行コメント記号をチェック
		for (int i = 0; i < this.rule.lineComments().size(); i++) {
			LineComment linecomm = this.rule.lineComments().get(i);
			int pos = linecomm.searchStartingMark(this.line, this.head, this.rule.isCaseInsense());
			if (pos >= 0) {
				String start = linecomm.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (lcPosMin < 0 || pos < lcPosMin
						|| (pos == lcPosMin && start.length() > lclen)) {
					// 最左の単一行コメント記号
					lcPosMin = pos;
					lcidx = i;
					lclen = start.length();
				}
			}
		}
		// 複数行コメント記号をチェック
		for (int i = 0; i < this.rule.areaComments().size(); i++) {
			AreaComment area = this.rule.areaComments().get(i);
			int pos = area.searchStartingMark(this.line, this.head, this.rule.isCaseInsense());
			if (pos >= 0) {
				String start = area.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (acPosMin < 0 || pos < acPosMin
						|| (pos == acPosMin && start.length() > aclen)) {
					// 最左の複数行コメント記号
					acPosMin = pos;
					acidx = i;
					aclen = start.length();
				}
			}
		}
		// リテラル文字列記号をチェック
		for (int i = 0; i < this.rule.literalStrings().size(); i++) {
			LiteralString literal = this.rule.literalStrings().get(i);
			int pos = literal.searchStartingMark(this.line);
			if (pos >= 0) {
				String start = literal.getStartString();
				// 開始位置がより左か、同じならマッチした長さが長い方を最左とする
				if (lsPosMin < 0 || pos < lsPosMin
						|| (pos == lsPosMin && start.length() > lslen)) {
					// 最左のリテラル記号
					lsPosMin = pos;
					lsidx = i;
					lslen = start.length();
				}
			}
		}
		// Script終了記号をチェック
		if (this.block != null) {
			int pos = this.block.searchEndingMark(this.line);
			if (pos >= 0) {
				String end = this.block.getEndString();
				sbPosMin = pos;
				sblen = end.length();
			}
		}
		// 各種類ごとの探索結果の保管と最左対象の決定
		EachLeftTarget each = new EachLeftTarget();
		if (lcPosMin >= 0) {
			each.setLineComment(lcPosMin, lclen, this.rule.lineComments().get(lcidx));
		}
		if (acPosMin >= 0) {
			each.setAreaComment(acPosMin, aclen, this.rule.areaComments().get(acidx));
		}
		if (lsPosMin >= 0) {
			each.setLiteralString(lsPosMin, lslen, this.rule.literalStrings().get(lsidx));
		}
		if (sbPosMin >= 0) {
			each.setScriptBlock(sbPosMin, sblen, this.block);
		}
		this.info = each.getMostLeftTarget();
	}

	/**
	 * 解析結果で対象が存在するか確認する
	 * @return 存在すればtrueを返す
	 */
	public boolean hasTarget() {
		if (this.info == null) {
			return false;
		}
		return true;
	}

	/**
	 * 最左の解析対象の行内での先頭からの文字位置を返す
	 * @return 先頭からの位置
	 */
	public int mostLeftPosition() {
		return this.info.position();
	}

	/**
	 * 最左の解析対象のインスタンスを返す
	 * @return 解析対象インスタンス
	 */
	public Object mostLeftTarget() {
		return this.info.element();
	}
}
