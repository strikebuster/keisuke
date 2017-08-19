package keisuke.count;

import java.io.File;
import java.io.IOException;

import jp.sf.amateras.stepcounter.diffcount.DiffSource;

/**
 * ソースコードからコメントや空行など、Diffのカウント時に不要な部分を
 * 取り除くためのカッターのインターフェースです。
 * origin: jp.sf.amateras.stepcounter.Cutter
 */
public interface Cutter {

	/**
	 * ソースコードから不要な部分を取り除きます。
	 *
	 * @param file ソースファイル
	 * @param charset ソースのエンコード
	 * @return 不要な部分を取り除いたソースコード文字列
	 * @throws IOException ソースファイル読み取りで異常がある場合に発行
	 */
	DiffSource cut(File file, String charset) throws IOException;

	/**
	 * ファイルタイプを取得します
	 * @return ファイルタイプ
	 */
	String getFileType();

}
