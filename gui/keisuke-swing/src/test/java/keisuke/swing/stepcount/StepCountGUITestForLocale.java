package keisuke.swing.stepcount;

import static keisuke.swing.GUIConstant.*;
import static keisuke.swing.GUITestUtil.*;
import static keisuke.swing.stepcount.StepCountGUIConstant.*;
import static keisuke.swing.stepcount.StepCountTestUtil.chooseSomeFiles;
import static keisuke.swing.stepcount.StepCountTestUtil.parentPathOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.contains;
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
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.timing.Timeout;
import org.junit.Test;

/**
 * Test class of StepCounter GUI under location which is not system default.
 */
public final class StepCountGUITestForLocale extends FestSwingJUnitTestCase {

	private static final int FORMAT_TEXT_IDX = 0;
	private static final int FORMAT_CSV_IDX = 1;
	private static final int FORMAT_EXCEL_IDX = 2;
	private static final int FORMAT_XML_IDX = 3;
	private static final int FORMAT_JSON_IDX = 4;

	private static final int SORT_ON_IDX = 0;
	private static final int SORT_OS_IDX = 1;
	private static final int SORT_OFF_IDX = 2;

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
	protected void onTearDown() {
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
	public void countJavaUsingCsvFormatWithShowDirWhenLocaleIsEnglish() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatWithShowDirWhenLocaleIsEnglish ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(srcRoot))));
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		//String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);

		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		frame.checkBox(SHOWDIR_CHECK).click();
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass()
				.getResource("StepCount_java_showdir_en.csv");
		assertThat(text, is(equalTo(contentOf(expected))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void countJavaAndDisplayTableStyleWhenLocaleIsEnglish() {
		System.out.println("## StepCountGUI ## countJavaAndDisplayTableStyleWhenLocaleIsEnglish ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(srcRoot))));
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(false));

		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java_en.txt");
		assertThat(text, is(equalTo(contentOf(expected))));
		// ボタンの状態を検査
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		stat = frame.radioButton(FORMAT_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.radioButton(TABLE_RADIO).target.isEnabled();
		assertThat(stat, is(true));
		stat = frame.button(SAVE_BUTTON).target.isEnabled();
		assertThat(stat, is(true));

		frame.radioButton(TABLE_RADIO).click();
		sleep(SLEEPTIME);
		JTableFixture table = frame.table(RESULT_TABLE).requireEnabled(Timeout.timeout(WAITTIME));
		JTableHeader header = table.tableHeader().target;
		List<String> actualHead = labelsOf(header);
		//System.out.println(actualHead);
		String[] expectedHead = {"FileName", "Type", "Category", "Exec", "Blanc", "Cmnt", "Sum"};
		assertThat(actualHead, contains(expectedHead));
		String[][] actualData = table.contents();
		//System.out.println(contentOf(actualData));
		URL expectedData = this.getClass().getResource("StepCount_java_en.csv");
		assertThat(contentOf(actualData), is(equalTo(contentOf(expectedData))));
	}

}
