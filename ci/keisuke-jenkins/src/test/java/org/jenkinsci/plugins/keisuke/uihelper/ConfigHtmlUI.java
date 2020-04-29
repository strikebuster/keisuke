package org.jenkinsci.plugins.keisuke.uihelper;

import static keisuke.util.TestUtil.nameOfSystemOS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITLONGTIME;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITTIME;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfErrorDivFromTdata;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfErrorTdataFromInput;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfErrorTdataFromSelect;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfHelpAnchorFromInput;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfHelpAnchorFromSelect;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfHelpDivFromTdata;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfHelpTdataFromErrorableElement;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfHelpTdataFromNonErrorElement;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfPropertyTrowFromElement;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * UI helper for operating html elements in config page.
 */
public class ConfigHtmlUI {

	static final int RETRY_FOR_YUI = 5; // need to wait for YUI rendering, so retrying

	private ConfigUIClient uiClient = null;

	/**
	 * Constructs an instance.
	 * @param client ConfigUIClient instance.
	 */
	public ConfigHtmlUI(final ConfigUIClient client) {
		this.uiClient = client;
	}

	/**
	 * Finds the 1st &lt;input name="_.property"&gt;.
	 * @param property name of setting property.
	 * @return HtmlInput instance.
	 */
	public HtmlInput findInputProperty(final String property) {
		return this.findInputProperty(property, 0);
	}

	/**
	 * Finds the N-th &lt;input name="_.property"&gt;.
	 * @param property name of setting property.
	 * @param index N-th number - 1.
	 * @return HtmlInput instance.
	 */
	public HtmlInput findInputProperty(final String property, final int index) {
		List<HtmlInput> inputList = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			inputList = this.uiClient.configForm().getInputsByName("_." + property);
			if (inputList != null && index < inputList.size()) {
				break;
			}
			System.out.println("[TEST DEBUG] get input[" + property + "] retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		if (inputList == null || inputList.isEmpty()) {
			fail("_." + property + " not found.");
		} else if (index >= inputList.size()) {
			fail("_." + property + " exist less than " + Integer.toString(index) + ".");
		}
		//System.out.println("[TEST DEBUG] " + property + "[" + Integer.toString(index)
		//		+ "] XPath:" + inputList.get(index).getCanonicalXPath());
		return inputList.get(index);
	}

