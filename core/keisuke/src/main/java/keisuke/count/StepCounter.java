package keisuke.count;

import java.io.File;
import java.io.IOException;

/**
 * ソースコードのステップ数計測オブジェクトのインタフェース
 */
public interface StepCounter {

	/**
	 * ソースコードファイルのステップ数を計測する
	 * @param file ソースコードファイル
	 * @param charset ソースのエンコード指定
	 * @return 有効行・空白行・コメント行のカウント結果
	 * @throws IOException ファイル読み取りで異常があれば発行
	 */
	StepCountResultForCount count(File file, String charset) throws IOException;

	/**
	 * ソースコードの言語タイプを取得します
	 * @return 言語タイプ
	 */
	String getFileType();
}
