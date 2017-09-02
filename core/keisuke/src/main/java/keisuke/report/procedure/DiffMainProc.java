package keisuke.report.procedure;

import static keisuke.report.option.CommandOptionConstant.*;
import static keisuke.report.property.MessageConstant.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map.Entry;

import keisuke.CountResult;
import keisuke.CountResultMap;
import keisuke.DiffCountResult;
import keisuke.DiffCountResultsAssortedStatus;
import keisuke.IllegalFormattedLineException;
import keisuke.ProcedureType;
import keisuke.DiffStatusEnum;
import keisuke.DiffStatusTexts;
import keisuke.DiffStatusTextsImpl;
import keisuke.report.property.PropertyDefine;
import keisuke.util.PathDepthList;


/**
 * Class of main procedure to account result of DiffCount
 */
final class DiffMainProc extends AbstractMainProc {

	//private String[] statusArray = null;
	private DiffStatusTexts statusTexts = null;
	private CountResultMap resultMap = null;
	private boolean unchangeByLang = true;
	private int unchangeFiles = 0; // 変更なしファイルの合計本数
	private int ignoreFiles = 0; // 計測対象外ファイル本数
	private String aoutfile = null; // 新規ファイルリスト出力ファイル
	private String aoutText = null;
	private String moutfile = null; // 修正ファイルリスト出力ファイル
	private String moutText = null;

	protected DiffMainProc() {
		super();
		this.createBindedFuncs(ProcedureType.DIFF_PROC);
	}

	@Override
	public void main(final String[] args) {
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMapEntity() == null) {
			return;
		}
		//this.argMap().debug();
		String pfile = this.argMapEntity().get(OPT_PROP);
		PropertyDefine propDef = new PropertyDefine();
		if (pfile != null) {
			propDef.customizePropertyDefine(pfile);
		}
		this.setColumnMap(propDef.getDiffProperties());
		this.setMessageMap(propDef.getMessageProperties());
		//this.columnMap().debug();
		//this.messageMap().debug();

		String ctype = this.argMapEntity().get(OPT_CLASS);
		if (ctype == null ||  !(ctype.equals(OPTVAL_LANGUAGE)
				|| ctype.equals(OPTVAL_LANGGROUP)
				|| ctype.startsWith(OPTVAL_FW))) {
			ctype = OPTVAL_EXTENSION;
		}
		String xfile = this.argMapEntity().get(OPT_XML);
		if (xfile != null) {
			this.makeClassifier(ctype, xfile);
		} else {
			this.makeClassifier(ctype);
		}

		String infile = this.argMapEntity().get(ARG_INPUT);
		//if (infile == null) {
		//	throw new RuntimeException("!! Input file is not specified.");
		//}
		String aout = this.argMapEntity().get(OPT_AOUT);
		if (aout != null) {
			this.aoutfile = aout;
		}
		String mout = this.argMapEntity().get(OPT_MOUT);
		if (mout != null) {
			this.moutfile = mout;
		}
		String unch = this.argMapEntity().get(OPT_UNCHANGE);
		if (unch != null && !unch.equals(OPTVAL_DETAIL)) {
			this.unchangeByLang = false;
		}

