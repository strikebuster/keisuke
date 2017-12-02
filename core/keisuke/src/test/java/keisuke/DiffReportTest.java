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
		String outMessage = null;

		String[] args = {"test/data/diff01.txt"};
		try {
			DiffReport.main(args);
		} finally {
			outMessage = capture.getCapturedString();
			capture.finish();
		}

		assertThat(outMessage, is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingDefaultAndOutOption() throws Exception {
		System.out.println("## DiffReportTest ## diff02 ## accountUsingDefaultAndOutOption ##");
		URL expected = this.getClass().getResource("report/procedure/DiffTest_diff01.csv");

		String[] args = {"test/data/diff01.txt", "-out", "test/out/diffrep02.csv"};
		DiffReport.main(args);

		File actual = new File("test/out/diffrep02.csv");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}
}
