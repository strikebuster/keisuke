package org.jenkinsci.plugins.keisuke;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

/**
 * Data bound class with Jenkins config UI.
 * This has values of Counting Unit.
 */
public class CountingUnit extends AbstractDescribableImpl<CountingUnit> implements Serializable {

	private static final long serialVersionUID = 2L; // since ver.2.0.0
	private transient PrintStream syslogger = System.out;

	private InputSetting inputSettingProp = null;
	private boolean outputEnabled = false;
	private OutputSetting outputSettingProp = null;

	/**
	 * Constructor.
	 * with arguments which are mandatory variables
	 * @param inputSetting &lt;f:property field="inputSetting"&gt;
	 */
	@DataBoundConstructor
	public CountingUnit(final InputSetting inputSetting) {
		if (inputSetting == null) {
			//syslogger.println("[DEBUG] CountingUnit#CountingUnit(null)");
			this.setInputSetting(null);
		} else {
			//syslogger.println("[DEBUG] CountingUnit#CountingUnit(" + inputSetting + ")");
			this.setInputSetting(inputSetting);
		}
	}

	/**
	 * setter for &lt;f:property field="inputSetting"&gt; in config.jelly.
	 * @param inputSetting InputSetting instance constructed from &lt;f:property&gt; .
	 */
	//@DataBoundSetter
	public void setInputSetting(final InputSetting inputSetting) {
		if (inputSetting == null) {
			throw new RuntimeException(Messages.errorInputSettingRequired());
		}
		//syslogger.println("[DEBUG] CountingUnit#setInputSetting(" + inputSetting + ")");
		this.inputSettingProp = inputSetting;
	}

	/**
	 * getter for &lt;f:property field="inputSetting"&gt; in config.jelly.
	 * @return InputSetting instance specified in this unit.
	 */
	public InputSetting getInputSetting() {
		if (this.inputSettingProp == null) {
			//syslogger.println("[DEBUG] CountingUnit#getInputSetting():null");
			return null;
		}
		//syslogger.println("[DEBUG] CountingUnit#getInputSetting():" + this.inputSettingProp);
		return this.inputSettingProp;
	}

	/**
	 * setter for &lt;f:optionalProperty field="outputSetting"&gt; in config.jelly.
	 * @param outputSetting output values property.
	 */
	@DataBoundSetter
	public void setOutputSetting(final OutputSetting outputSetting) {
		if (outputSetting == null) {
			//syslogger.println("[DEBUG] CountingUnit#setOutputSetting(null)");
			this.setOutputEnabled(false);
		} else {
			//syslogger.println("[DEBUG] CountingUnit#setOutputSetting(" + outputSetting + ")");
			this.setOutputEnabled(true);
		}
		this.outputSettingProp = outputSetting;
	}

	/**
	 * getter for &lt;f:optionalProperty field="outputSetting"&gt; in config.jelly.
	 * @return output values property.
	 */
	public OutputSetting getOutputSetting() {
		if (this.outputSettingProp == null) {
			//syslogger.println("[DEBUG] CountingUnit#getOutputSetting():null");
			return null;
		}
		//syslogger.println("[DEBUG] CountingUnit#getOutputSetting():" + this.outputSettingProp);
		return this.outputSettingProp;
	}

	/**
	 * get boolean value which means outputSettings are configured.
	 * @return boolean value.
	 */
	public boolean isOutputEnabled() {
		//syslogger.println("[DEBUG] CountingUnit#isOutputEnabled():" + this.outputEnabled);
		return this.outputEnabled;
	}

	/**
	 * set boolean value which means outputSettings are configured.
	 * @param bool if configured, then true.
	 */
	private void setOutputEnabled(final boolean bool) {
		//syslogger.println("[DEBUG] CountingUnit#setOutputEnabled(" + bool + ")");
		this.outputEnabled = bool;
	}

	/**
     * Gets string for printing the value of this class
     * @return string
     */
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"inputSetting\":").append(this.inputSettingProp);
    	if (this.outputSettingProp != null) {
    		sb.append(",\"outputSetting\":").append(this.outputSettingProp);
    	}
    	sb.append("}");
    	return sb.toString();
    }

    /**
	 * Gets string for Pipeline configuration script
	 * @return string
	 */
    public String toScript() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("[inputSetting: ").append(this.inputSettingProp.toScript());
    	if (this.outputSettingProp != null) {
    		sb.append(", outputSetting: ").append(this.outputSettingProp.toScript());
    	}
    	sb.append("]");
    	return sb.toString();
    }

	/**
	 * Do the counting process about this counting unit.
	 * @param buildAction as build action.
	 * @param workspace plugin's workspace.
	 * @param vars plugin's environment.
	 * @param logger plugin's log writer.
	 * @return true if this task has successed.
	 * @throws IOException signal tells that io error is occured.
	 * @throws InterruptedException signal tells that this task is interrupted.
	 */
	protected boolean doCountingTask(final StepCountBuildAction buildAction, final FilePath workspace,
			final EnvVars vars, final PrintStream logger) throws IOException, InterruptedException {

		//syslogger.println("[DEBUG] CountingUnit#doCountingTask()");
		if (this.inputSettingProp == null) {
			logger.println("[keisuke] inputSetting is null.");
			return false;
		}
		//syslogger.println("[DEBUG] unitName = " + this.inputSettingProp.getUnitName());
		KeisukeCountCallable call = new KeisukeCountCallable(this, logger);
		BuildResult result = workspace.act(call); // do call.invoke() at workspace(master or slave)
		result.setInputSetting(this.inputSettingProp);
		buildAction.put(this.inputSettingProp.getUnitName(), result);
		logger.println("[keisuke] count " + result.getFileSteps().size() + " files "
				+ "and total sum is " + result.getStepsSum() + " lines.");
		if (result.isDiffExist()) {
			logger.println("[keisuke] diff count " + result.getDiffSteps().size() + " files "
					+ "and total added steps is " + result.getAddedSum() + " lines "
					+ "and total deleted steps is " + result.getDeletedSum() + " lines.");
		}
		return true;
	}

	/**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//syslogger.println("[DEBUG] CountingUnit#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

    /**
	 * a Descriptor internal class for counting unit.
	 * this will be used for Job Configuration.
	 */
    @Extension
    public static class DescriptorImpl extends Descriptor<CountingUnit> {
    	//private transient PrintStream syslogger = System.out;
    }

}
