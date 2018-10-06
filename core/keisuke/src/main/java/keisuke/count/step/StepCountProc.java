package keisuke.count.step;

import static keisuke.count.option.CountOptionConstant.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import keisuke.StepCountResult;
import keisuke.count.AbstractCountMainProc;
import keisuke.count.Formatter;
import keisuke.count.StepCountResultForCount;
import keisuke.count.option.StepCountOption;
import keisuke.count.step.format.FormatterFactory;
import keisuke.count.util.FileNameUtil;
import keisuke.util.LogUtil;

/**
 * コマンドラインから引数で指定したファイルやディレクトリ配下のファイルの
 * ソースコード行数をカウントするコマンドのメイン処理クラス
 */
public class StepCountProc extends AbstractCountMainProc {

	private String[] pathArray;
	private boolean showDirectory = false;
	private String sortType = OPTVAL_SORT_ON;
	private StepCountResult[] resultsArray;

	public StepCountProc() {
		this.setCommandOption(new StepCountOption());
	}

	/** {@inheritDoc} */
	protected void setFileArguments() throws IllegalArgumentException {
		String[] argArray = this.commandOption().makeRestArgArray();
		try {
			this.validateFileArguments(argArray);
		} catch (IllegalArgumentException e) {
			LogUtil.errorLog("not specified source path.");
			//throw new IllegalArgumentException("short of arguments");
			throw e;
		}
		// 対象ファイルの設定
		this.setFileNames(argArray);
	}

