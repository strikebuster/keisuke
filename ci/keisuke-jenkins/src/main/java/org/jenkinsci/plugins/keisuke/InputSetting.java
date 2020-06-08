package org.jenkinsci.plugins.keisuke;

import static keisuke.count.util.EncodeUtil.unicodeEscape;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;

import org.jenkinsci.plugins.keisuke.util.CheckUtil;
import org.jenkinsci.plugins.keisuke.util.EncodeUtil;

/**
 * Data bound class with Jenkins config UI.
 * This has values of Input Setting which means InputCategory
 */
public class InputSetting extends AbstractDescribableImpl<InputSetting> implements Serializable {
	private static final long serialVersionUID = 10L; // since ver.2.0.0

	@SuppressWarnings("unused")
	private transient PrintStream syslogger = System.out;

	private static final CountingModeEnum DEFAULT_MODE = CountingModeEnum.ONLY_STEP_SIMPLY;
	private static final String DEFAULT_FILEPATTERN = "";
	private static final String DEFAULT_FILEPATTERN_EXCLUDE = "";

	private String unit = "default";
	private String sourceDir = ".";
	private String oldSourceDir = "";
	private String includeFilesPattern = DEFAULT_FILEPATTERN;
	private String excludeFilesPattern = DEFAULT_FILEPATTERN_EXCLUDE;
	private String sourceEncoding = "";
	private String xmlFilePath = "";

	private CountingModeEnum countMode = DEFAULT_MODE;

    /**
     * constructor.
     * @param unitName input string of &lt;f:entry field="unitName"&gt;.
     * @param sourceDirectory input string of &lt;f:entry field="sourceDirectory"&gt;.
     * @param encoding input string of &lt;f:entry field="encoding"&gt;.
     * @param xmlPath input string of &lt;f:entry field="xmlPath"&gt;.
     * @param countingMode radio value of &lt;f:radioBlock name="countinMode"&gt;.
     */
    @DataBoundConstructor
    public InputSetting(final String unitName, final String sourceDirectory, final String encoding,
    		final String xmlPath, final String countingMode) {
    	//syslogger.println("[DEBUG] InputSetting#InputSetting(" + unitName + "," + sourceDirectory + ","
    	//		+ encoding + "," + xmlPath + "," + countingMode + ")");
    	if (unitName != null) {
    		this.unit = unitName;
    	}
    	if (sourceDirectory != null && !sourceDirectory.isEmpty()) {
    		this.sourceDir = sourceDirectory;
    	}
       if (encoding != null) {
        	this.sourceEncoding = encoding;
        }
       if (xmlPath != null) {
        	this.xmlFilePath = xmlPath;
        }
       if (countingMode == null) {
    	   this.countMode = DEFAULT_MODE;
       } else if (countingMode.equals(CountingModeEnum.ONLY_STEP_SIMPLY.getValue())) {
    	   this.countMode = CountingModeEnum.ONLY_STEP_SIMPLY;
       } else if (countingMode.equals(CountingModeEnum.ONLY_STEP_USING_FILE_SET.getValue())) {
    	   this.countMode = CountingModeEnum.ONLY_STEP_USING_FILE_SET;
       } else if (countingMode.equals(CountingModeEnum.BOTH_STEP_AND_DIFF.getValue())) {
    	   this.countMode = CountingModeEnum.BOTH_STEP_AND_DIFF;
       } else {
    	   this.countMode = DEFAULT_MODE;
       }
    }

    /**
     * Sets properties of file_set as countingMode,
     * and return this instance.
     * @param includePattern input string of &lt;f:entry field="includePattern"&gt;.
     * @param excludePattern input string of &lt;f:entry field="excludePattern"&gt;.
     * @return this instance.
     */
    public InputSetting setItemsOfFileSet(final String includePattern, final String excludePattern) {
    	this.setIncludePattern(includePattern);
    	this.setExcludePattern(excludePattern);
    	return this;
    }

    /**
     * Sets properties of diff_too as countingMode,
     * and return this instance.
     * @param oldSourceDirectory input string of &lt;f:entry field="oldSourceDirectory"&gt;.
     * @return this instance.
     */
    public InputSetting setItemsOfDiffToo(final String oldSourceDirectory) {
    	this.setOldSourceDirectory(oldSourceDirectory);
    	return this;
    }

    /**
     * Sets value of &lt;input name="_.includePattern"&gt;
     * @param includePattern value of &lt;input name="_.includePattern"&gt;
     */
    @DataBoundSetter
    public void setIncludePattern(final String includePattern) {
    	if (includePattern != null) {
    		this.includeFilesPattern = includePattern;
    	}
    }

