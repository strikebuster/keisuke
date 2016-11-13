package keisuke;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractLanguageDefine {

	protected final static String xmlfile = "/keisuke/language.xml";
	protected XmlElementFactory xmlElemFactory = null;
	protected XmlLanguageDefine xmlLangDefine = null;
	protected Map<String, LanguageElement> langExtMap = null;
	
	protected AbstractLanguageDefine() { }
	
	protected void init() {
		if (this.xmlElemFactory == null) {
			throw new RuntimeException("!! xmlElementFactory is not created by calling init().");
		}
		try {
			this.xmlLangDefine = this.xmlElemFactory.createXmlLanguageDefine();
			URL urlfile = this.getClass().getResource(xmlfile);
			String uriStr = urlfile.toURI().toString();
			//System.out.println("[DEBUG] xml uri=" + uriStr);
			//System.out.flush();
			this.langExtMap = this.xmlLangDefine.createLangExtMap(uriStr);
			//debug(this.langExtMap);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void customizeLanguageDefinitions(String fname) {
		Map<String, LanguageElement> customMap = this.xmlLangDefine.createLangExtMap(fname);
		//debug(customMap);
		appendLangExtMap(customMap);
		//debug(this.langExtMap);
	}
	
	protected void appendLangExtMap(Map<String,  LanguageElement> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		for (String key : map.keySet()) {
			this.langExtMap.put(key, map.get(key));
		}
	}
	
	protected void debug(Map<String, LanguageElement> map) {
		System.out.println("[DEBUG] Language Map contains as follows");
		if (map == null || map.isEmpty()) {
			System.out.println("[DEBUG] map is null.");
			return;
		}
		List<String> keylist = new ArrayList<String>(map.keySet());
		Collections.sort(keylist);
		for ( String key : keylist) {
			LanguageElement data = map.get( key );
			System.out.println("[DEBUG] MapKey ext=" + key);
			System.out.println(data.debug());
		}
	}
}
