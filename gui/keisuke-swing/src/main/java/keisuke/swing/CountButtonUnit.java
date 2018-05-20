package keisuke.swing;

import static keisuke.swing.GUIConstant.COUNT_BUTTON;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * 行数計測の実行指示ボタン
 */
public class CountButtonUnit {

	private CommandComponent parent;
	private JButton button;

	CountButtonUnit(final CommandComponent owner) {
		this.parent = owner;
		this.button = new JButton();
		this.button.setName(COUNT_BUTTON);
		this.button.setText("");
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfCountButton(event);
			}
		});
		this.setDisable();
	}

	private void doActionOfCountButton(final ActionEvent event) {
		// 計測実行を呼び出し、ボタンを無効化する
		// 計測条件が変更されるまでボタンは有効化されない
		this.parent.doCounting();
		this.setDisable();
	}

	/**
	 * 計測指示ボタンのテキストを設定する
	 * @param text 計測指示ボタンのテキスト
	 */
	protected void setButtonText(final String text) {
		this.button.setText(text);
	}

	/**
	 * JButtonインスタンスを返す
	 * @return JButtonインスタンス
	 */
	public JButton button() {
		return this.button;
	}

	/**
	 * 計測指示ボタンを有効化する
	 */
	protected void setEnable() {
		this.button.setEnabled(true);
	}

	/**
	 * 計測指示ボタンを無効化する
	 */
	protected void setDisable() {
		this.button.setEnabled(false);
	}

}
