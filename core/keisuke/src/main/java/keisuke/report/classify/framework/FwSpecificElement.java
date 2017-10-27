package keisuke.report.classify.framework;

import static keisuke.report.classify.framework.XmlFrameworkConstant.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import keisuke.xmldefine.XmlParseSubject;

/**
 * Framework artifact type specification element node of XML to define framework.
 */
public class FwSpecificElement implements XmlParseSubject {
	private String name = null;
	private String group = null;
	private List<String> patternStrings = null;

	/**
	 * Frameworkの成果物種類定義の名称を設定し、判定用パターンの格納Listを
	 * 用意するコンストラクタ
	 * @param lang 成果物種類の名称
	 * @param grp 成果物種類グループの名称
	 */
	protected FwSpecificElement(final String lang, final String grp) {
		this.name = lang;
		this.group = grp;
		this.patternStrings = new ArrayList<String>();
	}

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return XML_NODE_SPECIFIC;
	}

	/**
	 * 自ノードに定義された属性名のListを返す
	 * @return 自ノードの属性名List
	 */
	protected List<String> getXmlNodeAttributes() {
		return Arrays.asList("name", "group");
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(XML_NODE_PATTERN);
	}

	/**
	 * 自ノード成果物種類の名称を返す
	 * @return 成果物種類の名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 自ノード成果物種類のグループ名称を返す
	 * @return 成果物種類グループの名称
	 */
	protected String getGroup() {
		return this.group;
	}

	/**
	 * 自ノード成果物種類と判定するファイルパス正規表現パターンを追加する
	 * @param str 成果物パスパターン文字列
	 */
	protected void addPatternString(final String str) {
		if (str == null) {
			return;
		}
		this.patternStrings.add(str);
	}

	/**
	 * 自ノード成果物種類と判定するファイルパス正規表現パターンを返す
	 * @return 成果物パスパターン文字列
	 */
	protected List<String> getPatternStrings() {
		return this.patternStrings;
	}

	/**
	 * 自ノード成果物種類に定義されたパスパターンの数を返す
	 * １以上となる定義を想定する
	 * @return 成果物パスパターンの定義数
	 */
	protected int countPatternStrings() {
		return this.patternStrings.size();
	}

	/**
	 * DEBUG用Framework成果物種類定義の解析ログの文字列を作成する
	 * @return ログ文字列
	 */
	protected String debug() {
		StringBuilder sb = new StringBuilder();
		//sb.append("[DEBUG] ");
		sb.append("FwSpecificElement : name=" + this.name + " group=" + this.group);
		sb.append("\n[DEBUG] ");
		sb.append("FwSpecificElement : patternStringns=");
		for (String str : this.patternStrings) {
			sb.append(str + " , ");
		}
		//sb.append("\n");
		return sb.toString();
	}
}
