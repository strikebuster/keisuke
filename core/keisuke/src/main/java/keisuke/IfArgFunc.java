package keisuke;

import java.util.Map;

/**
 * Interface for dealing command options.
 * @author strikebuster
 *
 */
public interface IfArgFunc {
	/**
	 * コマンドライン引数の解析
	 * @param args コマンドライン引数
	 * @return Map<String, String> Map<オプション名, オプション値>
	 */
	Map<String, String> makeMapOfArgs(String[] args);

}
