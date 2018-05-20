package keisuke.swing.stepcount;

import keisuke.StepCountResult;
import keisuke.report.property.MessageDefine;
import keisuke.swing.ResultViewComponent;

/**
 * StepCount結果表示関連GUI部品グループ
 */
class CountResultViewComponent extends ResultViewComponent {

	private StepCountResult[] currentResult;

	CountResultViewComponent(final StepCountGUI owner, final MessageDefine msgdef) {
		super(owner);
		this.setResultPane(new CountResultPaneUnit(this, msgdef));
	}

	void refreshWith(final String text, final StepCountResult[] result) {
		this.currentResult = result;
		super.refreshWith(text);
	}

	@Override
	protected void displayWithTableStyle() {
		((CountResultPaneUnit) this.resultPane()).showTable(this.currentResult);
	}

}
