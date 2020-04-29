package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import org.jenkinsci.plugins.keisuke.uihelper.ProjectConfigUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

/**
 * Testing publisher's explanation in project config page.
 */
public class ProjectConfigUITest implements JenkinsUITester {

	private JenkinsRule.WebClient webClient = null;
	private ProjectConfigUI projectUI = null;

	// JenkinsUITester methods.
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

	// Test rule methods.
	@Before
	public void setUp() throws Exception {
		this.webClient = this.jenkinsRule.createWebClient();
		this.projectUI = new ProjectConfigUI(this);
		this.projectUI.prepareKeisukeConfig();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();

	// Test case methods.
	@Test
	public void showTitleInProjectConfig() {
		System.out.println("## ProjectConfigUI ## showTitleInProjectConfig ##");
		String titleText = this.projectUI.getTitleOfKeisuke();
		System.out.println("[TEST] Title text:" + titleText);
		assertThat(titleText, startsWith("Keisuke"));
	}

	@Test
	public void showHelpInProjectConfig() {
		System.out.println("## ProjectConfigUI ## showHelpInProjectConfig ##");
		// Pluginのヘルプの検証
		HtmlAnchor helpAnchor = this.projectUI.findKeisukeHelpAnchor();
		assertThat(helpAnchor, notNullValue());
		//System.out.println("[TEST] helpAnchor[keisuke] XPath:" + helpAnchor.getCanonicalXPath());
		String expected = "Keisuke";
		String actual = this.projectUI.getHelpContentFrom(helpAnchor);
		assertThat(actual, containsString(expected));
	}

}
