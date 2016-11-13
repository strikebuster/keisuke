package keisuke;

import java.io.FileNotFoundException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.*;


public class CountProcTest {
	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testCountProc_arg01() throws Exception {
		//String newRoot = "test/sample/java/root1";
		System.out.println("## testCountProc_arg01 ##");
		URL expected = this.getClass().getResource("CountTest_nop.txt");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-?", "xxx"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.argMap);
	}
	
	@Test
	public void testCountProc_arg02() throws Exception {
		System.out.println("## testCountProc_arg02 ##");
		URL expected = this.getClass().getResource("CountTest_arg02.txt");
		CountMainProc cproc = new CountMainProc();
		String args[] = {"test/data/dummy.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.argMap);
	}
	
	@Test
	public void testCountProc_arg03() throws Exception {
		System.out.println("## testCountProc_arg03 ##");
		URL expected = this.getClass().getResource("CountTest_arg03.txt");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-prop", "test/data/ktest.properties", "test/data/dummy.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.argMap);
	}
	
	
	@Rule
    public ExpectedException thrownEx = ExpectedException.none();
	
	@Test
	public void testCountProc_error01() throws Exception {
		System.out.println("## testCountProc_error01 ##");
		//URL expected = this.getClass().getResource("CountTest_nop.txt");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		CountMainProc cproc = new CountMainProc();
		String args[] = {"test/data/nofile.csv"};
		cproc.main(args);
		//TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_error02() throws Exception {
		System.out.println("## testCountProc_error02 ##");
		//URL expected = this.getClass().getResource("CountTest_count01.csv");	
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-prop", "test/data/nofile.properties", "test/data/count01.csv"};
		cproc.main(args);
		//TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_error03() throws Exception {
		System.out.println("## testCountProc_error03 ##");
		//URL expected = this.getClass().getResource("CountTest_error03.csv");
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage("FileNotFoundException");
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "language", "-xml", "test/data/nofile.xml", "test/data/count01.csv"};
		cproc.main(args);
		//TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_error04() throws Exception {
		System.out.println("## testCountProc_error04 ##");
		URL expected = this.getClass().getResource("CountTest_count05.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "language", "-xml", "test/data/ktestf.xml", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_error05() throws Exception {
		System.out.println("## testCountProc_error05 ##");
		URL expected = this.getClass().getResource("CountTest_error05.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "fw:ow", "-xml", "test/data/ktestl.xml", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_error06() throws Exception {
		System.out.println("## testCountProc_error06 ##");
		URL expected = this.getClass().getResource("CountTest_error05.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "fw:spring", "-xml", "test/data/ktestf.xml", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count01() throws Exception {
		System.out.println("## testCountProc_count01 ##");
		URL expected = this.getClass().getResource("CountTest_count01.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count02() throws Exception {
		System.out.println("## testCountProc_count02 ##");
		URL expected = this.getClass().getResource("CountTest_count02.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"test/data/dummy.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count03() throws Exception {
		System.out.println("## testCountProc_count03 ##");
		URL expected = this.getClass().getResource("CountTest_count03.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-prop", "test/data/ktest.properties", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count04() throws Exception {
		System.out.println("## testCountProc_count04 ##");
		URL expected = this.getClass().getResource("CountTest_count01.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "xxx", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count05() throws Exception {
		System.out.println("## testCountProc_count05 ##");
		URL expected = this.getClass().getResource("CountTest_count05.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "language", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count06() throws Exception {
		System.out.println("## testCountProc_count06 ##");
		URL expected = this.getClass().getResource("CountTest_count06.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"test/data/count01.csv", "-class", "group"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count07() throws Exception {
		System.out.println("## testCountProc_count07 ##");
		URL expected = this.getClass().getResource("CountTest_count07.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "language", "-xml", "test/data/ktestl.xml", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count08() throws Exception {
		System.out.println("## testCountProc_count08 ##");
		URL expected = this.getClass().getResource("CountTest_count08.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "group", "test/data/count01.csv", "-xml", "test/data/ktestl.xml"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count20() throws Exception {
		System.out.println("## testCountProc_count20 ##");
		URL expected = this.getClass().getResource("CountTest_count20.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "fw:ow", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count21() throws Exception {
		System.out.println("## testCountProc_count21 ##");
		URL expected = this.getClass().getResource("CountTest_count21.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "fw:ow", "-xml", "test/data/ktestf.xml", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	
	@Test
	public void testCountProc_count22() throws Exception {
		System.out.println("## testCountProc_count22 ##");
		URL expected = this.getClass().getResource("CountTest_count22.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "fw:o3w", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
	@Test
	public void testCountProc_count23() throws Exception {
		System.out.println("## testCountProc_count23 ##");
		URL expected = this.getClass().getResource("CountTest_count23.csv");	
		CountMainProc cproc = new CountMainProc();
		String args[] = {"-class", "fw:spring", "test/data/count01.csv"};
		cproc.main(args);
		TestUtil.assertEquals(expected, cproc.reportOutput);
	}
	
}
