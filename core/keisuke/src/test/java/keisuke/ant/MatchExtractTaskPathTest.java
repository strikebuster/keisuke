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
 * Test class of CountReportTask with path option.
 *
 */
public class MatchExtractTaskPathTest {

	private String projfile = "test/data/ant/matchextract.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void extractLinesOfMatchedFilesWithSubPath() throws Exception {
		System.out.println("## MatchExtractTaskPath ## match11 ## extractLinesOfMatchedFilesWithSubPath ##");
		String targetName = "ExtractSub";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match03s.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match_sub.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesWithBasePath() throws Exception {
		System.out.println("## MatchExtractTaskPath ## match12 ## extractLinesOfMatchedFilesWithBasePath ##");
		String targetName = "ExtractBase";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match03b.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match_base.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void cannotDoBecausePathIsWrongValue() throws Exception {
		System.out.println("## MatchExtractTaskPath ## error11 ## cannotDoBecausePathIsWrongValue ##");
		String targetName = "InvalidPath";
		String expected = "invalid path value";
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
	public void extractNoLinesWhenMaIsV1AndPathValueisBase() throws Exception {
		System.out.println("## MatchExtractTaskPath ## error12 ## "
				+ "extractNoLinesWhenMaIsV1AndPathValueisBase ##");
		String targetName = "MaV1_BasePath";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		String errMessage = driver.getStderr();
		System.out.println(driver.getLog());
		System.err.println(errMessage);
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match_mav1base.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
		assertThat(errMessage, is(allOf(containsString("not found"), containsString("no output"))));
	}

	@Test
	public void cannotDoWhenMaIsV1AndTrIsSubPath() throws Exception {
		System.out.println("## MatchExtractTaskPath ## error13 ## cannotDoWhenMaIsV1AndTrIsSubPath ##");
		String targetName = "MaV1_TrSub";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		String errMessage = driver.getStderr();
		System.out.println(driver.getLog());
		System.err.println(errMessage);
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match_mav1trsub.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
		assertThat(errMessage, is(allOf(containsString("not found"), containsString("no output"))));
	}

	@Test
	public void cannotDoWhenInputIsSubAndMissingPath() throws Exception {
		System.out.println("## MatchExtractTaskPath ## error14 ## cannotDoWhenInputIsSubAndMissingPath ##");
		String targetName = "InputSub_MissPath";
		URL expected = this.getClass()
				.getResource("../report/procedure/MatchTest_match01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		String errMessage = driver.getStderr();
		System.out.println(driver.getLog());
		System.err.println(errMessage);
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_match_inputsubmisspath.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
		assertThat(errMessage, is(allOf(containsString("not found"), containsString("no output"))));
	}
}
