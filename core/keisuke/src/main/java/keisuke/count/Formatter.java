package keisuke.count;

/**
 * 計測結果総称型の出力用フォーマッター
 * @param <T> 計測結果を格納するクラスであるStepCountResult[]またはDiffFolderResult
 */
public interface Formatter<T> {

	/**
	 * 計測結果を出力形式に整形したバイト配列を返す
	 * @param result 計測結果
	 * @return 出力形式に整形されたバイト配列
	 */
	byte[] format(T result);

	/**
	 * 出力データはテキストデータであるかの真偽を返す
	 * @return テキストであればtrue
	 */
	boolean isText();

	/**
	 * 出力データのテキストの文字エンコード名を返す
	 * 例）"UTF-8", "MS932"
	 * @return エンコード名
	 */
	String textEncoding();
}
