package org.jenkinsci.plugins.keisuke;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;

import org.jenkinsci.plugins.keisuke.util.EncodeUtil;

/**
 * Data bound class with Jenkins config UI.
 * This has values of Global Setting
 */
public class GlobalSetting extends AbstractDescribableImpl<GlobalSetting> implements Serializable {

	private static final long serialVersionUID = 3L; // since ver.2.0.0
	private transient PrintStream syslogger = System.out;

	private String sourceEncoding = System.getProperty("file.encoding");
	private String xmlFilePath = "";

	/**
	 * constructor.
	 * @param encoding input string of &lt;f:entry field="encoding"&gt;.
	 * @param xmlPath  input string of &lt;f:entry field="xmlPath"&gt;.
	 */
	@DataBoundConstructor
	public GlobalSetting(final String encoding, final String xmlPath) {
		//syslogger.println("[DEBUG] GlobalSetting#GlobalSetting(" + encoding + ")");
		this.setEncoding(encoding);
		this.setXmlPath(xmlPath);
	}

	/**
	 * getter for encoding.
	 * @return input string of encoding.
	 */
	@Nonnull
    public String getEncoding() {
    	//syslogger.println("[DEBUG] GlobalSetting#getEncoding():" + this.sourceEncoding);
        return this.sourceEncoding;
    }

    /**
     * setter for encoding
     * @param encoding encoding name
     */
    private void setEncoding(final String encoding) {
    	if (encoding != null && !encoding.isEmpty()) {
        	this.sourceEncoding = encoding;
        }
    }

    /**
     * getter for xml file path.
     * @return input string of xml file path.
     */
    @Nonnull
    public String getXmlPath() {
    	//syslogger.println("[DEBUG] GlobalSetting#getXmlPath():" + this.xmlFilePath);
    	return this.xmlFilePath;
    }

    /**
     * setter for xml file path.
     * @param xmlPath xml file path.
     */
    private void setXmlPath(final String xmlPath) {
    	if (xmlPath != null && !xmlPath.isEmpty()) {
        	this.xmlFilePath = xmlPath;
        }
    }

    /**
     * Gets string for printing the value of this class
     * @return string
     */
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{encoding:\"").append(this.sourceEncoding)
    		.append("\",xmlPath:\"").append(this.xmlFilePath)
    		.append("\"}");
    	return sb.toString();
    }

    /**
	 * Gets string for Pipeline configuration script
	 * @return string
	 */
	public String toScript() {
		StringBuffer sb = new StringBuffer();
    	sb.append("[encoding: '").append(this.sourceEncoding)
    		.append("' ,xmlPath:'").append(this.xmlFilePath)
    		.append("']");
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
    public static class DescriptorImpl extends Descriptor<GlobalSetting> {
    	//private transient PrintStream syslogger = System.out;
    	/**
    	 * do on-the-fly validation of the form field.
		 * validate global value field "encoding".
		 * @param value input string of &lt;f:entry field="encoding"&gt;.
		 * @return outcome of the validation.
		 */
        public FormValidation doCheckEncoding(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] GlobalSetting.DescriptorImpl#doCheckEncoding(" + value + ")");
        	if (value == null || value.isEmpty()) {
        		return FormValidation.ok();
        	}
        	if (EncodeUtil.isSupportedEncoding(value)) {
        		return FormValidation.ok();
        	}
            return FormValidation.error(Messages.errorUnsupportedEncoding());
        }

        /**
         * do on-the-fly validation of the form field.
         * validate global value field "xmlPath".
         * @param value input string of &lt;f:entry field="xmlPath"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckXmlPath(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] GlobalSetting.DescriptorImpl#doCheckXmlPath(" + value + ")");
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
