package keisuke.report.procedure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import keisuke.AbstractMainProc;
import keisuke.MessageMap;
import keisuke.report.Classifier;
import keisuke.report.ProcedureType;
import keisuke.report.ReportColumnMap;
import keisuke.report.ReportEditor;
import keisuke.report.classify.ClassifierFactory;
import keisuke.report.editor.ReportEditorFactory;
import keisuke.report.option.ReportOptionFactory;
import keisuke.report.property.PropertyDefine;
import keisuke.util.LogUtil;
import keisuke.util.StringUtil;

/**
 * Abstract class for main procedure of reporting command.
 */
abstract class AbstractReportMainProc extends AbstractMainProc {

	private ProcedureType proctype = null;
	private ReportEditor reportEditor = null;
	private Classifier classifyType = null;
	private String reportOutput = "";
	private ReportColumnMap columnMap = null;
	private MessageMap messageMap = null;
	private String outputFileName = null;

	protected AbstractReportMainProc() { }

	///** {@inheritDoc} */
	//public abstract void main(String[] args);

	/**
	 * create other functional objects to binded to main procedure depending on command type.
	 * @param type command process type.
	 */
	protected final void createBindedFuncs(final ProcedureType type) {
		this.proctype = type;
		this.setCommandOption(ReportOptionFactory.create(this.proctype));
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
	 * プロパティ定義ファイルからレポートカラムとメッセージを設定する
	 * @param filename プロパティ定義ファイル名
	 */
	public void setSomeFromProperties(final String filename) {
		PropertyDefine propDef = new PropertyDefine();
		if (filename != null) {
			propDef.customizePropertyDefine(filename);
		}
		if (this.proctype.equals(ProcedureType.COUNT_PROC)) {
			this.setColumnMap(propDef.getCountProperties());
		} else if (this.proctype.equals(ProcedureType.DIFF_PROC)) {
			this.setColumnMap(propDef.getDiffProperties());
		}
		this.setMessageMap(propDef.getMessageProperties());
		//this.columnMap().debugMap();
		//this.messageMap().debugMap();
	}

	/**
	 * 集計分類および分類内容を定義するXMLファイルから分類手段を設定する
	 * @param classify 集計分類の名称
	 * @param filename XMLファイル名
	 */
	public void setClassifierFromXml(final String classify, final String filename) {
		if (filename != null) {
			this.makeClassifier(classify, filename);
		} else {
			this.makeClassifier(classify);
		}
	}

	/**
	 * Write the result of this procedue into System.out or output file.
	 */
	protected void writeOutput() {
		BufferedWriter writer = null;
		try {
			if (this.outputFileName == null) {
				// 出力ファイル：標準出力
				writer = new BufferedWriter(new OutputStreamWriter(System.out));
			} else {
				// デフォルトエンコーディングでファイルに出力
				writer = new BufferedWriter(new FileWriter(this.outputFileName));
			}
			//writer.write(this.reportOutput);
			for (String line : StringUtil.splitArrayOfLinesFrom(this.reportOutput)) {
				writer.write(line);
				writer.newLine(); // システム依存の改行コード
			}
			writer.flush();
		} catch (IOException e) {
			String outname = "<System.out>";
			if (this.outputFileName != null) {
				outname = this.outputFileName;
			}
			LogUtil.errorLog("Write error : " + outname);
			throw new RuntimeException("fail to write output", e);
		} finally {
			try {
				if (this.outputFileName != null && writer != null) writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	/**
	 * 出力ファイル名を設定する
	 * @param filename 出力ファイル名
	 */
	public void setOutputFileName(final String filename) {
		this.outputFileName = filename;
	}
}
