package keisuke.swing.diffcount;

import static keisuke.swing.diffcount.DiffCountGUIConstant.*;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Alignment;

/**
 * ２つのディレクトリ選択GUI部品をセットにした部品
 *
 */
public final class RootDirectoriesComponent {

	private DiffCountGUI root;
	private RootDirectoryUnit oldRootUnit;
	private RootDirectoryUnit newRootUnit;

	RootDirectoriesComponent(final DiffCountGUI owner) {
		this.root = owner;
		String[] oldNames = {OLD_DIR_TEXTFIELD, OLD_DIR_BUTTON};
		this.oldRootUnit = new RootDirectoryUnit(this, "Old source directory:", "browse", oldNames);
		String[] newNames = {NEW_DIR_TEXTFIELD, NEW_DIR_BUTTON};
		this.newRootUnit = new RootDirectoryUnit(this, "New source directory:", "browse", newNames);
	}

	/**
	 * ディレクトリが設定されているかチェックする
	 * @return 2つとも設定済みならtrue
	 */
	boolean isReady() {
		if (this.oldRootUnit.isReady() && this.newRootUnit.isReady()) {
			return true;
		}
		return false;
	}

	/**
	 * ステップ数の差分計測ボタンの状態を更新します。
	 * ディレクトリ名が２つ指定されていればボタンを有効にします。
	 * そうでなければ無効にします。
	 */
	void updateStatus() {
		if (this.isReady()) {
			this.root.setEnableToCount();
		} else {
			this.root.setDisableToCount();
		}
	}

	/**
	 * 選択された旧版のディレクトリパス名を返す
	 * @return 選択されたディレクトリパス名
	 */
	String oldDirName() {
		return this.oldRootUnit.choosedDirName();
	}

	/**
	 * 選択された新版のディレクトリパス名を返す
	 * @return 選択されたディレクトリパス名
	 */
	String newDirName() {
		return this.newRootUnit.choosedDirName();
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
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したParallelGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのParallelGroup
	 */
	GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(this.oldRootUnit.createGroupForVerticalGroup(container))
				.addGroup(this.newRootUnit.createGroupForVerticalGroup(container));
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
				.addComponent(this.oldRootUnit.label())
				.addComponent(this.newRootUnit.label()))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.oldRootUnit.dirField())
				.addComponent(this.newRootUnit.dirField()))
			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
				.addComponent(this.oldRootUnit.browseButton())
				.addComponent(this.newRootUnit.browseButton()));
	}
}
