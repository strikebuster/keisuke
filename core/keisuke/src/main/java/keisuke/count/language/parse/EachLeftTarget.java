package keisuke.count.language.parse;

import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.LineComment;
import keisuke.count.syntax.LiteralString;
import keisuke.count.syntax.ScriptBlock;

/**
 * ソースコード解析の処理対象行内の未解析部分の最左の解析対象要素を探すため
 * 定義された各要素の解析種類毎の最左の情報を保持する
 */
public class EachLeftTarget {
	private ParseInfo lineComment = new ParseInfo();
	private ParseInfo areaComment = new ParseInfo();
	private ParseInfo literalString = new ParseInfo();
	private ParseInfo scriptBlock = new ParseInfo();

	public EachLeftTarget() { }

	/**
	 * 単一行コメントのうち最左にあるものを記録する
	 * @param pos 先頭からの文字位置
	 * @param len 開始記号の文字列長
	 * @param linecomm 対象コメント記号インスタンス
	 */
	public void setLineComment(final int pos, final int len, final LineComment linecomm) {
		this.lineComment.setPosition(pos);
		this.lineComment.setLength(len);
		if (linecomm != null) {
			this.lineComment.setElement((Object) linecomm);
		}
	}

	/**
	 * 複数行コメントのうち最左にあるものを記録する
	 * @param pos 先頭からの文字位置
	 * @param len 開始記号の文字列長
	 * @param area 対象コメント記号インスタンス
	 */
	public void setAreaComment(final int pos, final int len, final AreaComment area) {
		this.areaComment.setPosition(pos);
		this.areaComment.setLength(len);
		if (area != null) {
			this.areaComment.setElement((Object) area);
		}
	}

	/**
	 * リテラル定義のうち最左にあるものを記録する
	 * @param pos 先頭からの文字位置
	 * @param len 開始記号の文字列長
	 * @param literal 対象リテラル記号インスタンス
	 */
	public void setLiteralString(final int pos, final int len, final LiteralString literal) {
		this.literalString.setPosition(pos);
		this.literalString.setLength(len);
		if (literal != null) {
			this.literalString.setElement((Object) literal);
		}
	}

	/**
	 * Scriptletブロック終了記号のうち最左にあるものを記録する
	 * @param pos 先頭からの文字位置
	 * @param len 終了記号の文字列長
	 * @param script 対象Scriptlet終了記号インスタンス
	 */
	public void setScriptBlock(final int pos, final int len, final ScriptBlock script) {
		this.scriptBlock.setPosition(pos);
		this.scriptBlock.setLength(len);
		if (script != null) {
			this.scriptBlock.setElement((Object) script);
		}
	}

	/**
	 * 保持している解析種類毎の最左の記号の中から最も左に位置する解析対象を返す
	 * @return 最左の解析対象記号のParseInfoインスタンス
	 */
	public ParseInfo getMostLeftTarget() {
		// 最左のコメントを決定
		int tmpPosMin = -1;
		int tmplen = 0;
		// 行コメントで最左
		int lcPosMin = this.lineComment.position();
		int lclen = this.lineComment.length();
		// 暫定最左を行コメント
		tmpPosMin = lcPosMin;
		tmplen = lclen;

		// 行コメント vs. ブロックコメント
		// ブロックコメントで最左
		int acPosMin = this.areaComment.position();
		int aclen = this.areaComment.length();
		if (tmpPosMin < 0) {
			tmpPosMin = acPosMin;
			tmplen = aclen;
		} else {
			if (acPosMin >=  0) {
				if (acPosMin < tmpPosMin || (acPosMin == tmpPosMin && aclen > tmplen)) {
					// 最左は複数行コメント記号
					lcPosMin = -1;
					tmpPosMin = acPosMin;
					tmplen = aclen;
				} else {
					// 最左は単一行コメント記号
					acPosMin = -1;
				}
			}
		}

		// コメントとリテラルのどっちが左か決定
		int lsPosMin = this.literalString.position();
		int lslen = this.literalString.length();
		// vs. 文字列リテラル
		if (tmpPosMin < 0) {
			tmpPosMin = lsPosMin;
			tmplen = lslen;
		} else {
			if (lsPosMin >= 0) {
				if (lsPosMin < tmpPosMin || (lsPosMin == tmpPosMin && lslen > tmplen)) {
					// 最左はリテラル記号
					lcPosMin = -1;
					acPosMin = -1;
					tmpPosMin = lsPosMin;
					tmplen = lslen;
				} else {
					// 最左はコメント記号
					lsPosMin = -1;
				}
			}
		}
		// コメント・リテラルの最左とScriptのどっちが左か決定
		int sbPosMin = this.scriptBlock.position();
		int sblen = this.scriptBlock.length();
		// vs. Scriptlet
		//LogUtil.debugLog("vs.ScriptBlock: sbPosMin=" + sbPosMin + ", sblen=" + sblen
		//		+ ", elseone: tmpPosMin=" + tmpPosMin + ", tmplen=" + tmplen);
		if (tmpPosMin < 0) {
			tmpPosMin = sbPosMin;
			tmplen = sblen;
		} else {
			if (sbPosMin >= 0) {
				if (sbPosMin < tmpPosMin || (sbPosMin == tmpPosMin && sblen >= tmplen)) {
					// 最左はScript記号
					lcPosMin = -1;
					acPosMin = -1;
					lsPosMin = -1;
					tmpPosMin = sbPosMin;
					tmplen = sblen;
				} else {
					// 最左はコメント or リテラル記号
					sbPosMin = -1;
				}
			}
		}
		if (lcPosMin >= 0) {
			//LogUtil.debugLog("Find LineComment start[" + lcPosMin + "]: "
			//		+ (String)(this.lineComment.element));
			return this.lineComment;
		} else if (acPosMin >= 0) {
			//LogUtil.debugLog("Find AreaComment start[" + acPosMin + "]: "
			//		+ ((AreaComment)(this.areaComment.element)).getStartString());
			return this.areaComment;
		} else if (lsPosMin >= 0) {
			//LogUtil.debugLog("Find LiteralString start[" + lsPosMin + "]: "
			//		+ ((LiteralString)(this.literalString.element)).getStartString());
			return this.literalString;
		} else if (sbPosMin >= 0) {
			//LogUtil.debugLog("Find ScriptBlock end[" + sbPosMin + "]: "
			//		+ ((ScriptBlock)(this.scriptBlock.element)).getEndString());
			return this.scriptBlock;
		} else {
			return null;
		}
	}
}
