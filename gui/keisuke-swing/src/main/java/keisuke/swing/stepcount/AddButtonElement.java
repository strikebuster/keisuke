package keisuke.swing.stepcount;

import static keisuke.swing.stepcount.StepCountGUIConstant.ADD_BUTTON;
import static keisuke.swing.stepcount.StepCountGUIConstant.FILE_CHOOSER;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * 計測対象の追加ボタン
 */
class AddButtonElement {

	private SourceListComponent parent;
	private JButton button;
	// 直近のディレクトリ記憶用
	private File lastWorkingDir = null;

	AddButtonElement(final SourceListComponent owner) {
		this.parent = owner;
		this.button = new JButton();
		this.button.setName(ADD_BUTTON);
		this.button.setText("add source...");
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfAddButton(event);
			}
		});
	}

	private void doActionOfAddButton(final ActionEvent event) {
		String name = ((JButton) event.getSource()).getName();
		File[] files = this.chooseFolder(name);
		if (files != null) {
			this.parent.addSources(files);
		}
	}

	private File[] chooseFolder(final String name) {
		JFileChooser chooser = new JFileChooser();
		chooser.setName(FILE_CHOOSER);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(this.lastWorkingDir); // if null, set default
		if (chooser.showOpenDialog(this.parent.mainWindow()) == JFileChooser.APPROVE_OPTION) {
			this.lastWorkingDir = chooser.getCurrentDirectory();
			return chooser.getSelectedFiles();
		}
		return null;
	}

	JButton button() {
		return this.button;
	}
}
