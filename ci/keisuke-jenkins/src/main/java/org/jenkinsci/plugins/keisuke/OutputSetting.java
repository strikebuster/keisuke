package org.jenkinsci.plugins.keisuke;

import static keisuke.count.util.EncodeUtil.unicodeEscape;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

import keisuke.count.FormatEnum;
import keisuke.count.option.StepCountOption;

/**
 * Data bound class with Jenkins config UI.
 * This has values of Output Setting
 */
public class OutputSetting extends AbstractDescribableImpl<OutputSetting> implements Serializable {
	private static final long serialVersionUID = 4L; // since ver.2.0.0

	@SuppressWarnings("unused")
	private transient PrintStream syslogger = System.out;

	private static final String DEFAULT_FORMAT = "text";
	private static ListBoxModel outputFormatItems = new ListBoxModel();
	private static Map<String, String> formatExtensionMap = new HashMap<String, String>();
	static {
		// count format values
		for (FormatEnum format : StepCountOption.STEPCOUNT_FORMATS) {
			// (label, value) of listbox
			outputFormatItems.add(format.label(), format.value());
			// map of (format value, file extension)
			formatExtensionMap.put(format.value(), format.fileExtension());
		}
	}

	private String formatType = DEFAULT_FORMAT;
	private boolean pathStyleIsBase = true;
	private String filePath = "";
	private boolean diffEnabled = false;
	private OutputSettingForDiff diffOutputSettingProp = null;

	/**
	 * Constructor.
	 * @param outputFilePath input string of &lt;f:entry field="outputFilePath"&gt;.
	 * @param outputFormat input string of &lt;f:entry field="outputFormat"&gt;.
	 * @throws RuntimeException occurs when the parameter "outputFilePath" is null or empty.
	 */
	@DataBoundConstructor
	public OutputSetting(final String outputFilePath, final String outputFormat) {
		if (outputFilePath == null || outputFilePath.isEmpty()) {
			throw new RuntimeException("Output file path is required when you ask to save results.");
		}
		//syslogger.println("[DEBUG] OutputSetting#OutputSetting(" + outputFilePath
		//		+ "," + outputFormat + ")");
		this.filePath = outputFilePath;
		if (outputFormat != null && !outputFormat.isEmpty()) {
			this.formatType = outputFormat;
		}
	}

	/**
	 * getter of &lt;f:entry field="outputFilePath"&gt;.
	 * @return input string of outputFilePath.
	 */
	@Nonnull
	public String getOutputFilePath() {
		//syslogger.println("[DEBUG] OutputSetting#getOutputFilePath()=" + this.filePath);
		return this.filePath;
	}

	/**
	 * getter of &lt;f:entry field="outputFormat"&gt;.
	 * @return input string of outputFormat.
	 */
	@Nonnull
	public String getOutputFormat() {
		//syslogger.println("[DEBUG] OutputSetting#getOutputFormat()=" + this.formatType);
		return this.formatType;
	}

	/**
	 * get boolean value which means countingMode is diff_too.
	 * @return boolean value.
	 */
	public boolean isDiffEnabled() {
		//syslogger.println("[DEBUG] OutputSetting#isDiffEnabled()=" + this.diffEnabled);
		return this.diffEnabled;
	}

	/**
	 * set boolean value which means countingMode is diff_too.
	 * @param bool if countingMode is diff_too, you set true.
	 */
	protected void setDiffEnabled(final boolean bool) {
		this.diffEnabled = bool;
	}

	/**
	 * getter for &lt;f:optionalProperty field="diffOutputSetting"&gt; in config.jelly.
	 * @return OutputSettingForDiff values property.
	 */
	public OutputSettingForDiff getDiffOutputSetting() {
		return this.diffOutputSettingProp;
	}

	/**
	 * setter for &lt;f:optionalProperty field="diffOutputSetting"&gt; in config.jelly.
	 * @param diffOutputSetting OutputSettingForDiff values property.
	 * @return OutputSetting instance myself.
	 */
	@DataBoundSetter
	public OutputSetting setDiffOutputSetting(final OutputSettingForDiff diffOutputSetting) {
		if (diffOutputSetting == null) {
			this.setDiffEnabled(false);
		} else {
			this.setDiffEnabled(true);
		}
		this.diffOutputSettingProp = diffOutputSetting;
		return this;
	}

