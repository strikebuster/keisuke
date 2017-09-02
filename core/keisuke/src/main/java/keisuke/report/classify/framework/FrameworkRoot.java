package keisuke.report.classify.framework;

import static keisuke.report.classify.framework.XmlFrameworkConstant.*;

import java.util.Arrays;
import java.util.List;

import keisuke.xmldefine.XmlParseSubject;

/**
 * Root node of framework defining xml.
 */
class FrameworkRoot implements XmlParseSubject {

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return XML_NODE_FWROOT;
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(XML_NODE_FW);
	}

}
