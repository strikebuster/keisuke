package keisuke.count.xmldefine;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import keisuke.XmlElementFactory;
import keisuke.XmlLanguageDefine;

public class XmlLanguageDefineWithRule extends XmlLanguageDefine {

	private LanguageCountRule currentLangRule = null;
	
	public XmlLanguageDefineWithRule(XmlElementFactory factory) {
		this.xmlElemFactory = factory;
	}
	
	@Override
	protected void dealChild(String name, Element elem) {
		if (name.equals(SCXmlCommonDefine.XML_NODE_RULE)) {
			parseLangCountRule(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_SKIP)) {
			parseRuleOfSkip(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_LINECOM)) {
			parseRuleOfLineComment(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_BLOCKCOM)) {
			parseRuleOfBlockComment(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_COMEXPR)) {
			parseRuleOfCommentExpression(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_LITERAL)) {
			parseRuleOfLiteralString(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_HEREDOC)) {
			parseRuleOfLabelHereDoc(elem);
		} else if (name.equals(SCXmlCommonDefine.XML_NODE_SCRIPT)) {
			parseRuleOfScriptBlock(elem);
		} else {
			super.dealChild(name, elem);
		}
	}
	
	protected void parseLangCountRule(Element element) {
		this.currentLangRule = new LanguageCountRule();
		LanguageElementWithRule lewr = (LanguageElementWithRule)this.currentLangElem;
		lewr.setCountRule(this.currentLangRule);
		//System.out.println("[DEBUG] parse lang=" + lewr.getName());
		String unstr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_UNSUPPORT);
		if ("true".equals(unstr)) {
			this.currentLangRule.setUnsupportedTrue();
			//System.out.println("[DEBUG] unsupported=true");
			return;
		}
		String spstr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_SPECIAL);
		if ("true".equals(spstr)) {
			this.currentLangRule.setSpecializedTrue();
			//System.out.println("[DEBUG] specialized=true");
			return;
		}
		String funcstr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_FUNCTIONAL);
		if ("true".equals(funcstr)) {
			this.currentLangRule.setFunctionalTrue();
			//System.out.println("[DEBUG] functional=true");
		}
		String herestr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_HEREDOC);
		if ("true".equals(herestr)) {
			this.currentLangRule.setLabelHereDocTrue();
			//System.out.println("[DEBUG] labelHereDoc=true");
		}
		String samestr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_SAME);
		if (samestr != null && ! samestr.isEmpty()) {
			this.currentLangRule.setSameAs(samestr);
			//System.out.println("[DEBUG] sameAs=" + samestr);
		}
		String casestr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_CASE);
		if ("true".equals(casestr)) {
			this.currentLangRule.setCaseInsenseTrue();
			//System.out.println("[DEBUG] caseInsense=true");
		}
		String indentstr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_INDENT);
		if ("true".equals(indentstr)) {
			this.currentLangRule.setIndentSenseTrue();
			//System.out.println("[DEBUG] indentSense=true");
		}
		String scriptstr = element.getAttribute(SCXmlCommonDefine.XML_ATTR_SCRIPT);
		if ("true".equals(scriptstr)) {
			this.currentLangRule.setScriptletTrue();
			//System.out.println("[DEBUG] scriptlet=true");
		}
		parseChildrenNodes(element, this.currentLangRule.getXmlChildrenNames());
	}
	
	protected void parseRuleOfSkip(Element element) {
		String skipstr = element.getTextContent();
		//System.out.println("[DEBUG] SkipPattern's Element=" + skipstr);
		this.currentLangRule.addSkipPattern(skipstr);
	}
	
	protected void parseRuleOfLineComment(Element element) {
		LineCommentDefine linecom = new LineCommentDefine();
		NodeList lcdChildren = element.getChildNodes();		
		for (int i=0; i < lcdChildren.getLength(); i++) {
			Node node = lcdChildren.item(i);
			if (! checkElementNode(node)) {
				continue;
			}
			Element child = (Element)node;
			//System.out.println("[DEBUG] LineComment's Element=" + child.getNodeName());
			String nodename = getNameWithoutNS(child.getNodeName());
			if (nodename.equals(SCXmlCommonDefine.XML_NODE_START)) {
				String str = child.getTextContent();
				linecom.setStart(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_NEEDHEAD)) {
				String str = child.getTextContent();
				if ( "true".equals(str)) {
					linecom.setNeedHeadTrue();
				}
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_NEEDBLANC)) {
				String str = child.getTextContent();
				if ( "true".equals(str)) {
					linecom.setNeedBlancTrue();
				}
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_DELIMITER)) {
				String str = child.getTextContent();
				linecom.setLineDelimiter(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_ESCAPE)) {
				String str = child.getTextContent();
				linecom.setEscape(str);
			} else {
				System.err.println("![WARN] illegal element for LineComment : " + nodename);
				continue;
			}
		}
		this.currentLangRule.addLineComment(linecom);
	}
	
	protected void parseRuleOfBlockComment(Element element) {
		BlockCommentDefine blockcom = new BlockCommentDefine();
		NodeList bcdChildren = element.getChildNodes();		
		for (int i=0; i < bcdChildren.getLength(); i++) {
			Node node = bcdChildren.item(i);
			if (! checkElementNode(node)) {
				continue;
			}
			Element child = (Element)node;
			//System.out.println("[DEBUG] BlockComment's Element=" + child.getNodeName());
			String nodename = getNameWithoutNS(child.getNodeName());
			if (nodename.equals(SCXmlCommonDefine.XML_NODE_START)) {
				String str = child.getTextContent();
				blockcom.setStart(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_END)) {
				String str = child.getTextContent();
				blockcom.setEnd(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_NEST)) {
				String str = child.getTextContent();
				if ( "true".equals(str)) {
					blockcom.setNestTrue();
				}
			} else {
				System.err.println("![WARN] illegal element for BlockComment : " + nodename);
				continue;
			}
		}
		this.currentLangRule.addBlockComment(blockcom);
	}
	
	protected void parseRuleOfCommentExpression(Element element) {
		CommentExpressionDefine expr = new CommentExpressionDefine();
		NodeList cedChildren = element.getChildNodes();		
		for (int i=0; i < cedChildren.getLength(); i++) {
			Node node = cedChildren.item(i);
			if (! checkElementNode(node)) {
				continue;
			}
			Element child = (Element)node;
			//System.out.println("[DEBUG] CommentExpression's Element=" + child.getNodeName());
			String nodename = getNameWithoutNS(child.getNodeName());
			if (nodename.equals(SCXmlCommonDefine.XML_NODE_START)) {
				String str = child.getTextContent();
				expr.setStart(str);
			} else {
				System.err.println("![WARN] illegal element for CommentExpression : " + nodename);
				continue;
			}
		}
		this.currentLangRule.addCommentExpression(expr);
	}
	
	protected void parseRuleOfLiteralString(Element element) {
		LiteralStringDefine literalstr = new LiteralStringDefine();
		NodeList lsdChildren = element.getChildNodes();		
		for (int i=0; i < lsdChildren.getLength(); i++) {
			Node node = lsdChildren.item(i);
			if (! checkElementNode(node)) {
				continue;
			}
			Element child = (Element)node;
			//System.out.println("[DEBUG] LiteralString's Element=" + child.getNodeName());
			String nodename = getNameWithoutNS(child.getNodeName());
			if (nodename.equals(SCXmlCommonDefine.XML_NODE_START)) {
				String str = child.getTextContent();
				literalstr.setStart(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_END)) {
				String str = child.getTextContent();
				literalstr.setEnd(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_ESCAPE)) {
				String str = child.getTextContent();
				literalstr.setEscape(str);
			} else {
				System.err.println("![WARN] illegal element for LiteralString : " + nodename);
				continue;
			}
		}
		this.currentLangRule.addLiteralString(literalstr);
	}
	
	protected void parseRuleOfLabelHereDoc(Element element) {
		LabelHereDocDefine heredoc = new LabelHereDocDefine();
		NodeList lhddChildren = element.getChildNodes();		
		for (int i=0; i < lhddChildren.getLength(); i++) {
			Node node = lhddChildren.item(i);
			if (! checkElementNode(node)) {
				continue;
			}
			Element child = (Element)node;
			//System.out.println("[DEBUG] LabelHereDoc's Element=" + child.getNodeName());
			String nodename = getNameWithoutNS(child.getNodeName());
			if (nodename.equals(SCXmlCommonDefine.XML_NODE_START)) {
				String str = child.getTextContent();
				heredoc.setStart(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_END)) {
				String str = child.getTextContent();
				heredoc.setEnd(str);
			} else {
				System.err.println("![WARN] illegal element for LabelHereDoc : " + nodename);
				continue;
			}
		}
		this.currentLangRule.addLabelHereDoc(heredoc);
	}
	
	protected void parseRuleOfScriptBlock(Element element) {
		ScriptBlockDefine script = new ScriptBlockDefine();
		NodeList sbdChildren = element.getChildNodes();		
		for (int i=0; i < sbdChildren.getLength(); i++) {
			Node node = sbdChildren.item(i);
			if (! checkElementNode(node)) {
				continue;
			}
			Element child = (Element)node;
			//System.out.println("[DEBUG] ScriptBlock's Element=" + child.getNodeName());
			String nodename = getNameWithoutNS(child.getNodeName());
			if (nodename.equals(SCXmlCommonDefine.XML_NODE_START)) {
				String str = child.getTextContent();
				script.setStart(str);
			} else if (nodename.equals(SCXmlCommonDefine.XML_NODE_END)) {
				String str = child.getTextContent();
				script.setEnd(str);
			} else {
				System.err.println("![WARN] illegal element for ScriptBlock : " + nodename);
				continue;
			}
		}
		this.currentLangRule.addScriptBlock(script);
	}

}
