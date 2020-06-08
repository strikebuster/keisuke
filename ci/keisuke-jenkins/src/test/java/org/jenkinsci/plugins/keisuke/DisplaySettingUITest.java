package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.jenkinsci.plugins.keisuke.uihelper.DisplaySettingConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;

/**
 * Testing DisplaySetting in project config page.
 */
public class DisplaySettingUITest extends AbstractSettingUITest {

	private DisplaySettingConfigUI displayUI = null;

	// Test rule methods.
	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		super.prepareKeisukeConfig();
		this.displayUI = new DisplaySettingConfigUI(this);
		// wait for rendering config page about KeisukePublisher.
		this.waitLongForEventCallbackProcess();
	}

	// common methods for this test.
	private void configureDisplayStepKindTo(final DisplayStepKindEnum kind) {
		List<HtmlRadioButtonInput> kindRadioList = this.displayUI.getRadiosOfDisplayStepKind();
		// 初期値はcode_only
		int checkedIdx = 0;
		String checkedValue = kindRadioList.get(checkedIdx).getAttribute("value");
		String expected = "code_only";
		assertThat(checkedValue, is(equalTo(expected)));

		// kindを選択
		expected = kind.getValue();
		this.displayUI.setDisplayStepKind(kind);
		this.displayUI.verifyRadioSelected(expected);
	}

	// Test case methods.
	@Test
	public void showInitialDisplayProperties() {
		System.out.println("## DisplaySettingUI ## showInitialDisplayProperties ##");
		// 画面表示設定タイトル
		//HtmlTableRow trow = this.displayUI.findTrowOfDisplaySetting();
		String title = this.displayUI.getTitleOfDisplaySetting();
		System.out.println("[TEST] DisplaySetting title:" + title);
		assertThat(title, is(not(emptyOrNullString())));
		// 行数遷移グラフの表示設定のタイトル
		String kindTitle = this.displayUI.getTitleOfDisplayStepKind();
		System.out.println("[TEST] DisplayStepKind title:" + kindTitle);
		assertThat(kindTitle, is(not(emptyOrNullString())));
		// 表示設定のラジオボタン
		String radioName = this.displayUI.getRadioNameOfDisplayStepKind();
		System.out.println("[TEST] DisplayStepKind 1st radio name:" + radioName);
		assertThat(radioName, endsWith("displayStepKind"));
		String expected = "code_only";
		this.displayUI.verifyRadioSelected(expected);

		// エラー表示がないこと検証
		String errmsg = this.displayUI.getErrorContentOfDisplayStepKind();
		System.out.println("[TEST] ErrorTd[kind] Content:" + errmsg);
		assertThat(errmsg, is(emptyString()));
	}

	@Test
	public void configureDisplayStepKindToWithComment() {
		System.out.println("## DisplaySettingUI ## configureDisplayStepKindToWithComment ##");
		this.configureDisplayStepKindTo(DisplayStepKindEnum.CODE_AND_COMMENT);
	}

	@Test
	public void configureDisplayStepKindToAllSteps() {
		System.out.println("## DisplaySettingUI ## configureDisplayStepKindToAllSteps ##");
		this.configureDisplayStepKindTo(DisplayStepKindEnum.CODE_AND_COMMENT_AND_BLANK);
	}

	@Test
	public void showHelpAboutDisplayProperties() {
		System.out.println("## DisplaySettingUI ## showHelpAboutDisplayProperties ##");
		List<HtmlRadioButtonInput> kindRadioList = this.displayUI.getRadiosOfDisplayStepKind();
		HtmlInput kindRadio = kindRadioList.get(0);
		System.out.println("[TEST] displayStepKind[radio] XPath:" + kindRadio.getCanonicalXPath());

		// 行数遷移グラフ表示行数のヘルプ
		String helpText = this.configHtmlUI().getHelpContentOf("displayStepKind", kindRadio);
		assertThat(helpText, is(not(emptyString())));
	}
}
