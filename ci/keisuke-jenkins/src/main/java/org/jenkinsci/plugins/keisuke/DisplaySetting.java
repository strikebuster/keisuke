package org.jenkinsci.plugins.keisuke;

import static keisuke.count.util.EncodeUtil.unicodeEscape;

import java.io.PrintStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.keisuke.util.DisplayStepKindUtil;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
//import hudson.util.ListBoxModel;

/**
 * Data bound class with Jenkins config UI.
 * This has values of Display Setting
 */
public class DisplaySetting extends AbstractDescribableImpl<DisplaySetting> implements Serializable {
	private static final long serialVersionUID = 2L; // since ver.2.0.0

	@SuppressWarnings("unused")
	private transient PrintStream syslogger = System.out;

	private static final DisplayStepKindEnum DEFAULT_KIND = DisplayStepKindEnum.CODE;
	//private static ListBoxModel displayStepKindItems = DisplayStepKindUtil.createSelectItems();
	private static DisplayStepKindEnum[] displayStepKindArray = DisplayStepKindEnum.values();
	private DisplayStepKindEnum displayKind = DEFAULT_KIND;

	/**
	 * Constructor.
	 * @param displayStepKind input string of &lt;f:entry field="displayStepKind"&gt;.
	 */
	@DataBoundConstructor
	public DisplaySetting(final String displayStepKind) {
		if (displayStepKind == null) {
			//syslogger.println("[DEBUG] DisplaySetting#DisplaySetting(null)");
			return;
		}
		//syslogger.println("[DEBUG] DisplaySetting#DisplaySetting(" + displayStepKind + ")");
		this.setDisplayStepKind(displayStepKind);
	}

	private void setDisplayStepKindEnum(final DisplayStepKindEnum kind) {
		if (kind == null) {
			return;
		}
		this.displayKind = kind;
	}

	/**
	 * setter of string value of &lt;f:entry field="displayStepKind"&gt;.
	 * @param displayStepKind string value of DisplayStepKindEnum
	 */
	//@DataBoundSetter
	public void setDisplayStepKind(final String displayStepKind) {
		//syslogger.println("[DEBUG] DisplaySetting#setDisplayStepKind(" + displayStepKind + ")");
		if (DisplayStepKindEnum.CODE.getValue().equals(displayStepKind)) {
			this.setDisplayStepKindEnum(DisplayStepKindEnum.CODE);
		} else if (DisplayStepKindEnum.CODE_AND_COMMENT.getValue().equals(displayStepKind)) {
			this.setDisplayStepKindEnum(DisplayStepKindEnum.CODE_AND_COMMENT);
		} else if (DisplayStepKindEnum.CODE_AND_COMMENT_AND_BLANK.getValue().equals(displayStepKind)) {
			this.setDisplayStepKindEnum(DisplayStepKindEnum.CODE_AND_COMMENT_AND_BLANK);
		} else {
			this.setDisplayStepKindEnum(DEFAULT_KIND);
		}
	}

	/**
	 * Gets DisplayStepKindEnum element.
	 * @return selected element of displayStepKindEnum.
	 */
	public DisplayStepKindEnum getDisplayStepKindEnum() {
		//syslogger.println("[DEBUG] DisplaySetting#getDisplayStepKindEnum()=" + this.displayKind);
		return this.displayKind;
	}

	/**
	 * getter of string value of &lt;f:entry field="displayStepKind"&gt;.
	 * @return selected value of displayStepKind.
	 */
	@Nonnull
	public String getDisplayStepKind() {
		//syslogger.println("[DEBUG] DisplaySetting#getDisplayStepKind()=" + this.displayKind.getValue());
		return this.displayKind.getValue();
	}

	/**
     * Gets string for printing the value of this class
     * @return string
     */
	@Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("{\"displayStepKind\":\"").append(unicodeEscape(this.displayKind.toString()))
    		.append("\"}");
    	return sb.toString();
    }

	/**
	 * Gets string for Pipeline configuration script
	 * @return string
	 */
	public String toScript() {
		StringBuffer sb = new StringBuffer();
    	sb.append("[displayStepKind: '").append(this.displayKind.getValue())
    		.append("']");
    	return sb.toString();
	}

	/**
	 * Gets the descriptor for this instance.
	 * @return descriptor instance
	 */
	@Override
	public DescriptorImpl getDescriptor() {
		//syslogger.println("[DEBUG] DisplaySetting#getDescriptor()");
		return (DescriptorImpl) super.getDescriptor();
	}

	/**
	 * Descriptor internal class for display the results.
	 * this will be used for Job Configuration.
	 */
    @Extension
    public static class DescriptorImpl extends Descriptor<DisplaySetting> {
    	//private transient PrintStream syslogger = System.out;

    	/**
    	 * Gets an array of displayStepKind's enumeration
    	 * @return array of DisplayStepKindEnum
    	 */
    	@Nonnull
    	public static DisplayStepKindEnum[] getAllDisplayStepKinds() {
    		//syslogger.println("[DEBUG] DisplaySetting.DescriptorImpl#getAllDisplayStepKinds()");
    		return DisplaySetting.displayStepKindArray;
    	}

    	/*
         * Gets the instance of ListBoxModel for &lt;f:select name="displayStepKind"&gt;
         * @return instance of ListBoxModel
         *
        public ListBoxModel doFillDisplayStepKindItems() {
        	//syslogger.println("[DEBUG] DisplaySetting.DescriptorImpl#doFillDisplayStepKindItems()");
    		return DisplaySetting.displayStepKindItems;
    	}*/

        /**
         * do on-the-fly validation of the form field.
         * @param value input string of &lt;f:entry field="displayStepKind"&gt;.
         * @return outcome of the validation.
         */
        public FormValidation doCheckDisplayStepKind(@QueryParameter final String value) {
        	//syslogger.println("[DEBUG] DisplaySetting.DescriptorImpl#doCheckDisplayStepKind(" + value + ")");
        	if (value == null || value.isEmpty() || !isSupportedKind(value)) {
                return FormValidation.error(Messages.errorUnsupportedDisplayStepKind());
            }
        	return FormValidation.ok();
        }

        private boolean isSupportedKind(final String kindValue) {
        	return DisplayStepKindUtil.existsAsDisplayStepKind(kindValue);
        }

    }
}
