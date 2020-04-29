package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.BuildResult;
import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.Messages;
import org.jenkinsci.plugins.keisuke.util.FileFinder;

import keisuke.StepCountResult;
import keisuke.count.step.StepCountProc;
import keisuke.count.step.StepCountProceduralFunc;

/**
 * Adapter for keisuke.count.step.StepCountProc or StepCountProceduralFunc
 */
public class StepCountAdapter extends AbstractCountAdapter {

	private String[] dirNames = new String[1];

	/**
	 * constructor.
	 * @param unit CountingUnit instance.
	 * @param workspace jenkins job workspace.
	 * @param base directory of source code files.
	 * @param including path style is base, or sub.
	 * @param stream log stream.
	 * @throws IOException signal of failure about base.getCannonicalPath.
	 */
	public StepCountAdapter(final CountingUnit unit, final File workspace, final File base,
			final BaseDirIncludingSwitch including, final PrintStream stream) throws IOException {
		super(unit, workspace, base, including, stream);
		this.setCountProc(new StepCountProceduralFunc());
		this.initCommonProps();
		this.setPathStyle();
		this.setSourceDirectory(base);
	}

	private void setSourceDirectory(final File srcdir) throws IOException {
		this.dirNames[0] = srcdir.getCanonicalPath();
	}

	private void setPathStyle() {
		((StepCountProc) this.countProc()).setPathStyle(this.pathStyle().value());
	}

	/**
	 * Counts line steps under source directory,
	 * and gets counting result.
	 * @return array of step counting result.
	 * @throws IOException signal for error.
	 */
	public StepCountResult[] getCountingResultAboutDir() throws IOException {
		String srcdir = this.inputSetting().getSourceDirectory();
		this.logger().println("[keisuke] source dir " + srcdir
				+ " is actually " + this.baseDirectory().getCanonicalPath());
		return ((StepCountProceduralFunc) this.countProc()).getResultOfCountingPaths(this.dirNames);
	}

	/**
	 * Counts line steps about file set,
	 * and gets counting result.
	 * @return list of step counting result.
	 * @throws IllegalArgumentException signal about argument error.
	 * @throws IOException signal about io error.
	 */
	public List<StepCountResult> getCountingResultAboutFileSet() throws IllegalArgumentException, IOException {
		String srcdir = this.inputSetting().getSourceDirectory();
		this.logger().println("[keisuke] source dir " + srcdir
				+ " is actually " + this.baseDirectory().getCanonicalPath());
		String[] fileNames = new FileFinder(this.baseDirectory(), this.inputSetting().getIncludePattern(),
				this.inputSetting().getExcludePattern()).getFoundedFileNames();
		if (fileNames == null || fileNames.length == 0) {
			this.logger().println("[keisuke] " + Messages.filenotfound());
			return new ArrayList<StepCountResult>();
		}
		this.logger().println("[keisuke] source dir has " + fileNames.length + " target files.");
		return ((StepCountProceduralFunc) this.countProc())
				.getResultOfCountingFileSet(this.baseDirectory(), fileNames);
	}

	/** {@inheritDoc}	 */
	@Override
	public void writeResult(final BuildResult result) {
		StepCountResultWriter writer =
				new StepCountResultWriter(this, this.outputSetting(), this.workspace(), this.logger());
		List<StepCountResultForPublish> list = result.getFileSteps();
		writer.write(list);
	}
}
