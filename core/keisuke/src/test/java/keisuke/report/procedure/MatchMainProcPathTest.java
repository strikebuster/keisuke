package keisuke.report.procedure;

import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.util.StderrCapture;

/**
 * Test class of MatchProc with path option.
 */
public class MatchMainProcPathTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileOfOptionWhenListsAreSortedAndBasePath() throws Exception {
		System.out.println("## MatchProcPathTest ## match11 ## "
			+ "extractLinesOfMatchedFilesIntoOutfileOfOptionWhenListsAreSortedAndBasePath ##");
		URL expected = this.getClass().getResource("MatchTest_match03b.csv");
		String outFileName = "test/out/match15.csv";

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03b_sorted.csv: oldsrc/ is root
		// match_tr03b_sorted.txt: src/ is root
		// matching file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03b_sorted.csv",
				"test/data/match_tr03b_sorted.txt", "-o", outFileName, "-p", "base"};
		mproc.main(args);

		File actual = new File(outFileName);
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileOfOptionWhenListsAreSortedAndSubPath() throws Exception {
		System.out.println("## MatchProcPathTest ## match12 ## "
			+ "extractLinesOfMatchedFilesIntoOutfileOfOptionWhenListsAreSortedAndSubPath ##");
		URL expected = this.getClass().getResource("MatchTest_match03s.csv");
		String outFileName = "test/out/match12.csv";

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03s_sorted.csv: root is not included
		// match_tr03s_sorted.txt: root is not included
		// matching file path
		String[] args = {"-p", "sub", "test/data/match_ma03s_sorted.csv",
				"test/data/match_tr03s_sorted.txt", "-o", outFileName};
		mproc.main(args);

		File actual = new File(outFileName);
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void extractNoLinesWhenMaIsV1AndTrIsBasePath() throws Exception {
		System.out.println("## MatchProcPathTest ## error11 ## extractNoLinesWhenMaIsV1AndTrIsBasePath ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03b_sorted.txt: src/ is root
		// matching file path, but extract no lines because path styles are not matched
		String[] args = {"test/data/match_ma03_sorted.csv", "test/data/match_tr03b_sorted.txt"};
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
	public void extractNoLinesWhenMaIsV1AndTrIsSubPath() throws Exception {
		System.out.println("## MatchProcPathTest ## error12 ## extractNoLinesWhenMaIsV1AndTrIsSubPath ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03s_sorted.txt: root is not included
		// matching file path, but extract no lines because path styles are not matched
		String[] args = {"test/data/match_ma03_sorted.csv", "test/data/match_tr03s_sorted.txt", "-p", "sub"};
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
	public void extractNoLinesWhenMaIsBasePathAndTrIsV1() throws Exception {
		System.out.println("## MatchProcPathTest ## error13 ## extractNoLinesWhenMaIsBasePathAndTrIsV1 ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03b_sorted.csv: oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// matching file path, but extract no lines because path styles are not matched
		String[] args = {"test/data/match_ma03b_sorted.csv", "test/data/match_tr03_sorted.txt", "-p", "base"};
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
	public void extractNoLinesWhenMaIsBasePathAndTrIsSubPath() throws Exception {
		System.out.println("## MatchProcPathTest ## error14 ## "
				+ "extractNoLinesWhenMaIsBasePathAndTrIsSubPath ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03b_sorted.csv: oldsrc/ is root
		// match_tr03s_sorted.txt: root is not included
		// matching file path, but extract no lines because path styles are not matched
		String[] args = {"test/data/match_ma03b_sorted.csv", "test/data/match_tr03s_sorted.txt", "-p", "sub"};
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
	public void extractNoLinesWhenMaIsSubPathAndTrIsV1() throws Exception {
		System.out.println("## MatchProcPathTest ## error15 ## extractNoLinesWhenMaIsSubPathAndTrIsV1 ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03s_sorted.csv: root is not included
		// match_tr03_sorted.txt: /src/ is root
		// matching file path, but extract no lines because path styles are not matched
		String[] args = {"test/data/match_ma03s_sorted.csv", "test/data/match_tr03_sorted.txt", "-p", "sub"};
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
	public void extractNoLinesWhenMaIsSubPathAndTrIsBasePath() throws Exception {
		System.out.println("## MatchProcPathTest ## error16 ## "
				+ "extractNoLinesWhenMaIsSubPathAndTrIsBasePath ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03s_sorted.csv: root is not included
		// match_tr03b_sorted.txt: src/ is root
		// matching file path, but extract no lines because path styles are not matched
		String[] args = {"test/data/match_ma03s_sorted.csv", "test/data/match_tr03b_sorted.txt", "-p", "base"};
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
	public void extractNoLinesWhenInputIsBasePathWithoutPathOption() throws Exception {
		System.out.println("## MatchProcPathTest ## error17 ## "
			+ "extractNoLinesWhenInputIsBasePathWithoutPathOption ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03b_sorted.csv: oldsrc/ is root
		// match_tr03b_sorted.txt: src/ is root
		// matching file path, but extract no lines because path option is forgotten
		String[] args = {"test/data/match_ma03b_sorted.csv", "test/data/match_tr03b_sorted.txt"};
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
	public void extractNoLinesWhenInputIsBasePathWithSubPathOption() throws Exception {
		System.out.println("## MatchProcPathTest ## error18 ## "
			+ "extractNoLinesWhenInputIsBasePathWithSubPathOption ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03b_sorted.csv: oldsrc/ is root
		// match_tr03b_sorted.txt: src/ is root
		// matching file path, but extract no lines because path option is wrong
		String[] args = {"-p", "sub", "test/data/match_ma03b_sorted.csv", "test/data/match_tr03b_sorted.txt"};
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
	public void extractNoLinesWhenInputIsSubPathWithoutPathOption() throws Exception {
		System.out.println("## MatchProcPathTest ## error19 ## "
			+ "extractNoLinesWhenInputIsSubPathWithoutPathOption ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03s_sorted.csv: root ins not included
		// match_tr03s_sorted.txt: root ins not included
		// matching file path, but extract no lines because path option is forgotten
		String[] args = {"test/data/match_ma03s_sorted.csv", "test/data/match_tr03s_sorted.txt"};
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
	public void extractNoLinesWhenInputIsV1WithBasePathOption() throws Exception {
		System.out.println("## MatchProcPathTest ## error20 ## "
			+ "extractNoLinesWhenInputIsV1WithBasePathOption ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// matching file path, but extract no lines because path option is wrong
		String[] args = {"-p", "base", "test/data/match_ma03_sorted.csv", "test/data/match_tr03_sorted.txt"};
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
	public void extractNoLinesWhenInputIsV1WithSubPathOption() throws Exception {
		System.out.println("## MatchProcPathTest ## error21 ## "
			+ "extractNoLinesWhenInputIsV1WithSubPathOption ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// matching file path, but extract no lines because path option is wrong
		String[] args = {"-p", "sub", "test/data/match_ma03_sorted.csv", "test/data/match_tr03_sorted.txt"};
		try {
			mproc.main(args);
		} finally {
			errMessage = capture.getCapturedString();
			capture.finish();
		}

		assertThat(mproc.reportText(), is(emptyString()));
		assertThat(errMessage, is(allOf(containsString("not found"), containsString("no output"))));
	}
}
