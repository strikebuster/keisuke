package keisuke;

import java.util.Map;

/**
 * Interface for making report to output.
 * @author strikebuster
 *
 */
interface IfReportFunc {
	/**
	 * 出力レポートの列の並び順を設定
	 * @param map column key and column properties
	 */
	void setColumnOrder(Map<String, ReportColumn> map);

	/**
	 * 出力レポートのタイトルヘッダー行の生成
	 * @return String titles header line
	 */
	String getColumnTitles();

	/**
	 * 出力レポートのデータ行の生成
	 * @param str key of amounting
	 * @param ice amounting result
	 * @return String values line
	 */
	String getColumnValues(String str, ICountElement ice);

	/**
	 * 出力レポートのファイル数のみのタイトルヘッダー行の生成
	 * @return String title header line for number of files
	 */
	String getOnlyFilesNumTitles();

	/**
	 * 出力レポートのファイル数のみのデータ行の生成
	 * @param str key of amounting
	 * @param ice amounting result
	 * @return String value line for number of files
	 */
	String getOnlyFilesNumColumnValues(String str, ICountElement ice);
}
