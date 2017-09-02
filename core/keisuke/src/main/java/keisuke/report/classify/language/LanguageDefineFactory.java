package keisuke.report.classify.language;

/**
 * Factory to make instances which are classes for parsing language definition xml
 * about LanguageElement (without counting rules)
 */
public class LanguageDefineFactory {

	public LanguageDefineFactory() { }

	/**
	 * 渡されたLanguage名、グループ名を設定したLanguageElementインスタンスを作成して返す
	 * @param name Language名称
	 * @param group Languageグループ名称
	 * @return LanguageElement
	 */
	public LanguageElement createLanguageElement(final String name, final String group) {
		return new LanguageElement(name, group);
	}

	/**
	 * キーが文字列、値がLanguageElementの空のHashMapを作成して返す
	 * @return HashMap
	 */
	public LanguageElementMap createLanguageElementMap() {
		return new LanguageElementMapImpl();
	}

	/**
	 * 自身のインスタンスを保持するXmlLanguageDefineインスタンスを作成して返す
	 * @return XmlLnguageDefine
	 */
	public XmlLanguageDefine createXmlLanguageDefine() {
		return new XmlLanguageDefine(this);
	}
}
