package keisuke.count.xmldefine;

import keisuke.report.classify.language.LanguageDefineFactory;

/**
 * Factory to make instances which are classes for parsing language definition xml
 * about LanguageElementWithRule
 */
public class LanguageDefineWithRuleFactory extends LanguageDefineFactory {

	public LanguageDefineWithRuleFactory() { }

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
