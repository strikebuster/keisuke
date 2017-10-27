package keisuke.report.classify.language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import keisuke.util.LogUtil;

import java.util.Map.Entry;

/**
 * LanguageElementMapインタフェースを実装するクラス
 * Map<String, LanguageElement>形式で設定を保持する
 */
public class LanguageElementMapImpl implements LanguageElementMap {

	private Map<String, LanguageElement> langMap = null;

	protected LanguageElementMapImpl() {
		this.langMap = new HashMap<String, LanguageElement>();
	}

	/** {@inheritDoc} */
	public LanguageElement get(final String key) {
		return this.langMap.get(key);
	}

	/** {@inheritDoc} */
	public void put(final String key, final LanguageElement result) {
		this.langMap.put(key, result);
	}

	/** {@inheritDoc} */
	public boolean isEmpty() {
		return this.langMap.isEmpty();
	}

	/** {@inheritDoc} */
	public boolean containsKey(final String key) {
		return this.langMap.containsKey(key);
	}

	/** {@inheritDoc} */
	public Set<Entry<String, LanguageElement>> entrySet() {
		return this.langMap.entrySet();
	}

	/** {@inheritDoc} */
	public Collection<LanguageElement> values() {
		return this.langMap.values();
	}

	/** {@inheritDoc} */
	public void mergeMap(final LanguageElementMap map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		for (Entry<String, LanguageElement> entry : map.entrySet()) {
			this.langMap.put(entry.getKey(), entry.getValue());
		}
	}

	/** {@inheritDoc} */
	public void debugMap() {
		System.out.println("[DEBUG] Language Map contains as follows");
		if (this.langMap == null || this.langMap.isEmpty()) {
			LogUtil.debugLog("map is null.");
			return;
		}
		List<String> keylist = new ArrayList<String>(this.langMap.keySet());
		Collections.sort(keylist);
		for (String key : keylist) {
			LanguageElement data = this.langMap.get(key);
			LogUtil.debugLog("MapKey ext=" + key);
			LogUtil.debugLog(data.debug());
		}
	}

}
