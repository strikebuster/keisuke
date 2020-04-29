package org.jenkinsci.plugins.keisuke;

import org.jvnet.hudson.test.JenkinsRule;

/**
 * Interface for testing Jenkins plugin with Web UI.
 */
public interface JenkinsUITester {

	/**
	 * Gets JenkinsRule instance.
	 * @return JenkinsRule instance.
	 */
	JenkinsRule jenkinsRule();

	/**
	 * Gets JenkinsRule.WebClient instance.
	 * @return JenkinsRule.WebClient instance.
	 */
	JenkinsRule.WebClient webClient();

}
