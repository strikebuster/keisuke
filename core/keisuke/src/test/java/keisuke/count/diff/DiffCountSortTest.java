package keisuke.count.diff;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class of DiffCount about sort option.
 */
public class DiffCountSortTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void handleOptionsButInvalidSort() throws Exception {
		System.out.println("## DiffCount ## arg ## handleOptionsButInvalidSort ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String expected = "invalid sort value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"-e", "UTF-8", "-S", "off", "-o", "test/out/diff_java.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void countDiffJavaUsingTextFormatAndSortInOsOrder() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingTextFormatAndSortInOsOrder ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_osSort.txt";
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_osSort_win.txt");
		} else {
			expected = this.getClass().getResource("diffCount_java_codeSort.txt");
		}

		String[] args = {"-e", "UTF-8", "--sort", "os", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingTextFormatAndSortInCodeOrder() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingTextFormatAndSortInCodeOrder ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_codeSort.txt";
		URL expected = this.getClass().getResource("diffCount_java_codeSort.txt");

		String[] args = {"-e", "UTF-8", "--sort", "on", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndSortInCodeOrder() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormatAndSortInCodeOrder ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_codeSort.csv";
		URL expected = this.getClass().getResource("diffCount_java.csv");

		String[] args = {"-e", "UTF-8", "-f", "csv", "-sort", "on", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndSortInNodeOrder() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormatAndSortInNodeOrder ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_nodeSort.csv";
		URL expected = this.getClass().getResource("diffCount_java_nodeSort.csv");

		String[] args = {"-e", "UTF-8", "-f", "csv", "-S", "node", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
