package keisuke.swing.diffcount;

import static keisuke.swing.diffcount.DiffCountGUIConstant.HIDE_BUTTON;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * JXTreeTable表示データの階層の全収納ボタン
 */
class HideAllButtonElement {

	private DiffResultViewComponent parent;
	private JButton button;

	HideAllButtonElement(final DiffResultViewComponent owner) {
		this.parent = owner;
		this.button = new JButton();
		this.button.setName(HIDE_BUTTON);
		this.button.setText("hide all");
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfHideButton(event);
			}
		});
		this.setDisable();
	}

	private void doActionOfHideButton(final ActionEvent event) {
		this.parent.hideAll();
	}

	void setEnable() {
		this.button.setEnabled(true);
	}

	void setDisable() {
		this.button.setEnabled(false);
	}

	JButton button() {
		return this.button;
	}
}
