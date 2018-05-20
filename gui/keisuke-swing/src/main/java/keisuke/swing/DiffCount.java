package keisuke.swing;

import javax.swing.SwingUtilities;

import keisuke.swing.diffcount.DiffCountGUI;

/**
 * Swing画面DiffCount起動クラス
 *
 */
public final class DiffCount {

	private DiffCount() { }

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DiffCountGUI gui = new DiffCountGUI();
				gui.mainWindow().setVisible(true);
			}
		});
	}
}
