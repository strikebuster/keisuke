package keisuke.ant;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class of StepCountTask which is ant task to call StepCounter.
 *
 */
public class StepCountTaskTest {

	private String projfile = "test/data/ant/stepcount.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseSourceRootIsNotGiven() throws Exception {
		System.out.println("## StepCountTask ## arg01 ## cannotDoBecauseSourceRootIsNotGiven ##");
		String targetName = "NoSrcRoot";
		String expected = "not exist";
		thrownEx.expect(BuildException.class);
		thrownEx.expectMessage(expected);

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void cannotDoBecauseOutputIsNotAccessed() throws Exception {
		System.out.println("## StepCountTask ## arg02 ## cannotDoBecauseOutputIsNotAccessed ##");
		String targetName = "FailOutput";
		String expected = "fail to open";
		thrownEx.expect(BuildException.class);
		thrownEx.expectMessage(expected);

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void cannotDoBecauseXmlDoesNotExist() throws Exception {
		System.out.println("## StepCountTask ## arg03 ## cannotDoBecauseXmlDoesNotExist ##");
		String targetName = "NotExistXml";
		String expected = "FileNotFoundException";
		thrownEx.expect(BuildException.class);
		thrownEx.expectMessage(expected);

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void cannotDoBecauseInvalidFormat() throws Exception {
		System.out.println("## StepCountTask ## arg04 ## cannotDoBecauseInvalidFormat ##");
		String targetName = "InvalidFormat";
		String expected = "invalid format";
		thrownEx.expect(BuildException.class);
		thrownEx.expectMessage(expected);

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void cannotDoBecauseInvalidSort() throws Exception {
		System.out.println("## StepCountTask ## arg05 ## cannotDoBecauseInvalidSort ##");
		String targetName = "InvalidSort";
		String expected = "invalid sort";
		thrownEx.expect(BuildException.class);
		thrownEx.expectMessage(expected);

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void cannotDoBecauseInvalidPath() throws Exception {
		System.out.println("## StepCountTask ## arg06 ## cannotDoBecauseInvalidPath ##");
		String targetName = "InvalidPath";
		String expected = "invalid path";
		thrownEx.expect(BuildException.class);
		thrownEx.expectMessage(expected);

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void countJavaUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndBasePath ##");
		String targetName = "CountJava_Csv_BasePath";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_java_basePath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_basePath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndSubPath ##");
		String targetName = "CountJava_Csv_SubPath";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_java_subPath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_subPath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormat() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormat ##");
		String targetName = "CountJava_Csv";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_java_noPath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndNoPathAndShowDir() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndNoPathAndShowDir ##");
		String targetName = "CountJava_Csv_NoPath_ShowDir";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_java_noPath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_noPath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOff() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndSortOff ##");
		String targetName = "CountJava_Csv_SortOff";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_java_noPath_noSort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_noSort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCustomRuleAndCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCustomRuleAndCsvFormatAndBasePath ##");
		String targetName = "CountJava_Rule_Csv_BasePath";
		URL expected = this.getClass()
				.getResource("../count/step/stepCount_rule_java_basePath.csv");
		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_rule_java_basePath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndBasePathWhenFileListIsGiven() throws Exception {
		System.out.println("## StepCountTask ## "
				+ "countJavaUsingCsvFormatAndBasePathWhenFileListIsGiven ##");
		String targetName = "CountJava_Csv_FileList_BasePath";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_basePath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files_basePath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSubPathWhenFileListIsGiven() throws Exception {
		System.out.println("## StepCountTask ## "
				+ "countJavaUsingCsvFormatAndSubPathWhenFileListIsGiven ##");
		String targetName = "CountJava_Csv_FileList_SubPath";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_subPath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files_subPath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenFileListIsGiven() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatWhenFileListIsGiven ##");
		String targetName = "CountJava_Csv_FileList";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_noPath.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndBasePathAndSortOffWhenFileListIsGiven() throws Exception {
		System.out.println("## StepCountTask ## "
				+ "countJavaUsingCsvFormatAndBasePathAndSortOffWhenFileListIsGiven ##");
		String targetName = "CountJava_Csv_FileList_BasePath_SortOff";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_basePath_noSort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files_basePath_noSort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWhenFileListIsGiven() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndSortOffWhenFileListIsGiven ##");
		String targetName = "CountJava_Csv_FileList_SortOff";
		URL expected = this.getClass()
				.getResource("SCTask_java_files_noPath_noSort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_files_noSort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
