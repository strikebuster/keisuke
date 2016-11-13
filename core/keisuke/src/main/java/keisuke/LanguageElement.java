package keisuke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageElement implements IfXmlParseSubject {

	protected String name = null;
	protected String group = null;
	protected List<String> extensions = null;
	
	public LanguageElement(String lang, String grp) {
		this.name = lang;
		this.group = grp;
		this.extensions = new ArrayList<String>();
	}
	
	public String getXmlNodeName() {
		return CommonDefine.XML_NODE_LANG;
	}
	
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_EXT);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getGroup() {
		return this.group;
	}
	
	public void addExtension(String ext) {
		String str = ext;
		if (ext == null) {
			return;
		} else if (ext.length() != 0 && !ext.startsWith(".")) {
			str = "." + ext;
		}
		this.extensions.add(str);
	}
	
	public List<String> getExtensions() {
		return this.extensions;
	}
	
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("[DEBUG] LanguageElement : name=" + this.name + " group=" + this.group);
		sb.append("\n");
		sb.append("[DEBUG] LanguageElement : extensions=");
		for (String str : this.extensions) {
			sb.append(str + " , ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
