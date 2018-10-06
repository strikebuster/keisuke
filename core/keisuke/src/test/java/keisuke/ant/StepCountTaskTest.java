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
	public void countJavaUsingCsvFormat() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormat ##");
		String targetName = "CountJava_Csv";
		URL expected = this.getClass()
				.getResource("../count/step/StepCounterTest_testCount_java.csv");

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
	public void countJavaUsingCsvFormatWithoutShowDirectory() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatWithoutShowDirectory ##");
		String targetName = "CountJavaWithoutShowDir";
		URL expected = this.getClass()
				.getResource("../count/step/StepCounterTest_testCount_java_nodir.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_nodir.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWithoutShowDirectory() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndSortOffWithoutShowDirectory ##");
		String targetName = "CountJavaWithoutShowDirWithSortOff";
		URL expected = this.getClass()
				.getResource("../count/step/StepCounterTest_testCount_java_nodir_nosort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_nodir_nosort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndCustomRule() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndCustomRule ##");
		String targetName = "CountJavaUsingCustomRule";
		URL expected = this.getClass()
				.getResource("../count/step/RuleCounterTest_testCount_java.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_usingCustomRule.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenFilelistIsGiven() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatWhenFilelistIsGiven ##");
		String targetName = "CountJavaWhenFilelist";
		URL expected = this.getClass()
				.getResource("../ant/SCTaskTest_testCount_java_files.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_whenFilelist.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithoutShowDirectoryWhenFilelistIsGiven() throws Exception {
		System.out.println("## StepCountTask ## "
				+ "countJavaUsingCsvFormatWithoutShowDirectoryWhenFilelistIsGiven ##");
		String targetName = "CountJavaWithoutShowDirWhenFilelist";
		URL expected = this.getClass()
				.getResource("../ant/SCTaskTest_testCount_java_files_nodir.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_nodir_whenFilelist.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWhenFilelistIsGiven() throws Exception {
		System.out.println("## StepCountTask ## countJavaUsingCsvFormatAndSortOffWhenFilelistIsGiven ##");
		String targetName = "CountJavaWithSortOffWhenFilelist";
		URL expected = this.getClass()
				.getResource("../ant/SCTaskTest_testCount_java_files_nosort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_nosort_whenFilelist.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWithoutShowDirectoryWhenFilelistIsGiven() throws Exception {
		System.out.println("## StepCountTask ## "
				+ "countJavaUsingCsvFormatAndSortOffWithoutShowDirectoryWhenFilelistIsGiven ##");
		String targetName = "CountJavaWithoutShowDirWithSortOffWhenFilelist";
		URL expected = this.getClass()
				.getResource("../ant/SCTaskTest_testCount_java_files_nodir_nosort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_count_java_nodir_nosort_whenFilelist.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
