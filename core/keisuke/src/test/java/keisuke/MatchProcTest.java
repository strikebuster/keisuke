package keisuke;

import static keisuke.TestUtil.*;

import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test class of MatchProc.
 * @author strikebuster
 *
 */
public class MatchProcTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void dealHelpOption() throws Exception {
		System.out.println("## MatchProcTest ## arg01 ## dealHelpOption ##");

		MatchMainProc mproc = new MatchMainProc();
		String[] args = {"-?", "xxx"};
		mproc.main(args);

		assertThat(mproc.argMap(), is(nullValue()));
	}

	@Test
	public void dealArgsWhichHaveOnlyInfile() throws Exception {
		System.out.println("## MatchProcTest ## arg02 ## dealArgsWhichHaveOnlyInfile ##");

		MatchMainProc mproc = new MatchMainProc();
		String[] args = {"test/data/dummy.csv"};
		mproc.main(args);

		assertThat(mproc.argMap(), is(nullValue()));
	}

	@Test
	public void extractNoLinesWhenListsAreNotSorted() throws Exception {
		System.out.println("## MatchProcTest ## match01 ##  extractNoLinesWhenListsAreNotSorted ##");

		StderrCapture capture = new StderrCapture();

		//URL expected = this.getClass().getResource("MatchTest_match01.csv");

		MatchMainProc mproc = new MatchMainProc();
		// match_ma01.csv: /src/ is root
		// match_tr01.txt: /src/ is root
		// matching file path, but extract no lines because lists are not sorted
		String[] args = {"test/data/match_ma01.csv", "test/data/match_tr01.txt"};
		mproc.main(args);

		//assertThat(mproc.reportText(), is(equalTo(contentOf(expected))));
		assertThat(mproc.reportText(), isEmptyString());

		String errMessage = capture.getCapturedString();
		capture.finish();
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

		assertThat(mproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileWhenListsAreSorted() throws Exception {
		System.out.println(
			"## MatchProcTest ## match03 ## extractLinesOfMatchedFilesIntoOutfileWhenListsAreSorted ##");
		URL expected = this.getClass().getResource("MatchTest_match03.csv");

		MatchMainProc mproc = new MatchMainProc();
		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// mathing file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", "test/out/match03.csv"};
		mproc.main(args);

		File actual = new File("test/out/match03.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

}
