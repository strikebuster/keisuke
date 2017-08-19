package keisuke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Framework element node of XML to define framework.
 * @author strikebuster
 *
 */
class FrameworkElement implements IfXmlParseSubject {
	private String name = null;
	private String group = null;
	private List<FwSpecificElement> specificTypes = null;


	/**
	 * Frameworkノードに定義されている名称を設定し、成果物種類を格納するListを
	 * 用意するコンストラクタ
	 * @param fw Framework名称
	 * @param grp Frameworkグループ名称
	 */
	protected FrameworkElement(final String fw, final String grp) {
		this.name = fw;
		this.group = grp;
		this.specificTypes = new ArrayList<FwSpecificElement>();
	}

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return CommonDefine.XML_NODE_FW;
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_SPECIFIC);
	}

	/**
	 * 自ノードのframework名称を返す
	 * @return framework名称
	 */
	protected String getName() {
		return this.name;
	}

	/**
	 * 自ノードのframeworkグループ名称を返す
	 * @return frameworkグループ名称
	 */
	protected String getGroup() {
		return this.group;
	}

	/**
	 * 自ノードframeworkに成果物種類の定義を追加する
	 * @param sptype framework成果物種類
	 */
	protected void addSpecificType(final FwSpecificElement sptype) {
		if (sptype == null) {
			return;
		}
		this.specificTypes.add(sptype);
	}

	/**
	 * 自ノードframeworkに定義された成果物種類のListを返す
	 * @return framework成果物種類のList
	 */
	protected List<FwSpecificElement> getSpecificTypes() {
		return this.specificTypes;
	}

	/**
	 * DEBUG用Framework定義の解析ログの文字列を作成する
	 * @return ログ文字列
	 */
	protected String debug() {
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
