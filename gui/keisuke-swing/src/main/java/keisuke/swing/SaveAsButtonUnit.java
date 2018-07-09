package keisuke.swing;

import static keisuke.swing.GUIConstant.SAVE_BUTTON;
import static keisuke.swing.GUIConstant.SAVE_CHOOSER;
import static keisuke.swing.GUIConstant.SAVE_LABEL;
import static keisuke.swing.GUIConstant.SAVE_TEXT;

import java.awt.Container;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.GroupLayout.Alignment;

/**
 * 計測結果出力ファイル設定GUI部品
 */
public class SaveAsButtonUnit extends AbstractChooseFileUnit {

	private static final String INIT_MSG = "save result if you want.";
	private ResultViewComponent parent;
	private File choosedFile = null;
	private HiddenSavingElement hiddenSaving;

	SaveAsButtonUnit(final ResultViewComponent owner) {
		this.parent = owner;
		this.hiddenSaving = new HiddenSavingElement(this);
		super.initialize(INIT_MSG, SAVE_TEXT, SAVE_BUTTON, SAVE_CHOOSER, ChooseFileEnum.SAVE);
		super.setLabelName(SAVE_LABEL);
		this.textField().setEditable(false);
		this.reset();
	}

	/**
	 * 選択されたファイルのFileインスタンスを返す
	 * @return Fileインスタンス
	 */
	protected File choosedFile() {
		return this.choosedFile;
	}

	/**
	 * 保存先のファイル名を返す
	 * @return ファイル名
	 */
	protected String saveFileName() {
		return super.choosedFileName();
	}

	/**
	 * 部品の表示を初期化する
	 */
	protected void reset() {
		this.label().setText(INIT_MSG);
		this.textField().setText("");
	}

	/**
	 * 部品を無効化する
	 */
	public void setDisable() {
		this.button().setEnabled(false);
		this.textField().setEnabled(false);
		this.label().setEnabled(false);
	}

	/**
	 * 部品を有効化する
	 */
	protected void setEnable() {
		this.button().setEnabled(true);
		this.textField().setEnabled(true);
		this.label().setEnabled(true);
	}

	/** {@inheritDoc} */
	protected JFrame mainWindow() {
		return this.parent.mainWindow();
	}

	/**
	 * {@inheritDoc}
	 * 選択されたファイルへ結果を保存する
	 */
	@Override
	void handleFile(final File file) {
		super.handleFile(file);
		this.choosedFile = file;
		this.parent.saveResult();
	}

	/**
	 * {@inheritDoc}
	 * 保存先ファイル名のテキストボックスを無効化する
	 * 保存結果メッセージを無効化する
	 */
	@Override
	void updateToChoose() {
		this.label().setEnabled(false);
		this.textField().setEnabled(false);
	}

	/**
	 * 保存後の表示状態にする
	 * 保存先ファイル名を表示する
	 * 保存結果メッセージを表示する
	 * @param msg 保存結果メッセージ
	 */
	protected void updateToSaved(final String msg) {
		//this.button().setEnabled(true);
		this.label().setText(msg);
		this.label().setEnabled(true);
		this.textField().setText(this.saveFileName());
		this.textField().setEnabled(true);
	}

	/*
	boolean isSaved() {
		if (this.saveFileName().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	*/

	/**
	 * {@inheritDoc}
	 * 通常のファイル選択GUI部品とパーツの並びが異なる
	 * ボタン、テキストボックス、ラベルの順になる
	 */
	@Override
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.button())
					.addComponent(this.textField())
					.addComponent(this.label())
					.addComponent(this.hiddenSaving.hiddenField()));
	}

	/**
	 * {@inheritDoc}
	 * 通常のファイル選択GUI部品とパーツの並びが異なる
	 * ボタン、テキストボックス、ラベルの順になる
	 */
	@Override
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.button()))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.textField())
				.addComponent(this.hiddenSaving.hiddenField()))
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.label()));
	}
}
