package keisuke;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;

/**
 * Framework definitions with patterns of artifacts types.
 * @author strikebuster
 *
 */
class XmlFrameworkDefine extends AbstractXmlDefine {

	private Map<String, FrameworkElement> fwMap = null;
	private FrameworkElement currentFwElem = null;
	private int indexFwSpec = -1;
	private FwSpecificElement currentFwSpecElem = null;

	/**
	 * Framework定義のXMLファイルの内容を設定するコンストラクタ
	 * @param fname Framework定義XMLファイル名
	 */
	protected XmlFrameworkDefine(final String fname) {
		super();
		this.fwMap = new HashMap<String, FrameworkElement>();
		IfXmlParseSubject subject = new FrameworkRoot();
		parseXml(fname, subject);
		//System.out.println("[DEBUG] fwMap");
		//debugFwMap(this.fwMap);
	}

	/** {@inheritDoc} */
	protected void dealChild(final String name, final Element elem) {
		if (name.equals(CommonDefine.XML_NODE_FW)) {
			parseFramework(elem);
		} else if (name.equals(CommonDefine.XML_NODE_SPECIFIC)) {
			parseFwSpecific(elem);
		} else if (name.equals(CommonDefine.XML_NODE_PATTERN)) {
			parseFwSpecPattern(elem);
		} else {
			System.err.println("![WARN] illegal element : " + name);
			return;
		}
	}

	/**
	 * XML中のFrameworkノードを解析する
	 * @param element FrameworkノードのXML Element
	 */
	private void parseFramework(final Element element) {
		String fwName = element.getAttribute(CommonDefine.XML_ATTR_NAME);
		String fwGroup = element.getAttribute(CommonDefine.XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Framework=" + fwGroup + "." + fwName);
		this.currentFwElem = new FrameworkElement(fwName, fwGroup);
		this.fwMap.put(fwName, this.currentFwElem);
		this.indexFwSpec = -1;
		parseChildrenNodes(element, this.currentFwElem.getXmlChildrenNames());
	}

	/**
	 * XML中のFrameworkノード配下のFwSpecificノードを解析する
	 * @param element FwSpecificノードのXML Element
	 */
	private void parseFwSpecific(final Element element) {
		this.indexFwSpec++;
		String spNameOrder = String.format("%03d", this.indexFwSpec);
		String spName = spNameOrder + "#" + element.getAttribute(CommonDefine.XML_ATTR_NAME);
		String spGroup = element.getAttribute(CommonDefine.XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Fw SpecificType=" + fwGroup + "." + fwName);
		this.currentFwSpecElem = new FwSpecificElement(spName, spGroup);
		this.currentFwElem.addSpecificType(this.currentFwSpecElem);
		parseChildrenNodes(element, this.currentFwSpecElem.getXmlChildrenNames());
	}

	/**
	 * XML中のFrameworkノード配下のFwSpecPattern属性を解析する
	 * @param element FwSpecPattern属性のXML Element
	 */
	private void parseFwSpecPattern(final Element element) {
		String ptnstr = element.getTextContent();
		//System.out.println("[DEBUG] Fw SpecType Pattern=" + ptnstr);
		this.currentFwSpecElem.addPatternString(ptnstr);
	}

	/**
	 * 指定されたFramework名称に該当するFwSpecificElementのListを作成して返す
	 * @param fw Framework名称
	 * @return FwSpecificElementのList
	 */
	protected List<FwSpecificElement> createFwSpecificList(final String fw) {
		//System.out.println("[DEBUG] createFwSpecificList for fw=" + fw);
		if (fw == null) {
			return null;
		}
		for (Entry<String, FrameworkElement> entry : this.fwMap.entrySet()) {
			//String key = entry.getKey();
			//System.out.println("[DEBUG] fwMap.get(" + key +")");
			FrameworkElement fwElem = entry.getValue();
			String name = fwElem.getName();
			//System.out.println("[DEBUG] FrameworkElement name=" + name);
			if (name == null) {
				continue;
			}
			name = name.toUpperCase();
			if (fw.toUpperCase().equals(name)) {
				return fwElem.getSpecificTypes();
			}
		}
		System.err.println("![WARN] not found Framework definition for fw:" + fw);
		return null;
	}

	/**
	 * DEBUG用Framework定義マップの内容を表示する
	 * @param map Framework定義マップ
	 */
	private void debugFwMap(final Map<String, FrameworkElement> map) {
		for (Entry<String, FrameworkElement> entry : map.entrySet()) {
			String key = entry.getKey();
			FrameworkElement data = entry.getValue();
			System.out.println("[DEBUG] Map Key=" + key);
			System.out.println(data.debug());
		}
	}
}
