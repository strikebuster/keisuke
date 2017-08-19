package keisuke.count;

import keisuke.StderrCapture;
import static keisuke.count.SCTestUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class of StepCounter.
 * @author strkebuster
 *
 */
public class StepCounterTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void dealHelpOption() throws Exception {
		System.out.println("## StepCount ## arg01 ## dealHelpOption ##");
		//URL expected = this.getClass().getResource("empty.txt");

		String[] args = {"-?", "xxx"};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		//assertThat(contentOf(stepcount.argMap()), is(equalTo(contentOf(expected))));
		assertThat(stepcount.argMap(), is(nullValue()));
	}

	@Test
	public void dealIllegalOption() throws Exception {
		System.out.println("## StepCount ## arg02 ## dealIllegalOption ##");
		//URL expected = this.getClass().getResource("empty.txt");

		String[] args = {"-zzz", "-s", "-e", "EUC-JP"};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		//assertThat(contentOf(stepcount.argMap()), is(equalTo(contentOf(expected))));
		assertThat(stepcount.argMap(), is(nullValue()));
	}

	@Test
	public void dealOptionsButSourceRootIsNotGiven() throws Exception {
		System.out.println("## StepCount ## arg03 ## dealOptionsButSourceRootIsNotGiven ##");

		StderrCapture capture = new StderrCapture();

		URL expected = this.getClass().getResource("testCount_arg03.txt");

		String[] args = {"-s", "-e", "EUC-JP", "-f", "xml", "-x", "test/data/ktestl2.xml"};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		final int expectedNumber = 4;
		assertThat(contentOf(stepcount.argMap()), is(equalTo(contentOf(expected))));
		assertThat(stepcount.argMap().size(), is(expectedNumber));
		assertThat(stepcount.argMap(), hasEntry("showDirectory", "true"));
		assertThat(stepcount.argMap(), hasEntry("xml", "test/data/ktestl2.xml"));
		assertThat(stepcount.argMap(), hasEntry("format", "xml"));
		assertThat(stepcount.argMap(), hasEntry("encoding", "EUC-JP"));

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("Not specified source path"));
	}

	@Test
	public void countJavaUsingCsvFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		File actual = new File("test/out/count_java.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.txt");

		String[] args = {"-s", "-f", "csv", "-e", "UTF-8",
				"-o", "test/out/count_java_en.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingDefaultFormatWhichIsText() throws Exception {
		System.out.println("## StepCount ## countJavaUsingTextFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java_text.txt");

		String[] args = {"-showDirectory", "-encoding", "UTF-8",
				"-output", "test/out/count_java_text.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		File actual = new File("test/out/count_java_text.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingTextFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingTextFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java_text_en.txt");

		String[] args = {"--showDirectory", "--encoding", "UTF-8",
				"--output", "test/out/count_java_text_en.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		Locale.setDefault(org);
		File actual = new File("test/out/count_java_text_en.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingJsonFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java.json");

		String[] args = {"-showDirectory", "-format", "json", "-encoding", "UTF-8",
				"-output", "test/out/count_java.json", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		File actual = new File("test/out/count_java.json");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingJsonFormatWhenLocaleIsEnglish ##");

		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.json");

		String[] args = {"-s", "--format", "json", "-encoding", "UTF-8",
				"--output", "test/out/count_java_en.json", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.json");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingXmlFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingXmlFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java.xml");

		String[] args = {"-showDirectory", "-format", "xml", "-encoding", "UTF-8",
				"-output", "test/out/count_java.xml", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		File actual = new File("test/out/count_java.xml");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingXmlFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingXmlFormatWhenLocaleIsEnglish ##");

		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.xml");

		String[] args = {"-showDirectory", "-format", "xml", "-encoding", "UTF-8",
				"-output", "test/out/count_java_en.xml", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.xml");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countJavaUsingExcelFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcelFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java.xls");

		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", "test/out/count_java.xls", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		File actual = new File("test/out/count_java.xls");
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcelFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcelFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.xls");

		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", "test/out/count_java_en.xls", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.xls");
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countWebCodingInUtf() throws Exception {
		System.out.println("## StepCount ## countWebCodingInUtf ##");
		String newRoot = "test/data/web/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_web.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_web.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_web.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countWebCodingInSjis() throws Exception {
		System.out.println("## StepCount ## countWebCodingInSjis ##");
		String newRoot = "test/data/web/root1S";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_webS.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_webS.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_webS.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countC() throws Exception {
		System.out.println("## StepCount ## countC ##");
		String newRoot = "test/data/c/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_c.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/count_c.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_c.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjis() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludingMiscCommentsInSjis ##");
		String newRoot = "test/data/commentS/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_commentS.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_commentS.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentS.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjis2() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludingMiscCommentsInSjis(2) ##");
		String newRoot = "test/data/commentS/root10";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_commentS2.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_commentS2.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentS2.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludigMiscCommentsInUtf() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludigMiscCommentsInUtf ##");
		String newRoot = "test/data/commentU/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_commentU.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_commentU.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentU.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludigMiscCommentsInUtf2() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludigMiscCommentsInUtf(2) ##");
		String newRoot = "test/data/commentU/root10";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_commentU2.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_commentU2.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentU2.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	// これ以下のテスト用のデータは未公開
	@Test
	public void countCobol() throws Exception {
		System.out.println("## StepCount ## countCobol ##");
		String newRoot = "test/data/cobol/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_cobol.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/count_cobol.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_cobol.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countOW() throws Exception {
		System.out.println("## StepCount ## countOW ##");
		String newRoot = "test/data/ow/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_ow.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_ow.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_ow.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

}
