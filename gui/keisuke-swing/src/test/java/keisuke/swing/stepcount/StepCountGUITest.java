package keisuke.swing.stepcount;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.table.JTableHeader;

import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.timing.Timeout;
import org.junit.Test;

import static keisuke.swing.GUIConstant.*;
import static keisuke.swing.GUITestUtil.*;
import static keisuke.swing.stepcount.StepCountGUIConstant.*;
import static keisuke.swing.stepcount.StepCountTestUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test class of StepCount GUI.
 */
public final class StepCountGUITest extends FestSwingJUnitTestCase {

	private static final float UPPER_RATIO = 1.1f;
	private static final float LOWER_RATIO = 0.9f;

	private static final int FORMAT_TEXT_IDX = 0;
	private static final int FORMAT_CSV_IDX = 1;
	private static final int FORMAT_EXCEL_IDX = 2;
	private static final int FORMAT_XML_IDX = 3;
	private static final int FORMAT_JSON_IDX = 4;

	private static final int SORT_ON_IDX = 0;
	private static final int SORT_OS_IDX = 1;
	private static final int SORT_OFF_IDX = 2;

	private FrameFixture frame;

	@Override
	protected void onSetUp() {
		//sleep(SLEEPLONGTIME);
		this.startGUI();
		sleep(SLEEPVERYLONGTIME);
	}

	@Override
	protected void onTearDown() {
		super.onTearDown();
		sleep(SLEEPLONGTIME);
		//this.frame.cleanUp();
		//sleep(SLEEPTIME);
		//this.frame = null;
	}

	@RunsInEDT
	private static JFrame createGUI() {
		return GuiActionRunner.execute(new GuiQuery<JFrame>() {
			protected JFrame executeInEDT() {
				return new StepCountGUI().mainWindow();
			}
		});
	}

