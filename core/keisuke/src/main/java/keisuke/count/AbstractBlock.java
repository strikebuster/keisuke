package keisuke.count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  keisuke:追加クラス
 *  ブロックコメントおよび文字列リテラルブロック、関数型の式ブロック
 *  などの要素の開始・終了記号を扱うための抽象クラス
 */
public abstract class AbstractBlock {
	
	// ブロック開始記号
	protected String start;
	// 開始記号が行頭のみ有効か示すフラグ
	protected boolean startNeedHead = false;
	// 開始記号の直後は空白文字か行末が必要条件か示すフラグ
	protected boolean startNeedBlanc = false;
	// ブロック終了記号
	protected String end;
	// 終了記号が行頭のみ有効か示すフラグ
	protected boolean endNeedHead = false;
	// 開始記号の直後は空白文字か行末が必要条件か示すフラグ
	protected boolean endNeedBlanc = false;
	// LUA用 N段長括弧ブロックの可変記号用の変数
	public static String LONGBRACKET_MAGIC_WORD = "@+@";
	protected boolean longBracket = false;
	protected String longBracketKey = "";

	protected AbstractBlock() { }
	
	/**
	 * 開始文字列と終了文字列を指定するコンストラクタ
	 *
	 * @param start 開始文字列
	 * @param end   終了文字列
	 */
	public AbstractBlock(String start,String end){
		setStartString(start);
		setEndString(end);
	}
	
	/**
	 * ブロックの開始記号文字列を設定します
	 *
	 * @param start 開始文字列
	 */
	public void setStartString(String start){
		if (start == null) {
			this.start = "";
			return;
		}
		if (start.charAt(0) == '^') {
			// 先頭しか有効でないもの　例) =begin -> "^=begin "
			this.startNeedHead = true;
			start = start.substring(1);
		}
		int pos = start.indexOf(LONGBRACKET_MAGIC_WORD);
		if (pos > 0) {
			// １文字以上の文字列の後ろに"@=@"があれば可変記号指定と判定
			this.longBracket = true;
		}
		if (start.charAt(start.length()-1) == ' ') {
			// 直後が空白でなければならないもの　例) =begin -> "^=begin "
			this.startNeedBlanc = true;
			start = start.substring(0, start.length()-1);
		}
		this.start = start;
	}

	/**
	 * ブロックの開始記号文字列を取得します
	 *
	 * @return 開始文字列
	 */
	public String getStartString(){
		if (this.longBracket) {
			int pos = this.start.indexOf(LONGBRACKET_MAGIC_WORD);
			if (pos > 0) {
				// 可変部は事前に不明なのでprefixで判定するためにそれだけ返す
				return this.start.substring(0, pos);
			}
		}
		return this.start;
	}
	
	/**
	 * 開始記号が行頭のみ有効フラグを取得します
	 *
	 * @return 該当ならtrue
	 */
	public boolean getStartNeedHead() {
		return this.startNeedHead;
	}
	
	/**
	 * 開始記号直後が行末か空白のみ有効フラグを取得します
	 *
	 * @return 該当ならtrue
	 */
	public boolean getStartNeedBlanc() {
		return this.startNeedBlanc;
	}
	
	/**
	 * ブロックの終了記号文字列を設定します
	 *
	 * @param end 終了文字列
	 */
	public void setEndString(String end){
		if (end == null || end.length() == 0) {
			this.end = "";
			return;
		}
		if (end.charAt(0) == '^') {
			// 先頭しか有効でないもの　例)　=end -> "^=end "
			this.endNeedHead = true;
			end = end.substring(1);
		}
		if (end.charAt(end.length()-1) == ' ') {
			// 直後が空白でなければならないもの　例）=end -> "^=end "
			this.endNeedBlanc = true;
			end = end.substring(0, end.length()-1);
		}
		this.end = end;
	}
	
	/**
	 * ブロックの終了記号文字列を取得します
	 *
	 * @return 終了文字列
	 */
	public String getEndString(){
		if (this.longBracket) {
			// 可変部を置換して返す
			return this.end.replace(LONGBRACKET_MAGIC_WORD, this.longBracketKey);
		}
		return this.end;
	}
	
	/**
	 * 終了記号が行頭のみ有効フラグを取得します
	 *
	 * @return 該当ならtrue
	 */
	public boolean getEndNeedHead() {
		return this.endNeedHead;
	}
	
	/**
	 * 終了記号直後が行末か空白のみ有効フラグを取得します
	 *
	 * @return 該当ならtrue
	 */
	public boolean getEndNeedBlanc() {
		return this.endNeedBlanc;
	}
	
	/**
	 * 可変記号であるかチェックします
	 *
	 * @param なし
	 */
	public boolean checkLongBracket(){
		return this.longBracket;
	}
	
	/**
	 * 可変記号を設定します
	 *
	 * @param なし
	 */
	public void setLongBracketKey(String key){
		this.longBracketKey = key;
	}
	
	/**
	 * ブロック開始記号の可変部設定後の開始文字列を取得します
	 *
	 * @return 開始文字列
	 */
	public String getStartRealString(){
		if (this.longBracket) {
			StringBuilder sb = new StringBuilder();
			int pos;
			pos = this.start.indexOf(LONGBRACKET_MAGIC_WORD);
			if (pos > 0) {
				// prefix部
				sb.append(this.start.substring(0, pos));
			}
			if (this.longBracketKey.length() > 0) {
				sb.append(this.longBracketKey);
			}
			pos += LONGBRACKET_MAGIC_WORD.length();
			if (pos < this.start.length()) {
				// postfix部
				sb.append(this.start.substring(pos));
			}
			return sb.toString();
		}
		return this.start;
	}
	
	/**
	 * 長括弧開始記号の可変部取得用の正規表現を返す
	 *
	 * @return 開始文字列
	 */
	public String getStartRegxString(){
		if (this.longBracket) {
			// prefix(=*)postfix		
			int pos = this.start.indexOf(LONGBRACKET_MAGIC_WORD);
			String preStr = this.start.substring(0, pos);
			String postStr = "";
			String chStr = this.start.substring(pos-1, pos);
			pos += LONGBRACKET_MAGIC_WORD.length();
			if (pos < this.start.length()) {
				postStr = this.start.substring(pos);
			}
			StringBuilder sb = new StringBuilder(Pattern.quote(preStr));
			sb.append("(");
			sb.append(Pattern.quote(chStr));
			sb.append("*)");
			sb.append(Pattern.quote(postStr));
			return sb.toString();
		}
		return this.start;
	}
	
	/**
	 * 長括弧の開始記号か確認し可変部を設定して終端を返す
	 *
	 * @line　 処理対象文字列
	 * @return 開始記号があった場合の終端インデックス、なければ-1
	 */
	public int validateLongBracketStart(String line) {
		int pos;
		String patternStr = getStartRegxString();
		//System.out.println("[DEBUG] LongBracket pattern = " + patternStr);
		//System.out.println("[DEBUG] line = " + line);
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			String key = matcher.group(1);
			setLongBracketKey(key);
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
