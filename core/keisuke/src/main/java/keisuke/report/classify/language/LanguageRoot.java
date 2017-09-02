package keisuke.report.classify.language;

import static keisuke.report.classify.language.XmlLanguageConstant.*;

import java.util.Arrays;
import java.util.List;

import keisuke.xmldefine.XmlParseSubject;

/**
 * Root node of language defining xml.
 */
public class LanguageRoot implements XmlParseSubject {
	/** Default constructor */
	public LanguageRoot() { }

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return XML_NODE_LANGROOT;
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(XML_NODE_LANG);
	}

}
