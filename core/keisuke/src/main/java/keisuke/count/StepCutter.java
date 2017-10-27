package keisuke.count;

import java.io.File;
import java.io.IOException;

/**
 * ソースコードからコメントや空行など、Diffのカウント時に不要な部分を
 * 取り除くためのカッターのインターフェースです。
 * origin: jp.sf.amateras.stepcounter.Cutter
 */
public interface StepCutter {

	/**
	 * ソースコードから有効行以外を除去します
	 * @param file カウント対象のファイル
	 * @param charset ファイルのエンコード
	 * @return 有効行のみにカットしたソース
	 * @throws IOException ファイル読み取りで異常があれば発行
	 */
	NakedSourceCode cut(File file, String charset) throws IOException;

	/**
	 * ソースコードの言語タイプを取得します
	 * @return 言語タイプ
	 */
	String getFileType();

}
