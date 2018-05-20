package keisuke.swing;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Alignment;


/**
 * 計測結果表示関連GUI部品グループ
 */
public abstract class ResultViewComponent {

	private AbstractMainGUI root;
	private ResultPaneUnit resultPane;
	private DisplayRadioUnit displayRadio;
	private SeparatorUnit separator;
	private SaveAsButtonUnit saveAsBox;
	private String message;
	private String currentFormattedText;

	protected ResultViewComponent(final AbstractMainGUI owner) {
		this.root = owner;
		this.displayRadio = new DisplayRadioUnit(this);
		this.separator = new SeparatorUnit();
		this.saveAsBox = new SaveAsButtonUnit(this);
		this.displayRadio.setDisable();
		this.saveAsBox.setDisable();
	}

	/**
	 * 結果表示GUI部品を返す
	 * @return 結果表示GUI部品インスタンス
	 */
	protected ResultPaneUnit resultPane() {
		return this.resultPane;
	}

	/**
	 * 結果表示GUI部品インスタンスを設定する
	 * @param pane 結果表示GUI部品インスタンス
	 */
	protected void setResultPane(final ResultPaneUnit pane) {
		this.resultPane = pane;
	}

	/**
	 * 表示形式ラジオボタン部品を返す
	 * @return 表示形式ラジオボタン部品インスタンス
	 */
	protected DisplayRadioUnit displayRadio() {
		return this.displayRadio;
	}

	/**
	 * 保存先指定GUI部品を返す
	 * @return 保存先指定GUI部品インスタンス
	 */
	protected SaveAsButtonUnit saveAsBox() {
		return this.saveAsBox;
	}

	/**
	 * ファイル選択画面を開くときの親画面を返す
	 * @return 親画面インスタンス
	 */
	protected JFrame mainWindow() {
		return this.root.mainWindow();
	}

	/**
	 * 計測結果を保存先ファイルに書き出す
	 */
	protected void saveResult() {
		this.root.saveResult(this.saveAsBox.choosedFile());
		this.saveAsBox.updateToSaved(this.message);
	}

	/**
	 * 保存状態を示すメッセージを設定する
	 * @param msg メッセージ文字列
	 */
	protected void setSavingMessage(final String msg) {
		this.message = msg;
	}

	/**
	 * 出力結果を表示する
	 * @param text 出力フォーマットで整形された文字列
	 */
	protected void refreshWith(final String text) {
		this.currentFormattedText = text;
		// 計測直後の表示はフォーマット済みテキスト形式を使う
		this.displayRadio.selectFormattedStyle();
		this.displayWithFormattedStyle();
		//if (DisplayStyleEnum.FORMAT.equals(this.displayRadio.getSelectedStyle())) {
		//	this.displayWithFormattedStyle();
		//} else {
		//	this.displayWithTableStyle();
		//}
		this.displayRadio.setEnable();
		this.saveAsBox.reset();
		this.saveAsBox.setEnable();
	}

	/**
	 * 計測結果をテキスト形式で表示する
	 */
	protected void displayWithFormattedStyle() {
		this.resultPane.showText(this.currentFormattedText);
	}

	/**
	 * 計測結果をテーブル形式で表示する
	 */
	protected abstract void displayWithTableStyle();


	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addComponent(this.resultPane.basePane()))
					//.addGroup(this.resultPane.createGroupForVerticalGroup(container)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(this.displayRadio.createGroupForVerticalGroup(container)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(this.separator.createGroupForVerticalGroup(container)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(this.saveAsBox.createGroupForVerticalGroup(container)));
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
					.addComponent(this.resultPane.basePane())
					//.addGroup(this.resultPane.createGroupForHorizontalGroup(container))
					.addGroup(this.displayRadio.createGroupForHorizontalGroup(container))
					.addGroup(this.separator.createGroupForHorizontalGroup(container))
					.addGroup(this.saveAsBox.createGroupForHorizontalGroup(container)));
	}
}