    /**
     * Sets value of &lt;input name="_.excludePattern"&gt;
     * @param excludePattern value of &lt;input name="_.excludePattern"&gt;
     */
    @DataBoundSetter
    public void setExcludePattern(final String excludePattern) {
    	if (excludePattern != null) {
    		this.excludeFilesPattern = excludePattern;
    	}
    }

    /**
     * Sets value of &lt;input name="_.oldSourceDirectory"&gt;
     * @param oldSourceDirectory value of &lt;input name="_.oldSourceDirectory"&gt;
     */
    @DataBoundSetter
    public void setOldSourceDirectory(final String oldSourceDirectory) {
    	if (oldSourceDirectory != null && !oldSourceDirectory.isEmpty()) {
 		   this.oldSourceDir = oldSourceDirectory;
 	   }
    }
    /**
	 * getter of &lt;f:entry field="unitName"&gt;.
	 * @return input string of unitName.
	 */
    @Nonnull
    public String getUnitName() {
    	//syslogger.println("[DEBUG] InputSetting#getUnitName()="
    	//		+ this.unit);
    	return this.unit;
    }

    /**
   	 * getter of &lt;f:entry field="sourceDirectory"&gt;.
   	 * @return input string of sourceDirectory.
   	 */
    @Nonnull
	public String getSourceDirectory() {
		//syslogger.println("[DEBUG] InputSetting#getSourceDirectory()="
		//		+ this.sourceDir);
    	return this.sourceDir;
	}

    /**
     * Gets CountingModeEnum element.
     * @return selected element of countingModeEnum.
     */
    public CountingModeEnum getCountingModeEnum() {
    	//syslogger.println("[DEBUG] InputSetting#getCountingModeEnum()="
    	//		+ this.countingMode);
    	return this.countMode;
    }

    /**
	 * getter of &lt;f:radioBlock name="countingMode"&gt;.
	 * @return radio value of countingMode.
	 */
    @Nonnull
    public String getCountingMode() {
    	//syslogger.println("[DEBUG] InputSetting#getCountingMode()="
    	//		+ this.countingMode.getValue());
    	return this.countMode.getValue();
    }

    /**
	 * getter of &lt;f:entry field="includePattern"&gt;.
	 * @return input string of includePattern.
	 */
    @Nonnull
    public String getIncludePattern() {
    	//syslogger.println("[DEBUG] InputSetting#getIncludePattern()="
    	//		+ this.includeFilesPattern);
    	return this.includeFilesPattern;
    }

    /**
	 * getter of &lt;f:entry field="excludePattern"&gt;.
	 * @return input string of excludePattern.
	 */
    @Nonnull
    public String getExcludePattern() {
    	//syslogger.println("[DEBUG] InputSetting#getExcludePattern()="
    	//		+ this.excludeFilesPattern);
    	return this.excludeFilesPattern;
    }

    /**
   	 * getter of &lt;f:entry field="oldSourceDirectory"&gt;.
   	 * @return input string of oldSourceDirectory.
   	 */
    @Nonnull
	public String getOldSourceDirectory() {
		//syslogger.println("[DEBUG] InputSetting#getOldSourceDirectory()="
		//		+ this.oldSourceDir);
    	return this.oldSourceDir;
	}

    /**
	 * getter of &lt;f:entry field="encoding"&gt;.
	 * @return input string of encoding.
	 */
    @Nonnull
    public String getEncoding() {
    	//syslogger.println("[DEBUG] InputSetting#getEncoding()="
    	//		+ this.sourceEncoding);
    	return this.sourceEncoding;
    }

    /**
	 * getter of &lt;f:entry field="xmlPath"&gt;.
	 * @return input string of xmlPath.
	 */
    @Nonnull
    public String getXmlPath() {
    	//syslogger.println("[DEBUG] InputSetting#getXmlPath()="
    	//		+ this.xmlFilePath);
    	return this.xmlFilePath;
    }

