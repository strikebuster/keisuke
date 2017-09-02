package keisuke;

import static keisuke.util.TestUtil.contentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class of MatchExtract.
 */
public class MatchExtractTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void extractLinesOfMatchedFilesIntoOutfileWhenListsAreSorted() throws Exception {
		System.out.println(
				"## MatchExtractTest ## extract03 ## "
				+ "extractLinesOfMatchedFilesIntoOutfileWhenListsAreSorted ##");
		URL expected = this.getClass().getResource("report/procedure/MatchTest_match03.csv");

		// match_ma03_sorted.csv: /oldsrc/ is root
		// match_tr03_sorted.txt: /src/ is root
		// mathing file path, but ignore difference of root dir
		String[] args = {"test/data/match_ma03_sorted.csv",
				"test/data/match_tr03_sorted.txt", "test/out/extract03.csv"};
		MatchExtract.main(args);

		File actual = new File("test/out/extract03.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}
}
