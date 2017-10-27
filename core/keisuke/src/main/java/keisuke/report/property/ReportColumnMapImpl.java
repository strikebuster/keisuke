package keisuke.report.property;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import keisuke.report.ReportColumn;
import keisuke.report.ReportColumnMap;
import keisuke.util.LogUtil;

/**
 * ReportColumnMapインタフェースを実装するクラス
 * Map<String, ReportColumn>形式で設定を保持する
 */
public class ReportColumnMapImpl implements ReportColumnMap {

	private Map<String, ReportColumn> columnMap = null;

	protected ReportColumnMapImpl(final Map<String, ReportColumn> map) {
		this.setMap(map);
	}

	/** {@inheritDoc} */
	public ReportColumn get(final String key) {
		return this.columnMap.get(key);
	}

	/** {@inheritDoc} */
	public int size() {
		return this.columnMap.size();
	}

	/** {@inheritDoc} */
	public Set<Entry<String, ReportColumn>> entrySet() {
		return this.columnMap.entrySet();
	}

	/** {@inheritDoc} */
	public void setMap(final Map<String, ReportColumn> map) {
		this.columnMap = map;
	}

	/** {@inheritDoc} */
	public void debugMap() {
		for (Entry<String, ReportColumn> entry : this.columnMap.entrySet()) {
			String key = entry.getKey();
			ReportColumn repcol = entry.getValue();
			int idx = repcol.getIndex();
			String title = repcol.getTitle();
			LogUtil.debugLog(key + ": [" + idx + "][" + title + "]");
		}
	}

}
