package keisuke.count.xmldefine;

/**
 * Final class to define constants of keisuke.count.xmldefine.*
 * オプション引数やプロパティキーの文字列定義
 */
public final class SCXmlCommonDefine {

	private SCXmlCommonDefine() { }

	//public static final String XML_NODE_LANG = "Language";		// CommonDefineで定義済み
	//public static final String XML_NODE_EXT = "FileExtension";	// CommonDefineで定義済み
	public static final String XML_NODE_RULE = "CountRules";

	public static final String XML_NODE_SKIP = "SkipPattern";

	public static final String XML_NODE_LINECOM = "LineComment";
	public static final String XML_NODE_START = "start";
	public static final String XML_NODE_NEEDHEAD = "needHead";
	public static final String XML_NODE_NEEDBLANC = "needBlanc";
	public static final String XML_NODE_DELIMITER = "lineDelimiter";
	public static final String XML_NODE_ESCAPE = "escape";

	public static final String XML_NODE_BLOCKCOM = "BlockComment";
	//public static final String XML_NODE_START = "start";
	public static final String XML_NODE_END = "end";
	public static final String XML_NODE_NEST = "nest";

	public static final String XML_NODE_COMEXPR = "CommentExpression";

	public static final String XML_NODE_LITERAL = "LiteralString";
	//public static final String XML_NODE_START = "start";
	//public static final String XML_NODE_END = "end";
	//public static final String XML_NODE_ESCAPE = "escape";

	public static final String XML_NODE_HEREDOC = "LabelHereDoc";
	public static final String XML_NODE_SCRIPT = "ScriptBlock";

	public static final String XML_ATTR_UNSUPPORT = "unsupported";
	public static final String XML_ATTR_SPECIAL = "specialized";
	public static final String XML_ATTR_FUNCTIONAL = "functional";
	public static final String XML_ATTR_HEREDOC = "labelHereDoc";
	public static final String XML_ATTR_CASE = "caseInsense";
	public static final String XML_ATTR_INDENT = "indentSense";
	public static final String XML_ATTR_SCRIPT = "scriptlet";
	public static final String XML_ATTR_SAME = "sameAs";

}
