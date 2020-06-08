package keisuke.report.procedure;

import static keisuke.count.step.format.FormatConstant.*;
import static keisuke.report.option.ReportOptionConstant.*;
import static keisuke.report.property.MessageConstant.*;
import static keisuke.util.StringUtil.LINE_SEP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map.Entry;

import keisuke.report.CountResultForReport;
import keisuke.report.CountResultForReportMap;
import keisuke.report.IllegalFormattedLineException;
import keisuke.report.ProcedureType;
import keisuke.report.StepCountResultForReport;
import keisuke.util.LogUtil;

/**
 * Class of main procedure to account result of StepCount
 */
public final class CountMainProc extends AbstractReportMainProc {

	private CountResultForReportMap resultMap = null;
	private int ignoreFiles = 0; // 計測対象外ファイル本数

	public CountMainProc() {
		super();
		createBindedFuncs(ProcedureType.COUNT_PROC);
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
		this.setClassifierFromXml(classify, this.argMap().get(OPT_XML));
		this.setOutputFileName(this.argMap().get(OPT_OUT));
		this.aggregateFrom(this.argMap().get(ARG_INPUT));
	}

	/**
	 * 入力ファイルから集計した結果を出力する
	 * @param infile 入力ファイル名
	 */
	public void aggregateFrom(final String infile) {
		this.prepareResultMap();
		this.aggregateCount(infile);
		this.reportCount();
		this.writeOutput();
	}

	private void prepareResultMap() {
		this.resultMap = new CountResultMapImpl();
		List<String> list = this.classifier().getClassifyFixedList();
		if (list == null || list.size() < 1) {
			return;
		}
		for (String key : list) {
			this.resultMap.put(key, new StepCountResultForReport(key));
		}
	}

	private void aggregateCount(final String infile) {
		BufferedReader reader = null;
		String line = null;
		String unsupportedLabel = this.messageMap().get(MSG_COUNT_FMT_UNDEF);
		try {
			// 入力ファイル：stepcounterのCSV形式出力
			if (infile == null) {
				reader = new BufferedReader(new InputStreamReader(System.in));
			} else {
				reader = new BufferedReader(new FileReader(infile));
			}
			@SuppressWarnings("unused")
			int linectr = 0;
			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr++;
				line = line.trim();
				// 行の解析結果をStepCountResultに保持
				StepCountResultForReport result = null;
				try {
					result = new StepCountResultForReport(line, unsupportedLabel);
				} catch (IllegalFormattedLineException e) {
					this.ignoreFiles++;
					continue;
				}
				String strpath = result.filePath();
			    // 対象言語分類の取得
			    String classify = this.classifier().getClassifyName(strpath);
			    result.setClassify(classify);

			    // 言語種類での集計
			    if (this.resultMap.containsKey(classify)) {
			        StepCountResultForReport sumResult =
			        		(StepCountResultForReport) this.resultMap.get(classify);
			        sumResult.add(result);
			        this.resultMap.put(classify, sumResult);
			    } else {
			    	// 新しい拡張子用にキーを追加
			    	this.resultMap.put(classify, result);
			    }
			}
			//LogUtil.debugLog("Input Lines = " + linectr);
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

	private void reportCount() {
		this.reportEditor().setColumnOrderFrom(this.columnMap());
		StringBuilder sb = new StringBuilder();
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_COUNT_SUBJECT_HEAD)).append(LINE_SEP);
		// 列タイトルの出力
		sb.append(this.reportEditor().makeColumnTitlesLine()).append(LINE_SEP);
		// 言語毎の集計結果の出力
		for (Entry<String, CountResultForReport> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			StepCountResultForReport result = (StepCountResultForReport) entry.getValue();
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			sb.append(this.reportEditor().makeColumnValuesLineFrom(langlabel, result)).append(LINE_SEP);
		}
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_COUNT_SUBJECT_UNSUPPORT)).append(LINE_SEP);
		sb.append(this.reportEditor().makeOnlyFilesNumTitleLine()).append(LINE_SEP);
		sb.append("ALL , ").append(this.ignoreFiles).append(LINE_SEP);
		// 出力結果の保管
		this.setReportText(sb.toString());
	}

}
