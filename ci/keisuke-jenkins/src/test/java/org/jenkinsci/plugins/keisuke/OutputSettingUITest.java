package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.jenkinsci.plugins.keisuke.uihelper.OutputSettingConfigUI.DEFAULT_FORMAT;

import org.jenkinsci.plugins.keisuke.uihelper.OutputSettingConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Testing OutputSetting in project config page.
 */
public class OutputSettingUITest extends AbstractSettingUITest {

	private OutputSettingConfigUI outputUI = null;

	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		super.prepareKeisukeConfig();
		this.outputUI = new OutputSettingConfigUI(this);
		// wait for rendering config page about KeisukePublisher.
		this.waitLongForEventCallbackProcess();
	}

	@Test
	public void showInitialOutputProperties() {
		System.out.println("## OutputSettingUI ## showInitialOutputProperties ##");
		this.outputUI.findOutputCheckboxThenVerify(false);
		// 出力指定のインプットは非表示
		// ファイルパス指定は非表示
		this.outputUI.verifyTrowStyleOfOutputFilePathAboutVisibility(false);
		// フォーマット指定は非表示
		this.outputUI.verifyTrowStyleOfOutputFormatAboutVisibility(false);
		// ディレクトリ名表記指定は非表示
		this.outputUI.verifyTrowStyleOfBaseDirInclusionAboutVisibility(false);
	}

	@Test
	public void enableOutputProperties() {
		System.out.println("## OutputSettingUI ## enableOutputProperties ##");
		// チェックボックス選択
		this.outputUI.checkOnOutputCheckbox();
		// ファイル出力チェックボックスの状態は選択
		this.outputUI.findOutputCheckboxThenVerify(true);
		// ファイルパスを検証
		this.outputUI.findOutputFilePathThenVerify("", false);
		this.outputUI.verifyTrowStyleOfOutputFilePathAboutVisibility(true);
		// フォーマット入力を検証
		this.outputUI.findOutputFormatThenVerify(DEFAULT_FORMAT.value(), true);
		this.outputUI.verifyTrowStyleOfOutputFormatAboutVisibility(true);
		// ディレクトリ名表記チェックボックスを検証
		this.outputUI.findBaseDirInclusionThenVerify(false);
	}

	@Test
	public void configureOutputProperties() {
		System.out.println("## OutputSettingUI ## configureOutputProperties ##");
		// チェックボックス選択
		this.outputUI.checkOnOutputCheckbox();

		String empty = "";
		// ファイルパスを検証
		HtmlTextInput pathTextbox = this.outputUI.findOutputFilePathThenVerify("", false);
		// ””を指定
		System.out.println("[TEST] === input an empty value into outputFilePath.");
		pathTextbox.setText(empty);
		this.waitForEventCallbackProcess();
		this.outputUI.findOutputFilePathThenVerify(empty, false);
		// 任意の値を指定
		System.out.println("[TEST] === input a good value into outputFilePath.");
		String pathGoodValue = "result.csv";
		pathTextbox.setText(pathGoodValue);
		this.waitForEventCallbackProcess();
		this.outputUI.findOutputFilePathThenVerify(pathGoodValue, true);

		// フォーマット入力を検証
		HtmlSelect formatSelectbox = this.outputUI.findOutputFormatThenVerify("text", true);
		// "csv"を選択
		System.out.println("[TEST] === select CSV format.");
		HtmlOption option = formatSelectbox.getOptionByText("CSV");
		System.out.println("[TEST] Option value(will be selected):" + option.getAttribute("value"));
		formatSelectbox.setSelectedAttribute(option, true);
		this.waitForEventCallbackProcess();
		this.outputUI.findOutputFormatThenVerify("csv", true);

		// ディレクトリ名表記チェックボックスを検証
		HtmlCheckBoxInput dirCheckbox = this.outputUI.findBaseDirInclusionThenVerify(false);
		// チェック
		System.out.println("[TEST] === check on baseDirInclusion.");
		dirCheckbox.setChecked(true);
		this.waitForEventCallbackProcess();
		this.outputUI.findBaseDirInclusionThenVerify(true);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 設定内容を確認
		this.outputUI.findOutputCheckboxThenVerify(true);
		this.outputUI.findOutputFilePathThenVerify(pathGoodValue, true);
		this.outputUI.findOutputFormatThenVerify("csv", true);
		this.outputUI.findBaseDirInclusionThenVerify(true);
	}

	@Test
	public void configureOutputPropertiesThenDisable() {
		System.out.println("## OutputSettingUI ## configureOutputPropertiesThenDisable ##");
		// チェックボックス選択
		this.outputUI.checkOnOutputCheckbox();

		// ファイルパスを検証
		HtmlTextInput pathTextbox = this.outputUI.findOutputFilePathThenVerify("", false);
		// 任意の値を指定
		System.out.println("[TEST] === input a good value into outputFilePath.");
		String pathGoodValue = "report/result.xml";
		pathTextbox.setText(pathGoodValue);
		this.waitForEventCallbackProcess();
		this.outputUI.findOutputFilePathThenVerify(pathGoodValue, true);

		// フォーマット入力を検証
		HtmlSelect formatSelectbox = this.outputUI.findOutputFormatThenVerify("text", true);
		// "xml"を選択
		System.out.println("[TEST] === select XML format.");
		HtmlOption option = formatSelectbox.getOptionByText("XML");
		System.out.println("[TEST] Option value(will be selected):" + option.getAttribute("value"));
		formatSelectbox.setSelectedAttribute(option, true);
		this.waitForEventCallbackProcess();
		this.outputUI.findOutputFormatThenVerify("xml", true);

		// ディレクトリ名表記チェックボックスを検証
		HtmlCheckBoxInput dirCheckbox = this.outputUI.findBaseDirInclusionThenVerify(false);
		// チェック
		System.out.println("[TEST] === check on baseDirInclusion.");
		dirCheckbox.setChecked(true);
		this.waitForEventCallbackProcess();
		this.outputUI.findBaseDirInclusionThenVerify(true);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 設定内容を確認
		this.outputUI.findOutputCheckboxThenVerify(true);
		this.outputUI.findOutputFilePathThenVerify(pathGoodValue, true);
		this.outputUI.findOutputFormatThenVerify("xml", true);
		this.outputUI.findBaseDirInclusionThenVerify(true);

		// チェックボックス非選択
		System.out.println("[TEST] === check off about outputSetting.");
		this.outputUI.checkOffOutputCheckbox();
		// ファイルパス指定は非表示
		this.outputUI.verifyTrowStyleOfOutputFilePathAboutVisibility(false);
		// フォーマット指定は非表示
		this.outputUI.verifyTrowStyleOfOutputFormatAboutVisibility(false);
		// ディレクトリ名表記指定は非表示
		this.outputUI.verifyTrowStyleOfBaseDirInclusionAboutVisibility(false);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 設定内容を確認
		this.outputUI.findOutputCheckboxThenVerify(false);
	}

	@Test
	public void showHelpAboutOutputProperties() {
		System.out.println("## OutputSettingUI ## showHelpAboutOutputProperties ##");
		HtmlCheckBoxInput outputCheckbox = this.outputUI.findOutputCheckbox();
		// ファイル出力チェックボックスのヘルプ
		String helpTextSet = this.configHtmlUI().
				getHelpContentOfOptionalCheckBox("outputSetting", outputCheckbox);
		assertThat(helpTextSet, not(isEmptyString()));

		// チェックボックス選択
		this.outputUI.checkOnOutputCheckbox();
		// ファイルパスのヘルプ
		HtmlTextInput pathTextbox = this.outputUI.findOutputFilePath();
		helpTextSet = this.configHtmlUI().getHelpContentOf("outputFilePath", pathTextbox);
		assertThat(helpTextSet, containsString("(e.g. 'build/report/result.txt')"));
		// フォーマットのヘルプ
		HtmlSelect formatSelectbox = this.outputUI.findOutputFormat();
		helpTextSet = this.configHtmlUI().getHelpContentOf("outputFormat", formatSelectbox);
		assertThat(helpTextSet, not(isEmptyString()));
		// ディレクトリ名表記のヘルプ
		HtmlCheckBoxInput dirCheckbox = this.outputUI.findBaseDirInclusion();
		helpTextSet = this.configHtmlUI().getHelpContentOf("baseDirInclusion", dirCheckbox);
		assertThat(helpTextSet, allOf(containsString("-path base"), containsString("-path sub")));
	}
}
