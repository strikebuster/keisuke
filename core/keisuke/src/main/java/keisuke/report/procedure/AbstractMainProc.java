package keisuke.report.procedure;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import keisuke.ArgumentMap;
import keisuke.CommandOption;
import keisuke.MainProcedure;
import keisuke.MessageMap;
import keisuke.report.Classifier;
import keisuke.report.ProcedureType;
import keisuke.report.ReportColumnMap;
import keisuke.report.ReportEditor;
import keisuke.report.classify.ClassifierFactory;
import keisuke.report.editor.ReportEditorFactory;
import keisuke.report.option.ReportOptionFactory;
import keisuke.util.LogUtil;

/**
 * Abstract class for main procedure of command.
 */
abstract class AbstractMainProc implements MainProcedure {
	private ProcedureType proctype = null;
	private CommandOption commandOption = null;
	private ReportEditor reportEditor = null;
	private Classifier classifyType = null;
	private String reportOutput = "";
	private ArgumentMap argMap = null;
	private ReportColumnMap columnMap = null;
	private MessageMap messageMap = null;

	protected AbstractMainProc() { }

	/** {@inheritDoc} */
	public abstract void main(String[] args);

	/**
	 * create other functional objects to binded to main procedure depending on command type.
	 * @param type command process type.
	 */
	protected final void createBindedFuncs(final ProcedureType type) {
		this.proctype = type;
		this.commandOption = ReportOptionFactory.create(this.proctype);
		this.reportEditor = ReportEditorFactory.create(this.proctype);
	}

	/**
	 * Get command type of this procedure
	 * @return Command type of this procedure
	 */
	protected ProcedureType proctype() {
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
			LogUtil.errorLog("Write error : <System.out>");
			throw new RuntimeException(e);
		//} finally {
			//if (writer != null) try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}

	/**
	 * コマンドオプション処理のインスタンスを返す
	 * @return commandOption for main procedure.
	 */
	protected CommandOption commandOption() {
		return this.commandOption;
	}

	/**
	 * 集計分類タイプのインスタンスを生成して設定する
	 * @param classtype 集計分類タイプを指定する文字列
	 */
	protected void makeClassifier(final String classtype) {
		this.classifyType = new ClassifierFactory().create(classtype);
	}

	/**
	 * 集計分類タイプのインスタンスを生成して設定する
	 * @param classtype 集計分類タイプを指定する文字列
	 * @param xname 集計分類タイプの定義用のXMLファイル名
	 */
	protected void makeClassifier(final String classtype, final String xname) {
		this.classifyType = new ClassifierFactory().create(classtype, xname);
	}
	/**
	 * 集計分類タイプのインスタンスを返す
	 * @return classifier to amount
	 */
	protected Classifier classifier() {
		return this.classifyType;
	}

	/**
	 * 集計結果レポートの編集インスタンスを返す
	 * @return reportEditor for main procedure.
	 */
	protected ReportEditor reportEditor() {
		return this.reportEditor;
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
	protected void setArgMap(final ArgumentMap map) {
		this.argMap = map;
	}

	/**
	 * 引数解析結果のマップを返す
	 * @return 引数解析結果のマップ
	 */
	protected ArgumentMap argMap() {
		return this.argMap;
	}

	/**
	 * 引数解析結果のマップを返す
	 * @return 引数解析結果のマップの実体Map
	 */
	protected Map<String, String> argMapEntity() {
		if (this.argMap == null) {
			return null;
		}
		return this.argMap.getMap();
	}

	/**
	 * レポート用カラムタイトルのマップを設定する
	 * @param map レポート用カラムタイトルのマップ
	 */
	protected void setColumnMap(final ReportColumnMap map) {
		this.columnMap = map;
	}

	/**
	 * レポート用カラムタイトルのマップを返す
	 * @return レポート用カラムタイトルのマップ
	 */
	protected ReportColumnMap columnMap() {
		return this.columnMap;
	}

	/**
	 * レポート用出力メッセージのマップを設定する
	 * @param map レポート用出力メッセージのマップ
	 */
	protected void setMessageMap(final MessageMap map) {
		this.messageMap = map;
	}

	/**
	 * レポート用出力メッセージのマップを返す
	 * @return レポート用出力メッセージのマップ
	 */
	protected MessageMap messageMap() {
		return this.messageMap;
	}

}
