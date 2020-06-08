package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.jenkinsci.plugins.keisuke.setup.TestDataConstant.CUSTOM_LANGUAGE_XML;

import java.io.IOException;

import org.jenkinsci.plugins.keisuke.uihelper.InputSettingConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * testing InputSetting in project config page about couting mode.
 */
public class InputSettingUICountModeTest extends AbstractSettingUITest {

	private InputSettingConfigUI inputUI = null;

	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		this.prepareKeisukeConfig();
		this.inputUI = new InputSettingConfigUI(this);
		// wait for rendering config page about KeisukePublisher.
		this.waitLongForEventCallbackProcess();
	}

	@Test
	public void configureInputPropertiesUsingFileSetModeWithDefault() {
		System.out.println("## InputSettingUI ## configureInputPropertiesUsingFileSetModeWithDefault ##");

		String empty = "";
		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり
		// 任意のカテゴリ名を指定
		System.out.println("[TEST] === input a good value into unitName.");
		String unitGoodValue = "default";
		this.inputUI.inputValue(unitTextbox, unitGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// 任意のパスを指定
		System.out.println("[TEST] === input a good value into sourceDirectory.");
		String srcdirGoodValue = "src/main/java";
		this.inputUI.inputValue(srcdirTextbox, srcdirGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし

		// エンコード入力ボックスの検証
		String defaultEncoding = System.getProperty("file.encoding");
		this.inputUI.findEncodingThenVerify(defaultEncoding, true);
		// 初期値file.encoding, エラー表示なし

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.inputUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		// ""を指定
		System.out.println("[TEST] === input a good value into xmlPath.");
		this.inputUI.inputValue(xmlpathTextbox, empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findXmlPathThenVerify(empty, true); // 指定値"", エラー表示なし

		// ファイルセットモードの選択
		HtmlRadioButtonInput radio = this.inputUI.getRadioByCountingMode(this.inputUI.getRadiosOfCountingMode(),
				CountingModeEnum.ONLY_STEP_USING_FILE_SET.getValue());
		try {
			radio.click();
			System.out.println("[TEST] === click radio:" + radio.toString());
			this.waitLongForEventCallbackProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.inputUI.verifyTrowStyleOfCountingModeAboutVisibility(true);

		// インクルード入力ボックスの検証
		HtmlTextInput includeTextbox = this.inputUI.findIncludePatternThenVerify("", true); // 初期値"", エラー表示なし
		// ""を指定
		System.out.println("[TEST] === input an empty value into includePattern.");
		this.inputUI.inputValue(includeTextbox, empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findIncludePatternThenVerify(empty, true); // 指定値"", エラー表示なし

		// 除外入力ボックスの検証
		HtmlTextInput excludeTextbox = this.inputUI.findExcludePatternThenVerify("", true); // 初期値"", エラー表示なし
		// ""を指定
		System.out.println("[TEST] === input an empty value into excludePattern.");
		this.inputUI.inputValue(excludeTextbox, empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findExcludePatternThenVerify(empty, true); // 指定値"", エラー表示なし

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 保存した値が設定されている
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findEncodingThenVerify(defaultEncoding, true); // 指定値, エラー表示なし
		// 高度な設定の開始divを取得
		HtmlDivision advancedDiv = this.inputUI.findAdvancedDivision();
		assertThat(advancedDiv.getAttribute("class"), equalTo("advancedLink"));
		System.out.println("[TEST] AdvancedDiv style(re-open):" + advancedDiv.getAttribute("style"));
		assertThat(advancedDiv.getAttribute("style"), not(containsString("display: none")));
		// 項目編集ありの非表示の検証
		HtmlSpan advancedSpan = this.inputUI.findAdvancedSpanIn(advancedDiv);
		System.out.println("[TEST] AdvancedSpan style(re-open):" + advancedSpan.getAttribute("style"));
		assertThat(advancedSpan.getAttribute("style"), containsString("display: none"));
		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		// 保存した値が設定されている
		this.inputUI.findXmlPathThenVerify(empty, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(true);
		this.inputUI.findIncludePatternThenVerify(empty, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(true);
		this.inputUI.findExcludePatternThenVerify(empty, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(true);
		this.inputUI.findOldSourceDirectoryThenVerify(empty, false); // 指定値, エラー表示あり
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(false);
	}

	@Test
	public void configureInputPropertiesUsingFileSetMode() {
		System.out.println("## InputSettingUI ## configureInputPropertiesUsingFileSetMode ##");

		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり
		// 任意のユニット名を指定
		System.out.println("[TEST] === input a good value into unitName.");
		String unitGoodValue = "java";
		this.inputUI.inputValue(unitTextbox, unitGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// 任意のパスを指定
		System.out.println("[TEST] === input a good value into sourceDirectory.");
		String srcdirGoodValue = "src/main/java";
		this.inputUI.inputValue(srcdirTextbox, srcdirGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし

		// エンコード入力ボックスの検証
		HtmlTextInput encodingTextbox =
				this.inputUI.findEncodingThenVerify(System.getProperty("file.encoding"), true);
		// 初期値file.encoding, エラー表示なし
		System.out.println("[TEST] === input a good value into encoding.");
		String encodingGoodValue = "Shift_JIS";
		this.inputUI.inputValue(encodingTextbox, encodingGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findEncodingThenVerify(encodingGoodValue, true); // 指定値, エラー表示なし

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();

		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.inputUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(true);
		// 存在するXMLファイルを指定
		System.out.println("[TEST] === input a good value into xmlPath.");
		String xmlpathGoodValue = CUSTOM_LANGUAGE_XML;
		this.inputUI.inputValue(xmlpathTextbox, xmlpathGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findXmlPathThenVerify(xmlpathGoodValue, true); // 指定値, エラー表示なし

		// ファイルセットモードの選択
		HtmlRadioButtonInput radio = this.inputUI.getRadioByCountingMode(this.inputUI.getRadiosOfCountingMode(),
				CountingModeEnum.ONLY_STEP_USING_FILE_SET.getValue());
		try {
			radio.click();
			System.out.println("[TEST] === click radio:" + radio.toString());
			this.waitLongForEventCallbackProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.inputUI.verifyTrowStyleOfCountingModeAboutVisibility(true);

		// インクルード入力ボックスの検証
		HtmlTextInput includeTextbox = this.inputUI.findIncludePatternThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(true);
		// 任意のパターンを指定
		System.out.println("[TEST] === input a good value into includePattern.");
		String includeGoodValue = "**/*.java";
		this.inputUI.inputValue(includeTextbox, includeGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findIncludePatternThenVerify(includeGoodValue, true); // 指定値, エラー表示なし

		// 除外入力ボックスの検証
		HtmlTextInput excludeTextbox = this.inputUI.findExcludePatternThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(true);
		// 任意のパターンを指定
		System.out.println("[TEST] === input a good value into excludePattern.");
		String excludeGoodValue = "**/test/**/*.java";
		this.inputUI.inputValue(excludeTextbox, excludeGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findExcludePatternThenVerify(excludeGoodValue, true); // 指定値, エラー表示なし

		// 旧ソースディレクトリ入力ボックスの検証
		// 初期値"", エラー表示あり
		this.inputUI.findOldSourceDirectoryThenVerify("", false);
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(false);

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();

		// 保存した値が設定されている
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findEncodingThenVerify(encodingGoodValue, true); // 指定値, エラー表示なし
		// 高度な設定の開始divを取得
		HtmlDivision advancedDiv = this.inputUI.findAdvancedDivision();
		assertThat(advancedDiv.getAttribute("class"), is(equalTo("advancedLink")));
		System.out.println("[TEST] AdvancedDiv style(re-open):" + advancedDiv.getAttribute("style"));
		assertThat(advancedDiv.getAttribute("style"), not(containsString("display: none")));
		// 項目編集ありの表示の検証
		HtmlSpan advancedSpan = this.inputUI.findAdvancedSpanIn(advancedDiv);
		System.out.println("[TEST] AdvancedSpan style(re-open):" + advancedSpan.getAttribute("style"));
		assertThat(advancedSpan.getAttribute("style"), not(containsString("display: none")));
		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		// 保存した値が設定されている
		this.inputUI.findXmlPathThenVerify(xmlpathGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(true);
		this.inputUI.findIncludePatternThenVerify(includeGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(true);
		this.inputUI.findExcludePatternThenVerify(excludeGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(true);
		this.inputUI.findOldSourceDirectoryThenVerify("", false); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(false);
	}

	@Test
	public void configureInputPropertiesUsingDiffTooMode() {
		System.out.println("## InputSettingUI ## configureInputPropertiesUsingDiffTooMode ##");

		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり
		// 任意のユニット名を指定
		System.out.println("[TEST] === input a good value into unitName.");
		String unitGoodValue = "java";
		this.inputUI.inputValue(unitTextbox, unitGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// 任意のパスを指定
		System.out.println("[TEST] === input a good value into sourceDirectory.");
		String srcdirGoodValue = "src/main/java";
		this.inputUI.inputValue(srcdirTextbox, srcdirGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし

		// エンコード入力ボックスの検証
		// 初期値file.encoding, エラー表示なし
		String defaultEncoding = System.getProperty("file.encoding");
		this.inputUI.findEncodingThenVerify(defaultEncoding, true);

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();

		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.inputUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(true);
		// 存在するXMLファイルを指定
		System.out.println("[TEST] === input a good value into xmlPath.");
		String xmlpathGoodValue = CUSTOM_LANGUAGE_XML;
		this.inputUI.inputValue(xmlpathTextbox, xmlpathGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findXmlPathThenVerify(xmlpathGoodValue, true); // 指定値, エラー表示なし

		// Diffモードの選択
		HtmlRadioButtonInput radio = this.inputUI.getRadioByCountingMode(this.inputUI.getRadiosOfCountingMode(),
				CountingModeEnum.BOTH_STEP_AND_DIFF.getValue());
		try {
			radio.click();
			System.out.println("[TEST] === click radio:" + radio.toString());
			this.waitLongForEventCallbackProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.inputUI.verifyTrowStyleOfCountingModeAboutVisibility(true);

		// インクルード入力ボックスの検証
		this.inputUI.findIncludePatternThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(false);

		// 除外入力ボックスの検証
		this.inputUI.findExcludePatternThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(false);

		// 旧ソースディレクトリ入力ボックスの検証
		// 初期値"", エラー表示あり
		HtmlTextInput olddirTextbox = this.inputUI.findOldSourceDirectoryThenVerify("", false);
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(true);
		// 任意のパスを指定
		System.out.println("[TEST] === input a good value into oldSourceDirectory.");
		String olddirGoodValue = "/opt/base/project/src/main/java";
		this.inputUI.inputValue(olddirTextbox, olddirGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findOldSourceDirectoryThenVerify(olddirGoodValue, true); // 指定値, エラー表示なし

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();

		// 保存した値が設定されている
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findEncodingThenVerify(defaultEncoding, true); // 指定値, エラー表示なし
		// 高度な設定の開始divを取得
		HtmlDivision advancedDiv = this.inputUI.findAdvancedDivision();
		assertThat(advancedDiv.getAttribute("class"), equalTo("advancedLink"));
		System.out.println("[TEST] AdvancedDiv style(re-open):" + advancedDiv.getAttribute("style"));
		assertThat(advancedDiv.getAttribute("style"), not(containsString("display: none")));
		// 項目編集ありの表示の検証
		HtmlSpan advancedSpan = this.inputUI.findAdvancedSpanIn(advancedDiv);
		System.out.println("[TEST] AdvancedSpan style(re-open):" + advancedSpan.getAttribute("style"));
		assertThat(advancedSpan.getAttribute("style"), not(containsString("display: none")));
		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		// 保存した値が設定されている
		this.inputUI.findXmlPathThenVerify(xmlpathGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(true);
		this.inputUI.findIncludePatternThenVerify("", true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(false);
		this.inputUI.findExcludePatternThenVerify("", true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(false);
		this.inputUI.findOldSourceDirectoryThenVerify(olddirGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(true);
	}

}
