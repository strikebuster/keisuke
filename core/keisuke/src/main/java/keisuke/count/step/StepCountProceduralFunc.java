package keisuke.count.step;

import static keisuke.count.option.CountOptionConstant.OPTVAL_SORT_ON;
import static keisuke.count.option.CountOptionConstant.OPTVAL_SORT_OS;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import keisuke.StepCountResult;
import keisuke.count.StepCountResultForCount;
import keisuke.count.util.FileNameUtil;

/**
 * コマンドライン以外の呼び出し元に対して、StepCountPrcoの処理のうち計測と出力を
 * 別に呼び出せるようなAPIを提供するクラス
 */
public class StepCountProceduralFunc extends StepCountProc {

	public StepCountProceduralFunc() {	}

	/**
	 * このメソッドはAntタスクI/Fから呼び出すことを想定している.
	 * 指定されたカウント対象ファイルパス名配列のファイル全てをカウントする.
	 * ファイルパスは指定された基点ディレクトリからの相対パスで指定される.
	 * カウント結果にはディレクトリパスは含まないファイル名とともに
	 * 指定された基点ディレクトリからの相対パスも記録する.
	 * カウント結果をListに格納して返す.
	 *
	 * @param baseDir カウント対象ファイルパスの基点ディレクトリ
	 * @param filePaths カウント対象ファイルパスの配列
	 * @param isCategory 基点ディレクトリ名をカテゴリとして記録する場合はtrueを指定する
	 * @return カウント結果のList
	 * @throws IllegalArgumentException フォーマット名が不正であれば発行する
	 */
	public List<StepCountResult> getResultOfCountingFileSet(final File baseDir, final String[] filePaths,
			final boolean isCategory) throws IllegalArgumentException {
		this.validateSortOption(this.sortOrder());
		StepCountFunction stepcounter = new StepCountFunction(this.sourceEncoding(), this.xmlFileName());
		stepcounter.setSortingOrder(this.sortOrder());

		List<StepCountResultForCount> list =
				stepcounter.countFileSet(baseDir, filePaths, this.isShowDirectory(), isCategory);
		if (this.sortOrder().equals(OPTVAL_SORT_ON)) {
			Collections.sort(list, new Comparator<StepCountResult>() {
				public int compare(final StepCountResult o1, final StepCountResult o2) {
					return FileNameUtil.compareInCodeOrder(o1.filePath(), o2.filePath());
				}
			});
		} else if (this.sortOrder().equals(OPTVAL_SORT_OS)) {
			Collections.sort(list, new Comparator<StepCountResult>() {
				public int compare(final StepCountResult o1, final StepCountResult o2) {
					return FileNameUtil.compareInOsOrder(o1.filePath(), o2.filePath());
				}
			});
		}
		//this.setResult((StepCountResult[]) list.toArray(new StepCountResultForCount[list.size()]));
		List<StepCountResult> resultList = new ArrayList<StepCountResult>();
		resultList.addAll(list);
		return resultList;
	}

	/**
	 * このメソッドはAntタスクI/Fから呼び出すことを想定している.
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
		this.validateFormatOption(format);
		this.setFormat(format);
		if (output != null) {
			this.setOutputStream(output);
			this.writeResults();
		}
	}
}
