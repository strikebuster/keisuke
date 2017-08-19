package keisuke;

import java.util.Arrays;
import java.util.List;

/**
 * Root node of framework defining xml.
 * @author strikebuster
 *
 */
class FrameworkRoot implements IfXmlParseSubject {

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return "FrameworkDifinitions";
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_FW);
	}

}
