package keisuke.util;

import java.util.Locale;

/**
 * Localeに関するユーティリティメソッドを提供します。
 */
public final class LocaleUtil {

	private LocaleUtil() { }

	/**
	 * ロケールに対応するリソースファイル名用の接尾語を返す
	 * "_"とロケール言語　例）"_ja"
	 * @return ロケール言語接尾語
	 */
	public static String getLocalePostfix() {
		Locale locale = Locale.getDefault();
		return "_" + locale.getLanguage();
	}

}
