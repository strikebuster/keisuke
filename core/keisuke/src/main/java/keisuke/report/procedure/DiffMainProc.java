package keisuke.report.procedure;

import static keisuke.report.option.ReportOptionConstant.*;
import static keisuke.report.property.MessageConstant.*;
import static keisuke.util.StringUtil.LINE_SEP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map.Entry;

import keisuke.DiffStatusEnum;
import keisuke.DiffStatusLabels;
import keisuke.DiffStatusLabelsImpl;
import keisuke.report.CountResultForReport;
import keisuke.report.CountResultForReportMap;
import keisuke.report.DiffCountResultForReport;
import keisuke.report.DiffCountResultsAssortedStatus;
import keisuke.report.IllegalFormattedLineException;
import keisuke.report.ProcedureType;
import keisuke.util.LogUtil;
import keisuke.util.PathDepthList;


/**
 * Class of main procedure to account result of DiffCount
 */
public final class DiffMainProc extends AbstractReportMainProc {

	//private String[] statusArray = null;
	private DiffStatusLabels statusText = null;
	private CountResultForReportMap resultMap = null;
	private String formatType = OPTVAL_TEXT;
	private boolean unchangeByLang = true;
	private int unchangeFiles = 0; // 変更なしファイルの合計本数
	private int ignoreFiles = 0; // 計測対象外ファイル本数
	private String aoutfile = null; // 新規ファイルリスト出力ファイル
	private String aoutText = null;
	private String moutfile = null; // 修正ファイルリスト出力ファイル
	private String moutText = null;

	public DiffMainProc() {
		super();
		this.createBindedFuncs(ProcedureType.DIFF_PROC);
	}

	@Override
	public void main(final String[] args) {
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMap() == null) {
			return;
		}
		//this.argMap().debugMap();
		this.setSomeFromProperties(this.argMap().get(OPT_PROP));

