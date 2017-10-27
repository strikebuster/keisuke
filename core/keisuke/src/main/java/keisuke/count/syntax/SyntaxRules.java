package keisuke.count.syntax;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Program言語のコメント記号等の解析ルール一式の情報
 */
public interface SyntaxRules {

	/**
	 * プログラミング言語を返す
	 * @return プログラミング言語名
	 */
	String languageName();

	/**
	 * スキップするパターン（正規表現）のリストを返す
	 * @return パターンのリスト
	 */
	List<Pattern> skipPatterns();

	/**
	 * 単一行コメントの定義のリストを返す
	 * @return 単一行コメント定義のリスト
	 */
	List<LineComment> lineComments();

	/**
	 * 複数行コメントの定義のリストを返す
	 * @return 複数行コメント定義のリスト
	 */
	List<AreaComment> areaComments();

	/**
	 * リテラル文字列の定義のリストを返す
	 * @return リテラル文字列定義のリスト
	 */
	List<LiteralString> literalStrings();

	/**
	 * 大文字小文字の区別のない言語かを示す
	 * @return 大文字小文字無視する場合はtrue
	 */
	boolean isCaseInsense();

	/**
	 * インデントブロック文法を使う言語かを示す
	 * @return 使う場合はtrue
	 */
	boolean isUsingIndentBlock();

}
