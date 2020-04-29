package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.jenkinsci.plugins.keisuke.uihelper.InputSettingConfigUI.FIRST_COUNTING_MODE;

import java.io.IOException;

import org.jenkinsci.plugins.keisuke.uihelper.InputSettingConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * testing InputSetting in project config page.
 */
public class InputSettingUITest extends AbstractSettingUITest {

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
	public void showInitialInputProperties() {
		System.out.println("## InputSettingUI ## showInitialInputProperties ##");
		// inputSettingの存在確認
		HtmlTableRow inputSettingTr = this.inputUI.findTrowOfInputSetting();
		assertThat(inputSettingTr, not(nullValue()));
		System.out.println("[TEST] inputSetting:" + inputSettingTr.toString());

		// ユニット入力ボックスの検証
		this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり
		// ソースディレクトリ入力ボックスの検証
		this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// エンコード入力ボックスの検証
			// 初期値file.encoding, エラー表示なし
		this.inputUI.findEncodingThenVerify(System.getProperty("file.encoding"), true);

		// 高度な設定の検証
		// 高度な設定の開始divを取得
		HtmlDivision advancedDiv = this.inputUI.findAdvancedDivision();
		assertThat(advancedDiv.getAttribute("class"), equalTo("advancedLink"));
		System.out.println("[TEST] AdvancedDiv style(init):" + advancedDiv.getAttribute("style"));
		assertThat(advancedDiv.getAttribute("style"), not(containsString("display: none")));
		// 項目編集ありの非表示の検証
		HtmlSpan advancedSpan = this.inputUI.findAdvancedSpanIn(advancedDiv);
		System.out.println("[TEST] AdvancedSpan style(init):" + advancedSpan.getAttribute("style"));
		assertThat(advancedSpan.getAttribute("style"), containsString("display: none"));
		// 高度な設定のTableの確認
		HtmlTable advancedTable = this.inputUI.findAdvancedTableIn(advancedDiv);
		assertThat(advancedTable.getAttribute("class"), equalTo("advancedBody"));
		// 高度な設定に隠れた項目の非表示検証
		// XMLファイルパス入力ボックスの非表示検証
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(false);
		// ラジオボタンの非表示検証
		this.inputUI.verifyTrowStyleOfCountingModeAboutVisibility(false);
		// インクルード入力ボックスの非表示検証
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(false);
		// 除外入力ボックスの非表示検証
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(false);
		// 旧ソースディレクトリ入力ボックスの非表示検証
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(false);

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		// 表示を検証
		System.out.println("[TEST] AdvancedDiv style(after):" + advancedDiv.getAttribute("style"));
		assertThat(advancedDiv.getAttribute("style"), containsString("display: none"));
		//System.out.println("[TEST] AdvancedSpan style(after):" + advancedSpan.getAttribute("style"));
		// notにならなかった。assertThat(advancedSpan.getAttribute("style"), not(containsString("display: none")));

		// XMLファイルパス入力ボックスの検証
		this.inputUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfXmlPathAboutVisibility(true);
		// ラジオボタンの表示検証
		String expectedMode = FIRST_COUNTING_MODE.getValue();
		this.inputUI.findSelectedCountingModeThenVerify(expectedMode); // 初期値”step_simply”
		this.inputUI.verifyTrowStyleOfCountingModeAboutVisibility(true);
		// インクルード入力ボックスの検証
		this.inputUI.findIncludePatternThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(false);
		// 除外入力ボックスの検証
		this.inputUI.findExcludePatternThenVerify("", true); // 初期値"", エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(false);
		// 旧ソースディレクトリ入力ボックスの検証
		this.inputUI.findOldSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(false);
	}


