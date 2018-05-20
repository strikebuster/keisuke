package keisuke.swing.diffcount;

import static keisuke.swing.GUITestUtil.*;
import static keisuke.swing.diffcount.DiffCountGUIConstant.*;
import static keisuke.swing.diffcount.DiffCountTestUtil.actualTextOf;
import static keisuke.swing.diffcount.DiffCountTestUtil.deriveTotalAddedStepsFrom;
import static keisuke.swing.diffcount.DiffCountTestUtil.deriveTotalDeletedStepsFrom;
import static keisuke.swing.diffcount.DiffCountTestUtil.deriveTotalStatusFrom;
import static keisuke.count.CountTestUtil.withoutHeadLines;
import static keisuke.count.diff.DiffCountTestConstant.TEXT_IGNORE_LINES;
import static keisuke.swing.GUIConstant.COUNT_BUTTON;
import static keisuke.swing.GUIConstant.RESULT_TABLE;
import static keisuke.swing.GUIConstant.RESULT_TEXT;
import static keisuke.swing.GUIConstant.TABLE_RADIO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URL;
import java.util.List;
import java.util.Locale;

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


/**
 * Test class of DiffCount GUI under location which is not system default.
 */
public final class DiffCountGUITestForLocale extends FestSwingJUnitTestCase {

	private FrameFixture frame;
	private Locale orgLocale = Locale.getDefault();

	@Override
	protected void onSetUp() {
		Locale.setDefault(Locale.ENGLISH);
		sleep(SLEEPTIME);
		this.startGUI();
		sleep(SLEEPLONGTIME);
	}

	@Override
	public void onTearDown() {
		Locale.setDefault(orgLocale);
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
	public void countDiffJavaWhenLocaleIsEnglish() {
		System.out.println("## DiffCountGUI ## countDiffJavaWhenLocaleIsEnglish ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(oldRoot));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(newRoot));

		// 計測前のトータル表示ラベルの状態を検査
		JLabelFixture total = frame.label(TOTAL_LABEL);
		assertThat(total.target.getText(), is(allOf(containsString("Status[ ]"),
				containsString("Added Steps[0]"), containsString("Deleted Steps[0]"))));

		// 計測実行し結果を検査
		URL expected = this.getClass()
				.getResource("DiffCount_java_en.txt");
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPVERYLONGTIME);
		String text = frame.textBox(RESULT_TEXT).requireEnabled(Timeout.timeout(WAITTIME)).text();
		//System.out.println(text);
		assertThat(actualTextOf(text, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
		// 計測後のトータル表示ラベルの検査
		URL expectedCsv = this.getClass().getResource("DiffCount_java_top_en.csv");
		String[][] expectedArray = convertToTableArrayFrom(contentOf(expectedCsv));
		String expectedStatus = deriveTotalStatusFrom(expectedArray);
		long expectedAdded = deriveTotalAddedStepsFrom(expectedArray);
		long expectedDeleted = deriveTotalDeletedStepsFrom(expectedArray);
		assertThat(total.target.getText(), is(allOf(containsString("Status[" + expectedStatus + "]"),
				containsString("Added Steps[" + expectedAdded + "]"),
				containsString("Deleted Steps[" + expectedDeleted + "]"))));
	}

	@Test
	public void countDiffJavaAndDisplayTableStyleWhenLocaleIsEnglish() {
		System.out.println("## DiffCountGUI ## countDiffJavaAndDisplayTableStylehenLocaleIsEnglish ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		// 旧バージョンのDir入力
		chooseFile(frame, frame.button(OLD_DIR_BUTTON), FILE_CHOOSER + "-" + OLD_DIR_BUTTON,
				 ".", oldRoot, frame.textBox(OLD_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(OLD_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(OLD_DIR_TEXTFIELD).text(), endsWith(oldRoot));

		// 新バージョンのDir入力
		chooseFile(frame, frame.button(NEW_DIR_BUTTON), FILE_CHOOSER + "-" + NEW_DIR_BUTTON,
				 ".", newRoot, frame.textBox(NEW_DIR_TEXTFIELD));
		//System.out.println(frame.textBox(NEW_DIR_TEXTFIELD).text());
		assertThat(frame.textBox(NEW_DIR_TEXTFIELD).text(), endsWith(newRoot));

		// 計測実行しテーブル形式で表示
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPVERYLONGTIME);
		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);
		JTableFixture table = frame.table(RESULT_TABLE).requireEnabled(Timeout.timeout(WAITTIME));
		// 表のヘッダーラベルの検査
		String[] expectedHeader = {"Dir/File Name", "Status", "Added Steps", "Deleted Steps"};
		JTableHeader header = table.tableHeader().target;
		List<String> actualLabel = labelsOf(header);
		//System.out.println(actual);
		assertThat(actualLabel, contains(expectedHeader));
		// 表の内容の検査
		URL expected = this.getClass().getResource("DiffCount_java_top_en.csv");
		String[][] actual = table.contents();
		//System.out.println(contentOf(actual));
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
		// トータル表示ラベルの検査
		JLabelFixture total = frame.label(TOTAL_LABEL);
		boolean stat = total.target.isEnabled();
		assertThat(stat, is(true));
		String[][] expectedArray = convertToTableArrayFrom(contentOf(expected));
		String expectedStatus = deriveTotalStatusFrom(expectedArray);
		long expectedAdded = deriveTotalAddedStepsFrom(expectedArray);
		long expectedDeleted = deriveTotalDeletedStepsFrom(expectedArray);
		assertThat(total.target.getText(), is(allOf(containsString("Status[" + expectedStatus + "]"),
				containsString("Added Steps[" + expectedAdded + "]"),
				containsString("Deleted Steps[" + expectedDeleted + "]"))));

	}
}
