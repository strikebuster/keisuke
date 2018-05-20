package keisuke.swing;

import static keisuke.swing.GUIConstant.RESULT_TEXT;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

/**
 * 計測結果テキスト表示用GUI部品
 */
public abstract class ResultPaneUnit {

	private static final int DEFAULT_FONT_SIZE = 12;
	protected static final int PANE_WIDTH = 576;
	private static final int PANE_HEIGHT = 206;

	private String fontName = Font.MONOSPACED;
	private int    fontSize = DEFAULT_FONT_SIZE;

	//private ResultViewComponent parent;
	private JScrollPane basePane;
	private JTextArea textArea;

	protected ResultPaneUnit(final ResultViewComponent owner) {
		//this.parent = owner;
		this.textArea = new JTextArea();
		this.textArea.setName(RESULT_TEXT);
		this.textArea.setFont(new Font(this.fontName, Font.PLAIN, this.fontSize));
		this.textArea.setEditable(false);
		this.clearText();
		this.basePane = new JScrollPane();
		this.basePane.setViewportView(this.textArea);
		this.basePane.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
	}

	/**
	 * JScrollPaneインスタンスを返す
	 * @return JScrollPaneインスタンス
	 */
	protected JScrollPane basePane() {
		return this.basePane;
	}

	/**
	 * 結果表示テキストエリアにテキスト形式の計測結果を表示する
	 * @param text テキスト形式の計測結果
	 */
	protected void showText(final String text) {
		this.textArea.setText(text);
		this.basePane.setViewportView(this.textArea);
		//this.textArea.setCaretPosition(this.textArea.getText().length());
		//System.out.println("Result dimension = " + this.basePane.getWidth()
		//		+ ", " + this.basePane.getHeight());
	}

	/**
	 * 結果表示テキストエリアの表示を初期化する
	 */
	protected void clearText() {
		this.textArea.setText("");
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	protected GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.basePane));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	protected GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.basePane));
	}
}
