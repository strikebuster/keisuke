package keisuke.count.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static keisuke.util.TestUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test class for FileNameUtil
 */
public class FileNameUtilTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception { }

	@Test
	public void cannotGetPathWhenFullPathIsNull() {
		System.out.println("## FileNameUtil ## cannotGetPathWhenFullPathIsNull ##");
		String fullPath = null;
		String basePath = "srcdir";
		String expected = "";

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void cannotGetPathWhenFullPathIsEmpty() {
		System.out.println("## FileNameUtil ## cannotGetPathWhenFullPathIsEmpty ##");
		String fullPath = "";
		String basePath = "srcdir";
		String expected = "";

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void cannotGetSubPathWhenBasePathIsNull() {
		System.out.println("## FileNameUtil ## cannotGetPathWhenBasePathIsNull ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = null;
		String expected = fullPath;

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void cannotGetSubPathWhenBasePathIsEmpty() {
		System.out.println("## FileNameUtil ## cannotGetPathWhenBasePathIsEmpty ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "";
		String expected = fullPath;

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void cannotGetSubPathWhenBasePathIsIllegal() {
		System.out.println("## FileNameUtil ## cannotGetPathWhenBasePathIsIllegal ##");
		String fullPath = "C:/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String expected = fullPath;

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getRelativePathOnUnix() {
		System.out.println("## FileNameUtil ## getRelativePathOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String expected = "main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getRelativePathWhenBasePathIsRootOnUnix() {
		System.out.println("## FileNameUtil ## getRelativePathWhenBasePathIsRootOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/";
		String expected = "var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		if (!nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void cannotGetRelativePathWhenBasePathIsNotMatchedOnUnix() {
		System.out.println("## FileNameUtil ## "
				+ "cannotGetRelativePathWhenBasePathIsNotMatchedOnUnix ##");
		String fullPath = "/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String expected = fullPath;

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getRelativePathOnWindows() {
		System.out.println("## FileNameUtil ## getRelativePathOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String expected = "main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getRelativePathWhenBasePathIsRootOnWindows() {
		System.out.println("## FileNameUtil ## getRelativePathWhenBasePathIsRootOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/";
		String expected = "var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void cannotGetRelativePathWhenBasePathIsNotMatchedOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "cannotGetRelativePathWhenBasePathIsNotMatchedOnWindows ##");
		String fullPath = "D:/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String expected = fullPath;

		String actual = FileNameUtil.getRelativePath(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromBottomOfBaseOnUnix() {
		System.out.println("## FileNameUtil ## getSubPathFromBottomOfBaseOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String expected = "src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromBottomOfBase(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromBottomOfBaseWhenBasePathIsRootOnUnix() {
		System.out.println("## FileNameUtil ## getSubPathFromBottomOfBaseWhenBasePathIsRootOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/";
		String expected = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromBottomOfBase(fullPath, basePath);
		assertThat(actual, equalTo(expected));
		/* Windowsでも同じ結果が得られる
		if (!nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
		*/
	}

	@Test
	public void cannotGetSubPathFromBottomOfBaseWhenBasePathIsNotMatchedOnUnix() {
		System.out.println("## FileNameUtil ## "
				+ "cannotGetSubPathFromBottomOfBaseWhenBasePathIsNotMatchedOnUnix ##");
		String fullPath = "/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String expected = fullPath;

		String actual = FileNameUtil.getSubPathFromBottomOfBase(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromBottomOfBaseOnWindows() {
		System.out.println("## FileNameUtil ## getSubPathFromBottomOfBaseOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String expected = "src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromBottomOfBase(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromBottomOfBaseWhenBasePathIsRootOnWindows() {
		System.out.println("## FileNameUtil ## getSubPathFromBottomOfBaseWhenBasePathIsRootOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/";
		String expected = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromBottomOfBase(fullPath, basePath);
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void cannotGetSubPathFromBottomOfBaseWhenBasePathIsNotMatchedOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "cannotGetSubPathFromBottomOfBaseWhenBasePathIsNotMatchedOnWindows ##");
		String fullPath = "D:/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String expected = fullPath;

		String actual = FileNameUtil.getSubPathFromBottomOfBase(fullPath, basePath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromTopOfBaseOnUnix() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String currentPath = "/var/dir/toolbox";
		String expected = "../work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromTopOfBaseWhenBasePathIsChildOfCurrentOnUnix() {
		System.out.println("## FileNameUtil ## "
				+ "getSubPathFromTopOfBaseWhenBasePathIsChildOfCurrentOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String currentPath = "/var/dir";
		String expected = "work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromTopOfBaseWhenBasePathIsRootOnUnix() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseWhenBasePathIsRootOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/";
		String currentPath = "/var/dir/toolbox";
		String expected = "../../../var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		if (!nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void getSubPathFromTopOfBaseWhenCurrentPathIsRootOnUnix() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseWhenCurrentPathIsRootOnUnix ##");
		String fullPath = "/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String currentPath = "/";
		String expected = "var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		if (!nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void cannotGetSubPathFromTopOfBaseWhenBasePathIsNotMatchedOnUnix() {
		System.out.println("## FileNameUtil ## "
				+ "cannotGetSubPathFromTopOfBaseWhenBasePathIsNotMatchedOnUnix ##");
		String fullPath = "/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "/var/dir/work/src";
		String currentPath = "/var/dir/toolbox";
		String expected = fullPath;

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromTopOfBaseAtAnotherDriveOnWindows() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseAtAnotherDriveOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String currentPath = "C:/bin/toolbox/count";
		String expected = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
		/* Unixでも同じ結果が得られる
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
		*/
	}

	@Test
	public void getSubPathFromTopOfBaseAtSameDriveOnWindows() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseAtSameDriveOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String currentPath = "D:/bin/toolbox/count";
		String expected = "../../../var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromTopOfBaseWhenBasePathIsChildOfCurrentOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "getSubPathFromTopOfBaseWhenBasePathIsChildOfCurrentOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String currentPath = "D:/var/dir";
		String expected = "work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getSubPathFromTopOfBaseWhenBasePathIsRootOnWindows() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseWhenBasePathIsRootOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/";
		String currentPath = "D:/var/dir/toolbox";
		String expected = "../../../var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void getSubPathFromTopOfBaseWhenCurrentPathIsRootOnWindows() {
		System.out.println("## FileNameUtil ## getSubPathFromTopOfBaseWhenCurrentPathIsRootOnWindows ##");
		String fullPath = "D:/var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String currentPath = "D:/";
		String expected = "var/dir/work/src/main/java/keisuke/count/util/FileNameUtil.java";

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
		/* Unixでも同じ結果が得られる
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
		*/
	}

	@Test
	public void cannotGetSubPathFromTopOfBaseWhenBasePathIsNotMatchedOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "cannotGetSubPathFromTopOfBaseWhenBasePathIsNotMatchedOnWindows ##");
		String fullPath = "D:/var/dir/work/srcdir/main/java/keisuke/count/util/FileNameUtil.java";
		String basePath = "D:/var/dir/work/src";
		String currentPath = "D:/var/dir/toolbox";
		String expected = fullPath;

		String actual = FileNameUtil.getSubPathFromTopOfBase(fullPath, basePath, currentPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getTrueFullPathIsAbsolutePathOnUnix() {
		System.out.println("## FileNameUtil ## "
				+ "getTrueFullPathIsAbsolutePathOnUnix ##");
		String targetPath = "/var/dir/work/srcdir";
		boolean expected = true;

		boolean actual = FileNameUtil.isAbsolutePath(targetPath);
		if (!nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void getFalseRelativePathIsAbsolutePathOnUnix() {
		System.out.println("## FileNameUtil ## "
				+ "getFalseRelativePathIsAbsolutePathOnUnix ##");
		String targetPath = "work/srcdir";
		boolean expected = false;

		boolean actual = FileNameUtil.isAbsolutePath(targetPath);
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void getTrueFullPathIsAbsolutePathOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "getTrueFullPathIsAbsolutePathOnWindows ##");
		String targetPath = "C:/var/dir/work/srcdir";
		boolean expected = true;

		boolean actual = FileNameUtil.isAbsolutePath(targetPath);
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void getTrueFullPathWithSmallDriveLetterIsAbsolutePathOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "getTrueFullPathWithSmallDriveLetterIsAbsolutePathOnWindows ##");
		String targetPath = "c:/var/dir/work/srcdir";
		boolean expected = true;

		boolean actual = FileNameUtil.isAbsolutePath(targetPath);
		if (nameOfSystemOS().startsWith("Windows")) {
			assertThat(actual, equalTo(expected));
		} else {
			System.out.println("--- but this is runnning on " + nameOfSystemOS()
					+ ", so we do not get expected result.");
			System.out.println("--- expected=" + expected);
			System.out.println("--- actual  =" + actual);
			assertThat(actual, not(equalTo(expected)));
		}
	}

	@Test
	public void getFalseRelativePathIsAbsolutePathOnWindows() {
		System.out.println("## FileNameUtil ## "
				+ "getFalseRelativePathIsAbsolutePathOnWindows ##");
		String targetPath = "work/srcdir";
		boolean expected = false;

		boolean actual = FileNameUtil.isAbsolutePath(targetPath);
		assertThat(actual, equalTo(expected));
	}
}
