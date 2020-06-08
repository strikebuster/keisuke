package keisuke.swing.stepcount;

import static keisuke.swing.GUIConstant.COUNT_BUTTON;
import static keisuke.swing.GUIConstant.FORMAT_SELECT;
import static keisuke.swing.GUIConstant.PATH_SELECT;
import static keisuke.swing.GUIConstant.RESULT_TEXT;
import static keisuke.swing.GUITestUtil.SLEEPLONGTIME;
import static keisuke.swing.GUITestUtil.SLEEPTIME;
import static keisuke.swing.GUITestUtil.SLEEPVERYLONGTIME;
import static keisuke.swing.GUITestUtil.WAITTIME;
import static keisuke.swing.GUITestUtil.pathForLocalSystem;
import static keisuke.swing.GUITestUtil.sleep;
import static keisuke.swing.stepcount.StepCountGUIConstant.ADD_BUTTON;
import static keisuke.swing.stepcount.StepCountGUIConstant.FILE_CHOOSER;
import static keisuke.swing.stepcount.StepCountGUIConstant.SOURCE_LIST;
import static keisuke.swing.stepcount.StepCountGUITestConstant.*;
import static keisuke.swing.stepcount.StepCountTestUtil.chooseSomeFiles;
import static keisuke.swing.stepcount.StepCountTestUtil.parentPathOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.array;
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
 * Test class of StepCount GUI using path style box.
 */
public final class StepCountGUIPathTest extends FestSwingJUnitTestCase {

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

	@Test
	public void countJavaUsingDefault() {
		System.out.println("## StepCountGUI ## countJavaUsingDefault ##");

		// ソースリストに１行追加
		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actualSrc = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actualSrc));
		assertThat(actualSrc, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java.txt");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithBasePath() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatWithBasePath ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actualSrc = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actualSrc));
		assertThat(actualSrc, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		boolean stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.comboBox(PATH_SELECT).selectItem(PATH_BASE_IDX);
		sleep(SLEEPTIME);
		stat = frame.button(COUNT_BUTTON).target.isEnabled();
		assertThat(stat, is(true));
		frame.button(COUNT_BUTTON).click();
		sleep(SLEEPLONGTIME);

		URL expected = this.getClass().getResource("StepCount_java_basePath.csv");
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSubPathWhenFilesAreGiven() {
		System.out.println("## StepCountGUI ## countJavaUsingCsvFormatAndSubPathWhenFilesAreGiven ##");

		String srcFile1 = "test/data/java/root1/src/StringUtil.java";
		String srcFile2 = "test/data/java/root1/test/Utils.java";
		String[] fileNames = {srcFile1, srcFile2};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				null, fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcFile1)),
				endsWith(pathForLocalSystem(srcFile2)))));
		frame.comboBox(PATH_SELECT).selectItem(PATH_SUB_IDX);
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_CSV_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java_files_subPath.csv");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormatWithSubPath() {
		System.out.println("## StepCountGUI ## countJavaUsingJsonFormatWithSubPath ##");

		String srcRoot = "test/data/java/root1";
		String[] fileNames = {srcRoot};
		chooseSomeFiles(frame, frame.button(ADD_BUTTON), FILE_CHOOSER,
				parentPathOf(srcRoot), fileNames, frame.list(SOURCE_LIST));
		String[] actual = frame.list(SOURCE_LIST).contents();
		//System.out.println(contentOf(actual));
		assertThat(actual, is(array(endsWith(pathForLocalSystem(srcRoot)))));
		frame.comboBox(PATH_SELECT).selectItem(PATH_SUB_IDX);
		sleep(SLEEPTIME);
		frame.comboBox(FORMAT_SELECT).selectItem(FORMAT_JSON_IDX);
		sleep(SLEEPTIME);
		frame.button(COUNT_BUTTON).requireEnabled(Timeout.timeout(WAITTIME)).click();
		sleep(SLEEPLONGTIME);
		String text = frame.textBox(RESULT_TEXT).text();
		//System.out.println(text);
		URL expected = this.getClass().getResource("StepCount_java_subPath.json");
		assertThat(text, is(equalTo(textContentOf(expected))));
	}

}
