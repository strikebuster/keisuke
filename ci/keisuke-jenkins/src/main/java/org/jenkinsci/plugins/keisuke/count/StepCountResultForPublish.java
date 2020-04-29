package org.jenkinsci.plugins.keisuke.count;

import java.io.Serializable;

import org.jenkinsci.plugins.keisuke.util.RelativePathUtil;

import keisuke.StepCountResult;

/**
 * StepCount result which has some line steps numbers of a source code file.
 * this is serializable.
 */
public class StepCountResultForPublish extends StepCountResult implements Serializable {

	private static final long serialVersionUID = 5L; // since ver.2.0.0

	private String parentDirPath = "";
	private String fileName = "";

	/**
	 * keisukeの１ファイルの行数計測結果からplugin用の計測結果オブジェクトを生成する
	 * パス情報をbaseDirを基点にするように記録する
	 * @param result keisukeの行数計測結果
	 */
	public StepCountResultForPublish(final StepCountResult result) {
		super(result.filePath(), result.sourceType(), result.sourceCategory(),
				result.execSteps(), result.blancSteps(), result.commentSteps());
		String[] divPath = RelativePathUtil.dividePath(super.filePath());
    	if (divPath != null) {
    		this.parentDirPath = divPath[0];
    		this.fileName = divPath[1];
    	}
	}

    /**
     * Gets runable code line steps in a file.
     * @return line steps
     */
    public long getCodes() {
        return super.execSteps();
    }

    /**
     * Gets comment line steps in a file.
     * @return line steps
     */
    public long getComments() {
        return super.commentSteps();
    }

    /**
     * Gets blank line steps in a file.
     * @return line steps
     */
    public long getBlanks() {
        return super.blancSteps();
    }

    /**
     * Gets all line steps in a file.
     * @return line steps
     */
    public long getSum() {
        return super.sumSteps();
    }

    /**
     * Gets type of a file
     * @return type of a file
     */
    public String getFileType() {
        return super.sourceType();
    }

    /**
     * Gets name of a file.
     * @return name of a file
     */
    public String getFileName() {
    	return this.fileName;
    }

    /**
     * Gets parent directory path of a file.
     * @return parent directory path.
     */
    public String getDirectoryPath() {
    	return this.parentDirPath;
    }

    /**
     * Gets values string.
     * @return values string.
     */
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(this.filePath()).append(',')
			.append(this.getFileType()).append(',')
			.append(this.sourceCategory()).append(',')
			.append(this.getCodes()).append(',')
			.append(this.getBlanks()).append(',')
			.append(this.getComments()).append(',')
			.append(this.getSum()).append('\n');
    	return sb.toString();
    }
}
