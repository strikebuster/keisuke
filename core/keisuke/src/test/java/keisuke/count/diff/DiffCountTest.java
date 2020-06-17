package keisuke.count.diff;

import static keisuke.count.CountTestUtil.*;
import static keisuke.count.diff.DiffCountTestConstant.*;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import keisuke.count.DiffCount;
import keisuke.util.StderrCapture;
import keisuke.util.StdoutCapture;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * Test class of DiffCount.
 */
public class DiffCountTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void handleHelpOption() throws Exception {
		System.out.println("## DiffCount ## arg01 ## handleHelpOption ##");
		//String oldRoot = "test/data/java/root1";
		//String newRoot = "test/data/java/root2";
		//URL expected = this.getClass().getResource("empty.txt");

		String[] args = {"--help", "xxx"};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		//assertThat(contentOf(diffcount.argMap), is(equalTo(contentOf(expected))));
		assertThat(diffcount.argMapEntity(), is(nullValue()));
	}

	@Test
	public void handleIllegalOption() throws Exception {
		System.out.println("## DiffCount ## arg02 ## handleIllegalOption ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String expected = "fail to parse";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"-zzz", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButSourceRootsAreShort() throws Exception {
		System.out.println("## DiffCount ## arg03 ## handleOptionsButSourceRootsAreShort ##");

		StdoutCapture outCapture = new StdoutCapture();
		StderrCapture errCapture = new StderrCapture();
		String outMessage = null;
		String errMessage = null;
		Exception firedException = null;
		String expected = "no old directory";

		//String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		String[] args = {"-e", "EUC-JP", "-f", "html", "-x", "test/data/ktestl2.xml",
				"-o", "test/out/diff_arg03.txt", newRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			outMessage = outCapture.getCapturedString();
			outCapture.finish();
			errMessage = errCapture.getCapturedString();
			errCapture.finish();
		}

		final int expectedNumber = 5;
		assertThat(diffcount.argMapEntity().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(diffcount.argMapEntity(), hasEntry("output", "test/out/diff_arg03.txt"));
		assertThat(diffcount.argMapEntity(), hasEntry("xml", "test/data/ktestl2.xml"));
		assertThat(diffcount.argMapEntity(), hasEntry("format", "html"));
		assertThat(diffcount.argMapEntity(), hasEntry("encoding", "EUC-JP"));
		assertThat(diffcount.argMapEntity(), hasEntry("newDir", newRoot));

		assertThat(outMessage, containsString("usage"));
		assertThat(errMessage, containsString("not specified"));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}

	@Test
	public void handleOptionsButInvalidFormat() throws Exception {
		System.out.println("## DiffCount ## arg04 ## handleOptionsButInvalidFormat ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String expected = "invalid format value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"-e", "UTF-8", "-f", "record", "-o", "test/out/diff_java.json", newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void countDiffJava() throws Exception {
		System.out.println("## DiffCount ## countDiffJava ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java.txt";
		URL expected = this.getClass().getResource("diffCount_java.txt");

		String[] args = {"-e", "UTF-8", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaWhenLocaleIsEnglish() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_en.txt";
		URL expected = this.getClass().getResource("diffCount_java_en.txt");

		String[] args = {"-e", "UTF-8", "-f", "text", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		try {
			diffcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJava2() throws Exception {
		System.out.println("## DiffCount ## countDiffJava(2) ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root3";
		String outFileName = "test/out/diff_java2.txt";
		URL expected = this.getClass().getResource("diffCount_java2.txt");

		String[] args = {"-e", "UTF-8", "-f", "text", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaWhenThereIsNoDifference() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaWhenThereIsNoDifference ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/diff_java_nodiff.txt";
		URL expected = this.getClass().getResource("diffCount_java_nodiff.txt");

		String[] args = {"-e", "UTF-8", "-f", "text", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffJavaWhenScmDirectoriesExist() throws Exception {
		System.out.println("## DiffCount ## countDiffJavaWhenScmDirectoriesExist ##");
		String oldRoot = "test/data/java/root4";
		String newRoot = "test/data/java/root2";
		String outFileName = "test/out/diff_java_scm.txt";
		URL expected = this.getClass().getResource("diffCount_java.txt");

		String[] args = {"-e", "UTF-8", "-f", "text", "-o", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffWebCodingInUtf() throws Exception {
		System.out.println("## DiffCount ## countDiffWebCodingInUtf ##");
		String oldRoot = "test/data/web/root1";
		String newRoot = "test/data/web/root2";
		String outFileName = "test/out/diff_web.txt";
		URL expected = this.getClass().getResource("diffCount_web.txt");

		String[] args = {"--encoding", "UTF-8", "--output", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffWebCodingInSjis() throws Exception {
		System.out.println("## DiffCount ## countDiffWebCodingInSjis ##");
		String oldRoot = "test/data/web/root1S";
		String newRoot = "test/data/web/root2S";
		String outFileName = "test/out/diff_webS.txt";
		URL expected = this.getClass().getResource("diffCount_webS.txt");

		String[] args = {"-encoding", "Windows-31J", "-output", outFileName, newRoot, oldRoot};
		DiffCountProc diffcount = new DiffCountProc();
		diffcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffC() throws Exception {
		System.out.println("## DiffCount ## countDiffC ##");
		String oldRoot = "test/data/c/root1";
		String newRoot = "test/data/c/root2";
		String outFileName = "test/out/diff_c.txt";
		URL expected = this.getClass().getResource("diffCount_c.txt");

		String[] args = {"-encoding", "Shift_JIS", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInSjis() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInSjis ##");
		String oldRoot = "test/data/commentS/root1";
		String newRoot = "test/data/commentS/root2";
		String outFileName = "test/out/diff_commentS.txt";
		URL expected = this.getClass().getResource("diffCount_commentS.txt");

		String[] args = {"-encoding", "Windows-31J", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInSjis2() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInSjis(2) ##");
		String oldRoot = "test/data/commentS/root10";
		String newRoot = "test/data/commentS/root20";
		String outFileName = "test/out/diff_commentS2.txt";
		URL expected = this.getClass().getResource("diffCount_commentS2.txt");

		String[] args = {"-encoding", "Windows-31J", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInUtf() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInUtf ##");
		String oldRoot = "test/data/commentU/root1";
		String newRoot = "test/data/commentU/root2";
		String outFileName = "test/out/diff_commentU.txt";
		URL expected = this.getClass().getResource("diffCount_commentU.txt");

		String[] args = {"-encoding", "UTF-8", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInUtf2() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInUtf(2) ##");
		String oldRoot = "test/data/commentU/root10";
		String newRoot = "test/data/commentU/root20";
		String outFileName = "test/out/diff_commentU2.txt";
		URL expected = this.getClass().getResource("diffCount_commentU2.txt");

		String[] args = {"-encoding", "UTF-8", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffMiscSourcesIncludingMiscCommentsInUtf3() throws Exception {
		System.out.println("## DiffCount ## countDiffMiscSourcesIncludingMiscCommentsInUtf(3) ##");
		String oldRoot = "test/data/commentU/root30";
		String newRoot = "test/data/commentU/root40";
		String outFileName = "test/out/diff_commentU3.txt";
		URL expected = this.getClass().getResource("diffCount_commentU3.txt");

		String[] args = {"-encoding", "UTF-8", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		//System.out.println(rawContentOf(actual));
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	// これ以下のテスト用データは未公開
	@Test
	public void countDiffCobol() throws Exception {
		System.out.println("## DiffCount ## countDiffCobol ##");
		String oldRoot = "test/data/cobol/root1";
		String newRoot = "test/data/cobol/root2";
		String outFileName = "test/out/diff_cobol.txt";
		URL expected = this.getClass().getResource("diffCount_cobol.txt");

		String[] args = {"-encoding", "Shift_JIS", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffOW() throws Exception {
		System.out.println("## DiffCount ## countDiffOW ##");
		String oldRoot = "test/data/ow/root1";
		String newRoot = "test/data/ow/root2";
		String outFileName = "test/out/diff_ow.txt";
		URL expected = this.getClass().getResource("diffCount_ow.txt");

		String[] args = {"-encoding", "UTF-8", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countDiffOW2() throws Exception {
		System.out.println("## DiffCount ## countDiffOW(2) ##");
		String oldRoot = "test/data/ow/root1";
		String newRoot = "test/data/ow/root3";
		String outFileName = "test/out/diff_ow2.txt";
		URL expected = this.getClass().getResource("diffCount_ow2.txt");

		String[] args = {"-encoding", "UTF-8", "-output", outFileName, newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)),
				is(equalTo(textContentOf(expected))));
	}

}
