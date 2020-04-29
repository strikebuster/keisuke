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
 * Test class of DiffReportTask with format option.
 *
 */
public class DiffReportTaskFormatTest {

	private String projfile = "test/data/ant/diffreport.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseFormatOptionIsWrongValue() throws Exception {
		System.out.println("## DiffReportTaskFormat ## error11 ## cannotDoBecauseFormatOptionIsWrongValue ##");
		String targetName = "InvalidFormat";
		String expected = "invalid format value";
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
	public void accountUsingCsvFormat() throws Exception {
		System.out.println("## DiffReportTaskFormat ## accountUsingCsvFormat ##");
		String targetName = "DiffReportCsv";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_diff01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_csv.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void accountUsingTextFormat() throws Exception {
		System.out.println("## DiffReportTaskFormat ## accountUsingTextFormat ##");
		String targetName = "DiffReportText";
		URL expected = this.getClass()
				.getResource("../report/procedure/DiffTest_diff01.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diffreport_text.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

}
