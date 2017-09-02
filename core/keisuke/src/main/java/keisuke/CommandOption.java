package keisuke;

/**
 * Interface for dealing command options.
 */
public interface CommandOption {

	/**
	 * コマンドライン引数の解析
	 * @param args コマンドライン引数
	 * @return Map<String, String> Map<オプション名, オプション値>
	 */
	ArgumentMap makeMapOfOptions(String[] args);

}
