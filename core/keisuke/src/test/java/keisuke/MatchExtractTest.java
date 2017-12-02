package keisuke;

import static keisuke.report.option.ReportOptionConstant.OPT_OUT;
import static keisuke.util.TestUtil.contentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.util.StderrCapture;
import keisuke.util.StdoutCapture;

/**
 * Test class of MatchExtract.
 */
public class MatchExtractTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void extractLinesOfMatchedFilesIntoStdout() throws Exception {
		System.out.println(
				"## MatchExtractTest ## extract01 ## "
				+ "extractLinesOfMatchedFilesIntoStdout ##");
		URL expected = this.getClass().getResource("report/procedure/MatchTest_match03.csv");

		StdoutCapture capture = new StdoutCapture();

		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// mathing file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",	"test/data/match_tr03_sorted.txt"};
		MatchExtract.main(args);

		String outMessage = capture.getCapturedString();
		capture.finish();
		assertThat(outMessage, is(equalTo(contentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileOfArgument() throws Exception {
		System.out.println(
				"## MatchExtractTest ## extract02 ## "
				+ "extractLinesOfMatchedFilesIntoOutfileOfArgument ##");
		URL expected = this.getClass().getResource("report/procedure/MatchTest_match03.csv");

		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// mathing file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", "test/out/extract02.csv"};
		MatchExtract.main(args);

		File actual = new File("test/out/extract02.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileOfOutOption() throws Exception {
		System.out.println(
				"## MatchExtractTest ## extract03 ## "
				+ "extractLinesOfMatchedFilesIntoOutfileOfOutOption ##");
		URL expected = this.getClass().getResource("report/procedure/MatchTest_match03.csv");

		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// mathing file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", "--" + OPT_OUT, "test/out/extract03.csv"};
		MatchExtract.main(args);

		File actual = new File("test/out/extract03.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileOfOutOptionNotArgument() throws Exception {
		System.out.println(
				"## MatchExtractTest ## extract03 ## "
				+ "extractLinesOfMatchedFilesIntoOutfileOfOutOptionNotArgument ##");
		URL expected = this.getClass().getResource("report/procedure/MatchTest_match03.csv");
		StderrCapture capture = new StderrCapture();
		String errMessage = null;

		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// mathing file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", "test/out/extract04A.csv",
				"--" + OPT_OUT, "test/out/extract04.csv"};
		try {
			MatchExtract.main(args);
		} finally {
			errMessage = capture.getCapturedString();
			capture.finish();
		}

		File actual = new File("test/out/extract04.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
		assertThat(errMessage, containsString("ignore the argument"));
	}
}
