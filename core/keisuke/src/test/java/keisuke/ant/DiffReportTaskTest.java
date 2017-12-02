package keisuke.ant;

import static keisuke.util.TestUtil.contentOf;
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
 * Test class of DiffReportTask which is ant task to call DiffReport.
 *
 */
public class DiffReportTaskTest {

	private String projfile = "test/data/ant/diffreport.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseInputFileIsNotGiven() throws Exception {
		System.out.println("## DiffReportTask ## arg01 ## cannotDoBecauseInputFileIsNotGiven ##");
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
		System.out.println("## DiffReportTask ## arg02 ## cannotDoBecauseOutputFileIsNotGiven ##");
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
		System.out.println("## DiffReportTask ## error01 ## cannotDoBecauseInputFileDoesNotExist ##");
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
		System.out.println("## DiffReportTask ## error02 ## cannotDoBecauseOutputFileIsNotAccessed ##");
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
		System.out.println("## DiffReportTask ## error03 ## cannotDoBecauseClassOptionIsWrongValue ##");
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
		System.out.println("## DiffReportTask ## error04 ## cannotDoBecauseXmlFileDoesNotExist ##");
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
		System.out.println("## DiffReportTask ## error05 ## cannotDoBecausePropFileDoesNotExist ##");
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
	public void accountUsingDefault() throws Exception {
		System.out.println("## DiffReportTask ## accountUsingDefault ##");
		String targetName = "DiffReport";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_diff01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingLanguage() throws Exception {
		System.out.println("## DiffReportTask ## accountUsingLanguage ##");
		String targetName = "DiffReportUsingLanguage";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_diff06.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_usingLanguage.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingCustomRule() throws Exception {
		System.out.println("## DiffReportTask ## accountUsingCustomRule ##");
		String targetName = "DiffReportUsingCustomRule";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_diff09.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_usingCustomRule.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingProp() throws Exception {
		System.out.println("## DiffReportTask ## accountUsingProp ##");
		String targetName = "DiffReportUsingProp";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_diff05.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_usingProp.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingXmlForFrameworkWhichDoesNotHaveTargetFramework() throws Exception {
		System.out.println("## DiffReportTask ##"
				+ "accountUsingXmlForFrameworkWhichDoesNotHaveTargetFramework ##");
		String targetName = "DiffReportUsingFwButInvalidName";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_error06.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		//System.err.println(driver.getStderr());
		String errMessage = driver.getStderr();
		System.err.println(errMessage);
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_usingInvalidFw.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
		assertThat(errMessage, containsString("not found Framework definition"));
	}

	@Test
	public void accountUsingAoutAndMout() throws Exception {
		System.out.println("## DiffReportTask ## accountUsingAoutAndMout ##");
		String targetName = "DiffReportUsingAoutAndMout";
		URL expectedA = this.getClass()
				.getResource("../report/procedure/DiffTest_diff03.txt");
		URL expectedM = this.getClass()
				.getResource("../report/procedure/DiffTest_diff04.txt");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actualA = new File("test/out/ant_diffreport_aout.csv");
		assertThat(contentOf(actualA), is(equalTo(contentOf(expectedA))));
		File actualM = new File("test/out/ant_diffreport_mout.csv");
		assertThat(contentOf(actualM), is(equalTo(contentOf(expectedM))));
	}

	@Test
	public void accountUsingUnchangeTotal() throws Exception {
		System.out.println("## DiffReportTask ## accountUsingUnchangeTotal ##");
		String targetName = "DiffReportUsingUnchangeTotal";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_arg06.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_total.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}
}
