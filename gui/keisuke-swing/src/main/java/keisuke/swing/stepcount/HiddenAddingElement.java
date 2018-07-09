package keisuke.swing.stepcount;

import static keisuke.swing.stepcount.StepCountGUIConstant.HIDDEN_ADDING;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 計測対象の追加をする隠し部品
 * 自動テストのときに利用する
 */
class HiddenAddingElement {

	private static final int FIELD_WIDTH = 1;
	private static final int FIELD_HEIGHT = 1;

	private SourceListComponent parent;
	private JTextField textField;

	HiddenAddingElement(final SourceListComponent owner) {
		this.parent = owner;
		this.textField = new JTextField();
		this.textField.setName(HIDDEN_ADDING);
		this.textField.setText("");
		this.textField.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		this.textField.setEditable(false);
		//this.textField.setVisible(false);
		this.textField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(final DocumentEvent e) {
				noAction(e);
			}

			public void insertUpdate(final DocumentEvent e) {
				addValueIntoList(e);
			}

			public void changedUpdate(final DocumentEvent e) {
				noAction(e);
			}
		});
	}

	private void addValueIntoList(final DocumentEvent e) {
		String path = this.textField.getText();
		//System.out.println("HiddenUI(addIntoList) event = " + e.getType() + " path = " + path);
		if (path != null && !path.isEmpty()) {
			File[] files = {new File(path)};
			this.parent.addSources(files);
		}
	}

	private void noAction(final DocumentEvent e) {
		//String path = this.textField.getText();
		//System.out.println("HiddenUI(noAction) event = " + e.getType() + " path = " + path);
	}

	JTextField hiddenField() {
		return this.textField;
	}
}
