package keisuke.swing;

import static keisuke.count.util.FileNameUtil.getCanonicalOrAbsolutePath;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ファイル設定GUI部品基底クラス
 */
abstract class AbstractChooseFileUnit {

	private JLabel label;
	private JTextField textField;
	private JButton browseButton;
	private String chooserName;
	private ChooseFileEnum chooseEnum;
	// 直近のディレクトリ記憶用
	private File lastWorkingDir = null;

	AbstractChooseFileUnit() {	}

	/**
	 * 初期化処理
	 * @param labeltext ラベル表示文字列
	 * @param fieldname テキストフィールドインスタンス名
	 * @param buttonname 参照ボタンインスタンス名
	 * @param choosername ダイアログタイトル
	 * @param choosetype 選択目的の列挙子
	 */
	protected void initialize(final String labeltext, final String fieldname,
			final String buttonname, final String choosername, final ChooseFileEnum choosetype) {
		this.label = new JLabel(labeltext);
		this.textField = new JTextField();
		this.textField.setName(fieldname);
		this.textField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(final DocumentEvent e) {
				observeFileName();
			}

			public void insertUpdate(final DocumentEvent e) {
				observeFileName();
			}

			public void changedUpdate(final DocumentEvent e) {
				observeFileName();
			}
		});
		//this.textField.setEditable(false);
		this.browseButton = new JButton();
		this.browseButton.setName(buttonname);
		if (choosetype == ChooseFileEnum.SAVE) {
			this.browseButton.setText("save as...");
		} else {
			this.browseButton.setText("browse...");
		}
		this.browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfBrowseButton(event);
			}
		});
		this.chooserName = choosername;
		this.chooseEnum = choosetype;
	}

	/**
	 * ラベルに名前を付ける
	 * @param name ラベル名
	 */
	protected void setLabelName(final String name) {
		this.label.setName(name);
	}

	/**
	 * ラベルインスタンスを返す
	 * @return ラベルインスタンス
	 */
	protected JLabel label() {
		return this.label;
	}

	/**
	 * テキストフィールドインスタンスを返す
	 * @return テキストフィールドインスタンス
	 */
	protected JTextField textField() {
		return this.textField;
	}

	/**
	 * ボタンインスタンスを返す
	 * @return ボタンインスタンス
	 */
	protected JButton button() {
		return this.browseButton;
	}

	/**
	 * 参照ボタンを押された時のコールバック処理
	 * @param event 発生したイベント
	 */
	protected void doActionOfBrowseButton(final ActionEvent event) {
		//String name = ((JButton) event.getSource()).getName();
		this.updateToChoose();
		File file = this.chooseFile(this.chooserName);
		if (file != null) {
			this.handleFile(file);
		}
	}

	/**
	 * ファイル選択画面を開いて選択されたファイルを返す
	 * @param name ファイル選択画面を開くイベントを発生させたソースを示す文字列
	 * @return 選択されたFileインスタンス
	 */
	protected File chooseFile(final String name) {
		JFileChooser chooser = new JFileChooser();
		chooser.setName(name);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(this.lastWorkingDir); // if null, set default
		int stat = -1;
		if (this.chooseEnum == ChooseFileEnum.SAVE) {
			stat = chooser.showSaveDialog(this.mainWindow());
		} else {
			stat = chooser.showOpenDialog(this.mainWindow());
		}
		if (stat == JFileChooser.APPROVE_OPTION) {
			this.lastWorkingDir = chooser.getCurrentDirectory();
			return chooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * 選択したファイル名を返す
	 * @return ファイル名
	 */
	protected String choosedFileName() {
		return this.textField.getText();
	}

	/**
	 * ファイル選択画面を開くときの親画面を返す
	 * 親画面はこのクラスでは不明なので、具象化クラスで実装する
	 * @return 親画面インスタンス
	 */
	abstract JFrame mainWindow();

	/**
	 * ファイル指定に直接入力があった時の状態更新処理
	 */
	void observeFileName() { };

	/**
	 * ファイル選択画面を開く時の状態更新処理
	 */
	void updateToChoose() { };

	/**
	 * ファイルが選択された時の状態更新処理
	 * @param file 選択されたファイル
	 */
	void handleFile(final File file) {
		this.textField().setText(getCanonicalOrAbsolutePath(file));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.label)
					.addComponent(this.textField)
					.addComponent(this.browseButton));
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
				.addComponent(this.label))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.textField))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.browseButton));
	}
}
