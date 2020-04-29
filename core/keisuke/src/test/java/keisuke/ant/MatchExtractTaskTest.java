package keisuke.ant;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
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
public class MatchExtractTaskTest {

	private String projfile = "test/data/ant/matchextract.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseMasterFileIsNotGiven() throws Exception {
		System.out.println("## MatchExtractTask ## arg01 ## cannotDoBecauseMasterFileIsNotGiven ##");
		String targetName = "NoMaster";
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
	public void cannotDoBecauseTranFileIsNotGiven() throws Exception {
		System.out.println("## MatchExtractTask ## arg02 ## cannotDoBecauseTranFileIsNotGiven ##");
		String targetName = "NoTran";
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
		System.out.println("## MatchExtractTask ## arg03 ## cannotDoBecauseOutputFileIsNotGiven ##");
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
	public void cannotDoBecauseTranFileDoesNotExist() throws Exception {
		System.out.println("## MatchExtractTask ## error01 ## cannotDoBecauseTranFileDoesNotExist ##");
		String targetName = "NotExistTran";
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
		System.out.println("## MatchExtractTask ## error02 ## cannotDoBecauseOutputFileIsNotAccessed ##");
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
	public void extractLinesOfMatchedFiles() throws Exception {
		System.out.println("## MatchExtractTask ## match01 ## extractLinesOfMatchedFiles ##");
		String targetName = "Extract";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match02.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void extractNoLinesBecauseMatchingError() throws Exception {
		System.out.println("## MatchExtractTask ## match01 ## extractNoLinesBecauseMatchingError ##");
		String targetName = "ExtractError";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		String errMessage = driver.getStderr();
		System.out.println(driver.getLog());
		System.err.println(errMessage);
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match_error.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
		assertThat(errMessage, is(allOf(containsString("not found"), containsString("no output"))));
	}

}
