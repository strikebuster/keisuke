package keisuke.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.tools.ant.BuildException;

import keisuke.count.diff.DiffCountProc;

/**
 * 差分ステップ数計測(keisuke.count.DiffCount)を実行するAntタスク
 */
public class DiffCountTask extends AbstractCountTask {

	private File srcdir = null;
	private File olddir = null;

	/**
	 * 現在のソースディレクトリを指定します。
	 * @param newRoot 現在のソースディレクトリ
	 */
	public void setSrcdir(final File newRoot) {
		this.srcdir = newRoot;
	}

	/**
	 * 過去のソースディレクトリを指定します。
	 * @param oldRoot 過去のソースディレクトリ
	 */
	public void setOlddir(final File oldRoot) {
		this.olddir = oldRoot;
	}

	/**
	 * 差分測定を実行します。
	 * @see org.apache.tools.ant.Task#execute()
	 * @throws BuildException when one of some exception occured
	 */
    @Override
    public void execute() throws BuildException {
    	this.validateAttributes();
    	OutputStream out = null;
    	try {
    		if (this.outputFile() != null) {
	    		try {
					out =  new FileOutputStream(this.outputFile());
				} catch (FileNotFoundException e) {
					throw new BuildException("fail to open " + this.outputFile().getPath(), e);
				}
	    	} else {
	    		out = System.out;
	    	}
    		DiffCountProc proc = new DiffCountProc();
        	proc.setSourceEncoding(this.sourceEncoding());
        	proc.setXmlFileName(this.xmlFileName());
        	proc.setFormat(this.formatType());
        	proc.setPathStyle(this.pathStyleName());
        	proc.setSortOrder(this.sortType());
			proc.doCountingAndWriting(this.olddir, this.srcdir, out);
    	} catch (BuildException e) {
			throw e;
    	} catch (IllegalArgumentException e) {
			throw new BuildException(e.getMessage(), e);
    	} catch (IOException e) {
			throw new BuildException("I/O Error", e);
    	} catch (Exception e) {
    		throw new BuildException(e);
    	} finally {
    		try {
    			if (out != null && out != System.out) out.close();
			} catch (IOException e) {
				throw new BuildException("I/O Error", e);
			}
    	}
    }

    /**
     * 属性値が不正でないか検証する.
     * @exception BuildException if an error occurs.
     */
    protected void validateAttributes() throws BuildException {
    	if (this.srcdir == null) {
			throw new BuildException("srcdir is required.");
		}
		if (!this.srcdir.isDirectory()) {
			throw new BuildException("srcdir '" + srcdir + "' is not directory.");
		}
		if (this.olddir == null) {
			throw new BuildException("olddir is required.");
		}
		if (!this.olddir.isDirectory()) {
			throw new BuildException("olddir '" + olddir + "' is not directory.");
		}
    }

}
