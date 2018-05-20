package keisuke.count.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.LabelHereDoc;
import keisuke.count.syntax.LineComment;
import keisuke.count.syntax.LiteralString;
import keisuke.util.LogUtil;

/**
 * Ruby用ステップカウンタ
 */
public class RubyCounter extends LabelHereDocScriptCounter {

	/**
	 * コンストラクター
	 */
	public RubyCounter() {
		super();
		this.setRules();
	}

	/**
	 * サブクラスEmbeddedRuby用のコンストラクタ
	 * @param sflag Scriptlet言語の場合にtrueを指定する
	 */
	public RubyCounter(final boolean sflag) {
		super(sflag);
		this.setRules();
	}

	/**
	 * Ruby言語解析用のルール設定
	 */
	private void setRules() {
		this.addLineComment(new LineComment("#", "?#"));
		this.addAreaComment(new AreaComment("^=begin ", "^=end "));
		this.addLiteralString(new LiteralString("\"", "\"", "\\\""));
		this.addLiteralString(new LiteralString("'", "'", "\\'"));
		this.addLiteralString(new LiteralString("/", "/", "\\/"));
		this.addLiteralString(new LiteralString("%@%@", "@%@")); // @%@は特別な意味あり
		this.addLiteralString(new LabelHereDoc("<<@?@", "@?@")); // @?@は特別な意味あり
		this.addLiteralString(new LabelHereDoc("<<-@?@", "@?@"));
		this.addLiteralString(new LabelHereDoc("<<~@?@", "@?@"));
		this.setFileType("Ruby");
	}

	/* "<<"がヒアドキュメントではないケースのチェックメソッド */
	@Override
	protected boolean checkExcludeLiteralStringStart(final String line, final LiteralString literal) {
		String start = literal.getStartString();
		if (start.equals("<<")) {
			int pos = line.indexOf(start);
			if (pos < 0) {
				// ありえないパスだが
				LogUtil.warningLog("<< is missed :" + line);
				return super.checkExcludeLiteralStringStart(line, literal);
			}
			String regxStr = "class[ \\t]+<<";
			Pattern pattern = Pattern.compile(regxStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find() && matcher.start() < pos) {
				// ヒアドキュメントでない "<<"
				//LogUtil.debugLog("Exclude << Case : " + line);
				return true;
			}
		}
		return super.checkExcludeLiteralStringStart(line, literal);
	}

	/* リテラル文字列の開始から行末までの処理をする */
	@Override
	public String handleLiteralStringStart(
			final ProgramLangRule lang, final String line, final LiteralString literal) {
		StringBuilder sb = new StringBuilder();
		if (literal.checkPercentNotation()) {
			// %記法リテラル処理
			// 区切り文字を抽出
			String patternStr = literal.getStartRegxString();
			//LogUtil.debugLog("%Notation pattern = " + patternStr);
			//LogUtil.debugLog("line = " + line);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String delim = matcher.group(1);
				literal.setPercentStart(delim);
				//LogUtil.debugLog("%Delimiter = " + delim);
				// 開始記号の処理
				int pos = matcher.end();
				sb.append(line.substring(0, pos));
				// 開始記号より右の処理
				sb.append(this.searchLiteralStringEnd(lang, line.substring(pos)));
				return sb.toString();
			} else {
				// 区切り文字が見つからない = 区切り文字が改行文字
				literal.setPercentStart("\n");
				//LogUtil.debugLog("%Delimiter = \n !!");
				// 開始記号の処理＆右はなし
				return line;
			}
		} else {
			// 通常の引用符またはラベル終了リテラル
			return super.handleLiteralStringStart(lang, line, literal);
		}
	}

}
