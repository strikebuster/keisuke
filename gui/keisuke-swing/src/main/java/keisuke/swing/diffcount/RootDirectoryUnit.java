package keisuke.swing.diffcount;

import static keisuke.count.util.FileNameUtil.getCanonicalOrAbsolutePath;
import static keisuke.swing.diffcount.DiffCountGUIConstant.FILE_CHOOSER;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ディレクトリ選択用のGUI部品
 */
class RootDirectoryUnit {

	private RootDirectoriesComponent parent;
	private JLabel label;
	private JButton browseButton;
	private JTextField dirField;
	private boolean ready = false;
	// 直近のディレクトリ記憶用
	private File lastWorkingDir = null;

	RootDirectoryUnit(final RootDirectoriesComponent owner, final String title, final String buttonLabel,
			final String[] names) {
		this.parent = owner;
		this.label = new JLabel();
		this.label.setText(title);
		this.dirField = new JTextField();
		this.dirField.setName(names[0]);
		this.dirField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(final DocumentEvent e) {
				updateStatus();
			}

			public void insertUpdate(final DocumentEvent e) {
				updateStatus();
			}

			public void changedUpdate(final DocumentEvent e) {
				updateStatus();
			}
		});
		this.browseButton = new JButton();
		this.browseButton.setName(names[1]);
		this.browseButton.setText(buttonLabel + "...");
		this.browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfBrowseButton(event);
			}
		});
	}

	/**
	 * JLabelインスタンスを返す
	 * @return JLabelインスタンス
	 */
	JLabel label() {
		return this.label;
	}

	/**
	 * JTextFieldインスタンスを返す
	 * @return JTextFieldインスタンス
	 */
	JTextField dirField() {
		return this.dirField;
	}

	/**
	 * JButtonインスタンスを返す
	 * @return JButtonインスタンス
	 */
	JButton browseButton() {
		return this.browseButton;
	}

	/**
	 * 選択されたディレクトリパス名を返す
	 * @return 選択されたディレクトリパス名
	 */
	String choosedDirName() {
		return this.dirField.getText();
	}

	/**
	 * ディレクトリが設定されているかチェックする
	 * @return 設定済みならtrue
	 */
	boolean isReady() {
		return this.ready;
	}

	private void updateStatus() {
		this.ready = false;
		String dirStr = this.dirField.getText();
		if (!dirStr.isEmpty()) {
			File dirFile = new File(dirStr);
			if (dirFile.exists() && dirFile.isDirectory()) {
				this.ready = true;
			}
		}
		this.parent.updateStatus();
	}

	private void doActionOfBrowseButton(final ActionEvent event) {
		String name = ((JButton) event.getSource()).getName();
		File file = this.chooseFolder(name);
		if (file != null) {
			this.dirField.setText(getCanonicalOrAbsolutePath(file));
		}
	}

	private File chooseFolder(final String name) {
		JFileChooser chooser = new JFileChooser();
		chooser.setName(FILE_CHOOSER + "-" + name);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(this.lastWorkingDir); // if null, set default
		if (chooser.showOpenDialog(this.parent.mainWindow()) == JFileChooser.APPROVE_OPTION) {
			this.lastWorkingDir = chooser.getCurrentDirectory();
			return chooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したParallelGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのParallelGroup
	 */
	GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.label)
					.addComponent(this.dirField)
					.addComponent(this.browseButton));
	}

}
