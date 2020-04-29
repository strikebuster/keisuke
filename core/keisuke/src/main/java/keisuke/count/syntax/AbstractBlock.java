package keisuke.count.syntax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  ブロックコメントおよび文字列リテラルブロック、関数型の式ブロック
 *  などの要素の開始・終了記号を扱うための抽象クラス
 */
public abstract class AbstractBlock {

	// ブロック開始記号
	private String startingMark;
	// 開始記号が行頭のみ有効か示すフラグ
	private boolean startNeedHead = false;
	// 開始記号の直後は空白文字か行末が必要条件か示すフラグ
	private boolean startNeedBlanc = false;
	// ブロック終了記号
	private String endingMark;
	// 終了記号が行頭のみ有効か示すフラグ
	private boolean endNeedHead = false;
	// 開始記号の直後は空白文字か行末が必要条件か示すフラグ
	private boolean endNeedBlanc = false;
	// LUA用 N段長括弧ブロックの可変記号用の変数
	protected static final String LONGBRACKET_MAGIC_WORD = "@+@";
	private boolean longBracket = false;
	private String longBracketKey = "";

	protected AbstractBlock() { }

	/**
	 * 開始文字列と終了文字列を指定するコンストラクタ
	 *
	 * @param start 開始文字列
	 * @param end   終了文字列
	 */
	public AbstractBlock(final String start, final String end) {
		this.setStartString(start);
		this.setEndString(end);
	}

	/**
	 *  "@?@"等可変キーワードを含む開始記号として設定された生の文字列を返す
	 * @return 開始記号定義文字列
	 */
	public String startRawString() {
		return this.startingMark;
	}

	/**
	 *  "@?@"等可変キーワードを含む終了記号として設定された生の文字列を返す
	 * @return 終了記号定義文字列
	 */
	public String endRawString() {
		return this.endingMark;
	}

	/**
	 * ブロックの開始記号文字列を設定します
	 * @param start 開始定義文字列
	 */
	public void setStartString(final String start) {
		if (start == null) {
			this.startingMark = "";
			return;
		}
		String str = start;
		if (str.charAt(0) == '^') {
			// 先頭しか有効でないもの　例) =begin -> "^=begin "
			this.startNeedHead = true;
			str = str.substring(1);
		}
		int pos = str.indexOf(LONGBRACKET_MAGIC_WORD);
		if (pos > 0) {
			// １文字以上の文字列の後ろに"@=@"があれば可変記号指定と判定
			this.longBracket = true;
		}
		if (str.charAt(str.length() - 1) == ' ') {
			// 直後が空白でなければならないもの　例) =begin -> "^=begin "
			this.startNeedBlanc = true;
			str = str.substring(0, str.length() - 1);
		}
		this.startingMark = str;
	}

	/**
	 * ブロックの開始記号文字列を取得します
	 * 可変部定義がある場合は事前に不明なのでprefix部分だけ返す
	 * @return 開始判定文字列
	 */
	public String getStartString() {
		if (this.longBracket) {
			int pos = this.startingMark.indexOf(LONGBRACKET_MAGIC_WORD);
			if (pos > 0) {
				// 可変部は事前に不明なのでprefixで判定するためにそれだけ返す
				return this.startingMark.substring(0, pos);
			}
		}
		return this.startingMark;
	}

	/**
	 * 開始記号が行頭のみ有効フラグを取得します
	 * @return 該当ならtrue
	 */
	public boolean checkStartingMarkRequiredAtHead() {
		return this.startNeedHead;
	}

	/**
	 * 開始記号直後が行末か空白のみ有効フラグを取得します
	 * @return 該当ならtrue
	 */
	public boolean checkStartingMarkRequiredFollowingBlanc() {
		return this.startNeedBlanc;
	}

	/**
	 * ブロックの終了記号文字列を設定します
	 * @param end 終了定義文字列
	 */
	public void setEndString(final String end) {
		if (end == null || end.length() == 0) {
			this.endingMark = "";
			return;
		}
		String str = end;
		if (str.charAt(0) == '^') {
			// 先頭しか有効でないもの　例)　=end -> "^=end "
			this.endNeedHead = true;
			str = str.substring(1);
		}
		if (str.charAt(str.length() - 1) == ' ') {
			// 直後が空白でなければならないもの　例）=end -> "^=end "
			this.endNeedBlanc = true;
			str = str.substring(0, str.length() - 1);
		}
		this.endingMark = str;
	}

