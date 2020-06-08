package keisuke.report.procedure;

import keisuke.util.StderrCapture;
import keisuke.util.StdoutCapture;

import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

/**
 * Test class of MatchProc.
 */
public class MatchMainProcTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void handleHelpOption() throws Exception {
		System.out.println("## MatchProcTest ## arg01 ## handleHelpOption ##");

		MatchMainProc mproc = new MatchMainProc();
		String[] args = {"-?", "xxx"};
		mproc.main(args);

		assertThat(mproc.argMapEntity(), nullValue());
	}

	@Test
	public void handleArgsWhichHaveOnlyOnefile() throws Exception {
		System.out.println("## MatchProcTest ## arg02 ## handleArgsWhichHaveOnlyOneFile ##");

		StdoutCapture outCapture = new StdoutCapture();
		StderrCapture errCapture = new StderrCapture();
		String outMessage = null;
		String errMessage = null;
		Exception firedException = null;
		String expected = "short of arguments";

		MatchMainProc mproc = new MatchMainProc();
		String[] args = {"test/data/dummy.csv"};
		try {
			mproc.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			outMessage = outCapture.getCapturedString();
			outCapture.finish();
			errMessage = errCapture.getCapturedString();
			errCapture.finish();
		}

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
	public void extractNoFileWhenOutputDoesNotAccessed() throws Exception {
		System.out.println("## MatchProcTest ## error01 ## extractNoFileWhenOutputDoesNotAccessed ##");
		StderrCapture capture = new StderrCapture();
		String errMessage = null;
		Exception firedException = null;
		String expectedMsg = "fail to write";
		URL expected = this.getClass().getResource("MatchTest_match02.csv");

		MatchMainProc mproc = new MatchMainProc();
		// match_ma01_sorted.csv: /src/ is root
		// match_tr01_sorted.txt: /src/ is root
		// matching file path, then lists are sorted
		// These files(lists) are sorted in unix order, not Windows order.
		// So this will fail,if this runs on Windows
		String[] args = {"test/data/match_ma01_sorted.csv", "test/data/match_tr01_sorted.txt",
				"/dummy.cvs"};
		try {
			mproc.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			errMessage = capture.getCapturedString();
			capture.finish();
		}

		assertThat(mproc.reportText(), is(equalTo(textContentOf(expected))));
		assertThat(errMessage, containsString("Write error"));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expectedMsg));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expectedMsg);
		}
	}

	@Test
	public void extractNoLinesWhenListsAreNotSorted() throws Exception {
		System.out.println("## MatchProcTest ## error02 ##  extractNoLinesWhenListsAreNotSorted ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma01.csv: /src/ is root
		// match_tr01.txt: /src/ is root
		// matching file path, but extract no lines because lists are not sorted
		String[] args = {"test/data/match_ma01.csv", "test/data/match_tr01.txt"};
		try {
			mproc.main(args);
		} finally {
			errMessage = capture.getCapturedString();
			capture.finish();
		}

		assertThat(mproc.reportText(), is(emptyString()));
		assertThat(errMessage, is(allOf(containsString("not found"), containsString("no output"))));
	}

	@Test
	public void extractLinesOfMatchedFilesWhenListsAreSorted() throws Exception {
		System.out.println("## MatchProcTest ## match02 ## extractLinesOfMatchedFilesWhenListsAreSorted ##");
		URL expected = this.getClass().getResource("MatchTest_match02.csv");

		MatchMainProc mproc = new MatchMainProc();
		// match_ma01_sorted.csv: /src/ is root
		// match_tr01_sorted.txt: /src/ is root
		// matching file path, then lists are sorted
		// These files(lists) are sorted in unix order, not Windows order.
		// So this will fail,if this runs on Windows
		String[] args = {"test/data/match_ma01_sorted.csv", "test/data/match_tr01_sorted.txt"};
		mproc.main(args);

		assertThat(mproc.reportText(), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileWhenListsAreSorted() throws Exception {
		System.out.println(
			"## MatchProcTest ## match03 ## extractLinesOfMatchedFilesIntoOutfileWhenListsAreSorted ##");
		URL expected = this.getClass().getResource("MatchTest_match03.csv");
		String outFileName = "test/out/match03.csv";

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// matching file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", outFileName};
		mproc.main(args);

		File actual = new File(outFileName);
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileOfOptionWhenListsAreSorted() throws Exception {
		System.out.println("## MatchProcTest ## match04 ## "
			+ "extractLinesOfMatchedFilesIntoOutfileOfOptionWhenListsAreSorted ##");
		URL expected = this.getClass().getResource("MatchTest_match03.csv");
		String outFileName = "test/out/match04.csv";

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// matching file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", "-o", outFileName};
		mproc.main(args);

		File actual = new File(outFileName);
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expected))));
	}

}