	@Test
	public void configureInputPropertiesWithDefault() {
		System.out.println("## InputSettingUI ## configureInputPropertiesWithDefault ##");

		String empty = "";
		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり
		// ""を指定
		System.out.println("[TEST] === input an empty value into unitName.");
		unitTextbox.setText(empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(empty, false); // 指定値, エラー表示あり
		// 任意のカテゴリ名を指定
		System.out.println("[TEST] === input a good value into unitName.");
		String unitGoodValue = "default";
		unitTextbox.setText(unitGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// ””を指定
		System.out.println("[TEST] === input an empty value into sourceDirectory.");
		srcdirTextbox.setText(empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(empty, false); // 指定値, エラー表示あり
		// 任意のパスを指定
		System.out.println("[TEST] === input a good value into sourceDirectory.");
		String srcdirGoodValue = "src/main/java";
		srcdirTextbox.setText(srcdirGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし

		// エンコード入力ボックスの検証
		String defaultEncoding = System.getProperty("file.encoding");
		HtmlTextInput encodingTextbox = this.inputUI.findEncodingThenVerify(defaultEncoding, true);
		// 初期値file.encoding, エラー表示なし
		// ""を指定
		System.out.println("[TEST] === input an empty value into encoding.");
		encodingTextbox.setText(empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findEncodingThenVerify(empty, false); // 指定値, エラー表示あり
		// 存在するエンコード名を指定
		System.out.println("[TEST] === input a good value into encoding.");
		encodingTextbox.setText(defaultEncoding);
		this.waitForEventCallbackProcess();
		this.inputUI.findEncodingThenVerify(defaultEncoding, true); // 指定値, エラー表示なし

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
		this.inputUI.verifyTrowStyleOfIncludePatternAboutVisibility(false);
		this.inputUI.findExcludePatternThenVerify(empty, true); // 指定値, エラー表示なし
		this.inputUI.verifyTrowStyleOfExcludePatternAboutVisibility(false);
		this.inputUI.findOldSourceDirectoryThenVerify(empty, false); // 指定値, エラー表示あり
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(false);
	}


	@Test
	public void checkInvalidValues() {
		System.out.println("## InputSettingUI ## checkInvalidValues ##");

		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり

		// 誤ったユニット名を指定
		System.out.println("[TEST] === input a bad value into unitName.");
		String unitBadValue = "===Bad-name===";
		unitTextbox.setText(unitBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitBadValue, false); // 指定値, エラー表示あり

		// ユニット名に空文字列を指定
		System.out.println("[TEST] === input an empty string into unitName.");
		unitBadValue = "";
		unitTextbox.setText(unitBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitBadValue, false); // 指定値, エラー表示あり

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// 空文字以外を指定
		System.out.println("[TEST] === input some value into source directory.");
		String srcdirBadValue = "dummy";
		srcdirTextbox.setText(srcdirBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirBadValue, true); // 指定値, エラー表示なし
		// 空文字を指定
		System.out.println("[TEST] === input empty into source directory.");
		srcdirBadValue = "";
		srcdirTextbox.setText(srcdirBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirBadValue, false); // 指定値, エラー表示あり

		// エンコード入力ボックスの検証
		HtmlTextInput encodingTextbox =
				this.inputUI.findEncodingThenVerify(System.getProperty("file.encoding"), true);
		// 初期値file.encoding, エラー表示なし

		// 存在しないエンコード名を指定
		System.out.println("[TEST] === input a bad value into encoding.");
		String encodingBadValue = "WRONG-ENCODING";
		encodingTextbox.setText(encodingBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findEncodingThenVerify(encodingBadValue, false); // 指定値 エラー表示あり

		// エンコード名に空文字列を指定
		System.out.println("[TEST] === input empty into encoding.");
		encodingBadValue = "";
		encodingTextbox.setText(encodingBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findEncodingThenVerify(encodingBadValue, false); // 指定値 エラー表示あり

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();

		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.inputUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		// 存在しないファイルパスを指定
		System.out.println("[TEST] === input non-exist file path into xml file path.");
		String xmlpathBadValue = "test/data/non-exist.xml";
		xmlpathTextbox.setText(xmlpathBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findXmlPathThenVerify(xmlpathBadValue, false); // 指定値 エラー表示あり

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
		// 旧ソースディレクトリ入力ボックスの検証
		// 初期値"", エラー表示あり
		HtmlTextInput olddirTextbox = this.inputUI.findOldSourceDirectoryThenVerify("", false);
		this.inputUI.verifyTrowStyleOfOldSourceDirectoryAboutVisibility(true);
		// 空文字以外を指定
		System.out.println("[TEST] === input empty into old source directory.");
		String olddirBadValue = "dummy";
		olddirTextbox.setText(olddirBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findOldSourceDirectoryThenVerify(olddirBadValue, true); // 指定値, エラー表示なし
		// 空文字を指定
		System.out.println("[TEST] === input empty into old source directory.");
		olddirBadValue = "";
		olddirTextbox.setText(olddirBadValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findOldSourceDirectoryThenVerify(olddirBadValue, false); // 指定値, エラー表示あり
	}

	@Test
	public void showHelpAboutInputProperties() {
		System.out.println("## InputSettingUI ## showHelpAboutInputProperties ##");

		String helpText;
		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり
		// ユニット名のヘルプ
		helpText =  this.configHtmlUI().getHelpContentOf("unitName", unitTextbox);
		assertThat(helpText, containsString("[a-z][A-Z][0-9][-_]"));

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり
		// ソースディレクトリのヘルプ
		helpText =  this.configHtmlUI().getHelpContentOf("sourceDirectory", srcdirTextbox);
		assertThat(helpText, containsString("(e.g. 'subproject/src/main/java')"));

		// エンコード入力ボックスの検証
		HtmlTextInput encodingTextbox =
				this.inputUI.findEncodingThenVerify(System.getProperty("file.encoding"), true);
		// 初期値file.encoding, エラー表示なし
		// エンコードのヘルプ
		helpText =  this.configHtmlUI().getHelpContentOf("encoding", encodingTextbox);
		assertThat(helpText, containsString("(e.g. 'UTF-8')"));

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		// XMLファイルパス入力ボックスの検証
		HtmlTextInput xmlpathTextbox = this.inputUI.findXmlPathThenVerify("", true); // 初期値"", エラー表示なし
		// XMLファイルパスのヘルプ
		helpText = this.configHtmlUI().getHelpContentOf("xmlPath", xmlpathTextbox);
		assertThat(helpText, containsString("(e.g. '/home/develop/conf/language.xml')"));

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
		// インクルード入力ボックスの検証
		HtmlTextInput includeTextbox = this.inputUI.findIncludePatternThenVerify("", true); // 初期値"", エラー表示なし
		// インクルードパターンのヘルプ
		helpText =  this.configHtmlUI().getHelpContentOf("includePattern", includeTextbox);
		assertThat(helpText, containsString("(e.g. 'src/**/*.java')"));

		// 除外パターン入力ボックスの検証
		HtmlTextInput excludeTextbox = this.inputUI.findExcludePatternThenVerify("", true); // 初期値"", エラー表示なし
		// 除外パターンのヘルプ
		helpText =  this.configHtmlUI().getHelpContentOf("excludePattern", excludeTextbox);
		assertThat(helpText, containsString("(e.g. 'src/test/**/*.java')"));

		// Diffモードの選択
		radio = this.inputUI.getRadioByCountingMode(this.inputUI.getRadiosOfCountingMode(),
				CountingModeEnum.BOTH_STEP_AND_DIFF.getValue());
		try {
			radio.click();
			System.out.println("[TEST] === click radio:" + radio.toString());
			this.waitLongForEventCallbackProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 旧ソースディレクトリ入力ボックスの検証
		// 初期値"", エラー表示あり
		HtmlTextInput olddirTextbox = this.inputUI.findOldSourceDirectoryThenVerify("", false);
		// 旧ソースディレクトリのヘルプ
		helpText =  this.configHtmlUI().getHelpContentOf("oldSourceDirectory", olddirTextbox);
		assertThat(helpText, containsString("(e.g. '/var/proj/old/src/main/java')"));
	}
}