	/**
	 * getter for &lt;f:entry filed="baseDirInclusion"&gt; in config.jelly.
	 * @return if notes path including base directory name, then true.
	 */
	public boolean isBaseDirInclusion() {
		return this.pathStyleIsBase;
	}

	/*
	 * setter for &lt;f:entry filed="baseDirInclusion"&gt; in config.jelly.
	 * @param baseDirInclusion if notes path including base directory name, then "true" or "checked".
	 * @return OutputSetting instance myself.
	 *
	@DataBoundSetter
	public OutputSetting setBaseDirInclusion(final String baseDirInclusion) {
		if (baseDirInclusion == null) {
			this.pathStyleIsBase = false;
		} else if (baseDirInclusion.equals("checked") || baseDirInclusion.equals("true")) {
			this.pathStyleIsBase = true;
		} else {
			this.pathStyleIsBase = false;
		}
		return this;
	}*/

	/**
	 * setter for &lt;f:entry filed="baseDirInclusion"&gt; in config.jelly.
	 * @param baseDirInclusion if notes path including base directory name, then true.
	 * @return OutputSetting instance myself.
	 */
	@DataBoundSetter
	public OutputSetting setBaseDirInclusion(final boolean baseDirInclusion) {
		this.pathStyleIsBase = baseDirInclusion;
		return this;
	}

	/**
     * Gets string for printing the value of this class
     * @return string
     */
	@Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"outputFilePath\":\"").append(unicodeEscape(this.getOutputFilePath()))
    		.append("\",\"outputFormat\":\"").append(this.getOutputFormat())
    		.append("\",\"baseDirInclusion\":\"").append(this.isBaseDirInclusion())
    		.append("\"");
    	if (this.diffOutputSettingProp != null) {
    		sb.append(",\"diffOutputSetting\":").append(this.getDiffOutputSetting());
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
    	sb.append("[outputFilePath: '").append(this.filePath)
    		.append("' ,outputFormat: '").append(this.formatType)
    		.append("' ,baseDirInclusion: ").append(this.pathStyleIsBase);
    		//.append("' ,baseDirInclusion: '").append(this.pathStyleIsBase)
    		//.append("'");
    	if (this.diffOutputSettingProp != null) {
    		sb.append(", diffOutputSetting: ").append(this.diffOutputSettingProp.toScript());
    	}
    	sb.append("]");
    	return sb.toString();
	}

	/**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//syslogger.println("[DEBUG] OutputSetting#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor internal class for output file.
	 * this will be used for Job Configuration.
	 */
    @Extension
    public static class DescriptorImpl extends Descriptor<OutputSetting> {
    	//private transient PrintStream syslogger = System.out;

        /**
         * Gets the instance of ListBoxModel for &lt;f:select name="outputFormat"&gt;
         * @return instance of ListBoxModel
         */
        public ListBoxModel doFillOutputFormatItems() {
    		return OutputSetting.outputFormatItems;
    	}

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="outputFilePath"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckOutputFilePath(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] OutputSetting.DescriptorImpl#doCheckOutputFilePath(" + value + ")");
        	if (value == null || value.isEmpty()) {
                return FormValidation.error(Messages.errorOutputFilePathRequired());
            }
        	File file = new File(value);
        	try {
	        	if (file.canWrite() || !file.exists()) {
	        		return FormValidation.ok();
	        	}
        	} catch (SecurityException ex) {
        		// ignore exception
        	}
        	return FormValidation.error(Messages.errorOutputFilePathNotWritable());
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="outputFormat"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckOutputFormat(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] OutputSetting.DescriptorImpl#doCheckOutputFormat(" + value + ")");
        	if (value == null || value.isEmpty() || !isSupportedFormat(value)) {
                return FormValidation.error(Messages.errorUnsupportedFormat());
            }
        	return FormValidation.ok();
        }

        private boolean isSupportedFormat(final String format) {
        	for (ListBoxModel.Option element : OutputSetting.outputFormatItems) {
        		if (element.value.equals(format)) {
        			return true;
        		}
        	}
        	return false;
        }

    }
}
