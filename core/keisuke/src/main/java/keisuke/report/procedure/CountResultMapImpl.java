package keisuke.report.procedure;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import keisuke.report.CountResultForReport;
import keisuke.report.CountResultForReportMap;

/**
 * CountResultMapインタフェースを実装するクラス
 * Map<String, CountResult>形式で設定を保持する
 */
public class CountResultMapImpl implements CountResultForReportMap {

	private Map<String, CountResultForReport> resultMap = null;

	protected CountResultMapImpl() {
		this.resultMap = new TreeMap<String, CountResultForReport>();
	}

	/** {@inheritDoc} */
	public CountResultForReport get(final String key) {
		return this.resultMap.get(key);
	}

	/** {@inheritDoc} */
	public void put(final String key, final CountResultForReport result) {
		this.resultMap.put(key, result);
	}

	/** {@inheritDoc} */
	public boolean containsKey(final String key) {
		return this.resultMap.containsKey(key);
	}

	/** {@inheritDoc} */
	public Set<Entry<String, CountResultForReport>> entrySet() {
		return this.resultMap.entrySet();
	}

}
