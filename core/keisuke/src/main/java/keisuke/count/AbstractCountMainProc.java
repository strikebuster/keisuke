package keisuke.count;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import keisuke.ArgumentMap;
import keisuke.CommandOption;
import keisuke.MainProcedure;

/**
 * Abstract class for main procedure of counting command.
 */
public abstract class AbstractCountMainProc implements MainProcedure {

	private CommandOption commandOption = null;
	private String[] argArray = null;
	private OutputStream outputStream = System.out;
	private String srcEncoding = System.getProperty("file.encoding");
	private String xmlFileName = null;
	private ArgumentMap argMap = null;

	protected AbstractCountMainProc() { }

	/**
	 * StepCount/DiffCountの主処理メソッド
	 * コマンドライン引数の処理、カウント処理、出力処理を呼び出す
	 * @param args 引数配列
	 */
	public void main(final String[] args) {
		// 引数処理
		// オプション解析
		this.setArgMap(this.commandOption.makeMapOfOptions(args));
		if (this.argMap() == null) {
			return;
		}
		// 引数で指定されたカウント対象を設定
		this.argArray = this.commandOption.makeRestArgArray();
		// コマンド毎の引数処理
		try {
			this.setFileArguments();
		} catch (IllegalArgumentException e) {
			this.commandOption().showUsage();
			return;
		}

		try {
			// 引数のオプション指定を設定
			this.setOptions();
			//カウント処理
			this.executeCounting();
			// 出力処理
			this.writeResults();
		} catch (Throwable t) {
			t.printStackTrace();
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
	 * コマンドオプション処理のインスタンスを設定する
	 * @param comOpt コマンドオプション処理のインスタンス
	 */
	protected void setCommandOption(final CommandOption comOpt) {
		this.commandOption = comOpt;
	}

	/**
	 * コマンドオプション処理のインスタンスを返す
	 * @return コマンドオプション処理のインスタンス
	 */
	protected CommandOption commandOption() {
		return this.commandOption;
	}

	/**
	 * コマンドオプションを取り除いた残りの引数を格納した文字列配列を返す
	 * @return 引数を格納した文字列配列
	 */
	protected String[] argArray() {
		return this.argArray;
	}

	/**
	 * ソースファイルのエンコード名をセット
	 * @param encoding エンコード名
	 */
	protected void setSourceEncoding(final String encoding) {
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
	protected void setXmlFileName(final String name) {
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
	 * テスト用　引数解析結果のマップを返す
	 * @return 引数解析結果のマップの実体Map
	 */
	protected Map<String, String> argMapEntity() {
		if (this.argMap == null) {
			return null;
		}
		return this.argMap.getMap();
	}
}
