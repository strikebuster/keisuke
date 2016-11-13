package keisuke.count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * keisuke: 追加クラス
 * 関数型言語の式のコメント化対応
 */
public class CommentExpr extends AreaComment {
	// 式の開始・終了記号の定数
	public static String EXPR_START = "(";
	public static String EXPR_END = ")";
	
	// コード中の実際の開始文字列保持用
	private String realStart = "";
	
	/**
	 * 開始文字列を指定したコンストラクタ
	 */
	public CommentExpr(String start){
		super(start, null);
		setEndString(EXPR_END);
		setEnableNest();
	}
	
	/**
	 * マッチした開始文字列を設定する
	 */
	public void setStartRealString(String rstart) {
		this.realStart = rstart;
	}
	
	/**
	 * マッチした開始文字列を取得する
	 */
	public String getStartRealString() {
		return this.realStart;
	}
	
	/**
	 * コメント式開始記号の可変部取得用の正規表現を返す
	 *
	 * @return 開始文字列
	 */
	@Override
	public String getStartRegxString(){
		String preStr = "";
		String postStr = "";
		String escapedStart = "\\" + EXPR_START;
		StringBuilder sb = new StringBuilder();
		
		// prefix[ \t]*postfix "#_ ("
		// prefix[ \t]*postfix(\W?) "( comment\W"
		int pos = this.start.indexOf(EXPR_START);
		if (pos == 0) {
			// 式の中にコメント命令のタイプ
			preStr = EXPR_START;
			sb.append(escapedStart);
			postStr = this.start.substring(pos+1);
			if (postStr.charAt(0) == ' ') {
				sb.append("[ \\t]*");
				postStr = postStr.substring(1);
			}
			sb.append(Pattern.quote(postStr));
			sb.append("(\\W?)");
		} else if (pos > 0) {
			if (start.charAt(pos-1) == ' ') {
				preStr = start.substring(0, pos-1);
				sb.append(Pattern.quote(preStr));
				sb.append("[ \\t]*");
			} else {
				preStr = start.substring(0, pos);
				sb.append(Pattern.quote(preStr));
			}
			sb.append(escapedStart);
			if (pos+1 < start.length()) {
				// 想定外のためあっても無視
				postStr = this.start.substring(pos+1);
			}
		} else {
			return this.start;
		}
		return sb.toString();
	}

	/**
	 * 保持する開始記号が文字列中にあるか探索する
	 *
	 * @line　 処理対象文字列
	 * @head   lineが行頭から始まる文字列か示すフラグ
	 * @insense 文字列比較時の大文字小文字無視フラグ
	 * @return 開始記号があった場合の開始インデックス、なければ-1
	 */
	@Override
	public int searchStart(String line, boolean head, boolean insense ) {
		int pos = -1;
		String start = getStartString();
		
		// コメント化式
		String patternStr = getStartRegxString();
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			start = matcher.group();
			if (matcher.groupCount() > 0) {
				String removeChar = matcher.group(1);
				//System.out.println("[DEBUG] removeChar = " + removeChar);
				start = start.substring(0, start.length() - removeChar.length());
			}
			setStartRealString(start);
			// 返り値のための開始記号の位置
			pos = matcher.start();
			//System.out.println("[DEBUG] commentExpr = [" + start +"] at " + pos);
		}
		return pos;
	}
	
	/**
	 * 式の終了記号がコメント式の終了か確認し終端を返す
	 *
	 * @line　 処理対象文字列
	 * @return コメント式終了した場合の終端インデックス、なければ-1
	 */
	public int searchEnd(String line) {
		String end = EXPR_END;
		int pos2 = line.indexOf(end);
		if (checkEnableNest()) {
			// 入れ子の式開始を確認
			String start = EXPR_START;
			int pos1 = line.indexOf(start);
			while (pos1 >= 0 && (pos2 < 0 || pos1 < pos2)) {
				// 終了記号（なくてもOK)より左に開始記号がある
				// 入れ子の式開始
				addNest();
				//System.out.println("[DEBUG] Add Nest at "+ pos1 + " of [" + line + "]");
				pos1 = line.indexOf(start, pos1+start.length());
			}
		}
		if (pos2 >= 0) {
			// 終了記号あり
			subtractNest();
			//System.out.println("[DEBUG] Subtract Nest at "+ pos2 + " of [" + line + "]");
			pos2 += end.length();
			if (checkNestQuit()) {
				// コメント式終了した
				//System.out.println("[DEBUG] Comment End");
				return pos2;
			} else {
				// コメント式は終了してない
				if ( pos2 < line.length() ) {
					// ")"から右についても処理
					//System.out.println("[DEBUG] Recursive after "+ pos2 +" for [" + line.substring(pos2) + "]");
					int pos3 = searchEnd(line.substring(pos2));
					if (pos3 < 0) {
						return -1;
					} else {
						//右側で終了が見つかった
						return pos2 + pos3;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * コメント式の開始記号か確認し可変部を設定して終端を返す
	 *
	 * @line　 処理対象文字列
	 * @onexpr 既に処理中のコメント式
	 * @return 開始記号があった場合の終端インデックス、なければ-1
	 */
	public int validateCommentExpressionStart(String line, CommentExpr onExpr) {
		int pos;
		String start = getStartRealString();
		if (onExpr == null) {
			// コメント化式の開始記号を保存
			addNest();
			//System.out.println("[DEBUG] New Expression Start = " + start);
		} else {
			// 既にコメント化式が開始しているため今回の式はネスト式の１つ
			onExpr.addNest();
			//System.out.println("[DEBUG] Nested Expression = " + start);
		}
		pos = line.indexOf(start);
		//System.out.println("[DEBUG] Add Nest at "+ pos + " of [" + line + "]");
		pos += start.length();
		return pos;
	}
}
