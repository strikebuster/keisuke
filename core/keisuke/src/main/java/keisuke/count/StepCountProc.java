package keisuke.count;

import static keisuke.count.option.CountOptionConstant.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import keisuke.StepCountResult;
import keisuke.count.format.FormatterFactory;
import keisuke.count.option.StepCountOption;
import keisuke.util.LogUtil;

/**
 * コマンドラインから引数で指定したファイルやディレクトリ配下のファイルの
 * ソースコード行数をカウントするコマンドのメイン処理クラス
 */
public class StepCountProc extends AbstractCountMainProc {

	private File[] filesArray;
	private Formatter outputFormatter = null;
	private boolean showDirectory = false;
	private StepCountResult[] resultsArray;

	public StepCountProc() {
		this.setCommandOption(new StepCountOption());
	}

	/** {@inheritDoc} */
	protected void setFileArguments() throws IllegalArgumentException {
		if (this.argArray() == null || this.argArray().length == 0) {
			LogUtil.errorLog("Not specified source path.");
			throw new IllegalArgumentException();
		}
		// 対象ファイルの設定
		List<File> fileList = new ArrayList<File>();
		for (int i = 0; i < this.argArray().length; i++) {
			fileList.add(new File(this.argArray()[i]));
		}
		this.setFiles((File[]) fileList.toArray(new File[fileList.size()]));
	}

	/** {@inheritDoc} */
	protected void setOptions() throws FileNotFoundException {
		String encoding = this.argMap().get(OPT_ENCODE);
		String outfile = this.argMap().get(OPT_OUTPUT);
		String format = this.argMap().get(OPT_FORMAT);
		String show = this.argMap().get(OPT_SHOWDIR);
		String xmlfile = this.argMap().get(OPT_XML);
		// 対象ファイルのエンコード指定を設定
		if (encoding != null) {
			this.setSourceEncoding(encoding);
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

	/** {@inheritDoc} */
	protected void executeCounting() throws IOException {
		StepCountFunction stepcounter = new StepCountFunction(this.sourceEncoding(), this.xmlFileName());
		List<StepCountResultForCount> list = stepcounter.countAll(this.filesArray);
		if (this.showDirectory) {
			for (StepCountResultForCount result : list) {
				// 差分ディレクトリ付きのファイル名に上書きします。
				result.setFilePath(this.pathFromTopOf(result.file()));
			}
		}
		Collections.sort(list, new Comparator<StepCountResult>() {
			public int compare(final StepCountResult o1, final StepCountResult o2) {
				return o1.filePath().compareTo(o2.filePath());
			}
		});
		this.resultsArray = (StepCountResult[]) list.toArray(new StepCountResultForCount[list.size()]);
	}

	/** {@inheritDoc} */
	protected void writeResults() throws IOException {
		// フォーマッタが設定されていない場合はデフォルトを使用
		if (this.outputFormatter == null) {
			this.outputFormatter = FormatterFactory.getFormatter("");
		}
		this.outputStream().write(this.outputFormatter.format(this.resultsArray));
		this.outputStream().flush();
	}

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
	private void setFormatter(final Formatter formatter) {
		this.outputFormatter = formatter;
	}

	/**
	 * ディレクトリ付きファイル名の出力形式を取得します。
	 * @param file 対象のファイルのFileインスタンス
	 * @return ディレクトリパス付きのファイルパス
	 * @throws IOException 引数ファイルの操作で異常があった場合に発行
	 */
	private String pathFromTopOf(final File file) throws IOException {
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

}
