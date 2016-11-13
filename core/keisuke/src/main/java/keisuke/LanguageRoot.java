package keisuke;

import java.util.Arrays;
import java.util.List;

public class LanguageRoot implements IfXmlParseSubject {

	public String getXmlNodeName() {
		return "LanguageDifinitions";
	}
	
	public List<String> getXmlNodeAttributes() {
		return null;
	}
	
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_LANG);
	}
	
}
