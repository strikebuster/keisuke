package keisuke.swing.diffcount;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;
import keisuke.swing.ResultViewComponent;

/**
 * Diff結果表示関連GUI部品グループ
 */
class DiffResultViewComponent extends ResultViewComponent {

	private ShowAllButtonUnit showButton;
	private HideAllButtonUnit hideButton;
	private JLabel padding;
	private TotalSumUnit totalSum;
	private DiffFolderResult currentResult;

	DiffResultViewComponent(final DiffCountGUI owner, final MessageDefine msgdef) {
		super(owner);
		this.showButton = new ShowAllButtonUnit(this);
		this.hideButton = new HideAllButtonUnit(this);
		this.padding = new JLabel("   "); //3 spaces
		this.totalSum = new TotalSumUnit(msgdef);
		this.setResultPane(new DiffResultPaneUnit(this, msgdef));
	}

	void refreshWith(final String text, final DiffFolderResult result) {
		this.currentResult = result;
		this.totalSum.showTotal(result.status(), result.addedSteps(), result.deletedSteps());
		super.refreshWith(text);
	}

	@Override
	protected void displayWithFormattedStyle() {
		super.displayWithFormattedStyle();
		this.disableExpandingButtons();
	}

	@Override
	protected void displayWithTableStyle() {
		((DiffResultPaneUnit) this.resultPane()).showTable(this.currentResult);
		this.enableExpandingButtons();
	}

	void enableExpandingButtons() {
		this.showButton.setEnable();
		this.hideButton.setEnable();
	}

	void disableExpandingButtons() {
		this.showButton.setDisable();
		this.hideButton.setDisable();
	}

	void showAll() {
		((DiffResultPaneUnit) this.resultPane()).showAll();
	}

	void hideAll() {
		((DiffResultPaneUnit) this.resultPane()).hideAll();
	}

	@Override
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.showButton.button())
					.addComponent(this.hideButton.button())
					.addComponent(this.padding)
					.addComponent(this.totalSum.label()))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(super.createGroupForVerticalGroup(container)));
	}

	@Override
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.showButton.button()))
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.hideButton.button()))
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.padding))
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
							.addComponent(this.totalSum.label())))
				.addGroup(super.createGroupForHorizontalGroup(container))
			);
	}

}
