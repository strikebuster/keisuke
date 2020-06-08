package keisuke.count.diff;

import static keisuke.count.CountTestUtil.binaryContentOf;
//import static keisuke.count.CountTestUtil.excelContentOf;
import static keisuke.count.CountTestUtil.rawContentOf;
import static keisuke.count.CountTestUtil.textContentOf;
import static keisuke.count.CountTestUtil.htmlToRemoveMutableIdFrom;
import static keisuke.count.CountTestUtil.withoutHeadLines;
import static keisuke.count.diff.DiffCountTestConstant.HTML_IGNORE_LINES;
import static keisuke.count.diff.DiffCountTestConstant.TEXT_IGNORE_LINES;
import static keisuke.util.TestUtil.nameOfSystemOS;
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
 * Test class of DiffCount about path option.
 */
public class DiffCountPathTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void handleOptionsButInvalidPath() throws Exception {
		System.out.println("## DiffCount ## arg ## handleOptionsButInvalidPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String expected = "invalid path value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"-e", "UTF-8", "-p", "abc", "-o", "test/out/diff_java.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void countDiffJavaUsingTextFormatAndNoPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingTextFormatAndNoPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_noPath.txt";
		URL expected = this.getClass().getResource("diffCount_java.txt");

		String[] args = {"-e", "UTF-8", "--path", "no", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingHtmlFormatAndSubPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingHtmlFormatAndSubPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_subPath.html";
		URL expected = this.getClass().getResource("diffCount_java.html");

		String[] args = {"-e", "UTF-8", "-f", "html", "-path", "sub", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(htmlToRemoveMutableIdFrom(rawContentOf(actual, withoutHeadLines(HTML_IGNORE_LINES))),
				is(equalTo(htmlToRemoveMutableIdFrom(textContentOf(expected)))));
	}

	@Test
	public void countDiffJavaUsingXmlFormatAndBasePath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingXmlFormatAndBasePath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_basePath.xml";
		URL expected = this.getClass().getResource("diffCount_java.xml");

		String[] args = {"-e", "UTF-8", "-f", "xml", "-p", "base", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndNoPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormatAndNoPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_noPath.csv";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "csv", "-path", "no", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_noPath_win.csv");
		} else {
			expected = this.getClass().getResource("diffCount_java_noPath.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormatAndSubPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_subPath.csv";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "csv", "-path", "sub", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_subPath_win.csv");
		} else {
			expected = this.getClass().getResource("diffCount_java_subPath.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormatAndBasePath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_basePath.csv";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "csv", "-path", "base", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_win.csv");
		} else {
			expected = this.getClass().getResource("diffCount_java.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
	@Test
	public void countDiffJavaUsingJsonFormatAndSubPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingJsonFormatAndSubPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_subPath.json";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "json", "-p", "sub", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_subPath_win.json");
		} else {
			expected = this.getClass().getResource("diffCount_java_subPath.json");
		}
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	/*
	@Test
	public void countDiffJavaUsingExcelFormatAndNoPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcelFormatAndNoPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_noPath.xlsx";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "excel", "-p", "no", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_noPath_win.xlsx");
		} else {
			expected = this.getClass().getResource("diffCount_java_noPath.xlsx");
		}
		//assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
		assertThat(excelContentOf(actual), is(equalTo(excelContentOf(expected))));
	}
	*/

	@Test
	public void countDiffJavaUsingExcel97FormatAndNoPath() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcel97FormatAndNoPath ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_noPath.xls";
		URL expected;

		//String[] args = {"-e", "UTF-8", "-f", "excel97", "-p", "no", "-o", outFileName, newRoot, oldRoot};
		String[] args = {"-e", "UTF-8", "-f", "excel", "-p", "no", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_noPath_win.xls");
		} else {
			expected = this.getClass().getResource("diffCount_java_noPath.xls");
		}
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}
}
