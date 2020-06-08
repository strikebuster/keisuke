package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.jenkinsci.plugins.keisuke.util.PathUtil.getExistingFilePath;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITLONGTIME;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.WAITTIME;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jenkinsci.plugins.keisuke.uihelper.ConfigUIClient;
import org.jenkinsci.plugins.keisuke.uihelper.GlobalSettingConfigUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


/**
 * Testing GlobalSetting in Global(System) config page.
 */
public class GlobalSettingUITest implements ConfigUIClient {

	private JenkinsRule.WebClient webClient = null;
	private HtmlPage globalConfigPage = null;
	private HtmlForm globalConfigForm = null;
	private GlobalSettingConfigUI globalUI = null;

	// ConfigUIClient methods.
	/**
	 * Gets configuration page of freestyle project
	 * @return configuration page
	 */
	@Override
	public HtmlPage configPage() {
		return this.globalConfigPage;
	}

	/**
	 * Gets config form in the config page of freestyle project
	 * @return config form
	 */
	@Override
	public HtmlForm configForm() {
		return this.globalConfigForm;
	}

	// Test rule methods.
	@Before
	public void setUp() throws Exception {
		this.webClient = this.jenkinsRule.createWebClient();
		this.prepareKeisukeGlobalConfig();
		this.globalUI = new GlobalSettingConfigUI(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public JenkinsRule jenkinsRule = new JenkinsRule();

	// common methods fot this test.
	private void prepareKeisukeGlobalConfig() {
		try {
			this.globalConfigPage = this.webClient.goTo("configure");
			this.globalConfigForm = this.globalConfigPage.getFormByName("config");
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}  catch (SAXException ex) {
			ex.printStackTrace();
			fail("Unexpected SAXException is occured.");
		}  catch (ElementNotFoundException ex) {
			ex.printStackTrace();
			fail("Unexpected ElementNotFoundException is occured.");
		}
	}

	/**
	 * Waits some time for background process which is called by some UI event.
	 */
	private void waitForEventCallbackProcess() {
		this.webClient.waitForBackgroundJavaScript(WAITTIME);
	}

	/**
	 * Waits some long time for background process which is called by some UI event.
	 */
	private void waitLongForEventCallbackProcess() {
		this.webClient.waitForBackgroundJavaScript(WAITLONGTIME);
	}
	// Test case methods.
	@Test
	public void configureDefaultValueWithNoInput() {
		System.out.println("## GlobalSettingUI ## configureDefaultValueWithNoInput ##");
		HtmlTableRow keisukeTrow = this.globalUI.findKeisukeConfigTrow();

		// タイトルの検証
		System.out.println("[TEST] keisukeTrow XPath:" + keisukeTrow.getCanonicalXPath());
		HtmlDivision titleDiv = this.globalUI.findKeisukeTitleDivisionFromTop(keisukeTrow);
		//System.out.println("[TEST] title XPath:" + title.getCanonicalXPath());
		System.out.println("[TEST] title Content:" + titleDiv.getTextContent());
		String expected = "Keisuke";
		assertThat(titleDiv.getTextContent(), containsString(expected));

		// エンコード入力ボックスの検証
		this.globalUI.findEncodingThenVerify("", true); // 初期値"", エラー表示なし
		// XMLファイルパス入力ボックスの検証
		this.globalUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		try {
			this.jenkinsRule.submit(this.globalConfigForm);
			this.waitLongForEventCallbackProcess();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
		// 再度設定画面を開く
		this.prepareKeisukeGlobalConfig();
		// エンコード入力ボックスの検証
		this.globalUI.findEncodingThenVerify(System.getProperty("file.encoding"), true); // システムデフォルト値, エラー表示なし
		// XMLファイルパス入力ボックスの検証
		this.globalUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
	}

	@Test
	public void configureWithSpecifiedValue() {
		System.out.println("## GlobalSettingUI ## configureWithSpecifiedValue ##");

		HtmlTextInput encodingTextbox = this.globalUI.findEncodingThenVerify("", true); // 初期値"", エラー表示なし
		// 存在するエンコード名を指定
		System.out.println("[TEST] === input a good value into encoding.");
		String encodingGoodValue = "Shift_JIS";
		this.globalUI.inputValue(encodingTextbox, encodingGoodValue);
		this.waitForEventCallbackProcess();
		this.globalUI.findEncodingThenVerify(encodingGoodValue, true); // 指定値, エラー表示なし

		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.globalUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		// 存在するパスを指定
		System.out.println("[TEST] === input a good value into xmlPath.");
		String xmlpathGoodValue = getExistingFilePath();
		this.globalUI.inputValue(xmlpathTextbox, xmlpathGoodValue);
		this.waitForEventCallbackProcess();
		this.globalUI.findXmlPathThenVerify(xmlpathGoodValue, true); // 指定値, エラー表示なし
		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		try {
			this.jenkinsRule.submit(this.globalConfigForm);
			this.waitLongForEventCallbackProcess();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
		this.prepareKeisukeGlobalConfig();
		// 保存した値が設定されている
		this.globalUI.findEncodingThenVerify(encodingGoodValue, true); // 指定値, エラー表示なし
		this.globalUI.findXmlPathThenVerify(xmlpathGoodValue, true); // 指定値, エラー表示なし
	}

	@Test
	public void checkInvalidValues() {
		System.out.println("## GlobalSettingUI ## checkInvalidValues ##");

		// エンコード入力ボックスの検証
		HtmlTextInput encodingTextbox = this.globalUI.findEncodingThenVerify("", true); // 初期値"", エラー表示なし
		// 存在しないエンコード名を指定
		System.out.println("[TEST] === input a bad value into encoding.");
		String encodingBadValue = "WRONG-ENCODING";
		this.globalUI.inputValue(encodingTextbox, encodingBadValue);
		this.waitForEventCallbackProcess();
		this.globalUI.findEncodingThenVerify(encodingBadValue, false); // 指定値, エラー表示あり

		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.globalUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		// 存在しないパスを指定
		System.out.println("[TEST] === input a bad value into xmlPath.");
		String xmlpathBadValue = "WRONG/XML/FILE/PATH";
		this.globalUI.inputValue(xmlpathTextbox, xmlpathBadValue);
		this.waitForEventCallbackProcess();
		this.globalUI.findXmlPathThenVerify(xmlpathBadValue, false); // 指定値, エラー表示あり
	}

	@Test
	public void showHelpAboutGlobalProperties() {
		System.out.println("## GlobalSettingUI ## showHelpAboutGlobalProperties ##");

		String helpText;
		// エンコード入力ボックスの検証
		helpText = this.globalUI.getHelpContentOfEncoding();
		assertThat(helpText, is(allOf(containsString("file.encoding"), containsString("(e.g. 'UTF-8')"))));

		// XMLファイルパス入力ボックスの検証
		helpText = this.globalUI.getHelpContentOfXmlPath();
		assertThat(helpText, is(allOf(containsString("XML"),
				containsString("(e.g. '/home/develop/conf/language.xml')"))));
	}

}
