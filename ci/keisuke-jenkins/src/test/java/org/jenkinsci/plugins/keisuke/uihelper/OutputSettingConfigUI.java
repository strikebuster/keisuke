package org.jenkinsci.plugins.keisuke.uihelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI.RETRY_FOR_YUI;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfLabelFromCheckbox;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.keisuke.ProjectConfigUITester;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import keisuke.count.FormatEnum;

/**
 * UI helper for OutputSetting configuration of KeisukePublisher in project config page.
 */
public class OutputSettingConfigUI {

	public static final FormatEnum DEFAULT_FORMAT = FormatEnum.TEXT;
	private ProjectConfigUITester testOwner = null;

	/**
	 * Constructs an instance.
	 * @param owner ProjectConfigUITester instance.
	 */
	public OutputSettingConfigUI(final ProjectConfigUITester owner) {
		this.testOwner = owner;
	}

	/**
	 * Gets list of &lt;input type="checkbox" name="_.outputSetting"&gt;.
	 * @return List of DomElement.
	 */
	public List<DomElement> getListOfCheckboxForOutputSetting() {
		List<DomElement> checkboxList = this.testOwner.configPage().getElementsByName("_.outputSetting");
		if (checkboxList == null || checkboxList.isEmpty()) {
			fail("_.outputSetting not found.");
		}
		return checkboxList;
	}

	/**
	 * Finds the 1st &lt;input type="checkbox" name="_.outputSetting"&gt;
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findOutputCheckbox() {
		return this.findOutputCheckbox(0);
	}

	/**
	 * Finds the N-th &lt;input type="checkbox" name="_.outputSetting"&gt;
	 * @param index N-th number - 1.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findOutputCheckbox(final int index) {
		return (HtmlCheckBoxInput) this.testOwner.configHtmlUI().findInputProperty("outputSetting", index);
	}

	/**
	 * Finds the 1st &lt;input type="checkbox" name="_.outputSetting"&gt;,
	 * and verifies if that its checked status is expected boolean value.
	 * @param check boolean of checking status.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findOutputCheckboxThenVerify(final boolean check) {
		return this.findOutputCheckboxThenVerify(0, check);
	}

	/**
	 * Finds the N-th &lt;input type="checkbox" name="_.outputSetting"&gt;,
	 * and verifies if that its checked status is expected boolean value.
	 * @param index N-th number - 1.
	 * @param check boolean of checking status.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findOutputCheckboxThenVerify(final int index, final boolean check) {
		HtmlCheckBoxInput outputCheckbox = this.findOutputCheckbox(index);
		// ファイル出力チェックボックスのラベル
		String labelXPath = getXPathOfLabelFromCheckbox(outputCheckbox);
		HtmlLabel checkboxLabel = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			checkboxLabel = (HtmlLabel) this.testOwner.configPage().getFirstByXPath(labelXPath);
			if (checkboxLabel != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get checkbox lable retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		if (checkboxLabel == null) {
			fail("output setting checkbox label not found.");
		}
		//System.out.println("[TEST] checkboxLabel XPath:" + checkboxLabel.getCanonicalXPath());
		System.out.println("[TEST] checkboxLabel Content:" + checkboxLabel.getTextContent());
		assertThat(checkboxLabel.getTextContent(), not(isEmptyString()));
		// ファイル出力チェックボックスの初期状態は非選択
		String checkedValue = outputCheckbox.getAttribute("checked");
		System.out.println("[TEST] outputCheckbox checked:" + checkedValue);
		if (check) {
			assertThat(checkedValue, anyOf(equalTo("checked"), equalTo("true")));
		} else {
			assertThat(checkedValue, not(anyOf(equalTo("checked"), equalTo("true"))));
		}
		return outputCheckbox;
	}

	/**
	 * Checks on the 1st output setting by clicking the checkbox.
	 */
	public void checkOnOutputCheckbox() {
		this.checkOnOutputCheckbox(0);
	}

