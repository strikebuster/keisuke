package keisuke.report.procedure;

import static keisuke.report.option.ReportOptionConstant.ARG_INPUT;
import static keisuke.report.option.ReportOptionConstant.OPT_AOUT;
import static keisuke.report.option.ReportOptionConstant.OPT_FORMAT;
import static keisuke.report.option.ReportOptionConstant.OPT_MOUT;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.util.StderrCapture;

/**
 * Test class of DiffProc with format option.
 */
public class DiffMainProcFormatTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void handleArgsWhichHaveFormatOptionButWrongValue() throws Exception {
		System.out.println("## DiffProcFormatTest ## arg11 ## handleArgsWhichHaveFormatOptionButWrongValue ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;
		Exception firedException = null;
		String expected = "invalid format value";

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"--" + OPT_FORMAT, "xxx", "test/data/diff01.txt"};
		try {
			dproc.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			errMessage = capture.getCapturedString();
			capture.finish();
		}

		final int expectedNumber = 4;
		assertThat(dproc.argMapEntity().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMapEntity(), hasEntry(ARG_INPUT, "test/data/diff01.txt"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_FORMAT, "xxx"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_AOUT, null));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_MOUT, null));
		assertThat(errMessage, containsString(expected));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}

	@Test
	public void accountUsingTextFormatAndAoutAndMoutOptionIsGiven() throws Exception {
		System.out.println("## DiffProcFormatTest ## diff11 ## "
				+ "accountUsingTextFormatAndAoutAndMoutOptionIsGiven ##");
		URL expected = this.getClass().getResource("DiffTest_diff01.csv");
		URL expectedA = this.getClass().getResource("DiffTest_diff03.txt");
		URL expectedM = this.getClass().getResource("DiffTest_diff04.txt");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_AOUT, "test/out/add11.txt", "-" + OPT_MOUT, "test/out/modify11.txt",
				"-" + OPT_FORMAT, "text", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(textContentOf(expected))));
		File actual = new File("test/out/add11.txt");
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expectedA))));
		actual = new File("test/out/modify11.txt");
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expectedM))));
	}

	@Test
	public void accountUsingCsvFormatAndAoutAndMoutOptionIsGiven() throws Exception {
		System.out.println("## DiffProcFormatTest ## diff12 ## "
			+ "accountUsingCsvFormatAndAoutAndMoutOptionIsGiven ##");
		URL expected = this.getClass().getResource("DiffTest_diff01.csv");
		URL expectedA = this.getClass().getResource("DiffTest_diff03b.txt");
		URL expectedM = this.getClass().getResource("DiffTest_diff04b_csv.txt");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_AOUT, "test/out/add12.txt", "-" + OPT_MOUT, "test/out/modify12.txt",
				"-" + OPT_FORMAT, "csv", "test/data/diff01b.csv"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(textContentOf(expected))));
		File actual = new File("test/out/add12.txt");
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expectedA))));
		actual = new File("test/out/modify12.txt");
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expectedM))));
	}

	@Test
	public void accountUsingCsvFormatWithSubPathAndAoutAndMoutOptionIsGiven() throws Exception {
		System.out.println("## DiffProcFormatTest ## diff13 ## "
			+ "accountUsingCsvFormatWithSubPathAndAoutAndMoutOptionIsGiven ##");
		URL expected = this.getClass().getResource("DiffTest_diff01.csv");
		URL expectedA = this.getClass().getResource("DiffTest_diff03s.txt");
		URL expectedM = this.getClass().getResource("DiffTest_diff04s_csv.txt");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_AOUT, "test/out/add13.txt", "-" + OPT_MOUT, "test/out/modify13.txt",
				"-" + OPT_FORMAT, "csv", "test/data/diff01s.csv"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(textContentOf(expected))));
		File actual = new File("test/out/add13.txt");
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expectedA))));
		actual = new File("test/out/modify13.txt");
		assertThat(textContentOf(actual), is(equalTo(textContentOf(expectedM))));
	}
}
