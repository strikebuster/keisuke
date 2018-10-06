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
 * Test class of StepCounter about customized xml rules.
 *
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
	public void countJavaUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countJavaUsingCustomRule ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_java.csv");

		String[] args = {"-showDirectory", "-format", "csv",
				"-output", "test/out/rule_count_java.csv",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_java.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInUtfUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countWebCodingInUtfUsingCustomRule ##");
		String newRoot = "test/data/web/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_web.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/rule_count_web.csv",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_web.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInSjisUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countWebCodingInSjisUsingCustomRule ##");
		String newRoot = "test/data/web/root1S";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_webS.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/rule_count_webS.csv",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_webS.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countCUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countCUsingCustomRule ##");
		String newRoot = "test/data/c/root1";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_c.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/rule_count_c.csv",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_c.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule ##");
		String newRoot = "test/data/commentS/root1";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_commentS.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentS.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentS.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule2() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule(2) ##");
		String newRoot = "test/data/commentS/root10";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_commentS2.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentS2.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentS2.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule ##");
		String newRoot = "test/data/commentU/root1";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_commentU.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentU.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentU.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule2() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule(2) ##");
		String newRoot = "test/data/commentU/root10";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_commentU2.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentU2.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentU2.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	// このテスト用データは未公開
	@Test
	public void countOWUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countOWUsingCustomRule ##");
		String newRoot = "test/data/ow/root1";
		URL expected = this.getClass()
				.getResource("RuleCounterTest_testCount_ow.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_ow.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_ow.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

}