	/**
	 * Checks on the N-th output setting by clicking the checkbox.
	 * @param index N-th number - 1.
	 */
	public void checkOnOutputCheckbox(final int index) {
		HtmlCheckBoxInput outputCheckbox = this.findOutputCheckbox(index);
		if (outputCheckbox.isChecked()) {
			// すでにON
			return;
		}
		// ボタン押下
		System.out.println("[TEST] === click the outputSetting checkbox on:" + outputCheckbox.toString());
		try {
			outputCheckbox.click();
			this.testOwner.configHtmlUI().waitLongForBackgroundProcess();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
		String checked = outputCheckbox.getAttribute("checked");
		assertThat(checked, anyOf(equalTo("checked"), equalTo("true")));
	}

	/**
	 * Checks off the 1st output setting by clicking the checkbox.
	 */
	public void checkOffOutputCheckbox() {
		this.checkOffOutputCheckbox(0);
	}

	/**
	 * Checks off the N-th output setting by clicking the checkbox.
	 * @param index N-th number - 1.
	 */
	public void checkOffOutputCheckbox(final int index) {
		HtmlCheckBoxInput outputCheckbox = this.findOutputCheckbox(index);
		if (!outputCheckbox.isChecked()) {
			// すでにOFF
			return;
		}
		// ボタン押下
		System.out.println("[TEST] === click the outputSetting checkbox off:" + outputCheckbox.toString());
		try {
			outputCheckbox.click();
			this.testOwner.configHtmlUI().waitLongForBackgroundProcess();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
		String checked = outputCheckbox.getAttribute("checked");
		assertThat(checked, not(anyOf(equalTo("checked"), equalTo("true"))));
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.outputPath"&gt;.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findOutputFilePath() {
		return this.findOutputFilePath(0);
	}

	/**
	 * Finds n-th &lt;input type="text" name="_.outputPath"&gt;.
	 * @param index N-th number - 1.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findOutputFilePath(final int index) {
		return (HtmlTextInput) this.testOwner.configHtmlUI().findInputProperty("outputFilePath", index);
	}

	/**
	 * Finds 1st &lt;select name="_.outputFormat"&gt;.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findOutputFormat() {
		return this.findOutputFormat(0);
	}

	/**
	 * Finds N-th &lt;select name="_.outputFormat"&gt;.
	 * @param index N-th number - 1.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findOutputFormat(final int index) {
		return this.testOwner.configHtmlUI().findSelectProperty("outputFormat", index);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.outputFilePath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findOutputFilePathThenVerify(final String expected, final boolean check) {
		return this.findOutputFilePathThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.outputFilePath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findOutputFilePathThenVerify(final int index, final String expected,
			final boolean check) {
		return this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("outputFilePath", index, expected, check);
	}

	/**
	 * Finds 1st &lt;select name="_.outputFormat"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;select&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlSelect findOutputFormatThenVerify(final String expected, final boolean check) {
		return this.findOutputFormatThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;select name="_.outputFormat"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;select&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlSelect findOutputFormatThenVerify(final int index, final String expected, final boolean check) {
		return this.testOwner.configHtmlUI()
				.findSelectPropertyThenVerify("outputFormat", index, expected, check);
	}

	/**
	 * About 1st &lt;input type="text" name="_.outputFilePath"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfOutputFilePathAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfOutputFilePathAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;input type="text" name="_.outputFilePath"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfOutputFilePathAboutVisibility(final int index, final boolean visible) {
		HtmlInput input = this.testOwner.configHtmlUI().findInputProperty("outputFilePath", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("outputFilePath", input, visible);
	}

	/**
	 * About 1st &lt;select name="_.outputFormat"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfOutputFormatAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfOutputFormatAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;select name="_.outputFormat"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfOutputFormatAboutVisibility(final int index, final boolean visible) {
		HtmlSelect select = this.testOwner.configHtmlUI().findSelectProperty("outputFormat", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("outputFormat", select, visible);
	}

	/**
	 * Finds N-th &lt;input type="checkbox" name="_.baseDirInclusion"&gt;.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findBaseDirInclusion() {
		return this.findBaseDirInclusion(0);
	}

	/**
	 * Finds N-th &lt;input type="checkbox" name="_.baseDirInclusion"&gt;.
	 * @param index N-th number - 1.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findBaseDirInclusion(final int index) {
		return (HtmlCheckBoxInput) this.testOwner.configHtmlUI().findInputProperty("baseDirInclusion", index);
	}

	/**
	 * Finds the 1st &lt;input type="checkbox" name="_.baseDirInclusion"&gt;,
	 * and verifies if that its checked status is expected boolean value.
	 * @param check boolean of checking status.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findBaseDirInclusionThenVerify(final boolean check) {
		return this.findBaseDirInclusionThenVerify(0, check);
	}
	/**
	 * Finds the N-th &lt;input type="checkbox" name="_.baseDirInclusion"&gt;,
	 * and verifies if that its checked status is expected boolean value.
	 * @param index N-th number - 1.
	 * @param check boolean of checking status.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findBaseDirInclusionThenVerify(final int index, final boolean check) {
		return this.testOwner.configHtmlUI()
				.findCheckboxPropertyThenVerify("baseDirInclusion", index, check);
	}

	/**
	 * About 1st &lt;checkbox name="baseDirInclusion"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfBaseDirInclusionAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfBaseDirInclusionAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;checkbox name="baseDirInclusion"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfBaseDirInclusionAboutVisibility(final int index, final boolean visible) {
		HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) this.testOwner.configHtmlUI()
				.findInputProperty("baseDirInclusion", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("baseDirInclusion", checkbox, visible);
	}
}
