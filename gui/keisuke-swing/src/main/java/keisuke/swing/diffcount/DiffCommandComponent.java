package keisuke.swing.diffcount;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import keisuke.count.option.DiffCountOption;
import keisuke.swing.CommandComponent;


/**
 * 計測オプション指定用GUI部品
 */
class DiffCommandComponent extends CommandComponent {

	private JLabel padding11;
	private JLabel padding21;
	private JLabel padding22;

	DiffCommandComponent(final DiffCountGUI owner) {
		super(owner, "count diff steps", new DiffCountOption());
		this.padding11 = new JLabel("   "); //3 spaces
		this.padding21 = new JLabel("   "); //3 spaces
		this.padding22 = new JLabel("                                        "); //40 spaces
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
						.addComponent(this.padding22)
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
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.padding11))
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.padding21)))
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addGroup(this.xmlBox()
								.createGroupForHorizontalGroup(container))))
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.padding22))
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.countButton().button()))));
	}
}
