package keisuke;

import java.util.Arrays;
import java.util.List;

public class FrameworkRoot implements IfXmlParseSubject {

	public String getXmlNodeName() {
		return "FrameworkDifinitions";
	}
	
	public List<String> getXmlNodeAttributes() {
		return null;
	}
	
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_FW);
	}
	
}
