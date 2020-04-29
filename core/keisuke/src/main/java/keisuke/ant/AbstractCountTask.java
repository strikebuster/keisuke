package keisuke.ant;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * ステップ数計測を実行するAntタスクの共通処理を持つ基底クラス
 */
public abstract class AbstractCountTask extends Task {

	private File outputFile = null;
	private String formatType = "";
	private String pathStyleName = "";
	private String sortType = "";
	private String sourceEncoding;
	private String xmlFileName = null;

	/**
	 * 出力するファイルを設定する
	 * @param file 出力ファイル
	 */
	public void setOutput(final File file) {
		this.outputFile = file;
	}

	/**
	 * 出力フォーマットを設定する
	 * @param format フォーマット
	 */
	public void setFormat(final String format) {
		this.formatType = format;
	}

	/**
	 * パス表記スタイルを設定する
	 * @param style パス表記スタイル
	 */
	public void setPath(final String style) {
		this.pathStyleName = style;
	}

	/**
	 * 出力内容に書き出すファイルリストのソート順を設定する
	 * @param order ソート順
	 */
    public void setSort(final String order) {
    	this.sortType = order;
    }

	/**
	 * ソースファイルの文字コードを設定する
	 * @param encoding 文字コード
	 */
	public void setEncoding(final String encoding) {
		this.sourceEncoding = encoding;
	}

	 /**
     * ユーザ定義のXMLファイルを設定する
     * @param filename XMLファイル名
     */
    public void setXml(final String filename) {
    	this.xmlFileName = filename;
    }

    /**
	 * 集計結果を書き出す出力ファイルを返す
	 * @return 出力ファイル
	 */
    protected File outputFile() {
    	return this.outputFile;
    }

    /**
	 * 出力フォーマットを返す
	 * @return フォーマット
	 */
    protected String formatType() {
    	return this.formatType;
    }

    /**
     * パス表記スタイルを返す
     * @return パス表記スタイル
     */
    protected String pathStyleName() {
    	return this.pathStyleName;
    }

    /**
	 * 結果出力のソート順を返す
	 * @return ソート順
	 */
    protected String sortType() {
    	return this.sortType;
    }

    /**
	 * ソースファイルの文字コードを返す
	 * @return 文字コード
	 */
    protected String sourceEncoding() {
    	return this.sourceEncoding;
    }

    /**
     * ユーザ定義のXMLファイル名を返す
     * @return XMLファイル名
     */
    protected String xmlFileName() {
    	return this.xmlFileName;
    }

    /**
     * Antプロジェクトのログ出力にMSG_INFOレベルでデバッグメッセージを書き込む
     * @param msg デバッグメッセージ
     */
    protected void debugLog(final String msg) {
    	log("[DEBUG]" +  msg + "\n", Project.MSG_INFO);
    }
}
