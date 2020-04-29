package org.jenkinsci.plugins.keisuke.uihelper;

import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITLONGTIME;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITTIME;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * UI helper for jenkins project or build reporting page which uses KeisukePublisher.
 */
public abstract class AbstractReportPageUI {
	private JenkinsRule.WebClient webClient = null;
	private HtmlPage targetPage = null;

	/**
	 * Constructs an instance.
	 * @param client JenkinsRule.WebClient for jenkins web ui.
	 * @param page HtmlPage of jenkins web ui.
	 */
	protected AbstractReportPageUI(final JenkinsRule.WebClient client, final HtmlPage page) {
		this.webClient = client;
		this.targetPage = page;
	}

	/**
	 * Constructs an instance.
	 * @param client JenkinsRule.WebClient for jenkins web ui.
	 * @param name name of jenkins project.
	 */
	protected AbstractReportPageUI(final JenkinsRule.WebClient client, final String name) {
		this.webClient = client;
		this.openPage(name);
	}

	/**
	 * Gets opened target page.
	 * @return target page.
	 */
	public HtmlPage getPage() {
		return this.targetPage;
	}

	/**
	 * Gets URL path of target page.
	 * @param projectName project name which is included the URL path.
	 * @return URL path string.
	 */
	abstract String getPageUrlPath(String projectName);

	/**
	 * Open project target page.
	 * @param projectName project name
	 */
	protected void openPage(final String projectName) {
		try {
			this.targetPage = this.webClient.goTo(this.getPageUrlPath(projectName));
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}  catch (SAXException ex) {
			ex.printStackTrace();
			fail("Unexpected SAXException is occured.");
		}
	}

	/**
	 * Finds html division of "main-panel" in target page.
	 * @return html division.
	 */
	public HtmlDivision findDivOfMainPanel() {
		DomElement node = this.targetPage.getElementById("main-panel");
		//System.out.println("[TEST] main-panel XPath:" + node.getCanonicalXPath());
		return (HtmlDivision) node;
	}

	/**
	 * Waits a few hundred mili-seconds for runnning background process.
	 */
	public void waitForBackgroundProcess() {
		this.getPage().getWebClient().waitForBackgroundJavaScript(WAITTIME);
		if (nameOfSystemOS().startsWith("Windows")) {
			// Windows is heavy about HTML UI, so additional waiting
			this.getPage().getWebClient().waitForBackgroundJavaScript(WAITTIME);
		}
	}

	/**
	 * Waits a second for runnning background process.
	 */
	public void waitLongForBackgroundProcess() {
		this.getPage().getWebClient().waitForBackgroundJavaScript(WAITLONGTIME);
	}
}
