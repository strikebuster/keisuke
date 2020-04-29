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
 * UI helper for OutputSettingforDiff configuration of KeisukePublisher in project config page.
 */
public class OutputSettingForDiffConfigUI {

	public static final FormatEnum DEFAULT_FORMAT = FormatEnum.TEXT;
	private ProjectConfigUITester testOwner = null;

	/**
	 * Constructs an instance.
	 * @param owner ProjectConfigUITester instance.
	 */
	public OutputSettingForDiffConfigUI(final ProjectConfigUITester owner) {
		this.testOwner = owner;
	}

	/**
	 * Gets list of &lt;input type="checkbox" name="_.diffOutputSetting"&gt;.
	 * @return List of DomElement.
	 */
	public List<DomElement> getListOfCheckboxForDiffOutputSetting() {
		List<DomElement> checkboxList = this.testOwner.configPage().getElementsByName("_.diffOutputSetting");
		if (checkboxList == null || checkboxList.isEmpty()) {
			fail("_.diffOutputSetting not found.");
		}
		return checkboxList;
	}

	/**
	 * Finds the 1st &lt;input type="checkbox" name="_.diffOutputSetting"&gt;
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findDiffOutputCheckbox() {
		return this.findDiffOutputCheckbox(0);
	}

	/**
	 * Finds the N-th &lt;input type="checkbox" name="_.diffOutputSetting"&gt;
	 * @param index N-th number - 1.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findDiffOutputCheckbox(final int index) {
		return (HtmlCheckBoxInput) this.testOwner.configHtmlUI().findInputProperty("diffOutputSetting", index);
	}

	/**
	 * Finds the 1st &lt;input type="checkbox" name="_.diffOutputSetting"&gt;,
	 * and verifies if that its checked status is expected boolean value.
	 * @param check boolean of checking status.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findDiffOutputCheckboxThenVerify(final boolean check) {
		return this.findDiffOutputCheckboxThenVerify(0, check);
	}

	/**
	 * Finds the N-th &lt;input type="checkbox" name="_.diffOutputSetting"&gt;,
	 * and verifies if that its checked status is expected boolean value.
	 * @param index N-th number - 1.
	 * @param check boolean of checking status.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findDiffOutputCheckboxThenVerify(final int index, final boolean check) {
		HtmlCheckBoxInput outputCheckbox = this.findDiffOutputCheckbox(index);
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
			fail("output diff checkbox label not found.");
		}
		//System.out.println("[TEST] checkboxLabel XPath:" + checkboxLabel.getCanonicalXPath());
		System.out.println("[TEST] checkboxLabel Content:" + checkboxLabel.getTextContent());
		assertThat(checkboxLabel.getTextContent(), not(isEmptyString()));
		// ファイル出力チェックボックスの初期状態は非選択
		String checkedValue = outputCheckbox.getAttribute("checked");
		System.out.println("[TEST] diffOutputCheckbox checked:" + checkedValue);
		if (check) {
			assertThat(checkedValue, anyOf(equalTo("checked"), equalTo("true")));
		} else {
			assertThat(checkedValue, not(anyOf(equalTo("checked"), equalTo("true"))));
		}
		return outputCheckbox;
	}

	/**
	 * Checks on the 1st diff output setting by clicking the checkbox.
	 */
	public void checkOnDiffOutputCheckbox() {
		this.checkOnDiffOutputCheckbox(0);
	}

	/**
	 * Checks on the N-th diff output setting by clicking the checkbox.
	 * @param index N-th number - 1.
	 */
	public void checkOnDiffOutputCheckbox(final int index) {
		HtmlCheckBoxInput outputCheckbox = this.findDiffOutputCheckbox(index);
		if (outputCheckbox.isChecked()) {
			// すでにON
			return;
		}
		// ボタン押下
		System.out.println("[TEST] === click the diffOutputSetting checkbox on:" + outputCheckbox.toString());
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
	 * Checks off the 1st diff output setting by clicking the checkbox.
	 */
	public void checkOffDiffOutputCheckbox() {
		this.checkOffDiffOutputCheckbox(0);
	}

	/**
	 * Checks off the N-th diff output setting by clicking the checkbox.
	 * @param index N-th number - 1.
	 */
	public void checkOffDiffOutputCheckbox(final int index) {
		HtmlCheckBoxInput outputCheckbox = this.findDiffOutputCheckbox(index);
		if (!outputCheckbox.isChecked()) {
			// すでにOFF
			return;
		}
		// ボタン押下
		System.out.println("[TEST] === click the diffOutputSetting checkbox off:" + outputCheckbox.toString());
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
	 * Finds 1st &lt;input type="text" name="_.diffOutputFilePath"&gt;.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findDiffOutputFilePath() {
		return this.findDiffOutputFilePath(0);
	}

	/**
	 * Finds n-th &lt;input type="text" name="_.diffOutputFilePath"&gt;.
	 * @param index N-th number - 1.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findDiffOutputFilePath(final int index) {
		return (HtmlTextInput) this.testOwner.configHtmlUI().findInputProperty("diffOutputFilePath", index);
	}

	/**
	 * Finds 1st &lt;select name="_.diffOutputFormat"&gt;.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findDiffOutputFormat() {
		return this.findDiffOutputFormat(0);
	}

	/**
	 * Finds N-th &lt;select name="_.diffOutputFormat"&gt;.
	 * @param index N-th number - 1.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findDiffOutputFormat(final int index) {
		return this.testOwner.configHtmlUI().findSelectProperty("diffOutputFormat", index);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.diffOutputFilePath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findDiffOutputFilePathThenVerify(final String expected, final boolean check) {
		return this.findDiffOutputFilePathThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.diffOutputFilePath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findDiffOutputFilePathThenVerify(final int index, final String expected,
			final boolean check) {
		return this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("diffOutputFilePath", index, expected, check);
	}

	/**
	 * Finds 1st &lt;select name="_.diffOutputFormat"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;select&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlSelect findDiffOutputFormatThenVerify(final String expected, final boolean check) {
		return this.findDiffOutputFormatThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;select name="_.diffOutputFormat"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;select&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlSelect findDiffOutputFormatThenVerify(final int index, final String expected, final boolean check) {
		return this.testOwner.configHtmlUI()
				.findSelectPropertyThenVerify("diffOutputFormat", index, expected, check);
	}

	/**
	 * About 1st &lt;input type="text" name="_.diffOutputFilePath"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfDiffOutputFilePathAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfDiffOutputFilePathAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;input type="text" name="_.diffOutputFilePath"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfDiffOutputFilePathAboutVisibility(final int index, final boolean visible) {
		HtmlInput input = this.testOwner.configHtmlUI().findInputProperty("diffOutputFilePath", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("diffOutputFilePath", input, visible);
	}

	/**
	 * About 1st &lt;select name="_.diffOutputFormat"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfDiffOutputFormatAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfDiffOutputFormatAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;select name="_.diffOutputFormat"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfDiffOutputFormatAboutVisibility(final int index, final boolean visible) {
		HtmlSelect select = this.testOwner.configHtmlUI().findSelectProperty("diffOutputFormat", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("diffOutputFormat", select, visible);
	}
}
