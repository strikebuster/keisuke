package org.jenkinsci.plugins.keisuke.setup;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.scm.NullSCM;
import hudson.scm.RepositoryBrowser;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import hudson.scm.SCMRevisionState;
import net.sf.json.JSONObject;

/**
 * SCM subclass to checkout(extract) from zip file into workspace or an absolute path.
 * and it returns not null when getDiscriptor() is called, to avoid error at opening
 * project config web page.
 */
public class PrepareDataSCM extends NullSCM {

	static final String DATA_ZIP_PATH = "test/archive/data4ut.zip";
	private URL zipUrl = null;
	private File destDir = null;

	/**
	 * Constructor.
	 * @throws MalformedURLException signal for URL error.
	 */
	@DataBoundConstructor
	public PrepareDataSCM() throws MalformedURLException {
		this.zipUrl = new File(DATA_ZIP_PATH).toURI().toURL();
		//System.out.println("[TEST DEBUG] PrepareDataSCM is constructed now.");
	}

	/**
	 * Sets destination directory path.
	 * @param dest directory path.
	 * @return this instance.
	 */
	@DataBoundSetter
	public PrepareDataSCM setDestination(final String dest) {
		if (dest != null && !dest.isEmpty()) {
			this.destDir = new File(dest);
		}
		return this;
	}

	/**
	 * Gets Zip URL.
	 * @return URL instance.
	 */
	protected URL getZipUrl() {
		return this.zipUrl;
	}

	/**
	 * refresh data at workspace from internal defined zip file.
	 * If absolute path was set, do at the path instead of workspace.
	 * @param build RUn
	 * @param launcher launcher
	 * @param workspace workspace
	 * @param listener TaskListener
	 * @param changelogFile change log file
	 * @param baseline SCMRevisionState to compare
	 */
	@Override
	public void checkout(final Run<?, ?> build, final Launcher launcher,
			final FilePath workspace, final TaskListener listener, final File changelogFile,
			final SCMRevisionState baseline) throws IOException, InterruptedException {
		//System.out.println("[TEST DEBUG] PrepareDataSCM#checkout(Run).");
		if (build instanceof AbstractBuild && listener instanceof BuildListener
				&& Util.isOverridden(SCM.class, getClass(), "checkout", AbstractBuild.class,
						Launcher.class, FilePath.class, BuildListener.class, File.class)) {
			this.checkout((AbstractBuild<?, ?>) build, launcher, workspace,
					(BuildListener) listener, changelogFile);
		} else if (build instanceof WorkflowRun) {
			this.checkout((WorkflowRun) build, launcher, workspace, listener, changelogFile);
		} else {
			System.out.println("[TEST] PrepareDataSCM#checkout(): arguments are not expected class.");
			System.out.println("[TEST] Run : " + build.getClass().getCanonicalName());
			System.out.println("[TEST] listener : " + listener.getClass().getCanonicalName());
			throw new AbortException("PrepareDataSCM#checkout(): arguments are not expected class.");
		}
	}

	/**
	 * Check out from zip file into destination directory.
	 * @param build build
	 * @param launcher launcher
	 * @param workspace workspace
	 * @param listener listener
	 * @param changeLogFile change log file
	 */
	@Override
	public boolean checkout(final AbstractBuild<?, ?> build, final Launcher launcher,
			final FilePath workspace, final BuildListener listener, final File changeLogFile)
			throws IOException, InterruptedException {
		//System.out.println("[TEST DEBUG] PrepareDataSCM#checkout(AbstructBuild).");
		this.extractIntoWorkspaceZip(workspace, listener);
		this.copyDirectory(workspace);
		return true;
	}

	private boolean checkout(final WorkflowRun build, final Launcher launcher,
			final FilePath workspace, final TaskListener listener, final File changeLogFile)
			throws IOException, InterruptedException {
		//System.out.println("[TEST DEBUG] PrepareDataSCM#checkout(WorkflowRun).");
		this.extractIntoWorkspaceZip(workspace, listener);
		this.copyDirectory(workspace);
		return true;
	}

	private void extractIntoWorkspaceZip(final FilePath workspace, final TaskListener listener)
			throws IOException, InterruptedException {
		if (workspace.exists()) {
			System.out.println("[TEST] === Deleting existing workspace " + workspace.getRemote());
			workspace.deleteRecursive();
		}
		System.out.println("[TEST] === Unzipping " + this.zipUrl);
		workspace.unzipFrom(this.zipUrl.openStream());
	}

	private void copyDirectory(final FilePath workspace) throws IOException {
		if (this.destDir != null) {
			FileUtils.copyDirectory(new File(workspace.getRemote() + "/test"),
					new File(this.destDir, "test"));
			System.out.println("[TEST] === copy test data to " + this.destDir.getCanonicalPath());
		} else {
			System.out.println("[TEST] === copy test data to workspace.");
		}
	}

	/*
	 * @return SCMRevisionState.NONE;
	 *
	@Override
	public SCMRevisionState calcRevisionsFromBuild(final Run<?, ?> build, final FilePath workspace,
			final Launcher launcher, final TaskListener listener) throws IOException, InterruptedException {
		//System.out.println("[TEST DEBUG] PrepareDataSCM#calcRevisionsFromBuild().");
		//Thread.sleep(20000);
		return SCMRevisionState.NONE;
	}*/

	/**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//System.out.println("[TEST DEBUG] PrepareDataSCM#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * a Descriptor internal class of jenkins plugin.
	 */
	@Extension
	public static class DescriptorImpl extends SCMDescriptor<SCM> {
		public DescriptorImpl() {
			super(RepositoryBrowser.class);
			//System.out.println("[TEST DEBUG] PrepareDataSCM.DescriptorImpl#DescriptorImpl()");
			this.load();
		}

		/**
		 * Gets display name.a
		 * return display name.
		 */
		@Override
		public String getDisplayName() {
			return "PrepareDataSCM";
		}

		/**
		 * Creates a configured instance from the submitted form of config.jelly.
		 *
		 * @param req object includes represents the entire submission.
		 * @param formData The JSON object that captures the configuration data for this Descriptor.
		 * @return New object implements Publisher
		 * @throws FormException Signals a problem in the submitted form.
		 */
		@Override
		public SCM newInstance(final StaplerRequest req, final JSONObject formData) throws FormException {
			//System.out.println("[TEST DEBUG] PrepareDataSCM.DescriptorImpl#newInstance("
			//		+ req + "," + formData + ")");
			// even though request is always non-null, needs check (see note on Descriptor.newInstance)
			if (req == null) {
				return super.newInstance(null, formData);
            }
			return (PrepareDataSCM) super.newInstance(req, formData);
		}
	}
}
