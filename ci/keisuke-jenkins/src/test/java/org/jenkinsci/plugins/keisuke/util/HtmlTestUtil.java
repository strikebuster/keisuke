package org.jenkinsci.plugins.keisuke.util;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 * Test utility for web UI of Jenkins plugin
 */
public final class HtmlTestUtil {
	public static final long WAITTIME = 200L;
	public static final long WAITLONGTIME = WAITTIME * 5;

	private HtmlTestUtil() { }

	// Global(system) configure page

	/**
	 * システム設定のKeisukePublisherの先頭Trからタイトル表示領域のXpathを返す
	 * @param top KeisukePublisherの先頭Tr
	 * @return タイトル表示領域のXpath
	 */
	public static String getXPathOfGlobalHeaderDivFromTop(final HtmlElement top) {
		if (top == null) {
			return null;
		}
		String[] topArray = splitIntoArray(top.getCanonicalXPath());
		String[] array = new String[topArray.length + 2];
		for (int i = 0; i < topArray.length; i++) {
			array[i] = topArray[i];
		}
		int leafIdx = array.length - 1;
		int tdIdx = leafIdx - 1;
		int trIdx = leafIdx - 2;
		if (trIdx < 0) {
			throw new RuntimeException("fail to scan section top:" + top.getCanonicalXPath());
		}
		array[trIdx] = getBrotherNode(array[trIdx], "+1");
		array[tdIdx] = "td[1]";
		array[leafIdx] = "div";
		return makeXPathFrom(array);
		// *top*/../tr[m+1]/td[1]/div
	}

	// Project configure page

	/**
	 * KeisukePublisher設定全体のdivisionの中にあるタイトル表示領域のXpathを返す
	 * @param top KeisukePublisher設定全体のdivision
	 * &lt;div descriptorid="org.jenkinsci.plugins.keisuke.KeisukePublisher" /&gt;
	 * @return タイトル表示領域のXpath
	 */
	public static String getXPathOfKeisukeTitleFromTop(final HtmlDivision top) {
		if (top == null) {
			return  null;
		}
		return top.getCanonicalXPath() + "/table/tbody/tr[1]/td[1]/div";
		// top=/html/body/div[6]/div/div/div/div/form/table/tbody/tr[99]/td/div/div[1]
	}

	/**
	 * KeisukePublisher設定全体のヘルプアンカーに対するヘルプ表示領域Xpathを返す
	 * @param anchor KeisukePublisher設定全体のヘルプアンカー &lt;a/&gt;
	 * @return ヘルプ表示領域のXpath
	 */
	public static String getXPathOfKeisukeHelpTdataFromAnchor(final HtmlAnchor anchor) {
		return getXPathOfHelpTdataFromNonErrorElement(anchor);
	}

	/**
	 * KeisukePublisher設定の中の繰り返し設定である計測ユニット設定のAddボタンのXpathを返す
	 * @param repeatedChunk 繰り返し設定であるcountingUnitsのdivision
	 * &lt;div name="countingUnits" /&gt;
	 * @return AddボタンのXpathを返す
	 */
	public static String getXPathOfAddButtonFromRepeatedDiv(final HtmlDivision repeatedChunk) {
		if (repeatedChunk == null) {
			return  null;
		}
		String[] array = splitIntoArray(repeatedChunk.getCanonicalXPath());
		int leafIdx = array.length - 1;
		array[leafIdx] = null;
		return makeXPathFrom(array) + "/input";
	}

	public static String getXPathOfYuiAddButtonFromRepeatedDiv(final HtmlDivision repeatedChunk) {
		if (repeatedChunk == null) {
			return  null;
		}
		String[] array = splitIntoArray(repeatedChunk.getCanonicalXPath());
		int leafIdx = array.length - 1;
		array[leafIdx] = null;
		return makeXPathFrom(array) + "/span/span/button";
	}

