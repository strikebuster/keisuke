package keisuke.count.diff;

import static keisuke.count.diff.renderer.RendererConstant.MSG_DIFF_RND_PREFIX;
import static keisuke.count.option.CountOptionConstant.*;
import static keisuke.report.property.MessageConstant.MSG_DIFF_STATUS_PREFIX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import keisuke.count.AbstractCountMainProc;
import keisuke.count.FormatEnum;
import keisuke.count.Formatter;
import keisuke.count.PathStyleEnum;
import keisuke.count.SortOrderEnum;
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
		super();
		this.setCommandOption(new DiffCountOption());
	}

	/** {@inheritDoc} */
	protected void setDefaultFormat() {
		// DiffCountのformatオプションのデフォルトはTEXT
		this.setFormatEnum(FormatEnum.TEXT);
	}

	/** {@inheritDoc} */
	protected void setDefaultPathStyle() {
		// DiffCountのpathオプションのデフォルトはBASE
		this.setPathStyleEnum(PathStyleEnum.BASE);
	}

	/** {@inheritDoc} */
	protected void setDefaultSortOrder() {
		// DiffCountのsortオプションのデフォルトはnull
		// デフォルト値のままであればRendererFactoryがformatに応じて再設定する
		this.setSortOrderEnum(null); // 実際にはnullが設定されるのでなく変更されない
	}

	/** {@inheritDoc} */
	protected void setFileArguments() throws IllegalArgumentException {
		// 新しい版のソースの基点ディレクトリの設定
		String newdir = this.argMap().get(ARG_NEWDIR);
		if (newdir == null) {
			LogUtil.errorLog("new directory is not specified.");
			throw new IllegalArgumentException("no new directory");
		} else {
			try {
				this.validateAndSetNewDirectory(newdir);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + newdir + "' is not directory.");
				throw e;
			}
		}
		// 古い版のソースの基点ディレクトリの設定
		String olddir = this.argMap().get(ARG_OLDDIR);
		if (olddir == null) {
			LogUtil.errorLog("old directory is not specified.");
			throw new IllegalArgumentException("no old directory.");
		} else {
			try {
				this.validateAndSetOldDirectory(olddir);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + olddir + "' is not directory.");
				throw e;
			}
		}
	}

	/** {@inheritDoc} */
	protected void setOptions() throws FileNotFoundException {
		String encoding = this.argMap().get(OPT_ENCODE);
		String outfile = this.argMap().get(OPT_OUTPUT);
		String format = this.argMap().get(OPT_FORMAT);
		String path = this.argMap().get(OPT_PATH);
		String sort = this.argMap().get(OPT_SORT);
		String xmlfile = this.argMap().get(OPT_XML);
		// 対象ファイルのエンコード指定を設定
		if (encoding != null && encoding.length() > 0) {
			this.setSourceEncoding(encoding);
		}
		// 出力フォーマットの指定
		// フォーマットを設定
		if (format != null) {
			try {
				this.validateFormatOption(format);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + format + "' is invalid option value for '" + OPT_FORMAT + "'.");
				throw e;
			}
			this.setFormat(format);
		}
		// 出力のパス表記方法の指定
		if (path != null) {
			try {
				this.validatePathOption(path);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + path + "' is invalid option value for '" + OPT_PATH + "'.");
				throw e;
			}
			this.setPathStyle(path);
		}
		// ソート順の指定
		if (sort != null) {
			try {
				this.validateSortOption(sort);
			} catch (IllegalArgumentException e) {
				LogUtil.errorLog("'" + sort + "' is invalid option value for '" + OPT_SORT + "'.");
				throw e;
			}
			this.setSortOrder(sort);
		}
		// カスタマイズしたXML定義ファイル指定
		if (xmlfile != null) {
			this.setXmlFileName(xmlfile);
		}
		// 出力先の指定
		if (outfile != null && outfile.length() > 0) {
			this.setOutputStream(new FileOutputStream(new File(outfile)));
		}
	}

	private void validateAndSetNewDirectory(final String dirname) {
		File newdirFile = this.validateAndGetDirectory("new", dirname);
		this.setNewDirectory(newdirFile);
	}

	private void validateAndSetOldDirectory(final String dirname) {
		File olddirFile = this.validateAndGetDirectory("old", dirname);
		this.setOldDirectory(olddirFile);
	}

	private File validateAndGetDirectory(final String prefix, final String dirname) {
		if (dirname == null || dirname.isEmpty()) {
			throw new IllegalArgumentException(prefix + " directory is not specified.");
		}
		File dirFile = new File(dirname);
		return this.validateDirectory(prefix, dirFile);
	}

	private void validateAndSetNewDirectory(final File dirFile) {
		this.validateDirectory("new", dirFile);
		this.setNewDirectory(dirFile);
	}

	private void validateAndSetOldDirectory(final File dirFile) {
		this.validateDirectory("old", dirFile);
		this.setOldDirectory(dirFile);
	}

	private File validateDirectory(final String prefix, final File dirFile) {
		if (dirFile == null) {
			throw new IllegalArgumentException(prefix + " directory is null.");
		}
		if (!dirFile.isDirectory()) {
			throw new IllegalArgumentException(dirFile.getPath() + " is not directory.");
		}
		return dirFile;
	}

	/**
	 * 保持している２つのソースルートディレクトリに対してカウントを実行します
	 * @throws IOException 出力時に異常があると発行
	 */
	protected void executeCounting() throws IOException {
		this.msgDef = new MessageDefine(MSG_DIFF_PREFIXES);
		DiffCountFunction diffcounter = new DiffCountFunction(this.sourceEncoding(), this.xmlFileName());
		if (this.sortOrder() == SortOrderEnum.OFF) {
			// 未指定の場合はOS順で計測し、出力時は形式毎に決まる
			diffcounter.setSortingOrder(SortOrderEnum.OS);
		} else {
			diffcounter.setSortingOrder(this.sortOrder());
		}
		this.diffResult = diffcounter.countDiffBetween(this.oldSrcdir, this.srcdir);
	}

	/** {@inheritDoc} */
	protected void writeResults() throws IOException {
		// フォーマッタを設定
		Formatter<DiffFolderResult> renderer
			= RendererFactory.getFormatter(this.format(), this.msgDef, this.pathStyle(), this.sortOrder());
		this.setFormatter(renderer);
		if (renderer == null) {
			throw new RuntimeException("fail to get a formatter");
		}
		this.outputStream().write(renderer.format(this.diffResult));
		this.outputStream().flush();
		//LogUtil.debugLog("カウント結果を出力しました。");
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

	/**
	 * 計測結果データを格納しているルートオブジェクトをセットします
	 * @param result 計測結果のルートオブジェクト
	 */
	protected void setResult(final DiffFolderResult result) {
		this.diffResult = result;
	}

	/**
	 * 計測結果を格納している木構造データのルートを返す
	 * @return 木構造データのルート
	 */
	public DiffFolderResult getResultAsRawData() {
		return this.diffResult;
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
		File oldDirFile = this.validateAndGetDirectory("old", olddir);
		// 新バージョンDir
		File newDirFile = this.validateAndGetDirectory("new", newdir);
		// 計測と出力
		this.doCountingAndWriting(oldDirFile, newDirFile, output);
	}

	/**
	 * オプションや計測対象ファイル名を設定後に、差分ステップ計測と結果出力を実行する
	 * @param olddir 計測対象の旧バージョンのディレクトリ
	 * @param newdir 計測対象の新バージョンのディレクトリ
	 * @param output 結果出力先
	 * @throws IOException ファイル入出力で異常があれば発行する
	 */
	public void doCountingAndWriting(final File olddir, final File newdir,
					final OutputStream output) throws IOException {
		// 旧バージョンDir
		this.validateAndSetOldDirectory(olddir);
		// 新バージョンDir
		this.validateAndSetNewDirectory(newdir);
		// 計測と出力
		this.validatePathOption(this.pathStyleName());
		this.validateSortOption(this.sortType());
		this.executeCounting();
		if (output != null) {
			this.setOutputStream(output);
			this.validateFormatOption(this.formatType());
			this.writeResults();
		}
	}

}