	/** {@inheritDoc} */
	protected void setOptions() throws FileNotFoundException {
		String encoding = this.argMap().get(OPT_ENCODE);
		String outfile = this.argMap().get(OPT_OUTPUT);
		String format = this.argMap().get(OPT_FORMAT);
		String show = this.argMap().get(OPT_SHOWDIR);
		String sort = this.argMap().get(OPT_SORT);
		String xmlfile = this.argMap().get(OPT_XML);
		// 対象ファイルのエンコード指定を設定
		if (encoding != null) {
			this.setSourceEncoding(encoding);
		}
		// 出力フォーマットの指定
		if (format == null) {
			format = OPTVAL_TEXT;
		} else {
			try {
				this.validateFormatOption(format);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + format + "' is invalid option value for '" + OPT_FORMAT + "'.");
				throw e;
			}
		}
		this.setFormat(format);
		// 出力にディレクトリパスを付けるかを設定
		if ("true".equalsIgnoreCase(show)) {
			this.setShowDirectory(true);
		}
		// ソート順の指定
		if (sort == null) {
			sort = OPTVAL_SORT_ON;
		} else {
			try {
				this.validateSortOption(sort);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + sort + "' is invalid option value for '" + OPT_SORT + "'.");
				throw e;
			}
		}
		this.setSortOrder(sort);
		// カスタマイズしたXML定義ファイル指定
		if (xmlfile != null) {
			this.setXmlFileName(xmlfile);
		}
		// 出力先の指定
		if (outfile != null && outfile.length() > 0) {
			this.setOutputStream(new FileOutputStream(outfile));
		}
	}

	/**
	 * 計測対象のファイルが指定されているかチェックして存在しない場合は例外を投げる
	 * @param files ファイル名配列
	 * @throws IllegalArgumentException ファイル名が空の場合に発行
	 */
	private void validateFileArguments(final String[] files) throws IllegalArgumentException {
		if (files == null || files.length == 0) {
			throw new IllegalArgumentException("no file argument.");
		}
	}

	/**
	 * フォーマットオプションの値としてチェックして不当な場合は例外を投げる
	 * @param format フォーマット名
	 * @throws IllegalArgumentException フォーマット名が不正の場合に発行
	 */
	protected void validateFormatOption(final String format) throws IllegalArgumentException {
		if (format == null || format.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_FORMAT).contains(format)) {
			throw new IllegalArgumentException(format + " is invalid format value.");
		}
	}

	/**
	 * ソートオプションの値としてチェックして不当な場合は例外を投げる
	 * @param sort ソート順
	 * @throws IllegalArgumentException ソート順が不正の場合に発行
	 */
	protected void validateSortOption(final String sort) throws IllegalArgumentException {
		if (sort == null || sort.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_SORT).contains(sort)) {
			throw new IllegalArgumentException(sort + " is invalid sort value.");
		}
	}

	/** {@inheritDoc} */
	protected void executeCounting() throws IOException {
		StepCountFunction stepcounter = new StepCountFunction(this.sourceEncoding(), this.xmlFileName());
		stepcounter.setSortingOrder(this.sortType);

		List<StepCountResultForCount> list = stepcounter.countAll(this.pathArray);
		if (this.showDirectory) {
			for (StepCountResultForCount result : list) {
				// 指定したディレクトリからのファイルパスに上書きします。
				result.setFilePath(result.getSubPathFromBase());
			}
		}
		if (this.sortType.equals(OPTVAL_SORT_ON)) {
			Collections.sort(list, new Comparator<StepCountResult>() {
				public int compare(final StepCountResult o1, final StepCountResult o2) {
					return FileNameUtil.compareInCodeOrder(o1.filePath(), o2.filePath());
				}
			});
		} else if (this.sortType.equals(OPTVAL_SORT_OS)) {
			Collections.sort(list, new Comparator<StepCountResult>() {
				public int compare(final StepCountResult o1, final StepCountResult o2) {
					return FileNameUtil.compareInOsOrder(o1.filePath(), o2.filePath());
				}
			});
		}
		this.resultsArray = (StepCountResult[]) list.toArray(new StepCountResultForCount[list.size()]);
	}

	/** {@inheritDoc} */
	protected void writeResults() throws IOException {
		// フォーマッタを設定
		Formatter<StepCountResult[]> formatter = FormatterFactory.getFormatter(this.format());
		this.setFormatter(formatter);
		if (formatter == null) {
			throw new RuntimeException("fail to get a formatter");
		}
		this.outputStream().write(formatter.format(this.resultsArray));
		this.outputStream().flush();
	}

	/**
	 * 引数で指定したディレクトリからの階層を表示するか設定します
	 * @param show ファイル名にディレクトリパス付ける場合はtrue
	 */
	public void setShowDirectory(final boolean show) {
		this.showDirectory = show;
	}

	/**
	 * 出力するファイル名にディレクトリパスを付けるかの真偽を返す
	 * @return ファイル名にディレクトリパス付ける場合はtrue
	 */
	protected boolean isShowDirectory() {
		return this.showDirectory;
	}

	/**
	 * 結果出力のソート順をセットします
	 * @param order ソート順
	 */
	public void setSortOrder(final String order) {
		this.sortType = order;
	}

	/**
	 * 結果出力のソート順を返す
	 * @return ソート順
	 */
	protected String sortOrder() {
		return this.sortType;
	}

	/**
	 * 計測対象のファイル名配列をセットします
	 * @param filenames ファイル名配列
	 */
	private void setFileNames(final String[] filenames) {
		this.pathArray = filenames;
	}

	/**
	 * 計測結果データを格納している配列をセットします
	 * @param results 計測結果の配列
	 */
	protected void setResult(final StepCountResult[] results) {
		this.resultsArray = results;
	}

	/**
	 * 計測結果データを格納している配列を返す
	 * @return 計測結果データを格納している配列
	 */
	public StepCountResult[] getResultAsRawData() {
		return this.resultsArray;
	}

	/** {@inheritDoc} */
	public void doCountingAndWriting(final String[] filenames, final OutputStream output) throws IOException {
		this.setFileNames(filenames);
		if (output != null) {
			this.setOutputStream(output);
		}
		this.validateSortOption(this.sortType);
		this.executeCounting();
		if (output != null) {
			this.validateFormatOption(this.format());
			this.writeResults();
		}
	}

}
