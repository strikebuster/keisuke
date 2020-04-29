package keisuke.swing;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.GroupLayout.Alignment;

import static keisuke.swing.GUIConstant.ENCODING_TEXT;

import java.awt.Container;

/**
 * Encoding指定GUI部品
 */
public class SpecifyEncodingUnit {

	private AbstractCommandComponent parent;
	private JLabel label;
	private JTextField textField;
	private String srcEncoding = System.getProperty("file.encoding");

	SpecifyEncodingUnit(final AbstractCommandComponent owner) {
		this.parent = owner;
		this.label = new JLabel("encoding:");
		this.textField = new JTextField();
		this.textField.setName(ENCODING_TEXT);
		this.textField.setText(this.srcEncoding);
		this.textField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(final DocumentEvent e) {
				updateValue();
			}

			public void insertUpdate(final DocumentEvent e) {
				updateValue();
			}

			public void changedUpdate(final DocumentEvent e) {
				updateValue();
			}
		});
	}

	private void updateValue() {
		this.srcEncoding = this.textField.getText();
		//System.out.println(this.srcEncoding);
		this.parent.updateStatus();
	}

	/**
	 * エンコード値を返す
	 * @return エンコード値
	 */
	protected String encoding() {
		return this.srcEncoding;
	}

	/**
	 * JLabelインスタンスを返す
	 * @return JLabelインスタンス
	 */
	protected JLabel label() {
		return this.label;
	}

	/**
	 * JTextFieldインスタンスを返す
	 * @return JTextFieldインスタンス
	 */
	protected JTextField textField() {
		return this.textField;
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.label)
					.addComponent(this.textField));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.label))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.textField));
	}
}