	/**
	 * ブロックの終了記号文字列を取得します
	 * 可変部がある場合には可変部を開始部で定義された文字列に置換して返す
	 * @return 終了判定文字列
	 */
	public String getEndString() {
		if (this.longBracket) {
			// 可変部を置換して返す
			return this.endingMark.replace(LONGBRACKET_MAGIC_WORD, this.longBracketKey);
		}
		return this.endingMark;
	}

	/**
	 * 終了記号が行頭のみ有効フラグを取得します
	 * @return 該当ならtrue
	 */
	public boolean checkEndngMarkRequiredAtHead() {
		return this.endNeedHead;
	}

	/**
	 * 終了記号直後が行末か空白のみ有効フラグを取得します
	 * @return 該当ならtrue
	 */
	public boolean checkEndingMarkRequiredFollowingBlanc() {
		return this.endNeedBlanc;
	}

	/**
	 * 長括弧可変記号であるかチェックします
	 * @return 該当ならtrue
	 */
	public boolean checkLongBracket() {
		return this.longBracket;
	}

	/**
	 * 長括弧可変記号を設定します
	 * @param key 可変記号文字列
	 */
	public void setLongBracketKey(final String key) {
		this.longBracketKey = key;
	}

	/**
	 * ブロック開始記号の可変部設定後の開始文字列を取得します
	 * @return 開始判定文字列
	 */
	public String getActualStartString() {
		if (this.longBracket) {
			StringBuffer sb = new StringBuffer();
			int pos;
			pos = this.startingMark.indexOf(LONGBRACKET_MAGIC_WORD);
			if (pos > 0) {
				// prefix部
				sb.append(this.startingMark.substring(0, pos));
			}
			if (this.longBracketKey.length() > 0) {
				sb.append(this.longBracketKey);
			}
			pos += LONGBRACKET_MAGIC_WORD.length();
			if (pos < this.startingMark.length()) {
				// postfix部
				sb.append(this.startingMark.substring(pos));
			}
			return sb.toString();
		}
		return this.startingMark;
	}

	/**
	 * 長括弧開始記号の可変部取得用の正規表現を返す
	 * @return 開始文字列
	 */
	public String getStartRegxString() {
		if (this.longBracket) {
			// prefix(=*)postfix
			int pos = this.startingMark.indexOf(LONGBRACKET_MAGIC_WORD);
			String preStr = this.startingMark.substring(0, pos);
			String postStr = "";
			String chStr = this.startingMark.substring(pos - 1, pos);
			pos += LONGBRACKET_MAGIC_WORD.length();
			if (pos < this.startingMark.length()) {
				postStr = this.startingMark.substring(pos);
			}
			StringBuffer sb = new StringBuffer(Pattern.quote(preStr));
			sb.append("(");
			sb.append(Pattern.quote(chStr));
			sb.append("*)");
			sb.append(Pattern.quote(postStr));
			return sb.toString();
		}
		return this.startingMark;
	}

	/**
	 * 長括弧の開始記号か確認し可変部を設定して終端を返す
	 * @param line 処理対象文字列
	 * @return 開始記号があった場合の終端インデックス、なければ-1
	 */
	public int validateLongBracketStart(final String line) {
		int pos;
		String patternStr = this.getStartRegxString();
		//System.out.println("[DEBUG] LongBracket pattern = " + patternStr);
		//System.out.println("[DEBUG] line = " + line);
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			String key = matcher.group(1);
			this.setLongBracketKey(key);
			//System.out.println("[DEBUG] LongBracketKey = [=" + key + "] in : " + line);
			// 開始記号末尾の位置
			pos = matcher.end();
		} else {
			// 一致しなかったのでブロック開始しない
			//System.out.println("[DEBUG] Not LongBracketKey in : " + line);
			// 開始記号末尾の位置
			pos = -1;
		}
		return pos;
	}

}
