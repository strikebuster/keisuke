package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.jenkinsci.plugins.keisuke.OutputSettingForDiff;

import keisuke.count.diff.DiffCountProceduralFunc;
import keisuke.count.diff.DiffFolderResult;

/**
 * Class for writing the diff counting result to output file.
 */
public class DiffCountResultWriter extends AbstractResultWriter<DiffFolderResult> {

	DiffCountResultWriter(final AbstractCountAdapter adapter, final OutputSettingForDiff setting,
			final File workspace, final PrintStream stream) {
		super(adapter, setting.getDiffOutputFilePath(), setting.getDiffOutputFormat(), workspace, stream);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void callWritingProcedure(final OutputStream out, final List<? extends DiffFolderResult> results)
			throws IllegalArgumentException, IOException {
		DiffFolderResult result = (DiffFolderResult) results.get(0);
		System.out.println(result.toString());
		((DiffCountProceduralFunc) this.countAdapter().countProc())
			.doFormattingAndWritingAbout(result, out, this.outFormat());
	}
}
