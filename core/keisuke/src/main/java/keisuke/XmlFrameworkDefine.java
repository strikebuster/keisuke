package keisuke;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public class XmlFrameworkDefine extends AbstractXmlDefine {
	
	private Map<String, FrameworkElement> fwMap = null;
	private FrameworkElement currentFwElem = null;
	private int indexFwSpec = -1;
	private FwSpecificElement currentFwSpecElem = null;
	 
	public XmlFrameworkDefine(String fname) {
		this.fwMap = new HashMap<String, FrameworkElement>();
		IfXmlParseSubject subj = new FrameworkRoot();
		parseXml(fname, subj);
		//System.out.println("[DEBUG] fwMap");
		//debugFwMap(this.fwMap);
	}
	
	protected void dealChild(String name, Element elem) {
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
	
	private void parseFramework(Element element) {
		String fwName = element.getAttribute(CommonDefine.XML_ATTR_NAME);
		String fwGroup = element.getAttribute(CommonDefine.XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Framework=" + fwGroup + "." + fwName);
		this.currentFwElem = new FrameworkElement(fwName, fwGroup);
		this.fwMap.put(fwName, this.currentFwElem);
		this.indexFwSpec = -1;
		parseChildrenNodes(element, this.currentFwElem.getXmlChildrenNames());
	}
	
	private void parseFwSpecific(Element element) {
		this.indexFwSpec++;
		String spNameOrder = String.format("%03d", this.indexFwSpec);
		String spName = spNameOrder + "#" + element.getAttribute(CommonDefine.XML_ATTR_NAME);
		String spGroup = element.getAttribute(CommonDefine.XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Fw SpecificType=" + fwGroup + "." + fwName);
		this.currentFwSpecElem = new FwSpecificElement(spName, spGroup);
		this.currentFwElem.addSpecificType(this.currentFwSpecElem);
		parseChildrenNodes(element, this.currentFwSpecElem.getXmlChildrenNames());
	}
	
	private void parseFwSpecPattern(Element element) {
		String ptnstr = element.getTextContent();
		//System.out.println("[DEBUG] Fw SpecType Pattern=" + ptnstr);
		this.currentFwSpecElem.addPatternString(ptnstr);
	}
	
	public List<FwSpecificElement> createFwSpecificList(String fw) {
		//System.out.println("[DEBUG] createFwSpecificList for fw=" + fw);
		if (fw == null) {
			return null;
		}
		fw = fw.toUpperCase();
		for ( String key : this.fwMap.keySet() ) {
			//System.out.println("[DEBUG] fwMap.get(" + key +")");
			FrameworkElement fe = this.fwMap.get( key );
			String name = fe.getName();
			//System.out.println("[DEBUG] FrameworkElement name=" + name);
			if (name == null) {
				continue;
			}
			name = name.toUpperCase();
			if (fw.equals(name)) {
				return fe.getSpecificTypes();
			}
		}
		System.err.println("![WARN] not found Framework definition for fw:" + fw);
		return null;
	}
	
	private void debugFwMap(Map<String, FrameworkElement> map) {
		for ( String key : map.keySet() ) {
			FrameworkElement data = map.get( key );
			System.out.println("[DEBUG] Map Key=" + key);
			System.out.println(data.debug());
		}
	}
}
