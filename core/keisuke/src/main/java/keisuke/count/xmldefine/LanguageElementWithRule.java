package keisuke.count.xmldefine;

import java.util.Arrays;
import java.util.List;

import keisuke.CommonDefine;
import keisuke.LanguageElement;

/**
 * Language element node of XML to define language with counting rules.
 */
public class LanguageElementWithRule extends LanguageElement {
	private LanguageCountRule countRule = null;

	/**
	 * Languageノードに定義されている値を設定するコンストラクタ
	 * @param lang Language名称
	 * @param grp Group名称
	 */
	protected LanguageElementWithRule(final String lang, final String grp) {
		super(lang, grp);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_EXT, SCXmlCommonDefine.XML_NODE_RULE);
	}

	/**
	 * 言語解析ルール定義を設定する
	 * @param rule 言語解析ルール定義インスタンス
	 */
	protected void setCountRule(final LanguageCountRule rule) {
		this.countRule = rule;
	}

	/**
	 * 言語解析ルール定義を返す
	 * @return 言語解析ルール定義インスタンス
	 */
	public LanguageCountRule getCountRule() {
		return this.countRule;
	}

	/** {@inheritDoc} */
	@Override
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.debug());
		sb.append("[DEBUG] LanguageElementWithRule : countRule\n");
		if (this.countRule != null) {
			sb.append(this.countRule.debug());
		}
		return sb.toString();
	}
}
