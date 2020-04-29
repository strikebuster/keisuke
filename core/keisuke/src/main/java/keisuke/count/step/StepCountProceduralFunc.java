package keisuke.count.step;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import keisuke.StepCountResult;
import keisuke.count.SortOrderEnum;
import keisuke.count.StepCountResultForCount;
import keisuke.count.util.FileNameUtil;
//import keisuke.util.LogUtil;

/**
 * コマンドライン以外の呼び出し元に対して、StepCountPrcoの処理のうち計測と出力を
 * 別に呼び出せるようなAPIを提供するクラス
 */
public class StepCountProceduralFunc extends StepCountProc {

	public StepCountProceduralFunc() {	}

	/**
	 * このメソッドは計測した結果をそのままデータとして返す.
	 * クライントは取得したカウント結果の一部情報を加工した後、このクラスの別メソッド
	 * doFormattingAndWritingAbout()に対して出力整形することを想定する.
	 * @param paths カウント対象ディレクトリおよびファイルパスの配列
	 * @return カウント結果の配列
	 * @throws IOException 結果出力しないので発行は起きない
	 */
	public StepCountResult[] getResultOfCountingPaths(final String[] paths) throws IOException {
		this.doCountingAndWriting(paths, null);
		return this.getResultAsRawData();
	}

	/**
	 * このメソッドはAntタスクI/Fなどファイルセットを扱うクライアントから呼び出すことを想定している.
	 * 指定されたカウント対象ファイルパス名配列のファイル全てをカウントする.
	 * ファイルパスは指定された基点ディレクトリからの相対パスで指定される.
	 * カウント結果にはディレクトリパスは含まないファイル名とともに
	 * 指定された基点ディレクトリからの相対パスも記録する.
	 * カウント結果をListに格納して返す.
	 *
	 * @param baseDir カウント対象ファイルパスの基点ディレクトリ
	 * @param filePaths カウント対象ファイルパスの配列
	 * @return カウント結果のList
	 * @throws IllegalArgumentException フォーマット名が不正であれば発行する
	 */
	public List<StepCountResult> getResultOfCountingFileSet(final File baseDir, final String[] filePaths)
			throws IllegalArgumentException {

		StepCountFunction stepcounter = new StepCountFunction(this.sourceEncoding(), this.xmlFileName());
		stepcounter.setSortingOrder(this.sortOrder());

		List<StepCountResultForCount> list =
				stepcounter.countFileSet(baseDir, filePaths);
		if (this.isWithDirectory()) {
			String baseFullPath = null;
			try {
				baseFullPath = baseDir.getCanonicalPath().replace('\\', '/');
			} catch (IOException e) {
				throw new RuntimeException("I/O Error: " + baseDir, e);
			}
			for (StepCountResultForCount result : list) {
				// 基点ディレクトリを記録
				result.setBaseDirPath(baseFullPath);
				// 指定したディレクトリからのファイルパスに上書きします。
				result.setFilePathAsPathStyle(this.pathStyle());
				//LogUtil.debugLog(result.filePath());
			}
		} else {
			// ディレクトリ含むパスでソートされているのでファイル名のみでソートし直す
			if (this.sortOrder() == SortOrderEnum.ON) {
				Collections.sort(list, new Comparator<StepCountResult>() {
					public int compare(final StepCountResult o1, final StepCountResult o2) {
						return FileNameUtil.compareInCodeOrder(o1.filePath(), o2.filePath());
					}
				});
			} else if (this.sortOrder() == SortOrderEnum.OS) {
				Collections.sort(list, new Comparator<StepCountResult>() {
					public int compare(final StepCountResult o1, final StepCountResult o2) {
						return FileNameUtil.compareInOsOrder(o1.filePath(), o2.filePath());
					}
				});
			}
		}
		//this.setResult((StepCountResult[]) list.toArray(new StepCountResultForCount[list.size()]));
		List<StepCountResult> resultList = new ArrayList<StepCountResult>();
		resultList.addAll(list);
		return resultList;
	}

	/**
	 * このメソッドはAntタスクI/Fなど計測結果の一部情報をクライアントが書き換えてから呼び出すことを想定している.
	 * このクラスの外部で集約されたカウント結果配列を指定されたフォーマットで整形して
	 * 指定された出力先に出力する.
	 *
	 * @param results カウント結果のList
	 * @param output 出力先
	 * @param format フォーマット名
	 * @throws IllegalArgumentException フォーマット名が不正であれば発行する
	 * @throws IOException 出力で異常があれば発行する
	 */
	public void doFormattingAndWritingAbout(final List<StepCountResult> results, final OutputStream output,
			final String format) throws IllegalArgumentException, IOException {
		if (results == null || results.isEmpty()) {
			return;
		}
		this.setResult(results.toArray(new StepCountResult[results.size()]));
		this.setFormat(format);
		if (output != null) {
			this.setOutputStream(output);
			this.writeResults();
		}
	}
}
