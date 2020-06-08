package org.jenkinsci.plugins.keisuke;

import static keisuke.util.TestUtil.rawContentOf;
import static keisuke.util.TestUtil.textContentOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.jenkinsci.plugins.keisuke.setup.ProjectMaker;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * countingMode is diff_too.
 */
public class KeisukePublisherUsingDiffTooWithoutBaseDirTest extends AbstractProjectTest {

	@Override @Before
	public void setUp() throws Exception {
		this.setProjectMaker(new ProjectMaker(this.jenkinsRule, CountingModeEnum.BOTH_STEP_AND_DIFF));
	}

	@Test
	public void countJavaUsingCsvFormatAsOneUnit() {
		System.out.println("## KeisukePublisherUsingDiffTooWithoutBaseDir ## "
				+ "countJavaUsingCsvFormatAsOneUnit ##");

		URL[] expected = {
				this.getClass().getResource("stepCountNoBase_java_unit.csv"),
				this.getClass().getResource("diffCountNoBase_java_unit.csv")};
		String[] outfile = {
				"test/out/stepCountDiffTooNoBase_java_unit.csv",
				"test/out/diffCountNoBase_java_unit.csv"};
		File[] actual = new File[2];

		try {
			this.setProject(this.projectMaker().createJobToCountJavaDiffToo(
					"JavaCsvJob", outfile[0], "csv", false, outfile[1], "csv"));
			this.jenkinsRule.buildAndAssertSuccess(this.project());
			//System.out.println("[TEST] Workspace is " + this.workspace().getAbsolutePath());
			for (int i = 0; i < 2; i++) {
				actual[i] = new File(this.workspace(), outfile[i]);
				//System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}

		for (int i = 0; i < 2; i++) {
			System.out.println("[TEST] outfile[" + i + "] = " + actual[i].getAbsolutePath());
			System.out.println(rawContentOf(actual[i]));
			assertThat(rawContentOf(actual[i]), is(equalTo(textContentOf(expected[i]))));
		}
	}
}
