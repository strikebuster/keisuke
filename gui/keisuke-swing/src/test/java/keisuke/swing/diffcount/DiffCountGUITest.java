package keisuke.swing.diffcount;

import javax.swing.JFrame;
import javax.swing.table.JTableHeader;

import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.timing.Timeout;
import org.junit.Test;

import static keisuke.count.CountTestUtil.convertToTableArrayFrom;
import static keisuke.count.CountTestUtil.excelContentOf;
import static keisuke.count.CountTestUtil.htmlToRemoveMutableIdFrom;
import static keisuke.count.CountTestUtil.withoutHeadLines;
import static keisuke.count.diff.DiffCountTestConstant.HTML_IGNORE_LINES;
import static keisuke.count.diff.DiffCountTestConstant.TEXT_IGNORE_LINES;
import static keisuke.swing.GUIConstant.COUNT_BUTTON;
import static keisuke.swing.GUIConstant.ENCODING_TEXT;
import static keisuke.swing.GUIConstant.FORMAT_RADIO;
import static keisuke.swing.GUIConstant.FORMAT_SELECT;
import static keisuke.swing.GUIConstant.HIDDEN_SAVING;
import static keisuke.swing.GUIConstant.MSG_EXCUSE_BINARY;
import static keisuke.swing.GUIConstant.RESULT_TABLE;
import static keisuke.swing.GUIConstant.RESULT_TEXT;
import static keisuke.swing.GUIConstant.SAVE_BUTTON;
import static keisuke.swing.GUIConstant.SAVE_CHOOSER;
import static keisuke.swing.GUIConstant.SAVE_LABEL;
import static keisuke.swing.GUIConstant.SAVE_TEXT;
import static keisuke.swing.GUIConstant.TABLE_RADIO;
import static keisuke.swing.GUIConstant.XML_BUTTON;
import static keisuke.swing.GUIConstant.XML_CHOOSER;
import static keisuke.swing.GUIConstant.XML_TEXT;
import static keisuke.swing.GUITestUtil.SLEEPLONGTIME;
import static keisuke.swing.GUITestUtil.SLEEPTIME;
import static keisuke.swing.GUITestUtil.SLEEPVERYLONGTIME;
import static keisuke.swing.GUITestUtil.WAITTIME;
import static keisuke.swing.GUITestUtil.chooseFile;
import static keisuke.swing.GUITestUtil.labelsOf;
import static keisuke.swing.GUITestUtil.pathForLocalSystem;
import static keisuke.swing.GUITestUtil.sleep;
import static keisuke.swing.diffcount.DiffCountGUIConstant.*;
import static keisuke.swing.diffcount.DiffCountGUITestConstant.*;
import static keisuke.swing.diffcount.DiffCountTestUtil.actualTextOf;
import static keisuke.swing.diffcount.DiffCountTestUtil.deriveTotalAddedStepsFrom;
import static keisuke.swing.diffcount.DiffCountTestUtil.deriveTotalDeletedStepsFrom;
import static keisuke.swing.diffcount.DiffCountTestUtil.deriveTotalStatusFrom;
import static keisuke.util.TestUtil.contentOf;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.removeFile;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Test class of DiffCount GUI.
 */
public final class DiffCountGUITest extends FestSwingJUnitTestCase {

	private FrameFixture frame;

	@Override
	protected void onSetUp() {
		//sleep(SLEEPLONGTIME);
		this.startGUI();
		sleep(SLEEPLONGTIME);
	}

