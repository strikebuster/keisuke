package keisuke.count.diff;

import static keisuke.count.option.CountOptionConstant.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import keisuke.DiffStatusLabelsImpl;
import keisuke.count.AbstractCountMainProc;
import keisuke.count.diff.renderer.RendererFactory;
import keisuke.count.option.DiffCountOption;
import keisuke.report.property.MessageDefine;
import keisuke.util.LogUtil;

/**
 * コマンドラインから引数で指定した2つのディレクトリ配下のファイルの
 * 差分行数をカウントするコマンドのメイン処理クラス
 */
public class DiffCountProc extends AbstractCountMainProc {

	private File srcdir = null;
	private File olddir = null;
	private String outputFormat = null;
	private MessageDefine msgDef = null;
	private DiffFolderResult diffResult;

	public DiffCountProc() {
		this.setCommandOption(new DiffCountOption());
	}

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

	/** {@inheritDoc} */
	protected void setFileArguments() throws IllegalArgumentException {
		if (this.argArray() == null || this.argArray().length != 2) {
			LogUtil.errorLog("Just 2 directories must be specified.");
			throw new IllegalArgumentException();
		}
		// 対象ディレクトリの設定
		File[] dirArray = {new File(this.argArray()[0]), new File(this.argArray()[1])};
		for (int i = 0; i < dirArray.length; i++) {
			if (!dirArray[i].isDirectory()) {
				LogUtil.errorLog("'" + dirArray[i].getAbsolutePath() + "' is not directory.");
				throw new IllegalArgumentException();
			}
		}
		this.setNewerDirectory(dirArray[0]);
		this.setOlderDirectory(dirArray[1]);
	}

	/** {@inheritDoc} */
	protected void setOptions() throws FileNotFoundException {
		String encoding = this.argMap().get(OPT_ENCODE);
		String outfile = this.argMap().get(OPT_OUTPUT);
		String format = this.argMap().get(OPT_FORMAT);
		String xmlfile = this.argMap().get(OPT_XML);
		// 対象ファイルのエンコード指定を設定
		if (encoding != null && encoding.length() > 0) {
			this.setSourceEncoding(encoding);
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
	 * 保持している２つのソースルートディレクトリに対してカウントを実行します
	 * @throws IOException 出力時に異常があると発行
	 */
	protected void executeCounting() throws IOException {
		String[] prefixes = {"diff.status.", "diff.render."};
		this.msgDef = new MessageDefine(prefixes);
		DiffCountFunction diffcounter = new DiffCountFunction(this.sourceEncoding(), this.xmlFileName(),
				new DiffStatusLabelsImpl(this.msgDef));
		this.diffResult = diffcounter.countDiffBetween(this.olddir, this.srcdir);
	}

	/** {@inheritDoc} */
	protected void writeResults() throws IOException {
		Renderer renderer = RendererFactory.getRenderer(this.outputFormat, this.msgDef);
		//if (renderer == null) {
		//	throw new RuntimeException(this.outputFormat + " is invalid format!");
		//}
		byte[] bytes = renderer.render(this.diffResult);
		this.outputStream().write(bytes);
		this.outputStream().flush();
		//LogUtil.debugLog(outputFile.getAbsolutePath() + "にカウント結果を出力しました。");
	}

}
