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
 * Test class of DiffReport.
 */
public class DiffReportTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void accountUsingDefault() throws Exception {
		System.out.println("## DiffReportTest ## diff01 ## accountUsingDefault ##");
		URL expected = this.getClass().getResource("report/procedure/DiffTest_diff01.csv");

		StdoutCapture capture = new StdoutCapture();

		String[] args = {"test/data/diff01.txt"};
		DiffReport.main(args);

		String outMessage = capture.getCapturedString();
		capture.finish();
		assertThat(outMessage, is(equalTo(contentOf(expected))));
	}

}
