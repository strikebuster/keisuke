package org.jenkinsci.plugins.keisuke.count;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.jenkinsci.plugins.keisuke.OutputSetting;

import keisuke.StepCountResult;
import keisuke.count.step.StepCountProceduralFunc;

/**
 * Class for writing the step counting result to output file.
 */
class StepCountResultWriter extends AbstractResultWriter<StepCountResult> {

	StepCountResultWriter(final AbstractCountAdapter adapter, final OutputSetting setting,
			final File workspace, final PrintStream stream) {
		super(adapter, setting.getOutputFilePath(), setting.getOutputFormat(), workspace, stream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override @SuppressWarnings("unchecked")
	void callWritingProcedure(final OutputStream out, final List<? extends StepCountResult> countResults)
			throws IllegalArgumentException, IOException {

    	((StepCountProceduralFunc) this.countAdapter().countProc())
    		.doFormattingAndWritingAbout((List<StepCountResult>) countResults, out, this.outFormat());
	}
}
