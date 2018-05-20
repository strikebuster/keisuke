package keisuke.swing;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.GroupLayout.Alignment;

import static keisuke.swing.GUIConstant.FORMAT_RADIO;
import static keisuke.swing.GUIConstant.TABLE_RADIO;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 表示モードの選択部品
 */
class DisplayRadioUnit {

	private ResultViewComponent parent;
	private JLabel label;
	private ButtonGroup radioGroup;
	private JRadioButton formatRadio;
	private JRadioButton tableRadio;

	DisplayRadioUnit(final ResultViewComponent owner) {
		this.parent = owner;
		this.label = new JLabel("display style:");
		this.radioGroup = new ButtonGroup();
		this.formatRadio = new JRadioButton();
		this.formatRadio.setName(FORMAT_RADIO);
		this.formatRadio.setText(DisplayStyleEnum.FORMAT.getValue());
		this.tableRadio = new JRadioButton();
		this.tableRadio.setName(TABLE_RADIO);
		this.tableRadio.setText(DisplayStyleEnum.TABLE.getValue());

		this.radioGroup.add(formatRadio);
		this.radioGroup.add(tableRadio);
		this.selectFormattedStyle();

		this.formatRadio.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfSelect(event);
			}
		});
		this.tableRadio.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfSelect(event);
			}
		});
	}

	void doActionOfSelect(final ActionEvent event) {
		String str = event.getActionCommand();
		//System.out.println("[DEBUG] on radio : " + str);
		if (str.equals(DisplayStyleEnum.FORMAT.getValue())) {
			this.parent.displayWithFormattedStyle();
		} else if (str.equals(DisplayStyleEnum.TABLE.getValue())) {
			this.parent.displayWithTableStyle();
		}
	}

	void setDisable() {
		this.label.setEnabled(false);
		this.formatRadio.setEnabled(false);
		this.tableRadio.setEnabled(false);
	}

	void setEnable() {
		this.label.setEnabled(true);
		this.formatRadio.setEnabled(true);
		this.tableRadio.setEnabled(true);
	}

	DisplayStyleEnum getSelectedStyle() {
		if (this.formatRadio.isSelected()) {
			return DisplayStyleEnum.FORMAT;
		} else if (this.tableRadio.isSelected()) {
			return DisplayStyleEnum.TABLE;
		}
		return null;
	}

	void selectFormattedStyle() {
		this.formatRadio.setSelected(true);
	}

	void selectTableStyle() {
		this.tableRadio.setSelected(true);
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.label)
					.addComponent(this.formatRadio)
					.addComponent(this.tableRadio));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.label))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.formatRadio))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.tableRadio));
	}
}
