package keisuke.count;

import static keisuke.count.SCTestConstant.TEXT_IGNORE_LINES;
import static keisuke.count.SCTestUtil.withoutHeadLines;
import static keisuke.util.TestUtil.contentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class of DiffCounter about customized xml rules.
 *
 */
public class DiffCountUsingCustomRuleTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void diffSomeUsingCustomRuleWhichDoesNotExist() throws Exception {
		System.out.println("## CustomRule ## diffSomeUsingCustomRuleWhichDoesNotExist ##");
		String expected = "FileNotFoundException";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		String[] args = {"-encoding", "UTF-8", "-format", "text",
				"-output", "test/out/rule_diff_error.txt",
				"-xml", "test/data/ktestl99.xml", newRoot, oldRoot};
		DiffCount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void diffJavaUsingCustomRule() throws Exception {
		System.out.println("## CustomRule ## diffJavaUsingCustomRule ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";
		URL expected = this.getClass()
				.getResource("RuleDiffCounterTest_testCount_java.txt");

		String[] args = {"-encoding", "UTF-8", "-format", "text",
				"-output", "test/out/rule_diff_java.txt",
				"-xml", "test/data/ktestl2.xml", newRoot, oldRoot};
		DiffCount.main(args);

		File actual = new File("test/out/rule_diff_java.txt");
		assertThat(contentOf(actual, withoutHeadLines(TEXT_IGNORE_LINES)), is(equalTo(contentOf(expected))));
	}
}