		String classify = this.argMap().get(OPT_CLASS);
		String format = this.argMap().get(OPT_FORMAT);
		String unchange = this.argMap().get(OPT_UNCHANGE);
		// 集計分類の指定
		this.setClassifierFromXml(classify, this.argMap().get(OPT_XML));
		// 入力フォーマットの指定
		this.setFormat(format);
		// 変更なしファイルの集計モードの指定
		this.setUnchangeMode(unchange);
		// 入出力ファイルの指定
		this.setAddedListFileName(this.argMap().get(OPT_AOUT));
		this.setModifiedListFileName(this.argMap().get(OPT_MOUT));
		this.setOutputFileName(this.argMap().get(OPT_OUT));
		this.aggregateFrom(this.argMap().get(ARG_INPUT));
	}

	/**
	 * 入力ファイルのフォーマットを設定します
	 * @param format 入力フォーマット
	 */
	public void setFormat(final String format) {
		this.validateFormatOption(format);
		if (format == null || format.isEmpty()) {
			this.formatType = OPTVAL_TEXT;
		} else {
			this.formatType = format;
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
			LogUtil.errorLog("'" + format + "' is invalid format value.");
			throw new IllegalArgumentException(format + " is invalid format value.");
		}
	}

	/**
	 * 変更なしファイルの集計モードを設定する
	 * @param mode 変更なしファイルの集計モード( detail | total )
	 */
	public void setUnchangeMode(final String mode) {
		this.validateUnchangeOption(mode);
		if (mode != null && mode.equals(OPTVAL_TOTAL)) {
			this.unchangeByLang = false;
		} else {
			this.unchangeByLang = true;
		}
	}

	/**
	 * 変更なしファイルの集計モードオプションの値としてチェックして不当な場合は例外を投げる
	 * @param mode 変更なしファイルの集計モード
	 * @throws IllegalArgumentException フォーマット名が不正の場合に発行
	 */
	protected void validateUnchangeOption(final String mode) throws IllegalArgumentException {
		if (mode == null || mode.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_UNCHANGE).contains(mode)) {
			LogUtil.errorLog("'" + mode + "' is invalid unchange value.");
			throw new IllegalArgumentException(mode + " is invalid unchange value.");
		}
	}

	/**
	 * 追加リストを出力するファイル名を設定する
	 * @param filename 追加リスト出力ファイル名
	 */
	public void setAddedListFileName(final String filename) {
		this.aoutfile = filename;
	}

	/**
	 * 変更リストを出力するファイル名を設定する
	 * @param filename 変更リスト出力ファイル名
	 */
	public void setModifiedListFileName(final String filename) {
		this.moutfile = filename;
	}

	/**
	 * 入力ファイルから集計した結果を出力する
	 * @param infile 入力ファイル名
	 */
	public void aggregateFrom(final String infile) {
		this.statusText = new DiffStatusLabelsImpl(this.messageMap());
		prepareResultMap();
		aggregateDiff(infile);
		reportDiff();
		writeOutputDiff();
		writeOutput();
	}

	private void prepareResultMap() {
		this.resultMap = new CountResultMapImpl();
		List<String> list = this.classifier().getClassifyFixedList();
		if (list == null || list.size() < 1) {
			return;
		}
		for (String key : list) {
			this.resultMap.put(key, new DiffCountResultsAssortedStatus(key));
		}
	}

	private void aggregateDiff(final String infile) {
		BufferedReader reader = null;
		try {
			if (infile == null) {
				reader = new BufferedReader(new InputStreamReader(System.in));
			} else {
				reader = new BufferedReader(new FileReader(infile));
			}
			int linectr = 0;
			if (this.formatType.equals(OPTVAL_TEXT)) {
				// 入力ファイル：StepCounter.DiffCountのTEXT形式出力
				String line = null;
				// 無効な冒頭の行をスキップ
				while ((line = reader.readLine()) != null) {
					// ファイル1行毎の処理
					linectr++;
					if (line.equals("--")) {
						//LogUtil.debugLog("skip lines in diffcount output = " + linectr);
						break;
					}
				}
			}
			this.aggregateDiff(reader, linectr);
		} catch (IOException e) {
			LogUtil.errorLog("Read error : " + infile);
			throw new RuntimeException(e);
		} finally {
			try {
				if (infile != null && reader != null) reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void aggregateDiff(final BufferedReader reader, final int skippedLines) throws IOException {
		if (reader == null) {
			return;
		}
		StringBuilder sbaout = new StringBuilder();
		StringBuilder sbmout = new StringBuilder();
		@SuppressWarnings("unused")
		int alinectr = 0;
		@SuppressWarnings("unused")
		int mlinectr = 0;

		@SuppressWarnings("unused")
		int linectr = skippedLines;
		String line = null;
		PathDepthList pathNodeList = new PathDepthList();

		while ((line = reader.readLine()) != null) {
			// ファイル1行毎の処理
			linectr++;
			// 行の解析結果をDiffCountResultに保持
			DiffCountResultForReport result = null;
			try {
				result = new DiffCountResultForReport(line, this.formatType);
			} catch (IllegalFormattedLineException e) {
				continue;
			}
			if (!OPTVAL_CSV.equals(this.formatType)) {
				// CSVではないテキスト形式の場合にノード名でパスリストを更新し、パスを設定する
				try {
					pathNodeList.setNodeIntoDepth(result.nodeName(), result.depth());
				} catch (Exception e) {
					LogUtil.warningLog("illegal path depth = " + line);
				}
				// TEXT形式の場合は、V.1.x互換で先頭に'/'を付与する
				StringBuilder sb = new StringBuilder();
				sb.append("/").append(pathNodeList.toString());
				result.setFilePath(sb.toString());
			}
			//LogUtil.debugLog("path = " + result.filePath() + " [" + result.statusText()
			//		+ "] (add)" + result.addedSteps() + " (del)" + result.deletedSteps());
			// ノードがファイルの場合は以下の処理実施
			if (result.isFile()) {
				// 	ファイルの処理
				// 言語（拡張子）のマッチング処理

				// 変更ステータスで仕分けて対象ファイルの処理
				DiffStatusEnum diffstat
					= this.statusText.whichDiffStatusIs(result.statusText());
				result.setDiffStatus(diffstat);
				if (diffstat == null) {
					LogUtil.warningLog("unknown status in " + line);
					continue;
				} else if (diffstat == DiffStatusEnum.ADDED) {
					if (this.aoutfile != null) {
						// 新規ファイルリスト出力ON
						// StepCounterの仕様に合わせ先頭に"/"
						// ファイル出力用文字列で今後処理もしないので改行コードをシステム用に
						sbaout.append(result.filePath()).append(LINE_SEP);
						alinectr++;
					}
				} else if (diffstat == DiffStatusEnum.MODIFIED) {
					if (this.moutfile != null) {
						// 修正ファイルリスト出力ON
						// StepCounterの仕様に合わせ先頭に"/"
						// ファイル出力用文字列で今後処理もしないので改行コードをシステム用に
						sbmout.append(result.filePath()).append(LINE_SEP);
						mlinectr++;
					}
				} else if (diffstat == DiffStatusEnum.DROPED) {
					@SuppressWarnings("unused")
					int nop = 0; // 処理なし
				} else if (diffstat == DiffStatusEnum.UNCHANGED) { // 変更なし
					this.unchangeFiles++;
					//LogUtil.debugLog("Unchange: " + line);
					// 変更なしファイルを言語別集計しないならループ内スキップ
					if (!this.unchangeByLang) {
						//LogUtil.debugLog(
						//		"Unchange files not to be classified");
						continue;
					}
				} else if (diffstat == DiffStatusEnum.UNSUPPORTED) { // サポート対象外
					this.ignoreFiles++;
					//LogUtil.debugLog("Unsupport: " + line);
					// サポート対象外ファイルをループ内スキップ
					continue;
				} else {
					LogUtil.warningLog("unknown status in " + line);
					continue;
				}

				// 分類キーの取得
				String classify = this.classifier().getClassifyName(result.filePath());
				result.setClassify(classify);
				// 分類キーでの集計
				DiffCountResultsAssortedStatus resultsAssorted;
				if (!this.resultMap.containsKey(classify)) {
					// 新しい分類種別のキーを追加
					resultsAssorted = new DiffCountResultsAssortedStatus(classify);
				} else {
					resultsAssorted
					= (DiffCountResultsAssortedStatus) this.resultMap.get(classify);
				}
				resultsAssorted.add(result);
				this.resultMap.put(classify, resultsAssorted);
			}
		}
		this.aoutText = sbaout.toString();
		this.moutText = sbmout.toString();
		//LogUtil.debugLog("Input Lines = " + linectr);
		//LogUtil.debugLog("AOut Lines = " + alinectr);
		//LogUtil.debugLog("MOut Lines = " + mlinectr);
	}

	private void reportDiff() {
		this.reportEditor().setColumnOrderFrom(this.columnMap());
		StringBuilder sb = new StringBuilder();
		// 追加ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_ADD)).append(LINE_SEP);
		// 列タイトルの出力
		sb.append(this.reportEditor().makeColumnTitlesLine()).append(LINE_SEP);
		// 言語毎の集計結果の出力
		for (Entry<String, CountResultForReport> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			DiffCountResultForReport elem
				= ((DiffCountResultsAssortedStatus) entry.getValue())
						.sumOfResultFor(DiffStatusEnum.ADDED);
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			sb.append(this.reportEditor().makeColumnValuesLineFrom(langlabel, elem)).append(LINE_SEP);
		}
		// 修正ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_MODIFY)).append(LINE_SEP);
		// 列タイトルの出力
		sb.append(this.reportEditor().makeColumnTitlesLine()).append(LINE_SEP);
		// 言語毎の集計結果の出力
		for (Entry<String, CountResultForReport> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			DiffCountResultForReport elem
				= ((DiffCountResultsAssortedStatus) entry.getValue())
						.sumOfResultFor(DiffStatusEnum.MODIFIED);
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			sb.append(this.reportEditor().makeColumnValuesLineFrom(langlabel, elem)).append(LINE_SEP);
		}

		// 廃止ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_DROP)).append(LINE_SEP);
		// 列タイトルの出力
		sb.append(this.reportEditor().makeColumnTitlesLine()).append(LINE_SEP);
		// 言語毎の集計結果の出力
		for (Entry<String, CountResultForReport> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			DiffCountResultForReport elem
				= ((DiffCountResultsAssortedStatus) entry.getValue())
						.sumOfResultFor(DiffStatusEnum.DROPED);
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			sb.append(this.reportEditor().makeColumnValuesLineFrom(langlabel, elem)).append(LINE_SEP);
		}

		// 修正なしファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_UNCHANGE)).append(LINE_SEP);
		// 列タイトルの出力
		sb.append(this.reportEditor().makeOnlyFilesNumTitleLine()).append(LINE_SEP);
		// オプションで出力レベルを指定されている
		if (this.unchangeByLang) {
			// 言語毎の集計結果の出力 -nochange detail
			for (Entry<String, CountResultForReport> entry : this.resultMap.entrySet()) {
				String langkey = entry.getKey();
				DiffCountResultForReport elem
					= ((DiffCountResultsAssortedStatus) entry.getValue())
							.sumOfResultFor(DiffStatusEnum.UNCHANGED);
				// 列毎の値を出力
				String langlabel = this.classifier().getClassifyNameForReport(langkey);
				sb.append(this.reportEditor().makeOnlyFilesNumColumnValueLineFrom(langlabel, elem))
					.append(LINE_SEP);
			}
		} else {
			// 言語区別なしで合計の出力 -nochange total
			sb.append("ALL , ").append(this.unchangeFiles)
				//.append(" , 0 , 0 , 0")
				.append(LINE_SEP);
		}
		// 計測対象外ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_UNSUPPORT)).append(LINE_SEP);
		sb.append(this.reportEditor().makeOnlyFilesNumTitleLine()).append(LINE_SEP);
		sb.append("ALL , ").append(this.ignoreFiles).append(LINE_SEP);
		// 出力結果の保管
		this.setReportText(sb.toString());
	}

	private void writeOutputDiff() {
		if (this.aoutfile != null) {
			BufferedWriter writer = null;
			try {
				// デフォルトエンコーディングでファイルに出力
				// 改行コードは既にシステム依存のもので作成済み
				writer = new BufferedWriter(new FileWriter(new File(this.aoutfile)));
				writer.write(this.aoutText);
			} catch (IOException e) {
				LogUtil.errorLog("Write error : " + this.aoutfile);
				throw new RuntimeException(e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (this.moutfile != null) {
			BufferedWriter writer = null;
			try {
				// デフォルトエンコーディングでファイルに出力
				// 改行コードは既にシステム依存のもので作成済み
				writer = new BufferedWriter(new FileWriter(new File(this.moutfile)));
				writer.write(this.moutText);
			} catch (IOException e) {
				LogUtil.errorLog("Write error : " + this.moutfile);
				throw new RuntimeException(e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
