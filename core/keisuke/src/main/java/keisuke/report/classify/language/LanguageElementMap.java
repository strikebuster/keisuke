package keisuke.report.classify.language;

import java.util.Set;
import java.util.Collection;
import java.util.Map.Entry;

/**
 * Interface of language element map object.
 * This interface extends LanguageSpecificsMap interface.
 */
public interface LanguageElementMap {

	/**
	 * 拡張子に対する言語定義を返す
	 * @param key 拡張子
	 * @return keyに対する言語定義
	 */
	LanguageElement get(String key);

	/**
	 * 拡張子とその言語定義を登録する
	 * @param key 拡張子
	 * @param element keyに対する言語定義
	 */
	void put(String key, LanguageElement element);

	/**
	 * 登録内容が空であるかチェックする
	 * @return 空ならばtrue
	 */
	boolean isEmpty();

	/**
	 * 言語定義内容をSetにして返す
	 * @return 言語定義エントリのSet
	 */
	Set<Entry<String, LanguageElement>> entrySet();

	/**
	 * 言語定義内容をColectionにして返す
	 * @return 言語定義内容のCollection
	 */
	Collection<LanguageElement> values();

	/**
	 * 渡されたLangauge定義Mapを自分のMapに上書き追加でマージする
	 * @param map Langauge定義Map
	 */
	void mergeMap(LanguageElementMap map);

	/**
	 * DEBUG用 定義内容の表示
	 */
	void debugMap();
}