	/**
	 * 設定項目から設定領域の先頭TrのXpathを返す
	 * <br>e.g.　&lt;input type="checkbox" name="_.outputSetting"/&gt;
	 * <br>&lt;input type="text" name="_.outputDirectory"/&gt;
	 * @param element 出力項目
	 * @return TrのXpath
	 */
	public static String getXPathOfPropertyTrowFromElement(final HtmlElement element) {
		if (element == null) {
			return  null;
		} else if (element instanceof HtmlRadioButtonInput) {
			return getXPathOfPropertyTrowFromRadio((HtmlRadioButtonInput) element);
		}
		String[] array = splitIntoArray(element.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		array[leafIdx] = null; // input:checkbox/text, select
		array[tdIdx] = null; // td
		return makeXPathFrom(array);
	}

	private static String getXPathOfPropertyTrowFromRadio(final HtmlRadioButtonInput radio) {
		String[] array = splitIntoArray(radio.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int labelIdx = idx--;
		int tdIdx = idx--;
		array[leafIdx] = null; // input:radio
		array[labelIdx] = null; // label
		array[tdIdx] = null; // td
		return makeXPathFrom(array);
	}

	/**
	 * 画面表示設定の先頭Trからタイトル表示TdのXpathを返す
	 * @param headTr 画面表示設定の先頭Tr &lt;Tr name="displaySettings"/&gt;
	 * @return TdのXpathを返す
	 */
	public static String getXPathOfTitleTdataForDisplaySettingFromTrow(final HtmlElement headTr) {
		if (headTr == null) {
			return  null;
		}
		String[] array = splitIntoArray(headTr.getCanonicalXPath());
		int trIdx = array.length - 1;
		array[trIdx] = getBrotherNode(array[trIdx], "-1"); // tr name="displaySettings"
		return makeXPathFrom(array) + "/td[2]"; // td class="setting-name" == title
	}

	/**
	 * 画面表示設定の先頭Trから行数遷移グラフ表示行数設定のタイトル表示TdのXpathを返す
	 * @param headTr 画面表示設定の先頭Tr &lt;Tr name="displaySettings"/&gt;
	 * @return TdのXpathを返す
	 */
	public static String getXPathOfTitleTdataForDisplayStepKindFromTrow(final HtmlElement headTr) {
		if (headTr == null) {
			return  null;
		}
		String[] array = splitIntoArray(headTr.getCanonicalXPath());
		int trIdx = array.length - 1;
		array[trIdx] = getBrotherNode(array[trIdx], "+1"); // tr name="displaySettings"
		return makeXPathFrom(array) + "/td[2]"; // td class="setting-name" == title
	}

	/**
	 * 行数遷移グラフ表示行数設定のタイトルTdから設定ラジオボタンの１つ目のXpathを返す
	 * @param titleTd 行数遷移グラフ表示行数設定のタイトルTd.
	 * @return inputのXpathを返す
	 */
	public static String getXPathOfDisplayStepKindFirstRadioFromTitleTdata(final HtmlTableDataCell titleTd) {
		if (titleTd == null) {
			return  null;
		}
		String[] array = splitIntoArray(titleTd.getCanonicalXPath());
		int tdIdx = array.length - 1;
		array[tdIdx] = getBrotherNode(array[tdIdx], "+1"); // td content == title
		return makeXPathFrom(array) + "/input";
	}

	/**
	 * 対象ファイル設定領域deivisionからタイトル表示TdのXpathを返す
	 * @param headDiv 対象ファイル設定領域 &lt;div name="countingUnits"/&gt;
	 * @return TdのXpathを返す
	 */
	public static String getXPathOfCountingUnitTitleFromDiv(final HtmlElement headDiv) {
		if (headDiv == null) {
			return  null;
		}
		String[] array = splitIntoArray(headDiv.getCanonicalXPath());
		int idx = array.length - 1;
		array[idx--] = null; // div name="countingUnits"
		array[idx--] = null; // div class="repeated-container"
		array[idx--] = "td[2]"; // td class="setting-name" == title
		return makeXPathFrom(array);
	}

	public static String getXPathOfLabelFromCheckbox(final HtmlInput input) {
		if (input == null) {
			return  null;
		}
		String[] array = splitIntoArray(input.getCanonicalXPath());
		int leafIdx = array.length - 1;
		array[leafIdx] = "label";
		return makeXPathFrom(array);
		// *input*/../label
	}

	public static String getXPathOfHelpAnchorFromCheckbox(final HtmlInput input) {
		if (input == null) {
			return  null;
		}
		String[] array = splitIntoArray(input.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		array[tdIdx] = getBrotherNode(array[tdIdx], "+1");
		array[leafIdx] = "a";
		return makeXPathFrom(array);
		// *input*/../../td[2]/a
	}

	public static String getXPathOfHelpAnchorFromSelect(final HtmlSelect select) {
		return getXPathOfHelpAnchorFrom(select);
	}

	public static String getXPathOfHelpAnchorFromInput(final HtmlInput input) {
		return getXPathOfHelpAnchorFrom(input);
	}

	private static String getXPathOfHelpAnchorFrom(final HtmlElement element) {
		if (element == null) {
			return  null;
		}
		if (!(element instanceof HtmlInput || element instanceof HtmlSelect)) {
			return  null;
		}
		String[] array = splitIntoArray(element.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		array[tdIdx] = getBrotherNode(array[tdIdx], "+1");
		array[leafIdx] = "a";
		return makeXPathFrom(array);
		// *input*/../../td[n+1]/a
	}

	/**
	 * checkboxの項目のヘルプ表示領域のXpathを返す
	 * checkboxについてはエラー表示領域がないため他の入力項目とヘルプアンカーとヘルプ表示領域の位置関係が異なるため別メソッドになる
	 * @param input &lt;input type="checkbox"/gt;
	 * @return ヘルプ表示領域のXpath
	 */
	public static String getXPathOfHelpTdataFromCheckbox(final HtmlInput input) {
		return getXPathOfHelpTdataFromNonErrorElement(input);
	}

	public static String getXPathOfHelpTdataFromNonErrorElement(final HtmlElement element) {
		if (element == null) {
			return  null;
		}
		String[] array = splitIntoArray(element.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		int trIdx = idx--;
		if (trIdx < 0) {
			throw new RuntimeException("fail to scan element:" + element.getCanonicalXPath());
		}
		array[trIdx] = getBrotherNode(array[trIdx], "+1");
		array[tdIdx] = "td[2]";
		array[leafIdx] = null;
		return makeXPathFrom(array);
		// *element* /../../../tr[m+1]/td[2]
	}

	public static String getXPathOfHelpTdataFromErrorableElement(final HtmlElement element) {
		if (element == null) {
			return null;
		}
		String[] array = splitIntoArray(element.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		int trIdx = idx--;
		if (trIdx < 0) {
			throw new RuntimeException("fail to scan element:" + element.getCanonicalXPath());
		}
		array[trIdx] = getBrotherNode(array[trIdx], "+2");
		array[tdIdx] = "td[2]";
		array[leafIdx] = null;
		return makeXPathFrom(array);
		// *anchor* /../../../tr[m+2]/td[2]
	}

	public static String getXPathOfHelpDivFromTdata(final HtmlTableDataCell tdata) {
		if (tdata == null) {
			return null;
		}
		return tdata.getCanonicalXPath() + "/div";
		// *tdata* /div
	}

	public static String getXPathOfErrorTdataFromSelect(final HtmlSelect select) {
		return getXPathOfErrorTdataFrom(select);
	}

	public static String getXPathOfErrorTdataFromInput(final HtmlInput input) {
		return getXPathOfErrorTdataFrom(input);
	}

	private static String getXPathOfErrorTdataFrom(final HtmlElement element) {
		if (element == null) {
			return null;
		}
		if (!(element instanceof HtmlInput || element instanceof HtmlSelect)) {
			return  null;
		}
		String[] array = splitIntoArray(element.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		int trIdx = idx--;
		if (trIdx < 0) {
			throw new RuntimeException("fail to scan input:" + element.getCanonicalXPath());
		}
		array[trIdx] = getBrotherNode(array[trIdx], "+1");
		array[tdIdx] = "td[2]";
		array[leafIdx] = null;
		return makeXPathFrom(array);
		// *element* /../../../tr[m+1]/td[2]
	}

	public static String getXPathOfErrorDivFromTdata(final HtmlTableDataCell tdata) {
		if (tdata == null) {
			return null;
		}
		return tdata.getCanonicalXPath() + "/div";
		// *tdata* /div
	}

	public static String getXpathOfAdvancedDivFromFirstInput(final HtmlElement element) {
		if (element == null) {
			return null;
		}
		String[] array = splitIntoArray(element.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--; // <input />
		int tdIdx = idx--; // <td>
		int trIdx = idx--; // <tr>
		int tbodyIdx = idx--; // <tbody>
		int tableIdx = idx--; // <table>

		if (tableIdx < 0) {
			throw new RuntimeException("fail to scan 1st element of Advanced:"
					+ element.getCanonicalXPath());
		}
		array[tableIdx] = "div"; // <div class="advancedLink">
		array[tbodyIdx] = null;
		array[trIdx] = null;
		array[tdIdx] = null;
		array[leafIdx] = null;
		return makeXPathFrom(array);
		// *element*/../../../../div
	}

	public static String getXpathOfAdvancedSpanFromTop(final HtmlDivision div) {
		if (div == null) {
			return null;
		}
		return div.getCanonicalXPath() + "/span[1]";
	}

	public static String getXpathOfAdvancedButtonFromTop(final HtmlDivision div) {
		if (div == null) {
			return null;
		}
		return div.getCanonicalXPath() + "/span[2]/span/button";
	}

	public static String getXpathOfAdvancedTableFromTop(final HtmlDivision div) {
		if (div == null) {
			return null;
		}
		String[] array = splitIntoArray(div.getCanonicalXPath());
		int leafIdx = array.length - 1; // <div>
		array[leafIdx] = "table";
		return makeXPathFrom(array);
	}

	// project main page

	public static String getXPathOfCaptionDivisionFromImageOfStepCount(final HtmlImage img) {
		if (img == null) {
			return null;
		}
		String[] array = splitIntoArray(img.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int divIdx = idx--;
		array[divIdx] = getBrotherNode(array[divIdx], "-1");
		array[leafIdx] = null;
		return makeXPathFrom(array);
	}

	public static String getXPathOfCaptionDivisionFromImageOfDiffCountAdded(final HtmlImage img) {
		if (img == null) {
			return null;
		}
		String[] array = splitIntoArray(img.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int divIdx = idx--;
		array[divIdx] = getBrotherNode(array[divIdx], "-2");
		array[leafIdx] = null;
		return makeXPathFrom(array);
	}

	public static String getXPathOfCaptionDivisionFromImageOfDiffCountDeleted(final HtmlImage img) {
		if (img == null) {
			return null;
		}
		String[] array = splitIntoArray(img.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int divIdx = idx--;
		array[divIdx] = getBrotherNode(array[divIdx], "-3");
		array[leafIdx] = null;
		return makeXPathFrom(array);
	}

	public static String getXPathOfStepTrFromAnchor(final HtmlAnchor anchor, final int index) {
		if (anchor == null) {
			return null;
		}
		String[] array = splitIntoArray(anchor.getCanonicalXPath());
		int idx = array.length - 1;
		int leafIdx = idx--;
		int tdIdx = idx--;
		int trIdx = idx--;
		String idxOffset = "+" + Integer.toString(index);
		array[trIdx] = getBrotherNode(array[trIdx], idxOffset);
		array[tdIdx] = null; // td - null
		array[leafIdx] = null; // a -> null
		return makeXPathFrom(array);
	}

	public static String getXPathOfValueTrFromLabelTr(final HtmlTableRow row) {
		return getXPathOfValueTrFromLabelTr(row, 1);
		/*
		if (row == null) {
			return null;
		}
		String[] array = splitIntoArray(row.getCanonicalXPath());
		int leafIdx = array.length - 1;
		int tbodyIdx = leafIdx - 1;
		array[tbodyIdx] = getBrotherNode(array[tbodyIdx], "+1");
		array[leafIdx] = "tr";
		return makeXPathFrom(array);
		*/
	}

	public static String getXPathOfValueTrFromLabelTr(final HtmlTableRow row, final int index) {
		if (row == null) {
			return null;
		}
		String[] array = splitIntoArray(row.getCanonicalXPath());
		int leafIdx = array.length - 1;
		int tbodyIdx = leafIdx - 1;
		array[tbodyIdx] = getBrotherNode(array[tbodyIdx], "+1");
		StringBuilder sb = new StringBuilder();
		sb.append("tr[").append(index).append("]");
		array[leafIdx] = sb.toString();
		return makeXPathFrom(array);
	}

	// common functions

	/**
	 * Converts xpath about HTML DOM to array of String
	 * @param xpath String of xpath about HTML DOM
	 * @return String array
	 */
	private static String[] splitIntoArray(final String xpath) {
		if (xpath == null) {
			return null;
		}
		if (xpath.isEmpty()) {
			return new String[0];
		}
		String[] array = xpath.split("/");
		return array;
	}

	private static String makeXPathFrom(final String[] array) {
		StringBuilder sb = new StringBuilder();
		for (String node : array) {
			if (node != null && !node.isEmpty()) {
				sb.append('/').append(node);
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private static String getBrotherNode(final String node, final int position) {
		if (position < 0) {
			throw new IllegalArgumentException("position is less than zero.");
		}
		if (node == null) {
			throw new IllegalArgumentException("node is null");
		}
		if (node.isEmpty()) {
			throw new IllegalArgumentException("node is empty");
		}
		StringBuilder sb = new StringBuilder();
		int posLeft = node.indexOf('[');
		int posRight = node.indexOf(']');
		if (posLeft < 0 || posRight < 0) {
			sb.append(node).append('[').append(position).append(']');
		} else {
			sb.append(node.substring(0, posLeft + 1)).append(position)
				.append(node.substring(posRight));
		}
		return sb.toString();
	}

	private static String getBrotherNode(final String node, final String distance) {
		int offset = Integer.parseInt(distance);
		if (node == null) {
			throw new IllegalArgumentException("node is null");
		}
		if (node.isEmpty()) {
			throw new IllegalArgumentException("node is empty");
		}
		StringBuilder sb = new StringBuilder();
		int posLeft = node.indexOf('[');
		int posRight = node.indexOf(']');
		if (posLeft < 0 || posRight < 0) {
			sb.append(node).append('[').append(offset).append(']');
		} else {
			String idxStr = node.substring(posLeft + 1, posRight);
			int nodeIdx = Integer.parseInt(idxStr);
			int brotherIdx = nodeIdx + offset;
			sb.append(node.substring(0, posLeft + 1)).append(brotherIdx)
				.append(node.substring(posRight));
		}
		return sb.toString();
	}
}
