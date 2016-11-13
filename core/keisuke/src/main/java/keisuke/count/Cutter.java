package keisuke.count;

import java.io.File;
import java.io.IOException;

import jp.sf.amateras.stepcounter.diffcount.DiffSource;


/**
 * ソースコードからコメントや空行など、
 * Diffのカウント時に不要な部分を取り除くためのカッターのインターフェースです。
 * 
 * keisuke:オリジナルと引数を変更し、Counterインターフェースと合わせた
 *
 */
public interface Cutter {

	/**
	 * ソースコードから不要な部分を取り除きます。
	 *
	 * @param file ソースファイル
	 * @param charset ソースのエンコード
	 * @return 不要な部分を取り除いたソースコード文字列
	 */
	
	public DiffSource cut(File file, String charset) throws IOException;

	/** ファイルタイプを取得します */
	public String getFileType();

}
