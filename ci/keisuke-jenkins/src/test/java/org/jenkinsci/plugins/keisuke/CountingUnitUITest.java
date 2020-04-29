package org.jenkinsci.plugins.keisuke;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.jenkinsci.plugins.keisuke.uihelper.CountingUnitConfigUI;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * testing CountingUnit in project config page.
 */
public class CountingUnitUITest extends AbstractSettingUITest {

	private CountingUnitConfigUI unitUI = null;

	@Before @Override
	public void setUp() throws Exception {
		super.setUp();
		this.prepareKeisukeConfig();
		this.unitUI = new CountingUnitConfigUI(this);
		// wait for rendering config page about KeisukePublisher.
		this.waitLongForEventCallbackProcess();
	}

	@Test
	public void showInitialProperties() {
		System.out.println("## CountingUnitUI ## showInitialProperties ##");
		HtmlDivision countingUnitsDiv = this.unitUI.findCountingUnitsDivision();
		// タイトル表示の検証
		String title = this.unitUI.getTitleOfCountingUnitsAtDiv(countingUnitsDiv);
		System.out.println("[TEST] title td content:" + title);
		assertThat(title, not(isEmptyOrNullString()));
		// coutingUnitsは１つ、またはなぜか時々２つ
		List<DomElement> divList = this.unitUI.getListOfDivForCountingUnits();
		int unitsNum = divList.size();
		System.out.println("[TEST] countingUnits[0]:" + divList.get(0).toString());
		System.out.println("[TEST] countingUnits division # :" + Integer.toString(unitsNum));
		assertThat(divList, anyOf(hasSize(1), hasSize(2))); //sometimes 2

		// inputSettingが１つ
		List<DomElement> trList = this.unitUI.getListOfTrowForInputSetting();
		System.out.println("[TEST] inputSetting[0]:" + trList.get(0).toString());
		assertThat(trList, hasSize(unitsNum));
		// _.outputSettingが１つ
		List<DomElement> checkboxList = this.unitUI.getListOfCheckboxForOutputSetting();
		System.out.println("[TEST] _.outputSetting[0]:" + checkboxList.get(0).toString());
		assertThat(checkboxList, hasSize(unitsNum));
	}

	@Test
	public void addSecondUnits() {
		System.out.println("## CountingUnitUI ## addSecondUnits ##");
		int unitsNum = this.unitUI.getListOfDivForCountingUnits().size();
		// 表示されている1つ目のCountingUnitのユニットに入力
		// ユニット入力ボックスの検証
		List<HtmlInput> unitList = this.unitUI.getListOfTextboxForUnitName();
		int initNum = 0;
		for (HtmlInput input : unitList) {
			System.out.println("[TEST] Textbox[unitName][" + Integer.toString(initNum) + "] Xpath="
					+ input.getCanonicalXPath());
			System.out.println("[TEST] Textbox[unitName][" + Integer.toString(initNum) + "] value="
					+ input.getAttribute("value"));
			initNum++;
		}
		assertThat(unitList, hasSize(unitsNum)); //sometimes 2
		HtmlTextInput unitTextbox = (HtmlTextInput) unitList.get(0);
		// 任意のユニット名を指定
		System.out.println("[TEST] === input a value into 1st unitName.");
		String unitGoodValue = "1st_unitName";
		unitTextbox.setText(unitGoodValue);
		this.waitForEventCallbackProcess();
		System.out.println("[TEST] Textbox[unitName][0] value(after):" + unitTextbox.getAttribute("value"));
		String actual = unitTextbox.getAttribute("value");
		// 入力値のまま
		assertThat(actual, equalTo(unitGoodValue));

		// 追加ボタンの押下
		this.unitUI.addNewCountingUnit();

		// 2つ目のCountingUnitの入力欄が表示されたはず
		// ユニット入力ボックスの検証
		unitList = this.unitUI.getListOfTextboxForUnitName();
		//List<HtmlTextInput> unitTextboxList = new ArrayList<HtmlTextInput>();
		int addedNum = 0;
		for (HtmlInput input : unitList) {
			System.out.println("[TEST] Textbox[unitName][" + Integer.toString(addedNum) + "] Xpath="
					+ input.getCanonicalXPath());
			System.out.println("[TEST] Textbox[unitName][" + Integer.toString(addedNum) + "] value="
					+ input.getAttribute("value"));
			//unitTextboxList.add((HtmlTextInput) input);
			actual = input.getAttribute("value");
			if (addedNum == 0) {
				assertThat(actual, equalTo(unitGoodValue));
			} else {
				assertThat(actual, isEmptyString());
			}
			addedNum++;
		}
		//assertThat(unitList, hasSize(2));
		assertThat(addedNum, greaterThan(initNum));
	}
}
