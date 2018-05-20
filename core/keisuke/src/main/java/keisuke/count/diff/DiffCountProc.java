package keisuke.count.diff;

import static keisuke.count.diff.renderer.RendererConstant.MSG_DIFF_RND_PREFIX;
import static keisuke.count.option.CountOptionConstant.*;
import static keisuke.report.property.MessageConstant.MSG_DIFF_STATUS_PREFIX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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

	public static final String[] MSG_DIFF_PREFIXES = {MSG_DIFF_STATUS_PREFIX, MSG_DIFF_RND_PREFIX};

	private File srcdir = null;
	private File oldSrcdir = null;
	private MessageDefine msgDef = null;
	private DiffFolderResult diffResult;

	public DiffCountProc() {
		this.setCommandOption(new DiffCountOption());
	}

	/** {@inheritDoc} */
	protected void setFileArguments() throws IllegalArgumentException {
		String newdir = this.argMap().get(ARG_NEWDIR);
		if (newdir == null) {
			LogUtil.errorLog("new directory is not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		File newdirFile = new File(newdir);
		if (!newdirFile.isDirectory()) {
			LogUtil.errorLog("'" + newdirFile.getAbsolutePath() + "' is not directory.");
			throw new IllegalArgumentException("not directory");
		}
		String olddir = this.argMap().get(ARG_OLDDIR);
		if (olddir == null) {
			LogUtil.errorLog("old directory is not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		File olddirFile = new File(olddir);
		if (!olddirFile.isDirectory()) {
			LogUtil.errorLog("'" + olddirFile.getAbsolutePath() + "' is not directory.");
			throw new IllegalArgumentException("not directory");
		}
		this.setNewDirectory(newdirFile);
		this.setOldDirectory(olddirFile);
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
		// フォーマットを設定
		if (format == null) {
			format = OPTVAL_TEXT;
		} else if (!this.commandOption().valuesAs(OPT_FORMAT).contains(format)) {
			LogUtil.errorLog("'" + format + "' is invalid option value for '" + OPT_FORMAT + "'.");
			throw new IllegalArgumentException("invalid option value");
		}
		this.setFormat(format);
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
		this.msgDef = new MessageDefine(MSG_DIFF_PREFIXES);
		DiffCountFunction diffcounter = new DiffCountFunction(this.sourceEncoding(), this.xmlFileName());
		this.diffResult = diffcounter.countDiffBetween(this.oldSrcdir, this.srcdir);
	}

	/** {@inheritDoc} */
	protected void writeResults() throws IOException {
		Renderer renderer = RendererFactory.getRenderer(this.format(), this.msgDef);
		if (renderer == null) {
			throw new RuntimeException("fail to get a formatter");
		}
		byte[] bytes = renderer.render(this.diffResult);
		this.outputStream().write(bytes);
		this.outputStream().flush();
		//LogUtil.debugLog(outputFile.getAbsolutePath() + "にカウント結果を出力しました。");
	}

	/**
	 * 新しい版のソースを格納するディレクトリをセットします
	 * @param dir 新しい版のディレクトリ
	 */
	private void setNewDirectory(final File dir) {
		this.srcdir = dir;
	}

	/**
	 * 古い版のソースを格納するディレクトリをセットします
	 * @param dir 古い版のディレクトリ
	 */
	private void setOldDirectory(final File dir) {
		this.oldSrcdir = dir;
	}

	/** {@inheritDoc} */
	public void doCountingAndWriting(final String[] filenames, final OutputStream output) throws IOException {
		if (filenames == null || filenames.length < 2) {
			LogUtil.errorLog("old and new directories are not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		doCountingAndWriting(filenames[0], filenames[1], output);
	}
	/**
	 * オプションや計測対象ファイル名を設定後に、差分ステップ計測と結果出力を実行する
	 * @param olddir 計測対象の旧バージョンのディレクトリ
	 * @param newdir 計測対象の新バージョンのディレクトリ
	 * @param output 結果出力先
	 * @throws IOException ファイル入出力で異常があれば発行する
	 */
	public void doCountingAndWriting(final String olddir, final String newdir,
					final OutputStream output) throws IOException {
		// 旧バージョンDir
		if (olddir == null) {
			LogUtil.errorLog("old directory is not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		File olddirFile = new File(olddir);
		if (!olddirFile.isDirectory()) {
			LogUtil.errorLog("'" + olddirFile.getAbsolutePath() + "' is not directory.");
			throw new IllegalArgumentException("not directory");
		}
		this.setOldDirectory(olddirFile);
		// 新バージョンDir
		if (newdir == null) {
			LogUtil.errorLog("new directory is not specified.");
			throw new IllegalArgumentException("short of arguments");
		}
		File newdirFile = new File(newdir);
		if (!newdirFile.isDirectory()) {
			LogUtil.errorLog("'" + newdirFile.getAbsolutePath() + "' is not directory.");
			throw new IllegalArgumentException("not directory");
		}
		this.setNewDirectory(newdirFile);
		// 計測と出力
		this.setOutputStream(output);
		this.executeCounting();
		this.writeResults();
	}

	/**
	 * 計測結果を格納している木構造データのルートを返す
	 * @return 木構造データのルート
	 */
	public DiffFolderResult getResultAsRawData() {
		return this.diffResult;
	}
}
