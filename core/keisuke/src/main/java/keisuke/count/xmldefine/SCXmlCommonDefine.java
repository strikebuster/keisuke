package keisuke.count.xmldefine;

public class SCXmlCommonDefine {
	//public final static String XML_NODE_LANG = "Language";		// CommonDefineで定義済み
	//public final static String XML_NODE_EXT = "FileExtension";	// CommonDefineで定義済み
	public final static String XML_NODE_RULE = "CountRules";
	
	public final static String XML_NODE_SKIP = "SkipPattern";
	
	public final static String XML_NODE_LINECOM = "LineComment";
	public final static String XML_NODE_START = "start";
	public final static String XML_NODE_NEEDHEAD = "needHead";
	public final static String XML_NODE_NEEDBLANC = "needBlanc";
	public final static String XML_NODE_DELIMITER = "lineDelimiter";
	public final static String XML_NODE_ESCAPE = "escape";
	
	public final static String XML_NODE_BLOCKCOM = "BlockComment";
	//public final static String XML_NODE_START = "start";
	public final static String XML_NODE_END = "end";
	public final static String XML_NODE_NEST = "nest";
	
	public final static String XML_NODE_COMEXPR = "CommentExpression";
	
	public final static String XML_NODE_LITERAL = "LiteralString";
	//public final static String XML_NODE_START = "start";
	//public final static String XML_NODE_END = "end";
	//public final static String XML_NODE_ESCAPE = "escape";
	
	public final static String XML_NODE_HEREDOC = "LabelHereDoc";
	public final static String XML_NODE_SCRIPT = "ScriptBlock";
	
	public final static String XML_ATTR_UNSUPPORT = "unsupported";
	public final static String XML_ATTR_SPECIAL = "specialized";
	public final static String XML_ATTR_FUNCTIONAL = "functional";
	public final static String XML_ATTR_HEREDOC = "labelHereDoc";
	public final static String XML_ATTR_CASE = "caseInsense";
	public final static String XML_ATTR_INDENT = "indentSense";
	public final static String XML_ATTR_SCRIPT = "scriptlet";
	public final static String XML_ATTR_SAME = "sameAs";
}
