package keisuke.swing.diffcount;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import keisuke.CommandOption;
import keisuke.count.option.DiffCountOption;
import keisuke.swing.AbstractCommandComponent;
import keisuke.swing.AbstractSelectPathUnit;
import keisuke.swing.AbstractSelectSortUnit;


/**
 * 計測オプション指定用GUI部品
 */
class DiffCommandComponent extends AbstractCommandComponent {

	private JLabel padding11;
	private JLabel padding21;
	private JLabel padding22;
	private JLabel padding23;

	DiffCommandComponent(final DiffCountGUI owner) {
		super(owner, "count diff steps", new DiffCountOption());
		this.padding11 = new JLabel("   "); //3 spaces
		this.padding21 = new JLabel("   "); //3 spaces
		this.padding22 = new JLabel("   "); //3 spaces
		this.padding23 = new JLabel("   "); //3 spaces
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractSelectSortUnit defineSortBox(final CommandOption option) {
		return new SelectSortUnit(this, option);
	}

	/** {@inheritDoc} */
	@Override
	protected AbstractSelectPathUnit definePathBox(final CommandOption option) {
		return new SelectPathUnit(this, option);
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
						.addGroup(this.formatBox().createGroupForVerticalGroup(container))
						.addComponent(this.padding21)
						.addGroup(this.sortBox().createGroupForVerticalGroup(container))
						.addComponent(this.padding22)
						.addGroup(this.pathBox().createGroupForVerticalGroup(container))
						.addComponent(this.padding23)
						.addComponent(this.countButton().button()));
	}

	/** {@inheritDoc} */
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
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
							.addGroup(this.sortBox()
								.createGroupForHorizontalGroup(container)))
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.padding22))
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addGroup(this.pathBox()
								.createGroupForHorizontalGroup(container)))
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.padding23))
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.countButton().button()))));
	}

}
