package keisuke.count.diff;

import static keisuke.count.CountTestUtil.binaryContentOf;
//import static keisuke.count.CountTestUtil.excelContentOf;
import static keisuke.count.CountTestUtil.rawContentOf;
import static keisuke.count.CountTestUtil.textContentOf;
import static keisuke.count.CountTestUtil.htmlToRemoveMutableIdFrom;
import static keisuke.count.CountTestUtil.withoutHeadLines;
import static keisuke.count.diff.DiffCountTestConstant.HTML_IGNORE_LINES;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class of DiffCount about no tree format option.
 */
public class DiffCountFormatTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void countDiffJavaUsingHtmlFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingHtmlFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.html";
		URL expected = this.getClass().getResource("diffCount_java.html");

		String[] args = {"-e", "UTF-8", "-f", "html", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(htmlToRemoveMutableIdFrom(rawContentOf(actual, withoutHeadLines(HTML_IGNORE_LINES))),
				is(equalTo(htmlToRemoveMutableIdFrom(textContentOf(expected)))));
	}

	@Test
	public void countDiffJavaUsingHtmlFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingHtmlFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.html";
		URL expected = this.getClass().getResource("diffCount_java_en.html");

		String[] args = {"-e", "UTF-8", "-f", "html", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(htmlToRemoveMutableIdFrom(rawContentOf(actual, withoutHeadLines(HTML_IGNORE_LINES))),
				is(equalTo(htmlToRemoveMutableIdFrom(textContentOf(expected)))));
	}

	@Test
	public void countDiffJavaUsingXmlFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingXmlFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.xml";
		URL expected = this.getClass().getResource("diffCount_java.xml");

		String[] args = {"-e", "UTF-8", "-f", "xml", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingXmlFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingXmlFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.xml";
		URL expected = this.getClass().getResource("diffCount_java_en.xml");

		String[] args = {"-e", "UTF-8", "-f", "xml", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingCsvFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.csv";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "csv", "-o", outFileName, newRoot, oldRoot};
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
	public void countDiffJavaUsingCsvFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingCsvFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.csv";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "csv", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_en_win.csv");
		} else {
			expected = this.getClass().getResource("diffCount_java_en.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingJsonFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingJsonFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.json";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "json", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_win.json");
		} else {
			expected = this.getClass().getResource("diffCount_java.json");
		}
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingJsonFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingJsonFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.json";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "json", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_en_win.json");
		} else {
			expected = this.getClass().getResource("diffCount_java_en.json");
		}
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	/*
	@Test
	public void countDiffJavaUsingExcelFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcelFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.xlsx";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "excel", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_win.xlsx");
		} else {
			expected = this.getClass().getResource("diffCount_java.xlsx");
		}
		File actual = new File(outFileName);
		//assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
		assertThat(excelContentOf(actual), is(equalTo(excelContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingExcelFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcelFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.xlsx";
		URL expected;

		String[] args = {"-e", "UTF-8", "-f", "excel", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_en_win.xlsx");
		} else {
			expected = this.getClass().getResource("diffCount_java_en.xlsx");
		}
		//assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
		assertThat(excelContentOf(actual), is(equalTo(excelContentOf(expected))));
	}
	*/

	@Test
	public void countDiffJavaUsingExcel97Format() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcel97Format ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.xls";
		URL expected;

		//String[] args = {"-e", "UTF-8", "-f", "excel97", "-o", outFileName, newRoot, oldRoot};
		String[] args = {"-e", "UTF-8", "-f", "excel", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_win.xls");
		} else {
			expected = this.getClass().getResource("diffCount_java.xls");
		}
		File actual = new File(outFileName);
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingExcel97FormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcel97FormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.xls";
		URL expected;

		//String[] args = {"-e", "UTF-8", "-f", "excel97", "-o", outFileName, newRoot, oldRoot};
		String[] args = {"-e", "UTF-8", "-f", "excel", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("diffCount_java_en_win.xls");
		} else {
			expected = this.getClass().getResource("diffCount_java_en.xls");
		}
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}
}
