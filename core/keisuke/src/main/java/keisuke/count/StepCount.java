package keisuke.count;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.Util;
import jp.sf.amateras.stepcounter.format.ResultFormatter;
import keisuke.ArgumentMap;
import keisuke.count.format.FormatterFactory;


/**
 * コマンドラインから引数で指定したファイルやディレクトリ配下のファイルの
 * ソースコード行数をカウントする
 * origin: a part of jp.sf.amateras.stepcounter.Main
 */
public class StepCount {

	private File[] filesArray;
	private ResultFormatter outputFormatter = null;
	private OutputStream outputStream = System.out;
	private boolean showDirectory = false;
	private String xmlFileName = null;
	private ArgumentMap argMap = null;
	private CountResult[] resultsArray;

	/**
	 * 引数で指定したディレクトリからの階層を表示するか設定します
	 * @param show ファイル名にディレクトリパス付ける場合はtrue
	 */
	private void setShowDirectory(final boolean show) {
		this.showDirectory = show;
	}

	/**
	 * カウント対象ファイルをセットします
	 * @param files 対象Fileの配列
	 */
	private void setFiles(final File[] files) {
		this.filesArray = files;
	}

	/**
	 * 結果出力用のフォーマッタをセットします
	 * @param formatter 出力フォーマッター
	 */
	private void setFormatter(final ResultFormatter formatter) {
		this.outputFormatter = formatter;
	}

	/**
	 * 言語定義XMLファイル名をセット
	 * @param name XMLファイル名
	 */
	private void setXmlFileName(final String name) {
		this.xmlFileName = name;
	}

	/**
	 * 出力ストリームを設定します
	 * @param output 出力ストリーム
	 */
	private void setOutputStream(final OutputStream output) {
		this.outputStream = output;
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
	 * StepCountの主処理メソッド
	 * コマンドライン引数の処理、カウント処理、出力処理を呼び出す
	 * @param args 引数配列
	 */
	protected void countProc(final String[] args) {
		// 引数処理
		// オプション解析
		StepCountCommandOption argFunc = new StepCountCommandOption();
		this.argMap = argFunc.makeMapOfOptions(args);
		if (this.argMap == null) {
			return;
		}
		// 引数で指定されたカウント対象を設定
		String[] argArray = argFunc.makeRestArgArray();
		if (argArray == null || argArray.length == 0) {
			System.err.println("!! Not specified source path.");
			argFunc.showUsage();
			return;
		}
		// 対象ファイルの設定
		List<File> fileList = new ArrayList<File>();
		for (int i = 0; i < argArray.length; i++) {
			fileList.add(new File(argArray[i]));
		}
		this.setFiles((File[]) fileList.toArray(new File[fileList.size()]));

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
	 * 引数オプション解析Mapの設定内容からインスタンス変数の値を設定する
	 * @throws FileNotFoundException 出力先ファイルに異常があると発行
	 */
	private void setOptions() throws FileNotFoundException {
		String encoding = this.argMap.get(SCCommonDefine.OPT_ENCODE);
		String outfile = this.argMap.get(SCCommonDefine.OPT_OUTPUT);
		String format = this.argMap.get(SCCommonDefine.OPT_FORMAT);
		String show = this.argMap.get(SCCommonDefine.OPT_SHOWDIR);
		String xmlfile = this.argMap.get(SCCommonDefine.OPT_XML);
		// 対象ファイルのエンコード指定を設定
		if (encoding != null) {
			Util.setFileEncoding(encoding);
		}
		// 出力フォーマットの指定
		this.setFormatter(FormatterFactory.getFormatter(format));
		// 出力にディレクトリパスを付けるかを設定
		if ("true".equalsIgnoreCase(show)) {
			this.setShowDirectory(true);
		}
		// カスタマイズしたXML定義ファイル指定
		if (xmlfile != null) {
			this.setXmlFileName(xmlfile);
		}
		// 出力先の指定
		if (outfile != null && outfile.length() > 0) {
			this.setOutputStream(new FileOutputStream(outfile));
		}
	}

	/**
	 * 保持しているFile配列に対してカウントを実行します
	 * @throws IOException 出力時に異常があると発行
	 */
	private void executeCounting() throws IOException {
		//keisuke: results生成の主処理はStepCountFiles.countAll()として独立化
		StepCountFiles stepcounter = new StepCountFiles(this.xmlFileName);
		stepcounter.countEveryNodes(this.filesArray);
		List<CountResult> list = stepcounter.getResults();
		if (this.showDirectory) {
			for (CountResult result : list) {
				// 差分ディレクトリ付きのファイル名に上書きします。
				result.setFileName(getFileNameWithDir(result.getFile()));
			}
		}
		Collections.sort(list, new PathComparator());
		this.resultsArray = (CountResult[]) list.toArray(new CountResult[list.size()]);
	}

	/**
	 * カウント結果を指定された出力先へ出力フォーマットに変換して書き出す
	 * @throws IOException 出力時に異常があると発行
	 */
	private void writeResults() throws IOException {
		// フォーマッタが設定されていない場合はデフォルトを使用
		if (this.outputFormatter == null) {
			this.outputFormatter = FormatterFactory.getFormatter("");
		}
		this.outputStream.write(this.outputFormatter.format(this.resultsArray));
		this.outputStream.flush();
	}

	/**
	 * ディレクトリ付きファイル名の出力形式を取得します。
	 * @param file 対象のファイルのFileインスタンス
	 * @return ディレクトリパス付きのファイルパス
	 * @throws IOException 引数ファイルの操作で異常があった場合に発行
	 */
	private String getFileNameWithDir(final File file) throws IOException {
		if (file.isDirectory()) {
			return file.getName();
		}
		if (this.filesArray == null || this.filesArray.length == 0) {
			return file.getName();
		}
		// ファイルの正規パスを取得します。
		String filePath = file.getCanonicalPath();
		for (File f : this.filesArray) {
			String parentPath = f.getCanonicalPath();
			if (filePath.contains(parentPath)) {
				// 引数の正規パスにファイルが含まれている場合、
				// 選択されたディレクトリからのパスとファイル名を返却します。
				StringBuilder sb = new StringBuilder();
				sb.append('/');
				sb.append(f.getName());
				sb.append(filePath.substring(parentPath.length()).replaceAll("\\\\", "/"));
				return sb.toString();
			}
		}
		return file.getName();
	}

	/**
	 *コマンドライン起動用メソッド
	 * @param args コマンドライン引数配列
	 */
	public static void main(final String[] args) {

		if (args == null || args.length == 0) {
			System.exit(0);
		}
		StepCount myself = new StepCount();
		myself.countProc(args);
	}

	/**
	 * ファイル名リストをソートするための比較メソッド実装クラス
	 */
	static final class PathComparator implements Comparator<CountResult> {
		public int compare(final CountResult o1, final CountResult o2) {
			return o1.getFileName().compareTo(o2.getFileName());
		}
	}
}
