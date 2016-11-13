package keisuke.count;

/**
 * 複数行ブロックコメントの開始・終了記号を扱うクラス
 * keisuke:ネスト指定、先頭条件などの機能追加
 */
public class AreaComment extends AbstractBlock {
	// nestを設定するためのAPI定数
	public static boolean ALLOW_NEST = true;
	// ブロックコメントのネスト可否および実行時のネスト数保持
	protected int nest;

	/**
	 * 開始文字列と終了文字列を指定してAreaCommentを生成します。
	 *
	 * @param start 開始文字列
	 * @param end   終了文字列
	 */
	public AreaComment(String start,String end){
		super(start, end);
		this.nest = -1;
	}
	
	/**
	 * 開始文字列と終了文字列とネストを指定してAreaCommentを生成します。
	 *
	 * @param start 開始文字列
	 * @param end   終了文字列
	 * @param nest	ネスト指定
	 */
	public AreaComment(String start,String end, boolean nest){
		setStartString(start);
		setEndString(end);
		this.nest = (nest == ALLOW_NEST ? 0 : -1);
	}
	
	/**
	 * コメントのネストを設定します
	 *
	 * @param なし
	 */
	public void setEnableNest(){
		this.nest = 0;
	}

	/**
	 * コメントのネスト可否をチェックします
	 *
	 * @param なし
	 */
	public boolean checkEnableNest(){
		return this.nest >= 0 ? true: false;
	}
	
	/**
	 * コメントのネストを１つ加算します
	 *
	 * @param なし
	 */
	public void addNest(){
		if (this.nest >= 0) {
			this.nest += 1;
		}
	}
	
	/**
	 * コメントのネストを１つ減算します
	 *
	 * @param なし
	 */
	public void subtractNest(){
		if (this.nest > 0) {
			this.nest -= 1;
		}
	}
	
	/**
	 * コメントのネスト終了をチェックします
	 *
	 * @param なし
	 */
	public boolean checkNestQuit(){
		return this.nest <= 0 ? true: false;
	}
	
	/**
	 * 保持する開始記号が文字列中にあるか探索する
	 *
	 * @line　 処理対象文字列
	 * @head   lineが行頭から始まる文字列か示すフラグ
	 * @insense 文字列比較時の大文字小文字無視フラグ
	 * @return 開始記号があった場合の開始インデックス、なければ-1
	 */
	public int searchStart(String line, boolean head, boolean insense) {
		int pos = -1;
		String start = getStartString();
		if (startNeedHead && head == false) {
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
		// 開始記号が見つかったが、条件を満足するか確認
		if (pos > 0 && this.startNeedHead) { // 先頭であることが必要な場合
			// コメント条件に合致しない
			return -1;
		}
		String remainLine = "";
		if (pos >= 0 && this.startNeedBlanc) { // 直後が空白であることが必要な場合
			if ( !(pos + start.length() == line.length()
					|| line.charAt(pos + start.length()) == ' '
					|| line.charAt(pos + start.length()) == '\t')) {
				// コメント条件に合致しない
				remainLine = line.substring(pos+1);
				pos = -1;
			}
		}
		if (pos >= 0) {
			return pos;
		}
		return searchStart(remainLine, false, insense);
	}
	
	/**
	 * 保持する終了記号が文字列中にあるか探索する
	 *
	 * @line　 処理対象文字列
	 * @head   lineが行頭から始まる文字列か示すフラグ
	 * @insense 文字列比較時の大文字小文字無視フラグ
	 * @return 終了記号があった場合の終端インデックス、なければ-1
	 */
	public int searchEnd(String line, boolean head, boolean insense){
		int pos;
		String end = getEndString();
		// 終了記号を探す
		if (insense) {
			pos = line.toUpperCase().indexOf(end.toUpperCase());
		} else {
			pos = line.indexOf(end);
		}
		// ネスト可ならば終了記号の有無によらずネストの開始も探す
		if (checkEnableNest()) {
			// 入れ子のコメント開始を確認
			String start = getStartString();
			int pos1 = searchStart(line, head, insense);
			while (pos1 >= 0 && (pos < 0 || pos1 < pos)) {
				// 終了記号（なければ行末)より左に開始記号がある
				// 入れ子のコメント開始を発見
				addNest();
				pos1 = searchStart(line.substring(pos1+start.length()), false, insense);
			}
		}
		if (pos < 0) {
			// 終了記号がなかった
			return -1;
		} else if (this.endNeedHead && (head == false || pos > 0)) {
			// 先頭であることが必要な場合、コメント条件に合致しない
			return -1;
		}
		String remainLine = "";
		if (pos >= 0 && this.endNeedBlanc) { // 直後が空白であることが必要な場合
			if ( !(pos + end.length() == line.length()
					|| line.charAt(pos + end.length()) == ' '
					|| line.charAt(pos + end.length()) == '\t')) {
				// コメント条件に合致しない
				remainLine = line.substring(pos+1);
				return searchEnd(remainLine, false, insense);
			}
		}
		if (pos >= 0) {
			// コメント終了記号あり 
			subtractNest();
			 // 終了記号は終端のインデックスを返す
			pos += end.length();
			if (checkNestQuit()) {
				// コメント終了した
				return pos;
			} else {
				// ネストしたコメントは終了してないので、右側も複数行コメント領域として解析処理
				if ( pos < line.length() ) {
					remainLine = line.substring(pos);
				} else {
					// この行にはもう次の終了記号はない
					return -1;
				}
			}
		}
		// ネスト内部のコメントの終了記号しか見つかってないので更に探す
		return searchEnd(remainLine, false, insense);
	}
}
