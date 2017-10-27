package keisuke.count.diff;

/**
 * 差分計測結果の出力整形をするI/F
 */
public interface Renderer {

	/**
	 * 差分計測結果を出力形式に整形したバイト配列を返す
	 * @param result 差分計測結果
	 * @return 出力形式に整形したバイト配列
	 */
	byte[] render(DiffFolderResult result);

}
