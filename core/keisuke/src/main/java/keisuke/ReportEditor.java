package keisuke;

/**
 * Interface for making report to output.
 */
public interface ReportEditor {
	/**
	 * 出力レポートの列の並び順を設定
	 * @param map column key and column properties
	 */
	void setColumnOrderFrom(ReportColumnMap map);

	/**
	 * 出力レポートのタイトルヘッダー行の生成
	 * @return String titles header line
	 */
	String makeColumnTitlesLine();

	/**
	 * 出力レポートのデータ行の生成
	 * @param str key of amounting
	 * @param elem amounting result
	 * @return String values line
	 */
	String makeColumnValuesLineFrom(String str, CountResult elem);

	/**
	 * 出力レポートのファイル数のみのタイトルヘッダー行の生成
	 * @return String title header line for number of files
	 */
	String makeOnlyFilesNumTitleLine();

	/**
	 * 出力レポートのファイル数のみのデータ行の生成
	 * @param str key of amounting
	 * @param elem amounting result
	 * @return String value line for number of files
	 */
	String makeOnlyFilesNumColumnValueLineFrom(String str, CountResult elem);
}