		this.statusTexts = new DiffStatusTextsImpl(this.messageMap());
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
		StringBuilder sbaout = new StringBuilder();
		StringBuilder sbmout = new StringBuilder();
		int alinectr = 0;
		int mlinectr = 0;
		try {
			// 入力ファイル：StepCounter.DiffCountのTXT形式出力
			if (infile == null) {
				reader = new BufferedReader(new InputStreamReader(System.in));
			} else {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(infile))));
			}
			int linectr = 0;
			String line = null;
			// 無効な冒頭の行をスキップ
			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr += 1;
				if (line.equals("--")) {
					//System.out.println("[DEBUG] skip lines in diffcount output = " + linectr );
					break;
				}
			}
			PathDepthList pathNodeList = new PathDepthList();

			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr++;
				// 行の解析結果をDiffCountResultに保持
		        DiffCountResult result = null;
				try {
					result = new DiffCountResult(line, linectr);
				} catch (IllegalFormattedLineException e) {
					continue;
		        }
				// ノード名でパスリストを更新
		        try {
		        	pathNodeList.setNodeIntoDepth(result.nodeName(), result.depth());
		        } catch (Exception e) {
		        	System.out.println("![WARN] illegal path depth = " + line);
		        }
		        // ノードがファイルの場合は以下の処理実施
				if (result.isFile()) {
					// 	ファイルの処理
			        // 言語（拡張子）のマッチング処理
			        // パス文字列作成
			        String strpath = pathNodeList.toString();
			        result.setFilePath(strpath);
			        //System.out.println("[DEBUG] path = " + strpath +
			        //		" [" + stat + "] (add)" + numadd + " (del)" + numdel);

			        // 変更ステータスで仕分けて対象ファイルの処理
					DiffStatusEnum diffstat
							= this.statusTexts.whichDiffStatusIs(result.statusText());
					result.setDiffStatus(diffstat);
					if (diffstat == null) {
						System.out.println("![WARN] unknown status in " + line);
						continue;
					} else if (diffstat == DiffStatusEnum.ADDED) {
						if (this.aoutfile != null) {
							// 新規ファイルリスト出力ON
							sbaout.append("/"); //StepCounterの仕様に合わせ先頭に"/"
							sbaout.append(strpath);
							sbaout.append("\n");
							alinectr++;
						}
					} else if (diffstat == DiffStatusEnum.MODIFIED) {
						if (this.moutfile != null) {
							// 修正ファイルリスト出力ON
							sbmout.append("/"); //StepCounterの仕様に合わせ先頭に"/"
							sbmout.append(strpath);
							sbmout.append("\n");
							mlinectr++;
						}
					} else if (diffstat == DiffStatusEnum.DROPED) {
						int nop = 0; // 処理なし
					} else if (diffstat == DiffStatusEnum.UNCHANGED) { // 変更なし
						this.unchangeFiles++;
						//System.out.println("[DEBUG] Unchange: " + line);
						// 変更なしファイルを言語別集計しないならループ内スキップ
						if (!this.unchangeByLang) {
							//System.out.println(
							//		"[DEBUG] Unchange files not to be classified");
							continue;
						}
					} else if (diffstat == DiffStatusEnum.UNSUPPORTED) { // サポート対象外
						this.ignoreFiles++;
						//System.out.println("[DEBUG] Unsupport: " + line);
						// サポート対象外ファイルをループ内スキップ
						continue;
					} else {
						System.out.println("![WARN] unknown status in " + line);
						continue;
					}

					// 分類キーの取得
			        String classify = this.classifier().getClassifyName(strpath);
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
			//System.out.println("[DEBUG] Input Lines = " + linectr);
			//System.out.println("[DEBUG] AOut Lines = " + alinectr);
			//System.out.println("[DEBUG] MOut Lines = " + mlinectr);
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

	private void reportDiff() {
		this.reportEditor().setColumnOrderFrom(this.columnMap());
		StringBuilder sb = new StringBuilder();
		String line;
		// 追加ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_ADD));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportEditor().makeColumnTitlesLine();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for (Entry<String, CountResult> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			DiffCountResult elem
				= ((DiffCountResultsAssortedStatus) entry.getValue())
						.sumOfResultFor(DiffStatusEnum.ADDED);
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			line = this.reportEditor().makeColumnValuesLineFrom(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}
		// 修正ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_MODIFY));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportEditor().makeColumnTitlesLine();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for (Entry<String, CountResult> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			DiffCountResult elem
				= ((DiffCountResultsAssortedStatus) entry.getValue())
						.sumOfResultFor(DiffStatusEnum.MODIFIED);
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			line = this.reportEditor().makeColumnValuesLineFrom(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}

		// 廃止ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_DROP));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportEditor().makeColumnTitlesLine();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for (Entry<String, CountResult> entry : this.resultMap.entrySet()) {
			String langkey = entry.getKey();
			DiffCountResult elem
				= ((DiffCountResultsAssortedStatus) entry.getValue())
						.sumOfResultFor(DiffStatusEnum.DROPED);
			// 列毎の値を出力
			String langlabel = this.classifier().getClassifyNameForReport(langkey);
			line = this.reportEditor().makeColumnValuesLineFrom(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}

		// 修正なしファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_UNCHANGE));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportEditor().makeOnlyFilesNumTitleLine();
		sb.append(line);
		sb.append("\n");
		// オプションで出力レベルを指定されている
		if (this.unchangeByLang) {
			// 言語毎の集計結果の出力 -nochange detail
			for (Entry<String, CountResult> entry : this.resultMap.entrySet()) {
				String langkey = entry.getKey();
				DiffCountResult elem
					= ((DiffCountResultsAssortedStatus) entry.getValue())
							.sumOfResultFor(DiffStatusEnum.UNCHANGED);
				// 列毎の値を出力
				String langlabel = this.classifier().getClassifyNameForReport(langkey);
				line = this.reportEditor().makeOnlyFilesNumColumnValueLineFrom(langlabel, elem);
				sb.append(line);
				sb.append("\n");
			}
		} else {
			// 言語区別なしで合計の出力 -nochange total
			sb.append("ALL , ");
			sb.append(this.unchangeFiles);
			//sb.append(" , 0 , 0 , 0\n");
			sb.append("\n");
		}
		// 計測対象外ファイル
		// 出力グループの表題
		sb.append(this.messageMap().get(MSG_DIFF_SUBJECT_UNSUPPORT));
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

	private void writeOutputDiff() {
		if (this.aoutfile != null) {
			BufferedWriter writer = null;
			try {
				// 出力ファイル
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File(this.aoutfile))));
				writer.write(this.aoutText);
			} catch (IOException e) {
				System.err.println("!! Write error : " + this.aoutfile);
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
				// 出力ファイル
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File(this.moutfile))));
				writer.write(this.moutText);
			} catch (IOException e) {
				System.err.println("!! Write error : " + this.moutfile);
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
