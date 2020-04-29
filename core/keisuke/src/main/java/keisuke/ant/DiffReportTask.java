package keisuke.ant;

import static keisuke.report.option.ReportOptionConstant.OPTVAL_EXTENSION;
import static keisuke.report.option.ReportOptionConstant.OPT_CLASS;

import org.apache.tools.ant.BuildException;

import keisuke.report.procedure.DiffMainProc;

/**
 * 差分ステップ数計測結果集計(keisuke.DiffReport)を実行するAntタスク
 */
public class DiffReportTask extends AbstractReportTask {

	private String addedListFileName = null;
	private String modifiedListFileName = null;
	private String unchangeMode = null;
	private String formatType = null;

	/**
	 * 差分結果から追加されたファイルのリストを出力するファイル名を設定する
	 * @param filename 出力ファイル
	 */
	public void setAout(final String filename) {
		this.addedListFileName = filename;
	}

	/**
	 * 差分結果から変更されたファイルのリストを出力するファイル名を設定する
	 * @param filename 出力ファイル
	 */
	public void setMout(final String filename) {
		this.modifiedListFileName = filename;
	}

	/**
	 * 変更なしファイル本数を分類毎に集計するdetailモードと
	 * 分類せずに集計するtotalモードのいずれかを設定する
	 * @param mode detailかtotal
	 */
	public void setUnchange(final String mode) {
		this.unchangeMode = mode;
	}

	/**
	 * 入力ファイルのフォーマット形式がtextかcsvのいずれであるかを設定する
	 * @param format textかcsv
	 */
	public void setFormat(final String format) {
		this.formatType = format;
	}

	/**
	 * 差分ステップ数計測結果集計を実行します。
	 * @see org.apache.tools.ant.Task#execute()
	 * @throws BuildException when one of some exception occured
	 */
    @Override
	public void execute() throws BuildException {
    	this.validateAttributes();
		DiffMainProc dproc = new DiffMainProc();
		dproc.setSomeFromProperties(this.propFileName());
		String ctype = this.classType();
		if (ctype == null) {
			ctype = OPTVAL_EXTENSION;
		} else if (!dproc.commandOption().valuesAs(OPT_CLASS).contains(ctype)) {
			throw new BuildException(ctype + " is invalid classify value.");
		}
		dproc.setClassifierFromXml(ctype, this.xmlFileName());
		dproc.setFormat(this.formatType);
		dproc.setAddedListFileName(this.addedListFileName);
		dproc.setModifiedListFileName(this.modifiedListFileName);
		dproc.setUnchangeMode(this.unchangeMode);
		dproc.setOutputFileName(this.outputFileName());
		dproc.aggregateFrom(this.inputFileName());
    }

    /**
     * 属性値が不正でないか検証する.
     * @exception BuildException if an error occurs.
     */
    protected void validateAttributes() throws BuildException {
    	if (this.inputFileName() == null) {
			throw new BuildException("input is required!");
		}
		if (this.outputFileName() == null) {
			throw new BuildException("output is required!");
		}
    }
}
