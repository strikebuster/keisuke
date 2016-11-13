package keisuke.count;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.TestUtil;
import keisuke.count.StepCount;

public class StepCounterTest {
	
	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testCount_arg01() throws Exception {
		System.out.println("## testCount_arg01 ##");
		URL url = this.getClass().getResource("empty.txt");
		
		String args[] = {"-?", "xxx"};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		TestUtil.assertEquals(url, stepcount.argMap);
	}
	
	@Test
	public void testCount_arg02() throws Exception {
		System.out.println("## testCount_arg02 ##");
		URL url = this.getClass().getResource("empty.txt");
		
		String args[] = {"-zzz", "-s", "-e", "EUC-JP"};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		TestUtil.assertEquals(url, stepcount.argMap);
	}
	
	@Test
	public void testCount_arg03() throws Exception {
		System.out.println("## testCount_arg03 ##");
		URL url = this.getClass().getResource("testCount_arg03.txt");
		
		String args[] = {"-s", "-e", "EUC-JP", "-f", "xml", "-x", "test/data/ktestl2.xml"};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);

		TestUtil.assertEquals(url, stepcount.argMap);
	}
	
	@Test
	public void testCount_java() throws Exception {
		System.out.println("## testCount_java ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		File actual = new File("test/out/count_java.txt");
		TestUtil.assertEquals(url, actual);
	}

	@Test
	public void testCount_java_en() throws Exception {
		System.out.println("## testCount_java_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.txt");

		String args[] = {"-s", "-f", "csv", "-e", "UTF-8",
				"-o", "test/out/count_java_en.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_text() throws Exception {
		System.out.println("## testCount_java_text ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java_text.txt");

		String args[] = {"-showDirectory", "-encoding", "UTF-8",
				"-output", "test/out/count_java_text.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		File actual = new File("test/out/count_java_text.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_text_en() throws Exception {
		System.out.println("## testCount_java_text_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java_text_en.txt");

		String args[] = {"--showDirectory", "--encoding", "UTF-8",
				"--output", "test/out/count_java_text_en.txt", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/count_java_text_en.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_json() throws Exception {
		System.out.println("## testCount_java_json ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java.json");

		String args[] = {"-showDirectory", "-format", "json", "-encoding", "UTF-8",
				"-output", "test/out/count_java.json", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		File actual = new File("test/out/count_java.json");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_json_en() throws Exception {
		System.out.println("## testCount_java_json_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.json");

		String args[] = {"-s", "--format", "json", "-encoding", "UTF-8",
				"--output", "test/out/count_java_en.json", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.json");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_xml() throws Exception {
		System.out.println("## testCount_java_xml ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java.xml");

		String args[] = {"-showDirectory", "-format", "xml", "-encoding", "UTF-8",
				"-output", "test/out/count_java.xml", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		File actual = new File("test/out/count_java.xml");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_xml_en() throws Exception {
		System.out.println("## testCount_java_xml_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.xml");

		String args[] = {"-showDirectory", "-format", "xml", "-encoding", "UTF-8",
				"-output", "test/out/count_java_en.xml", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.xml");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_java_excel() throws Exception {
		System.out.println("## testCount_java_excel ##");
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java.xls");

		String args[] = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", "test/out/count_java.xls", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		File actual = new File("test/out/count_java.xls");
		//TestUtil.assertEquals(url, actual);
	}

	@Test
	public void testCount_java_excel_en() throws Exception {
		System.out.println("## testCount_java_excel_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String newRoot = "test/data/java/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_java_en.xls");

		String args[] = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", "test/out/count_java_en.xls", newRoot};
		StepCount stepcount = new StepCount();
		stepcount.countProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/count_java_en.xls");
		//TestUtil.assertEquals(url, actual);
	}

	@Test
	public void testCount_webUTF() throws Exception {
		System.out.println("## testCount_webUTF ##");
		String newRoot = "test/data/web/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_web.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_web.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_web.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_webSJIS() throws Exception {
		System.out.println("## testCount_webSJIS ##");
		String newRoot = "test/data/web/root1S";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_webS.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_webS.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_webS.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_c() throws Exception {
		System.out.println("## testCount_c ##");
		String newRoot = "test/data/c/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_c.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/count_c.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_c.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_cobol() throws Exception {
		System.out.println("## testCount_cobol ##");
		String newRoot = "test/data/cobol/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_cobol.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/count_cobol.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_cobol.txt");
		TestUtil.assertEquals(url, actual);
	}
	

	@Test
	public void testCount_commentSJIS() throws Exception {
		System.out.println("## testCount_commentSJIS ##");
		String newRoot = "test/data/commentS/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_commentS.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_commentS.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_commentS.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_commentSJIS2() throws Exception {
		System.out.println("## testCount_SJIS2 ##");
		String newRoot = "test/data/commentS/root10";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_commentS2.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_commentS2.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_commentS2.txt");
		TestUtil.assertEquals(url, actual);
	}
		
	@Test
	public void testCount_commentUTF() throws Exception {
		System.out.println("## testCount_commentUTF ##");
		String newRoot = "test/data/commentU/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_commentU.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_commentU.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_commentU.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_commentUTF2() throws Exception {
		System.out.println("## testCount_commentUTF2 ##");
		String newRoot = "test/data/commentU/root10";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_commentU2.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_commentU2.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_commentU2.txt");
		TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testCount_OW() throws Exception {
		System.out.println("## testCount_OW ##");
		String newRoot = "test/data/ow/root1";

		URL url = this.getClass().getResource(
				"StepCounterTest_testCount_ow.txt");

		String args[] = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_ow.txt", newRoot};
		StepCount.main(args);
		
		File actual = new File("test/out/count_ow.txt");
		TestUtil.assertEquals(url, actual);
	}
	
}
