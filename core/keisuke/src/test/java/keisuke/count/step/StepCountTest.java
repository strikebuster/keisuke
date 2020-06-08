package keisuke.count.step;

import static keisuke.count.CountTestUtil.binaryContentOf;
//import static keisuke.count.CountTestUtil.excelContentOf;
import static keisuke.count.CountTestUtil.rawContentOf;
import static keisuke.count.CountTestUtil.textContentOf;
import static keisuke.count.option.CountOptionConstant.*;
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
 * Test class of StepCounter.
 */
public class StepCountTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void countJavaUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath.csv");

		String[] args = {"--" + OPT_PATH, "base", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndSubPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_subPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_subPath.csv");

		String[] args = {"--" + OPT_PATH, "sub", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndNoPathWithShowDir() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndNoPathWithShowDir ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_noPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_noPath.csv");

		String[] args = {"--" + OPT_SHOWDIR, "--" + OPT_PATH, "no", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithoutPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWithoutPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java.csv";
		// ファイル名だけで文字列ソート順になる
		URL expected = this.getClass()
				.getResource("stepCount_java_noPath.csv");

		String[] args = {"-f", "csv", "-e", "UTF-8", "-o", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndNoPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndNoPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_noPath.csv";
		// ファイル名だけで文字列ソート順になる
		URL expected = this.getClass()
				.getResource("stepCount_java_noPath.csv");

		String[] args = {"-p", "no", "-f", "csv", "-e", "UTF-8", "-o", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndBasePathAndSortOff() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndBasePathAndSortOff ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_noSort.csv";
		// ソートoffでも引数が１つなら文字列順ソートになるのでデフォルトと同じ

		String[] args = {"-path", "base", "-format", "csv", "-encoding", "UTF-8",
				"--" + OPT_SORT, "off", "-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("stepCount_java_basePath_noSort_win.csv");
		} else {
			expected = this.getClass().getResource("stepCount_java_basePath.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWithoutPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndSortOffWithoutPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_noSort.csv";
		// ソートなしの場合、showDirectory指定した場合のソートなしの順番と同じになる

		String[] args = {"-f", "csv", "-e", "UTF-8", "-S", "off",
				"-o", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		URL expected;
		if (nameOfSystemOS().startsWith("Windows")) {
			expected = this.getClass().getResource("stepCount_java_noPath_noSort_win.csv");
		} else {
			expected = this.getClass().getResource("stepCount_java_noPath_noSort.csv");
		}
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndBasePathWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndBasePathWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_files_basePath.csv";
		// ディレクトリ付きパス名で文字列ソート順になる
		// ファイルが引数のときはディレクトリパスは編集しない。-p base, -p sub, -s は同じ結果
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
	public void countJavaUsingCsvFormatWithoutPathWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCount ## "
				+ "countJavaUsingCsvFormatWithoutPathWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_files.csv";
		// ファイル名だけで文字列ソート順になる
		URL expected = this.getClass()
				.getResource("stepCount_java_files_noPath.csv");

		String[] args = {"-format", "csv", "-encoding", "UTF-8", "-output", outFileName,
				newRoot + "/test/Utils.java", newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatAndSortOffWithoutPathWhenFilesAreGiven() throws Exception {
		System.out.println("## StepCount ## "
				+ "countJavaUsingCsvFormatAndSortOffWithoutPathWhenFilesAreGiven ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_files_noSort.csv";
		// ファイルを引数で渡したときにソートなしの場合、引数の指定順になる
		URL expected = this.getClass()
				.getResource("stepCount_java_files_noPath_noSort.csv");

		String[] args = {"-format", "csv", "-encoding", "UTF-8", "-sort", "off", "-output", outFileName,
				newRoot + "/test/Utils.java", newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWithoutPathWhenAFileIsGiven() throws Exception {
		System.out.println("## StepCount ## "
				+ "countJavaUsingCsvFormatWithoutPathWhenAFileIsGiven ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_afile.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_afile_noPath.csv");

		String[] args = {"-format", "csv", "-encoding", "UTF-8", "-output", outFileName,
				newRoot + "/src/StringUtil.java"};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingCsvFormatWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_en.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_en.csv");

		String[] args = {"-p", "base", "-f", "csv", "-e", "UTF-8",
				"-o", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingDefaultFormatWhichIsTextAndBasePath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingTextFormat ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath.txt";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath.txt");

		String[] args = {"-path", "base", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);
		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingTextFormatAndBasePathWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingTextFormatWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_en.txt";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_en.txt");

		String[] args = {"--path", "base", "--encoding", "UTF-8",
				"--output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingJsonFormatAndSubPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_subPath.json";
		URL expected = this.getClass().getResource(
				"stepCount_java_subPath.json");

		String[] args = {"-p", "sub", "-format", "json", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingJsonFormatAndBasePathWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingJsonFormatAndBasePathWhenLocaleIsEnglish ##");

		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_en.json";
		// 出力結果は英語でも日本語でも同じ
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_en.json");

		String[] args = {"-p", "base", "--format", "json", "-encoding", "UTF-8",
				"--output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		try {
			stepcount.main(args);
		} finally {
			Locale.setDefault(org);
		}
		File actual = new File(outFileName);
		// JSON形式ではエンコードはUTF-8固定
		assertThat(rawContentOf(actual, "UTF-8"), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingXmlFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingXmlFormatAndSubPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_subPath.xml";
		URL expected = this.getClass()
				.getResource("stepCount_java_subPath.xml");

		String[] args = {"-path", "sub", "-format", "xml", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countJavaUsingXmlFormatAndBasePathWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingXmlFormatAndBasePathWhenLocaleIsEnglish ##");

		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_en.xml";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_en.xml");

		String[] args = {"-path", "base", "-format", "xml", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
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
	public void countJavaUsingExcelFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcelFormatAndSubPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_subPath.xlsx";
		URL expected = this.getClass()
				.getResource("stepCount_java_subPath.xlsx");

		String[] args = {"-path", "sub", "-format", "excel", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		//assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
		assertThat(excelContentOf(actual), is(equalTo(excelContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcelFormatAndBasePathWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcelFormatAndBasePathWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_en.xlsx";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_en.xlsx");

		String[] args = {"-path", "base", "-format", "excel", "-encoding", "UTF-8",
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
	public void countJavaUsingExcel97FormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcel97FormatAndSubPath ##");
		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_subPath.xls";
		URL expected = this.getClass()
				.getResource("stepCount_java_subPath.xls");

		//String[] args = {"-path", "sub", "-format", "excel97", "-encoding", "UTF-8",
		String[] args = {"-path", "sub", "-format", "excel", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(binaryContentOf(actual), is(equalTo(binaryContentOf(expected))));
	}

	@Test
	public void countJavaUsingExcel97FormatAndBasePathWhenLocaleIsEnglish() throws Exception {
		System.out.println("## StepCount ## countJavaUsingExcel97FormatAndBasePathWhenLocaleIsEnglish ##");
		Locale org = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);

		String newRoot = "test/data/java/root1";
		String outFileName = "test/out/count_java_basePath_en.xls";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_en.xls");

		//String[] args = {"-path", "base", "-format", "excel97", "-encoding", "UTF-8",
		String[] args = {"-path", "base", "-format", "excel", "-encoding", "UTF-8",
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
	public void countJavaUsingCsvFormatAndBasePathWhenScmDirectoriesExist() throws Exception {
		System.out.println("## StepCount ## countJavaUsingCsvFormatAndBasePathWhenScmDirectoriesExist ##");
		String newRoot = "test/data/java/root4";
		String outFileName = "test/out/count_java_basePath_scm.csv";
		URL expected = this.getClass()
				.getResource("stepCount_java_basePath_scm.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCountProc stepcount = new StepCountProc();
		stepcount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInUtfUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countWebCodingInUtfUsingCsvFormatAndSubPath ##");
		String newRoot = "test/data/web/root1";
		String outFileName = "test/out/count_web_subPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_web_subPath.csv");

		String[] args = {"-p", "sub", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countWebCodingInSjisUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## countWebCodingInSjisUsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/web/root1S";
		String outFileName = "test/out/count_webS_basePath_scm.csv";
		URL expected = this.getClass()
				.getResource("stepCount_webS_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "Windows-31J",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countCUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countCUsingCsvFormatAndSubPath ##");
		String newRoot = "test/data/c/root1";
		String outFileName = "test/out/count_c_subPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_c_subPath.csv");

		String[] args = {"-p", "sub", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjisUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## "
				+ "countMiscSourcesIncludingMiscCommentsInSjisUsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentS/root1";
		String outFileName = "test/out/count_commentS_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_commentS_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "Windows-31J",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludingMiscCommentsInSjis2UsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## "
				+ "countMiscSourcesIncludingMiscCommentsInSjis(2)UsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentS/root10";
		String outFileName = "test/out/count_commentS2_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_commentS2_basePath.csv");

		String[] args = {"-p", "base", "-format", "csv", "-encoding", "Windows-31J",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludigMiscCommentsInUtfUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## "
				+ "countMiscSourcesIncludigMiscCommentsInUtfUsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentU/root1";
		String outFileName = "test/out/count_commentU_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_commentU_basePath.csv");

		String[] args = {"-path", "base", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countMiscSourcesIncludigMiscCommentsInUtf2UsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## "
				+ "countMiscSourcesIncludigMiscCommentsInUtf(2)UsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/commentU/root10";
		String outFileName = "test/out/count_commentU2_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_commentU2_basePath.csv");

		String[] args = {"-path", "base", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	// これ以下のテスト用のデータは未公開
	@Test
	public void countCobolUsingCsvFormatAndSubPath() throws Exception {
		System.out.println("## StepCount ## countCobolUsingCsvFormatAndSubPath ##");
		String newRoot = "test/data/cobol/root1";
		String outFileName = "test/out/count_cobol_subPath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_cobol_subPath.csv");

		String[] args = {"-p", "sub", "-format", "csv", "-encoding", "Shift_JIS",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

	@Test
	public void countOWUsingCsvFormatAndBasePath() throws Exception {
		System.out.println("## StepCount ## countOWUsingCsvFormatAndBasePath ##");
		String newRoot = "test/data/ow/root1";
		String outFileName = "test/out/count_ow_basePath.csv";
		URL expected = this.getClass()
				.getResource("stepCount_ow_basePath.csv");

		String[] args = {"-path", "base", "-format", "csv", "-encoding", "UTF-8",
				"-output", outFileName, newRoot};
		StepCount.main(args);

		File actual = new File(outFileName);
		assertThat(rawContentOf(actual), is(equalTo(textContentOf(expected))));
	}

}
