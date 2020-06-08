package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.jenkinsci.plugins.keisuke.uihelper.OutputSettingConfigUI.DEFAULT_FORMAT;

import org.jenkinsci.plugins.keisuke.uihelper.OutputSettingConfigUI;
import org.jenkinsci.plugins.keisuke.uihelper.OutputSettingForDiffConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Testing OutputSettingForDiff in project config page.
 */
public class OutputSettingForDiffUITest extends AbstractSettingUITest {

	private OutputSettingConfigUI outputUI = null;
	private OutputSettingForDiffConfigUI diffOutputUI = null;

	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		super.prepareKeisukeConfig();
		this.outputUI = new OutputSettingConfigUI(this);
		this.diffOutputUI = new OutputSettingForDiffConfigUI(this);
		// wait for rendering config page about KeisukePublisher.
		this.waitLongForEventCallbackProcess();
		// 出力指定のチェックと必須項目設定
		this.enableOutputSetting();
	}

	private void enableOutputSetting() {
		this.outputUI.checkOnOutputCheckbox();
		// ファイルパスを検証
		HtmlTextInput pathTextbox = this.outputUI.findOutputFilePath();
		// 任意の値を指定
		String pathGoodValue = "report/result.xml";
		this.diffOutputUI.inputValue(pathTextbox, pathGoodValue);
		this.waitForEventCallbackProcess();
		//this.outputUI.findOutputFilePathThenVerify(pathGoodValue, true);
	}

	@Test
	public void showInitialDiffOutputProperties() {
		System.out.println("## OutputSettingForDiffUI ## showInitialDiffOutputProperties ##");
		this.outputUI.findOutputCheckboxThenVerify(true);
		// 出力指定のインプットは表示
		this.diffOutputUI.findDiffOutputCheckboxThenVerify(false);
		// Diff出力指定のインプットは非表示

		// ファイルパス指定は非表示
		this.diffOutputUI.verifyTrowStyleOfDiffOutputFilePathAboutVisibility(false);
		// フォーマット指定は非表示
		this.diffOutputUI.verifyTrowStyleOfDiffOutputFormatAboutVisibility(false);
	}

	@Test
	public void enableDiffOutputProperties() {
		System.out.println("## OutputSettingForDiffUI ## enableDiffOutputProperties ##");
		// チェックボックス選択
		this.diffOutputUI.checkOnDiffOutputCheckbox();
		// ファイル出力チェックボックスの状態は選択
		this.diffOutputUI.findDiffOutputCheckboxThenVerify(true);
		// ファイルパスを検証
		this.diffOutputUI.findDiffOutputFilePathThenVerify("", false);
		this.diffOutputUI.verifyTrowStyleOfDiffOutputFilePathAboutVisibility(true);
		// フォーマット入力を検証
		this.diffOutputUI.findDiffOutputFormatThenVerify(DEFAULT_FORMAT.value(), true);
		this.diffOutputUI.verifyTrowStyleOfDiffOutputFormatAboutVisibility(true);
	}

	@Test
	public void configureDiffOutputProperties() {
		System.out.println("## OutputSettingForDiffUI ## configureDiffOutputProperties ##");
		// チェックボックス選択
		this.diffOutputUI.checkOnDiffOutputCheckbox();

		String empty = "";
		// ファイルパスを検証
		HtmlTextInput pathTextbox = this.diffOutputUI.findDiffOutputFilePathThenVerify("", false);
		// ””を指定
		System.out.println("[TEST] === input an empty value into diffOutputFilePath.");
		this.diffOutputUI.inputValue(pathTextbox, empty);
		this.waitForEventCallbackProcess();
		this.diffOutputUI.findDiffOutputFilePathThenVerify(empty, false);
		// 任意の値を指定
		System.out.println("[TEST] === input a good value into diffOutputFilePath.");
		String pathGoodValue = "result.csv";
		this.diffOutputUI.inputValue(pathTextbox, pathGoodValue);
		this.waitForEventCallbackProcess();
		this.diffOutputUI.findDiffOutputFilePathThenVerify(pathGoodValue, true);

		// フォーマット入力を検証
		HtmlSelect formatSelectbox = this.diffOutputUI.findDiffOutputFormatThenVerify("text", true);
		// "csv"を選択
		System.out.println("[TEST] === select CSV format.");
		HtmlOption option = formatSelectbox.getOptionByText("CSV");
		System.out.println("[TEST] Option value(will be selected):" + option.getAttribute("value"));
		formatSelectbox.setSelectedAttribute(option, true);
		this.waitForEventCallbackProcess();
		this.diffOutputUI.findDiffOutputFormatThenVerify("csv", true);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 設定内容を確認
		this.diffOutputUI.findDiffOutputCheckboxThenVerify(true);
		this.diffOutputUI.findDiffOutputFilePathThenVerify(pathGoodValue, true);
		this.diffOutputUI.findDiffOutputFormatThenVerify("csv", true);
	}

	@Test
	public void configureDiffOutputPropertiesThenDisable() {
		System.out.println("## OutputSettingFotDiffUI ## configureDiffOutputPropertiesThenDisable ##");
		// チェックボックス選択
		this.diffOutputUI.checkOnDiffOutputCheckbox();

		String empty = "";
		// ファイルパスを検証
		HtmlTextInput pathTextbox = this.diffOutputUI.findDiffOutputFilePathThenVerify("", false);
		// ””を指定
		System.out.println("[TEST] === input an empty value into diffOutputFilePath.");
		this.diffOutputUI.inputValue(pathTextbox, empty);
		this.waitForEventCallbackProcess();
		this.diffOutputUI.findDiffOutputFilePathThenVerify(empty, false);
		// 任意の値を指定
		System.out.println("[TEST] === input a good value into diffOutputFilePath.");
		String pathGoodValue = "result.json";
		this.diffOutputUI.inputValue(pathTextbox, pathGoodValue);
		this.waitForEventCallbackProcess();
		this.diffOutputUI.findDiffOutputFilePathThenVerify(pathGoodValue, true);

		// フォーマット入力を検証
		HtmlSelect formatSelectbox = this.diffOutputUI.findDiffOutputFormatThenVerify("text", true);
		// "csv"を選択
		System.out.println("[TEST] === select JSON format.");
		HtmlOption option = formatSelectbox.getOptionByText("JSON");
		System.out.println("[TEST] Option value(will be selected):" + option.getAttribute("value"));
		formatSelectbox.setSelectedAttribute(option, true);
		this.waitForEventCallbackProcess();
		this.diffOutputUI.findDiffOutputFormatThenVerify("json", true);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 設定内容を確認
		this.diffOutputUI.findDiffOutputCheckboxThenVerify(true);
		this.diffOutputUI.findDiffOutputFilePathThenVerify(pathGoodValue, true);
		this.diffOutputUI.findDiffOutputFormatThenVerify("json", true);

		// チェックボックス非選択
		this.diffOutputUI.checkOffDiffOutputCheckbox();
		// ファイルパス指定は非表示
		this.diffOutputUI.verifyTrowStyleOfDiffOutputFilePathAboutVisibility(false);
		// フォーマット指定は非表示
		this.diffOutputUI.verifyTrowStyleOfDiffOutputFormatAboutVisibility(false);
		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 設定内容を確認
		this.diffOutputUI.findDiffOutputCheckboxThenVerify(false);
	}

	@Test
	public void showHelpAboutDiffOutputProperties() {
		System.out.println("## OutputSettingForDiffUI ## showHelpAboutDiffOutputProperties ##");
		HtmlCheckBoxInput outputCheckbox = this.diffOutputUI.findDiffOutputCheckbox();
		// Diff結果出力チェックボックスのヘルプ
		String helpTextSet = this.configHtmlUI()
				.getHelpContentOfOptionalCheckBox("diffOutputSetting", outputCheckbox);
		assertThat(helpTextSet, is(not(emptyString())));
		// チェックボックス選択
		this.diffOutputUI.checkOnDiffOutputCheckbox();
		// ファイルパスのヘルプ
		HtmlTextInput pathTextbox = this.diffOutputUI.findDiffOutputFilePath();
		helpTextSet = this.configHtmlUI().getHelpContentOf("diffOutputFilePath", pathTextbox);
		assertThat(helpTextSet, containsString("(e.g. 'build/report/result_diff.txt')"));
		// フォーマットのヘルプ
		HtmlSelect formatSelectbox = this.diffOutputUI.findDiffOutputFormat();
		helpTextSet = this.configHtmlUI().getHelpContentOf("diffOutputFormat", formatSelectbox);
		assertThat(helpTextSet, is(not(emptyString())));
	}
}
