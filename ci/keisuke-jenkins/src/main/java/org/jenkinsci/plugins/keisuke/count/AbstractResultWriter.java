package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import keisuke.util.LogUtil;

/**
 * Base class for writing the counting result to output file.
 * @param <T> class for counting result StepCountResultForPublish or DiffCountResultForPublish.
 */
abstract class AbstractResultWriter<T> {

	private AbstractCountAdapter countAdapter = null;
	private String outFilePath;
	private String outFormat;
	private File baseDir;
	private transient PrintStream logger;

	AbstractResultWriter(final AbstractCountAdapter adapter, final String path,
			final String format, final File workspace, final PrintStream stream) {
		this.countAdapter = adapter;
		this.outFilePath = path;
		this.outFormat = format;
		this.baseDir = workspace;
		this.logger = stream;
		this.logger.println("[keisuke] output file: <" + this.outFilePath + ">");
		this.logger.println("[keisuke] format: <" + this.outFormat + ">");
	}

	/**
	 * Gets AbstractCountAdapter instance.
	 * @return AbstractCountAdapter instance.
	 */
	protected AbstractCountAdapter countAdapter() {
		return this.countAdapter;
	}

	/**
	 * Gets output file path.
	 * @return file path.
	 */
	protected String outFilePath() {
		return this.outFilePath;
	}

	/**
	 * Gets output format.
	 * @return format.
	 */
	protected String outFormat() {
		return this.outFormat;
	}

	/**
	 * Gets base directory.
	 * @return base directory.
	 */
	protected File baseDir() {
		return this.baseDir;
	}

	/**
	 * Gets PrintStream to log.
	 * @return PrintStream.
	 */
	protected PrintStream logger() {
		return this.logger;
	}

	/**
	 * write the counting result to output file in specified format style.
	 * @param countResults List of class T which contains counting result.
	 */
	protected void write(final List<? extends T> countResults) {
		OutputStream out = null;
		try {
			File ofile = new File(this.baseDir, this.outFilePath);
			this.logger.println("[keisuke] output to " + ofile.getAbsolutePath()
					+ " in " + this.outFormat + " format");
			out = new FileOutputStream(ofile);
			// 結果を出力する
			this.callWritingProcedure(out, countResults);
		} catch (FileNotFoundException e) {
			this.logger.println("[keisuke] fail to open output file " + this.outFilePath);
			this.logger.println(LogUtil.getMessage(e));
		} catch (IllegalArgumentException e) {
			this.logger.println("[keisuke] illegal arguments error: " + LogUtil.getMessage(e));
    	} catch (IOException e) {
    		this.logger.println("[keisuke] I/O Error at writing to " + this.outFilePath);
    		this.logger.println(LogUtil.getMessage(e));
    	} catch (Exception e) {
    		this.logger.println("[keisuke] exception in calling to write: " + LogUtil.getMessage(e));
    	} finally {
    		try {
    			if (out != null) out.close();
			} catch (IOException e) {
				this.logger.println("[keisuke] I/O Error at closing " + this.outFilePath);
			}
    	}
	}

	/**
	 * Writes formatted count results into output stream.
	 * @param out OutputStream.
	 * @param countResults list of count results.
	 * @throws IllegalArgumentException signal of error to call.
	 * @throws IOException signal of error to output.
	 */
	abstract void callWritingProcedure(OutputStream out, List<? extends T> countResults)
			throws IllegalArgumentException, IOException;
}
