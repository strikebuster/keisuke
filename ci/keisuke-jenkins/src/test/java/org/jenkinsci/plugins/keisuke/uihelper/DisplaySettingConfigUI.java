package org.jenkinsci.plugins.keisuke.uihelper;

import static org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI.RETRY_FOR_YUI;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfTitleTdataForDisplaySettingFromTrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfDisplayStepKindFirstRadioFromTitleTdata;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfTitleTdataForDisplayStepKindFromTrow;
import static org.junit.Assert.fail;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfErrorTdataFromInput;

import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.keisuke.DisplayStepKindEnum;
import org.jenkinsci.plugins.keisuke.ProjectConfigUITester;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * UI helper for DisplaySetting configuration of KeisukePublisher in project config page.
 */
public class DisplaySettingConfigUI {

	private ProjectConfigUITester testOwner = null;

	/**
	 * Constructs an instance.
	 * The instance has owner object which is Jenkins test main class extends AbstractSettingUITest.
	 * @param owner AbstractSettingUITest instance.
	 */
	public DisplaySettingConfigUI(final ProjectConfigUITester owner) {
		this.testOwner = owner;
	}

	/**
	 * Finds &lt;tr&gt; which contains 'displaySetting' in config page.
	 * @return HtmlTableRow instance.
	 */
	public HtmlTableRow findTrowOfDisplaySetting() {
		HtmlTableRow trow = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			try {
				trow = (HtmlTableRow) this.testOwner.configPage().getElementByName("displaySetting");
				if (trow != null) {
					break;
				}
			} catch (ElementNotFoundException ex) {
				System.out.println("[TEST DEBUG] get trow[displaySetting] retry : " + retry);
			}
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		if (trow == null) {
			fail("displaySetting not found.");
		}
		return trow;
	}

	/**
	 * Gets title text of displaySetting.
	 * It is content of &lt;td&gt; which is contained previous &lt;tr&gt; of &lt;tr name="displaySetting"&gt;.
	 * @return title.
	 */
	public String getTitleOfDisplaySetting() {
		HtmlTableRow trow = this.findTrowOfDisplaySetting();
		return this.getTitleOfDisplaySettingAtTrow(trow);
	}

	/**
	 * Gets title text of displaySetting.
	 * It is content of &lt;td&gt; which is contained previous &lt;tr&gt; of &lt;tr name="displaySetting"&gt;.
	 * @param trow HtmlTableRow of which name is "displaySetting".
	 * @return title.
	 */
	public String getTitleOfDisplaySettingAtTrow(final HtmlTableRow trow) {
		String titleXpath = getXPathOfTitleTdataForDisplaySettingFromTrow(trow);
		HtmlTableDataCell titleTdata =
				(HtmlTableDataCell) this.testOwner.configPage().getFirstByXPath(titleXpath);
		return titleTdata.getTextContent();
	}

	/**
	 * Gets title text of "DisplayStepKind".
	 * @return title.
	 */
	public String getTitleOfDisplayStepKind() {
		HtmlTableRow trow = this.findTrowOfDisplaySetting();
		HtmlTableDataCell tdata = this.findTdataOfDisplayStepKindTitleFromDisplaySettingTrow(trow);
		return tdata.getTextContent();
	}

	/**
	 * Finds &lt;td&gt; for "DisplayStepKind".
	 * @param trow HtmlTableRow of which name is "displaySetting".
	 * @return HtmlTableDataCell instance.
	 */
	public HtmlTableDataCell findTdataOfDisplayStepKindTitleFromDisplaySettingTrow(final HtmlTableRow trow) {
		String kindTitleXpath = getXPathOfTitleTdataForDisplayStepKindFromTrow(trow);
		return  (HtmlTableDataCell) this.testOwner.configPage().getFirstByXPath(kindTitleXpath);
	}

	/**
	 * Finds 1st &lt;input type="radio"&gt; for "DisplayStepKind".
	 * @param tdata HtmlTableDataCell for Title of "DisplayStepKind".
	 * @return HtmlInput instance.
	 */
	public HtmlRadioButtonInput findFirstRadioOfDisplayStepKindAtTdata(final HtmlTableDataCell tdata) {
		String radioXpath = getXPathOfDisplayStepKindFirstRadioFromTitleTdata(tdata);
		return (HtmlRadioButtonInput) this.testOwner.configForm().getFirstByXPath(radioXpath);
	}

	/**
	 * Gets name of &lt;input type=radio&gt; for DisplayStepKind in config page.
	 * @return name of radio buttons.
	 */
	public String getRadioNameOfDisplayStepKind() {
		HtmlTableRow trow = this.findTrowOfDisplaySetting();
		HtmlTableDataCell tdata = this.findTdataOfDisplayStepKindTitleFromDisplaySettingTrow(trow);
		HtmlRadioButtonInput radio = this.findFirstRadioOfDisplayStepKindAtTdata(tdata);
		return radio.getAttribute("name");
	}

