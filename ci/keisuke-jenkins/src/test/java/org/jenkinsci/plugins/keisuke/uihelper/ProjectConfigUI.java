package org.jenkinsci.plugins.keisuke.uihelper;

import static org.jenkinsci.plugins.keisuke.setup.ProjectMakingUtil.createProjectJob;
import static org.jenkinsci.plugins.keisuke.uihelper.ConfigHtmlUI.RETRY_FOR_YUI;
import static org.jenkinsci.plugins.keisuke.util.HtmlTestUtil.getXPathOfKeisukeTitleFromTop;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.jenkinsci.plugins.keisuke.GlobalSetting;
import org.jenkinsci.plugins.keisuke.JenkinsUITester;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import hudson.model.FreeStyleProject;

/**
 * UI helper for Jenkins freestyle project config page using KeisukePublisher,
 * and preparing some useful variables.
 */
public class ProjectConfigUI implements ConfigUIClient {

	private static final String TEST_PROJECT = "Test";

	private JenkinsUITester testOwner = null;
	private FreeStyleProject project = null;
	private HtmlPage configPage = null;
	private HtmlForm configForm = null;
	private ConfigHtmlUI configUI = null;

	/**
	 * Constructs an instance.
	 * The instance has owner object which is Jenkins test main class implements JenkinsUITester.
	 * @param owner JenkinsUITester instance.
	 */
	public ProjectConfigUI(final JenkinsUITester owner) {
		this.testOwner = owner;
	}

	/**
	 * Constructs an instance.
	 * The instance has owner object which is Jenkins test main class implements JenkinsUITester.
	 * And existed FreeStyleProject instance is set to this project.
	 * @param owner JenkinsUITester instance.
	 * @param fsProject FreeStyleProject instance.
	 */
	public ProjectConfigUI(final JenkinsUITester owner, final FreeStyleProject fsProject) {
		this.testOwner = owner;
		this.project = fsProject;
	}

	// ConfigUIClient methods.
	/**
	 * Gets configuration page of freestyle project
	 * @return configuration page
	 */
	@Override
	public HtmlPage configPage() {
		return this.configPage;
	}

	/**
	 * Gets config form in the config page of freestyle project
	 * @return config form
	 */
	@Override
	public HtmlForm configForm() {
		return this.configForm;
	}

	// methods for project configure.
	/**
	 * Gets HtmlTestOperation instance for the config page of freestyle project
	 * which uses KeisukePublisher.
	 * @return HtmlTestOperation instance.
	 */
	public ConfigHtmlUI configHtmlUI() {
		return this.configUI;
	}

	/**
	 * Gets prepared freestyle project
	 * @return freestyle project
	 */
	public FreeStyleProject project() {
		return this.project;
	}

	/**
	 * Prepares KeisukePublisher initial configuration by web page ui.
	 */
	public void prepareKeisukeConfig() {
		this.prepareKeisukeConfigWithGlobalSetting(null);
	}

	/**
	 * Prepares KeisukePublisher initial configuration by web page ui,
	 * with global configuration.
	 * @param setting GlobalSetting instance
	 */
	public void prepareKeisukeConfigWithGlobalSetting(final GlobalSetting setting) {
		this.configureGlobalSetting(setting);
		try {
			this.project = createProjectJob(this.testOwner.jenkinsRule(), TEST_PROJECT);
			this.openProjectConfigPage();
			this.addKeisukeAtConfigPage();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}
		this.configUI = new ConfigHtmlUI(this);
	}

	/**
	 * Opens the project config page, and prepares config form.
	 */
	public void openProjectConfigPage() {
		String urlpath = "job/" + this.project.getName() + "/configure";
		System.out.println("[TEST] open config url:" + urlpath);
		try {
			this.configPage = this.testOwner.webClient().goTo(urlpath);
			this.configForm = this.configPage.getFormByName("config");
		} catch (IOException ex) {
			ex.printStackTrace();
			fail("Unexpected IOException is occured.");
		}  catch (SAXException ex) {
			ex.printStackTrace();
			fail("Unexpected SAXException is occured.");
		}
		this.configUI = new ConfigHtmlUI(this);
	}

