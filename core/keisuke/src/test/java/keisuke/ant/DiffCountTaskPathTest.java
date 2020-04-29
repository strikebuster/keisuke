package keisuke.ant;

import static keisuke.util.TestUtil.nameOfSystemOS;
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
 * Test class of DiffCountTask which is ant task to call DiffCount with path option.
 */
public class DiffCountTaskPathTest {
	private String projfile = "test/data/ant/diffcount.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseInvalidPath() throws Exception {
		System.out.println("## DiffCountTask ## error ## cannotDoBecauseInvalidPath ##");
		String targetName = "InvalidPath";
		String expected = "is invalid path";
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
	public void countDiffJavaUsingCsvFormatAndNoPath() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCsvFormatAndNoPath ##");
		String targetName = "DiffJava_Csv_NoPath";
		URL expected;

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_noPath.csv");
		if (!nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("../count/diff/diffCount_java_noPath.csv");
		} else {
			expected = this.getClass()
					.getResource("../count/diff/diffCount_java_noPath_win.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCsvFormatAndSubPath ##");
		String targetName = "DiffJava_Csv_SubPath";
		URL expected;

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_subPath.csv");
		if (!nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("../count/diff/diffCount_java_subPath.csv");
		} else {
			expected = this.getClass()
					.getResource("../count/diff/diffCount_java_subPath_win.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCsvFormatAndBasePath ##");
		String targetName = "DiffJava_Csv_BasePath";
		URL expected;

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_basePath.csv");
		if (!nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass()
					.getResource("../count/diff/diffCount_java.csv");
		} else {
			expected = this.getClass()
					.getResource("../count/diff/diffCount_java_win.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingXmlFormatAndSubPath() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingXmlFormatAndSubPath ##");
		String targetName = "DiffJava_Xml_SubPath";
		URL expected = this.getClass().getResource("../count/diff/diffCount_java.xml");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_subPath.xml");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