	private void startGUI() {
		this.frame = new FrameFixture(robot(), createGUI());
		this.frame.show();
		this.frame.robot.waitForIdle();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeFewFromChoosedSources() {
		System.out.println("## StepCountGUI ## removeFewFromChoosedSources ##");

		// 初期状態の削除ボタンの検査
		boolean stat = frame.button(REMOVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		// ソースリストに１行追加
		String srcRoot1 = "test/data/java/root1";
		String[] fileNames1st = {srcRoot1};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot1), fileNames1st, frame.list(SOURCE_LIST));
		// 追加後に削除ボタンの状態を検査
		stat = frame.button(REMOVE_BUTTON).requireEnabled(Timeout.timeout(WAITTIME))
				.target.isEnabled();
		assertThat(stat, is(true));

		// ソースリストに複数行を追加
		String srcRoot21 = "test/data/java/root2/src/jp";
		String srcRoot22 = "test/data/java/root2/src/JavaCutter.java";
		String srcRoot23 = "test/data/java/root2/src/Status.java";
		String[] fileNames2nd = {srcRoot21, srcRoot22, srcRoot23};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot21), fileNames2nd, frame.list(SOURCE_LIST));

		// ソースリストの内容を検査
		String[] actual = frame.list(SOURCE_LIST).requireEnabled(Timeout.timeout(WAITTIME)).contents();
		//System.out.println("Before:\n" + contentOf(actual));
		// check choosed sources
		if (nameOfSystemOS().startsWith("Windows")) {
			// maybe
			assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot1)),
					endsWith(pathForLocalSystem(srcRoot22)),
					endsWith(pathForLocalSystem(srcRoot21)),
					endsWith(pathForLocalSystem(srcRoot23)))));
		} else {
			assertThat(actual, is(array(endsWith(srcRoot1), endsWith(srcRoot22),
					endsWith(srcRoot23), endsWith(srcRoot21))));
		}

		// 選択＆削除
		int[] removeIndecies = {1, 2};
		frame.list(SOURCE_LIST).selectItems(removeIndecies);
		sleep(SLEEPTIME);
		frame.button(REMOVE_BUTTON).click();
		sleep(SLEEPTIME);
		// 削除後のソースリストの内容を検査
		actual = frame.list(SOURCE_LIST).contents();
		//System.out.println("After removing 2nd and 3rd:\n" + contentOf(actual));
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot1)),
					endsWith(pathForLocalSystem(srcRoot23)))));
		} else {
			assertThat(actual, is(array(endsWith(srcRoot1), endsWith(srcRoot21))));
		}
		// 一部削除後のボタンの状態を検査
		stat = frame.button(REMOVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void removeAllFromChoosedSources() {
		System.out.println("## StepCountGUI ## removeAllFromChoosedSources ##");

		// ソースリストに１行追加
		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).requireEnabled(Timeout.timeout(WAITTIME)).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));

		// 選択＆削除して、ソースリストが空であること検査
		int[] removeIndecies = {0};
		frame.list(SOURCE_LIST).selectItems(removeIndecies);
		sleep(SLEEPTIME);
		frame.button(REMOVE_BUTTON).click();
		sleep(SLEEPTIME);
		actual = frame.list(SOURCE_LIST).contents();
		assertThat(actual, is(arrayWithSize(0)));

		// 全行削除後のボタンの状態を検査
		boolean stat = frame.button(REMOVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void chooseOneSourceAndCountJavaAndSave() {
		System.out.println("## StepCountGUI ## chooseOneSourceAndCountJavaAndSave ##");

		// 初期状態のボタンの状態を検査
		boolean stat = frame.button(REMOVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		// ソースリストに１行追加
		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).requireEnabled(Timeout.timeout(WAITTIME)).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));

		// ソース選択後のボタンの状態を検査
		stat = frame.button(REMOVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(false));

		// カウント結果を検査
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java.txt");
		assertThat(text, is(equalTo(textContentOf(expected))));

		// 計測後のボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		String path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, isEmptyString());
		String msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("if you want"));

		// 保存結果のファイルを検査
		String saveFile = "test/out/stepcountGUI_java.txt";
		removeFile(saveFile);
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				".", saveFile, frame.textBox(HIDDEN_SAVING));
		assertThat(rawContentOf(new File(saveFile)), is(equalTo(textContentOf(expected))));

		// 保存後のボタン・ラベルなどの状態を検査
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(true));
		path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, endsWith(pathForLocalSystem(saveFile)));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(true));
		msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("finish"));
		// 他のボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaAndDisplayTableStyle() {
		System.out.println("## StepCountGUI ## countJavaAndDisplayTableStyle ##");
		String srcRoot = "test/data/java/root1";

		// ソースリストに１行追加
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actualSrc = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actualSrc));
		assertThat(actualSrc, is(array(endsWith(pathForLocalSystem(srcRoot)))));

		// ソース選択後のボタンの状態を検査
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));

		// カウント結果を検査
		URL expected = this.getClass().getResource("StepCount_java.txt");
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		assertThat(text, is(equalTo(textContentOf(expected))));

		// 計測後のボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));

		// テーブル形式に表示変更した結果を検査
		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);
		JTableFixture table = frame.table(RESULT_TABLE).requireEnabled(Timeout.timeout(WAITTIME));
		// テーブルヘッダーを検査
		JTableHeader header = table.tableHeader().target;
		List<String> actualHead = labelsOf(header);
		//System.out.println(actualHead);
		String[] expectedHead = {"ファイル", "種類", "カテゴリ", "実行", "空行", "ｺﾒﾝﾄ", "合計"};
		assertThat(actualHead, contains(expectedHead));
		// テーブルデータを検査
		String[][] actualData = table.contents();
		//System.out.println(contentOf(actualData));
		URL expectedData = this.getClass().getResource("StepCount_java.csv");
		assertThat(contentOf(actualData), is(equalTo(contentOf(expectedData))));
		// テーブルの列幅を検査
		int[] actualWidths = eachPercentageOf(header);
		//System.out.println("actual=" + Arrays.toString(actualWidths));
		int[] expectedWitdhs = COL_WIDTH_PERCENTAGE;
		//System.out.println("specified=" + Arrays.toString(expectedWitdhs));
		for (int i = 0; i < expectedWitdhs.length; i++) {
			double lower = expectedWitdhs[i] * LOWER_RATIO;
			double upper = expectedWitdhs[i] * UPPER_RATIO;
			assertThat((double) actualWidths[i], is(closeTo(lower, upper)));
			//System.out.println(actualWidths[i] + " is expected between " + lower + " and " + upper);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaThenSaveAndDoAgainUsingShowDirAndCancelSaving() {
		System.out.println("## StepCountGUI ## countJavaThenSaveAndDoAgainUsingShowDirAndCancelSaving ##");

		// ソースリストに１行追加
		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actualSrc = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actualSrc));
		assertThat(actualSrc, is(array(endsWith(pathForLocalSystem(srcRoot)))));

		// 計測実施
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME); // fileChooser often dead
		// 計測結果を保存し検査
		String saveFile1st = "test/out/stepcountGUI_java_again_1st.txt";
		removeFile(saveFile1st);
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				".", saveFile1st, frame.textBox(HIDDEN_SAVING));
		URL expected1st = this.getClass().getResource("StepCount_java.txt");
		assertThat(rawContentOf(new File(saveFile1st)), is(equalTo(textContentOf(expected1st))));
		// 保存後の状態を検査
		boolean stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(true));
		String path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, endsWith(pathForLocalSystem(saveFile1st)));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(true));
		String msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("finish"));

		// テーブル表示にしておく
		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);
		stat = frame.table(RESULT_TABLE).requireEnabled(Timeout.timeout(WAITTIME))
				.target.isEnabled();
		assertThat(stat, is(true));
		// 表示形式を切り替えても保存メッセージは変わらない
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(true));
		path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, endsWith(pathForLocalSystem(saveFile1st)));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(true));
		msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("finish"));

		// ShowDirをチェックし再計測
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPLONGTIME); // often gets false
		boolean checked = frame.checkBox(SHOWDIR_CHECK).target.isSelected();
		assertThat(checked, is(true));
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);

		// 再計測後に表示がテキストに戻っているか
		stat = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME))
				.target.isEnabled();
		assertThat(stat, is(true));

		// 再計測後に保存部品がリセットされているか検査
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(true));
		path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, isEmptyString());
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(true));
		msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("if you want"));

		// 再検索結果を保存し検査
		String saveFile2nd = "test/out/stepcountGUI_java_again_2nd.txt";
		removeFile(saveFile2nd);
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				".", saveFile2nd, frame.textBox(HIDDEN_SAVING));
		URL expected2nd = this.getClass().getResource("StepCount_java_showdir.txt");
		assertThat(rawContentOf(new File(saveFile2nd)), is(equalTo(textContentOf(expected2nd))));
		// 保存後の状態を検査
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(true));
		path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, is(endsWith(pathForLocalSystem(saveFile2nd))));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(true));
		msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("finish"));

		// 再度保存ダイアログを開くが取消
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				null, null, frame.textBox(HIDDEN_SAVING));
		// 保存ダイアログ取消し後の状態を検査
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(false));
		path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, endsWith(pathForLocalSystem(saveFile2nd)));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(false));
		msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("finish"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingCsvFormat() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormat ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingCsvFormatWithShowDir() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatWithShowDir ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		//String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);

		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPTIME);
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);

		URL expected = this.getClass().getResource("StepCount_java_showdir.csv");
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingCsvFormatWhenFilesAreGiven() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatWhenFilesAreGiven ##");

		String srcFile1 = "test/data/java/root1/src/StringUtil.java";
		String srcFile2 = "test/data/java/root1/test/Utils.java";
		String[] fileNames = {srcFile1, srcFile2};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				null, fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcFile1)),
				endsWith(pathForLocalSystem(srcFile2)))));
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java_files_showdir.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingCsvFormatAndSortOff() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatAndSortOff ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.comboBox(SORT_SELECT).selectItem(SORT_OFF_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("StepCount_java_nosort_win.csv");
		} else {
			expected = this.getClass().getResource("StepCount_java_nosort.csv");
		}
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingCsvFormatAndSortOS() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatAndSortOS ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.comboBox(SORT_SELECT).selectItem(SORT_OS_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("StepCount_java_sortOS_win.csv");
		} else {
			expected = this.getClass().getResource("StepCount_java_sortOS.csv");
		}
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingXmlFormatWithShowDir() {
		System.out.println("## StepCountGUI ## countJavaUsingXmlFormatWithShowDir ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_XML_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java_showdir.xml");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingJsonFormatWithShowDir() {
		System.out.println("## StepCountGUI ## countJavaUsingJsonFormatWithShowDir ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_JSON_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java_showdir.json");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingExcelFormatWithShowDir() {
		System.out.println("## StepCountGUI ## countJavaUsingExcelFormatWithShowDir ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_EXCEL_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String expectedText = MSG_EXCUSE_BINARY;
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		assertThat(text, is(equalTo(expectedText)));

		String saveFile = "test/out/stepcountGUI_java.xls";
		removeFile(saveFile);
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				".", saveFile, frame.textBox(HIDDEN_SAVING));
		sleep(SLEEPTIME);
		URL expected = this.getClass().getResource("StepCount_java_showdir.xls");
		assertThat(binaryContentOf(new File(saveFile)), is(binaryContentOf(expected)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCsvFormatWithShowDir() {
		System.out.println("## StepCountGUI ## "
				+ "countMiscSourcesIncludingMiscCommentsInSjisUsingCsvFormatWithShowDir ##");

		String srcRoot = "test/data/commentS/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		// エンコードを指定
		frame.textBox(ENCODING_TEXT).deleteText();
		frame.textBox(ENCODING_TEXT).enterText("Windows-31J");
		frame.checkBox(SHOWDIR_CHECK).click();
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);

		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_commentS_showdir.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));

		// エンコードを削除して再計測
		frame.textBox(ENCODING_TEXT).deleteText();
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		//エンコードがデフォルトのUTF-8になっても結果が同じだった
		//assertThat(text, is(not(equalTo(contentOf(expected)))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaUsingCustomRuleAndCsvFormatWithShowDir() {
		System.out.println("## StepCountGUI ## countJavaUsingCustomRuleAndCsvFormatWithShowDir ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));

		// Xml定義を選択
		String xmlFile = "test/data/ktestl2.xml";
		chooseFile(frame, frame.button(XML_BUTTON), XML_CHOOSER, ".", xmlFile, frame.textBox(XML_TEXT));
		//System.out.println(frame.textBox(XML_TEXT).text());
		assertThat(frame.textBox(XML_TEXT).text(), endsWith(pathForLocalSystem(xmlFile)));

		frame.checkBox(SHOWDIR_CHECK).click();
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);

		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("RuleCount_java_showdir.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));

		// Xml定義を削除して再度実行
		frame.textBox(XML_TEXT).deleteText();
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		expected = this.getClass().getResource("StepCount_java_showdir.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}
}