	/**
	 * Gets top division of KeisukePublisher in the config page of freestyle project
	 * @return top division
	 */
	public HtmlDivision getDivisionOfKeisuke() {
		HtmlDivision topDivision = this.findDivOfKeisukePublisher();
		//System.out.println("[TEST DEBUG] KeisukePublisher topDivXPath:" + topDivision.getCanonicalXPath());
		return topDivision;
	}

	private void addKeisukeAtConfigPage() throws IOException {
		HtmlElement node = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			// tag is NOT "input", but "button"
			node = this.configForm.getOneHtmlElementByAttribute("button", "suffix", "publisher");
			if (node != null && node.getAttribute("type").equals("button")) {
				break;
			}
			// not found
			System.out.println("[TEST DEBUG] get add button retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configHtmlUI().waitForBackgroundProcess();
			}
		}
		if (node == null) {
			fail("fail to find 'add' button");
		}
		HtmlButton addButton = (HtmlButton) node;
		addButton.click();
		HtmlAnchor keisukeAnchor = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			// wait for rendering overlay menu.
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configHtmlUI().waitForBackgroundProcess();
			}
			// find overlay menu
			node = this.configForm.getOneHtmlElementByAttribute("div", "class",
					"yui-module yui-overlay yuimenu yui-button-menu yui-menu-button-menu visible");
			//System.out.println("[TEST DEBUG] overlay-menu XPath:" + node.getCanonicalXPath());
			// find menu item KeisukePublisher
			keisukeAnchor = this.getAnchorOfKeisukeInMenu((HtmlDivision) node);
			if (keisukeAnchor != null) {
				break;
			}
			System.out.println("[TEST DEBUG] get Keisue menu retry : " + retry);
		}
		//System.out.println("[TEST DEBUG] menu-item Keisuke XPath:" + keisukeAnchor.getCanonicalXPath());
		System.out.println("[TEST] menu-item Keisuke Text:" + keisukeAnchor.getTextContent());
		// Click on the menu item
		keisukeAnchor.click();
		// wait for rendering KeisukePublisher configurations.
		this.configHtmlUI().waitLongForBackgroundProcess();
	}

	/**
	 * プロジェクトの設定ページで「ビルド後の処理を追加」で表示される選択リストメニューから
	 * KeisukePublisherのアンカーを返す
	 * @param menu 「ビルド後の処理を追加」で表示される選択リストメニュー
	 * @return KeisukePublisherのアンカー
	 */
	private HtmlAnchor getAnchorOfKeisukeInMenu(final HtmlDivision menu) {
		if (menu == null) {
			return  null;
		}
		HtmlPage page = menu.getHtmlPageOrNull();
		String listXPath = menu.getCanonicalXPath() + "/div[1]/ul[1]/li";
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			List<DomElement> nodes = page.getByXPath(listXPath);
			for (int i = 1; i <= nodes.size(); i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(listXPath).append('[').append(i).append("]/a");
				String xpath = sb.toString();
				List<DomElement> anchors = page.getByXPath(xpath);
				for (DomElement a : anchors) {
					HtmlAnchor anchor = (HtmlAnchor) a;
					String text = anchor.getTextContent();
					if (text.startsWith("Keisuke")) {
						return anchor;
					}
				}
			}
			// not found
			System.out.println("[TEST DEBUG] get Keisuke menu anchor retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configHtmlUI().waitForBackgroundProcess();
			}
		}
		System.out.println("[TEST] menu-item Keisuke not found");
		return null;
	}

	/**
	 * Finds the &lt;div descriptorid="org.jenkinsci.plugins.keisuke.KeisukePublisher" /&gt;
	 * @return HtmlDivision
	 */
	private HtmlDivision findDivOfKeisukePublisher() {
		HtmlDivision division = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			List<DomElement> nodes = this.configPage.getElementsByName("publisher");
			for (DomElement node : nodes) {
				//System.out.println("element:" + node.getLocalName() + ":"
				//		+ node.getAttribute("name"));
				String descriptorId = node.getAttribute("descriptorid");
				if (descriptorId == null || descriptorId.isEmpty()) {
					continue;
				}
				//System.out.println("descriptorid:" + descriptorId);
				if (descriptorId.equals("org.jenkinsci.plugins.keisuke.KeisukePublisher")) {
					division = (HtmlDivision) node;
					//System.out.println("[TEST DEBUG] KeisukePublisher XPath:"
					//		+ division.getCanonicalXPath());
					return division;
				}
			}
			// not found
			System.out.println("[TEST DEBUG] get division of keisukePublisher retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return division;
	}

	private void configureGlobalSetting(final GlobalSetting setting) {
		HtmlPage page = null;
		HtmlForm form = null;
		HtmlTextInput encodingTextbox = null;
		HtmlTextInput xmlpathTextbox = null;
		try {
			page = this.testOwner.webClient().goTo("configure");
			for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
				form = page.getFormByName("config");
				if (form != null) {
					encodingTextbox = form.getInputByName("_.encoding");
					xmlpathTextbox = form.getInputByName("_.xmlPath");
				}
				if (encodingTextbox != null && xmlpathTextbox != null) {
					break;
				}
				// not found
				System.out.println("[TEST DEBUG] get global setting items <input> retry : " + retry);
				for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
					this.configHtmlUI().waitForBackgroundProcess();
				}
			}
			if (setting != null) {
				encodingTextbox.setText(setting.getEncoding());
				xmlpathTextbox.setText(setting.getXmlPath());
			}
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
		// 設定を保存
		try {
			this.testOwner.jenkinsRule().submit(form);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Unexpected Exception is occured.");
		}
	}

	/**
	 * Gets title string of KeisukePublisher in config page.
	 * @return title string.
	 */
	public String getTitleOfKeisuke() {
		HtmlDivision topDivision = this.getDivisionOfKeisuke();
		String titleXPath = getXPathOfKeisukeTitleFromTop(topDivision);
		HtmlDivision titleDivision = null;
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			titleDivision = (HtmlDivision) this.configPage.getFirstByXPath(titleXPath);
			if (titleDivision != null) {
				break;
			}
			// not found
			System.out.println("[TEST DEBUG] get <div> retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return titleDivision.getTextContent();
	}

	private static final String KEISUKE_HELP_URL =
			"/jenkins/descriptor/org.jenkinsci.plugins.keisuke.KeisukePublisher/help";

	/**
	 * Finds &lt;a helpUrl="/jenkins/descriptor/org.jenkinsci.plugins.keisuke.KeisukePublisher/help"&gt;
	 * in project config page.
	 * KeisukePublisher設定全体のdivisionの中にあるタイトルのヘルプアンカーのXpathを返す
	 * @return HtmlAnchor instance.
	 */
	public HtmlAnchor findKeisukeHelpAnchor() {
		for (int retry = 0; retry < RETRY_FOR_YUI; retry++) {
			List<HtmlAnchor> anchors = this.configPage.getAnchors();
			for (HtmlAnchor anchor : anchors) {
				String url = anchor.getAttribute("helpURL");
				if (url == null) {
					continue;
				}
				if (url.equals(KEISUKE_HELP_URL)) {
					return anchor;
				}
			}
			// not found
			System.out.println("[TEST DEBUG] get anchor retry : " + retry);
			for (int j = 0; j < retry + 1 && retry < RETRY_FOR_YUI - 1; j++) {
				this.configHtmlUI().waitForBackgroundProcess();
			}
		}
		return null;
	}

	/**
	 * Gets help text of Keisuke in project config page.
	 * @param anchor Keisuke help anchor.
	 * @return help content text.
	 */
	public String getHelpContentFrom(final HtmlAnchor anchor) {
		return this.configUI.getHelpContentOf("Keisuke", anchor, true);
	}
}
