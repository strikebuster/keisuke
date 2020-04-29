package keisuke.count.step;

import static keisuke.count.CountTestUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import keisuke.count.StepCount;


/**
 * Test class of StepCount using customized xml rules.
 */
public class StepCountUsingCustomRuleTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void countSomeUsingCustomRuleWhichDoesNotExist() throws Exception {
		System.out.println("## CustomRule ## countSomeUsingCustomRuleWhichDoesNotExist ##");
		String expected = "FileNotFoundException";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);
		String newRoot = "test/data/java/root1";

		String[] args = {"-showDirectory", "-format", "csv",
				"-output", "test/out/rule_count_error.csv",
				"-xml", "test/data/ktestl99.xml", newRoot};
		StepCount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void countJavaUsingCustomRuleAndCsvFormatAndBasePath() throws Exception {
		System.out.println("## CustomRule ## countJavaUsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_rule_java_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_java_basePath.csv");

		String[] args = {"-path", "base", "-format", "csv",
				"-output", outFileName,
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInUtfUsingCustomRuleAndCsvFormatAndSubPath() throws Exception {
		System.out.println("## CustomRule ## countWebCodingInUtfUsingCustomRuleAndCsvFormatAndSubPath ##");
		String newRoot = "test/data/web/root1";
		String outFileName = "test/out/count_rule_web_subPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_web_subPath.csv");

		String[] args = {"-p", "sub", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName,
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInSjisUsingCustomRuleAndCsvFormatAndBasePath() throws Exception {
		System.out.println("## CustomRule ## countWebCodingInSjisUsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/web/root1S";
		String outFileName = "test/out/count_rule_webS_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_webS_basePath.csv");

		String[] args = {"--path", "base", "-format", "csv", "-encoding", "Windows-31J",
				"-output", outFileName,
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countCUsingCustomRuleAndCsvFormatAndBasePath() throws Exception {
		System.out.println("## CustomRule ## countCUsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/c/root1";
		String outFileName = "test/out/count_rule_c_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_c_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", outFileName,
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRuleAndCsvFormatAndBasePath()
			throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInSjis"
				+ "UsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentS/root1";
		String outFileName = "test/out/count_rule_commentS_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_commentS_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjis2UsingCustomRuleAndCsvFormatAndBasePath()
			throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInSjis(2)"
				+ "UsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentS/root10";
		String outFileName = "test/out/count_rule_commentS2_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_commentS2_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRuleAndCsvFormatAndBasePath()
			throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInUtf"
				+ "UsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentU/root1";
		String outFileName = "test/out/count_rule_commentU_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_commentU_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInUtf2UsingCustomRuleAndCsvFormatAndBasePath()
			throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInUtf(2)"
				+ "UsingCustomRuleAndCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentU/root10";
		String outFileName = "test/out/count_rule_commrntU2_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_commentU2_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	// このテスト用データは未公開
	@Test
	public void countOWUsingCustomRuleAndCsvFormatAndSubPath() throws Exception {
		System.out.println("## CustomRule ## countOWUsingCustomRuleAndCsvFormatAndSubPath ##");
		String newRoot = "test/data/ow/root1";
		String outFileName = "test/out/count_rule_ow_subPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_ow_subPath.csv");

		String[] args = {"-p", "sub", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

}
