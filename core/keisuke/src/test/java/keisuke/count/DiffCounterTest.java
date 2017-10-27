package keisuke.count;

import static keisuke.count.SCTestUtil.*;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.count.diff.DiffCountProc;
import keisuke.util.StderrCapture;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test class of DiffCounter.
 *
 */
public class DiffCounterTest {

	private static final int TEXT_IGNORE_LINES = 2;
	private static final int HTML_IGNORE_LINES = 41;

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void dealHelpOption() throws Exception {
		System.out.println("## DiffCount ## arg01 ## dealHelpOption ##");
		//String oldRoot = "test/data/java/root1";
		//String newRoot = "test/data/java/root2";
		//URL expected = this.getClass().getResource("dummy.txt");

		String[] args = {"--help", "xxx"};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		//assertThat(contentOf(diffcount.argMap), is(equalTo(contentOf(expected))));
		assertThat(diffcount.argMapEntity(), is(nullValue()));
	}

	@Test
	public void dealIllegalOption() throws Exception {
		System.out.println("## DiffCount ## arg02 ## dealIllegalOption ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		//URL expected = this.getClass().getResource("dummy.txt");

		String[] args = {"-zzz", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		//assertThat(contentOf(diffcount.argMap), is(equalTo(contentOf(expected))));
		assertThat(diffcount.argMapEntity(), is(nullValue()));
	}

	@Test
	public void dealOptionsButSourceRootsAreShort() throws Exception {
		System.out.println("## DiffCount ## arg03 ## dealOptionsButSourceRootsAreShort ##");

		StderrCapture capture = new StderrCapture();

		//String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		//URL expected = this.getClass().getResource("diffCount_arg03.txt");

		String[] args = {"-e", "EUC-JP", "-f", "html", "-x", "test/data/ktestl2.xml",
				"-o", "test/out/diff_arg03.txt", newRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		final int expectedNumber = 4;
		//assertThat(contentOf(diffcount.argMap), is(equalTo(contentOf(expected))));
		assertThat(diffcount.argMapEntity().size(), is(expectedNumber));
		assertThat(diffcount.argMapEntity(), hasEntry("output", "test/out/diff_arg03.txt"));
		assertThat(diffcount.argMapEntity(), hasEntry("xml", "test/data/ktestl2.xml"));
		assertThat(diffcount.argMapEntity(), hasEntry("format", "html"));
		assertThat(diffcount.argMapEntity(), hasEntry("encoding", "EUC-JP"));

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("2 directories must be specified"));
	}

	@Test
	public void countDiffJava() throws Exception {
		System.out.println("## DiffCount ## countDiffJava ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_java.txt");

		String[] args = {"-e", "UTF-8", "-f", "text",
				"-o", "test/out/diff_java.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_java.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffJavaWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_java_en.txt");

		String[] args = {"-e", "UTF-8", "-f", "text",
				"-o", "test/out/diff_java_en.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		Locale.setDefault(org);
		File actual = new File("test/out/diff_java_en.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingHtmlFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingHtmlFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"diffCount_java.html");

		String[] args = {"-e", "UTF-8", "-f", "html",
				"-o", "test/out/diff_java.html", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_java.html");
		assertThat(htmlToRemoveMutableIdFrom(contentOf(actual, withoutHeadLines(HTML_IGNORE_LINES))),
				is(equalTo(htmlToRemoveMutableIdFrom(contentOf(expected)))));
	}

	@Test
	public void countDiffJavaUsingHtmlFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingHtmlFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"diffCount_java_en.html");

		String[] args = {"-e", "UTF-8", "-f", "html",
				"-o", "test/out/diff_java_en.html", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		Locale.setDefault(org);
		File actual = new File("test/out/diff_java_en.html");
		assertThat(htmlToRemoveMutableIdFrom(contentOf(actual, withoutHeadLines(HTML_IGNORE_LINES))),
				is(equalTo(htmlToRemoveMutableIdFrom(contentOf(expected)))));
	}

	@Test
	public void countDiffJavaUsingExcelFormat() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcelFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"diffCount_java.xls");

		String[] args = {"-e", "UTF-8", "-f", "excel",
				"-o", "test/out/diff_java.xls", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_java.xls");
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countDiffJavaUsingExcelFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaUsingExcelFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"diffCount_java_en.xls");

		String[] args = {"-e", "UTF-8", "-f", "excel",
				"-o", "test/out/diff_java_en.xls", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		Locale.setDefault(org);
		File actual = new File("test/out/diff_java_en.xls");
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countDiffJava2() throws Exception {
		System.out.println("## DiffCount ## countDiffJava(2) ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root3";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_java2.txt");

		String[] args = {"-e", "UTF-8", "-f", "text",
				"-o", "test/out/diff_java2.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_java2.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffJavaWhenScmDirectoriesExist() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaWhenScmDirectoriesExist ##");
		String oldRoot = "test/data/java/root4";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_java.txt");

		String[] args = {"-e", "UTF-8", "-f", "text",
				"-o", "test/out/diff_java_scm.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_java_scm.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffWebCodingInUtf() throws Exception {
		System.out.println("## DiffCount ## countDiffWebCodingInUtf ##");
		String oldRoot = "test/data/web/root1";
		String newRoot = "test/data/web/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_web.txt");

		String[] args = {"--encoding", "UTF-8",
				"--output", "test/out/diff_web.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_web.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffWebCodingInSjis() throws Exception {
		System.out.println("## DiffCount ## countDiffWebCodingInSjis ##");
		String oldRoot = "test/data/web/root1S";
		String newRoot = "test/data/web/root2S";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_webS.txt");

		String[] args = {"-encoding", "Windows-31J",
				"-output", "test/out/diff_webS.txt", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File("test/out/diff_webS.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffC() throws Exception {
		System.out.println("## DiffCount ## countDiffC ##");
		String oldRoot = "test/data/c/root1";
		String newRoot = "test/data/c/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_c.txt");

		String[] args = {"-encoding", "Shift_JIS",
				"-output", "test/out/diff_c.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_c.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInSjis() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInSjis ##");
		String oldRoot = "test/data/commentS/root1";
		String newRoot = "test/data/commentS/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_commentS.txt");

		String[] args = {"-encoding", "Windows-31J",
				"-output", "test/out/diff_commentS.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_commentS.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInSjis2() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInSjis(2) ##");
		String oldRoot = "test/data/commentS/root10";
		String newRoot = "test/data/commentS/root20";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_commentS2.txt");

		String[] args = {"-encoding", "Windows-31J",
				"-output", "test/out/diff_commentS2.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_commentS2.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInUtf() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInUtf ##");
		String oldRoot = "test/data/commentU/root1";
		String newRoot = "test/data/commentU/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_commentU.txt");

		String[] args = {"-encoding", "UTF-8",
				"-output", "test/out/diff_commentU.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_commentU.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}


	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInUtf2() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInUtf(2) ##");
		String oldRoot = "test/data/commentU/root10";
		String newRoot = "test/data/commentU/root20";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_commentU2.txt");

		String[] args = {"-encoding", "UTF-8",
				"-output", "test/out/diff_commentU2.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_commentU2.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	// これ以下のテスト用データは未公開
	@Test
	public void countDiffCobol() throws Exception {
		System.out.println("## DiffCount ## countDiffCobol ##");
		String oldRoot = "test/data/cobol/root1";
		String newRoot = "test/data/cobol/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_cobol.txt");

		String[] args = {"-encoding", "Shift_JIS",
				"-output", "test/out/diff_cobol.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_cobol.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffOW() throws Exception {
		System.out.println("## DiffCount ## countDiffOW ##");
		String oldRoot = "test/data/ow/root1";
		String newRoot = "test/data/ow/root2";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_ow.txt");

		String[] args = {"-encoding", "UTF-8",
				"-output", "test/out/diff_ow.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_ow.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countDiffOW2() throws Exception {
		System.out.println("## DiffCount ## countDiffOW(2) ##");
		String oldRoot = "test/data/ow/root1";
		String newRoot = "test/data/ow/root3";
		URL expected = this.getClass().getResource(
				"DiffCounterTest_testCount_ow2.txt");

		String[] args = {"-encoding", "UTF-8",
				"-output", "test/out/diff_ow2.txt", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/diff_ow2.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}

}
