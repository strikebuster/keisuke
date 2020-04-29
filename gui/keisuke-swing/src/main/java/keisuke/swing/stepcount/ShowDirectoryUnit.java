package keisuke.swing.stepcount;

import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout.Alignment;

import static keisuke.swing.stepcount.StepCountGUIConstant.SHOWDIR_CHECK;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ShowDirectory指定用部品
 * ver.2.0.0以降では使用しない
 */
class ShowDirectoryUnit {

	private CountCommandComponent parent;
	private JCheckBox checkBox;

	ShowDirectoryUnit(final CountCommandComponent owner) {
		this.parent = owner;
		this.checkBox = new JCheckBox();
		this.checkBox.setName(SHOWDIR_CHECK);
		this.checkBox.setText("show directory");
		this.checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfCheckBox(event);
			}
		});
	}

	private void doActionOfCheckBox(final ActionEvent event) {
		this.parent.updateStatus();
	}

	JCheckBox checkBox() {
		return this.checkBox;
	}

	boolean selectedShowDirecory() {
		return this.checkBox.isSelected();
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	protected GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.checkBox));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	protected GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.checkBox));
	}
}
