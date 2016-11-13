package keisuke;

import java.util.Map;

public interface IfReportFunc {
	/** 初期化 */
	public void setColumnOrder(Map<String, ReportColumn> map);
	
	/** 出力レポートのタイトルヘッダー行の生成 */
	public String getColumnTitles();
	
	/** 出力レポートのデータ行の生成 */
	public String getColumnValues(String str, ICountElement ice);
	
	/** 出力レポートのファイル数のみのタイトルヘッダー行の生成 */
	public String getOnlyFilesNumTitles();
	
	/** 出力レポートのファイル数のみのデータ行の生成 */
	public String getOnlyFilesNumColumnValues(String str, ICountElement ice);
}