	@Override
	public void onTearDown() {
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
				return new DiffCountGUI().mainWindow();
			}
		});
	}

	private void startGUI() {
		this.frame = new FrameFixture(robot(), createGUI());
		this.frame.show();
		this.frame.robot.waitForIdle();
	}

	@Test
	public void countDiffJavaAndSave() {
		System.out.println("## DiffCountGUI ## countDiffJavaAndSave ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 初期の状態を検査
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SHOW_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(HIDE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 入力後にボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SHOW_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(HIDE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// 入力後にボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SHOW_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(HIDE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));

		// 計測前のトータル表示ラベルの状態を検査
		JLabelFixture total = frame.label(TOTAL_LABEL);
		stat = total.target.isEnabled();
		assertThat(stat, is(false));
		assertThat(total.target.getText(), is(allOf(containsString("状態[ ]"),
				containsString("追加行数[0]"), containsString("削除行数[0]"))));
		// 計測前の保存メッセージの状態を検査
		stat = frame.textBox(SAVE_TEXT).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.label(SAVE_LABEL).target.isEnabled();
		assertThat(stat, is(false));

		// 計測実行し結果を検査
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPVERYLONGTIME);
		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("DiffCount_java.txt");
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
		// 計測後のトータル表示ラベルの検査
		stat = total.target.isEnabled();
		assertThat(stat, is(true));
		URL expectedCsv = this.getClass().getResource("DiffCountTable_java_top.csv");
		String[][] expectedArray = convertToTableArrayFrom(contentOf(expectedCsv));
		String expectedStatus = deriveTotalStatusFrom(expectedArray);
		long expectedAdded = deriveTotalAddedStepsFrom(expectedArray);
		long expectedDeleted = deriveTotalDeletedStepsFrom(expectedArray);
		assertThat(total.target.getText(), is(allOf(containsString("状態[" + expectedStatus + "]"),
				containsString("追加行数[" + expectedAdded + "]"),
				containsString("削除行数[" + expectedDeleted + "]"))));
		// 計測後のボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SHOW_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(HIDE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		String path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, is(emptyString()));
		String msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("if you want"));

		// 保存結果のファイルを検査
		String saveFile = "test/out/diffcountGUI_java.txt";
		removeFile(saveFile);
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				".", saveFile, frame.textBox(HIDDEN_SAVING));
		assertThat(rawContentOf(new File(saveFile), withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));

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
		stat = frame.button(SHOW_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(HIDE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
	}

	@Test
	public void countDiffJavaAndDisplayTableStyleUsingShowAndHide() {
		System.out.println("## DiffCountGUI ## countDiffJavaAndDisplayTableStyleUsingShowAndHide ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// 計測実行しテーブル形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);
		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);
		JTableFixture table = frame.table(RESULT_TABLE).requireEnabled(Timeout.timeout(WAITTIME));
		// 表のヘッダーラベルの検査
		JTableHeader header = table.tableHeader().target;
		List<String> actualLabel = labelsOf(header);
		//System.out.println(actual);
		String[] expectedHeader = {"名前", "状態", "追加行数", "削除行数"};
		assertThat(actualLabel, contains(expectedHeader));
		// 表の内容の検査
		String[][] actual = table.contents();
		//System.out.println(contentOf(actual));
		URL expected = this.getClass().getResource("DiffCountTable_java_top.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
		// トータル表示ラベルの検査
		JLabelFixture total = frame.label(TOTAL_LABEL);
		boolean stat = total.target.isEnabled();
		assertThat(stat, is(true));
		String[][] expectedArray = convertToTableArrayFrom(contentOf(expected));
		String expectedStatus = deriveTotalStatusFrom(expectedArray);
		long expectedAdded = deriveTotalAddedStepsFrom(expectedArray);
		long expectedDeleted = deriveTotalDeletedStepsFrom(expectedArray);
		assertThat(total.target.getText(), is(allOf(containsString("状態[" + expectedStatus + "]"),
				containsString("追加行数[" + expectedAdded + "]"),
				containsString("削除行数[" + expectedDeleted + "]"))));

		// テーブル形式表示後のボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SHOW_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(HIDE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		String path = frame.textBox(SAVE_TEXT).target.getText();
		assertThat(path, is(emptyString()));
		String msg = frame.label(SAVE_LABEL).target.getText();
		assertThat(msg, containsString("if you want"));

		// ツリー展開表示
		frame.button(SHOW_BUTTON).click();
		sleep(SLEEPTIME);
		actual = table.contents();
		//System.out.println(contentOf(actual));
		URL expectedAll;
		if (nameOfSystemOS().startsWith("Windows")) {
			expectedAll = this.getClass()
					.getResource("DiffCountTable_java_all_win.csv");
		} else {
			expectedAll = this.getClass()
					.getResource("DiffCountTable_java_all.csv");
		}
		assertThat(contentOf(actual), is(equalTo(contentOf(expectedAll))));

		// ツリー折りたたみ表示
		frame.button(HIDE_BUTTON).click();
		sleep(SLEEPTIME);
		actual = table.contents();
		//System.out.println(contentOf(actual));
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffJavaWhenThereIsNoDifference() {
		System.out.println("## DiffCountGUI ## countDiffJavaWhenThereIsNoDifference ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root1";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// 計測実行しテーブル形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);
		// トータル表示ラベルの検査
		JLabelFixture total = frame.label(TOTAL_LABEL);
		assertThat(total.target.getText(), is(allOf(containsString("状態[変更なし]"),
				containsString("追加行数[0]"),
				containsString("削除行数[0]"))));
		// テーブル形式で表示
		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);

		JTableFixture table = frame.table(RESULT_TABLE);
		String[][] actual = table.contents();
		//System.out.println(contentOf(actual));
		URL expectedData = this.getClass()
				.getResource("DiffCountTable_java_nodiff_top.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expectedData))));
	}

	@Test
	public void countDiffJavaUsingTextFormat() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingTextFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// TEXTフォーマットを選択
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_TEXT_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("DiffCount_java.txt");
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormat() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingCsvFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// CSVフォーマットを選択
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("DiffCount_java_win.csv");
		} else {
			expected = this.getClass()
					.getResource("DiffCount_java.csv");
		}
		assertThat(actualTextOf(text, 0), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingExcelFormat() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingExcelFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// EXCELフォーマットを選択
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_EXCEL_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME); // very often ghost filechooser

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		String expectedText = MSG_EXCUSE_BINARY;
		assertThat(text, is(equalTo(expectedText)));

		// ファイル保存して検査
		String saveFile = "test/out/diffcountGUI_java.xls";
		removeFile(saveFile);
		chooseFile(frame, frame.button(SAVE_BUTTON), SAVE_CHOOSER,
				".", saveFile, frame.textBox(HIDDEN_SAVING));
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("DiffCount_java_win.xls");
		} else {
			expected = this.getClass()
					.getResource("DiffCount_java.xls");
		}
		assertThat(excelContentOf(new File(saveFile)), is(excelContentOf(expected)));
	}

	@Test
	public void countDiffJavaUsingXmlFormat() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingXmlFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// XMLフォーマットを選択
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_XML_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("DiffCount_java.xml");
		assertThat(actualTextOf(text, 0), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingJsonFormat() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingJsonFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// JSONフォーマットを選択
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_JSON_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("DiffCount_java_win.json");
		} else {
			expected = this.getClass()
					.getResource("DiffCount_java.json");
		}
		assertThat(actualTextOf(text, 0), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingHtmlFormat() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingHtmlFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// HTMLフォーマットを選択
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_HTML_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("DiffCount_java.html");
		assertThat(htmlToRemoveMutableIdFrom(actualTextOf(text, withoutHeadLines(HTML_IGNORE_LINES))),
				is(equalTo(htmlToRemoveMutableIdFrom(textContentOf(expected)))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInSjis() {
		System.out.println("## DiffCountGUI ## countDiffMiscSourcesIncludingMiscCommentsInSjis ##");
		String oldRoot = "test/data/commentS/root1";
		String newRoot = "test/data/commentS/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// encodingを選択
		frame.textBox(ENCODING_TEXT).deleteText();
		frame.textBox(ENCODING_TEXT).enterText("Windows-31J");
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("DiffCount_commentS.txt");
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));

		// encodingを削除して再実行
		frame.textBox(ENCODING_TEXT).deleteText();
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);
		text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		//エンコードがデフォルトのUTF-8になっても結果が同じだった
		//assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
		//		is(not(equalTo(contentOf(expected)))));
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCustomRule() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingCustomRule ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(oldRoot)));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(pathForLocalSystem(newRoot)));

		// 計測実行しテーブル形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);
		// テーブル形式で表示
		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);

		// Xml定義を選択
		String xmlFile = "test/data/ktestl2.xml";
		chooseFile(frame, frame.button(XML_BUTTON), XML_CHOOSER, ".", xmlFile, frame.textBox(XML_TEXT));
		//System.out.println(frame.textBox(XML_TEXT).text());
		assertThat(frame.textBox(XML_TEXT).text(), endsWith(pathForLocalSystem(xmlFile)));
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);
		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("DiffCount_rule_java.txt");
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));

		// Xml定義を削除して再計測
		frame.textBox(XML_TEXT).deleteText();
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		expected = this.getClass().getResource("DiffCount_java.txt");
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	/*
	@Test
	public void adjustColumnWidth() {
		System.out.println("## DiffCountGUI ## adjustColumnWidth ##");
		Integer[] expected = new Integer[COLUMNS_NUM];
		for (int i = 0; i < COLUMNS_NUM; i++) {
			expected[i] = new Integer(COL_WIDTH_PERCENTAGE[i]);
		}
		List<Integer> actual = null;

		JTableHeader header = frame.table(RESULT_TABLE).tableHeader().target;
		actual = numbersOf(header);
		System.out.println(actual);
		actual = percentageOf(header);
		System.out.println(actual);
		assertThat(actual, contains(expected));
	}
	*/
	/*
	@Test
	public void adjustColumnWidthAfterCounting() {
		System.out.println("## DiffCountGUI ## adjustColumnWidthAfterCounting ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		Integer[] expected = new Integer[COLUMNS_NUM];
		for (int i = 0; i < COLUMNS_NUM; i++) {
			expected[i] = new Integer(COL_WIDTH_PERCENTAGE[i]);
		}
		List<Integer> actual = null;

		frame.button(OLD_DIR_BUTTON).click();
		sleep(SLEEPTIME);
		JFileChooserFixture fileChooserOld = frame.fileChooser(FILE_CHOOSER + "-" + OLD_DIR_BUTTON);
		sleep(SLEEPTIME);
		fileChooserOld.setCurrentDirectory(new File("."))
			.fileNameTextBox().setText(oldRoot);
		fileChooserOld.approve();
		sleep(SLEEPTIME);
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(oldRoot));

		frame.button(NEW_DIR_BUTTON).click();
		sleep(SLEEPTIME);
		JFileChooserFixture fileChooserNew = frame.fileChooser(FILE_CHOOSER + "-" + NEW_DIR_BUTTON);
		sleep(SLEEPTIME);
		fileChooserNew.setCurrentDirectory(new File("."))
			.fileNameTextBox().setText(newRoot);
		fileChooserNew.approve();
		sleep(SLEEPTIME);
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(newRoot));

		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPTIME);
		JTableHeader header = frame.table(RESULT_TABLE).tableHeader().target;
		actual = numbersOf(header);
		System.out.println(actual);
		actual = percentageOf(header);
		System.out.println(actual);
		assertThat(actual, contains(expected));
	}
	*/
}
