package keisuke;

import static keisuke.report.option.ReportOptionConstant.OPT_OUT;
import static keisuke.util.TestUtil.contentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
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
		String outMessage = null;

		String[] args = {"test/data/count01.csv"};
		try {
			CountReport.main(args);
		} finally {
			outMessage = capture.getCapturedString();
			capture.finish();
		}

		assertThat(outMessage, is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingDefaultAndOutOption() throws Exception {
		System.out.println("## CountReportTest ## count02 ## countUsingDefaultAndOutOption ##");
		URL expected = this.getClass().getResource("report/procedure/CountTest_count01.csv");

		String[] args = {"test/data/count01.csv", "--" + OPT_OUT, "test/out/coutrep02.csv"};
		CountReport.main(args);

		File actual = new File("test/out/coutrep02.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

}
