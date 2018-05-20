package keisuke.swing.stepcount;

import static keisuke.swing.stepcount.StepCountGUIConstant.*;


import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * 計測対象ソース保持用GUI部品
 */
class SourceListUnit {

	private static final int PANE_WIDTH = 576;
	private static final int PANE_HEIGHT = 126;

	private SourceListComponent parent;
	private JScrollPane basePane;
	private JList<File> sourceList;
	private DefaultComboBoxModel<File> model;

	SourceListUnit(final SourceListComponent owner) {
		this.parent = owner;
		this.model = new DefaultComboBoxModel<File>();
		this.model.addListDataListener(new ListDataListener() {
			public void contentsChanged(final ListDataEvent e) { }
			public void intervalAdded(final ListDataEvent e) {
				updateStatus();
			}
			public void intervalRemoved(final ListDataEvent e) {
				updateStatus();
			}
		});
		this.sourceList = new JList<File>(this.model);
		this.sourceList.setName(SOURCE_LIST);
		this.sourceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.basePane = new JScrollPane(this.sourceList);
		this.basePane.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
	}

	private void updateStatus() {
		this.parent.updateStatus();
		//System.out.println("Source dimension = " + this.basePane.getWidth()
		//		+ ", " + this.basePane.getHeight());
	}

	boolean isEmpty() {
		return !(this.model.getSize() > 0);
	}

	void addSources(final File[] files) {
		if (files == null || files.length == 0) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			this.model.addElement(files[i]);
		}
	}

	void removeSelectedSources() {
		List<File> files = this.sourceList.getSelectedValuesList();
		if (files != null) {
			for (File file : files) {
				this.model.removeElement(file);
			}
		}
	}

	String[] getPathOfSources() {
		String[] files = new String[this.model.getSize()];
		for (int i = 0; i < this.model.getSize(); i++) {
			files[i] = ((File) this.model.getElementAt(i)).getPath();
		}
		return files;
	}

	JScrollPane listPane() {
		return this.basePane;
	}

}
