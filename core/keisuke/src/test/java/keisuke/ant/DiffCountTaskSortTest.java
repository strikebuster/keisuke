package keisuke.ant;

import static keisuke.count.CountTestUtil.withoutHeadLines;
import static keisuke.count.diff.DiffCountTestConstant.TEXT_IGNORE_LINES;
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
 * Test class of DiffCountTask which is ant task to call DiffCount with sort option.
 */
public class DiffCountTaskSortTest {
	private String projfile = "test/data/ant/diffcount.xml";

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void cannotDoBecauseInvalidSort() throws Exception {
		System.out.println("## DiffCountTask ## error ## cannotDoBecauseInvalidSort ##");
		String targetName = "InvalidSort";
		String expected = "is invalid sort";
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
	public void countDiffJavaUsingTextFormatAndSortOsOrder() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingTextFormatAndSortOsOrder ##");
		String targetName = "DiffJava_Text_OsSort";
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("../count/diff/diffCount_java_osSort_win.txt");
		} else {
			expected = this.getClass().getResource("../count/diff/diffCount_java_codeSort.txt");
		}

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_osSort.txt");
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingTextFormatAndSortCodeOrder() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingTextFormatAndSortCodeOrder ##");
		String targetName = "DiffJava_Text_CodeSort";
		URL expected = this.getClass().getResource("../count/diff/diffCount_java_codeSort.txt");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_codeSort.txt");
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndSortCodeOrder() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCsvFormatAndSortCodeOrder ##");
		String targetName = "DiffJava_Csv_CodeSort";
		URL expected = this.getClass().getResource("../count/diff/diffCount_java.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_codeSort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndSortNodeOrder() throws Exception {
		System.out.println("## DiffCountTask ## countDiffJavaUsingCsvFormatAndSortNodeOrder ##");
		String targetName = "DiffJava_Csv_NodeSort";
		URL expected = this.getClass().getResource("../count/diff/diffCount_java_nodeSort.csv");

		AntTaskTestDriver driver = new AntTaskTestDriver();
		driver.configureProject(projfile);
		driver.executeTarget(targetName);
		System.out.println(driver.getLog());
		System.err.println(driver.getStderr());
		System.out.println(driver.getStdout());

		File actual = new File("test/out/ant_diff_java_nodeSort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
