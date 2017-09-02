package keisuke.report.classify.language;

import java.net.URL;

/**
 * Language definitions for keisuke.
 * It uses default definition file "/keisuke/language.xml"
 */
public abstract class AbstractLanguageDefine {

	private static final String DEFAULT_XMLFILE = "/keisuke/language.xml";
	private LanguageDefineFactory langDefineFactory = null;
	private XmlLanguageDefine langDefine = null;
	private LanguageElementMap extLangMap = null;

	protected AbstractLanguageDefine() { }

	/**
	 * LanguageDefineFactoryを設定する
	 * @param factory Language定義用のLanguageDefineFactory
	 */
	protected void setLanguageDefineFactory(final LanguageDefineFactory factory) {
		this.langDefineFactory = factory;
	}

	/**
	 * LanguageDefineFactoryを設定する
	 */
	public void makeLanguageDefineFactory() {
		this.setLanguageDefineFactory(new LanguageDefineFactory());
	}

	/**
	 * LanguageDefineFactoryを返す
	 * @return XML解析用LanguageDefineFactory
	 */
	protected LanguageDefineFactory languageDefineFactory() {
		return this.langDefineFactory;
	}

	/**
	 * 言語定義内容をLanguageElementMapの形式で返す
	 * @return LanguageElementMapインスタンス
	 */
	protected LanguageElementMap extensionLanguageMap() {
		return this.extLangMap;
	}

	/**
	 * Language定義をデフォルト定義ファイルの内容で初期化する
	 */
	protected void initialize() {
		if (this.langDefineFactory == null) {
			throw new RuntimeException("!! langDefineFactory is not created by calling init().");
		}
		try {
			this.langDefine = this.langDefineFactory.createXmlLanguageDefine();
			URL urlfile = this.getClass().getResource(DEFAULT_XMLFILE);
			String uriStr = urlfile.toURI().toString();
			//System.out.println("[DEBUG] xml uri=" + uriStr);
			//System.out.flush();
			this.extLangMap = this.langDefine.createLanguageMapBy(uriStr);
			//this.extLangMap.debug();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Languageのカスタマイズ定義XMLの内容をデフォルト定義に上書き追加する
	 * @param fname カスタマイズ定義XMLファイル名
	 */
	public void customizeLanguageDefinitions(final String fname) {
		LanguageElementMap customMap = this.langDefine.createLanguageMapBy(fname);
		//customMap.debug();
		this.extLangMap.mergeMap(customMap);
		//this.extLangMap.debug();
	}

}