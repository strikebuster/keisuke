package org.jenkinsci.plugins.keisuke;

import org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Interface for testing Jenkins plugin configuring in project config Web UI.
 */
public interface ProjectConfigUITester extends JenkinsUITester {

	/**
	 * Gets HtmlPage of /jenkins/job/project/config
	 * @return HtmlPage instance.
	 */
	HtmlPage configPage();

	/**
	 * Gets HtmlForm in the HtmlPage of /jenkins/job/project/config
	 * @return HtmlForm instance.
	 */
	HtmlForm configForm();

	/**
	 * Gets ConfigHtmlUI instance for this config html page.
	 * @return ConfigHtmlUI instance.
	 */
	ConfigHtmlUI configHtmlUI();

}
