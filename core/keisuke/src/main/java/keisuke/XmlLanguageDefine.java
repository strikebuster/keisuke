package keisuke;

import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;

/**
 * Language definitions with attributes of file extensions.
 * @author strikebuster
 *
 */
public class XmlLanguageDefine extends AbstractXmlDefine {

	private Map<String, LanguageElement> extLangMap = null;
	private LanguageElement currentLangElem = null;
	private XmlElementFactory xmlElemFactory = null;

	/**
	 * Language定義（拡張子版）のXmlElementFactoryを設定するコンストラクタ
	 * @param factory Language定義用のXmlElementFactoryインスタンス
	 */
	public XmlLanguageDefine(final XmlElementFactory factory) {
		super();
		this.setLanguageDefineFactory(factory);
	}

	/**
	 * XmlElementFactoryを設定する
	 * @param xef Language定義用のXmlElementFactory
	 */
	protected void setLanguageDefineFactory(final XmlElementFactory xef) {
		this.xmlElemFactory = xef;
	}

	/**
	 * Language定義XML解析用に設定されたXmlElementFactoryを返す
	 * @return XML解析XmlElementFactory
	 */
	protected XmlElementFactory languageDefineFactory() {
		return this.xmlElemFactory;
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
		IfXmlParseSubject subj = new LanguageRoot();
		parseXml(fname, subj);
		//debug();
	}

	/** {@inheritDoc} */
	protected void dealChild(final String name, final Element elem) {
		if (name.equals(CommonDefine.XML_NODE_LANG)) {
			parseLanguage(elem);
		} else if (name.equals(CommonDefine.XML_NODE_EXT)) {
			parseLangExtension(elem);
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
		String langName = element.getAttribute(CommonDefine.XML_ATTR_NAME);
		String langGroup = element.getAttribute(CommonDefine.XML_ATTR_GROUP);
		//System.out.println("[DEBUG] Lang=" + langGroup + "." + langName);
		this.currentLangElem = this.languageDefineFactory().createLanguageElement(langName, langGroup);
		parseChildrenNodes(element, this.currentLangElem.getXmlChildrenNames());
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
	public Map<String, LanguageElement> createExtensionLanguageMapBy(final String fname) {
		initialize(fname);
		if (this.extLangMap.isEmpty()) {
			return null;
		}
		return this.extLangMap;
	}

	/**
	 * DEBUG用Language定義（拡張子版）マップの内容を表示する
	 */
	protected void debug() {
		System.out.println("[DEBUG] XmlLanguageDefine.extLangMap contains as follows");
		if (this.extLangMap.isEmpty()) {
			System.out.println("[DEBUG] map is null.");
			return;
		}
		for (Entry<String, LanguageElement> entry : this.extLangMap.entrySet()) {
			String key = entry.getKey();
			LanguageElement data = entry.getValue();
			System.out.println("[DEBUG] MapKey ext=" + key);
			System.out.println(data.debug());
		}
	}
}
