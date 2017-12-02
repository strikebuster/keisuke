package keisuke.ant;

import static keisuke.report.option.ReportOptionConstant.OPTVAL_EXTENSION;
import static keisuke.report.option.ReportOptionConstant.OPT_CLASS;

import org.apache.tools.ant.BuildException;

import keisuke.report.procedure.CountMainProc;

/**
 * ステップ数計測結果集計(keisuke.CountReport)を実行するAntタスク
 */
public class CountReportTask extends AbstractReportTask {

    /**
	 * ステップ数計測結果集計を実行します。
	 * @see org.apache.tools.ant.Task#execute()
	 * @throws BuildException when one of some exception occured
	 */
    @Override
	public void execute() throws BuildException {
    	this.validateAttributes();
		CountMainProc cproc = new CountMainProc();
		cproc.setSomeFromProperties(this.propFileName());
		String ctype = this.classType();
		if (ctype == null) {
			ctype = OPTVAL_EXTENSION;
		} else if (!cproc.commandOption().valuesAs(OPT_CLASS).contains(ctype)) {
			throw new BuildException(ctype + " is invalid classify value.");
		}
		cproc.setClassifierFromXml(ctype, this.xmlFileName());
		cproc.setOutputFileName(this.outputFileName());
		cproc.aggregateFrom(this.inputFileName());
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
