package keisuke.swing;

import javax.swing.SwingUtilities;

import keisuke.swing.stepcount.StepCountGUI;


/**
 * Swing画面StepCount起動クラス
 *
 */
public final class StepCount {

	private StepCount() { }

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StepCountGUI gui = new StepCountGUI();
				gui.mainWindow().setVisible(true);
			}
		});
	}
}
