package keisuke.count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * keisuke:追加クラス
 * 文字列リテラルブロックの開始・終了記号と終了エスケープを扱うクラス
 * ヒアドキュメント形式も扱う
 */
public class LiteralString extends AbstractBlock {

	// 引用符リテラル用の変数
	private String escapeMark = "";
	// Ruby %記法の用の変数
	public static final String PERCENT_MAGIC_WORD = "@%@";
	private boolean percentNotation = false;
	private String percentStart = "";
	private String percentEnd = "";

	/**
	 * 引数なしのコンストラクタ
	 */
	public LiteralString() {
		super();
	}

	/**
	 * 開始記号と終了記号を指定したコンストラクタ
	 * @param start 開始記号文字列
	 * @param end   終了記号文字列
	 */
	public LiteralString(final String start, final String end) {
		super();
		this.setStartString(start); // Override
		this.setEndString(end);
	}

	/**
	 * 開始記号と終了記号、エスケープ表記を指定してLiteralStringを生成します。
	 * @param start 開始記号文字列
	 * @param end   終了記号文字列
	 * @param escape 文字列中に終了文字を含めるエスケープ表記
	 */
	public LiteralString(final String start, final String end, final String escape) {
		super();
		this.setStartString(start); // Override
		this.setEndString(end);
		this.setEscapeString(escape);
	}

	/**
	 * 引用符リテラルのエスケープ文字列を設定します
	 * @param escape エスケープ文字列
	 */
	public void setEscapeString(final String escape) {
		if (escape == null) {
			this.escapeMark = "";
		} else {
			this.escapeMark = escape;
		}
	}

	/**
	 * 引用符リテラルのエスケープ文字列を取得します
	 * @return エスケープ文字列
	 */
	public String getEscapeString() {
		return this.escapeMark;
	}

	/**
	 * リテラルの開始記号を設定します
	 * @param start 開始記号文字列
	 */
	@Override
	public void setStartString(final String start) {
		if (start == null) {
			super.setStartString("");
			return;
		}
		int pos = start.indexOf(PERCENT_MAGIC_WORD);
		if (pos > 0) {
			// １文字以上の文字列の後ろに"@%@"があれば%記法と判定
			this.percentNotation = true;
		}
		super.setStartString(start);
	}

	/**
	 * リテラルの開始記号を取得します
	 * @param なし
	 */
	@Override
	public String getStartString() {
		int pos = -1;
		if (this.percentNotation) {
			pos = this.startRawString().indexOf(PERCENT_MAGIC_WORD);
		}
		if (pos > 0) {
			// 可変部は事前に不明なのでprefixで判定するためにそれだけ返す
			return this.startRawString().substring(0, pos);
		}
		return super.getStartString();
	}

	/**
	 * リテラルの終了記号を取得します
	 * @param なし
	 */
	@Override
	public String getEndString() {
		if (this.percentNotation) {
			return this.percentEnd;
		}
		return super.getEndString();
	}

	/**
	 *  Rubyの％記法リテラル形式
	 *    "@!@"の部分から区切り文字を抽出するPattern用正規表現文字列
	 *  LuaのN段長括弧[=[ ]=]形式
	 *    "@+@"の部分を抽出するPattern用正規表現文字列
	 *  を返す
	 *  @return 正規表現文字列
	 */
	@Override
	public String getStartRegxString() {
		int pos;
		String preStr = "";
		StringBuilder sb = new StringBuilder();
		if (this.percentNotation) {
			// prefix[a-zA-Z]?([^0-9a-zA-Z])
			pos = this.startRawString().indexOf(PERCENT_MAGIC_WORD);
			preStr = this.startRawString().substring(0, pos);
			sb.append(Pattern.quote(preStr));
			sb.append("[a-zA-Z]?([^0-9a-zA-Z])");
			return sb.toString();
		} else {
			// 引用符かN段長括弧
			return super.getStartRegxString();
		}
	}

