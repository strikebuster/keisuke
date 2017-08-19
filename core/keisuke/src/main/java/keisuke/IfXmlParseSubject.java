package keisuke;

import java.util.List;

/**
 * Subject defined for parsing XML node.
 * @author strikebuster
 *
 */
public interface IfXmlParseSubject {

	/**
	 * 解析対象の自ノードの定義名を返す
	 * @return 自ノードの定義名
	 */
	String getXmlNodeName();

	/*
	 * 自ノードの属性名リストを返す
	 * @return 自ノードの属性名のList
	 *
	List<String> getXmlNodeAttributes();
	*/

	/**
	 * 自ノード配下で解析対象の子ノードの定義名リストを返す
	 * @return 子ノードの定義名のList
	 */
	List<String> getXmlChildrenNames();

}
