package keisuke.report.procedure;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import keisuke.CountResult;
import keisuke.CountResultMap;

/**
 * CountResultMapインタフェースを実装するクラス
 * Map<String, CountResult>形式で設定を保持する
 */
public class CountResultMapImpl implements CountResultMap {

	private Map<String, CountResult> resultMap = null;

	protected CountResultMapImpl() {
		this.resultMap = new TreeMap<String, CountResult>();
	}

	/** {@inheritDoc} */
	public CountResult get(final String key) {
		return this.resultMap.get(key);
	}

	/** {@inheritDoc} */
	public void put(final String key, final CountResult result) {
		this.resultMap.put(key, result);
	}

	/** {@inheritDoc} */
	public boolean containsKey(final String key) {
		return this.resultMap.containsKey(key);
	}

	/** {@inheritDoc} */
	public Set<Entry<String, CountResult>> entrySet() {
		return this.resultMap.entrySet();
	}

}
