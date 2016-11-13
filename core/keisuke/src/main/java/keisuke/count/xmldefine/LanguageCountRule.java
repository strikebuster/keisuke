package keisuke.count.xmldefine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import keisuke.IfXmlParseSubject;

/**
 * keisuke:追加クラス
 * 言語の解析定義をXMLから取り込むクラス
 */
public class LanguageCountRule implements IfXmlParseSubject {

	private boolean unsupported = false;
	private boolean specialized = false;
	private boolean functional = false;
	private boolean labelHereDoc = false;
	private boolean caseInsense = false;
	private boolean indentSense = false;
	private boolean scriptlet = false;
	private String sameAs = null;
	private List<String> skipPatterns = null;
	private List<LineCommentDefine> lineComments = null;
	private List<BlockCommentDefine> blockComments = null;
	private List<CommentExpressionDefine> commentExpressions = null;
	private List<LiteralStringDefine> literalStrings = null;
	private List<LabelHereDocDefine> labelHereDocs = null;
	private List<ScriptBlockDefine> scriptBlocks = null;
	
	public String getXmlNodeName() {
		return SCXmlCommonDefine.XML_NODE_RULE;
	}
	
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(SCXmlCommonDefine.XML_NODE_SKIP, SCXmlCommonDefine.XML_NODE_LINECOM,
				SCXmlCommonDefine.XML_NODE_BLOCKCOM, SCXmlCommonDefine.XML_NODE_COMEXPR,
				SCXmlCommonDefine.XML_NODE_LITERAL, SCXmlCommonDefine.XML_NODE_HEREDOC,
				SCXmlCommonDefine.XML_NODE_SCRIPT);
	}
	
	public void setUnsupportedTrue() {
		this.unsupported = true;
	}
	
	public void setSpecializedTrue() {
		this.specialized = true;
	}
	
	public void setFunctionalTrue() {
		this.functional = true;
	}
	
	public void setLabelHereDocTrue() {
		this.labelHereDoc = true;
	}
	
	public void setCaseInsenseTrue() {
		this.caseInsense = true;
	}
	
	public void setIndentSenseTrue() {
		this.indentSense = true;
	}
	
	public void setScriptletTrue() {
		this.scriptlet = true;
	}
	
	public void setSameAs(String str) {
		this.sameAs = str;
	}
	
	public boolean getUnsupported() {
		return this.unsupported;
	}
	
	public boolean getSpecialized() {
		return this.specialized;
	}
	
	public boolean getFunctional() {
		return this.functional;
	}
	
	public boolean getLabelHereDoc() {
		return this.labelHereDoc;
	}
	
	public boolean getCaseInsense() {
		return this.caseInsense;
	}
	
	public boolean getIndentSense() {
		return this.indentSense;
	}
	
	public boolean getScriptlet() {
		return this.scriptlet;
	}
	
	public String getSameAs() {
		if (this.sameAs != null && !this.sameAs.isEmpty()) {
			return this.sameAs;
		} else {
			return null;
		}
	}
	
	/** Skip Patterns */
	public void addSkipPattern(String str) {
		if (this.skipPatterns == null) {
			this.skipPatterns = new ArrayList<String>();
		}
		this.skipPatterns.add(str);
		//System.out.println("[DEBUG] rule addSkipPattern=" + str);
	}
	
	public List<String> getSkipPatterns() {
		return this.skipPatterns;
	}
	
	/** Line Comments */
	public void addLineComment(LineCommentDefine lcd) {
		
		if (this.lineComments == null) {
			this.lineComments = new ArrayList<LineCommentDefine>();
		}
		this.lineComments.add(lcd);
		//System.out.println("[DEBUG] rule addLineComment=" + lcd);
	}
	
	public List<LineCommentDefine> getLineComments() {
		return this.lineComments;
	}
	
	/** Block Comments */
	public void addBlockComment(BlockCommentDefine bcd) {
		if (this.blockComments == null) {
			this.blockComments = new ArrayList<BlockCommentDefine>();
		}
		this.blockComments.add(bcd);
		//System.out.println("[DEBUG] rule addBlockComment=" + bcd);
	}
	
	public List<BlockCommentDefine> getBlockComments() {
		return this.blockComments;
	}
	
	/** Comment Expressions */
	public void addCommentExpression(CommentExpressionDefine ced) {
		if (this.commentExpressions == null) {
			this.commentExpressions = new ArrayList<CommentExpressionDefine>();
		}
		this.commentExpressions.add(ced);
		//System.out.println("[DEBUG] rule addCommentExpression=" + ced);
	}
	
	public List<CommentExpressionDefine> getCommentExpressions() {
		return this.commentExpressions;
	}

	/** Literal Strings */
	public void addLiteralString(LiteralStringDefine lsd) {
		if (this.literalStrings == null) {
			this.literalStrings = new ArrayList<LiteralStringDefine>();
		}
		this.literalStrings.add(lsd);
		//System.out.println("[DEBUG] rule addLiteralString=" + lsd);
	}
	
	public List<LiteralStringDefine> getLiteralStrings() {
		return this.literalStrings;
	}
	
	/** Label Here Documents */
	public void addLabelHereDoc(LabelHereDocDefine lhdd) {
		if (this.labelHereDocs == null) {
			this.labelHereDocs = new ArrayList<LabelHereDocDefine>();
		}
		this.labelHereDocs.add(lhdd);
		//System.out.println("[DEBUG] rule addLabelHereDoc=" + lhdd);
	}
	
	public List<LabelHereDocDefine> getLabelHereDocs() {
		return this.labelHereDocs;
	}
	
	/** Script Blocks */
	public void addScriptBlock(ScriptBlockDefine sbd) {
		if (this.scriptBlocks == null) {
			this.scriptBlocks = new ArrayList<ScriptBlockDefine>();
		}
		this.scriptBlocks.add(sbd);
		//System.out.println("[DEBUG] rule addSriptBlock=" + sbd);
	}
	
	public List<ScriptBlockDefine> getScriptBlocks() {
		return this.scriptBlocks;
	}
	
	/** Debug */
	public String debug() {
		if (this.unsupported) {
			return "[DEBUG] LanguageCountRule unsupported = true, no rules.\n";
		}
		if (this.specialized) {
			return "[DEBUG] LanguageCountRule specialized = true, not required to define rules.\n";
		}
		StringBuilder sb = new StringBuilder();
		if (this.functional) {
			sb.append("[DEBUG] functional = true\n");
		}
		if (this.labelHereDoc) {
			sb.append("[DEBUG] labelHereDoc = true\n");
		}
		if (this.caseInsense) {
			sb.append("[DEBUG] caseInsense = true\n");
		}
		if (this.indentSense) {
			sb.append("[DEBUG] indentSense = true\n");
		}
		if (this.sameAs != null && !this.sameAs.isEmpty()) {
			sb.append("[DEBUG] sameAs = " + this.sameAs + "\n");
		}
		sb.append("[DEBUG] LanguageCountRule contains as follows\n");
		if (this.skipPatterns != null) {
			for (String str : this.skipPatterns) {
				sb.append("[DEBUG] skipPattern = [");
				sb.append(str);
				sb.append("]\n");
			}
		}
		if (this.lineComments != null) {
			for (LineCommentDefine lcd : this.lineComments) {
				sb.append("[DEBUG] lineComment = [");
				sb.append(lcd.getStartDefineString());
				sb.append("]\n");
			}
		}
		if (this.blockComments != null) {
			for (BlockCommentDefine bcd : this.blockComments) {
				sb.append("[DEBUG] blockComment = [");
				sb.append(bcd.toString());
				sb.append("]\n");
			}
		}
		if (this.commentExpressions != null) {
			for (CommentExpressionDefine ced : this.commentExpressions) {
				sb.append("[DEBUG] commentExpression = [");
				sb.append(ced.toString());
				sb.append("]\n");
			}
		}
		if (this.literalStrings != null) {
			for (LiteralStringDefine lsd : this.literalStrings) {
				sb.append("[DEBUG] literalString = [");
				sb.append(lsd.toString());
				sb.append("]\n");
			}
		}
		if (this.labelHereDocs != null) {
			for (LabelHereDocDefine lhdd : this.labelHereDocs) {
				sb.append("[DEBUG] labelHereDoc = [");
				sb.append(lhdd.toString());
				sb.append("]\n");
			}
		}
		if (this.scriptBlocks != null) {
			for (ScriptBlockDefine sbd : this.scriptBlocks) {
				sb.append("[DEBUG] scriptBlock = [");
				sb.append(sbd.toString());
				sb.append("]\n");
			}
		}
		return sb.toString();
	}
}
