package keisuke.count;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import keisuke.AbstractMainProc;

/**
 * Template class for main procedure of counting command.
 */
public abstract class AbstractCountMainProc extends AbstractMainProc {

	private OutputStream outputStream = System.out;
	private String srcEncoding = System.getProperty("file.encoding");
	private String xmlFileName = null;
	private String formatType = "";

	protected AbstractCountMainProc() { }

	/**
	 * StepCount/DiffCountの主処理メソッド
	 * コマンドライン引数の処理、カウント処理、出力処理を呼び出す
	 * @param args 引数配列
	 */
	public void main(final String[] args) {
		// 引数処理
		// オプション解析
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMap() == null) {
			return;
		}
		// 引数で指定されたカウント対象を設定
		try {
			this.setFileArguments();
		} catch (IllegalArgumentException ex) {
			this.commandOption().showUsage();
			throw ex;
		}

		try {
			// 引数のオプション指定を設定
			this.setOptions();
			//カウント処理
			this.executeCounting();
			// 出力処理
			this.writeResults();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("Output file error.", fnfe);
		} catch (IOException ioe) {
			throw new RuntimeException("Count error.", ioe);
		} finally {
			if (this.outputStream != null && this.outputStream != System.out) {
				try {
					this.outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 計測対象のファイルまたはディレクトリを引数から設定する
	 * @throws IllegalArgumentException 引数指定に異常があると発行
	 */
	protected abstract void setFileArguments() throws IllegalArgumentException;

	/**
	 * 引数オプション解析Mapの設定内容からインスタンス変数の値を設定する
	 * @throws FileNotFoundException 出力先ファイルに異常があると発行
	 */
	protected abstract void setOptions() throws FileNotFoundException;

	/**
	 * 保持しているFile配列に対してカウントを実行します
	 * @throws IOException 出力時に異常があると発行
	 */
	protected abstract void executeCounting() throws IOException;

	/**
	 * カウント結果を指定された出力先へ出力フォーマットに変換して書き出す
	 * @throws IOException 出力時に異常があると発行
	 */
	protected abstract void writeResults() throws IOException;

	/**
	 * ソースファイルのエンコード名をセット
	 * @param encoding エンコード名
	 */
	public void setSourceEncoding(final String encoding) {
		this.srcEncoding = encoding;
	}

	/**
	 * 設定されたソースファイルのエンコード名を返す
	 * @return エンコード名
	 */
	protected String sourceEncoding() {
		return this.srcEncoding;
	}

	/**
	 * 言語定義XMLファイル名をセット
	 * @param name XMLファイル名
	 */
	public void setXmlFileName(final String name) {
		this.xmlFileName = name;
	}

	/**
	 * 設定された言語定義XMLファイル名を返す
	 * @return XMLファイル名
	 */
	protected String xmlFileName() {
		return this.xmlFileName;
	}

	/**
	 * 結果出力のフォーマットを設定します
	 * @param format 出力フォーマット
	 */
	public void setFormat(final String format) {
		this.formatType = format;
	}

	/**
	 * 結果出力のフォーマットを返す
	 * @return format 出力フォーマット
	 */
	protected String format() {
		return this.formatType;
	}

	/**
	 * 出力ストリームを設定します
	 * @param output 出力ストリーム
	 */
	protected void setOutputStream(final OutputStream output) {
		this.outputStream = output;
	}

	/**
	 * 設定された出力ストリームを返す
	 * @return 出力ストリーム
	 */
	protected OutputStream outputStream() {
		return this.outputStream;
	}

	/**
	 * オプションや計測対象ファイル名を設定後に、ステップ計測と結果出力を実行する
	 * @param filenames 計測対象ファイル名配列
	 * @param output 結果出力先
	 * @throws IOException ファイル入出力で異常があれば発行する
	 */
	public abstract void doCountingAndWriting(String[] filenames, OutputStream output)
			throws IOException;
}
