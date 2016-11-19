package keisuke;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DiffProcTest {
	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testDiffProc_arg01() throws Exception {
		//String newRoot = "test/sample/java/root1";
		System.out.println("## testDiffProc_arg01 ##");
		URL expected = this.getClass().getResource("DiffTest_arg01.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-?", "xxx"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.argMap);
	}
	
	@Test
	public void testDiffProc_arg02() throws Exception {
		System.out.println("## testDiffProc_arg02 ##");
		URL expected = this.getClass().getResource("DiffTest_arg02.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-aout", "test/out/add02a.txt", "-mout", "test/out/modify02a.txt", "test/data/dummy.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.argMap);
	}
	
	@Test
	public void testDiffProc_arg03() throws Exception {
		System.out.println("## testDiffProc_arg03 ##");
		URL expected = this.getClass().getResource("DiffTest_arg03.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-prop", "test/data/ktest.properties", "test/data/dummy.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.argMap);
	}
	
	@Test
	public void testDiffProc_arg04() throws Exception {
		System.out.println("## testDiffProc_arg04 ##");
		URL expected = this.getClass().getResource("DiffTest_arg04.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/dummy.txt", "-unchange"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.argMap);
	}
	
	@Test
	public void testDiffProc_arg05() throws Exception {
		System.out.println("## testDiffProc_arg05 ##");
		URL expected = this.getClass().getResource("DiffTest_arg05.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-unchange", "xxx", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.argMap);
	}
			
	@Test
	public void testDiffProc_arg06() throws Exception {
		System.out.println("## testDiffProc_arg06 ##");
		URL expected = this.getClass().getResource("DiffTest_arg06.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-unchange", "total", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	
	@Rule
    public ExpectedException thrownEx = ExpectedException.none();
	
	@Test
	public void testDiffProc_error01() throws Exception {
		System.out.println("## testDiffProc_error01 ##");
		//URL expected = this.getClass().getResource("DiffTest_diff01.csv");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/nofile.txt"};
		dproc.main(args);
		//TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_error02() throws Exception {
		System.out.println("## testDiffProc_error02 ##");
		//URL expected = this.getClass().getResource("DiffTest_error02.csv");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt", "-class", "extension", "-prop", "test/data/nofile.properties"};
		dproc.main(args);
		//TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_error03() throws Exception {
		System.out.println("## testDiffProc_error03 ##");
		//URL expected = this.getClass().getResource("DiffTest_error03.csv");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt", "-class", "language", "-xml", "test/data/nofile.xml"};
		dproc.main(args);
		//TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_error04() throws Exception {
		System.out.println("## testDiffProc_error04 ##");
		URL expected = this.getClass().getResource("DiffTest_diff07.csv");
		//thrownEx.expect(RuntimeException.class);
		//thrownEx.expectMessage("FileNotFoundException");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt", "-class", "group", "-xml", "test/data/ktestf.xml"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_error05() throws Exception {
		System.out.println("## testDiffProc_error05 ##");
		//URL expected = this.getClass().getResource("DiffTest_diff20.csv");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "fw:struts", "-xml", "test/data/nofile.xml", "test/data/diff01.txt"};
		dproc.main(args);
		//TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_error06() throws Exception {
		System.out.println("## testDiffProc_error06 ##");
		URL expected = this.getClass().getResource("DiffTest_error06.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "fw:rails", "-xml", "test/data/ktestl.xml", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_error07() throws Exception {
		System.out.println("## testDiffProc_error07 ##");
		URL expected = this.getClass().getResource("DiffTest_error06.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "fw:spring", "-xml", "test/data/ktestf.xml", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff01() throws Exception {
		System.out.println("## testDiffProc_diff01 ##");
		URL expected = this.getClass().getResource("DiffTest_diff01.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff02() throws Exception {
		System.out.println("## testDiffProc_diff02 ##");
		URL expected = this.getClass().getResource("DiffTest_diff02.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/dummy.csv"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff03() throws Exception {
		System.out.println("## testDiffProc_diff03 ##");
		URL expected = this.getClass().getResource("DiffTest_diff03.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-aout", "test/out/add03.txt", "test/data/diff01.txt"};
		dproc.main(args);
		File actual = new File("test/out/add03.txt");
		TestUtil.assertEquals(expected, actual);
	}
	
	@Test
	public void testDiffProc_diff04() throws Exception {
		System.out.println("## testDiffProc_fiff04 ##");
		URL expected = this.getClass().getResource("DiffTest_diff04.txt");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-aout", "test/out/add04.txt", "-mout", "test/out/modify04.txt", "test/data/diff01.txt"};
		dproc.main(args);
		File actual = new File("test/out/modify04.txt");
		TestUtil.assertEquals(expected, actual);
	}
	
	@Test
	public void testDiffProc_diff05() throws Exception {
		System.out.println("## testDiffProc_diff05 ##");
		URL expected = this.getClass().getResource("DiffTest_diff05.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt", "-class", "xxx", "-prop", "test/data/ktest.properties"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff06() throws Exception {
		System.out.println("## testDiffProc_diff06 ##");
		URL expected = this.getClass().getResource("DiffTest_diff06.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt", "-class", "language"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff07() throws Exception {
		System.out.println("## testDiffProc_diff07 ##");
		URL expected = this.getClass().getResource("DiffTest_diff07.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "group", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff08() throws Exception {
		System.out.println("## testDiffProc_diff08 ##");
		URL expected = this.getClass().getResource("DiffTest_diff08.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"test/data/diff01.txt", "-xml", "test/data/ktestl.xml", "-class", "language"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff09() throws Exception {
		System.out.println("## testDiffProc_diff09 ##");
		URL expected = this.getClass().getResource("DiffTest_diff09.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-xml", "test/data/ktestl.xml", "-class", "group", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff20() throws Exception {
		System.out.println("## testDiffProc_diff20 ##");
		URL expected = this.getClass().getResource("DiffTest_diff20.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "fw:ow", "-xml", "test/data/ktestf2.xml", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff21() throws Exception {
		System.out.println("## testDiffProc_diff21 ##");
		URL expected = this.getClass().getResource("DiffTest_diff21.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-xml", "test/data/ktestf.xml", "-class", "fw:ow", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff22() throws Exception {
		System.out.println("## testDiffProc_diff22 ##");
		URL expected = this.getClass().getResource("DiffTest_diff22.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "fw:o3w", "-xml", "test/data/ktestf2.xml", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
	@Test
	public void testDiffProc_diff23() throws Exception {
		System.out.println("## testDiffProc_diff23 ##");
		URL expected = this.getClass().getResource("DiffTest_diff23.csv");
		DiffMainProc dproc = new DiffMainProc();
		String args[] = {"-class", "fw:struts", "test/data/diff01.txt"};
		dproc.main(args);
		TestUtil.assertEquals(expected, dproc.reportOutput);
	}
	
}
