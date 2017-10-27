package keisuke.report.procedure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import keisuke.report.ProcedureType;
import keisuke.util.LogUtil;

import static keisuke.report.option.ReportOptionConstant.*;

/**
 * Class of main procedure to extract the matching files from result of StepCount.
 */
final class MatchMainProc extends AbstractMainProc {

	protected MatchMainProc() {
		super();
		createBindedFuncs(ProcedureType.MATCH_PROC);
	}

	@Override
	public void main(final String[] args) {
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMapEntity() == null) {
			return;
		}
		//this.argMap().debugMap();
		// propertiesの設定項目なし＝出力レポートなし
		// 入力ファイルの確認
		String mafile = this.argMapEntity().get(ARG_MASTER);
		if (mafile == null) {
			throw new RuntimeException("!! Master file is not specified.");
		}
		String trfile = this.argMapEntity().get(ARG_TRANSACTION);
		if (trfile == null) {
			throw new RuntimeException("!! Transaction file is not specified.");
		}
		String outfile = this.argMapEntity().get(ARG_OUTPUT);
		extractMatch(mafile, trfile);
		writeOutput(outfile);
	}

	private void extractMatch(final String mafile, final String trfile) {
		StringBuilder sb = new StringBuilder();
		BufferedReader readerMa = null;
		BufferedReader readerTr = null;
		String lineMa = null;
		String lineTr = null;
		try {
			// 入力マスターファイル：stepcounterのCSV形式出力
			readerMa = new BufferedReader(new InputStreamReader(new FileInputStream(new File(mafile))));
			// 入力トランザクションファイル：	抽出対象ファイルパスのリスト
			readerTr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(trfile))));
			int linectrMa = 0;
			int linectrTr = 0;
			while ((lineTr = readerTr.readLine()) != null) {
				// ファイル1行毎の処理
				linectrTr++;
				lineTr = lineTr.trim();
				if (lineTr.length() == 0) {
					// フォーマット不正行
					LogUtil.warningLog("empty line at " + linectrTr + " in " + trfile);
			        continue;
				}
				int pos = lineTr.indexOf('/', 1); // 先頭の/を除き次の/を探す
				if (pos < 0) {
					// フォーマット不正行
					LogUtil.warningLog("'/' not found in " + lineTr);
			        continue;
				}
				// TRファイルリストから対象1行のパスを格納
				String pathTr = lineTr.substring(pos);
			    //LogUtil.debugLog("TR_FILE#" + linectrTr + ": " + pathTr);
			    // MAファイルリストの読み込みを進める
			    String pathMa = null;
			    while ((lineMa = readerMa.readLine()) != null) {
			    	// ファイル1行毎の処理
					linectrMa++;
					lineMa = lineMa.trim();
					// 列要素分解
					String[] strArray = lineMa.split(",");
					String strpath = strArray[0];
					if (strpath.length() == 0) {
						// フォーマット不正行
						LogUtil.warningLog("illegal line at " + linectrMa + " in " + mafile);
				        continue;
					}
					int pos2 = strpath.indexOf('/', 1); // 先頭の/を除き次の/を探す
					if (pos2 < 0) {
						// フォーマット不正行
						LogUtil.warningLog("'/' not found in " + strpath);
				        continue;
					}
					// MAファイルリストから対象1行のパスを格納
					pathMa = strpath.substring(pos2);
					//LogUtil.debugLog("MA_FILE#" + linectrMa + ": " + pathMa);
					if (pathTr.equals(pathMa)) {
						break;
					}
					pathMa = null;
			    }
			    if (pathMa == null) {
			    	// MAファイルが終了＝一致しなかった
			    	LogUtil.errorLog("it is not found in master : " + pathTr);
			    	break;
			    }
			    sb.append(lineMa);
			    sb.append("\n");
			}
			if (lineTr == null) {
				// 正常にTRファイル処理終了
				this.setReportText(sb.toString());
			} else {
				// TR処理途中で異常終了
				LogUtil.errorLog("Fatal error occured. no output.");
			}
			//LogUtil.debugLog("read TR lines = " + linectrTr);
			//LogUtil.debugLog("read MA lines = " + linectrMa);
		} catch (IOException e) {
			LogUtil.errorLog("Read error : " + mafile + " or " + trfile);
			throw new RuntimeException(e);
		} finally {
			if (readerMa != null) {
				try {
					readerMa.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (readerTr != null) {
				try {
					readerTr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void writeOutput(final String outfile) {
		if (outfile == null) {
			super.writeOutput();
		} else {
			BufferedWriter writer = null;
			try {
				// 出力ファイル
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File(outfile))));
				writer.write(this.reportText());
			} catch (IOException e) {
				LogUtil.errorLog("Write error : " + outfile);
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
