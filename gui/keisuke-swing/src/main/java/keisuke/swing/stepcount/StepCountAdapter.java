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

	// not use since Ver.2.0.0
	//void setShowDirectory(final boolean show) {
	//	((StepCountProc) this.countProc()).setShowDirectory(show);
	//}

	StepCountResult[] getCountedResultAsRaw() {
		return ((StepCountProc) this.countProc()).getResultAsRawData();
	}
}
