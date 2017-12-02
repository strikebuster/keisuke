package keisuke.ant;

import org.apache.tools.ant.Task;

/**
 * ステップ数計測結果集計を実行するAntタスクの共通処理を持つ基底クラス
 */
public abstract class AbstractReportTask extends Task {

	private String inputFileName = null;
	private String outputFileName = null;
	private String classType = null;
	private String propFileName = null;
	private String xmlFileName = null;

	/**
	 * 集計元のステップ数計測結果ファイル名を設定する
	 * @param input 入力ファイル名
	 */
	public void setInput(final String input) {
		this.inputFileName = input;
	}

	/**
	 * 集計結果を書き出す出力ファイル名を設定する
	 * @param output 出力ファイル名
	 */
	public void setOutput(final String output) {
		this.outputFileName = output;
	}

	/**
	 * 集計分類のタイプを設定する
	 * @param classify 集計分類のタイプ名
	 */
	public void setClassify(final String classify) {
		this.classType = classify;
	}

	/**
     * ユーザ定義のプロパティファイル名を設定する
     * @param filename プロパティファイル名
     */
	public void setProperties(final String filename) {
		this.propFileName = filename;
	}

	/**
     * ユーザ定義のXMLファイル名を設定する
     * @param filename XMLファイル名
     */
    public void setXml(final String filename) {
    	this.xmlFileName = filename;
    }

    /**
     * 集計元のステップ数計測結果ファイル名を返す
     * @return 入力ファイル名
     */
    protected String inputFileName() {
    	return this.inputFileName;
    }

    /**
	 * 集計結果を書き出す出力ファイル名を返す
	 * @return 出力ファイル名
	 */
    protected String outputFileName() {
    	return this.outputFileName;
    }

    /**
	 * 集計分類のタイプを返す
	 * @return 集計分類のタイプ名
	 */
    protected String classType() {
    	return this.classType;
    }

    /**
     * ユーザ定義のプロパティファイル名を返す
     * @return プロパティファイル名
     */
    protected String propFileName() {
    	return this.propFileName;
    }

    /**
     * ユーザ定義のXMLファイル名を返す
     * @return XMLファイル名
     */
    protected String xmlFileName() {
    	return this.xmlFileName;
    }
}
