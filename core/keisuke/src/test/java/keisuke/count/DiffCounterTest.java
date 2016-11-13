package keisuke.count;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import jp.sf.amateras.stepcounter.Util;
import keisuke.TestUtil;
import keisuke.count.diff.DiffCounter;
import keisuke.count.diff.DiffFolderResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DiffCounterTest {

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testDiff_arg01() throws Exception {
		System.out.println("## diffCount_arg01 ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"dummy.txt");

		String args[] = {"--help", "xxx"};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		TestUtil.assertEquals(url, diffcount.argMap);
	}
	
	@Test
	public void testDiff_arg02() throws Exception {
		System.out.println("## diffCount_arg02 ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"dummy.txt");

		String args[] = {"-zzz", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		TestUtil.assertEquals(url, diffcount.argMap);
	}
	
	@Test
	public void testDiff_arg03() throws Exception {
		System.out.println("## diffCount_arg03 ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"diffCount_arg03.txt");

		String args[] = {"-e", "EUC-JP", "-f", "html", "-x", "test/data/ktestl2.xml",
				"-o", "test/out/diff_arg03.txt", newRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		TestUtil.assertEquals(url, diffcount.argMap);
	}
	
	
	@Test
	public void testDiff_java() throws Exception {
		System.out.println("## diffCount_java ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_java.txt");
		
		String args[] = {"-e", "UTF-8", "-f", "text", 
				"-o", "test/out/diff_java.txt", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		File actual = new File("test/out/diff_java.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	
	@Test
	public void testDiff_java_en() throws Exception {
		System.out.println("## diffCount_java_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_java_en.txt");

		String args[] = {"-e", "UTF-8", "-f", "text", 
				"-o", "test/out/diff_java_en.txt", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/diff_java_en.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_java_html() throws Exception {
		System.out.println("## diffCount_java_html ##");	
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"diffCount_java.html");

		String args[] = {"-e", "UTF-8", "-f", "html", 
				"-o", "test/out/diff_java.html", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		File actual = new File("test/out/diff_java.html");
		SCTestUtil.assertEqualHtmls(url, actual, 41);
	}
	
	@Test
	public void testDiff_java_html_en() throws Exception {
		System.out.println("## diffCount_java_html_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"diffCount_java_en.html");

		String args[] = {"-e", "UTF-8", "-f", "html", 
				"-o", "test/out/diff_java_en.html", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/diff_java_en.html");
		SCTestUtil.assertEqualHtmls(url, actual, 41);
	}

	@Test
	public void testDiff_java_excel() throws Exception {
		System.out.println("## diffCount_java_excel ##");		
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"diffCount_java.xls");

		String args[] = {"-e", "UTF-8", "-f", "excel", 
				"-o", "test/out/diff_java.xls", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		File actual = new File("test/out/diff_java.xls");
		//TestUtil.assertEquals(url, actual);
	}
	
	@Test
	public void testDiff_java_excel_en() throws Exception {
		System.out.println("## diffCount_java_excel_en ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root2";

		URL url = this.getClass().getResource(
				"diffCount_java_en.xls");

		String args[] = {"-e", "UTF-8", "-f", "excel", 
				"-o", "test/out/diff_java_en.xls", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		Locale.setDefault(org);
		File actual = new File("test/out/diff_java_en.xls");
		//TestUtil.assertEquals(url, actual);
	}
	
	
	@Test
	public void testDiff_java2() throws Exception {
		System.out.println("## diffCount_java ##");
		String oldRoot = "test/data/java/root1";
		String newRoot = "test/data/java/root3";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_java2.txt");

		String args[] = {"-e", "UTF-8", "-f", "text", 
				"-o", "test/out/diff_java2.txt", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		File actual = new File("test/out/diff_java2.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_webUTF() throws Exception {
		System.out.println("## diffCount_webUTF ##");
		String oldRoot = "test/data/web/root1";
		String newRoot = "test/data/web/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_web.txt");

		String args[] = {"--encoding", "UTF-8",
				"--output", "test/out/diff_web.txt", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		File actual = new File("test/out/diff_web.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_webSJIS() throws Exception {
		System.out.println("## diffCount_webSJIS ##");
		String oldRoot = "test/data/web/root1S";
		String newRoot = "test/data/web/root2S";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_webS.txt");

		String args[] = {"-encoding", "Windows-31J",
				"-output", "test/out/diff_webS.txt", newRoot, oldRoot};
		DiffCount diffcount = new DiffCount();
		diffcount.diffProc(args);
		
		File actual = new File("test/out/diff_webS.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_c() throws Exception {
		System.out.println("## diffCount_c ##");
		String oldRoot = "test/data/c/root1";
		String newRoot = "test/data/c/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_c.txt");

		String args[] = {"-encoding", "Shift_JIS",
				"-output", "test/out/diff_c.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_c.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}

	@Test
	public void testDiff_cobol() throws Exception {
		System.out.println("## diffCount_cobol ##");
		String oldRoot = "test/data/cobol/root1";
		String newRoot = "test/data/cobol/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_cobol.txt");

		String args[] = {"-encoding", "Shift_JIS",
				"-output", "test/out/diff_cobol.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_cobol.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_commentSJIS() throws Exception {
		System.out.println("## diffCount_commentSJIS ##");
		String oldRoot = "test/data/commentS/root1";
		String newRoot = "test/data/commentS/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_commentS.txt");

		String args[] = {"-encoding", "Windows-31J",
				"-output", "test/out/diff_commentS.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_commentS.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_commentSJIS2() throws Exception {
		System.out.println("## diffCount_commentSJIS2 ##");
		String oldRoot = "test/data/commentS/root10";
		String newRoot = "test/data/commentS/root20";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_commentS2.txt");

		String args[] = {"-encoding", "Windows-31J",
				"-output", "test/out/diff_commentS2.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_commentS2.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_commentUTF() throws Exception {
		System.out.println("## diffCount_commentUTF ##");
		String oldRoot = "test/data/commentU/root1";
		String newRoot = "test/data/commentU/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_commentU.txt");

		String args[] = {"-encoding", "UTF-8",
				"-output", "test/out/diff_commentU.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_commentU.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	

	@Test
	public void testDiff_commentUTF2() throws Exception {
		System.out.println("## diffCount_commentUTF2 ##");
		String oldRoot = "test/data/commentU/root10";
		String newRoot = "test/data/commentU/root20";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_commentU2.txt");

		String args[] = {"-encoding", "UTF-8",
				"-output", "test/out/diff_commentU2.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_commentU2.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}

	@Test
	public void testDiff_commentOW() throws Exception {
		System.out.println("## diffCount_OW ##");
		String oldRoot = "test/data/ow/root1";
		String newRoot = "test/data/ow/root2";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_ow.txt");

		String args[] = {"-encoding", "UTF-8",
				"-output", "test/out/diff_ow.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_ow.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
	@Test
	public void testDiff_commentOW2() throws Exception {
		System.out.println("## diffCount_OW2 ##");
		String oldRoot = "test/data/ow/root1";
		String newRoot = "test/data/ow/root3";

		URL url = this.getClass().getResource(
				"DiffCounterTest_testCount_ow2.txt");

		String args[] = {"-encoding", "UTF-8",
				"-output", "test/out/diff_ow2.txt", newRoot, oldRoot};
		DiffCount.main(args);
		
		File actual = new File("test/out/diff_ow2.txt");
		SCTestUtil.assertEquals(url, actual, 2);
	}
	
}
