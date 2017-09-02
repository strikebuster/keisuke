package keisuke.report.classify.language;

import java.util.List;

/**
 * Interface of language specifics for keisuke commands.
 */
public interface LanguageSpecifics {

	/**
	 * 言語名称を返す
	 * @return 言語名称
	 */
	String getName();

	/**
	 * 言語のgroup名称を返す
	 * @return Group名称
	 */
	String getGroup();

	/**
	 * 言語のソースファイルの拡張子のリストを返す
	 * @return ファイル拡張子のList
	 */
	List<String> getExtensions();

	/**
	 * DEBUG用 言語定義の内容を表示する文字列を返す
	 * @return 言語定義内容の文字列
	 */
	String debug();
}
