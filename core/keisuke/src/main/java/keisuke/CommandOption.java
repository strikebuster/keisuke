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

	/**
	 * オプション引数解析後の残りの引数の文字列配列を作成
	 * @return String[] オプション解析後の残った引数の配列
	 */
	String[] makeRestArgArray();

	/**
	 * 使用方法メッセージ
	 */
    void showUsage();

    /**
	 * オプション名に対する値の選択肢を返す
	 * @param optname オプション名
	 * @return 選択肢インスタンス
	 */
    OptionValues valuesAs(String optname);

}
