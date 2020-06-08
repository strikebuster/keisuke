package org.jenkinsci.plugins.keisuke;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

/**
 * a Recorder which extends Publisher class of jenkins plugin.
 * this is a main class of a plugin.
 */
public class KeisukePublisher extends Recorder implements SimpleBuildStep, Serializable {
	private static final long serialVersionUID = 4L; // since ver.2.0.0

	@SuppressWarnings("unused")
	private transient PrintStream syslogger = System.out;

	private static final DisplaySetting DEFAULT_DISPLAY_SETTING = new DisplaySetting("");
	private List<CountingUnit> countingUnitList = null;
	private DisplaySetting displaySettingProp = DEFAULT_DISPLAY_SETTING;
	/** Try to process the report files even if the build state is marked as failed. */
	//private final boolean ignoreBuildFailure;

	/**
	 * Constructor.
	 * with arguments which are mandatory variables
	 * @param countingUnits &lt;f:repeatableProperties field="countingUnits"&gt;
	 */
	@DataBoundConstructor
	public KeisukePublisher(final List<CountingUnit> countingUnits) {
		//syslogger.println("[DEBUG] KeisukePublisher#KeisukePublisher(countingUnits)");
		this.setCountingUnits(countingUnits);
	}

	/**
	 * setter for &lt;f:repeatableProperties field="countingUnits"&gt; in config.jelly.
	 * @param countingUnits list of CountingUnit constructed from &lt;f:repeatableProperties&gt;.
	 * @throws RuntimeException occurs when the parameter "countingUnits" is null or empty.
	 */
	//@DataBoundSetter
	public void setCountingUnits(final List<CountingUnit> countingUnits) {
		if (countingUnits == null || countingUnits.isEmpty()) {
			throw new RuntimeException(Messages.errorCountingUnitRequired());
		}
		//syslogger.println("[DEBUG] KeisukePublisher#setCountingUnits(" + countingUnits + ")");
		this.countingUnitList = countingUnits;
	}

	/**
	 * getter for &lt;f:repeatableProperties field="countingUnits"&gt; in config.jelly.
	 * @return list of CountingUnit defined in this KeisukePublisher plugin.
	 */
	public List<CountingUnit> getCountingUnits() {
		if (this.countingUnitList == null) {
			//syslogger.println("[DEBUG] KeisukePublisher#getCountingUnits():null");
			return null;
		}
		//syslogger.println("[DEBUG] KeisukePublisher#getCountingUnits():" + this.countingUnitList);
		return this.countingUnitList;
	}

	/**
	 * setter for &lt;f:property field="displaySetting"&gt; in config.jelly.
	 * @param displaySetting display values properties.
	 */
	@DataBoundSetter
	public void setDisplaySetting(final DisplaySetting displaySetting) {
		if (displaySetting == null) {
			//syslogger.println("[DEBUG] KeisukePublisher#setDisplaySetting(null)");
			this.displaySettingProp = DEFAULT_DISPLAY_SETTING;
			return;
		}
		//syslogger.println("[DEBUG] KeisukePublisher#setDisplaySetting(" + displaySetting + ")");
		this.displaySettingProp = displaySetting;
	}

	/**
	 * getter for &lt;f:property field="displaySetting"&gt; in config.jelly.
	 * @return display values property.
	 */
	public DisplaySetting getDisplaySetting() {
		if (this.displaySettingProp == null) {
			//syslogger.println("[DEBUG] KeisukePublisher#getDisplaySetting():null");
			return null;
		}
		//syslogger.println("[DEBUG] KeisukePublisher#getDisplaySetting():" + this.displaySettingProp);
		return this.displaySettingProp;
	}

	/**
	 * getter for globalSetting of global configuration.
	 * @return globalSetting value.
	 */
	 public GlobalSetting getGlobalSetting() {
		//syslogger.println("[DEBUG] KeisukePublisher#getGlobalSetting():"
		//		+ this.getDescriptor().getGlobalSetting());
		return this.getDescriptor().getGlobalSetting();
	}

