package keisuke;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Abstract class for main procedure of command.
 * @author strikebuster
 *
 */
abstract class AbstractMainProc implements IfMainProc {
	private String proctype = "";
	private IfArgFunc argFunc = null;
	private IfReportFunc reportFunc = null;
	private IfClassifyFunc classifyType = null;
	private String reportOutput = "";
	private Map<String, String> argMap = null;
	private Map<String, ReportColumn> columnMap = null;
	private Map<String, String> messageMap = null;

	protected AbstractMainProc() { }

	/** {@inheritDoc} */
	public abstract void main(String[] args);

	/**
	 * create other functional objects to binded to main procedure depending on command type.
	 * @param type command process type.
	 */
	protected final void createBindedFuncs(final String type) {
		this.proctype = type;
		this.argFunc = ArgFuncFactory.createArgFunc(this.proctype);
		this.reportFunc = ReportFuncFactory.createReportFunc(this.proctype);
	}

	/**
	 * Get command type of this procedure
	 * @return Command type of this procedure
	 */
	protected String proctype() {
		return this.proctype;
	}

	/**
	 * Write the result of this procedue into System.out
	 */
	protected void writeOutput() {
		BufferedWriter writer = null;
		try {
			// 出力ファイル：標準出力
			//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fname))));
			writer = new BufferedWriter(new OutputStreamWriter(System.out));
			writer.write(this.reportOutput);
			writer.flush();
		} catch (IOException e) {
			System.err.println("!! Write error : <System.out>");
			throw new RuntimeException(e);
		//} finally {
			//if (writer != null) try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}

	/**
	 * コマンドオプション処理のインスタンスを返す
	 * @return commandOption for main procedure.
	 */
	protected IfArgFunc commandOption() {
		return this.argFunc;
	}

	/**
	 * 集計分類タイプのインスタンスを設定する
	 * @param cltype classifier to amount
	 */
	protected void setClassifier(final IfClassifyFunc cltype) {
		this.classifyType = cltype;
	}

	/**
	 * 集計分類タイプのインスタンスを返す
	 * @return classifier to amount
	 */
	protected IfClassifyFunc classifier() {
		return this.classifyType;
	}

	/**
	 * 集計結果レポートの編集インスタンスを返す
	 * @return reportEditor for main procedure.
	 */
	protected IfReportFunc reportEditor() {
		return this.reportFunc;
	}

	/**
	 * 出力結果テキストを設定する
	 * @param report result of main procedure.
	 */
	protected void setReportText(final String report) {
		if (report == null) {
			return;
		}
		this.reportOutput = report;
	}

	/**
	 * 出力結果テキストを返す
	 * @return result of main procedure.
	 */
	protected String reportText() {
		return this.reportOutput;
	}

	/**
	 * 引数解析結果のマップを設定する
	 * @param map 引数解析結果のマップ
	 */
	protected void setArgMap(final Map<String, String> map) {
		this.argMap = map;
	}

	/**
	 * 引数解析結果のマップを返す
	 * @return 引数解析結果のマップ
	 */
	protected Map<String, String> argMap() {
		return this.argMap;
	}

	/**
	 * レポート用カラムタイトルのマップを設定する
	 * @param map レポート用カラムタイトルのマップ
	 */
	protected void setColumnMap(final Map<String, ReportColumn> map) {
		this.columnMap = map;
	}

	/**
	 * レポート用カラムタイトルのマップを返す
	 * @return レポート用カラムタイトルのマップ
	 */
	protected Map<String, ReportColumn> columnMap() {
		return this.columnMap;
	}

	/**
	 * レポート用出力メッセージのマップを設定する
	 * @param map レポート用出力メッセージのマップ
	 */
	protected void setMessageMap(final Map<String, String> map) {
		this.messageMap = map;
	}

	/**
	 * レポート用出力メッセージのマップを返す
	 * @return レポート用出力メッセージのマップ
	 */
	protected Map<String, String> messageMap() {
		return this.messageMap;
	}


	/**
	 * DEBUG用コマンド引数解析結果の表示
	 */
	protected void debugArgMap() {
		debugMap(this.argMap);
	}

	/**
	 * DEBUG用出力レポートのタイトル設定結果の表示
	 */
	protected void debugColMap() {
		for (Entry<String, ReportColumn> entry : this.columnMap.entrySet()) {
			String key = entry.getKey();
			ReportColumn repcol = entry.getValue();
			int idx = repcol.getIndex();
			String title = repcol.getTitle();
			System.out.println("[DEBUG] " + key + ": [" + idx + "][" + title + "]");
		}
	}

	/**
	 * DEBUG用メッセージ設定結果の表示
	 */
	protected void debugMsgMap() {
		debugMap(this.messageMap);
	}

	/**
	 * DEBUG用Map<String,String>の表示
	 * @param map Map
	 */
	protected void debugMap(final Map<String, String> map) {
		for (Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String data = entry.getValue();
			System.out.println("[DEBUG] " + key + ": " + data);
		}
	}
}
