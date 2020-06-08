package org.jenkinsci.plugins.keisuke;

import static org.jenkinsci.plugins.keisuke.util.PathUtil.getExistingFilePath;

import org.jenkinsci.plugins.keisuke.uihelper.InputSettingConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * testing InputSetting in project config page with global setting.
 */
public class InputSettingUIWithGlobalTest extends AbstractSettingUITest {

	private InputSettingConfigUI inputUI = null;
	private final String defaultEncoding = "EUC-JP";
	private final String defaultXmlPath = getExistingFilePath();

	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		// グローバル設定ありのプロジェクトを作成
		GlobalSetting setting = new GlobalSetting(defaultEncoding, defaultXmlPath);
		this.prepareKeisukeConfig(setting);
		this.inputUI = new InputSettingConfigUI(this);
		// wait for rendering config page about KeisukePublisher.
		this.waitLongForEventCallbackProcess();
	}

	@Test
	public void configureToOverwriteGlobalSettingValue() {
		System.out.println("## InputSettingUI ## configureToOverwriteGlobalSettingValue ##");
		// ユニット入力ボックスの検証
		HtmlTextInput unitTextbox = this.inputUI.findUnitNameThenVerify("", false); // 初期値"", エラー表示あり

		// ソースディレクトリ入力ボックスの検証
		HtmlTextInput srcdirTextbox = this.inputUI.findSourceDirectoryThenVerify("", false); // 初期値"", エラー表示あり

		// エンコード入力ボックスの検証
		HtmlTextInput encodingTextbox = this.inputUI.findEncodingThenVerify(defaultEncoding, true);
		// Global設定値, エラー表示なし

		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();

		// XMLファイルパス入力ボックスの検証
		// Global設定値, エラー表示なし
		HtmlTextInput xmlpathTextbox = this.inputUI.findXmlPathThenVerify(defaultXmlPath, true);

		// 任意のユニット名を指定
		System.out.println("[TEST] === input a good value into unitName.");
		String unitGoodValue = "default";
		this.inputUI.inputValue(unitTextbox, unitGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし

		// 任意のパスを指定
		System.out.println("[TEST] === input a good value into sourceDirectory.");
		String srcdirGoodValue = "src/main/java";
		this.inputUI.inputValue(srcdirTextbox, srcdirGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし

		// 存在するエンコード名を指定
		String encodingGoodValue = "Shift_JIS";
		System.out.println("[TEST] === input a good value into encoding.");
		this.inputUI.inputValue(encodingTextbox, encodingGoodValue);
		this.waitForEventCallbackProcess();
		this.inputUI.findEncodingThenVerify(encodingGoodValue, true); // 指定値, エラー表示なし

		// ""を指定
		String empty = "";
		System.out.println("[TEST] === input an empty string into xmlPath.");
		this.inputUI.inputValue(xmlpathTextbox, empty);
		this.waitForEventCallbackProcess();
		this.inputUI.findXmlPathThenVerify("", true); // 指定値, エラー表示なし

		// 設定を保存
		System.out.println("[TEST] === submit the form.");
		this.submitConfigAndReopen();
		// 保存した値が設定されている
		this.inputUI.findUnitNameThenVerify(unitGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findSourceDirectoryThenVerify(srcdirGoodValue, true); // 指定値, エラー表示なし
		this.inputUI.findEncodingThenVerify(encodingGoodValue, true); // 指定値, エラー表示なし
		// 高度な設定ボタンを押して展開
		this.inputUI.expandAdvancedProperties();
		this.inputUI.findXmlPathThenVerify(empty, true); // 指定値, エラー表示なし
	}

}
