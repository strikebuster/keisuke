package keisuke;

import java.util.Arrays;
import java.util.List;

/**
 * Root node of language defining xml.
 * @author strikebuster
 *
 */
public class LanguageRoot implements IfXmlParseSubject {
	/** Default constructor */
	public LanguageRoot() { }

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return "LanguageDifinitions";
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_LANG);
	}

}
