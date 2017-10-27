package keisuke.count;

import java.io.File;

import keisuke.StepCountResult;

/**
 * ソースコードファイルのステップ数計測結果の計測実行用の派生クラス
 */
public class StepCountResultForCount extends StepCountResult {

	private File file;

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

}
