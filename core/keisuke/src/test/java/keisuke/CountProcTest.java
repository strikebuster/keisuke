package keisuke;

import static keisuke.TestUtil.*;
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
 * Test class of CountProc.
 * @author strikebuster
 *
 */
public class CountProcTest {
	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void dealHelpOption() throws Exception {
		System.out.println("## CountProcTest ## arg01 ## dealHelpOption ##");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-?", "xxx"};
		cproc.main(args);

		assertThat(cproc.argMap(), is(nullValue()));
	}

	@Test
	public void dealArgsWhichHaveOnlyInfile() throws Exception {
		System.out.println("## CountProcTest ## arg02 ## dealArgsWhichHaveOnlyInfile ##");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"test/data/dummy.csv"};
		cproc.main(args);

		assertThat(cproc.argMap().size(), is(1));
		assertThat(cproc.argMap(), hasEntry("infile", "test/data/dummy.csv"));
	}

	@Test
	public void dealArgsWhichHavePropOption() throws Exception {
		System.out.println("## CountProcTest ## arg03 ## dealArgsWhichHavePropOption ##");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-prop", "test/data/ktest.properties", "test/data/dummy.csv"};
		cproc.main(args);

		assertThat(cproc.argMap().size(), is(2));
		assertThat(cproc.argMap(), hasEntry("infile", "test/data/dummy.csv"));
		assertThat(cproc.argMap(), hasEntry("properties", "test/data/ktest.properties"));
	}

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void dealInfileWhichDoesNotExist() throws Exception {
		System.out.println("## CountProcTest ## error01 ## dealInfileWhichDoesNotExist ##");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"test/data/nofile.csv"};
		cproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void dealPropOptionWhichFileDoesNotExist() throws Exception {
		System.out.println("## CountProcTest ##_error02 ## dealPropOptionWhichFileDoesNotExist ##");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-prop", "test/data/nofile.properties", "test/data/count01.csv"};
		cproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void dealXmlOptionWhichFileDoesNotExist() throws Exception {
		System.out.println("## CountProcTest ## error03 ## dealXmlOptionWhichFileDoesNotExist ##");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "language", "-xml", "test/data/nofile.xml", "test/data/count01.csv"};
		cproc.main(args);

		// Not reach here because exception is expected to be occured
		assertFalse(true);
	}

	@Test
	public void countUsingDefaultWhenArgsHaveIllegalXmlForLanguage() throws Exception {
		System.out.println(
				"## CountProcTest ## error04 ## countUsingDefaultWhenArgsHaveIllegalXmlForLanguage ##");
		URL expected = this.getClass().getResource("CountTest_count05.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "language", "-xml", "test/data/ktestf.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingDefaultWhenArgsHaveIllegalXmlForFramework() throws Exception {
		System.out.println(
			"## CountProcTest ## error05 ## countUsingDefaultWhenArgsHaveIllegalXmlForFramework ##");

		StderrCapture capture = new StderrCapture();

		URL expected = this.getClass().getResource("CountTest_error05.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "fw:rails", "-xml", "test/data/ktestl.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("not found Framework definition"));
	}

	@Test
	public void countUsingGivenXmlForFrameworkWhichDoesNotHaveTargetFramework() throws Exception {
		System.out.println("## CountProcTest ## error06 ## "
				+ "countUsingGivenXmlForFrameworkWhichDoesNotHaveTargetFramework ##");

		StderrCapture capture = new StderrCapture();

		URL expected = this.getClass().getResource("CountTest_error05.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "fw:spring", "-xml", "test/data/ktestf.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("not found Framework definition"));
	}

	@Test
	public void countUsingDefault() throws Exception {
		System.out.println("## CountProcTest ## count01 ## countUsingDefault ##");
		URL expected = this.getClass().getResource("CountTest_count01.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countEmptyInfileUsingDefault() throws Exception {
		System.out.println("## CountProcTest ## count02 ## countEmptyInfileUsingDefault ##");
		URL expected = this.getClass().getResource("CountTest_count02.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"test/data/dummy.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingPropOfEnglishTitles() throws Exception {
		System.out.println("## CountProcTest ## count03 ## countUsingPropOfEnglishTitles ##");
		URL expected = this.getClass().getResource("CountTest_count03.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-prop", "test/data/ktest.properties", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingDefaultWhenAgrsHaveIllegalClassOption() throws Exception {
		System.out.println("## CountProcTest ## count04 ## countUsingDefaultWhenAgrsHaveIllegalClassOption ##");
		URL expected = this.getClass().getResource("CountTest_count01.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "xxx", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfLanguage() throws Exception {
		System.out.println("## CountProcTest ## count05 ## countUsingClassOfLanguage ##");
		URL expected = this.getClass().getResource("CountTest_count05.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "language", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfGroup() throws Exception {
		System.out.println("## CountProcTet ## count06 ## countUsingClassOfGroup ##");
		URL expected = this.getClass().getResource("CountTest_count06.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"test/data/count01.csv", "-class", "group"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfLanguageAndGivenXmlWhichHasDecoratedNames() throws Exception {
		System.out.println(
			"## CountProcTest ## count07 ## countUsingClassOfLanguageAndGivenXmlWhichHasDecoratedNames ##");
		URL expected = this.getClass().getResource("CountTest_count07.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "language", "-xml", "test/data/ktestl.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfGroupAndGivenXmlWhichHasDecoratedNames() throws Exception {
		System.out.println(
			"## CountProcTest ## count08 ## countUsingClassOfGroupAndGivenXmlWhichHasDecoratedNames ##");
		URL expected = this.getClass().getResource("CountTest_count08.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "group", "test/data/count01.csv", "-xml", "test/data/ktestl.xml"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfFwAndGivenXmlWhichHasOw() throws Exception {
		System.out.println("## CountProcTest ## count20 ## countUsingClassOfFwAndGivenXmlWhichHasOw ##");
		URL expected = this.getClass().getResource("CountTest_count20.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "fw:ow", "-xml", "test/data/ktestf2.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfFwAndGivenXmlWhichHasOtherDefinitionOfOw() throws Exception {
		System.out.println(
			"## CountProcTest ## count21 ## countUsingClassOfFwAndGivenXmlWhichHasOtherDefinitionOfOw ##");
		URL expected = this.getClass().getResource("CountTest_count21.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "fw:ow", "-xml", "test/data/ktestf.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfFwAndGivenXmlWhichHasO3w() throws Exception {
		System.out.println("## CountProcTest ## count22 ## countUsingClassOfFwAndGivenXmlWhichHasO3w ##");
		URL expected = this.getClass().getResource("CountTest_count22.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "fw:o3w", "-xml", "test/data/ktestf2.xml", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countUsingClassOfFw() throws Exception {
		System.out.println("## CountProcTest ## count23 ## countUsingClassOfFw ##");
		URL expected = this.getClass().getResource("CountTest_count23.csv");

		CountMainProc cproc = new CountMainProc();
		String[] args = {"-class", "fw:spring", "test/data/count01.csv"};
		cproc.main(args);

		assertThat(cproc.reportText(), is(equalTo(contentOf(expected))));
	}

}
