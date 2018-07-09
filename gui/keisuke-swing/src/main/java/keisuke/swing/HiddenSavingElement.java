package keisuke.swing;

import static keisuke.swing.GUIConstant.HIDDEN_SAVING;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 計測結果の保存をする隠し部品
 * 自動テストのときに利用する
 */
public class HiddenSavingElement {

	private static final int FIELD_WIDTH = 1;
	private static final int FIELD_HEIGHT = 1;

	private SaveAsButtonUnit parent;
	private JTextField textField;

	HiddenSavingElement(final SaveAsButtonUnit owner) {
		this.parent = owner;
		this.textField = new JTextField();
		this.textField.setName(HIDDEN_SAVING);
		this.textField.setText("");
		this.textField.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		this.textField.setEditable(false);
		//this.textField.setVisible(false);
		this.textField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(final DocumentEvent e) {
				noAction(e);
			}

			public void insertUpdate(final DocumentEvent e) {
				saveToFile(e);
			}

			public void changedUpdate(final DocumentEvent e) {
				noAction(e);
			}
		});
	}

	private void saveToFile(final DocumentEvent e) {
		String path = this.textField.getText();
		//System.out.println("HiddenUI(saveToFile) event = " + e.getType() + " path = " + path);
		//this.parent.updateToChoose();
		if (path == null || path.isEmpty()) {
			return;
		}
		File file = new File(path);
		this.parent.handleFile(file);
	}

	private void noAction(final DocumentEvent e) {
		//String path = this.textField.getText();
		//System.out.println("HiddenUI(noAction) event = " + e.getType() + " path = " + path);
		this.parent.updateToChoose();
	}

	/**
	 * 隠し部品であるJTextFieldインスタンスを返す
	 * @return JTextFieldインスタンス
	 */
	protected JTextField hiddenField() {
		return this.textField;
	}
}
