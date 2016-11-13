package keisuke;

import java.util.HashMap;
import java.util.Map;

public class XmlElementFactory {

	public XmlElementFactory() { }
	
	public LanguageElement createLanguageElement(String name, String group) {
		return new LanguageElement(name, group);
	}
	
	public Map<String, LanguageElement> createLanguageElementMap() {
		return new HashMap<String, LanguageElement>();
	}
	
	public XmlLanguageDefine createXmlLanguageDefine() {
		return new XmlLanguageDefine(this);
	}
}
