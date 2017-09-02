package keisuke.count;

import static keisuke.count.SCTestUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.util.StderrCapture;

/**
 * Test class of StepCounter about customized xml rules.
 * @author strikebuster
 *
 */
public class CustomRuleCounterTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void countSomeUsingCustomRuleWhichDoesNotExist() throws Exception {
		System.out.println("## CustomRule ## countSomeUsingCustomRuleWhichDoesNotExist ##");

		StderrCapture capture = new StderrCapture();

		String newRoot = "test/data/java/root1";
		//URL expected = this.getClass().getResource(
		//		"dummy.txt");

		String[] args = {"-showDirectory", "-format", "csv",
				"-output", "test/out/rule_count_error.txt",
				"-xml", "test/data/ktestl99.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_error.txt");
		//assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
		assertThat(contentOf(actual), isEmptyString());

		String errMessage = capture.getCapturedString();
		capture.finish();
		assertThat(errMessage, containsString("Read error"));
	}

	@Test
	public void countJavaUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countJavaUsingCustomRule ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_java.txt");

		String[] args = {"-showDirectory", "-format", "csv",
				"-output", "test/out/rule_count_java.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_java.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countWebCodingInUtfUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countWebCodingInUtfUsingCustomRule ##");
		String newRoot = "test/data/web/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_web.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/rule_count_web.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_web.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countWebCodingInSjisUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countWebCodingInSjisUsingCustomRule ##");
		String newRoot = "test/data/web/root1S";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_webS.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/rule_count_webS.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_webS.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countCUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countCUsingCustomRule ##");
		String newRoot = "test/data/c/root1";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_c.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/rule_count_c.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_c.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule ##");
		String newRoot = "test/data/commentS/root1";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_commentS.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentS.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentS.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule2() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInSjisUsingCustomRule(2) ##");
		String newRoot = "test/data/commentS/root10";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_commentS2.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentS2.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentS2.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule ##");
		String newRoot = "test/data/commentU/root1";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_commentU.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentU.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentU.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule2() throws Exception {
		System.out.println("## CustomRule ## countMiscSourcesIncludingMiscCommentsInUtfUsingCustomRule(2) ##");
		String newRoot = "test/data/commentU/root10";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_commentU2.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentU2.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_commentU2.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

	// このテスト用データは未公開
	@Test
	public void countOWUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## countOWUsingCustomRule ##");
		String newRoot = "test/data/ow/root1";
		URL expected = this.getClass().getResource(
				"RuleCounterTest_testCount_ow.txt");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_ow.txt", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/rule_count_ow.txt");
		assertThat(contentOf(actual), is(equalTo(contentOf(expected))));
	}

}
