package keisuke.xmldefine;

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

import keisuke.util.LogUtil;

/**
 * Parsing XML which defines something about keisuke command.
 */
public abstract class AbstractXmlDefine {

	private XmlParseSubject parseSubject;

	protected AbstractXmlDefine() { }

	/**
	 * XMLファイルを解析して解析ルートXmlParseSubjectに設定する
	 * @param fname 解析対象のXMLファイル名
	 * @param subject 対象XMLの解析ルート
	 */
	protected void parseXml(final String fname, final XmlParseSubject subject) {
		this.parseSubject = subject;
		Element root = this.getDocRoot(fname);
		// 子ノード要素の処理
		List<String> children = this.parseSubject.getXmlChildrenNames();
		this.parseChildrenNodes(root, children);
	}

	/**
	 * XMLファイルのルートElementを返す
	 * @param fname 解析対象のXMLファイル名
	 * @return XMLのルートElement
	 */
	protected Element getDocRoot(final String fname) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(fname);
			Element root = document.getDocumentElement();
			//LogUtil.debugLog("XML Root：" + root.getNodeName());
			return root;
		} catch (IOException ioe) {
			LogUtil.errorLog("Read error : " + fname);
			throw new RuntimeException(ioe);
		} catch (SAXException saxe) {
			LogUtil.errorLog("SAXException : " + fname);
			throw new RuntimeException(saxe);
		} catch (ParserConfigurationException pce) {
			LogUtil.errorLog("XML ParseConfigurationException : " + fname);
			throw new RuntimeException(pce);
		}
	}

	/**
	 * 解析対象のXML Elementの子ノードを再帰的に解析して設定する
	 * @param parent 解析対象XMLノードElement
	 * @param childrenNames 解析対象の子ノード名のList
	 */
	protected void parseChildrenNodes(final Element parent, final List<String> childrenNames) {
		if (parent == null || childrenNames == null || childrenNames.isEmpty()) {
			return;
		}
		//LogUtil.debugLog("parent Node=" + parent.getNodeName());
		// 子ノード要素の取得
		int childLen = -1;
		NodeList myChildren = parent.getChildNodes();
		if (myChildren == null) {
			return;
		}
		childLen = myChildren.getLength();
		for (int i = 0; i < childLen; i++) {
			Node node = myChildren.item(i);
			//LogUtil.debugLog("child Node=" + node.getNodeName());
			if (!checkElementNode(node)) {
				continue;
			}
			Element element = (Element) node;
			String nodename = getNameWithoutNS(element.getNodeName());
			if (!childrenNames.contains(nodename)) {
				// 解析対象でないので無視
				//LogUtil.debugLog("skip element : " + parent.getNodeName()
				//		+ "->" + nodename);
				//LogUtil.debugLog("because targets are " + childrenNames);
				continue;
			}
			// 解析対象子ノードの処理
			this.handleChild(nodename, element);
		}
	}

	/**
	 * XMLノードElementを解析して設定する
	 * @param name 解析対象のXMLノードの定義名
	 * @param element 解析対象のXMLノードElement
	 */
	protected abstract void handleChild(String name, Element element);

	/**
	 * XMLノードがElementであるかチェックする
	 * @param node XMLノード
	 * @return XMLノードがElementであればTRUE
	 */
	protected static boolean checkElementNode(final Node node) {
		if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
			//LogUtil.debugLog("illegal element: type=" + node.getNodeType()
			//	+ " name=" + node.getNodeName() + " value=" + node.getNodeValue());
			// TextNode(改行)など
			return false;
		}
		return true;
	}

	/**
	 * XMLノードのノード名からNameSpace名を除いた名称を返す
	 * @param str XMLノード名
	 * @return ノード名からNameSpace名を除いた名称
	 */
	protected static String getNameWithoutNS(final String str) {
		if (str == null) {
			return null;
		}
		int pos = str.indexOf(':');
		if (pos < 0) {
			return str;
		} else if (pos == str.length() - 1) {
			return "";
		} else {
			return str.substring(pos + 1);
		}
	}

}
