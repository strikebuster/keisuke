package keisuke;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Interface of report column map object for keisuke commands.
 */
public interface ReportColumnMap {

	/**
	 * レポートカラム定義の項目キーに対するレポートカラムを返す
	 * @param key 項目キー
	 * @return keyに対するレポートカラム定義
	 */
	ReportColumn get(String key);

	/**
	 * レポートカラム定義のエントリ数を返す
	 * @return レポートカラム定義エントリ数
	 */
	int size();

	/**
	 * レポートカラム定義内容をSetにして返す
	 * @return レポートカラム定義エントリのSet
	 */
	Set<Entry<String, ReportColumn>> entrySet();

	/**
	 * レポートカラム定義内容を保持する
	 * @param map レポートカラム定義内容を保持するMap
	 */
	void setMap(Map<String, ReportColumn> map);

	/**
	 * DEBUG用 定義内容の表示
	 */
	void debug();
}
