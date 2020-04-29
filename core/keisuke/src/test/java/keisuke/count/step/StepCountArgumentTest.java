package keisuke.count.step;

import static keisuke.count.option.CountOptionConstant.OPT_ENCODE;
import static keisuke.count.option.CountOptionConstant.OPT_FORMAT;
import static keisuke.count.option.CountOptionConstant.OPT_OUTPUT;
import static keisuke.count.option.CountOptionConstant.OPT_PATH;
import static keisuke.count.option.CountOptionConstant.OPT_SORT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import keisuke.util.StderrCapture;
import keisuke.util.StdoutCapture;

/**
 * Test class of StepCounter about command line arguments.
 *
 */
public class StepCountArgumentTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void handleHelpOption() throws Exception {
		System.out.println("## StepCount ## arg01 ## handleHelpOption ##");
		//URL expected = this.getClass().getResource("empty.txt");

		String[] args = {"-?", "xxx"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		//assertThat(contentOf(stepcount.argMap()), is(equalTo(contentOf(expected))));
		assertThat(stepcount.argMapEntity(), is(nullValue()));
	}

	@Test
	public void handleIllegalOption() throws Exception {
		System.out.println("## StepCount ## arg02 ## handleIllegalOption ##");
		String expected = "fail to parse";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"-zzz", "-s", "-e", "EUC-JP"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButSourceRootIsNotGiven() throws Exception {
		System.out.println("## StepCount ## arg03 ## handleOptionsButSourceRootIsNotGiven ##");

		StdoutCapture outCapture = new StdoutCapture();
		StderrCapture errCapture = new StderrCapture();
		String outMessage = null;
		String errMessage = null;
		Exception firedException = null;
		String expected = "no file argument";

		String[] args = {"-s", "-e", "EUC-JP", "-f", "xml", "-x", "test/data/ktestl2.xml"};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			outMessage = outCapture.getCapturedString();
			outCapture.finish();
			errMessage = errCapture.getCapturedString();
			errCapture.finish();
		}

		final int expectedNumber = 4;
		assertThat(stepcount.argMapEntity().size(), is(expectedNumber));
		assertThat(stepcount.argMapEntity(), hasEntry("showDirectory", "true"));
		assertThat(stepcount.argMapEntity(), hasEntry("xml", "test/data/ktestl2.xml"));
		assertThat(stepcount.argMapEntity(), hasEntry("format", "xml"));
		assertThat(stepcount.argMapEntity(), hasEntry("encoding", "EUC-JP"));

		assertThat(outMessage, containsString("usage"));
		assertThat(errMessage, containsString("not specified source path"));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}

	@Test
	public void handleOptionsButInvalidFormat() throws Exception {
		System.out.println("## StepCount ## arg04 ## handleOptionsButInvalidFormat ##");
		String newRoot = "test/data/java/root1";
		String expected = "invalid format value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"--" + OPT_FORMAT, "html", "--" + OPT_ENCODE, "UTF-8",
				"--" + OPT_OUTPUT, "test/out/count_arg04.html", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButInvalidSort() throws Exception {
		System.out.println("## StepCount ## arg05 ## handleOptionsButInvalidSort ##");
		String newRoot = "test/data/java/root1";
		String expected = "invalid sort value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"--" + OPT_SORT, "abc", "-encoding", "UTF-8",
				"-output", "test/out/count_arg05.txt", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButInvalidPath() throws Exception {
		System.out.println("## StepCount ## arg06 ## handleOptionsButInvalidPath ##");
		String newRoot = "test/data/java/root1";
		String expected = "invalid path value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"--" + OPT_PATH, "abc", "-encoding", "UTF-8",
				"-output", "test/out/count_arg05.txt", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButSourceRootIsNotGiven2() throws Exception {
		System.out.println("## StepCount ## arg07 ## handleOptionsButSourceRootIsNotGiven2 ##");

		StdoutCapture outCapture = new StdoutCapture();
		StderrCapture errCapture = new StderrCapture();
		String outMessage = null;
		String errMessage = null;
		Exception firedException = null;
		String expected = "no file argument";

		String[] args = {"-p", "sub", "-e", "EUC-JP", "-f", "xml", "-x", "test/data/ktestl2.xml"};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			outMessage = outCapture.getCapturedString();
			outCapture.finish();
			errMessage = errCapture.getCapturedString();
			errCapture.finish();
		}

		final int expectedNumber = 4;
		assertThat(stepcount.argMapEntity().size(), is(expectedNumber));
		assertThat(stepcount.argMapEntity(), hasEntry("path", "sub"));
		assertThat(stepcount.argMapEntity(), hasEntry("xml", "test/data/ktestl2.xml"));
		assertThat(stepcount.argMapEntity(), hasEntry("format", "xml"));
		assertThat(stepcount.argMapEntity(), hasEntry("encoding", "EUC-JP"));

		assertThat(outMessage, containsString("usage"));
		assertThat(errMessage, containsString("not specified source path"));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}
}
