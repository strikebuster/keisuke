package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.Serializable;

import keisuke.count.diff.DiffFileResult;

/**
 * DiffCount result which has added and deleted line steps numbers of a source code file.
 * this is serializable.
 */
public class DiffCountResultForPublish extends DiffFileResultForPublish implements Serializable {

	private static final long serialVersionUID = 3L; // since ver.2.0.0

	/**
	 * keisukeの１ファイルの差分行数計測結果からplugin用の計測結果オブジェクトを生成する
	 * パス情報をbaseDirを基点にするように記録する
	 * @param result keisukeの差分行数計測結果
	 * @param baseDir パスとして記録する際の基点ディレクトリ
	 * @param including パス表記時にbaseDirのディレクトリ名を含むか否かの切替
	 */
	public DiffCountResultForPublish(final DiffFileResult result, final File baseDir,
			final BaseDirIncludingSwitch including) {
		super(result, baseDir, including);
	}

    /**
     * Gets file name
     * @return file name
     */
	public String getFileName() {
		return super.nodeName();
	}

	/**
	 * Gets diff status of a file.
	 * @return diff status.
	 */
	public String getStatus() {
		return super.status().toString();
	}

	/**
     * Gets added line steps in a file.
     * @return line steps.
     */
	public long getAdded() {
		return super.addedSteps();
	}

	/**
     * Gets deleted line steps in a file.
     * @return line steps.
     */
	public long getDeleted() {
		return super.deletedSteps();
	}

	 /**
     * Gets values string.
     * @return values string.
     */
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(this.filePath()).append(',')
			.append(this.getStatus()).append(',')
			.append(this.getAdded()).append(',')
			.append(this.getDeleted()).append('\n');
    	return sb.toString();
    }
}
