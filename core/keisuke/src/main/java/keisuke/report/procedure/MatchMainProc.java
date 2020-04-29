package keisuke.report.procedure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import keisuke.report.ProcedureType;
import keisuke.util.LogUtil;

import static keisuke.report.option.ReportOptionConstant.*;
import static keisuke.util.StringUtil.LINE_SEP;

/**
 * Class of main procedure to extract the matching files from result of StepCount.
 */
public final class MatchMainProc extends AbstractReportMainProc {

	private String pathStyle = null;

	public MatchMainProc() {
		super();
		createBindedFuncs(ProcedureType.MATCH_PROC);
	}

	@Override
	public void main(final String[] args) {
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMap() == null) {
			return;
		}
		//this.argMap().debugMap();
		// propertiesの設定項目なし＝出力レポートなし

		String style = this.argMap().get(OPT_PATH);
		// 入力ファイルのパス表記スタイルの指定
		this.setPathStyle(style);

		// 入力ファイルの確認
		String mafile = this.argMap().get(ARG_MASTER);
		if (mafile == null) {
			LogUtil.errorLog("Master file is not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		String trfile = this.argMap().get(ARG_TRANSACTION);
		if (trfile == null) {
			LogUtil.errorLog("Transaction file is not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		String outfileOfArg = this.argMap().get(ARG_OUTPUT);
		String outfile = this.argMap().get(OPT_OUT);
		if (outfile == null) {
			outfile = outfileOfArg;
		} else if (outfileOfArg != null) {
			LogUtil.warningLog("ignore the argument '" + outfileOfArg
					+ "' for output, because '--out' option is given high priority.");
		}
		if (outfile != null) {
			this.setOutputFileName(outfile);
		}
		this.extractFromMatching(mafile, trfile);
	}

	/**
	 * 入力ファイルのパス表記スタイルを設定します
	 * @param style パス表記スタイル
	 */
	public void setPathStyle(final String style) {
		this.validatePathOption(style);
		if (style == null || style.isEmpty()) {
			this.pathStyle = "";
		} else {
			this.pathStyle = style;
		}
	}

	/**
	 * パス表記スタイルオプションの値としてチェックして不当な場合は例外を投げる
	 * @param style パス表記スタイル
	 * @throws IllegalArgumentException スタイル名が不正の場合に発行
	 */
	protected void validatePathOption(final String style) throws IllegalArgumentException {
		if (style == null || style.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_PATH).contains(style)) {
			LogUtil.errorLog("'" + style + "' is invalid path value.");
			throw new IllegalArgumentException(style + " is invalid path value.");
		}
	}

	public void extractFromMatching(final String mafile, final String trfile) {
		this.extractMatchedContentFrom(mafile, trfile);
		this.writeOutput();
	}

	private void extractMatchedContentFrom(final String mafile, final String trfile) {
		StringBuilder sb = new StringBuilder();
		BufferedReader readerMa = null;
		BufferedReader readerTr = null;
		String lineMa = null;
		String lineTr = null;
		try {
			// 入力マスターファイル：stepcounterのCSV形式出力
			//readerMa = new BufferedReader(new InputStreamReader(new FileInputStream(new File(mafile))));
			readerMa = new BufferedReader(new FileReader(new File(mafile)));
			// 入力トランザクションファイル：	抽出対象ファイルパスのリスト
			//readerTr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(trfile))));
			readerTr = new BufferedReader(new FileReader(new File(trfile)));
			int linectrMa = 0;
			int linectrTr = 0;
			while ((lineTr = readerTr.readLine()) != null) {
				// ファイル1行毎の処理
				linectrTr++;
				lineTr = lineTr.trim();
				// TRファイルリストから対象1行のパスを格納
				String pathTr = this.getTargetPath(lineTr);
				if (pathTr == null || pathTr.isEmpty()) {
					// 空行を無視
					LogUtil.warningLog("empty line at " + linectrTr + " in " + trfile);
					continue;
				}
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
					// MAファイルリストから対象1行のパスを格納
					pathMa = this.getTargetPath(strpath);
					if (pathMa == null || pathMa.isEmpty()) {
						// 空行を無視
						LogUtil.warningLog("illegal line at " + linectrMa + " in " + mafile);
						continue;
					}
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
			    sb.append(lineMa).append(LINE_SEP);
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
		} catch (IllegalPathStyleException e) {
			LogUtil.errorLog("Fatal error occured. no output.");
			//throw new RuntimeException(e.getMessage());
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

	private String getTargetPath(final String path) throws IllegalPathStyleException {
		if (path == null || path.isEmpty()) {
			// フォーマット不正行
			return "";
		}
		if (OPTVAL_SUB.equals(this.pathStyle)) {
			return path;
		}
		int pos = path.indexOf('/'); // 先頭の/を探す
		if (!OPTVAL_BASE.equals(this.pathStyle)) {
			// -path optionが指定されてない場合
			if (pos == 0) {
				pos = path.indexOf('/', 1); // 先頭の/を除き次の/を探す
			} else {
				LogUtil.errorLog("path style error: '/' not found at top of " + path);
				throw new IllegalPathStyleException("-path not specified, but '/' not found at top");
			}
		}
		if (pos < 0) {
			LogUtil.errorLog("path style error: '/' of base directory not found in " + path);
			throw new IllegalPathStyleException("-path " + this.pathStyle
					+ " specified, but '/' of base directory not found");
		}
		// 比較対象のパスを返す
		return path.substring(pos + 1);
	}

	/**
	 * exception for illegal path style about line of input file
	 */
	static class IllegalPathStyleException extends Exception {
		private static final long serialVersionUID = 1L;

		IllegalPathStyleException(final String msg) {
			super(msg);
		}
	}
}
