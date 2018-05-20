package keisuke.swing.stepcount;

import keisuke.StepCountResult;
import keisuke.count.step.StepCountProc;
import keisuke.swing.AbstractCountAdapter;

/**
 * 行数計測機能のSwing画面からStepCountProcを呼び出すためのAdapter
 *
 */
class StepCountAdapter extends AbstractCountAdapter {

	StepCountAdapter() {
		this.setCountProc(new StepCountProc());
	}

	void setShowDirectory(final boolean show) {
		((StepCountProc) this.countProc()).setShowDirectory(show);
	}

	void setSort(final String sort) {
		((StepCountProc) this.countProc()).setSort(sort);
	}

	StepCountResult[] getCountedResultAsRaw() {
		return ((StepCountProc) this.countProc()).getResultAsRawData();
	}
}
