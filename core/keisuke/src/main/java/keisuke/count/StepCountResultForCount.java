package keisuke.count;

import java.io.File;

import keisuke.StepCountResult;
import keisuke.count.util.FileNameUtil;

/**
 * ソースコードファイルのステップ数計測結果の計測実行用の派生クラス
 */
public class StepCountResultForCount extends StepCountResult {

	private static final long serialVersionUID = 1L; // since ver.2.0.0

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
	 * 計測対象ファイルのパス表記を指定されたスタイルで設定する
	 * @param style パス表記スタイル
	 */
	public void setFilePathAsPathStyle(final PathStyleEnum style) {
		if (style.equals(PathStyleEnum.NO)) {
			return;
		}
		if (this.specifiedFilePath != null) {
			//ファイルパス指定がある場合はそれを優先
			this.setFilePath(this.specifiedFilePath);
			return;
		}
		String path = null;
		try {
			path = this.file.getCanonicalPath().replace('\\', '/');
		} catch (Exception e) {
			//ファイルの絶対パスが取得できないのでエラーメッセージ付きの表記
			this.setFilePath("*fail to get path*/" + this.filePath());
			return;
		}
		if (this.baseDirPath == null) {
			//基点ディレクトリがnullのため絶対パスを設定
			this.setFilePath(path);
			return;
		}
		if (style.equals(PathStyleEnum.BASE)) {
			this.setFilePath(FileNameUtil.getSubPathFromBottomOfBase(path, this.baseDirPath));
		} else if (style.equals(PathStyleEnum.SUB)) {
			this.setFilePath(FileNameUtil.getRelativePath(path, this.baseDirPath));
		} else if (style.equals(PathStyleEnum.SHOWDIR)) {
			StringBuffer sb = new StringBuffer();
			sb.append('/').append(FileNameUtil.getSubPathFromBottomOfBase(path, this.baseDirPath));
			this.setFilePath(sb.toString());
		}
	}
}
