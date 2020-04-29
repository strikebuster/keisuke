package org.jenkinsci.plugins.keisuke.uihelper;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Interface for client class using ConfigHtmlUI.
 */
public interface ConfigUIClient {

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
}
