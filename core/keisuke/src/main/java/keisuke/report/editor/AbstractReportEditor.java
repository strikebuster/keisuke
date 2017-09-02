package keisuke.report.editor;

import java.util.Map.Entry;

import keisuke.ReportColumn;
import keisuke.ReportColumnMap;
import keisuke.CountResult;
import keisuke.ReportEditor;
import static keisuke.report.property.PropertyConstant.*;

/**
 * Abstract class to make report.
 */
abstract class AbstractReportEditor implements ReportEditor {
	private String[] columnArray;
	private String[] titleArray;
	private int indexMax = -1;
	private String[] onlyFilesNumTitleArray = new String[2];
	private String[] onlyFilesNumColumnArray = new String[2];

	protected AbstractReportEditor() { }

	/** {@inheritDoc} */
	public void setColumnOrderFrom(final ReportColumnMap map) {
		int maplen = map.size();
		this.columnArray = new String[maplen];
		this.titleArray = new String[maplen];
		int maxidx = -1;
		for (Entry<String, ReportColumn> entry : map.entrySet()) {
			// 計測結果出力用の列ラベル
			String key = entry.getKey();
			ReportColumn repcol = entry.getValue();
			int idx = repcol.getIndex();
			String title = repcol.getTitle();
			//System.out.println("[DEBUG] set report column : key=" + key
			//		+ " idx=" + idx + " title=" + title);
			if (idx >= 0) {
				this.columnArray[idx] = key;
				this.titleArray[idx] = title;
				if (idx > maxidx) {
					maxidx = idx;
				}
			}
			// 計測対象外ファイル数集計用の列ラベル
			if (key.equals(CP_LANG) || key.equals(DP_LANG)) {
				this.onlyFilesNumTitleArray[0] = title;
				this.onlyFilesNumColumnArray[0] = key;
			} else if (key.equals(CP_FILENUM) || key.equals(DP_FILENUM)) {
				this.onlyFilesNumTitleArray[1] = title;
				this.onlyFilesNumColumnArray[1] = key;
			}
		}
		this.indexMax = maxidx;
	}

	/** {@inheritDoc} */
	public String makeColumnTitlesLine() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= this.indexMax; i++) {
			String title = this.titleArray[i];
			sb.append(title);
			if (i < (this.indexMax)) {
				sb.append(" , ");
			}
		}
		return sb.toString();
	}

	/** {@inheritDoc} */
	public String makeColumnValuesLineFrom(final String lang, final CountResult elem) {
		if (elem == null) {
			System.err.println("![WARN] not instance of CountResult , but null");
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= this.indexMax; i++) {
			String key = this.columnArray[i];
			//System.out.println("[DEBUG] columnArray[" + i + "]=" + key);
			if (key == null) {
				System.err.println("![WARN] columnArray[" + i + "]=null");
				continue;
			} else if (key.equals(CP_LANG) || key.equals(DP_LANG)) {
				sb.append(lang);
			} else {
				int num = elem.getValue(key);
				sb.append(num);
			}
			if (i < (this.indexMax)) {
				sb.append(" , ");
			}
		}
		return sb.toString();
	}

	/** {@inheritDoc} */
	public String makeOnlyFilesNumTitleLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.onlyFilesNumTitleArray[0]);
		sb.append(" , ");
		sb.append(this.onlyFilesNumTitleArray[1]);
		return sb.toString();
	}

	/** {@inheritDoc} */
	public String makeOnlyFilesNumColumnValueLineFrom(final String lang, final CountResult elem) {
		if (elem == null) {
			System.err.println("![WARN] not instance of CountResult , but null");
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int len = this.onlyFilesNumColumnArray.length;
		for (int i = 0; i < len; i++) {
			String key = this.onlyFilesNumColumnArray[i];
			//System.out.println("[DEBUG] onlyFilesNumColumnArray[" + i + "]=" + key);
			if (key == null) {
				System.err.println("![WARN] onlyFilesNumColumnArray[" + i + "]=null");
				continue;
			} else if (key.equals(CP_LANG) || key.equals(DP_LANG)) {
				sb.append(lang);
			} else {
				int num = elem.getValue(key);
				sb.append(num);
			}
			if (i < len - 1) {
				sb.append(" , ");
			}
		}
		return sb.toString();
	}
}
