package org.jenkinsci.plugins.keisuke.uihelper;

import static org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI.RETRY_FOR_YUI;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfGlobalHeaderDivFromTop;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * UI helper for GlobalSetting configuration of KeisukePublisher in global(system) config page.
 */
public class GlobalSettingConfigUI {

	private ConfigUIClient testOwner = null;
	private ConfigHtmlUI configUI = null;

	/**
	 * Constructs an instance.
	 * @param owner ProjectConfigUITester instance.
	 */
	public GlobalSettingConfigUI(final ConfigUIClient owner) {
		this.testOwner = owner;
		this.configUI = new ConfigHtmlUI(owner);
		//this.debugAllTableRows();
	}

	/**
	 * Finds &lt;trow name="org-jenkinsci-plugins-keisuke-KeisukePublisher"&gt; in global config page.
	 * @return HtmlTableRow instance.
	 */
	public HtmlTableRow findKeisukeConfigTrow() {
		// /html/body/div[5]/div[2]/form/table/tbody/tr[122]
		HtmlTableRow trow = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			try {
				trow = (HtmlTableRow) this.testOwner.configPage()
						.getElementByName("org-jenkinsci-plugins-keisuke-KeisukePublisher");
				if (trow != null) {
					break;
				}
			} catch (ElementNotFoundException ex) {
				System.out.println("[TEST DEBUG] get trow[KeisukePublisher] retry : " + retry);
			}
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configUI.waitForBackgroundProcess();
			}
		}
		if (trow == null) {
			fail("org-jenkinsci-plugins-keisuke-KeisukePublisher not found.");
		}
		return trow;
	}

	/**
	 * Finds &lt;div&gt; for title content of Keisuke.
	 * @param topTrow top TableRow for KeisukePublisher.
	 * @return HtmlDivision instance.
	 */
	public HtmlDivision findKeisukeTitleDivisionFromTop(final HtmlTableRow topTrow) {
		String headerXPath = getXPathOfGlobalHeaderDivFromTop(topTrow);
		//System.out.println("[TEST] headerXPath:" + headerXPath);
		// headerXPath: /html/body/div[5]/div[2]/form/table/tbody/tr[122+1]/td[1]/div
		return (HtmlDivision) this.testOwner.configPage().getFirstByXPath(headerXPath);
	}

	/**
	 * Inputs value into HtmlTextInput.
	 * @param textbox instance of HtmlTextInput
	 * @param value string to be filled
	 */
	public void inputValue(final HtmlTextInput textbox, final String value) {
		this.configUI.inputValue(textbox, value);
	}

	/**
	 * Finds &lt;input type="text" name="_.encoding"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findEncodingThenVerify(final String expected, final boolean check) {
		return this.configUI.findInputPropertyThenVerify("encoding", expected, check);
	}

	/**
	 * Finds &lt;input type="text" name="_.xmlPath"&gt;.
	 * Then verifies it has expected value, and error checking is expected boolean value.
	 * And returns &lt;input&gt; instance.
	 * @param expected expected value.
	 * @param check expected boolean.
	 * @return HtmlTextInput instance.
	 */
	public HtmlTextInput findXmlPathThenVerify(final String expected, final boolean check) {
		return this.configUI.findInputPropertyThenVerify("xmlPath", expected, check);
	}

	/**
	 * Gets help message of encoding property.
	 * @return help text.
	 */
	public String getHelpContentOfEncoding() {
		return this.getHelpContentOfProperty("encoding");
	}

	/**
	 * Gets help message of xmlPath property.
	 * @return help text.
	 */
	public String getHelpContentOfXmlPath() {
		return this.getHelpContentOfProperty("xmlPath");
	}

	private String getHelpContentOfProperty(final String property) {
		HtmlInput input = this.configUI.findInputProperty(property);
		return this.configUI.getHelpContentOf(property, input);
	}

	private String getPageTitle() {
		return this.testOwner.configPage().getTitleText();
	}

	private String getBodyId() {
		return this.testOwner.configPage().getBody().getId();
	}

	private String getConfigFormName() {
		return this.testOwner.configForm().getNameAttribute();
	}

	/**
	 * debug information.
	 */
	public void debugAllTableRows() {
		System.out.println("[TEST DEBUG] page title : " + this.getPageTitle());
		System.out.println("[TEST DEBUG] body id : " + this.getBodyId());
		System.out.println("[TEST DEBUG] form name : " + this.getConfigFormName());

		Iterator<DomElement> elements = this.testOwner.configForm().getChildElements().iterator();
		while (elements.hasNext()) {
			DomElement element = elements.next();
			System.out.println("[TEST DEBUG] element tag : " + element.getTagName());
			if (!(element instanceof HtmlTable)) {
				continue;
			}
			HtmlTable table = (HtmlTable) element;
			List<HtmlTableRow> trows = table.getRows();
			for (HtmlTableRow trow : trows) {
				String name = trow.getAttribute("name");
				if (name != null && !name.isEmpty()) {
					System.out.println("[TEST DEBUG] trow name : " + name + "xpath = "
							+ trow.getCanonicalXPath());
				}
			}
		}
	}
}
