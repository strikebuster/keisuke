package keisuke.count;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.sf.amateras.stepcounter.Util;
import keisuke.MessageDefine;
import keisuke.count.diff.DiffCountArgFunc;
import keisuke.count.diff.DiffCounter;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.diff.DiffStatusText;
import keisuke.count.diff.renderer.Renderer;
import keisuke.count.diff.renderer.RendererFactory;

/**
 * コマンドラインから引数で指定した2つのディレクトリ配下のファイルの
 * 差分行数をカウントする
 * origin: a part of jp.sf.amateras.stepcounter.diffcount.Main
 */
public class DiffCount {

	private OutputStream outputStream = System.out;

	private File srcdir = null;
	private File olddir = null;
	private String outputFormat = null;
	//private String encoding = null;
	private String xmlFileName = null;
	private Map<String, String> argMap = null;
	private MessageDefine msgDef = null;
	private DiffFolderResult diffResult;

	/**
	 * 新しい版のソースを格納するディレクトリをセットします
	 * @param dir 新しい版のディレクトリ
	 */
	private void setNewerDirectory(final File dir) {
		this.srcdir = dir;
	}

	/**
	 * 古い版のソースを格納するディレクトリをセットします
	 * @param dir 古い版のディレクトリ
	 */
	private void setOlderDirectory(final File dir) {
		this.olddir = dir;
	}

	/**
	 * 結果出力のフォーマットを設定します
	 * @param format 出力フォーマット
	 */
	private void setOutputFormat(final String format) {
		this.outputFormat = format;
	}

	/**
	 * 出力ストリームを設定します
	 * @param output 出力ストリーム
	 */
	private void setOutputStream(final OutputStream output) {
		this.outputStream = output;
	}

	/*
	private void setEncoding(String encoding) {
		this.encoding = encoding;
	}s
	*/

	/**
	 * 言語定義XMLファイル名をセット
	 * @param name XMLファイル名
	 */
	private void setXmlFileName(final String name) {
		this.xmlFileName = name;
	}

	/**
	 * 引数解析結果のマップを返す
	 * @return 引数解析結果のマップ
	 */
	protected Map<String, String> argMap() {
		return this.argMap;
	}

	/**
	 * DiffCountの主処理メソッド
	 * コマンドライン引数の処理、カウント処理、出力処理を呼び出す
	 * @param args 引数配列
	 */
	protected void diffProc(final String[] args) {
		// 引数処理
		// オプション解析
		DiffCountArgFunc argFunc = new DiffCountArgFunc();
		this.argMap = argFunc.makeMapOfArgs(args);
		if (this.argMap == null) {
			return;
		}
		// 引数で指定されたカウント対象を設定
		String[] argArray = argFunc.makeRestArgArray();
		if (argArray == null || argArray.length != 2) {
			System.err.println("!! Just 2 directories must be specified.");
			argFunc.showUsage();
			return;
		}
		// 対象ディレクトリの設定
		List<File> fileList = Arrays.asList(new File(argArray[0]), new File(argArray[1]));
		for (int i = 0; i < fileList.size(); i++) {
			if (!fileList.get(i).isDirectory()) {
				System.err.println("!! '" + fileList.get(i).getAbsolutePath() + "' is not directory.");
				argFunc.showUsage();
				return;
			}
		}
		this.setNewerDirectory(fileList.get(0));
		this.setOlderDirectory(fileList.get(1));

		try {
			// 引数のオプション指定を設定
			this.setOptions();
			//カウント処理
			this.executeCounting();
			// 出力処理
			this.writeResult();
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
	 * 引数オプション解析Mapの設定内容からインスタンス変数の値を設定する
	 * @throws FileNotFoundException 出力先ファイルに異常があると発行
	 */
	private void setOptions() throws FileNotFoundException {
		String encoding = this.argMap.get(SCCommonDefine.OPT_ENCODE);
		String outfile = this.argMap.get(SCCommonDefine.OPT_OUTPUT);
		String format = this.argMap.get(SCCommonDefine.OPT_FORMAT);
		String xmlfile = this.argMap.get(SCCommonDefine.OPT_XML);
		// 対象ファイルのエンコード指定を設定
		if (encoding != null && encoding.length() > 0) {
			Util.setFileEncoding(encoding);
		}
		// 出力フォーマットの指定
		// フォーマッタが設定されていない場合はデフォルトを使用
		if (format == null || format.length() == 0) {
			this.setOutputFormat("text");
		} else {
			this.setOutputFormat(format);
		}
		// カスタマイズしたXML定義ファイル指定
		if (xmlfile != null) {
			this.setXmlFileName(xmlfile);
		}
		// 出力先の指定
		if (outfile != null && outfile.length() > 0) {
			this.setOutputStream(new PrintStream(new FileOutputStream(new File(outfile))));
		}
	}

	/**
	 * 保持しているFile配列に対してカウントを実行します
	 * @throws IOException 出力時に異常があると発行
	 */
	private void executeCounting() throws IOException {
		String[] prefixes = {"diff.status.", "diff.render."};
		this.msgDef = new MessageDefine(prefixes);
		DiffStatusText dstext = new DiffStatusText(this.msgDef);
		DiffCounter diffcounter = new DiffCounter(this.xmlFileName, dstext);
		this.diffResult = diffcounter.count(this.olddir, this.srcdir);
	}

	/**
	 * カウント結果を指定された出力先へ出力フォーマットに変換して書き出す
	 * @throws IOException 出力時に異常があると発行
	 */
	private void writeResult() throws IOException {
		Renderer renderer = RendererFactory.getRenderer(this.outputFormat, this.msgDef);
		if (renderer == null) {
			throw new RuntimeException(this.outputFormat + " is invalid format!");
		}
		byte[] bytes = renderer.render(this.diffResult);
		this.outputStream.write(bytes);
		this.outputStream.flush();
		//System.out.println("[DEBUG] " + outputFile.getAbsolutePath() + "にカウント結果を出力しました。");
	}

	/**
	 *コマンドライン起動用メソッド
	 * @param args コマンドライン引数配列
	 */
	public static void main(final String[] args) {
		if (args == null || args.length == 0) {
			//System.exit(0);
			return;
		}
		DiffCount main = new DiffCount();
		main.diffProc(args);
	}

}
