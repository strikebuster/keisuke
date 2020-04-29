package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.BuildResult;
import org.jenkinsci.plugins.keisuke.CountingUnit;
import org.jenkinsci.plugins.keisuke.util.RelativePathUtil;

import keisuke.count.diff.DiffCountProc;
import keisuke.count.diff.DiffCountProceduralFunc;
import keisuke.count.diff.DiffFolderResult;

/**
 * Adapter for keisuke.count.diff.DiffCountProc
 */
public class DiffCountAdapter extends AbstractCountAdapter {

	private String oldDirectory = null;
	private String newDirectory = null;

	public DiffCountAdapter(final CountingUnit unit, final File workspace, final File base,
			final BaseDirIncludingSwitch including, final PrintStream stream) {
		super(unit, workspace, base, including, stream);
		this.setCountProc(new DiffCountProceduralFunc());
		this.initCommonProps();
		this.setPathStyle();
		this.setDirNames(this.inputSetting().getOldSourceDirectory(),
				this.inputSetting().getSourceDirectory());
	}

	private void setPathStyle() {
		((DiffCountProc) this.countProc()).setPathStyle(this.pathStyle().value());
	}

	private void setDirNames(final String olddir, final String newdir) {
		this.oldDirectory = olddir;
		this.newDirectory = newdir;
	}

	/**
	 * Counts diff line steps between old and new source directories,
	 * and gets counting result.
	 * @return diff counting result.
	 * @throws IOException signal of error.
	 */
	public DiffFolderResult getCountingResult() throws IOException {
		//this.logger().println("[keisuke] source dir " + this.newDirectory
		//		+ " is actually " + this.baseDirectory().getCanonicalPath());
		File oldDir = RelativePathUtil.getDirectoryOf(this.workspace(), this.oldDirectory);
		this.logger().println("[keisuke] old srcdir " + this.oldDirectory
				+ " is actually " + oldDir.getCanonicalPath());
		// count
		return ((DiffCountProceduralFunc) this.countProc())
				.getResultOfCountingDiff(oldDir, this.baseDirectory());
	}

	/** {@inheritDoc}	 */
	@Override
	public void writeResult(final BuildResult result) {
		DiffCountResultWriter writer = new DiffCountResultWriter(
				this, this.outputSetting().getDiffOutputSetting(), this.workspace(), this.logger());
		// List<DiffCountResult> cannot be accepted by DiffCountWriter,
		// List<DiffFolderResult> which has only one element can be accepted.
		List<DiffFolderResult> list = new ArrayList<DiffFolderResult>();
		list.add(result.getDiffResult());
		writer.write(list);
	}
}
