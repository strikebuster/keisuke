package keisuke.count.xmldefine;

import keisuke.XmlElementFactory;

/**
 * Factory to make instances which are classes for parsing language definition xml
 * about LanguageElementWithRule
 */
public class XmlElementWithRuleFactory extends XmlElementFactory {

	public XmlElementWithRuleFactory() { }

	/** {@inheritDoc} */
	@Override
	public LanguageElementWithRule createLanguageElement(final String name, final String group) {
		return new LanguageElementWithRule(name, group);
	}

	/** {@inheritDoc} */
	@Override
	public XmlLanguageDefineWithRule createXmlLanguageDefine() {
		return new XmlLanguageDefineWithRule(this);
	}
}