	/**
	 *  Rubyの％記法リテラル形式
	 *  区切り文字までを抽出するPattern用正規表現文字列を返す
	 *  @return 正規表現文字列
	 */
	public String getEndRegxString() {
		StringBuilder sb = new StringBuilder();
		if (this.percentNotation) {
			if (this.percentStart.equals(this.percentEnd)) {
				// [^!]*!
				if (this.percentEnd.equals("\n")) {
					return ".*";
				}
				sb.append("[^");
				sb.append(this.percentEnd);
				sb.append("]*");
				sb.append(this.percentEnd);
				return sb.toString();
			} else {
				// [^\{\}]*(\{[^\{\}]*\}[\^{\}]*)*\}
				sb.append("[^\\");
				sb.append(this.percentStart);
				sb.append("\\");
				sb.append(this.percentEnd);
				sb.append("]*");
				String notDelim = sb.toString();
				sb.append("(\\");
				sb.append(this.percentStart);
				sb.append(notDelim);
				sb.append("\\");
				sb.append(this.percentEnd);
				sb.append(notDelim);
				sb.append(")*");
				sb.append("\\");
				sb.append(this.percentEnd);
				return sb.toString();
			}
		}
		return this.endRawString();
	}

	/**
	 * リテラルが％記法であるかをチェックします
	 * @return ％記法であればtrue
	 */
	public boolean checkPercentNotation() {
		return this.percentNotation;
	}

	/**
	 * ％記法の区切り文字を設定します
	 * @param delim 開始区切り文字のString、終了区切りは自動判定して設定
	 */
	public void setPercentStart(final String delim) {
		if (delim == null || delim.length() == 0) {
			return;
		}
		this.percentStart = delim;
		if (delim.equals("(")) {
			this.percentEnd = ")";
		} else if (delim.equals("[")) {
			this.percentEnd = "]";
		} else if (delim.equals("{")) {
			this.percentEnd = "}";
		} else if (delim.equals("<")) {
			this.percentEnd = ">";
		} else {
			this.percentEnd = delim;
		}
	}

	/**
	 * 保持する開始記号が文字列中にあるか探索する
	 * @param line 処理対象文字列
	 * @return 開始記号があった場合の開始インデックス、なければ-1
	 */
	public int searchStartingMark(final String line) {
		int pos = -1;
		String start = this.getStartString();
		pos = line.indexOf(start);
		return pos;
	}

	/**
	 * 保持する終了記号が文字列中にあるか探索する
	 * @param line 処理対象文字列
	 * @return 開始記号があった場合の開始インデックス、なければ-1
	 */
	public int searchEndingMark(final String line) {
		if (this.checkPercentNotation()) {
			// %記法リテラル
			String patternStr = this.getEndRegxString();
			//System.out.println("[DEBUG] LiteralString pattern = " + patternStr);
			//System.out.println("[DEBUG] line = " + line);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				// リテラル終了
				int pos = matcher.end();
				return pos;
			}
			// リテラル終了していない
			return -1;
		} else {
			// 引用符リテラル or N段長括弧リテラル
			String end = this.getEndString();
			String escapeStr = this.getEscapeString();
			int pos1 = 0;
			int pos2 = line.indexOf(end);
			if (escapeStr.length() > 0) {
				while (pos2 >= 0) {
					int pos3 = line.indexOf(escapeStr, pos1);
					if (pos3 < 0) { // エスケープなし
						break;
					} else if (pos2 < pos3) { // エスケープより先に終了記号
						break;
					} else { // エスケープが文字列内にあるが再確認
						char escChar = escapeStr.charAt(0);
						if (escChar == '\\' || escChar == '$') {
							// 直前の文字がエスケープとして機能するか確認
							int i = pos3;
							while (i >= pos1 && line.charAt(i) == escChar) {
								i--;
							}
							if ((pos3 - i) % 2 == 0) { // \が偶数で機能しない
								break;
							}
						}
					}
					// エスケープが文字列内
					pos1 = pos3 + escapeStr.length();
					pos2 = line.indexOf(end, pos1);
				}
			}
			if (pos2 >= 0) {
				// 文字列終了
				pos2 += end.length();
				return pos2;
			}
			// 文字列終了していないので、全てリテラル
			return -1;
		}
	}
}
