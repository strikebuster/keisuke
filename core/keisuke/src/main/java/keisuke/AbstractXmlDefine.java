package keisuke;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractXmlDefine {
	
	private IfXmlParseSubject parseSubject;
	
	protected AbstractXmlDefine() { }
	
	protected void parseXml(String fname, IfXmlParseSubject subject) {
		this.parseSubject = subject;
		Element root = getDocRoot(fname);
		// 子ノード要素の処理
		List<String> children = this.parseSubject.getXmlChildrenNames();
		parseChildrenNodes(root,children);
	}
	
	protected Element getDocRoot(String fname) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(fname);
			Element root = document.getDocumentElement();
			//System.out.println("[DEBUG] XML Root：" + root.getNodeName());
			return root;
		} catch (IOException ioe) {
			System.err.println("!! Read error : " + fname);
			throw new RuntimeException(ioe);
		} catch (SAXException saxe) {
			System.err.println("!! SAXException : " + fname);
			throw new RuntimeException(saxe);
		} catch (ParserConfigurationException pce) {
			System.err.println("!! XML ParseConfigurationException : " + fname);
			throw new RuntimeException(pce);
		}
	}
	
	protected void parseChildrenNodes(Element parent, List<String> childrenNames) {
		if (parent == null || childrenNames == null || childrenNames.isEmpty()) {
			return;
		}
		//System.out.println("[DEBUG] parent Node=" + parent.getNodeName());
		// 子ノード要素の取得
		int childLen = -1;
		NodeList myChildren = parent.getChildNodes();
		if (myChildren == null || (childLen = myChildren.getLength()) < 1) {
			return;
		}
		for(int i=0; i < childLen; i++) {
			Node node = myChildren.item(i);
			//System.out.println("[DEBUG] child Node=" + node.getNodeName());
			if (! checkElementNode(node)) {
				continue;
			}
			Element element = (Element)node;
			String nodename = getNameWithoutNS(element.getNodeName());
			if (! childrenNames.contains(nodename)) {
				// 解析対象でないので無視
				//System.out.println("[DEBUG] skip element : " + parent.getNodeName() + "->" + nodename);
				//System.out.println("[DEBUG] because targets are " + childrenNames);
				continue;
			}
			// 解析対象子ノードの処理
			dealChild(nodename, element);
		}
	}
	
	abstract void dealChild(String name, Element element);
	
	protected boolean checkElementNode(Node node) {
		if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			//System.out.println("![WARN] illegal element: type=" + node.getNodeType()
			//	+ " name=" + node.getNodeName() + " value=" + node.getNodeValue());
			// TextNode(改行)など
			return false;
		}
		return true;
	}
	
	protected  String getNameWithoutNS(String str) {
		if (str == null) {
			return null;
		}
		int pos = str.indexOf(':');
		if (pos < 0) {
			return str;
		} else if (pos == str.length() - 1) {
			return "";
		} else {
			return str.substring(pos+1);
		}
	}
	
}
