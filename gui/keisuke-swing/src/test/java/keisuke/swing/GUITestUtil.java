package keisuke.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.fest.swing.timing.Timeout;

import keisuke.count.CountTestUtil;

import static keisuke.count.util.FileNameUtil.getCanonicalOrAbsolutePath;

/**
 * Swing画面テスト用ユーティリティ
 */
public class GUITestUtil extends CountTestUtil {

	public static final long SLEEPTIME = 500L;
	public static final long SLEEPLONGTIME = 1500L;
	public static final long SLEEPVERYLONGTIME = 2500L;
	public  static final long WAITTIME = 3000L;
	public static final int RETRY_TIMES = 3;

	private static final int HUNDRED_PERCENT = 100;
	// FileChooserを使うか使わないか指定するフラグ
	//   FileChooserが自動実行中に頻繁に不正状態になるため
	private static boolean usingDialog = false;

	public static boolean usingDialog() {
		return usingDialog;
	}
	/**
	 * 指定されたミリ秒数ほど処理を停止して待つ
	 * @param milisec ミリ秒数
	 */
	public static void sleep(final long milisec) {
		try {
			Thread.sleep(milisec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 親Frameの子画面としてファイル選択ダイアログを開く操作を割り当てられたボタンを押し、
	 * 指定した基準ディレクトリの指定したファイルを選択した状態にする。
	 * @param frame 親のFrameFixture
	 * @param browseBtn FileChooserダイアログを開くボタンのJButtonFixture
	 * @param dialogName FileChooserダイアログの名前
	 * @param dirName 選択する基準ディレクトリ名
	 * @param fileName 選択するファイル名
	 * @param textBox 選択したファイルパスを記録するJTextComponentFixture
	 */
	public static void chooseFile(final FrameFixture frame,
			final JButtonFixture browseBtn, final String dialogName,
			final String dirName, final String fileName,
			final JTextComponentFixture textBox) {

		if (usingDialog) {
			chooseFileByUsingDialog(frame, browseBtn, dialogName, dirName, fileName);
		} else {
			chooseFileByPuttingInTextField(textBox, dirName, fileName);
		}
	}

	private static void chooseFileByPuttingInTextField(final JTextComponentFixture textBox,
			final String dirName, final String fileName) {

		if (fileName == null || fileName.isEmpty()) {
			System.out.println("[TEST] not to use file-choose-dialog: set directly file="
					+ "<cancel>");
			textBox.setText("");
			return;
		}
		File file;
		if (dirName == null || dirName.isEmpty() || dirName.equals(".")) {
			file = new File(fileName);
		} else {
			file = new File(dirName, fileName);
		}
		String path = getCanonicalOrAbsolutePath(file);
		System.out.println("[TEST] not to use file-choose-dialog: set directly file=" + path);
		textBox.setText(path);
	}

	/**
	 * 親Frameの子画面としてファイル選択ダイアログを開き、指定した
	 * ファイルを選択した状態にする。
	 *
	 * @param frame 親のFrameFixture
	 * @param browseBtn FileChooserダイアログを開くボタンのJButtonFixture
	 * @param dialogName FileChooserダイアログの名前
	 * @param dirName 選択する基準ディレクトリ名
	 * @param fileName 選択するファイル名
	 */
	public static void chooseFileByUsingDialog(final FrameFixture frame,
			final JButtonFixture browseBtn, final String dialogName,
			final String dirName, final String fileName) {

		sleep(SLEEPVERYLONGTIME); // next filechooser is very often dead
		browseBtn.click();
		sleep(SLEEPLONGTIME);
		JFileChooserFixture fileChooser = findFileChooserWithRetrying(frame,
				dialogName, RETRY_TIMES);
		sleep(SLEEPTIME);

		if (fileName == null || fileName.isEmpty()) {
			try {
				// name == "取消" を探すが "Cancel"のときComponentLookupException
				fileChooser.cancelButton().click();
			} catch (ComponentLookupException ex) {
				JButton button = (JButton) fileChooser.robot.finder()
						.find(new GenericTypeMatcher<JButton>(javax.swing.JButton.class) {
							@Override
							protected boolean isMatching(final JButton button) {
								return "Cancel".equals(button.getText());
							}
						});
				JButtonFixture buttonFixture = new JButtonFixture(fileChooser.robot, button);
				buttonFixture.click();
			}
		} else {
			if (dirName != null && !dirName.isEmpty()) {
				fileChooser.focus().setCurrentDirectory(new File(dirName));
			}
			sleep(SLEEPTIME);
			fileChooser.fileNameTextBox().focus().setText(fileName);
			sleep(SLEEPTIME);
			fileChooser.approveButton().focus().click();
		}
		sleep(SLEEPLONGTIME);
	}

	/**
	 * 指定されたFrameFixtureの子画面として開くFileChooserFixtureを見つけて返す。
	 * 子画面を指定された名前で探し、見つからなかったときは指定されたリトライ回数
	 * だけ探しなおす。リトライ間隔は１秒。
	 * @param frame 親のFrameFixture
	 * @param name 探すFileChooserの名称
	 * @param times リトライ回数
	 * @return FileChooserFixtureインスタンス
	 */
	public static JFileChooserFixture findFileChooserWithRetrying(final FrameFixture frame,
			final String name, final int times) {

		try {
			return findFileChooserSimply(frame, name);
		} catch (ComponentLookupException ex) {
			if (times <= 1) {
				System.out.println("[TEST] give up retrying to find fileChooser.");
				throw ex;
			}
			System.out.println("[TEST] retry to find fileChooser, remaining times = " + (times - 1));
			return findFileChooserWithRetrying(frame, name, times - 1);
		}
	}

	private static JFileChooserFixture findFileChooserSimply(final FrameFixture frame,
			final String name) {

		sleep(SLEEPVERYLONGTIME);
		JFileChooserFixture fileChooser = frame.fileChooser(name);
		//sleep(ONE_THOUSAND_MILISEC);
		fileChooser.requireEnabled(Timeout.timeout(WAITTIME));
		return fileChooser;
	}

	/**
	 * JTableHeaderのColumn順にラベル文字列をListに格納して返す
	 * @param header JTableHeaderインスタンス
	 * @return ラベル文字列のList
	 */
	public static List<String> labelsOf(final JTableHeader header) {
		List<String> list = new ArrayList<String>();
		if (header == null) {
			return list;
		}
		TableColumnModel model = header.getColumnModel();
		int size = model.getColumnCount();
		for (int i = 0; i < size; i++) {
			TableColumn column = model.getColumn(i);
			list.add((String) column.getHeaderValue());
		}
		return list;
	}

	/**
	 * JTableHeaderのColumn順にカラム幅の％比率を配列に格納して返す
	 * @param header JTableHeaderインスタンス
	 * @return カラム幅の％比率の配列
	 */
	public static int[] eachPercentageOf(final JTableHeader header) {
		if (header == null) {
			return new int[0];
		}
		TableColumnModel model = header.getColumnModel();
		int total = model.getTotalColumnWidth();
		int size = model.getColumnCount();
		//System.err.println("expected total=" + total);
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			int w = model.getColumn(i).getWidth();
			array[i] = Math.round((float) HUNDRED_PERCENT * w / total);
		}
		return array;
	}

	/**
	 * JTableHeaderのColumn順にカラム幅を配列に格納して返す
	 * @param header JTableHeaderインスタンス
	 * @return カラム幅の配列
	 */
	public static int[] eachWidthOf(final JTableHeader header) {
		if (header == null) {
			return new int[0];
		}
		TableColumnModel model = header.getColumnModel();
		int size = model.getColumnCount();
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			TableColumn column = model.getColumn(i);
			//list.add(new Integer(column.getPreferredWidth()));
			array[i] = column.getWidth();
		}
		return array;
	}

	/**
	 * JTableHeaderの全幅に対するカラム毎の指定％比率から推定したカラム幅を格納して返す
	 * @param header JTableHeaderインスタンス
	 * @param percents カラム毎の幅の％比率を格納した配列
	 * @return 推定したカラム幅の配列
	 */
	public static int[] eachCalculatedWidthOf(final JTableHeader header, final int[] percents) {
		if (header == null) {
			return new int[0];
		}
		TableColumnModel model = header.getColumnModel();
		int total = model.getTotalColumnWidth();
		//System.err.println("expected total=" + total);
		int size = percents.length;
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = Math.round((float) total * percents[i] / HUNDRED_PERCENT);
		}
		return array;
	}

	/**
	 * パス文字列に含まれる'/'をWindowsで実行された場合は'\'に変換した文字列にして返す。
	 * Windowsでなければ変換なしでそのままの文字列を返す。
	 * @param orgPath パスの文字列
	 * @return OS依存のパス区切り文字に変換後の文字列
	 */
	public static String pathForLocalSystem(final String orgPath) {
		if (orgPath == null || orgPath.isEmpty()) {
			return orgPath;
		}
		if (nameOfSystemOS().startsWith("Windows")) {
			return orgPath.replaceAll("/", "\\\\");
		} else {
			return orgPath;
		}
	}
}
