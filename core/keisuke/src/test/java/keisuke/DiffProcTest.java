package keisuke;

import static keisuke.TestUtil.*;
import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test class of DiffProc.
 * @author strikebuster
 *
 */
public class DiffProcTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void dealHelpOption() throws Exception {
		//String newRoot = "test/sample/java/root1";
		System.out.println("## DiffProcTest ## arg01 ## dealHelpOption ##");
		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-?", "xxx"};
		dproc.main(args);
		assertThat(dproc.argMap(), is(nullValue()));
	}

	@Test
	public void dealArgsWhichHaveAoutAndMoutOption() throws Exception {
		System.out.println("## DiffProcTest ## arg02 ## dealArgsWhichHaveAoutAndMoutOption ##");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-aout", "test/out/add02a.txt", "-mout", "test/out/modify02a.txt",
				"test/data/dummy.txt"};
		dproc.main(args);

		final int expectedNumber = 3;
		assertThat(dproc.argMap().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMap(), hasEntry("infile", "test/data/dummy.txt"));
		assertThat(dproc.argMap(), hasEntry("aout", "test/out/add02a.txt"));
		assertThat(dproc.argMap(), hasEntry("mout", "test/out/modify02a.txt"));
	}

	@Test
	public void dealArgsWhichHavePropOption() throws Exception {
		System.out.println("## DiffProcTest ## arg03 ## dealArgsWhichHavePropOption ##");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-prop", "test/data/ktest.properties", "test/data/dummy.txt"};
		dproc.main(args);

		assertThat(dproc.argMap().size(), is(greaterThanOrEqualTo(2)));
		assertThat(dproc.argMap(), hasEntry("infile", "test/data/dummy.txt"));
		assertThat(dproc.argMap(), hasEntry("properties", "test/data/ktest.properties"));
	}

	@Test
	public void dealArgsWhichHaveUnchangeOptionButMissingValue() throws Exception {
		System.out.println("## DiffProcTest ## arg04 ## dealArgsWhichHaveUnchangeOptionButMissingValue ##");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/dummy.txt", "-unchange"};
		dproc.main(args);

		assertThat(dproc.argMap(), is(nullValue()));
	}

	@Test
	public void dealArgsWhichHaveUnchangeOptionButWrongValue() throws Exception {
		System.out.println("## DiffProcTest ## arg05 ## dealArgsWhichHaveUnchangeOptionButWrongValue ##");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-unchange", "xxx", "test/data/diff01.txt"};
		dproc.main(args);

		final int expectedNumber = 4;
		assertThat(dproc.argMap().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMap(), hasEntry("infile", "test/data/diff01.txt"));
		assertThat(dproc.argMap(), hasEntry("unchange", "xxx"));
		assertThat(dproc.argMap(), hasEntry("aout", null));
		assertThat(dproc.argMap(), hasEntry("mout", null));
	}

	@Test
	public void dealArgsWhichHaveUnchangeOptionAndValueIsTotal() throws Exception {
		System.out.println("## DiffProcTest ## arg06 ## dealArgsWhichHaveUnchangeOptionAndValueIsTotal ##");
		URL expected = this.getClass().getResource("DiffTest_arg06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-unchange", "total", "test/data/diff01.txt"};
		dproc.main(args);

		final int expectedNumber = 3;
		assertThat(dproc.argMap().size(), is(greaterThanOrEqualTo(expectedNumber)));
		assertThat(dproc.argMap(), hasEntry("infile", "test/data/diff01.txt"));
		assertThat(dproc.argMap(), hasEntry("unchange", "total"));
		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void dealInfileWhichDoesNotExist() throws Exception {
		System.out.println("## DiffProcTest ## error01 ## dealInfileWhichDoesNotExist ##");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/nofile.txt"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void dealPropOptionWhichFileDoesNotExist() throws Exception {
		System.out.println("## DiffProcTest ## error02 ## dealPropOptionWhichFileDoesNotExist ##");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-class", "extension", "-prop", "test/data/nofile.properties"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void dealXmlOptionWhichFileDoesNotExistAndClassOptionIsLanguage() throws Exception {
		System.out.println(
			"## DiffProcTest ## error03 ## dealXmlOptionWhichFileDoesNotExistAndClassOptionIsLanguage ##");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-class", "language", "-xml", "test/data/nofile.xml"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void accountUsingDefaultWhenArgsHaveIllegalXmlForLanguageAndClassOptionIsGroup() throws Exception {
		System.out.println("## DiffProcTest ## error04 ## "
				+ "accountUsingDefaultWhenArgsHaveIllegalXmlForLanguageAndClassOptionIsGroup ##");
		URL expected = this.getClass().getResource("DiffTest_diff07.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-class", "group", "-xml", "test/data/ktestf.xml"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void dealXmlOptionWhichFileDoesNotExistAndClassOptionIsFw() throws Exception {
		System.out.println(
			"## DiffProcTest ## error05 ## dealXmlOptionWhichFileDoesNotExistAndClassOptionIsFw ##");
		//URL expected = this.getClass().getResource("DiffTest_diff20.csv");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-class", "fw:struts", "-xml", "test/data/nofile.xml", "test/data/diff01.txt"};
		dproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void accountUsingDefaultWhenArgsHaveIllegalXmlForFwAndClassOptionIsFw() throws Exception {
		System.out.println("## DiffProcTest ## error06 ## "
				+ "accountUsingDefaultWhenArgsHaveIllegalXmlForFwAndClassOptionIsFw ##");

		StderrCapture capture = new StderrCapture();

		URL expected = this.getClass().getResource("DiffTest_error06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-class", "fw:rails", "-xml", "test/data/ktestl.xml", "test/data/diff01.txt"};
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
		String[] args = {"-class", "fw:spring", "-xml", "test/data/ktestf.xml", "test/data/diff01.txt"};
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
		String[] args = {"-aout", "test/out/add03.txt", "test/data/diff01.txt"};
		dproc.main(args);

		File actual = new File("test/out/add03.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingDefaultAndMoutOptionIsGiven() throws Exception {
		System.out.println("## DiffProcTest ## diff04 ## accountUsingDefaultAndMoutOptionIsGiven ##");
		URL expected = this.getClass().getResource("DiffTest_diff04.txt");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-aout", "test/out/add04.txt", "-mout", "test/out/modify04.txt",
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
		String[] args = {"test/data/diff01.txt", "-class", "xxx", "-prop", "test/data/ktest.properties"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfLanguage() throws Exception {
		System.out.println("## DiffProcTest ## diff06 ## accountUsingClassOfLanguage ##");
		URL expected = this.getClass().getResource("DiffTest_diff06.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-class", "language"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfGroup() throws Exception {
		System.out.println("## DiffProcTest ## diff07 ## accountUsingClassOfGroup ##");
		URL expected = this.getClass().getResource("DiffTest_diff07.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-class", "group", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfLanguageAndGivenXmlWhichHasDecoratedNames() throws Exception {
		System.out.println(
			"## DiffProcTest ## diff08 ## accountUsingClassOfLanguageAndGivenXmlWhichHasDecoratedNames ##");
		URL expected = this.getClass().getResource("DiffTest_diff08.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"test/data/diff01.txt", "-xml", "test/data/ktestl.xml", "-class", "language"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfGroupAndGivenXmlWhichHasDecoratedNames() throws Exception {
		System.out.println("## DiffProcTest ## diff09 ## "
				+ "accountUsingClassOfGroupAndGivenXmlWhichHasDecoratedNames ##");
		URL expected = this.getClass().getResource("DiffTest_diff09.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-xml", "test/data/ktestl.xml", "-class", "group", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFwAndGivenXmlWhichHasOw() throws Exception {
		System.out.println("## DiffProcTest ## diff20 ## accountUsingClassOfFwAndGivenXmlWhichHasOw ##");
		URL expected = this.getClass().getResource("DiffTest_diff20.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-class", "fw:ow", "-xml", "test/data/ktestf2.xml", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFwAndGivenXmlWhichHasOtherDefinitionOfOw() throws Exception {
		System.out.println("## DiffProcTest ## diff21 ## "
				+ "accountUsingClassOfFwAndGivenXmlWhichHasOtherDefinitionOfOw ##");
		URL expected = this.getClass().getResource("DiffTest_diff21.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-xml", "test/data/ktestf.xml", "-class", "fw:ow", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFwAndGivenXmlWhichHasO3w() throws Exception {
		System.out.println("## DiffProcTest ## diff22 ## accountUsingClassOfFwAndGivenXmlWhichHasO3w ##");
		URL expected = this.getClass().getResource("DiffTest_diff22.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-class", "fw:o3w", "-xml", "test/data/ktestf2.xml", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void accountUsingClassOfFw() throws Exception {
		System.out.println("## DiffProcTest ## diff23 ## accountUsingClassOfFw ##");
		URL expected = this.getClass().getResource("DiffTest_diff23.csv");

		DiffMainProc dproc = new DiffMainProc();
		String[] args = {"-class", "fw:struts", "test/data/diff01.txt"};
		dproc.main(args);

		assertThat(dproc.reportText(), is(equalTo(contentOf(expected))));
	}

}
