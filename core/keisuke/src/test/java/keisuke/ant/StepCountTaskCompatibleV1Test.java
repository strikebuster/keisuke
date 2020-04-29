package keisuke.ant;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class of StepCountTask which is ant task to call StepCounter,
 * about features compatible with ver.1.x.
 */
public class StepCountTaskCompatibleV1Test {

	private String projfile = "test/data/ant/stepcountV1.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void countJavaUsingCsvFormat() throws Exception {
		System.out.println("## StepCountTaskV1 ## countJavaUsingCsvFormat ##");
		String targetName = "CountJava_Csv_ShowDir";
		String outFileName = "test/out/ant_count_java_showDir.csv";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_java_showDir.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCustomRuleAndCsvFormat() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCustomRuleAndCsvFormat ##");
		String targetName = "CountJava_Rule_Csv_ShowDir";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_rule_java_showDir.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_rule_java_showDir.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenFileListIsGiven() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatWhenFileListIsGiven ##");
		String targetName = "CountJava_Csv_FileList_ShowDir";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_showDir.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files_showDir.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWhenFilelistIsGiven() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndSortOffWhenFilelistIsGiven ##");
		String targetName = "CountJava_Csv_FileList_ShowDir_SortOff";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_showDir_noSort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files_showDir_noSort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
