package keisuke.swing.stepcount;

import static keisuke.swing.GUIConstant.COUNT_BUTTON;
import static keisuke.swing.GUIConstant.FORMAT_RADIO;
import static keisuke.swing.GUIConstant.FORMAT_SELECT;
import static keisuke.swing.GUIConstant.PATH_SELECT;
import static keisuke.swing.GUIConstant.RESULT_TABLE;
import static keisuke.swing.GUIConstant.RESULT_TEXT;
import static keisuke.swing.GUIConstant.SAVE_BUTTON;
import static keisuke.swing.GUIConstant.TABLE_RADIO;
import static keisuke.swing.GUITestUtil.SLEEPLONGTIME;
import static keisuke.swing.GUITestUtil.SLEEPTIME;
import static keisuke.swing.GUITestUtil.WAITTIME;
import static keisuke.swing.GUITestUtil.labelsOf;
import static keisuke.swing.GUITestUtil.pathForLocalSystem;
import static keisuke.swing.GUITestUtil.sleep;
import static keisuke.swing.stepcount.StepCountGUIConstant.*;
import static keisuke.swing.stepcount.StepCountGUITestConstant.*;
import static keisuke.swing.stepcount.StepCountTestUtil.chooseSomeFiles;
import static keisuke.swing.stepcount.StepCountTestUtil.parentPathOf;
import static keisuke.util.TestUtil.contentOf;
import static keisuke.util.TestUtil.textContentOf;
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

	@Test
	public void countJavaUsingCsvFormatWithShowDirWhenLocaleIsEnglish() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatWithShowDirWhenLocaleIsEnglish ##");

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
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		//String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);

		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(false));
		frame.comboBox(PATH_SELECT).selectItem(PATH_SHOWDIR_IDX);
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass()
				.getResource("StepCount_java_showDir_en.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaAndDisplayTableStyleWhenLocaleIsEnglish() {
		System.out.println("## StepCountGUI ## countJavaAndDisplayTableStyleWhenLocaleIsEnglish ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
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
		assertThat(text, is(equalTo(textContentOf(expected))));
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
		String[] expectedHead = {"FileName", "Type", "Category", "Code", "Blank", "Comnt", "Sum"};
		assertThat(actualHead, contains(expectedHead));
		String[][] actualData = table.contents();
		//System.out.println(contentOf(actualData));
		URL expectedData = this.getClass().getResource("StepCount_java_en.csv");
		assertThat(contentOf(actualData), is(equalTo(contentOf(expectedData))));
	}

}
