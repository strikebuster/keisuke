package keisuke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrameworkElement implements IfXmlParseSubject {
	private String name = null;
	private String group = null;
	private List<FwSpecificElement> specificTypes = null;
	
	public FrameworkElement(String lang, String grp) {
		this.name = lang;
		this.group = grp;
		this.specificTypes = new ArrayList<FwSpecificElement>();
	}
	
	public String getXmlNodeName() {
		return CommonDefine.XML_NODE_FW;
	}
	
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_SPECIFIC);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public void addSpecificType(FwSpecificElement sptype) {
		if (sptype == null) {
			return;
		}
		this.specificTypes.add(sptype);
	}
	
	public List<FwSpecificElement> getSpecificTypes() {
		return this.specificTypes;
	}
	
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("[DEBUG] FrameworkElement : name=" + this.name + ", group=" + this.group);
		sb.append("\n");
		sb.append("[DEBUG] FrameworkElement : specificTypes as follows");
		sb.append("\n");
		for (FwSpecificElement fse : this.specificTypes) {
			sb.append(fse.debug());
		}
		sb.append("\n");
		return sb.toString();
	}
}
