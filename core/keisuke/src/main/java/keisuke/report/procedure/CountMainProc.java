package keisuke.report.procedure;

import static keisuke.report.option.CommandOptionConstant.*;
import static keisuke.report.property.MessageConstant.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map.Entry;

import keisuke.CountResult;
import keisuke.CountResultMap;
import keisuke.IllegalFormattedLineException;
import keisuke.ProcedureType;
import keisuke.StepCountResult;
import keisuke.report.property.PropertyDefine;

/**
 * Class of main procedure to account result of StepCount
 */
final class CountMainProc extends AbstractMainProc {

	private CountResultMap resultMap = null;
	private int ignoreFiles = 0; // 計測対象外ファイル本数

	protected CountMainProc() {
		super();
		createBindedFuncs(ProcedureType.COUNT_PROC);
	}

	@Override
	public void main(final String[] args) {
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMap() == null) {
			return;
		}
		//this.argMap().debug();
		String pfile = this.argMap().get(OPT_PROP);
		PropertyDefine propDef = new PropertyDefine();
		if (pfile != null) {
			propDef.customizePropertyDefine(pfile);
		}
		this.setColumnMap(propDef.getCountProperties());
		this.setMessageMap(propDef.getMessageProperties());
		//this.columnMap().debug();
		//this.messageMap().debug();

		String ctype = this.argMap().get(OPT_CLASS);
		if (ctype == null ||  !(ctype.equals(OPTVAL_LANGUAGE)
				|| ctype.equals(OPTVAL_LANGGROUP)
				|| ctype.startsWith(OPTVAL_FW))) {
			ctype = OPTVAL_EXTENSION;
		}
		String xfile = this.argMap().get(OPT_XML);
		if (xfile != null) {
			this.makeClassifier(ctype, xfile);
		} else {
			this.makeClassifier(ctype);
		}

		String infile = this.argMap().get(ARG_INPUT);
		//if (infile == null) {
		//	throw new RuntimeException("!! Input file is not specified.");
		//}

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
			this.resultMap.put(key, new StepCountResult(key));
		}
	}

	private void aggregateCount(final String infile) {
		BufferedReader reader = null;
		String line = null;
		try {
			// 入力ファイル：stepcounterのCSV形式出力
			if (infile == null) {
				reader = new BufferedReader(new InputStreamReader(System.in));
			} else {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(infile))));
			}
			int linectr = 0;
			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr++;
				line = line.trim();
				// 行の解析結果をStepCountResultに保持
				StepCountResult result = null;
				try {
					result = new StepCountResult(line, linectr);
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
			        StepCountResult sumResult = (StepCountResult) this.resultMap.get(classify);
			        sumResult.add(result);
			        this.resultMap.put(classify, sumResult);
			    } else {
			    	// 新しい拡張子用にキーを追加
			    	this.resultMap.put(classify, result);
			    }
			}
			//System.out.println("[DEBUG] Input Lines = " + linectr);
		} catch (IOException e) {
			System.err.println("!! Read error : " + infile);
			throw new RuntimeException(e);
		} finally {
			if (reader != null && infile != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void reportCount() {
		this.reportEditor().setColumnOrderFrom(this.columnMap());
		StringBuilder sb = new StringBuilder();
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_COUNT_SUBJECT_HEAD));
		sb.append("\n");
		// 列タイトルの出力
		String line = this.reportEditor().makeColumnTitlesLine();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for (Entry<String, CountResult> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			StepCountResult result = (StepCountResult) entry.getValue();
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			line = this.reportEditor().makeColumnValuesLineFrom(langlabel, result);
			sb.append(line);
			sb.append("\n");
		}
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_COUNT_SUBJECT_UNSUPPORT));
		sb.append("\n");
		line = this.reportEditor().makeOnlyFilesNumTitleLine();
		sb.append(line);
		sb.append("\n");
		sb.append("ALL , ");
		sb.append(this.ignoreFiles);
		sb.append("\n");
		// 出力結果の保管
		this.setReportText(sb.toString());
	}

}