	/**
	 * Finds the 1st &lt;select name="_.property"&gt;.
	 * @param property name of setting property.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findSelectProperty(final String property) {
		return this.findSelectProperty(property, 0);
	}

	/**
	 * Finds the N-th &lt;select name="_.property"&gt;.
	 * @param property name of setting property.
	 * @param index N-th number - 1.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findSelectProperty(final String property, final int index) {
		List<HtmlSelect> selectList = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			selectList = this.uiClient.configForm().getSelectsByName("_." + property);
			if (selectList != null && index < selectList.size()) {
				break;
			}
			System.out.println("[TEST DEBUG] get select[" + property + "] retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		if (selectList == null || selectList.isEmpty()) {
			fail("._" + property + " not found.");
		} else if (index >= selectList.size()) {
			fail("._" + property + " exist less than " + Integer.toString(index) + ".");
		}
		//System.out.println("[TEST DEBUG] " + property + "[" + Integer.toString(index)
		//		+ "] XPath:" + selectList.get(index).getCanonicalXPath());
		return selectList.get(index);
	}

	/**
	 * Finds 1st &lt;input type="text" name=_.property&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param property name of setting property.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findInputPropertyThenVerify(final String property, final String expected,
			final boolean check) {
		return this.findInputPropertyThenVerify(property, 0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name=_.property&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param property name of setting property.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findInputPropertyThenVerify(final String property, final int index,
			final String expected, final boolean check) {
		HtmlTextInput textbox = (HtmlTextInput) this.findInputProperty(property, index);
		//System.out.println("[TEST DEBUG] Textbox[" + property + "][" + Integer.toString(index)
		//		+ "] XPath:" + textbox.getCanonicalXPath());
		//System.out.println("[TEST DEBUG] Textbox[" + property + "][" + Integer.toString(index)
		//		+ "].name:" + textbox.getAttribute("name"));
		System.out.println("[TEST] Textbox[" + property + "][" + Integer.toString(index)
				+ "].value:" + textbox.getAttribute("value"));
		// 値の検証
		assertThat(textbox.getAttribute("value"), equalTo(expected));
		// エラー表示の検証
		String errorMsg = this.getErrorContentOf(property, textbox);
		if (check) {
			assertThat(errorMsg, isEmptyString());
		} else {
			assertThat(errorMsg, not(isEmptyString()));
		}
		return textbox;
	}

	/**
	 * Finds 1st &lt;input type="checkbox" name=_.property&gt;.
	 * Then verifies if it is expected check status.
	 * And returns &lt;input&gt; instance.
	 * @param property name of setting property.
	 * @param check expected boolean.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findCheckboxPropertyThenVerify(final String property,
			final boolean check) {
		return this.findCheckboxPropertyThenVerify(property, 0, check);
	}

	/**
	 * Finds N-th &lt;input type="checkbox" name=_.property&gt;.
	 * Then verifies if it is expected check status.
	 * And returns &lt;input&gt; instance.
	 * @param property name of setting property.
	 * @param index N-th number - 1.
	 * @param check expected boolean.
	 * @return HtmlCheckBoxInput instance.
	 */
	public HtmlCheckBoxInput findCheckboxPropertyThenVerify(final String property, final int index,
			final boolean check) {
		HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) this.findInputProperty(property, index);
		//System.out.println("[TEST DEBUG] Checkbox[" + property + "][" + Integer.toString(index)
		//		+ "] XPath:" + checkbox.getCanonicalXPath());
		//System.out.println("[TEST DEBUG] Checkbox[" + property + "][" + Integer.toString(index)
		//		+ "].name:" + checkbox.getAttribute("name"));
		System.out.println("[TEST] Checkbox[" + property + "][" + Integer.toString(index)
				+ "].value:" + checkbox.getAttribute("checked"));
		// チェックの検証
		if (check) {
			assertThat(checkbox.getAttribute("checked"), anyOf(equalTo("true"), equalTo("checked")));
		} else {
			assertThat(checkbox.getAttribute("checked"), not(anyOf(equalTo("true"), equalTo("checked"))));
		}
		// エラーなしの検証
		String errorMsg = this.getErrorContentOf(property, checkbox);
		assertThat(errorMsg, isEmptyString());
		return checkbox;
	}

	/**
	 * Finds 1st &lt;select name=_.property&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;select&gt; instance.
	 * @param property name of setting property.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findSelectPropertyThenVerify(final String property, final String expected,
			final boolean check) {
		return this.findSelectPropertyThenVerify(property, 0, expected, check);
	}

	/**
	 * Finds N-th &lt;select name=_.property&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;select&gt; instance.
	 * @param property name of setting property.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlSelect instance.
	 */
	public HtmlSelect findSelectPropertyThenVerify(final String property, final int index,
			final String expected, final boolean check) {
		HtmlSelect selectbox = (HtmlSelect) this.findSelectProperty(property, index);
		System.out.println("[TEST] Selectbox[" + property + "][" + Integer.toString(index)
				+ "].size:" + selectbox.getOptionSize());
		int idx = selectbox.getSelectedIndex();
		System.out.println("[TEST] Selectbox[" + property + "][" + Integer.toString(index)
				+ "].selected:" + Integer.toString(idx));
		System.out.println("[TEST] Selectbox[" + property + "][" + Integer.toString(index)
				+ "].value:" + selectbox.getAttribute("value"));
		System.out.println("[TEST] Selectbox[" + property + "][" + Integer.toString(index)
				+ "].selected.value:" + selectbox.getSelectedOptions().get(0).getAttribute("value"));
		// 選択値を検証
		String actual = selectbox.getSelectedOptions().get(0).getAttribute("value");
		assertThat(actual, equalTo(expected));
		// エラー表示を検証
		String errorMsg = this.getErrorContentOf(property, selectbox);
		if (check) {
			assertThat(errorMsg, isEmptyString());
		} else {
			assertThat(errorMsg, not(isEmptyString()));
		}
		return selectbox;
	}

	/**
	 * Verifies that the style of html table row is expected visibility.
	 * This row includes the target html element which name is property.
	 * And this property belongs to an optionalProperty.
	 * @param property name of property.
	 * @param element html input/select which name is property.
	 * @param visible visibility boolean value.
	 */
	public void verifyTrowStyleInOption(final String property, final HtmlElement element, final boolean visible) {
		String rowXPath = getXPathOfPropertyTrowFromElement(element);
		HtmlTableRow trRow = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			trRow = (HtmlTableRow) this.uiClient.configPage().getFirstByXPath(rowXPath);
			if (trRow != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get <tr> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		String rowStyle = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			if (trRow.hasAttribute("style")) {
				rowStyle = trRow.getAttribute("style");
				break;
			}
			System.out.println("[TEST DEBUG] get <tr style=*> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST] TrRow[" + property + "] XPath:" + trRow.getCanonicalXPath());
		System.out.println("[TEST] TrRow[" + property + "] style:" + rowStyle);
		if (visible) {
			if (rowStyle != null) {
				assertThat(rowStyle, not(containsString("display: none;"))); // 表示
			}
		} else {
			assertThat(rowStyle, containsString("display: none;")); // 非表示
		}
	}

	/**
	 * Verifies that the style of html table row is expected visibility.
	 * This row includes the target html element which name is property.
	 * And this property belongs to Advanced configurations.
	 * @param property name of property.
	 * @param element html input/select which name is property.
	 * @param visible visibility boolean value.
	 */
	public void verifyTrowStyleInAdvanced(final String property, final HtmlElement element,
			final boolean visible) {
		String rowXPath = getXPathOfPropertyTrowFromElement(element);
		HtmlTableRow trRow = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			trRow = (HtmlTableRow) this.uiClient.configPage().getFirstByXPath(rowXPath);
			if (trRow != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get <tr> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		if (trRow == null) {
			fail("trow[" + property + "] not found.");
		}
		String rowStyle = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			if (trRow.hasAttribute("style") || !visible) {
				rowStyle = trRow.getAttribute("style");
				break;
			}
			System.out.println("[TEST DEBUG] get <tr style=*> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST] TrRow[" + property + "] XPath:" + trRow.getCanonicalXPath());
		System.out.println("[TEST] TrRow[" + property + "] style:" + rowStyle);
		if (visible) {
			assertThat(rowStyle, containsString("opacity: 1"));
		} else {
			assertThat(rowStyle, not(containsString("opacity: 1")));
		}
	}

	/**
	 * Gets error contents string.
	 * This error belongs to the target html input which name is property.
	 * @param property name of property.
	 * @param input target html input instance.
	 * @return error contents string.
	 */
	public String getErrorContentOf(final String property, final HtmlInput input) {
		String errorTdXPath = getXPathOfErrorTdataFromInput(input);
		return this.getErrorContentOf(property, errorTdXPath);
	}

	/**
	 * Gets error contents string.
	 * This error belongs to the target html select which name is property.
	 * @param property name of property.
	 * @param select target html select instance.
	 * @return error contents string.
	 */
	public String getErrorContentOf(final String property, final HtmlSelect select) {
		String errorTdXPath = getXPathOfErrorTdataFromSelect(select);
		return this.getNotOccurredErrorContentOf(property, errorTdXPath);
	}

	/**
	 * Gets error contents string.
	 * This error belongs to the target html input which name is property.
	 * @param property name of property.
	 * @param input target html checkbox instance.
	 * @return error contents string.
	 */
	public String getErrorContentOf(final String property, final HtmlCheckBoxInput input) {
		String errorTdXPath = getXPathOfErrorTdataFromInput(input);
		return this.getNotOccurredErrorContentOf(property, errorTdXPath);
	}

	private String getNotOccurredErrorContentOf(final String property, final String errorTdXPath) {
		// propertyのエラー
		//System.out.println("[TEST DEBUG] errorTdXPath:" + errorTdXPath);
		HtmlTableDataCell errorTd = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			errorTd = (HtmlTableDataCell) this.uiClient.configPage().getFirstByXPath(errorTdXPath);
			if (errorTd != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get error content <td> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST DEBUG] ErrorTd[" + property + "] XPath:" + errorTd.getCanonicalXPath());
		String content = errorTd.getTextContent();
		//System.out.println("[TEST] ErrorTd[" + property + "] Content:" + content);
		return content;
	}

	private String getErrorContentOf(final String property, final String errorTdXPath) {
		// propertyのエラー
		//System.out.println("[TEST DEBUG] errorTdXPath:" + errorTdXPath);
		HtmlTableDataCell errorTd = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			errorTd = (HtmlTableDataCell) this.uiClient.configPage().getFirstByXPath(errorTdXPath);
			if (errorTd != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get error content <td> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST DEBUG] ErrorTd[" + property + "] XPath:" + errorTd.getCanonicalXPath());
		String content = errorTd.getTextContent();
		//System.out.println("[TEST] ErrorTd[" + property + "] Content:" + content);
		//return content;
		String errorDivXPath = getXPathOfErrorDivFromTdata(errorTd);
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			HtmlDivision errorDiv =
					(HtmlDivision) this.uiClient.configPage().getFirstByXPath(errorDivXPath);
			if (errorDiv != null) {
				content = errorDiv.getTextContent();
				System.out.println("[TEST] ErrorTd[" + property + "] Content:" + content);
				return content;
			}
			System.out.println("[TEST DEBUG] get error content <div> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		return content; // ""
	}

	/**
	 * Gets help contents string.
	 * This help belongs to the target html element which name is property.
	 * The html element is HtmlInput or HtmlSelect.
	 * @param property name of property.
	 * @param element target html element instance.
	 * @return help contents string.
	 */
	public String getHelpContentOf(final String property, final HtmlElement element) {
		String helpAnchorXpath = null;
		boolean isNonErrorElement = false;
		if (element instanceof HtmlSelect) {
			helpAnchorXpath = getXPathOfHelpAnchorFromSelect((HtmlSelect) element);
		} else if (element instanceof HtmlCheckBoxInput) {
			helpAnchorXpath = getXPathOfHelpAnchorFromInput((HtmlCheckBoxInput) element);
		} else if (element instanceof HtmlInput) {
			helpAnchorXpath = getXPathOfHelpAnchorFromInput((HtmlInput) element);
		} else {
			fail("fail to get help content of HtmlElement:" + element.toString());
			return null;
		}
		// propertyのヘルプ
		HtmlAnchor helpAnchor = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			helpAnchor = (HtmlAnchor) this.uiClient.configPage().getFirstByXPath(helpAnchorXpath);
			if (helpAnchor != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get help anchor retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST] HelpAnchor[" + property + "] XPath:" + helpAnchor.getCanonicalXPath());
		System.out.println("[TEST] HelpAnchor[" + property + "] helpURL:" + helpAnchor.getAttribute("helpURL"));
		return this.getHelpContentOf(property, helpAnchor, isNonErrorElement);
	}

	/**
	 * Gets help contents string.
	 * This help belongs to the target checkbox which name is property.
	 * The checkbox is generated by optional block of jelly.
	 * @param property name of property.
	 * @param checkbox target checkbox instance.
	 * @return help contents string.
	 */
	public String getHelpContentOfOptionalCheckBox(final String property, final HtmlCheckBoxInput checkbox) {
		String helpAnchorXpath = getXPathOfHelpAnchorFromInput(checkbox);
		boolean isNonErrorElement = true;
		// propertyのヘルプ
		HtmlAnchor helpAnchor = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			helpAnchor = (HtmlAnchor) this.uiClient.configPage().getFirstByXPath(helpAnchorXpath);
			if (helpAnchor != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get help anchor retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST] HelpAnchor[" + property + "] XPath:" + helpAnchor.getCanonicalXPath());
		System.out.println("[TEST] HelpAnchor[" + property + "] helpURL:" + helpAnchor.getAttribute("helpURL"));
		return this.getHelpContentOf(property, helpAnchor, isNonErrorElement);
	}

	/**
	 * Gets help contents string.
	 * This help wili be shown by the anchor.
	 * The anchor belongs to the html element which name is property.
	 * @param property name of property.
	 * @param anchor target html anchor instance.
	 * @param isNonError property which has the anchor does not have error message &lt;tr&gt;
	 * @return help contents string.
	 */
	public String getHelpContentOf(final String property, final HtmlAnchor anchor, final boolean isNonError) {
		if (anchor == null) {
			fail("fail to get help content because HtmlAnchor is null.");
			return null;
		}
		String helpTdXPath = null;
		if (isNonError) {
			helpTdXPath = getXPathOfHelpTdataFromNonErrorElement(anchor);
		} else {
			helpTdXPath = getXPathOfHelpTdataFromErrorableElement(anchor);
		}
		HtmlTableDataCell helpTd = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			helpTd = (HtmlTableDataCell) this.uiClient.configPage().getFirstByXPath(helpTdXPath);
			if (helpTd != null) {
				//System.out.println("[TEST] HelpTd[" + property + "] XPath:"
				//		+ helpTd.getCanonicalXPath());
				//String helpTextUnset = helpTd.getTextContent();
				break;
			}
			System.out.println("[TEST DEBUG] get help content(div) retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		if (helpTd == null) {
			fail("fail to get help content because help <td> not found.");
		}
		String content = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			String helpDivXPath = getXPathOfHelpDivFromTdata(helpTd);
			HtmlDivision helpDiv =
					(HtmlDivision) this.uiClient.configPage().getFirstByXPath(helpDivXPath);
			if (helpDiv != null) {
				content = helpDiv.getTextContent();
				break;
			}
			System.out.println("[TEST DEBUG] get help content(div) retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.waitForBackgroundProcess();
			}
		}
		String helpTextUnset = content;
		System.out.println("[TEST] HelpTd[" + property + "] Content(before):" + helpTextUnset);
		// ヘルプ表示
		System.out.println("[TEST] === click the help of " + property);
		try {
			anchor.click();
			this.waitForBackgroundProcess();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
		String helpTextSet = helpTd.getTextContent();
		System.out.println("[TEST] HelpTd[" + property + "] Content(after):" + helpTextSet);
		assertThat(helpTextSet, not(equalTo(helpTextUnset)));
		return helpTextSet;
	}

	/**
	 * Finds the html input-radio instances of setting property.
	 * @param name name of html input.
	 * @return list of HtmlInput radio instance.
	 */
	public List<HtmlRadioButtonInput> findRadioElements(final String name) {
		List<HtmlRadioButtonInput> radioList = new ArrayList<HtmlRadioButtonInput>();
		for (HtmlInput input : this.uiClient.configForm().getInputsByName(name)) {
			if (input.getAttribute("type").equals("radio")) {
				radioList.add((HtmlRadioButtonInput) input);
			}
		}
		return radioList;
	}

	/**
	 * Waits a few hundred mili-seconds for runnning background process.
	 */
	public void waitForBackgroundProcess() {
		this.uiClient.configPage().getWebClient().waitForBackgroundJavaScript(WAITTIME);
		if (nameOfSystemOS().startsWith("Windows")) {
			// Windows is heavy about HTML UI, so additional waiting
			this.uiClient.configPage().getWebClient().waitForBackgroundJavaScript(WAITTIME);
		}
	}

	/**
	 * Waits a second for runnning background process.
	 */
	public void waitLongForBackgroundProcess() {
		this.uiClient.configPage().getWebClient().waitForBackgroundJavaScript(WAITLONGTIME);
	}

	private void debugElement(final HtmlElement element) {
		System.out.println("[TEST DEBUG] element:" + element.toString());
		System.out.println("[TEST DEBUG] element Content:" + element.getTextContent());
		DomNode parent = element.getParentNode();
		System.out.println("[TEST DEBUG] parent:" + parent.toString());
		for (DomNode brother : parent.getChildren()) {
			System.out.println("[TEST DEBUG] brother:" + brother.toString());
			for (DomNode child : brother.getChildren()) {
				System.out.println("[TEST DEBUG] brother's child:" + child.toString());
			}
		}
		for (DomNode child : element.getChildren()) {
			System.out.println("[TEST DEBUG] my child:" + child.toString());
		}
	}
}
