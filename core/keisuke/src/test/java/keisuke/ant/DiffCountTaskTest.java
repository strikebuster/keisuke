package keisuke.ant;

import static keisuke.count.CountTestUtil.*;
import static keisuke.count.diff.DiffCountTestConstant.*;
import static keisuke.util.TestUtil.nameOfSystemOS;
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
 * Test class of DiffCountTask which is ant task to call DiffCount.
 */
public class DiffCountTaskTest {
	private String projfile = "test/data/ant/diffcount.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseSourceRootsAreShort() throws Exception {
		System.out.println("## DiffCountTask ## error01 ## cannotDoBecauseSourceRootsAreShort ##");
		String targetName = "TooFewRoot";
		String expected = "dir is required";
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
	public void cannotDoBecauseOutputIsNotAllowed() throws Exception {
		System.out.println("## DiffCountTask ## error02 ## cannotDoBecauseOutputIsNotAllowed ##");
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
	public void cannotDoBecauseInvalidFormat() throws Exception {
		System.out.println("## DiffCountTask ## error03 ## cannotDoBecauseInvalidFormat ##");
		String targetName = "InvalidFormat";
		String expected = "is invalid format";
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
	public void countDiffJava() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJava ##");
		String targetName = "DiffJava";
		URL expected = this.getClass()
				.getResource("../count/diff/diffCount_java.txt");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java.txt");
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormat() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCsvFormat ##");
		String targetName = "DiffJava_Csv";
		URL expected;

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java.csv");
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
	public void countDiffJavaUsingCustomRule() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCustomRule ##");
		String targetName = "DiffJava_Rule";
		URL expected = this.getClass()
				.getResource("../count/diff/diffCount_rule_java.txt");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_usingCustomRule.txt");
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

}
