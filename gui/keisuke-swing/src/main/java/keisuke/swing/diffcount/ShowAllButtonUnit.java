package keisuke.swing.diffcount;

import static keisuke.swing.diffcount.DiffCountGUIConstant.SHOW_BUTTON;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * JXTreeTable表示データの階層の全展開ボタン
 */
class ShowAllButtonUnit {

	private DiffResultViewComponent parent;
	private JButton button;

	ShowAllButtonUnit(final DiffResultViewComponent owner) {
		this.parent = owner;
		this.button = new JButton();
		this.button.setName(SHOW_BUTTON);
		this.button.setText("show all");
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfShowButton(event);
			}
		});
		this.setDisable();
	}

	private void doActionOfShowButton(final ActionEvent event) {
		this.parent.showAll();
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
