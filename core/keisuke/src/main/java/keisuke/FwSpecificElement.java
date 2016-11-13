package keisuke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FwSpecificElement implements IfXmlParseSubject {
	private String name = null;
	private String group = null;
	private List<String> patternStrings = null;
	
	public FwSpecificElement(String lang, String grp) {
		this.name = lang;
		this.group = grp;
		this.patternStrings = new ArrayList<String>();
	}
	
	public String getXmlNodeName() {
		return CommonDefine.XML_NODE_SPECIFIC;
	}
	
	public List<String> getXmlNodeAttributes() {
		return Arrays.asList("name", "group");
	}
	
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_PATTERN);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public void addPatternString(String str) {
		if (str == null) {
			return;
		}
		this.patternStrings.add(str);
	}
	
	public List<String> getPatternStrings() {
		return this.patternStrings;
	}
	
	public int countPatternStrings() {
		return this.patternStrings.size();
	}
	
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("[DEBUG] FwSpecificElement : name=" + this.name + " group=" + this.group);
		sb.append("\n");
		sb.append("[DEBUG] FwSpecificElement : patternStringns=");
		for (String str : this.patternStrings) {
			sb.append(str + " , ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
