package keisuke.swing.stepcount;

import static keisuke.swing.stepcount.StepCountGUIConstant.REMOVE_BUTTON;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * 計測対象の削除ボタン
 */
class RemoveButtonUnit {

	private SourceListComponent parent;
	private JButton button;

	RemoveButtonUnit(final SourceListComponent owner) {
		this.parent = owner;
		this.button = new JButton();
		this.button.setName(REMOVE_BUTTON);
		this.button.setText("remove selected ones");
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfRemoveButton(event);
			}
		});
	}

	private void doActionOfRemoveButton(final ActionEvent event) {
		this.parent.removeSelectedSources();
	}

	JButton button() {
		return this.button;
	}

	void setDisable() {
		this.button.setEnabled(false);
	}

	void setEnable() {
		this.button.setEnabled(true);
	}
}
