package org.jenkinsci.plugins.keisuke.uihelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI.RETRY_FOR_YUI;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfAddButtonFromRepeatedDiv;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfCountingUnitTitleFromDiv;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfYuiAddButtonFromRepeatedDiv;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.keisuke.ProjectConfigUITester;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;

/**
 * UI helper for CountingUnit configuration of KeisukePublisher in project config page.
 */
public class CountingUnitConfigUI {

	private ProjectConfigUITester testOwner = null;
	private InputSettingConfigUI inputUI = null;
	private OutputSettingConfigUI outputUI = null;

	/**
	 * Constructs an instance.
	 * @param owner ProjectConfigUITester instance.
	 */
	public CountingUnitConfigUI(final ProjectConfigUITester owner) {
		this.testOwner = owner;
		this.inputUI = new InputSettingConfigUI(owner);
		this.outputUI = new OutputSettingConfigUI(owner);
	}

	/**
	 * Gets list of &lt;div name='countingUnits'&gt;.
	 * @return List of DomElement.
	 */
	public List<DomElement> getListOfDivForCountingUnits() {
		List<DomElement> divList = this.testOwner.configPage().getElementsByName("countingUnits");
		if (divList == null || divList.isEmpty()) {
			fail("countingUnits not found.");
		}
		return divList;
	}

	/**
	 * Finds 1st &lt;div name='countingUnits'&gt;.
	 * @return HtmlDivision.
	 */
	public HtmlDivision findCountingUnitsDivision() {
		List<DomElement> divList = this.getListOfDivForCountingUnits();
		return (HtmlDivision) divList.get(0);
	}

	/**
	 * Gets title text of CountingUnits.
	 * It is content of left &lt;td&gt; from &lt;td&gt; which has &lt;div name="countingUnits"&gt;.
	 * @param repeatedChunk 1st HtmlDivision which class is "repeated-chunk".
	 * @return title.
	 */
	public String getTitleOfCountingUnitsAtDiv(final HtmlDivision repeatedChunk) {
		String titleXpath = getXPathOfCountingUnitTitleFromDiv(repeatedChunk);
		HtmlTableDataCell titleTdCell = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			titleTdCell =
					(HtmlTableDataCell) this.testOwner.configPage().getFirstByXPath(titleXpath);
			if (titleTdCell != null) {
				break;
			}
			// not found
			System.out.println("[TEST DEBUG] get countingUnit <td> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		//System.out.println("[TEST] title XPath:" + titleTdCell.getCanonicalXPath());
		return titleTdCell.getTextContent();
	}

	/**
	 * Finds &lt;input type="button" value="Add"&gt;.
	 * @param repeatedChunk HtmlDivision which class is "repeated-chunk".
	 * @return HtmlButtonInput to add repeated-chunk.
	 */
	private HtmlButtonInput findAddButtonForCountingUnitAtDiv(final HtmlDivision repeatedChunk) {
		String addButtonXpath = getXPathOfAddButtonFromRepeatedDiv(repeatedChunk);
		System.out.println("[TEST] repeat Add button(input) XPath:" + addButtonXpath);
		HtmlButtonInput input = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			input = (HtmlButtonInput) this.testOwner.configPage().getFirstByXPath(addButtonXpath);
			if (input != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get <input type='button'> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return input;
	}

	/**
	 * Finds &lt;button type="button"&gtAdd&lt;/button&gt;.
	 * @param repeatedChunk HtmlDivision which class is "repeated-chunk".
	 * @return HtmlButton to add repeated-chunk.
	 */
	public HtmlButton findYuiAddButtonForCountingUnitAtDiv(final HtmlDivision repeatedChunk) {
		// parsing <input type="button"> before paring <span class="yui-button"><button type="button">
		HtmlButtonInput input = this.findAddButtonForCountingUnitAtDiv(repeatedChunk);
		System.out.println("[TEST DEBUG] find <input type='button'> : " + input);
		this.testOwner.configHtmlUI().waitForBackgroundProcess();
		String addButtonXpath = getXPathOfYuiAddButtonFromRepeatedDiv(repeatedChunk);
		System.out.println("[TEST] repeat Add <button> XPath:" + addButtonXpath);
		HtmlButton button = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			button =  (HtmlButton) this.testOwner.configPage().getFirstByXPath(addButtonXpath);
			if (button != null) {
				return button;
			}
			System.out.println("[TEST DEBUG] get <button> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return null;
	}

	/**
	 * Gets list of &lt;tr name='inputSetting'&gt;.
	 * @return List of DomElement.
	 */
	public List<DomElement> getListOfTrowForInputSetting() {
		return this.inputUI.getListOfTrowForInputSetting();
	}

	/**
	 * Gets list of &lt;input type="text" name="_.unitName"&gt;.
	 * @return List of HtmlInput.
	 */
	public List<HtmlInput> getListOfTextboxForUnitName() {
		return this.inputUI.getListOfTextboxForUnitName();
	}

	/**
	 * Gets list of &lt;input type="checkbox" name="_.outputSetting"&gt;.
	 * @return List of DomElement.
	 */
	public List<DomElement> getListOfCheckboxForOutputSetting() {
		return this.outputUI.getListOfCheckboxForOutputSetting();
	}

	/**
	 * Click "Add" button, then create new CountingUnit division.
	 */
	public void addNewCountingUnit() {
		HtmlDivision countingUnitsDiv = this.findCountingUnitsDivision();
		System.out.println("[TEST] countingUnits Div XPath:" + countingUnitsDiv.getCanonicalXPath());
		HtmlButton addButton = this.findYuiAddButtonForCountingUnitAtDiv(countingUnitsDiv);
		assertThat(addButton, is(not(nullValue())));
		System.out.println("[TEST] Add button :" + addButton.toString());
		System.out.println("[TEST] === click the add button.");
		try {
			addButton.click();
			this.testOwner.configHtmlUI().waitLongForBackgroundProcess();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
	}
}
