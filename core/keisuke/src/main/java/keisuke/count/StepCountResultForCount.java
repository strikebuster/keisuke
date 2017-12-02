package keisuke.count;

import java.io.File;

import keisuke.StepCountResult;
import keisuke.count.util.FileNameUtil;

/**
 * ソースコードファイルのステップ数計測結果の計測実行用の派生クラス
 */
public class StepCountResultForCount extends StepCountResult {

	private File file;
	private String baseName = null;
	private String specifiedPath = null;

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
	 * 指定された基点ディレクトリを設定する
	 * @param name 相対パスの基点ディレクトリ
	 */
	public void setBaseName(final String name) {
		this.baseName = name;
	}

	/**
	 * 指定されたファイルパスを設定する
	 * @param path ファイルパス名
	 */
	public void setSpecifiedPath(final String path) {
		this.specifiedPath = path;
	}

	/**
	 * 指定された基点ディレクトリを返す
	 * @return 相対パスの基点ディレクトリ
	 */
	public String getSubPathFromBase() {
		if (this.specifiedPath != null) {
			return this.specifiedPath;
		}
		String path = null;
		try {
			path = this.file.getCanonicalPath().replace('\\', '/');
		} catch (Exception e) {
			return "*fail to get path*/" + this.filePath();
		}
		if (this.baseName == null) {
			return path;
		}
		return FileNameUtil.getSubPathFromBottomOfBase(path, this.baseName);
	}
}
