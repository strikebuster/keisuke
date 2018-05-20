package keisuke.report.procedure;

import keisuke.util.StderrCapture;
import keisuke.util.StdoutCapture;

import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static keisuke.report.option.ReportOptionConstant.*;
import static keisuke.util.TestUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * Test class of DiffProc.
 *
 */
public class DiffMainProcTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void handleHelpOption() throws Exception {
		//String newRoot = "test/sample/java/root1";
		System.out.println("## DiffProcTest ## arg01 ## handleHelpOption ##");
		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-?", "xxx"};
		dproc.main(args);
		assertThat(dproc.argMapEntity(), is(nullValue()));
	}

	@Test
	public void handleArgsWhichHaveAoutAndMoutOption() throws Exception {
		System.out.println("## DiffProcTest ## arg02 ## handleArgsWhichHaveAoutAndMoutOption ##");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-a", "test/out/add02a.txt", "-m", "test/out/modify02a.txt",
				"test/data/dummy.txt"};
		dproc.main(args);

		final int expectedNumber = 3;
		assertThat(dproc.argMapEntity().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMapEntity(), hasEntry(ARG_INPUT, "test/data/dummy.txt"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_AOUT, "test/out/add02a.txt"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_MOUT, "test/out/modify02a.txt"));
	}

	@Test
	public void handleArgsWhichHavePropOption() throws Exception {
		System.out.println("## DiffProcTest ## arg03 ## handleArgsWhichHavePropOption ##");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-p", "test/data/ktest.properties", "test/data/dummy.txt"};
		dproc.main(args);

		assertThat(dproc.argMapEntity().size(), is(greaterThanOrEqualTo(2)));
		assertThat(dproc.argMapEntity(), hasEntry(ARG_INPUT, "test/data/dummy.txt"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_PROP, "test/data/ktest.properties"));
	}

	@Test
	public void handleArgsWhichHaveUnchangeOptionButMissingValue() throws Exception {
		System.out.println("## DiffProcTest ## arg04 ## handleArgsWhichHaveUnchangeOptionButMissingValue ##");

		StdoutCapture capture = new StdoutCapture();
		String outMessage = null;
		Exception firedException = null;
		String expected = "fail to parse";

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/dummy.txt", "-u"};
		try {
			dproc.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			outMessage = capture.getCapturedString();
			capture.finish();
		}
		assertThat(dproc.argMapEntity(), is(nullValue()));
		assertThat(outMessage, containsString("usage"));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}

	@Test
	public void handleArgsWhichHaveUnchangeOptionButWrongValue() throws Exception {
		System.out.println("## DiffProcTest ## arg05 ## handleArgsWhichHaveUnchangeOptionButWrongValue ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;
		Exception firedException = null;
		String expected = "invalid option value";

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"--" + OPT_UNCHANGE, "xxx", "test/data/diff01.txt"};
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
		assertThat(dproc.argMapEntity(), hasEntry(OPT_UNCHANGE, "xxx"));
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
	public void handleArgsWhichHaveUnchangeOptionAndValueIsTotal() throws Exception {
		System.out.println("## DiffProcTest ## arg06 ## handleArgsWhichHaveUnchangeOptionAndValueIsTotal ##");
		URL expected = this.getClass().getResource("DiffTest_arg06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_UNCHANGE, OPTVAL_TOTAL, "test/data/diff01.txt"};
		dproc.main(args);

		final int expectedNumber = 3;
		assertThat(dproc.argMapEntity().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMapEntity(), hasEntry(ARG_INPUT, "test/data/diff01.txt"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_UNCHANGE, OPTVAL_TOTAL));
		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void handleArgsWhichHaveClassOptionButWrongValue() throws Exception {
		System.out.println("## DiffProcTest ## arg07 ## handleArgsWhichHaveClassOptionButWrongValue ##");

		StderrCapture capture = new StderrCapture();
		String errMessage = null;
		Exception firedException = null;
		String expected = "invalid option value";

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_CLASS, "xxx", "test/data/diff01.txt"};
		try {
			dproc.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			errMessage = capture.getCapturedString();
			capture.finish();
		}
		final int expectedNumber = 2;
		assertThat(dproc.argMapEntity().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMapEntity(), hasEntry(ARG_INPUT, "test/data/diff01.txt"));
		assertThat(dproc.argMapEntity(), hasEntry(OPT_CLASS, "xxx"));
		assertThat(errMessage, containsString(expected));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}

	@Test
	public void handleInfileWhichDoesNotExist() throws Exception {
		System.out.println("## DiffProcTest ## error01 ## handleInfileWhichDoesNotExist ##");
		String expected = "FileNotFoundException";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/nofile.txt"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handlePropOptionWhichFileDoesNotExist() throws Exception {
		System.out.println("## DiffProcTest ## error02 ## handlePropOptionWhichFileDoesNotExist ##");
		String expected = "FileNotFoundException";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-c", OPTVAL_EXTENSION,
				"-prop", "test/data/nofile.properties"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleXmlOptionWhichFileDoesNotExistAndClassOptionIsLanguage() throws Exception {
		System.out.println("## DiffProcTest ## error03 ## "
				+ "handleXmlOptionWhichFileDoesNotExistAndClassOptionIsLanguage ##");
		String expected = "FileNotFoundException";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-class", OPTVAL_LANGUAGE, "-x", "test/data/nofile.xml"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		fail("Should throw Exception : " + expected);
	}

	@Test
	public void accountUsingDefaultWhenArgsHaveIllegalXmlForLanguageAndClassOptionIsGroup() throws Exception {
		System.out.println("## DiffProcTest ## error04 ## "
				+ "accountUsingDefaultWhenArgsHaveIllegalXmlForLanguageAndClassOptionIsGroup ##");
		URL expected = this.getClass().getResource("DiffTest_diff07.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "--" + OPT_CLASS, OPTVAL_LANGGROUP,
				"--" + OPT_XML, "test/data/ktestf.xml"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void handleXmlOptionWhichFileDoesNotExistAndClassOptionIsFw() throws Exception {
		System.out.println("## DiffProcTest ## error05 ## "
				+ "handleXmlOptionWhichFileDoesNotExistAndClassOptionIsFw ##");
		//URL expected = this.getClass().getResource("DiffTest_diff20.csv");
		String expected = "FileNotFoundException";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_CLASS, OPTVAL_FW + "struts", "-" + OPT_XML, "test/data/nofile.xml",
				"test/data/diff01.txt"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		fail("Should throw Exception : " + expected);
	}

	@Test
	public void accountUsingDefaultWhenArgsHaveIllegalXmlForFwAndClassOptionIsFw() throws Exception {
		System.out.println("## DiffProcTest ## error06 ## "
				+ "accountUsingDefaultWhenArgsHaveIllegalXmlForFwAndClassOptionIsFw ##");

		StderrCapture capture = new StderrCapture();

		URL expected = this.getClass().getResource("DiffTest_error06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-c", OPTVAL_FW + "rails", "-x", "test/data/ktestl.xml", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("not found Framework definition"));
	}

	@Test
	public void accountUsingGivenXmlForFrameworkWhichDoesNotHaveTargetFramework() throws Exception {
		System.out.println("## DiffProcTest ## error07 ## "
				+ "accountUsingGivenXmlForFrameworkWhichDoesNotHaveTargetFramework ##");

		StderrCapture capture = new StderrCapture();

		URL expected = this.getClass().getResource("DiffTest_error06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-c", OPTVAL_FW + "spring", "-x", "test/data/ktestf.xml", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("not found Framework definition"));
	}

	@Test
	public void accountUsingDefault() throws Exception {
		System.out.println("## DiffProcTest ## diff01 ## accountUsingDefault ##");
		URL expected = this.getClass().getResource("DiffTest_diff01.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountEmptyInfileUsingDefault() throws Exception {
		System.out.println("## DiffProcTest ## diff02 ## accountEmptyInfileUsingDefault ##");
		URL expected = this.getClass().getResource("DiffTest_diff02.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/dummy.csv"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingDefaultAndAoutOptionIsGiven() throws Exception {
		System.out.println("## DiffProcTest ## diff03 ## accountUsingDefaultAndAoutOptionIsGiven ##");
		URL expected = this.getClass().getResource("DiffTest_diff03.txt");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"--" + OPT_AOUT, "test/out/add03.txt", "test/data/diff01.txt"};
		dproc.main(args);

		File actual = new File("test/out/add03.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingDefaultAndMoutOptionIsGiven() throws Exception {
		System.out.println("## DiffProcTest ## diff04 ## accountUsingDefaultAndMoutOptionIsGiven ##");
		URL expected = this.getClass().getResource("DiffTest_diff04.txt");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-" + OPT_AOUT, "test/out/add04.txt", "--" + OPT_MOUT, "test/out/modify04.txt",
			"test/data/diff01.txt"};
		dproc.main(args);

		File actual = new File("test/out/modify04.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingPropOfEnglishTitles() throws Exception {
		System.out.println("## DiffProcTest ## diff05 ## accountUsingPropOfEnglishTitles ##");
		URL expected = this.getClass().getResource("DiffTest_diff05.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-c", "extension", "--" + OPT_PROP,
				"test/data/ktest.properties"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfLanguage() throws Exception {
		System.out.println("## DiffProcTest ## diff06 ## accountUsingClassOfLanguage ##");
		URL expected = this.getClass().getResource("DiffTest_diff06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-c", OPTVAL_LANGUAGE};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfGroup() throws Exception {
		System.out.println("## DiffProcTest ## diff07 ## accountUsingClassOfGroup ##");
		URL expected = this.getClass().getResource("DiffTest_diff07.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-c", OPTVAL_LANGGROUP, "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfLanguageAndGivenXmlWhichHasDecoratedNames() throws Exception {
		System.out.println(
			"## DiffProcTest ## diff08 ## accountUsingClassOfLanguageAndGivenXmlWhichHasDecoratedNames ##");
		URL expected = this.getClass().getResource("DiffTest_diff08.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-x", "test/data/ktestl.xml", "-c", OPTVAL_LANGUAGE};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfGroupAndGivenXmlWhichHasDecoratedNames() throws Exception {
		System.out.println("## DiffProcTest ## diff09 ## "
				+ "accountUsingClassOfGroupAndGivenXmlWhichHasDecoratedNames ##");
		URL expected = this.getClass().getResource("DiffTest_diff09.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-x", "test/data/ktestl.xml", "-c", OPTVAL_LANGGROUP, "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFwAndGivenXmlWhichHasOw() throws Exception {
		System.out.println("## DiffProcTest ## diff20 ## accountUsingClassOfFwAndGivenXmlWhichHasOw ##");
		URL expected = this.getClass().getResource("DiffTest_diff20.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-c", OPTVAL_FW + "ow", "-x", "test/data/ktestf2.xml", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFwAndGivenXmlWhichHasOtherDefinitionOfOw() throws Exception {
		System.out.println("## DiffProcTest ## diff21 ## "
				+ "accountUsingClassOfFwAndGivenXmlWhichHasOtherDefinitionOfOw ##");
		URL expected = this.getClass().getResource("DiffTest_diff21.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-x", "test/data/ktestf.xml", "-c", OPTVAL_FW + "ow", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFwAndGivenXmlWhichHasO3w() throws Exception {
		System.out.println("## DiffProcTest ## diff22 ## accountUsingClassOfFwAndGivenXmlWhichHasO3w ##");
		URL expected = this.getClass().getResource("DiffTest_diff22.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-c", OPTVAL_FW + "o3w", "-x", "test/data/ktestf2.xml", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFw() throws Exception {
		System.out.println("## DiffProcTest ## diff23 ## accountUsingClassOfFw ##");
		URL expected = this.getClass().getResource("DiffTest_diff23.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-c", OPTVAL_FW + "struts", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

}
