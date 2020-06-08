package org.jenkinsci.plugins.keisuke;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Testing KeisukePublisher in Job,
 * countingMode is step_simply
 */
public abstract class AbstractJobTest {

	@Before
	public void setUp() throws Exception { }

	@After
	public void tearDown() throws Exception {	}

	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();

	@Rule
    public ExpectedException thrownEx = ExpectedException.none();

	/**
	 * Gets workspace root directory.
	 * @return File instance of workspace.
	 * @throws InterruptedException signal of error.
	 * @throws IOException signal of error.
	 */
	abstract File workspace() throws InterruptedException, IOException;

	private static final int THOUSAND = 1000;
	/**
	 * Sleep some seconds for waiting.
	 * @param seconds sleep time
	 */
	protected void sleep(final int seconds) {
		try {
			Thread.sleep(seconds * THOUSAND);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}
}
