package keisuke.count;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.sf.amateras.stepcounter.Util;
import keisuke.TestUtil;

public class CustomRuleCounterTest {

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testRule_error() throws Exception {
		System.out.println("## testRule_error ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"dummy.txt");

		String args[] = {"-showDirectory", "-format", "csv", 
				"-output", "test/out/rule_count_error.txt", 
				"-xml", "test/data/ktestl99.xml", newRoot};
		StepCount.main(args);
		File actual = new File("test/out/rule_count_error.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_java() throws Exception {
		System.out.println("## testRule_java ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_java.txt");

		String args[] = {"-showDirectory", "-format", "csv", 
				"-output", "test/out/rule_count_java.txt", 
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);
		File actual = new File("test/out/rule_count_java.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_webUTF() throws Exception {
		System.out.println("## testRule_webUTF ##");
		String newRoot = "test/data/web/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_web.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/rule_count_web.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_web.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_webSJIS() throws Exception {
		System.out.println("## testRule_webSJIS ##");
		String newRoot = "test/data/web/root1S";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_webS.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/rule_count_webS.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_webS.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_c() throws Exception {
		System.out.println("## testRule_c ##");
		String newRoot = "test/data/c/root1";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_c.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/rule_count_c.txt",
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_c.txt");
		TestUtil.assertEquals(url, actual);
	}

	@Test
	public void testRule_commentSJIS() throws Exception {
		System.out.println("## testRule_commentSJIS ##");
		String newRoot = "test/data/commentS/root1";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_commentS.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentS.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_commentS.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_commentSJIS2() throws Exception {
		System.out.println("## testRule_commentSJIS2 ##");
		String newRoot = "test/data/commentS/root10";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_commentS2.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentS2.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_commentS2.txt");
		TestUtil.assertEquals(url, actual);
	}
		
	@Test
	public void testRule_commentUTF() throws Exception {
		System.out.println("## testRule_commentUTF ##");
		String newRoot = "test/data/commentU/root1";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_commentU.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentU.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_commentU.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_commentUTF2() throws Exception {
		System.out.println("## testRule_commentUTF2 ##");
		String newRoot = "test/data/commentU/root10";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_commentU2.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_commentU2.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_commentU2.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testRule_OW() throws Exception {
		System.out.println("## testRule_OW ##");
		String newRoot = "test/data/ow/root1";

		URL url = this.getClass().getResource(
				"RuleCounterTest_testCount_ow.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-xml", "test/data/ktestl2.xml",
				"-output", "test/out/rule_count_ow.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/rule_count_ow.txt");
		TestUtil.assertEquals(url, actual);
	}
	
}
