package keisuke.swing.stepcount;

import java.awt.Container;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

/**
 * 計測対象ソース設定用GUI部品
 */
class SourceListComponent {

	private StepCountGUI root;
	private JLabel label;
	private SourceListUnit sourceList;
	private AddButtonUnit addButton;
	private RemoveButtonUnit removeButton;
	private HiddenAddingUnit hiddenAdding;

	SourceListComponent(final StepCountGUI owner) {
		this.root = owner;
		this.label = new JLabel("Target source directories/files");
		this.sourceList = new SourceListUnit(this);
		this.addButton = new AddButtonUnit(this);
		this.removeButton = new RemoveButtonUnit(this);
		this.removeButton.setDisable();
		this.hiddenAdding = new HiddenAddingUnit(this);
		this.sourceList.listPane().setEnabled(true);
	}

	void addSources(final File[] files) {
		this.sourceList.addSources(files);
	}

	void removeSelectedSources() {
		this.sourceList.removeSelectedSources();
	}

	String[] getPathOfSources() {
		return this.sourceList.getPathOfSources();
	}

	// sourceListは自分の変更があるとこの関数を呼ぶ
	void updateStatus() {
		if (this.sourceList.isEmpty()) {
			this.removeButton.setDisable();
			this.root.setDisableToCount();
		} else {
			this.removeButton.setEnable();
			this.root.setEnableToCount();
		}
	}

	/**
	 * 親画面のJFrameインスタンスを返す
	 * 当部品の子部品が親画面を知るために必要
	 * @return JFrameインスタンス
	 */
	JFrame mainWindow() {
		return this.root.mainWindow();
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
						.addComponent(this.addButton.button())
						.addComponent(this.removeButton.button()))
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(this.sourceList.listPane())
						.addComponent(this.hiddenAdding.hiddenField()));
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
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.label))
						.addGroup(layout.createParallelGroup(Alignment.TRAILING)
							.addComponent(this.addButton.button()))
						.addGroup(layout.createParallelGroup(Alignment.TRAILING)
							.addComponent(this.removeButton.button())))
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
							.addComponent(this.sourceList.listPane())
							.addComponent(this.hiddenAdding.hiddenField()))));
	}
}
