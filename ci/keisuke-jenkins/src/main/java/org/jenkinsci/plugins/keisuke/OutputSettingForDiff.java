package org.jenkinsci.plugins.keisuke;

import static keisuke.count.util.EncodeUtil.unicodeEscape;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import keisuke.count.FormatEnum;
import keisuke.count.option.DiffCountOption;

/**
 * Data bound class with Jenkins config UI.
 * This has values of Diff Output Setting
 */
public class OutputSettingForDiff extends AbstractDescribableImpl<OutputSettingForDiff> implements Serializable {

	private static final long serialVersionUID = 0L; // since ver.2.0.0
	private transient PrintStream syslogger = System.out;

	private static final String DEFAULT_FORMAT = "text";
	private static ListBoxModel outputFormatItems = new ListBoxModel();
	private static Map<String, String> formatExtensionMap = new HashMap<String, String>();
	static {
		// count format values
		for (FormatEnum format : DiffCountOption.DIFFCOUNT_FORMATS) {
			// (label, value) of listbox
			outputFormatItems.add(format.label(), format.value());
			// map of (format value, file extension)
			formatExtensionMap.put(format.value(), format.fileExtension());
		}
	}

	private String formatType = DEFAULT_FORMAT;
	private String filePath = "";

	/**
	 * Constructor.
	 * @param diffOutputFilePath input string of &lt;f:entry field="diffOutputFilePath"&gt;.
	 * @param diffOutputFormat input string of &lt;f:entry field="diffOutputFormat"&gt;.
	 */
	@DataBoundConstructor
	public OutputSettingForDiff(final String diffOutputFilePath, final String diffOutputFormat) {
		if (diffOutputFilePath == null || diffOutputFilePath.isEmpty()) {
			throw new RuntimeException("Output file path is required when you ask to save results.");
		}
		//syslogger.println("[DEBUG] OutputSettingForDiff#DiffOutputSetting(" + diffOutputFilePath
		//		+ "," + diffOutputFormat + ")");
		this.filePath = diffOutputFilePath;
		if (diffOutputFormat != null && !diffOutputFormat.isEmpty()) {
			this.formatType = diffOutputFormat;
		}
	}

	/**
	 * getter of &lt;f:entry field="diffOutputFilePath"&gt;.
	 * @return input string of diffOutputFilePath.
	 */
	@Nonnull
	public String getDiffOutputFilePath() {
		//syslogger.println("[DEBUG] OutputSettingForDiff#getDiffOutputFilePath()=" + this.filePath);
		return this.filePath;
	}

	/**
	 * getter of &lt;f:entry field="diffOutputFormat"&gt;.
	 * @return input string of diffOutputFormat.
	 */
	@Nonnull
	public String getDiffOutputFormat() {
		//syslogger.println("[DEBUG] OutputSettingForDiff#getDiffOutputFormat()=" + this.formatType);
		return this.formatType;
	}

	/**
     * Gets string for printing the value of this class
     * @return string
     */
	@Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"diffOutputFilePath\":\"").append(unicodeEscape(this.getDiffOutputFilePath()))
    		.append("\",\"diffOutputFormat\":\"").append(this.getDiffOutputFormat())
    		.append("}");
    	return sb.toString();
    }

	/**
	 * Gets string for Pipeline configuration script
	 * @return string
	 */
	public String toScript() {
		StringBuffer sb = new StringBuffer();
    	sb.append("[diffOutputFilePath: '").append(this.filePath)
    		.append("' ,diffOutputFormat: '").append(this.formatType)
    		.append("']");
    	return sb.toString();
	}

	/**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//syslogger.println("[DEBUG] OutputSettingForDiff#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor internal class for diff output file.
	 * this will be used for Job Configuration.
	 */
    @Extension
    public static class DescriptorImpl extends Descriptor<OutputSettingForDiff> {
    	//private transient PrintStream syslogger = System.out;

        /**
         * Gets the instance of ListBoxModel for &lt;f:select name="diffOutputFormat"&gt;
         * @return instance of ListBoxModel
         */
        public ListBoxModel doFillDiffOutputFormatItems() {
    		return OutputSettingForDiff.outputFormatItems;
    	}

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="diffOutputFilePath"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckDiffOutputFilePath(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] OutputSettingForDiff.DescriptorImpl#doCheckDiffOutputFilePath("
        	//		+ value + ")");
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
         * @param value input string of &lt;f:entry field="diffOutputFormat"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckDiffOutputFormat(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] OutputSettingForDiff.DescriptorImpl#doCheckDiffOutputFormat("
        	//		+ value + ")");
        	if (value == null || value.isEmpty() || !isSupportedFormat(value)) {
                return FormValidation.error(Messages.errorUnsupportedFormat());
            }
        	return FormValidation.ok();
        }

        private boolean isSupportedFormat(final String format) {
        	for (ListBoxModel.Option element : OutputSettingForDiff.outputFormatItems) {
        		if (element.value.equals(format)) {
        			return true;
        		}
        	}
        	return false;
        }

    }
}
