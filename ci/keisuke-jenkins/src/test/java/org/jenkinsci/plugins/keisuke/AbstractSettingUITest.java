package org.jenkinsci.plugins.keisuke;

import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITLONGTIME;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITTIME;
import static org.junit.Assert.fail;

import org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI;
import org.jenkinsci.plugins.keisuke.uihelper.ProjectConfigUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import hudson.model.FreeStyleProject;

/**
 * Preparing procedure that creates FreeStyleProject and execute FreeStyleBuild.
 */
public abstract class AbstractSettingUITest implements ProjectConfigUITester {

	private JenkinsRule.WebClient webClient = null;
	private ProjectConfigUI projectUI = null;

	// JenkinsUITester(super class of ProjectConfigUITester) methods.
	/**
	 * Gets JenkinsRule instance.
	 * @return JenkinsRule instance.
	 */
	@Override
	public JenkinsRule jenkinsRule() {
		return this.jenkinsRule;
	}

	/**
	 * Gets JenkinsRule.WebClient instance.
	 * @return JenkinsRule.WebClient instance.
	 */
	@Override
	public JenkinsRule.WebClient webClient() {
		return this.webClient;
	}

	// ProjectConfigUITester methods.
	/**
	 * Gets HtmlPage of /jenkins/job/project/config
	 * @return HtmlPage instance.
	 */
	@Override
	public HtmlPage configPage() {
		return this.projectUI.configPage();
	}

	/**
	 * Gets HtmlForm in the HtmlPage of /jenkins/job/project/config
	 * @return HtmlForm instance.
	 */
	@Override
	public HtmlForm configForm() {
		return this.projectUI.configForm();
	}

	/**
	 * Gets ConfigHtmlUI instance for this config html page.
	 * @return ConfigHtmlUI instance.
	 */
	@Override
	public ConfigHtmlUI configHtmlUI() {
		return this.projectUI.configHtmlUI();
	}

	// Test rule methods.
	@Before
	public void setUp() throws Exception {
		this.webClient = this.jenkinsRule.createWebClient();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();

	// common methods for this setting UI test.
	/**
	 * Prepares to set properties of keisuke plugin in project config page.
	 * create project, use keisuke plugin, open project config page.
	 */
	protected void prepareKeisukeConfig() {
		this.projectUI = new ProjectConfigUI(this);
		this.projectUI.prepareKeisukeConfig();
	}

	/**
	 * Prepares to set properties of keisuke plugin in project config page.
	 * Set global(system) config about keisuke plugin.
	 * create project, use keisuke plugin, open project config page.
	 * @param setting GlobalSetting instance.
	 */
	protected void prepareKeisukeConfig(final GlobalSetting setting) {
		this.projectUI = new ProjectConfigUI(this);
		this.projectUI.prepareKeisukeConfigWithGlobalSetting(setting);
	}

	/**
	 * Waits some time for background process which is called by some UI event.
	 */
	protected void waitForEventCallbackProcess() {
		this.webClient.waitForBackgroundJavaScript(WAITTIME);
		if (nameOfSystemOS().startsWith("Windows")) {
			// Windows is heavy about HTML UI, so additional waiting
			this.webClient.waitForBackgroundJavaScript(WAITTIME);
		}
	}

	/**
	 * Waits some long time for background process which is called by some UI event.
	 */
	protected void waitLongForEventCallbackProcess() {
		this.webClient.waitForBackgroundJavaScript(WAITLONGTIME);
	}

	/**
	 * Submits the config form of project config page.
	 */
	protected void submitConfig() {
		try {
			String urlpath = this.configPage().getUrl().getPath();
			//System.out.println("[TEST DEBUG] === submit the form of page:" + urlpath);
			this.jenkinsRule.submit(this.configForm());
			this.waitLongForEventCallbackProcess();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
	}

	/**
	 * Submits the config form of project config page and re-open the page.
	 */
	protected void submitConfigAndReopen() {
		try {
			String urlpath = this.configPage().getUrl().getPath();
			//System.out.println("[TEST DEBUG] === submit the form of page:" + urlpath);
			this.jenkinsRule.submit(this.configForm());
			this.waitLongForEventCallbackProcess();
			//System.out.println("[TEST DEBUG] === re-open page:" + "(job/Test/configure)");
			this.projectUI.openProjectConfigPage();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
	}

	/**
	 * Gets ProjectConfigUI instance for the project to test.
	 * @return ProjectConfigUI instance.
	 */
	protected ProjectConfigUI projectConfigUI() {
		return this.projectUI;
	}

	/**
	 * Gets FreeStyleProject instance.
	 * @return FreeStyleProject instance.
	 */
	protected FreeStyleProject project() {
		return this.projectUI.project();
	}
}
