package keisuke.count;

import java.io.File;

import keisuke.StepCountResult;
import keisuke.count.util.FileNameUtil;

/**
 * ソースコードファイルのステップ数計測結果の計測実行用の派生クラス
 */
public class StepCountResultForCount extends StepCountResult {

	private File file;
	private String baseDirPath = null;
	private String specifiedFilePath = null;

	public StepCountResultForCount() { }

	public StepCountResultForCount(final File target, final String name, final String type,
			final String category, final long exec, final long blanc, final long comment) {
		super(name, type, category, exec, blanc, comment);
		this.setFile(target);
	}

	private void setFile(final File target) {
		this.file = target;
	}

	/**
	 * 計測対象ファイルのインスタンスを返す
	 * @return 計測対象ファイルインスタンス
	 */
	public File file() {
		return this.file;
	}

	/**
	 * 指定された基点ディレクトリの絶対パスを設定する
	 * @param path 基点ディレクトリの絶対パス文字列
	 */
	public void setBaseDirPath(final String path) {
		this.baseDirPath = path;
	}

	/**
	 * 指定されたファイルパスを設定する
	 * 絶対パスと相対パスのいずれでもよく、指定された通りの文字列のまま設定する
	 * @param path ファイルパス名文字列
	 */
	public void setSpecifiedFilePath(final String path) {
		this.specifiedFilePath = path;
	}

	/**
	 * 計測対象ファイルの基点ディレクトリ名からの相対パス文字列を返す
	 * パスには基点ディレクトリ自身を含む
	 * @return 相対パス文字列
	 */
	public String getSubPathFromBase() {
		//ファイルパス指定がある場合はそれを優先
		if (this.specifiedFilePath != null) {
			return this.specifiedFilePath;
		}
		String path = null;
		try {
			path = this.file.getCanonicalPath().replace('\\', '/');
		} catch (Exception e) {
			return "*fail to get path*/" + this.filePath();
		}
		if (this.baseDirPath == null) {
			return path;
		}
		return FileNameUtil.getSubPathFromBottomOfBase(path, this.baseDirPath);
	}

	/**
	 * 計測対象ファイルの基点ディレクトリ名からの相対パス文字列をファイルパスに設定する
	 */
	public void setFilePathAsSubPathFromBase() {
		this.setFilePath(this.getSubPathFromBase());
	}
}