	/**
	 * perform main action as Publisher.
	 * method for old style(BuildStepCompatibilityLayer).
	 */
	@Override
	public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher,
			final BuildListener listener) throws InterruptedException, IOException {

		//syslogger.println("[DEBUG] KeisukePublisher#perform(AbstractBuild,Launcher,BuildListener)");
		StepCountBuildAction buildAction = new StepCountBuildAction(build);
		build.addAction(buildAction);
		boolean doneWell = false;
		try {
			EnvVars vars = build.getEnvironment(listener);
			FilePath workspace = build.getWorkspace();
			doneWell = this.doCountingTask(buildAction, workspace, vars, listener.getLogger());
		} catch (IOException e) {
			build.setResult(Result.FAILURE);
			listener.error(e.getMessage());
		} catch (InterruptedException e) {
			build.setResult(Result.FAILURE);
			listener.error(e.getMessage());
		}
		return doneWell;
	}

	/**
	 * perform main action as SimpleBuildStep
	 */
	@Override
	public void perform(final Run<?, ?> run, final FilePath workspace, final Launcher launcher,
			final TaskListener listener) throws InterruptedException, IOException {

		//syslogger.println("[DEBUG] KeisukePublisher#perform(Run,FilePath,Launcher,TaskListener)");
		StepCountBuildAction buildAction = new StepCountBuildAction(run);
		run.addAction(buildAction);
		try {
			EnvVars vars = run.getEnvironment(listener);
			this.doCountingTask(buildAction, workspace, vars, listener.getLogger());
		} catch (IOException e) {
			run.setResult(Result.FAILURE);
			listener.error(e.getMessage());
		} catch (InterruptedException e) {
			run.setResult(Result.FAILURE);
			listener.error(e.getMessage());
		}
	}

	private boolean doCountingTask(final StepCountBuildAction buildAction, final FilePath workspace,
			final EnvVars vars, final PrintStream logger) throws IOException, InterruptedException {

		//syslogger.println("[DEBUG] KeisukePublisher#doCountingTask()");
		if (this.getCountingUnits() == null) {
			logger.println("[keisuke] countingUnits is null.");
			return false;
		}
		for (CountingUnit unit : this.getCountingUnits()) {
			unit.doCountingTask(buildAction, workspace, vars, logger);
		}
		//syslogger.println("[DEBUG] set DisplayStepKind = " + this.displayProp.getDisplayStepKind());
		buildAction.setDisplaySetting(this.displaySettingProp);
		return true;
	}

	/**
	 * this can be executed after the same step in the previous build is completed.
	 */
	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		//syslogger.println("[DEBUG] KeisukePublisher#getRequiredMonitorService()");
		return BuildStepMonitor.STEP;
	}

	/**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//syslogger.println("[DEBUG] KeisukePublisher#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * a Descriptor internal class of jenkins plugin.
	 * this will be used for Global Tool Configuration.
	 */
	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
		//private transient PrintStream syslogger = System.out;

		// global tool configuration field
		private GlobalSetting globalSetting = null;

		public DescriptorImpl() {
			super(KeisukePublisher.class);
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#DescriptorImpl()");
			this.load();
		}

		/**
		 * Returns true if this task is applicable to the given project.
		 * @param jobType Class&lt;? extends AbstractProject&gt;
		 * @return true if this task is applicable
		 */
		@Override
		@SuppressWarnings("rawtypes")
		public boolean isApplicable(final Class<? extends AbstractProject> jobType) {
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#isApplicable()");
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		@Override
		public String getDisplayName() {
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#getDisplayName()");
			return Messages.keisukeDisplayName();
		}

		// Global configuration
		/**
		 * Invoked when the global configuration page is submitted.
		 *
		 * @param req object includes represents the entire submission.
		 * @param formData The JSON object that captures the configuration data for this Descriptor.
		 * @return false to keep the client in the same config page.
		 * @throws FormException Signals a problem in the submitted form.
		 */
		@Override
		public boolean configure(final StaplerRequest req, final JSONObject formData) throws FormException {
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#configure("
			//		+ req + "," + formData + ")");
			JSONObject json = formData.getJSONObject("globalSetting");
			this.setGlobalSetting(this.createGlobalSetting(json));
			this.save();
			return super.configure(req, formData);
		}

		private GlobalSetting createGlobalSetting(final JSONObject formData) {
			if (formData == null) {
				//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl"
				//		+ "#createGlobalSetting(null)");
				return null;
			}
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#createGlobalSetting("
			//		+ formData + ")");
			String encoding = formData.getString("encoding");
			String xmlPath = formData.getString("xmlPath");
			return new GlobalSetting(encoding, xmlPath);
		}

        /**
		 * getter for default configuration specified in global.jelly
		 * &lt;f:property field="globalSetting"&gt;
		 * @return GlobalSetting default values property
		 */
		public GlobalSetting getGlobalSetting() {
			if (this.globalSetting == null) {
				//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#getGlobalSetting():null");
				return null;
			}
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#getGlobalSetting():"
			//		+ this.globalSetting);
			return this.globalSetting;
		}

		/**
	     * setter for default configuration specified as GlobalSetting
	     * @param setting instance of GlobalSetting
	     */
	    //@DataBoundSetter
	    public void setGlobalSetting(final GlobalSetting setting) {
	    	if (setting == null) {
	    		//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#setGlobalSetting(null)");
	    		return;
	    	}
	    	//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#setGlobalSetting("
	    	//		+ setting + ")");
	    	this.globalSetting = setting;
	    }
       // end of global configuration

		/**
		 * Creates a configured instance from the submitted form of config.jelly.
		 *
		 * @param req object includes represents the entire submission.
		 * @param formData The JSON object that captures the configuration data for this Descriptor.
		 * @return New object implements Publisher
		 * @throws FormException Signals a problem in the submitted form.
		 */
		@Override
		public Publisher newInstance(final StaplerRequest req, final JSONObject formData) throws FormException {
			//syslogger.println("[DEBUG] KeisukePublisher.DescriptorImpl#newInstance("
			//		+ req + "," + formData + ")");
			// even though request is always non-null, needs check (see note on Descriptor.newInstance)
			if (req == null) {
				return super.newInstance(null, formData);
            }
			KeisukePublisher publisher = (KeisukePublisher) super.newInstance(req, formData);
			return publisher;
		}
	}
}
