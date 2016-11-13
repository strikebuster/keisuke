package keisuke.count.xmldefine;

import keisuke.XmlElementFactory;

public class XmlElementWithRuleFactory extends XmlElementFactory {

	public XmlElementWithRuleFactory() { }
	
	@Override
	public LanguageElementWithRule createLanguageElement(String name, String group) {
		return new LanguageElementWithRule(name, group);
	}
	
	@Override
	public XmlLanguageDefineWithRule createXmlLanguageDefine() {
		return new XmlLanguageDefineWithRule(this);
	}
}
