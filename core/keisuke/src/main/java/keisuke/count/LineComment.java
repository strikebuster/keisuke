package keisuke.count;

/**
 * keisuke:追加クラス
 * 単一行コメント用のクラス
 * 行の先頭のみ有効、開始文字列が有効なトークンの一部でない（直後に空白必要）などに対応
 */
public class LineComment {
	
	// 行コメント開始記号
	private String start;
	// 開始記号が行頭のみ有効か示すフラグ
	private boolean startNeedHead = false;
	// 行区切り記号がありその直後は行頭に含む場合の行区切り記号
	private String delimiter = null;
	// 開始記号の直前直後は空白文字か行の区切りが必要条件か示すフラグ
	private boolean startNeedBlanc = false;
	// 開始記号を含む他用途の記号がある(NG条件)場合に、開始記号の前にある文字列
	private String escape = null;
	
	/**
	 * 引数なしのコンストラクタ
	 */
	public LineComment() {}
	
	/**
	 * 開始文字列を指定するコンストラクタ
	 *
	 * @param start 開始文字列
	 */
	public LineComment(String start) {
		setStartString(start);
	}
	
	/**
	 * 開始文字列とエスケープ文字列を指定するコンストラクタ
	 *
	 * @param start 開始文字列
	 * @param escape エスケープ文字列
	 */
	public LineComment(String start, String escape) {
		setStartString(start);
		setEscapeString(escape);
	}
	

	/**
	 * コメントの開始記号文字列を設定します
	 *
	 * @param start 開始文字列
	 */
	public void setStartString(String start) {
		if (start == null) {
			this.start = "";
			return;
		}
		if (start.charAt(0) == '^') {
			// 先頭しか有効でないもの　例) Fortran　c -> "^c "
			this.startNeedHead = true;
			int pos = start.indexOf('^',1);
			if (pos > 0 && pos < start.length()-1) {
				// 先頭条件表記に合致する
				this.delimiter = start.substring(1,pos);
				start = start.substring(pos+1);
				//System.out.println("[DEBUG] delim=["+this.delimiter+"], start=["+start+"]");
			} else {
				start = start.substring(1);
			}
		}
		if (start.charAt(start.length()-1) == ' ') {
			// 直後が空白でなければならないもの　例）rem -> "rem "
			this.startNeedBlanc = true;
			start = start.substring(0, start.length()-1);
		}
		this.start = start;
	}
	
	/**
	 * コメントの開始記号エスケープ文字列を設定します
	 *
	 * @param escape 開始文字列
	 */
	public void setEscapeString(String escape) {
		if (escape == null || escape.length() == 0) {
			this.escape = "";
			return;
		}
		this.escape = escape;
		//System.out.println("[DEBUG] escape=["+this.escape+"]");
	}
	
	/**
	 * コメントの開始記号文字列を取得します
	 *
	 * @return 開始文字列
	 */
	public String getStartString() {
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
	 * コメントの開始記号エスケープ文字列から開始記号を除くPrefixを取得します
	 *
	 * @return エスケープPrefix文字列
	 */
	public String getEscapePrefix() {
		if (this.escape == null || this.escape.length() == 0) {
			return null;
		}
		int startlen = this.start.length();
		int escapelen = this.escape.length();
		if (startlen >= escapelen) {
			return null;
		}
		return this.escape.substring(0, escapelen - startlen);
	}
	
	/**
	 * 保持する開始記号が文字列中にあるか探索する
	 *
	 * @line　 処理対象文字列
	 * @head   lineが行頭から始まる文字列か示すフラグ
	 * @insense 文字列比較時の大文字小文字無視フラグ
	 * @return 開始記号があった場合のインデックス、なければ-1
	 */
	public int searchStart(String line, boolean head, boolean insense) {
		int pos = -1;
		boolean dlmhead = false;
		String start = getStartString();
		
		if (this.startNeedHead && this.delimiter == null && head == false) {
			// 先頭であることが必要な場合、コメント条件に合致しない
			return -1;
		}
		if (insense) {
			pos = line.toUpperCase().indexOf(start.toUpperCase());
		} else {
			pos = line.indexOf(start);
		}
		if (pos < 0) {
			// 開始記号が見つからない
			return -1;
		}
		
		String remainLine = line.substring(pos+1);;
		// 開始記号が見つかったが、条件を満足するか確認
		if (pos == 0 && head) { // 物理的な行頭であればneedHeadとneedBlancの直前はクリア
			dlmhead = true;
		} else if (pos > 0 && this.startNeedHead) { // 先頭必要だが、行の途中にある場合
			if (this.delimiter == null) { // 行区切り記号の確認
				// コメント条件に合致しない
				return -1;
			}
			//直前が区切り記号かチェック
			int dlmlen = this.delimiter.length();
			if (pos < dlmlen) { // 区切り記号が左にある可能性なし
				pos = -1;
			} else { // 区切り記号が左にある可能性あり
				int i = pos;
				while (i > dlmlen) {
					i--;
					char c = line.charAt(i);
					if (c == ' ' || c == '\t') {
						continue;
					} else {
						i++;
						break;
					}
				}
				String strLeft = line.substring(i-dlmlen, i);
				if (!strLeft.equals(this.delimiter)) {
					// 区切り文字列に合致しなかったのでコメント記号は無視
					//System.out.println("[DEBUG] Pass ["+strLeft+start+"] in ["+line+"]");
					pos = -1;
				}
				// 直前が行区切り記号であった
				dlmhead = true;
			}
		}
		
		String prefix = getEscapePrefix();
		if (pos > 0 && prefix != null) { // エスケープ条件がある場合
			int prefixlen = prefix.length();
			if (pos >= prefixlen) { // エスケープPrefixが左にある可能性あり
				String strLeft = line.substring(pos-prefixlen, pos);
				if (strLeft.equals(prefix)) {
					// エスケープ文字列に合致したのでコメント記号は無視
					//System.out.println("[DEBUG] Deny["+escape"] in ["+line+"]");
					pos = -1;
				}
			}
		}
		if (pos >= 0 && this.startNeedBlanc) { // 直前直後が空白であることが必要な場合
			// 直前が行頭か、空白文字かをチェック
			if ( !(dlmhead 
					|| line.charAt(pos - 1) == ' '
					|| line.charAt(pos - 1) == '\t')) {
				// そうでないので条件に合致しない
				pos = -1;
				
			// 直後が空白文字または、行末かをチェック
			} else if ( !(pos + start.length() == line.length()
					|| line.charAt(pos + start.length()) == ' '
					|| line.charAt(pos + start.length()) == '\t')) {
				// そうでないので条件に合致しない
				pos = -1;
			}
		}
		if (pos >= 0) {
			return pos;
		}
		return searchStart(remainLine, false, insense);
	}
}
