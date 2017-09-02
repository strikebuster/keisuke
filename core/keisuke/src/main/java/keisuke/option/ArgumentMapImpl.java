package keisuke.option;

import java.util.Map;

import keisuke.ArgumentMap;
import keisuke.util.ConfigMap;

/**
 * ArgumentMapインタフェースを実装するクラス
 * Map<String, String>形式でコマンド引数を保持する
 */
public class ArgumentMapImpl extends ConfigMap implements ArgumentMap {

	public ArgumentMapImpl(final Map<String, String> map) {
		super();
		this.setMap(map);
	}

}
