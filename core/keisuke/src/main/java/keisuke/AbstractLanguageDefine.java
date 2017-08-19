package keisuke;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Language definitions for keisuke.
 * It uses default definition file "/keisuke/language.xml"
 * @author strikebuster
 *
 */
public abstract class AbstractLanguageDefine {

	private static final String DEFAULT_XMLFILE = "/keisuke/language.xml";
	private XmlElementFactory xmlElemFactory = null;
	private XmlLanguageDefine xmlLangDefine = null;
	private Map<String, LanguageElement> extLangMap = null;

	protected AbstractLanguageDefine() { }

	/**
	 * XmlElementFactoryを設定する
	 * @param xef Language定義用のXmlElementFactory
	 */
	protected void setLanguageDefineFactory(final XmlElementFactory xef) {
		this.xmlElemFactory = xef;
	}

	/**
	 * XmlElementFactoryを返す
	 * @return XML解析用ElementFactory
	 */
	protected XmlElementFactory languageDefineFactory() {
		return this.xmlElemFactory;
	}

	/**
	 * ソースファイルの拡張子Stringと対応するLanguageElementのMapを返す
	 * @return ソース拡張子と対応するLanguageElementのMap
	 */
	protected Map<String, LanguageElement> extensionLanguageMap() {
		return this.extLangMap;
	}
	/**
	 * Language定義をデフォルト定義ファイルの内容で初期化する
	 */
	protected void initialize() {
		if (this.xmlElemFactory == null) {
			throw new RuntimeException("!! xmlElementFactory is not created by calling init().");
		}
		try {
			this.xmlLangDefine = this.xmlElemFactory.createXmlLanguageDefine();
			URL urlfile = this.getClass().getResource(DEFAULT_XMLFILE);
			String uriStr = urlfile.toURI().toString();
			//System.out.println("[DEBUG] xml uri=" + uriStr);
			//System.out.flush();
			this.extLangMap = this.xmlLangDefine.createExtensionLanguageMapBy(uriStr);
			//debug(this.langExtMap);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Languageのカスタマイズ定義XMLの内容をデフォルト定義に上書き追加する
	 * @param fname カスタマイズ定義XMLファイル名
	 */
	protected void customizeLanguageDefinitions(final String fname) {
		Map<String, LanguageElement> customMap = this.xmlLangDefine.createExtensionLanguageMapBy(fname);
		//debug(customMap);
		appendExtensionLanguageMap(customMap);
		//debug(this.extLangMap);
	}

	/**
	 * 渡されたLangauge定義Mapを自分のMapに上書き追加する
	 * @param map Langauge定義Map
	 */
	protected void appendExtensionLanguageMap(final Map<String,  LanguageElement> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		for (Entry<String, LanguageElement> entry : map.entrySet()) {
			this.extLangMap.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * DEBUG用Langauge定義Mapの内容を表示する
	 * @param map Langauge定義Map
	 */
	protected void debug(final Map<String, LanguageElement> map) {
		System.out.println("[DEBUG] Language Map contains as follows");
		if (map == null || map.isEmpty()) {
			System.out.println("[DEBUG] map is null.");
			return;
		}
		List<String> keylist = new ArrayList<String>(map.keySet());
		Collections.sort(keylist);
		for (String key : keylist) {
			LanguageElement data = map.get(key);
			System.out.println("[DEBUG] MapKey ext=" + key);
			System.out.println(data.debug());
		}
	}
}
