package keisuke.report.classify.language;

import static keisuke.report.classify.language.XmlLanguageConstant.*;

import org.w3c.dom.Element;

import keisuke.xmldefine.AbstractXmlDefine;
import keisuke.xmldefine.XmlParseSubject;

/**
 * Language definitions with attributes of file extensions.
 */
public class XmlLanguageDefine extends AbstractXmlDefine {

	private LanguageElementMap extLangMap = null;
	private LanguageElement currentLangElem = null;
	private LanguageDefineFactory langDefineFactory = null;

	/**
	 * Language定義（拡張子版）のLanguageDefineFactoryを設定するコンストラクタ
	 * @param factory Language定義用のLanguageDefineFactoryインスタンス
	 */
	public XmlLanguageDefine(final LanguageDefineFactory factory) {
		super();
		this.setLanguageDefineFactory(factory);
	}

	/**
	 * LanguageDefineFactoryを設定する
	 * @param factory Language定義用のLanguageDefineFactory
	 */
	protected void setLanguageDefineFactory(final LanguageDefineFactory factory) {
		this.langDefineFactory = factory;
	}

	/**
	 * Language定義XML解析用に設定されたLanguageDefineFactoryを返す
	 * @return XML解析LanguageDefineFactory
	 */
	protected LanguageDefineFactory languageDefineFactory() {
		return this.langDefineFactory;
	}

	/**
	 * 解析中のLanguage定義Elementを設定する
	 * @param le 解析中のLanguage定義Element
	 */
	protected void setCurrentLanguageElement(final LanguageElement le) {
		this.currentLangElem = le;
	}

	/**
	 * 解析中のLanguage定義Elementを返す
	 * @return 解析中のLanguage定義Element
	 */
	protected LanguageElement currentLanguageElement() {
		return this.currentLangElem;
	}

	/**
	 * Language定義（拡張子版）を指定の定義ファイルの内容で初期化する
	 * @param fname Language定義用のXMLファイル名
	 */
	protected void initialize(final String fname) {
		this.extLangMap = this.languageDefineFactory().createLanguageElementMap();
		XmlParseSubject subj = new LanguageRoot();
		this.parseXml(fname, subj);
		//this.extLangMap.debug();
	}

	/** {@inheritDoc} */
	protected void dealChild(final String name, final Element elem) {
		if (name.equals(XML_NODE_LANG)) {
			this.parseLanguage(elem);
		} else if (name.equals(XML_NODE_EXT)) {
			this.parseLangExtension(elem);
		} else {
			System.err.println("![WARN] illegal element : " + name);
			return;
		}
	}

	/**
	 * XML中のLanguageノードを解析する
	 * @param element LanguageノードのXML Element
	 */
	protected void parseLanguage(final Element element) {
		String langName = element.getAttribute(XML_ATTR_NAME);
		String langGroup = element.getAttribute(XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Lang=" + langGroup + "." + langName);
		this.currentLangElem = this.languageDefineFactory().createLanguageElement(langName, langGroup);
		this.parseChildrenNodes(element, this.currentLangElem.getXmlChildrenNames());
	}

	/**
	 * XML中のLanguageノード配下のExtension属性を解析する
	 * @param element Extension属性のXML Element
	 */
	protected void parseLangExtension(final Element element) {
		String ext = element.getTextContent();
		//System.out.println("[DEBUG] Ext=" + ext);
		this.currentLangElem.addExtension(ext);
		this.extLangMap.put(ext, this.currentLangElem);
	}

	/**
	 * Language定義を解析し拡張子とLanguageElementのMapを作成して返す
	 * @param fname Language定義XMLファイル名
	 * @return 拡張子とLanguageElementのMap
	 */
	public LanguageElementMap createLanguageMapBy(final String fname) {
		this.initialize(fname);
		if (this.extLangMap.isEmpty()) {
			return null;
		}
		return this.extLangMap;
	}

}
