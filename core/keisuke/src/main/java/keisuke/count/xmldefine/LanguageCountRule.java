package keisuke.count.xmldefine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import keisuke.IfXmlParseSubject;

/**
 * 言語の解析ルール定義をXMLから取り込むクラス
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

	/**
	 * デフォルトコンストラクター
	 */
	protected LanguageCountRule() { }

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return SCXmlCommonDefine.XML_NODE_RULE;
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(SCXmlCommonDefine.XML_NODE_SKIP, SCXmlCommonDefine.XML_NODE_LINECOM,
				SCXmlCommonDefine.XML_NODE_BLOCKCOM, SCXmlCommonDefine.XML_NODE_COMEXPR,
				SCXmlCommonDefine.XML_NODE_LITERAL, SCXmlCommonDefine.XML_NODE_HEREDOC,
				SCXmlCommonDefine.XML_NODE_SCRIPT);
	}

	/**
	 * 解析対象外フラグをtrueに設定する
	 */
	protected void setUnsupportedTrue() {
		this.unsupported = true;
	}

	/**
	 * 汎用ステップカウントクラスではなく、言語専用のステップカウントクラスを
	 * 使うフラクをtrueに設定する
	 */
	protected void setSpecializedTrue() {
		this.specialized = true;
	}

	/**
	 * 関数型言語であるフラグをtrueに設定する
	 */
	protected void setFunctionalTrue() {
		this.functional = true;
	}

	/**
	 * ラベル定義終了形式のリテラル(HereDoc)を記述可能な言語であるフラグをtrueに設定する
	 */
	protected void setLabelHereDocTrue() {
		this.labelHereDoc = true;
	}

	/**
	 * 大文字小文字の区別のない言語であるフラグをtrueに設定する
	 */
	protected void setCaseInsenseTrue() {
		this.caseInsense = true;
	}

	/**
	 * インデントがブロックを定義する言語であるフラグをtrueに設定する
	 */
	protected void setIndentSenseTrue() {
		this.indentSense = true;
	}

	/**
	 * Scriptlet形式の言語であるフラグをtrueに設定する
	 */
	protected void setScriptletTrue() {
		this.scriptlet = true;
	}

	/**
	 * 解析ルール定義を流用する他の言語の名称を設定する
	 * @param langname 言語名
	 */
	protected void setSameAs(final String langname) {
		this.sameAs = langname;
	}

	/**
	 * 解析対象外フラグを返す
	 * @return 解析対象外ならtrue
	 */
	public boolean getUnsupported() {
		return this.unsupported;
	}

	/**
	 * 言語専用のステップカウントクラスを使うかを返す
	 * @return 使うならtrue
	 */
	public boolean getSpecialized() {
		return this.specialized;
	}

	/**
	 * 関数型言語であるフラグを返す
	 * @return 関数型ならtrue
	 */
	public boolean getFunctional() {
		return this.functional;
	}

	/**
	 * ラベル定義終了形式のリテラル(HereDoc)を記述可能な言語であるフラグを返す
	 * @return 可能ならtrue
	 */
	public boolean getLabelHereDoc() {
		return this.labelHereDoc;
	}

	/**
	 *  大文字小文字の区別のない言語であるフラグを返す
	 * @return 区別なければtrue
	 */
	public boolean getCaseInsense() {
		return this.caseInsense;
	}

	/**
	 * インデントがブロックを定義する言語であるフラグを返す
	 * @return インデントがブロック定義するならtrue
	 */
	public boolean getIndentSense() {
		return this.indentSense;
	}

	/**
	 * Scriptlet形式の言語であるフラグを返す
	 * @return Scriptlet形式ならtrue
	 */
	public boolean getScriptlet() {
		return this.scriptlet;
	}

	/**
	 * 解析ルール定義を流用する他の言語の名称を返す
	 * 流用しない場合はnullを返す
	 * @return 言語名
	 */
	public String getSameAs() {
		if (this.sameAs != null && !this.sameAs.isEmpty()) {
			return this.sameAs;
		} else {
			return null;
		}
	}

	/**
	 * 解析をスキップする行の正規表現パターンを追加する
	 * @param patternString 正規表現パターン文字列
	 */
	protected void addSkipPattern(final String patternString) {
		if (this.skipPatterns == null) {
			this.skipPatterns = new ArrayList<String>();
		}
		this.skipPatterns.add(patternString);
		//System.out.println("[DEBUG] rule addSkipPattern=" + patternString);
	}

	/**
	 *  解析をスキップする行の正規表現パターンを格納するリストを返す
	 * @return 正規表現パターン文字列のリスト
	 */
	public List<String> getSkipPatterns() {
		return this.skipPatterns;
	}

	/**
	 * 行コメントの定義情報を追加する
	 * @param lineComment 行コメント定義情報インスタンス
	 */
	protected void addLineComment(final LineCommentDefine lineComment) {
		if (this.lineComments == null) {
			this.lineComments = new ArrayList<LineCommentDefine>();
		}
		this.lineComments.add(lineComment);
		//System.out.println("[DEBUG] rule addLineComment=" + lineComment);
	}

	/**
	 * 行コメントの定義情報を格納するリストを返す
	 * @return 行コメント定義情報のリスト
	 */
	public List<LineCommentDefine> getLineComments() {
		return this.lineComments;
	}

	/**
	 * ブロックコメントの定義情報を追加する
	 * @param blockComment ブロックコメント定義情報インスタンス
	 */
	protected void addBlockComment(final BlockCommentDefine blockComment) {
		if (this.blockComments == null) {
			this.blockComments = new ArrayList<BlockCommentDefine>();
		}
		this.blockComments.add(blockComment);
		//System.out.println("[DEBUG] rule addBlockComment=" + blockComment);
	}

	/**
	 * ブロックコメントの定義情報を格納するリストを返す
	 * @return ブロックコメント定義情報のリスト
	 */
	public List<BlockCommentDefine> getBlockComments() {
		return this.blockComments;
	}

	/**
	 * コメント式の定義情報を追加する
	 * @param commentExpr コメント式定義情報インスタンス
	 */
	protected void addCommentExpression(final CommentExpressionDefine commentExpr) {
		if (this.commentExpressions == null) {
			this.commentExpressions = new ArrayList<CommentExpressionDefine>();
		}
		this.commentExpressions.add(commentExpr);
		//System.out.println("[DEBUG] rule addCommentExpression=" + commentExpr);
	}

	/**
	 * コメント式の定義情報を格納するリストを返す
	 * @return コメント式定義情報のリスト
	 */
	public List<CommentExpressionDefine> getCommentExpressions() {
		return this.commentExpressions;
	}

	/**
	 * リテラル文字列定義情報を追加する
	 * @param literalString リテラル文字列定義情報インスタンス
	 */
	protected void addLiteralString(final LiteralStringDefine literalString) {
		if (this.literalStrings == null) {
			this.literalStrings = new ArrayList<LiteralStringDefine>();
		}
		this.literalStrings.add(literalString);
		//System.out.println("[DEBUG] rule addLiteralString=" + literalString);
	}

	/**
	 * リテラル文字列定義情報を格納するリストを返す
	 * @return リテラル文字列定義情報のリスト
	 */
	public List<LiteralStringDefine> getLiteralStrings() {
		return this.literalStrings;
	}

	/**
	 * ラベル終了ヒアドック定義情報を追加する
	 * @param hereDoc ラベル終了ヒアドック定義情報インスタンス
	 */
	protected void addLabelHereDoc(final LabelHereDocDefine hereDoc) {
		if (this.labelHereDocs == null) {
			this.labelHereDocs = new ArrayList<LabelHereDocDefine>();
		}
		this.labelHereDocs.add(hereDoc);
		//System.out.println("[DEBUG] rule addLabelHereDoc=" + hereDoc);
	}

	/**
	 * ラベル終了ヒアドック定義情報を格納するリストを返す
	 * @return ラベル終了ヒアドック定義情報のリスト
	 */
	public List<LabelHereDocDefine> getLabelHereDocs() {
		return this.labelHereDocs;
	}

	/**
	 * スクリプトレット定義情報を追加する
	 * @param scriptletBlock スクリプトレット定義情報インスタンス
	 */
	protected void addScriptBlock(final ScriptBlockDefine scriptletBlock) {
		if (this.scriptBlocks == null) {
			this.scriptBlocks = new ArrayList<ScriptBlockDefine>();
		}
		this.scriptBlocks.add(scriptletBlock);
		//System.out.println("[DEBUG] rule addSriptBlock=" + scriptletBlock);
	}

	/**
	 * スクリプトレット定義情報を格納するリストを返す
	 * @return スクリプトレット定義情報のリスト
	 */
	public List<ScriptBlockDefine> getScriptBlocks() {
		return this.scriptBlocks;
	}

	/**
	 * Debug用
	 * @return 定義情報を表示するための文字列
	 */
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