	/**
	 * Gets list of radio buttons for DisplayStepKind in config page.
	 * @return list of radio buttons.
	 */
	public List<HtmlRadioButtonInput> getRadiosOfDisplayStepKind() {
		String radioName = this.getRadioNameOfDisplayStepKind();
		return this.testOwner.configHtmlUI().findRadioElements(radioName);
	}

	/**
	 * Gets the radio button which is selected for DisplayStepKind in config page.
	 * @param radioList list of radio buttons for DisplayStepKind
	 * @param kind selected one of DisplayStepKind.
	 * @return radio button which is selected.
	 */
	public HtmlRadioButtonInput getRadioByDisplayStepKind(final List<HtmlRadioButtonInput> radioList,
			final String kind) {
		for (HtmlRadioButtonInput radio : radioList) {
			String value = radio.getAttribute("value");
			if (kind.equals(value)) {
				return radio;
			}
		}
		return null;
	}

	/**
	 * Sets specified kind to DisplayStepKind.
	 * @param kind one of DisplayStepKindEnum.
	 * @return index of checked radio button.
	 */
	public int setDisplayStepKind(final DisplayStepKindEnum kind) {
		List<HtmlRadioButtonInput> kindRadioList = this.getRadiosOfDisplayStepKind();
		String kindValue = kind.getValue();
		int selectIdx = this.getIndexOfRadioList(kindRadioList, kindValue);
		System.out.println("[TEST] === click the radio button of '" + kindValue
				+ "' that is indexed at " + Integer.toString(selectIdx) + ".");
		try {
			kindRadioList.get(selectIdx).click();
			this.testOwner.configHtmlUI().waitForBackgroundProcess();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
		//System.out.println("[TEST DEBUG] "	+ kindRadioList.get(selectIdx).asXml()
		//		+ "' isChecked " + kindRadioList.get(selectIdx).isChecked());
		return selectIdx;
	}

	/**
	 * Gets index of radio button which value equals target.
	 * @param list radio buttons.
	 * @param target value of DisplayStepKind.
	 * @return index.
	 */
	private int getIndexOfRadioList(final List<HtmlRadioButtonInput> list, final String target) {
		for (int i = 0; i < list.size(); i++) {
			String value = list.get(i).getAttribute("value");
			if (target.equals(value)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Verifies if that selected radio button equals expected.
	 * @param expected value of expected radio button.
	 */
	public void verifyRadioSelected(final String expected) {
		List<HtmlRadioButtonInput> kindRadioList = this.getRadiosOfDisplayStepKind();
		int checkedIdx = -1;
		int idx = 0;
		for (HtmlRadioButtonInput radio : kindRadioList) {
			System.out.println("[TEST] displayStepKind[radio] XPath(" + Integer.toString(idx) + "):"
					+ radio.getCanonicalXPath());
			String value = radio.getAttribute("value");
			String checked = radio.getAttribute("checked");
			System.out.println("[TEST] displayStepKind[radio](" + Integer.toString(idx)
					+ "):value=" + value);
			System.out.println("[TEST] displayStepKind[radio](" + Integer.toString(idx)
					+ "):checked(initialized)=" + checked);
			System.out.println("[TEST] displayStepKind[radio](" + Integer.toString(idx)
					+ "):isChecked=" + radio.isChecked());
			//System.out.println("[TEST DEBUG] "	+ kindRadioList.get(selectIdx).asXml());
			//if ("checked".equals(checked) || "true".equals(checked)) {
			if (radio.isChecked()) {	// changed after plugin POM update to 3.55
				checkedIdx = idx;
			}
			idx++;
		}
		String checkedValue = kindRadioList.get(checkedIdx).getAttribute("value");
		assertThat(checkedValue, equalTo(expected));
	}

	/**
	 * Gets error message about DisplayStepKind.
	 * @return error message.
	 */
	public String getErrorContentOfDisplayStepKind() {
		HtmlRadioButtonInput radio = this.getRadiosOfDisplayStepKind().get(0);
		HtmlTableDataCell tdata = this.findErrorTdataAboutDisplayStepKindFromRadio(radio);
		//System.out.println("[TEST] ErrorTd[kind] XPath:" + kindErrorTd.getCanonicalXPath());
		return tdata.getTextContent();
	}

	/**
	 * Finds &lt;div&gt; to showing the error about "DisplayStepKind".
	 * @param radio HtmlInput &lt;input type="radio" name="xx_.displayStepKind"&gt;.
	 * @return HtmlTableDataCell instance.
	 */
	public HtmlTableDataCell findErrorTdataAboutDisplayStepKindFromRadio(final HtmlInput radio) {
		String kindErrorTdXPath = getXPathOfErrorTdataFromInput(radio);
		//System.out.println("[TEST] kindErrorTdXPath:" + kindErrorTdXPath);
		// *input* /../../../tr[m+1]/td[2]
		HtmlTableDataCell tdata = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			tdata = (HtmlTableDataCell) this.testOwner.configPage().getFirstByXPath(kindErrorTdXPath);
			if (tdata != null) {
				break;
			}
			// not found
			System.out.println("[TEST DEBUG] get displayStepKind error<td> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return tdata;
	}
}
