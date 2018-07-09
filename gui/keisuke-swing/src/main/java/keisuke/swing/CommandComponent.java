package keisuke.swing;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JFrame;

import keisuke.CommandOption;

/**
 * 計測指示GUI部品の基底クラス
 */
public abstract class CommandComponent {

	private AbstractMainGUI root;
	private SpecifyEncodingUnit encodingBox;
	private ChooseXmlUnit xmlBox;
	private SelectFormatUnit formatBox;
	private CountButtonElement countButton;

	/**
	 * 計測指示に関連する部品集約コンポーネントのコンストラクタ
	 * @param owner 画面部品集約のルート
	 * @param text 計測指示ボタンのテキスト
	 * @param option フォーマット種類を保持するCommandOptionインスタンス
	 */
	protected CommandComponent(final AbstractMainGUI owner, final String text, final CommandOption option) {
		this.root = owner;
		this.encodingBox = new SpecifyEncodingUnit(this);
		this.xmlBox = new ChooseXmlUnit(this);
		this.formatBox = new SelectFormatUnit(this, option);
		this.countButton = new CountButtonElement(this);
		this.countButton.setButtonText(text);
	}

	/**
	 * 画面部品集約ルートのインスタンスを返す
	 * @return 画面部品集約ルートのインスタンス
	 */
	protected AbstractMainGUI root() {
		return this.root;
	}

	/**
	 * エンコーディング指定部品を返す
	 * @return エンコーディング指定部品インスタンス
	 */
	protected SpecifyEncodingUnit encodingBox() {
		return this.encodingBox;
	}

	/**
	 * 言語定義XMLファイル選択部品を返す
	 * @return 言語定義XMLファイル選択部品インスタンス
	 */
	protected ChooseXmlUnit xmlBox() {
		return this.xmlBox;
	}

	/**
	 * 出力フォーマット選択部品を返す
	 * @return 出力フォーマット選択部品インスタンス
	 */
	protected SelectFormatUnit formatBox() {
		return this.formatBox;
	}

	/**
	 * 計測指示ボタン部品を返す
	 * @return 計測指示ボタン部品インスタンス
	 */
	protected CountButtonElement countButton() {
		return this.countButton;
	}

	/**
	 * 選択されている言語定義XMLファイル名を返す
	 * @return XMLファイル名
	 */
	public String xmlFileName() {
		return this.xmlBox.xmlFileName();
	}

	/**
	 * 選択されているエンコード種類を返す
	 * @return エンコード種類を示す文字列
	 */
	public String encoding() {
		return this.encodingBox.encoding();
	}

	/**
	 * 選択されているフォーマット種類を返す
	 * @return フォーマット種類を示す文字列
	 */
	public String format() {
		return this.formatBox().selectedValue();
	}

	/**
	 * 画面のJFameインスタンスを返す
	 * @return JFameインスタンス
	 */
	protected JFrame mainWindow() {
		return this.root.mainWindow();
	}

	/**
	 * 計測指示ボタンを有効状態にする
	 */
	public void setEnableToCount() {
		this.countButton.setEnable();
	}

	/**
	 * 計測指示ボタンを無効状態にする
	 */
	public void setDisableToCount() {
		this.countButton.setDisable();
	}

	/**
	 * 集約した子部品の状態変更があったときに当コンポーネント部品の状態を更新する
	 * 子部品encodingBox, xmlBox, formatBoxは自分に変更があるとこの関数を呼ぶ
	 * 集約ルートの状態更新を呼ぶ
	 */
	public void updateStatus() {
		this.root.updateStatus();
	}

	/**
	 * 計測指示ボタンが押されたときに呼び出される処理
	 * 集約ルートの計測・結果表示処理を呼ぶ
	 */
	protected void doCounting() {
		this.root.showCountingResult();
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public abstract GroupLayout.SequentialGroup createGroupForVerticalGroup(Container container);

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public abstract GroupLayout.SequentialGroup createGroupForHorizontalGroup(Container container);
}
