package keisuke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Language element node of XML to define language.
 */
public class LanguageElement implements IfXmlParseSubject {

	private String name = null;
	private String group = null;
	private List<String> extensions = null;

	/**
	 * Languageノードに定義されている値を設定するコンストラクタ
	 * @param lang Language名称
	 * @param grp Group名称
	 */
	protected LanguageElement(final String lang, final String grp) {
		this.name = lang;
		this.group = grp;
		this.extensions = new ArrayList<String>();
	}

	/** {@inheritDoc} */
	public String getXmlNodeName() {
		return CommonDefine.XML_NODE_LANG;
	}

	/** {@inheritDoc} */
	public List<String> getXmlChildrenNames() {
		return Arrays.asList(CommonDefine.XML_NODE_EXT);
	}

	/**
	 * 自ノードに定義されているlanguage名称を返す
	 * @return Language名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 自ノードに定義されているlanguageのgroup名称を返す
	 * @return Group名称
	 */
	public String getGroup() {
		return this.group;
	}

	/**
	 * 自ノードに定義されているlanguageのソースファイルの拡張子を設定する
	 * @param ext ファイル拡張子
	 */
	protected void addExtension(final String ext) {
		String str = ext;
		if (ext == null) {
			return;
		} else if (ext.length() != 0 && !ext.startsWith(".")) {
			str = "." + ext;
		}
		this.extensions.add(str);
	}

	/**
	 * 自ノードに定義されているlanguageのソースファイルの拡張子のリストを返す
	 * @return ファイル拡張子のList
	 */
	public List<String> getExtensions() {
		return this.extensions;
	}

	/**
	 * DEBUG用解析経過のログ作成
	 * @return 解析経過のログ文字列
	 */
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
