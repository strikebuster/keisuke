package keisuke.count.step;

import static keisuke.count.CountTestUtil.binaryContentOf;
//import static keisuke.count.CountTestUtil.excelContentOf;
import static keisuke.count.CountTestUtil.rawContentOf;
import static keisuke.count.CountTestUtil.textContentOf;
import static keisuke.count.option.CountOptionConstant.OPT_SHOWDIR;
import static keisuke.count.option.CountOptionConstant.OPT_SORT;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import keisuke.count.StepCount;

/**
 * Test class of StepCounter features compatible with ver.1.x.
 */
public class StepCountCompatibleWithV1Test {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void countJavaUsingCsvFormat() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingCsvFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("stepCount_java_showDir.csv");
		String outFileName = "test/out/count_java_showDir.csv";

		String[] args = {"--" + OPT_SHOWDIR, "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOff() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingCsvFormatAndSortOff ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_showDir_noSort.csv";
		// ソートoffでも引数が１つなら文字列順ソートになるのでデフォルトと同じ

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"--" + OPT_SORT, "off", "-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("stepCount_java_showDir_noSort_win.csv");
		} else {
			expected = this.getClass().getResource("stepCount_java_showDir.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingCsvFormatWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_files_showDir.csv";
		// ディレクトリ付きパス名で文字列ソート順になる
		// ファイルが引数のときはディレクトリパスは編集しない。
		URL expected = this.getClass()
				.getResource("stepCount_java_files_path.csv");

		String[] args = {"-s", "-format", "csv", "-encoding", "UTF-8", "-output", outFileName,
				newRoot + "/test/Utils.java", newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingCsvFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_showDir_en.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_showDir_en.csv");

		String[] args = {"-s", "-f", "csv", "-e", "UTF-8", "-o", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	/*
	@Test
	public void countJavaUsingExcelFormat() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingExcelFormat ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_showDir.xlsx";
		URL expected = this.getClass()
				.getResource("stepCount_java_showDir.xlsx");

		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		//assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
		assertThat(excelContentOf(actual), is(equalTo(excelContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcelFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingExcelFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_showDir_en.xlsx";
		URL expected = this.getClass()
				.getResource("stepCount_java_showDir_en.xlsx");

		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		//assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
		assertThat(excelContentOf(actual), is(equalTo(excelContentOf(expected))));
	}
	*/

	@Test
	public void countJavaUsingExcel97Format() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingExcel97Format ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_showDir.xls";
		URL expected = this.getClass()
				.getResource("stepCount_java_showDir.xls");

		//String[] args = {"-showDirectory", "-format", "excel97", "-encoding", "UTF-8",
		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcel97FormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingExcel97FormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_showDir_en.xls";
		URL expected = this.getClass()
				.getResource("stepCount_java_showDir_en.xls");

		//String[] args = {"-showDirectory", "-format", "excel97", "-encoding", "UTF-8",
		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countJavaUsingCustomRuleAndCsvFormat() throws Exception {
		System.out.println("## StepCountV1 ## countJavaUsingCustomRuleAndCsvFormat ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_rule_java_showDir.csv";
		URL expected = this.getClass()
				.getResource("stepCount_rule_java_showDir.csv");

		String[] args = {"-showDirectory", "-format", "csv",
				"-output", outFileName,
				"-xml", "test/data/ktestl2.xml", newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}
}
