package keisuke.swing.diffcount;

import static keisuke.count.CountTestUtil.withoutHeadLines;
import static keisuke.count.diff.DiffCountTestConstant.TEXT_IGNORE_LINES;
import static keisuke.swing.GUIConstant.COUNT_BUTTON;
import static keisuke.swing.GUIConstant.FORMAT_SELECT;
import static keisuke.swing.GUIConstant.PATH_SELECT;
import static keisuke.swing.GUIConstant.RESULT_TEXT;
import static keisuke.swing.GUITestUtil.SLEEPLONGTIME;
import static keisuke.swing.GUITestUtil.SLEEPTIME;
import static keisuke.swing.GUITestUtil.SLEEPVERYLONGTIME;
import static keisuke.swing.GUITestUtil.WAITTIME;
import static keisuke.swing.GUITestUtil.chooseFile;
import static keisuke.swing.GUITestUtil.pathForLocalSystem;
import static keisuke.swing.GUITestUtil.sleep;
import static keisuke.swing.diffcount.DiffCountGUIConstant.*;
import static keisuke.swing.diffcount.DiffCountGUITestConstant.*;
import static keisuke.swing.diffcount.DiffCountTestUtil.actualTextOf;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URL;

import javax.swing.JFrame;

import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.timing.Timeout;
import org.junit.Test;

/**
 * Test class of DiffCount GUI using path style box.
 */
public final class DiffCountGUIPathTest extends FestSwingJUnitTestCase {

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
	public void countDiffJavaUsingTextFormatAndSubPath() {
		// TEXTフォーマットではpath styleの指定は無視される
		System.out.println("## DiffCountGUI ## countDiffJavaUsingTextFormatAndSubPath ##");
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
		// subパス表記スタイルを選択
		frame.comboBox(PATH_SELECT).selectItem(PATH_SUB_IDX);
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
	public void countDiffJavaUsingXmlFormatAndNoPath() {
		// XMLフォーマットではpath styleの指定は無視される
		System.out.println("## DiffCountGUI ## countDiffJavaUsingXmlFormatAndNoPath ##");
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
		// noパス表記スタイルを選択
		frame.comboBox(PATH_SELECT).selectItem(PATH_NO_IDX);
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
	public void countDiffJavaUsingCsvFormatAndSubPath() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingCsvFormatAndSubPath ##");
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
		// subパス表記スタイルを選択
		frame.comboBox(PATH_SELECT).selectItem(PATH_SUB_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("DiffCount_java_subPath_win.csv");
		} else {
			expected = this.getClass()
					.getResource("DiffCount_java_subPath.csv");
		}
		assertThat(actualTextOf(text, 0), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingJsonFormatAndNoPath() {
		System.out.println("## DiffCountGUI ## countDiffJavaUsingJsonFormatAndNoPath ##");
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
		// noパス表記スタイルを選択
		frame.comboBox(PATH_SELECT).selectItem(PATH_NO_IDX);
		sleep(SLEEPTIME);
		// 計測実行しテキスト形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);

		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("DiffCount_java_noPath_win.json");
		} else {
			expected = this.getClass()
					.getResource("DiffCount_java_noPath.json");
		}
		assertThat(actualTextOf(text, 0), is(equalTo(textContentOf(expected))));
	}
}
