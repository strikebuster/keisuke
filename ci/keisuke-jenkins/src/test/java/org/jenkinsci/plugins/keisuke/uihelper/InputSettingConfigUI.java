package org.jenkinsci.plugins.keisuke.uihelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI.RETRY_FOR_YUI;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXpathOfAdvancedButtonFromTop;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXpathOfAdvancedDivFromFirstInput;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXpathOfAdvancedSpanFromTop;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXpathOfAdvancedTableFromTop;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.CountingModeEnum;
import org.jenkinsci.plugins.keisuke.ProjectConfigUITester;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * UI helper for InputSetting configuration of KeisukePublisher in project config page.
 */
public class InputSettingConfigUI {

	/** the first and default selected counting mode */
	public static final CountingModeEnum FIRST_COUNTING_MODE = CountingModeEnum.ONLY_STEP_SIMPLY;
	private ProjectConfigUITester testOwner = null;

	/**
	 * Constructs an instance.
	 * @param owner ProjectConfigUITester instance.
	 */
	public InputSettingConfigUI(final ProjectConfigUITester owner) {
		this.testOwner = owner;
	}

	/**
	 * Gets list of &lt;tr name='inputSetting'&gt;.
	 * @return List of DomElement.
	 */
	public List<DomElement> getListOfTrowForInputSetting() {
		List<DomElement> trList = this.testOwner.configPage().getElementsByName("inputSetting");
		if (trList == null || trList.isEmpty()) {
			fail("inputSetting not found.");
		}
		return trList;
	}

	/**
	 * Finds 1st &lt;tr name='inputSetting'&gt;.
	 * @return HtmlTableRow instance.
	 */
	public HtmlTableRow findTrowOfInputSetting() {
		return this.findTrowOfInputSetting(0);
	}

	/**
	 * Finds N-th &lt;tr name='inputSetting'&gt;.
	 * @param index N-th number - 1.
	 * @return HtmlTableRow instance.
	 */
	public HtmlTableRow findTrowOfInputSetting(final int index) {
		List<DomElement> trList = this.getListOfTrowForInputSetting();
		return (HtmlTableRow) trList.get(index);
	}

	/**
	 * Gets list of &lt;input type="text" name="_.unitName"&gt;.
	 * @return List of HtmlInput.
	 */
	public List<HtmlInput> getListOfTextboxForUnitName() {
		List<HtmlInput> unitList = this.testOwner.configForm().getInputsByName("_.unitName");
		if (unitList == null || unitList.isEmpty()) {
			fail("_.unitName not found.");
		}
		return unitList;
	}