    /**
     * Gets string for printing the value of this class
     * @return string
     */
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"unitName\":\"").append(this.getUnitName())
    		.append("\",\"sourceDirectory\":\"").append(unicodeEscape(this.getSourceDirectory()))
    		.append("\",\"encoding\":\"").append(this.getEncoding())
    		.append("\",\"xmlPath\":\"").append(unicodeEscape(this.getXmlPath()))
    		.append("\",\"countingMode\":\"").append(this.getCountingMode())
    		.append("\",\"includePattern\":\"").append(unicodeEscape(this.getIncludePattern()))
    		.append("\",\"excludePattern\":\"").append(unicodeEscape(this.getExcludePattern()))
    		.append("\",\"oldSourceDirectory\":\"").append(unicodeEscape(this.getOldSourceDirectory()))
    		.append("\"}");
    	return sb.toString();
    }

    /**
	 * Gets string for Pipeline configuration script
	 * @return string
	 */
    public String toScript() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("[unitName: '").append(this.unit)
    		.append("' ,sourceDirectory: '").append(this.sourceDir)
    		.append("' ,encoding: '").append(this.sourceEncoding)
    		.append("' ,xmlPath: '").append(this.xmlFilePath)
    		.append("' ,countingMode: '").append(this.getCountingMode());
    	if (this.countMode.isFileSet()) {
    		sb.append("' ,excludePattern: '").append(this.excludeFilesPattern)
    			.append("' ,includePattern: '").append(this.includeFilesPattern);
    	} else if (this.countMode.isDiffToo()) {
    		sb.append("' ,oldSourceDirectory: '").append(this.oldSourceDir);
    	}
    	sb.append("']");
    	return sb.toString();
    }

    /**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//syslogger.println("[DEBUG] InputSetting#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

    /**
	 * a Descriptor internal class for target file set.
	 * this will be used for Job Configuration.
	 */
    @Extension
    public static class DescriptorImpl extends Descriptor<InputSetting> {
    	//private transient PrintStream syslogger = System.out;

    	/**
    	 * Gets default encoding defined global setting of KeisukePublisher.
    	 * @return default encoding name.
    	 */
    	public String getDefaultEncoding() {
    		KeisukePublisher.DescriptorImpl descriptor
    			= Jenkins.get().getDescriptorByType(KeisukePublisher.DescriptorImpl.class);
    		if (descriptor == null || descriptor.getGlobalSetting() == null) {
    			return "";
    		}
    		return descriptor.getGlobalSetting().getEncoding();
    	}

    	/**
    	 * Gets default xml file path defined global setting of KeisukePublisher.
    	 * @return default xml file path.
    	 */
    	public String getDefaultXmlPath() {
    		KeisukePublisher.DescriptorImpl descriptor
    			= Jenkins.get().getDescriptorByType(KeisukePublisher.DescriptorImpl.class);
    		if (descriptor == null || descriptor.getGlobalSetting() == null) {
    			return "";
    		}
    		return descriptor.getGlobalSetting().getXmlPath();
    	}

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="unitName"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckUnitName(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckUnitName(" + value + ")");
        	if (value == null || value.isEmpty()) {
        		return FormValidation.error(Messages.errorUnitNameRequired());
        	} else if (!CheckUtil.checkStringForCategory(value)) {
        		return FormValidation.error(Messages.errorUnitName());
        	}
        	return FormValidation.ok();
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="sourceDirectory"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckSourceDirectory(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckSourceDirectory(" + value + ")");
        	if (value == null || value.isEmpty()) {
        		return FormValidation.error(Messages.errorSourceDirRequired());
        	}
        	return FormValidation.ok();
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="oldSourceDirectory"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckOldSourceDirectory(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckOldSourceDirectory(" + value + ")");
        	if (value == null || value.isEmpty()) {
        		return FormValidation.error(Messages.errorSourceDirRequired());
        	}
        	return FormValidation.ok();
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="includePattern"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckIncludePattern(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckIncludePattern(" + value + ")");
        	return FormValidation.ok();
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="excludePattern"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckExcludePattern(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckExcludePattern(" + value + ")");
        	return FormValidation.ok();
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="encoding"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckEncoding(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckEncoding(" + value + ")");
        	if (value == null || value.isEmpty()) {
        		FormValidation.error(Messages.errorEncodingRequired());
        	} else if (EncodeUtil.isSupportedEncoding(value)) {
        		return FormValidation.ok();
        	}
        	return FormValidation.error(Messages.errorUnsupportedEncoding());
        }

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="xmlPath"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckXmlPath(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] InputSetting.DescriptorImpl#doCheckXmlPath(" + value + ")");
        	if (value == null || value.isEmpty()) {
        		return FormValidation.ok();
        	}
        	File file = new File(value);
        	try {
	        	if (file.isFile()) {
	        		return FormValidation.ok();
	        	}
        	} catch (SecurityException ex) {
        		// ignore exception
        	}
        	return FormValidation.error(Messages.errorXmlFileNotFound());
        }
    }
}
