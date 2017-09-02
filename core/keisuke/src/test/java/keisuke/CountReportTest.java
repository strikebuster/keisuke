package keisuke;

import static keisuke.util.TestUtil.contentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.util.StdoutCapture;

/**
 * Test class of CountReport.
 */
public class CountReportTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void countUsingDefault() throws Exception {
		System.out.println("## CountReportTest ## count01 ## countUsingDefault ##");
		URL expected = this.getClass().getResource("report/procedure/CountTest_count01.csv");

		StdoutCapture capture = new StdoutCapture();

		String[] args = {"test/data/count01.csv"};
		CountReport.main(args);

		String outMessage = capture.getCapturedString();
		capture.finish();
		assertThat(outMessage, is(equalTo(contentOf(expected))));
	}
}