	/**
	 * Inputs value into HtmlTextInput.
	 * @param textbox instance of HtmlTextInput
	 * @param value string to be filled
	 */
	public void inputValue(final HtmlTextInput textbox, final String value) {
		this.testOwner.configHtmlUI().inputValue(textbox, value);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.unitName"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findUnitNameThenVerify(final String expected, final boolean check) {
		return this.findUnitNameThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.unitName"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findUnitNameThenVerify(final int index, final String expected, final boolean check) {
		return this.testOwner.configHtmlUI().findInputPropertyThenVerify("unitName", index, expected, check);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.sourceDirectory"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findSourceDirectoryThenVerify(final String expected, final boolean check) {
		return this.findSourceDirectoryThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.sourceDirectory"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findSourceDirectoryThenVerify(final int index, final String expected,
			final boolean check) {
		return this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("sourceDirectory", index, expected, check);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.encoding&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findEncodingThenVerify(final String expected, final boolean check) {
		return this.findEncodingThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.encoding&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findEncodingThenVerify(final int index, final String expected, final boolean check) {
		return this.testOwner.configHtmlUI().findInputPropertyThenVerify("encoding", index, expected, check);
	}

	private HtmlTextInput findXmlPath(final int index) {
		List<HtmlInput> xmlpathList = this.testOwner.configForm().getInputsByName("_.xmlPath");
		if (xmlpathList == null || xmlpathList.isEmpty()) {
			fail("_.xmlPath not found.");
		}
		return (HtmlTextInput) xmlpathList.get(index);
	}

	/**
	 * Finds 1st &lt;div&gt; of advanced configurations.
	 * @return HtmlDivision.
	 */
	public HtmlDivision findAdvancedDivision() {
		return this.findAdvancedDivision(0);
	}

	/**
	 * Finds N-th &lt;div&gt; of advanced configurations.
	 * I know that 1st input element of advanced configurations is 'xmlPath'.
	 * So, finds 'xmlPath', then search near front of it.
	 * @param index N-th number - 1.
	 * @return HtmlDivision.
	 */
	public HtmlDivision findAdvancedDivision(final int index) {
		HtmlTextInput xmlpathTextbox = this.findXmlPath(index);
		// 先頭項目から高度な設定の開始divを取得
		String advancedDivXpath = getXpathOfAdvancedDivFromFirstInput(xmlpathTextbox);
		HtmlDivision div = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			div = (HtmlDivision) this.testOwner.configPage().getFirstByXPath(advancedDivXpath);
			if (div != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get advanced div retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return div;
	}

	/* 別のプラグインのAdvancedボタンも拾ってしまうのでNG
	public HtmlDivision findAdvancedDivisionNG(final int index) {
		List<HtmlElement> elements = this.testOwner.configForm()
				.getElementsByAttribute("div", "class", "advancedLink");
		if (elements == null || elements.isEmpty()) {
			throw new RuntimeException("fail to find any 'advancedLink' division");
		} else if (elements.size() <= index) {
			throw new RuntimeException("fail to find " + Integer.toString(index)
					+ "th 'advancedLink' division");
		}
		return (HtmlDivision) elements.get(index);
	}*/

	/**
	 * Finds &lt;span&gt; in the given &lt;div class="advancedLink"&gt;.
	 * this span controls visibility of "some items are edited" message.
	 * @param div target division of Advanced configuration.
	 * @return HtmlSpan instance.
	 */
	public HtmlSpan findAdvancedSpanIn(final HtmlDivision div) {
		String	advancedSpanXpath = getXpathOfAdvancedSpanFromTop(div);
		HtmlSpan span = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			span = (HtmlSpan) this.testOwner.configPage().getFirstByXPath(advancedSpanXpath);
			if (span != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get advanced span retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return span;
	}

	/**
	 * Finds &lt;table class="advancedBody"&gt; in the given &lt;div class="advancedLink"&gt;.
	 * @param div target division of Advanced configuration.
	 * @return HtmlTable instance.
	 */
	public HtmlTable findAdvancedTableIn(final HtmlDivision div) {
		String	advancedTableXpath = getXpathOfAdvancedTableFromTop(div);
		HtmlTable table = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			table = (HtmlTable) this.testOwner.configPage().getFirstByXPath(advancedTableXpath);
			if (table != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get advanced table retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return table;
	}

	/**
	 * Expands the 1st advanced configurations by clicking the button.
	 */
	public void expandAdvancedProperties() {
		this.expandAdvancedProperties(0);
	}

	/**
	 * Expands the N-th advanced configurations by clicking the button.
	 * @param index N-th number - 1.
	 */
	public void expandAdvancedProperties(final int index) {
		// 高度な設定の開始divを取得
		HtmlDivision advancedDiv = this.findAdvancedDivision(index);
		// 高度な設定のボタン
		String	advancedButtonXpath = getXpathOfAdvancedButtonFromTop(advancedDiv);
		HtmlButton advancedButton = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			advancedButton = (HtmlButton) this.testOwner.configForm().getFirstByXPath(advancedButtonXpath);
			if (advancedButton != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get advanced table retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.testOwner.configHtmlUI().waitForBackgroundProcess();
			}
		}
		if (advancedButton == null) {
			fail("Advanced button not found.");
		}
		System.out.println("[TEST] Advanced button:" + advancedButton.toString());
		// ボタン押下
		System.out.println("[TEST] === click the Advanced button.");
		try {
			advancedButton.click();
			this.testOwner.configHtmlUI().waitLongForBackgroundProcess();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.xmlPath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findXmlPathThenVerify(final String expected, final boolean check) {
		return this.findXmlPathThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.xmlPath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findXmlPathThenVerify(final int index, final String expected, final boolean check) {
		HtmlTextInput xmlpathTextbox = this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("xmlPath", index, expected, check);
		//this.testOwner.configHtmlUI().verifyTrowStyleInAdvanced("xmlPath", xmlpathTextbox, true);
		return xmlpathTextbox;
	}

	/**
	 * About 1st &lt;input type="text" name="_.xmlPath"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfXmlPathAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfXmlPathAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;input type="text" name="_.xmlPath"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfXmlPathAboutVisibility(final int index, final boolean visible) {
		HtmlTextInput xmlpathTextbox =
				(HtmlTextInput) this.testOwner.configHtmlUI().findInputProperty("xmlPath", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInAdvanced("xmlPath", xmlpathTextbox, visible);
	}

	/**
	 * Finds 1st  &lt;input type="text" name="_.includePattern"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findIncludePatternThenVerify(final String expected, final boolean check) {
		return this.findIncludePatternThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th  &lt;input type="text" name="_.includePattern"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number - 1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findIncludePatternThenVerify(final int index, final String expected, final boolean check) {
		HtmlTextInput includeTextbox = this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("includePattern", index, expected, check);
		//this.testOwner.configHtmlUI().verifyTrowStyleInAdvanced("includePattern", includeTextbox, true);
		return includeTextbox;
	}

	/**
	 * About 1st &lt;input type="text" name="_.includePattern"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfIncludePatternAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfIncludePatternAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;input type="text" name="_.includePattern"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfIncludePatternAboutVisibility(final int index, final boolean visible) {
		HtmlTextInput includeTextbox = (HtmlTextInput) this.testOwner.configHtmlUI()
				.findInputProperty("includePattern", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("includePattern", includeTextbox, visible);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.excludePattern"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findExcludePatternThenVerify(final String expected, final boolean check) {
		return this.findExcludePatternThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.excludePattern"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number -1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findExcludePatternThenVerify(final int index, final String expected, final boolean check) {
		HtmlTextInput excludeTextbox = this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("excludePattern", index, expected, check);
		//this.testOwner.configHtmlUI().verifyTrowStyleInAdvanced("excludePattern", excludeTextbox, true);
		return excludeTextbox;
	}

	/**
	 * About 1st &lt;input type="text" name="_.excludePattern"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfExcludePatternAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfExcludePatternAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;input type="text" name="_.excludePattern"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfExcludePatternAboutVisibility(final int index, final boolean visible) {
		HtmlTextInput excludeTextbox = (HtmlTextInput) this.testOwner.configHtmlUI()
				.findInputProperty("excludePattern", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("excludePattern", excludeTextbox, visible);
	}

	/**
	 * Finds 1st &lt;input type="text" name="_.oldSourceDirectory"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findOldSourceDirectoryThenVerify(final String expected, final boolean check) {
		return this.findOldSourceDirectoryThenVerify(0, expected, check);
	}

	/**
	 * Finds N-th &lt;input type="text" name="_.oldSourceDirectory"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number -1.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findOldSourceDirectoryThenVerify(final int index, final String expected,
			final boolean check) {
		HtmlTextInput olddirTextbox = this.testOwner.configHtmlUI()
				.findInputPropertyThenVerify("oldSourceDirectory", index, expected, check);
		//this.testOwner.configHtmlUI().verifyTrowStyleInAdvanced("oldSourceDirectory", olddirTextbox, true);
		return olddirTextbox;
	}

	/**
	 * About 1st &lt;input type="text" name="_.oldSourceDirectory"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfOldSourceDirectoryAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(0, visible);
	}

	/**
	 * About N-th &lt;input type="text" name="_.oldSourceDirectory"&gt;,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfOldSourceDirectoryAboutVisibility(final int index, final boolean visible) {
		HtmlTextInput olddirTextbox = (HtmlTextInput) this.testOwner.configHtmlUI()
				.findInputProperty("oldSourceDirectory", index);
		this.testOwner.configHtmlUI().verifyTrowStyleInOption("oldSourceDirectory", olddirTextbox, visible);
	}

	/**
	 * Finds 1st &lt;input type="radio" value="step_simply"&gt;.
	 * @return HtmlRadioButtonInput instance.
	 */
	public HtmlRadioButtonInput findHeadRadioOfCountingMode() {
		return this.findHeadRadioOfCountingMode(0);
	}

	/**
	 * Finds N-th &lt;input type="radio" value="step_simply"&gt;.
	 * @param index N-th number -1.
	 * @return HtmlRadioButtonInput instance.
	 */
	public HtmlRadioButtonInput findHeadRadioOfCountingMode(final int index) {
		List<HtmlRadioButtonInput> radioList = new ArrayList<HtmlRadioButtonInput>();
		for (HtmlInput input : this.testOwner.configForm().getInputsByValue(FIRST_COUNTING_MODE.getValue())) {
			if (input.getAttribute("type").equals("radio")) {
				radioList.add((HtmlRadioButtonInput) input);
			}
		}
		if (index < radioList.size()) {
			return radioList.get(index);
		}
		return null;
	}

	/**
	 * Gets name of 1st &lt;input type="radio"&gt; for CountingMode in config page.
	 * @return name of radio buttons.
	 */
	public String getRadioNameOfCountingMode() {
		return this.getRadioNameOfCountingMode(0);
	}

	/**
	 * Gets name of N-th &lt;input type="radio"&gt; for CountingMode in config page.
	 * @param index N-th number -1.
	 * @return name of radio buttons.
	 */
	public String getRadioNameOfCountingMode(final int index) {
		HtmlRadioButtonInput radio = this.findHeadRadioOfCountingMode(index);
		return radio.getAttribute("name");
	}

	/**
	 * Gets list of radio buttons for CountingMode of 1st CountingUnit in config page.
	 * @return list of radio buttons.
	 */
	public List<HtmlRadioButtonInput> getRadiosOfCountingMode() {
		return this.getRadiosOfCountingMode(0);
	}

	/**
	 * Gets list of radio buttons for CountingMode of N-th CountingUnit in config page.
	 * @param index N-th number -1.
	 * @return list of radio buttons.
	 */
	public List<HtmlRadioButtonInput> getRadiosOfCountingMode(final int index) {
		String radioName = this.getRadioNameOfCountingMode(index);
		return this.testOwner.configHtmlUI().findRadioElements(radioName);
	}

	/**
	 * Gets the radio button which is selected for CountingMode in config page.
	 * @param radioList list of radio buttons for CountingMode
	 * @param mode selected one of CountingMode.
	 * @return radio button which is selected.
	 */
	public HtmlRadioButtonInput getRadioByCountingMode(final List<HtmlRadioButtonInput> radioList,
			final String mode) {
		for (HtmlRadioButtonInput radio : radioList) {
			String value = radio.getAttribute("value");
			if (mode.equals(value)) {
				return radio;
			}
		}
		return null;
	}

	/**
	 * Finds selected &lt;input type="radio" &gt; of 1st CountingUnit.
	 * Then verifies it is expected mode.
	 * And returns &lt;input&gt; instance.
	 * @param mode expected counting mode.
	 * @return HtmlRadioButtonInput instance.
	 */
	public HtmlRadioButtonInput findSelectedCountingModeThenVerify(final String mode) {
		 return this.findSelectedCountingModeThenVerify(0, mode);
	}

	/**
	 * Finds selected &lt;input type="radio" &gt; of N-th CountingUnit.
	 * Then verifies it is expected mode.
	 * And returns &lt;input&gt; instance.
	 * @param index N-th number -1.
	 * @param mode expected counting mode.
	 * @return HtmlRadioButtonInput instance.
	 */
	public HtmlRadioButtonInput findSelectedCountingModeThenVerify(final int index, final String mode) {
		 List<HtmlRadioButtonInput> radioList = this.getRadiosOfCountingMode(index);
		 for (HtmlRadioButtonInput radio : radioList) {
			 String checked = radio.getAttribute("checked");
			 String value = radio.getAttribute("value");
			 if ("checked".equals(checked) || "true".equals(checked)) {
				 assertThat(mode, equalTo(value));
				 return radio;
			 } else {
				 assertThat(mode, is(not(equalTo(value))));
			 }
		 }
		 return null;
	}

	/**
	 * About list of radio buttons for CountingMode of 1st CountingUnit in config page,
	 * verifies its visibility is expected boolean value.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfCountingModeAboutVisibility(final boolean visible) {
		this.verifyTrowStyleOfCountingModeAboutVisibility(0, visible);
	}

	/**
	 * About list of radio buttons for CountingMode of N-th CountingUnit in config page,
	 * verifies its visibility is expected boolean value.
	 * @param index N-th number - 1.
	 * @param visible expected boolean.
	 */
	public void verifyTrowStyleOfCountingModeAboutVisibility(final int index, final boolean visible) {
		String radioName = this.getRadioNameOfCountingMode(index);
		for (HtmlRadioButtonInput radio : this.testOwner.configHtmlUI().findRadioElements(radioName)) {
			StringBuilder sb  = new StringBuilder();
			sb.append("countingMode").append("[").append(radio.getValueAttribute()).append("]");
			this.testOwner.configHtmlUI().verifyTrowStyleInAdvanced(sb.toString(), radio, visible);
		}
	}
}
