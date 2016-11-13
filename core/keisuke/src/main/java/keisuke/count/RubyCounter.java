package keisuke.count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * keisuke:追加クラス
 * Ruby用ステップカウンタ
 */
public class RubyCounter extends LabelHereDocScriptCounter {

	/**
	 * コンストラクター
	 *
	 */
	public RubyCounter() {
		super();
		addLineComment(new LineComment("#", "?#"));
		addAreaComment(new AreaComment("^=begin ","^=end "));
		addLiteralString(new LiteralString("\"", "\"", "\\\""));
		addLiteralString(new LiteralString("'", "'", "\\'"));
		addLiteralString(new LiteralString("/", "/", "\\/"));
		addLiteralString(new LiteralString("%@%@", "@%@")); // @%@は特別な意味あり
		addLiteralString(new LabelHereDoc("<<@?@", "@?@")); // @?@は特別な意味あり
		addLiteralString(new LabelHereDoc("<<-@?@", "@?@"));;
		addLiteralString(new LabelHereDoc("<<~@?@", "@?@"));
		setFileType("Ruby");
	}
	
	/** "<<"がヒアドキュメントではないケースのチェックメソッド */
	@Override
	protected boolean checkExcludeLiteralStringStart(String line, LiteralString literal) {
		String start = literal.getStartString();
		if (start.equals("<<")) {
			int pos = line.indexOf(start);
			if (pos < 0) {
				// ありえないパスだが
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			String regxStr = "class[ \\t]+<<";
			Pattern pattern = Pattern.compile(regxStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find() && matcher.start() < pos) {
				// ヒアドキュメントでない "<<"
				//System.out.println("[DEBUG] Exclude << Case : " + line);
				return true;
			}
		}
		return super.checkExcludeLiteralStringStart(line, literal);
	}
	

	/** リテラル文字列の開始から行末までの処理をする */
	@Override
	public String dealLiteralStringStart(ProgramLangRule pr, String line, LiteralString literal) {
		StringBuilder sb = new StringBuilder();
		if (literal.checkPercentNotation()) {
			// %記法リテラル処理
			// 区切り文字を抽出
			String patternStr = literal.getStartRegxString();
			//System.out.println("[DEBUG] %Notation pattern = " + patternStr);
			//System.out.println("[DEBUG] line = " + line);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String delim = matcher.group(1);
				literal.setPercentStart(delim);
				//System.out.println("[DEBUG] %Delimiter = " + delim);
				// 開始記号の処理
				int pos = matcher.end();
				sb.append(line.substring(0, pos));
				// 開始記号より右の処理
				sb.append(searchLiteralStringEnd(pr, line.substring(pos)));
				return sb.toString();
			} else {
				// 区切り文字が見つからない = 区切り文字が改行文字
				literal.setPercentStart("\n");
				//System.out.println("[DEBUG] %Delimiter = \n !!");
				// 開始記号の処理＆右はなし
				return line;
			}
		} else {
			// 通常の引用符またはラベル終了リテラル
			return super.dealLiteralStringStart(pr, line, literal);
		}
	}

}
