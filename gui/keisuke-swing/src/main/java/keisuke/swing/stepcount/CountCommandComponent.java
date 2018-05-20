package keisuke.swing.stepcount;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import keisuke.count.option.StepCountOption;
import keisuke.swing.CommandComponent;
import keisuke.swing.SeparatorUnit;

import javax.swing.JLabel;

/**
 * 計測オプション指定用GUI部品
 */
class CountCommandComponent extends CommandComponent {

	private JLabel padding11;
	private SeparatorUnit separator;
	private ShowDirectoryUnit showDirBox;
	private JLabel padding21;
	private JLabel padding22;
	private SelectSortUnit sortBox;
	private JLabel padding23;

	CountCommandComponent(final StepCountGUI owner) {
		super(owner, "count steps", new StepCountOption());

		this.padding11 = new JLabel("   ");
		this.separator = new SeparatorUnit();
		this.showDirBox = new ShowDirectoryUnit(this);
		this.padding21 = new JLabel("   ");
		this.padding22 = new JLabel("   ");
		this.sortBox = new SelectSortUnit(this);
		this.padding23 = new JLabel("   ");
	}

	boolean showDirectory() {
		return this.showDirBox.selectedShowDirecory();
	}

	String sort() {
		return this.sortBox.selectedValue();
	}

	/** {@inheritDoc} */
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
					.addGroup(this.encodingBox().createGroupForVerticalGroup(container))
					.addComponent(this.padding11)
					.addGroup(this.xmlBox().createGroupForVerticalGroup(container)))
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
					.addGroup(this.separator.createGroupForVerticalGroup(container)))
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
					.addGroup(this.formatBox().createGroupForVerticalGroup(container))
					.addComponent(this.padding21)
					.addGroup(this.sortBox.createGroupForVerticalGroup(container))
					.addComponent(this.padding22)
					.addGroup(this.showDirBox.createGroupForVerticalGroup(container))
					.addComponent(this.padding23)
					.addComponent(this.countButton().button()));
	}

	/** {@inheritDoc} */
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(this.encodingBox()
								.createGroupForHorizontalGroup(container))
						.addGroup(this.formatBox()
								.createGroupForHorizontalGroup(container)))
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.padding11)
							.addComponent(this.padding21))
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(this.xmlBox()
								.createGroupForHorizontalGroup(container))
						.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(this.sortBox
									.createGroupForHorizontalGroup(container)))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.padding22))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(this.showDirBox
									.createGroupForHorizontalGroup(container)))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.padding23))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.countButton().button())))
						))
				.addGroup(this.separator.createGroupForHorizontalGroup(container))
			);
	}
}
