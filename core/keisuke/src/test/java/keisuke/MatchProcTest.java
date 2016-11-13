package keisuke;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MatchProcTest {
	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testMatchProc_arg01() throws Exception {
		System.out.println("## testMatchProc_arg01 ##");
		URL expected = this.getClass().getResource("MatchTest_arg01.txt");	
		MatchMainProc mproc = new MatchMainProc();
		String args[] = {"-?", "xxx"};
		mproc.main(args);
		TestUtil.assertEquals(expected, mproc.argMap);
	}
	
	@Test
	public void testMatchProc_arg02() throws Exception {
		System.out.println("## testMatchProc_arg02 ##");
		URL expected = this.getClass().getResource("MatchTest_arg02.txt");
		MatchMainProc mproc = new MatchMainProc();
		String args[] = {"test/data/dummy.csv"};
		mproc.main(args);
		TestUtil.assertEquals(expected, mproc.argMap);
	}
	
	@Test
	public void testMatchProc_match01() throws Exception {
		System.out.println("## testMatchProc_match01 ##");
		URL expected = this.getClass().getResource("MatchTest_match01.csv");
		MatchMainProc mproc = new MatchMainProc();
		String args[] = {"test/data/match_ma01.csv", "test/data/match_tr01.txt"};
		mproc.main(args);
		TestUtil.assertEquals(expected, mproc.reportOutput);
	}
	
	@Test
	public void testMatchProc_match02() throws Exception {
		System.out.println("## testMatchProc_match02 ##");
		URL expected = this.getClass().getResource("MatchTest_match02.csv");
		MatchMainProc mproc = new MatchMainProc();
		String args[] = {"test/data/match_ma01_sorted.csv", "test/data/match_tr01_sorted.txt"};
		mproc.main(args);
		TestUtil.assertEquals(expected, mproc.reportOutput);
	}
	
	@Test
	public void testMatchProc_match03() throws Exception {
		System.out.println("## testMatchProc_match03 ##");
		URL expected = this.getClass().getResource("MatchTest_match03.csv");
		MatchMainProc mproc = new MatchMainProc();
		String args[] = {"test/data/match_ma03_sorted.csv", "test/data/match_tr03_sorted.txt", "test/out/match03.csv"};
		mproc.main(args);
		File actual = new File("test/out/match03.csv");
		TestUtil.assertEquals(expected, actual);
	}
	
}
