package keisuke.count.xmldefine;

import java.util.Arrays;
import java.util.List;

import keisuke.CommonDefine;
import keisuke.LanguageElement;

public class LanguageElementWithRule extends LanguageElement {
	private LanguageCountRule countRule = null;
	
	public LanguageElementWithRule(String lang, String grp) {
		super(lang, grp);
	}
	
	@Override
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_EXT, SCXmlCommonDefine.XML_NODE_RULE);
	}
	
	public void setCountRule(LanguageCountRule lcr) {
		this.countRule = lcr;
	}
	
	public LanguageCountRule getCountRule() {
		return this.countRule;
	}
	
	@Override
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.debug());
		sb.append("[DEBUG] LanguageElementWithRule : countRule\n");
		if ( this.countRule != null) {
			sb.append(this.countRule.debug());
		}
		return sb.toString();
	}
}
