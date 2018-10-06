package keisuke.ant;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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
 * Test class of CountReportTask which is ant task to call CountReport.
 *
 */
public class CountReportTaskTest {

	private String projfile = "test/data/ant/countreport.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseInputFileIsNotGiven() throws Exception {
		System.out.println("## CountReportTask ## arg01 ## cannotDoBecauseInputFileIsNotGiven ##");
		String targetName = "NoInput";
		String expected = "is required";
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
	public void cannotDoBecauseOutputFileIsNotGiven() throws Exception {
		System.out.println("## CountReportTask ## arg02 ## cannotDoBecauseOutputFileIsNotGiven ##");
		String targetName = "NoOutput";
		String expected = "is required";
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
	public void cannotDoBecauseInputFileDoesNotExist() throws Exception {
		System.out.println("## CountReportTask ## error01 ## cannotDoBecauseInputFileDoesNotExist ##");
		String targetName = "FailInput";
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
	public void cannotDoBecauseOutputFileIsNotAccessed() throws Exception {
		System.out.println("## CountReportTask ## error02 ## cannotDoBecauseOutputFileIsNotAccessed ##");
		String targetName = "FailOutput";
		String expected = "fail to write";
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
	public void cannotDoBecauseClassOptionIsWrongValue() throws Exception {
		System.out.println("## CountReportTask ## error03 ## cannotDoBecauseClassOptionIsWrongValue ##");
		String targetName = "InvalidClassify";
		String expected = "invalid classify value";
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
	public void cannotDoBecauseXmlFileDoesNotExist() throws Exception {
		System.out.println("## CountReportTask ## error04 ## cannotDoBecauseXmlFileDoesNotExist ##");
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
	public void cannotDoBecausePropFileDoesNotExist() throws Exception {
		System.out.println("## CountReportTask ## error05 ## cannotDoBecausePropFileDoesNotExist ##");
		String targetName = "NotExistProp";
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
	public void countUsingDefault() throws Exception {
		System.out.println("## CountReportTask ## countUsingDefault ##");
		String targetName = "CountReport";
		URL expected = this.getClass()
				.getResource("../report/procedure/CountTest_count01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_countreport.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countUsingLanguage() throws Exception {
		System.out.println("## CountReportTask ## countUsingLanguage ##");
		String targetName = "CountReportUsingLanguage";
		URL expected = this.getClass()
				.getResource("../report/procedure/CountTest_count05.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_countreport_usingLanguage.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countUsingCustomRule() throws Exception {
		System.out.println("## CountReportTask ## countUsingCustomRule ##");
		String targetName = "CountReportUsingCustomRule";
		URL expected = this.getClass()
				.getResource("../report/procedure/CountTest_count08.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_countreport_usingCustomRule.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countUsingProp() throws Exception {
		System.out.println("## CountReportTask ## countUsingProp ##");
		String targetName = "CountReportUsingProp";
		URL expected = this.getClass()
				.getResource("../report/procedure/CountTest_count03.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_countreport_usingProp.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countUsingXmlForFrameworkWhichDoesNotHaveTargetFramework() throws Exception {
		System.out.println("## CountReportTask ## countUsingXmlForFrameworkWhichDoesNotHaveTargetFramework ##");
		String targetName = "CountReportUsingFwButInvalidName";
		URL expected = this.getClass()
				.getResource("../report/procedure/CountTest_error05.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		//System.err.println(driver.getStderr());
		String errMessage = driver.getStderr();
		System.err.println(errMessage);
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_countreport_usingInvalidFw.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
		assertThat(errMessage, containsString("not found Framework definition"));
	}
}
