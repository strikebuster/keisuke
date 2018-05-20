package keisuke.swing;

import static keisuke.swing.GUIConstant.XML_BUTTON;
import static keisuke.swing.GUIConstant.XML_CHOOSER;
import static keisuke.swing.GUIConstant.XML_TEXT;

import javax.swing.JFrame;

/**
 * 言語定義XMLファイル設定GUI部品
 */
public class ChooseXmlUnit extends AbstractChooseFileUnit {

	private CommandComponent parent;

	ChooseXmlUnit(final CommandComponent owner) {
		this.parent = owner;
		super.initialize("use xml:", XML_TEXT, XML_BUTTON, XML_CHOOSER, ChooseFileEnum.OPEN);
	}

	/** {@inheritDoc} */
	protected JFrame mainWindow() {
		return this.parent.mainWindow();
	}

	/**
	 * {@inheritDoc}
	 * 選択したファイル名に変更があった時には親部品の状態更新処理を呼ぶ
	 */
	@Override
	void observeFileName() {
		this.parent.updateStatus();
	}

	/*
	@Override
	void handleFile(final File file) {
		super.handleFile(file);
	}
	*/

	/**
	 * 選択したファイル名を返す
	 * @return ファイル名
	 */
	protected String xmlFileName() {
		return super.choosedFileName();
	}

}
