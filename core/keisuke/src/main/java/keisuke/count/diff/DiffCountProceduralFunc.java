package keisuke.count.diff;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * コマンドライン以外の呼び出し元に対して、DiffCountPrcoの処理のうち計測と出力を
 * 別に呼び出せるようなAPIを提供するクラス
 */
public class DiffCountProceduralFunc extends DiffCountProc {

	public DiffCountProceduralFunc() {	}

	/**
	 * このメソッドはJenkinsPlugin I/Fから呼び出すことを想定している.
	 * 指定された２つのディレクトリ配下のソースコードの差分行数をカウントする.
	 *
	 * @param olddir 比較する旧版のソースコードのトップディレクトリ
	 * @param srcdir 比較する新版のソースコードのトップディレクトリ
	 * @return カウント結果の木構造データのルート
	 * @throws IOException ファイル入出力でエラーがあれば発行する
	 * @throws IllegalArgumentException フォーマット名が不正であれば発行する
	 */
	public DiffFolderResult getResultOfCountingDiff(final String olddir, final String srcdir) throws IOException {
		this.doCountingAndWriting(olddir, srcdir, null);
		return this.getResultAsRawData();
	}

	/**
	 * このメソッドはJenkinsPlugin I/Fから呼び出すことを想定している.
	 * 指定された２つのディレクトリ配下のソースコードの差分行数をカウントする.
	 *
	 * @param olddir 比較する旧版のソースコードのトップディレクトリ
	 * @param srcdir 比較する新版のソースコードのトップディレクトリ
	 * @return カウント結果の木構造データのルート
	 * @throws IOException ファイル入出力でエラーがあれば発行する
	 * @throws IllegalArgumentException フォーマット名が不正であれば発行する
	 */
	public DiffFolderResult getResultOfCountingDiff(final File olddir, final File srcdir) throws IOException {
		this.doCountingAndWriting(olddir, srcdir, null);
		return this.getResultAsRawData();
	}

	/**
	 * このメソッドはJenkinsPlugin I/Fから呼び出すことを想定している.
	 * Diff計測結果を指定されたフォーマットで整形して指定された出力先に出力する.
	 *
	 * @param result DiffCount計測結果
	 * @param output 出力先
	 * @param format フォーマット名
	 * @throws IllegalArgumentException フォーマット名が不正であれば発行する
	 * @throws IOException 出力で異常があれば発行する
	 */
	public void doFormattingAndWritingAbout(final DiffFolderResult result, final OutputStream output,
			final String format) throws IllegalArgumentException, IOException {
		if (result == null) {
			return;
		}
		this.setFormat(format);
		this.setResult(result);
		if (output != null) {
			this.setOutputStream(output);
			this.writeResults();
		}
	}
}
