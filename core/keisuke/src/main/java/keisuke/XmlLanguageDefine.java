package keisuke;

import java.util.Map;

import org.w3c.dom.Element;

public class XmlLanguageDefine extends AbstractXmlDefine {

	protected Map<String, LanguageElement> extensionLangElemMap = null;
	protected LanguageElement currentLangElem = null;
	protected XmlElementFactory xmlElemFactory = null;
	
	protected XmlLanguageDefine() { }
	
	public XmlLanguageDefine(XmlElementFactory factory) {
		this.xmlElemFactory = factory;
	}

	protected void init(String fname) {
		this.extensionLangElemMap = this.xmlElemFactory.createLanguageElementMap();
		IfXmlParseSubject subj = new LanguageRoot();
		parseXml(fname, subj);
		//debug();
	}
	
	protected void dealChild(String name, Element elem) {
		if (name.equals(CommonDefine.XML_NODE_LANG)) {
			parseLanguage(elem);
		} else if (name.equals(CommonDefine.XML_NODE_EXT)) {
			parseLangExtension(elem);
		} else {
			System.err.println("![WARN] illegal element : " + name);
			return;
		}
	}
	
	protected void parseLanguage(Element element) {
		String langName = element.getAttribute(CommonDefine.XML_ATTR_NAME);
		String langGroup = element.getAttribute(CommonDefine.XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Lang=" + langGroup + "." + langName);
		this.currentLangElem = this.xmlElemFactory.createLanguageElement(langName, langGroup);
		parseChildrenNodes(element, this.currentLangElem.getXmlChildrenNames());
	}
	
	protected void parseLangExtension(Element element) {
		String ext = element.getTextContent();
		//System.out.println("[DEBUG] Ext=" + ext);
		this.currentLangElem.addExtension(ext);
		this.extensionLangElemMap.put(ext, this.currentLangElem);
	}
	
	public Map<String, LanguageElement> createLangExtMap(String fname) {
		init(fname);
		if (this.extensionLangElemMap.isEmpty()) {
			return null;
		}
		return this.extensionLangElemMap;
	}
	
	protected void debug() {
		System.out.println("[DEBUG] XmlLanguageDefine.extensionLangElemMap contains as follows");
		if (this.extensionLangElemMap.isEmpty()) {
			System.out.println("[DEBUG] map is null.");
			return;
		}	
		for ( String key : this.extensionLangElemMap.keySet() ) {
			LanguageElement data = this.extensionLangElemMap.get( key );
			System.out.println("[DEBUG] MapKey ext=" + key);
			System.out.println(data.debug());
		}
	}
}
