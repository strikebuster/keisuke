package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.jenkinsci.plugins.keisuke.setup.ProjectMakingUtil.createProjectJobWithKeisukePublisher;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jenkins.model.Jenkins;

/**
 * Testing KeisukePublisher in FreeStyleProject,
 * before step counting.
 */
public class KeisukePublisherNoCountingTest extends AbstractProjectTest {

	@Test
	public void configureDefaultGlobalSetting() {
		System.out.println("## KeisukePublisher ## configureDefaultGlobalSetting ##");
		try {
			this.setProject(this.projectMaker().createJobToCountJava("DummyJob", null, null, true));
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured." + ex);
		}
		KeisukePublisher.DescriptorImpl descriptor
			= Jenkins.getInstance().getDescriptorByType(KeisukePublisher.DescriptorImpl.class);
		descriptor.setGlobalSetting(new GlobalSetting(null, null));
		KeisukePublisher publisher = (KeisukePublisher) this.project().getPublisher(descriptor);
		String actual = publisher.getGlobalSetting().getEncoding();
		assertThat(actual, equalTo(System.getProperty("file.encoding")));
	}

	@Test
	public void cannotConfigureWhenCountingUnitsIsNull() {
		System.out.println("## KeisukePublisherNoCountingTest ## cannotConfigureWhenCountingUnitsIsNull ##");
		String expected = Messages.errorCountingUnitRequired();
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		DisplaySetting displaySetting = new DisplaySetting("");
		try {
			this.setProject(createProjectJobWithKeisukePublisher(
					this.jenkinsRule, "NoUnitJob", displaySetting, null));
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
		fail("Should throw Exception : " + expected);
	}

	@Test
	public void cannotConfigureWhenCountingUnitIsEmpty() {
		System.out.println("## KeisukePublisherNoCountingTest ## cannotConfigureWhenCountingUnitIsEmpty ##");
		String expected = Messages.errorCountingUnitRequired();
		thrownEx.expect(RuntimeException.class);
		thrownEx.expectMessage(expected);

		DisplaySetting displaySetting = new DisplaySetting("");
		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		try {
			this.setProject(createProjectJobWithKeisukePublisher(this.jenkinsRule, "NoUnitJob",
					displaySetting, countingUnits));
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
		fail("Should throw Exception : " + expected);
	}

	@Test
	public void configureDefaultSettingAboutOtherThanInputSetting() {
		System.out.println("## KeisukePublisherNoCountingTest ## "
				+ "configureDefaultSettingAboutOtherThanInputSetting ##");

		List<CountingUnit> countingUnits = new ArrayList<CountingUnit>();
		countingUnits.add(new CountingUnit(new InputSetting("dummy", "test/data", "UTF-8",
				"", CountingModeEnum.ONLY_STEP_SIMPLY.getValue())));
		try {
			this.setProject(createProjectJobWithKeisukePublisher(
					this.jenkinsRule, "NoTargetJob", null, countingUnits));
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
		KeisukePublisher.DescriptorImpl descriptor
			= Jenkins.getInstance().getDescriptorByType(KeisukePublisher.DescriptorImpl.class);
		KeisukePublisher publisher = (KeisukePublisher) this.project().getPublisher(descriptor);
		assertThat(publisher.getDisplaySetting(), not(nullValue()));
		assertThat(publisher.getDisplaySetting().getDisplayStepKindEnum(), equalTo(DisplayStepKindEnum.CODE));
		assertThat(publisher.getCountingUnits().get(0).getOutputSetting(), nullValue());
	}

}
