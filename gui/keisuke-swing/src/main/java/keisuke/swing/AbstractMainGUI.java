package keisuke.swing;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

import keisuke.report.property.MessageDefine;

/**
 * Swing部品の集約クラスの基底クラス
 */
public abstract class AbstractMainGUI {

	private static final int WIN_WIDTH = 640;
	private static final int WIN_HEIGHT = 450;

	private MessageDefine msgDef;
	private JFrame mainWindow;
	private AbstractCommandComponent commander;
	private ResultViewComponent resultView;
	private byte[] result = null;

	public AbstractMainGUI() {
		installLnF();
		this.initMainWindow();
	}

	private static void installLnF() {
		try {
			String lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("fail to set 'CrossPlatformLookAndFeel': " + e.getMessage());
		}
	}

	/**
	 * GUIメイン画面の初期化
	 */
	private void initMainWindow() {
		this.mainWindow = new JFrame();
		this.mainWindow.setName("main_frame");
		this.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainWindow.setTitle("Title should be setted");
		this.mainWindow.setSize(WIN_WIDTH, WIN_HEIGHT);
	}

	/**
	 * メインウィンドウのJFrameインスタンスを返す
	 * @return メインウィンドウのJFrameインスタンス
	 */
	public JFrame mainWindow() {
		return this.mainWindow;
	}

	/**
	 * メインウィンドウの横幅を返す
	 * @return 横幅の長さ
	 */
	protected int width() {
		return (int) this.mainWindow.getSize().getWidth();
	}

	/**
	 * メッセージ定義インスタンスを返す
	 * @return メッセージ定義インスタンス
	 */
	protected MessageDefine messageDefine() {
		return this.msgDef;
	}

	/**
	 * 作成済みのメッセージ定義インスタンスを設定する
	 * @param messageDef メッセージ定義インスタンス
	 */
	protected void setMessageDefine(final MessageDefine messageDef) {
		this.msgDef = messageDef;
	}

	/**
	 * 計測指示GUI部品CommandComponentインスタンスを返す
	 * @return CommandComponentインスタンス
	 */
	protected AbstractCommandComponent commander() {
		return this.commander;
	}

	/**
	 * 計測指示GUI部品CommandComponentインスタンスを設定する
	 * @param commComp CommandComponentインスタンス
	 */
	protected void setCommander(final AbstractCommandComponent commComp) {
		this.commander = commComp;
	}

	/**
	 * 計測結果表示GUI部品ResultViewComponentのインスタンスを返す
	 * @return ResultViewComponentインスタンス
	 */
	protected ResultViewComponent resultView() {
		return this.resultView;
	}

	/**
	 * 計測結果表示GUI部品ResultViewComponentのインスタンスを設定する
	 * @param viewComp ResultViewComponentインスタンス
	 */
	protected void setResultView(final ResultViewComponent viewComp) {
		this.resultView = viewComp;
	}

	/**
	 * 画面部品のステータス更新の呼び出し。
	 */
	protected abstract void updateStatus();

	/**
	 * 計測実行して結果を表示する。
	 */
	protected abstract void showCountingResult();

	/**
	 * 計測指示ボタンを有効化
	 */
	public void setEnableToCount() {
		this.commander().setEnableToCount();
	}

	/**
	 * 計測指示ボタンを無効化
	 */
	public void setDisableToCount() {
		this.commander().setDisableToCount();
	}

	/**
	 * フォーマット済みの計測結果のバイト配列を設定する
	 * @param bytes バイト配列
	 */
	protected void setResultBytes(final byte[] bytes) {
		this.result = bytes;
	}

	/**
	 * フォーマット済みの計測結果のバイト配列を返す
	 * @return バイト配列
	 */
	protected byte[] resultBytes() {
		return this.result;
	}

	/**
	 * 計測結果を保存先ファイルに保存する
	 * @param file 保存先ファイル
	 */
	protected void saveResult(final File file) {
		FileSaver saver = new FileSaver(file);
		saver.save(this.result);
		this.resultView.setSavingMessage(saver.getMessage());
	}
}
