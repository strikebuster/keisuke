package keisuke.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import keisuke.report.procedure.MatchMainProc;

/**
 * ステップ数計測結果から一部抽出(keisuke.MatchExtract)を実行するAntタスク
 */
public class MatchExtractTask extends Task {

	private String masterFileName = null;
	private String transactionFileName = null;
	private String outputFileName = null;

	/**
	 * 抽出元のステップ数計測結果ファイル名を設定する
	 * @param filename 抽出元ファイル名
	 */
	public void setMaster(final String filename) {
		this.masterFileName = filename;
	}

	/**
	 * 抽出するファイルを指定するリストのファイル名を設定する
	 * @param filename 抽出対象リストファイル名
	 */
	public void setTransaction(final String filename) {
		this.transactionFileName = filename;
	}

	/**
	 * 抽出結果を書き出す出力ファイル名を設定する
	 * @param output 出力ファイル名
	 */
	public void setOutput(final String output) {
		this.outputFileName = output;
	}

	/**
	 * 差分ステップ数計測結果集計を実行します。
	 * @see org.apache.tools.ant.Task#execute()
	 * @throws BuildException when one of some exception occured
	 */
    @Override
	public void execute() throws BuildException {
    	this.validateAttributes();
		MatchMainProc mproc = new MatchMainProc();
		mproc.setOutputFileName(this.outputFileName);
		mproc.extractFromMatching(this.masterFileName, this.transactionFileName);
    }

    /**
     * 属性値が不正でないか検証する.
     * @exception BuildException if an error occurs.
     */
    protected void validateAttributes() throws BuildException {
    	if (this.masterFileName == null) {
			throw new BuildException("master file is required!");
		}
		if (this.transactionFileName == null) {
			throw new BuildException("transaction file is required!");
		}
		if (this.outputFileName == null) {
			throw new BuildException("output is required!");
		}
    }
}
