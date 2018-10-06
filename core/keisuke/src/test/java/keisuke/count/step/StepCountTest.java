package keisuke.count.step;

import static keisuke.count.CountTestUtil.*;
import static keisuke.count.option.CountOptionConstant.*;
import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import keisuke.count.StepCount;
import keisuke.util.StderrCapture;
import keisuke.util.StdoutCapture;

/**
 * Test class of StepCounter.
 *
 */
public class StepCountTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	@Test
	public void handleHelpOption() throws Exception {
		System.out.println("## StepCount ## arg01 ## handleHelpOption ##");
		//URL expected = this.getClass().getResource("empty.txt");

		String[] args = {"-?", "xxx"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		//assertThat(contentOf(stepcount.argMap()), is(equalTo(contentOf(expected))));
		assertThat(stepcount.argMapEntity(), is(nullValue()));
	}

	@Test
	public void handleIllegalOption() throws Exception {
		System.out.println("## StepCount ## arg02 ## handleIllegalOption ##");
		String expected = "fail to parse";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"-zzz", "-s", "-e", "EUC-JP"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButSourceRootIsNotGiven() throws Exception {
		System.out.println("## StepCount ## arg03 ## handleOptionsButSourceRootIsNotGiven ##");

		StdoutCapture outCapture = new StdoutCapture();
		StderrCapture errCapture = new StderrCapture();
		String outMessage = null;
		String errMessage = null;
		Exception firedException = null;
		String expected = "no file argument";

		String[] args = {"-s", "-e", "EUC-JP", "-f", "xml", "-x", "test/data/ktestl2.xml"};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} catch (Exception e) {
			firedException = e;
		} finally {
			outMessage = outCapture.getCapturedString();
			outCapture.finish();
			errMessage = errCapture.getCapturedString();
			errCapture.finish();
		}

		final int expectedNumber = 4;
		assertThat(stepcount.argMapEntity().size(), is(expectedNumber));
		assertThat(stepcount.argMapEntity(), hasEntry("showDirectory", "true"));
		assertThat(stepcount.argMapEntity(), hasEntry("xml", "test/data/ktestl2.xml"));
		assertThat(stepcount.argMapEntity(), hasEntry("format", "xml"));
		assertThat(stepcount.argMapEntity(), hasEntry("encoding", "EUC-JP"));

		assertThat(outMessage, containsString("usage"));
		assertThat(errMessage, containsString("not specified source path"));
		if (firedException != null) {
			assertThat(firedException.getMessage(), containsString(expected));
		} else {
			// Not reach here because exception is expected to be occured
			fail("Should throw Exception : " + expected);
		}
	}

	@Test
	public void handleOptionsButInvalidFormat() throws Exception {
		System.out.println("## StepCount ## arg04 ## handleOptionsButInvalidFormat ##");
		String newRoot = "test/data/java/root1";
		String expected = "invalid format value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"--" + OPT_FORMAT, "html", "--" + OPT_ENCODE, "UTF-8",
				"--" + OPT_OUTPUT, "test/out/count_arg04.html", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void handleOptionsButInvalidSort() throws Exception {
		System.out.println("## StepCount ## arg05 ## handleOptionsButInvalidSort ##");
		String newRoot = "test/data/java/root1";
		String expected = "invalid sort value";
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		String[] args = {"--" + OPT_SORT, "abc", "-encoding", "UTF-8",
				"-output", "test/out/count_arg05.txt", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		fail("Should throw Exception : " + expected);
	}

	@Test
	public void countJavaUsingCsvFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java.csv");

		String[] args = {"--" + OPT_SHOWDIR, "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java.csv", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOff() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndSortOff ##");
		String newRoot = "test/data/java/root1";
		// ソートoffでも引数が１つなら文字列順ソートになるのでデフォルトと同じ

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"--" + OPT_SORT, "off", "-output", "test/out/count_java_nosort.csv", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_nosort.csv");
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("StepCounterTest_testCount_java_nosort_win.csv");
		} else {
			expected = this.getClass().getResource("StepCounterTest_testCount_java.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithoutShowDirectory() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWithoutShowDirectory ##");
		String newRoot = "test/data/java/root1";
		// ファイル名だけで文字列ソート順になる
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_nodir.csv");

		String[] args = {"-f", "csv", "-e", "UTF-8",
				"-o", "test/out/count_java_nodir.csv", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_nodir.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWithoutShowDirectory() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndSortOffWithoutShowDirectory ##");
		String newRoot = "test/data/java/root1";
		// ソートなしの場合、showDirectory指定した場合のソートなしの順番と同じになる

		String[] args = {"-f", "csv", "-e", "UTF-8", "-S", "off",
				"-o", "test/out/count_java_nodir_nosort.csv", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_nodir_nosort.csv");
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("StepCounterTest_testCount_java_nodir_nosort_win.csv");
		} else {
			expected = this.getClass().getResource("StepCounterTest_testCount_java_nodir_nosort.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		// ディレクトリ付きパス名で文字列ソート順になる
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_files.csv");

		String[] args = {"-s", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java_files.csv",
				newRoot + "/test/Utils.java", newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_files.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithoutShowDirectoryWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCount ## "
				+ "countJavaUsingCsvFormatWithoutShowDirectoryWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		// ファイル名だけで文字列ソート順になる
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_nodir_files.csv");

		String[] args = {"-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java_nodir_files.csv",
				newRoot + "/test/Utils.java", newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_nodir_files.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWithoutShowDirectoryWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCount ## "
				+ "countJavaUsingCsvFormatAndSortOffWithoutShowDirectoryWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		// ファイルを引数で渡したときにソートなしの場合、引数の指定順になる
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_nodir_files_nosort.csv");

		String[] args = {"-format", "csv", "-encoding", "UTF-8", "-sort", "off",
				"-output", "test/out/count_java_nodir_files_nosort.csv",
				newRoot + "/test/Utils.java", newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_nodir_files_nosort.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithoutShowDirectoryWhenAFileIsGiven() throws Exception {
		System.out.println("## StepCount ## "
				+ "countJavaUsingCsvFormatWithoutShowDirectoryWhenAFileIsGiven ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_nodir_afile.csv");

		String[] args = {"-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java_nodir_afile.csv",
				newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_nodir_afile.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_en.csv");

		String[] args = {"-s", "-f", "csv", "-e", "UTF-8",
				"-o", "test/out/count_java_en.csv", newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File("test/out/count_java_en.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingDefaultFormatWhichIsText() throws Exception {
		System.out.println("## StepCount ## countJavaUsingTextFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java.txt");

		String[] args = {"-showDirectory", "-encoding", "UTF-8",
				"-output", "test/out/count_java.txt", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);
		File actual = new File("test/out/count_java.txt");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingTextFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingTextFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_en.txt");

		String[] args = {"--showDirectory", "--encoding", "UTF-8",
				"--output", "test/out/count_java_en.txt", newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File("test/out/count_java_en.txt");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingJsonFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass().getResource(
				"StepCounterTest_testCount_java.json");

		String[] args = {"-showDirectory", "-format", "json", "-encoding", "UTF-8",
				"-output", "test/out/count_java.json", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java.json");
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingJsonFormatWhenLocaleIsEnglish ##");

		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_en.json");

		String[] args = {"-s", "--format", "json", "-encoding", "UTF-8",
				"--output", "test/out/count_java_en.json", newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File("test/out/count_java_en.json");
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingXmlFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingXmlFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java.xml");

		String[] args = {"-showDirectory", "-format", "xml", "-encoding", "UTF-8",
				"-output", "test/out/count_java.xml", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java.xml");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingXmlFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingXmlFormatWhenLocaleIsEnglish ##");

		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_en.xml");

		String[] args = {"-showDirectory", "-format", "xml", "-encoding", "UTF-8",
				"-output", "test/out/count_java_en.xml", newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File("test/out/count_java_en.xml");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcelFormat() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcelFormat ##");
		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java.xls");

		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", "test/out/count_java.xls", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java.xls");
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcelFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcelFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_en.xls");

		String[] args = {"-showDirectory", "-format", "excel", "-encoding", "UTF-8",
				"-output", "test/out/count_java_en.xls", newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File("test/out/count_java_en.xls");
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenScmDirectoriesExist() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWhenScmDirectoriesExist ##");
		String newRoot = "test/data/java/root4";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_java_scm.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_java_scm.csv", newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File("test/out/count_java_scm.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInUtf() throws Exception {
		System.out.println("## StepCount ## countWebCodingInUtf ##");
		String newRoot = "test/data/web/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_web.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_web.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_web.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInSjis() throws Exception {
		System.out.println("## StepCount ## countWebCodingInSjis ##");
		String newRoot = "test/data/web/root1S";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_webS.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_webS.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_webS.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countC() throws Exception {
		System.out.println("## StepCount ## countC ##");
		String newRoot = "test/data/c/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_c.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/count_c.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_c.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjis() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludingMiscCommentsInSjis ##");
		String newRoot = "test/data/commentS/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_commentS.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_commentS.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentS.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjis2() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludingMiscCommentsInSjis(2) ##");
		String newRoot = "test/data/commentS/root10";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_commentS2.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Windows-31J",
				"-output", "test/out/count_commentS2.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentS2.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludigMiscCommentsInUtf() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludigMiscCommentsInUtf ##");
		String newRoot = "test/data/commentU/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_commentU.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_commentU.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentU.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludigMiscCommentsInUtf2() throws Exception {
		System.out.println("## StepCount ## countMiscSourcesIncludigMiscCommentsInUtf(2) ##");
		String newRoot = "test/data/commentU/root10";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_commentU2.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_commentU2.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_commentU2.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	// これ以下のテスト用のデータは未公開
	@Test
	public void countCobol() throws Exception {
		System.out.println("## StepCount ## countCobol ##");
		String newRoot = "test/data/cobol/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_cobol.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", "test/out/count_cobol.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_cobol.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countOW() throws Exception {
		System.out.println("## StepCount ## countOW ##");
		String newRoot = "test/data/ow/root1";
		URL expected = this.getClass()
				.getResource("StepCounterTest_testCount_ow.csv");

		String[] args = {"-showDirectory", "-format", "csv", "-encoding", "UTF-8",
				"-output", "test/out/count_ow.csv", newRoot};
		StepCount.main(args);

		File actual = new File("test/out/count_ow.csv");
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

}
