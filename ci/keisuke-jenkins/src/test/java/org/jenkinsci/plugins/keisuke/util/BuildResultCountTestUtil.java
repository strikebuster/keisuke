package org.jenkinsci.plugins.keisuke.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.keisuke.Messages;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import keisuke.count.CountTestUtil;

/**
 * Utility class for testing build result of KesiukePublisher
 */
public final class BuildResultCountTestUtil extends CountTestUtil {

	// {N/A, Status, Files#, Sum, Code, Comment, Blank}
	private static final String[] STEP_SUMMARY_TABLE_LABELS = {null, Messages.countStatus(),
			Messages.files(), Messages.total(), Messages.codes(), Messages.comments(), Messages.blanks()};

	// {#, Dir, File, Code, Blank, Comment, Sum}
	private static final String[] STEP_DETAIL_TABLE_LABELS = {null, Messages.dirPath(), Messages.fileName(),
			Messages.codes(), Messages.blanks(), Messages.comments(), Messages.sum()};

	// # of {Path, Code, Blank, Comment, Sum}
	private static final int STEPCOUNT_RESULT_SIMPLE_SIZE = 5;

	// {N/A, Status, Files#, Added, Deleted}
	private static final String[] DIFF_SUMMARY_TABLE_LABELS = {null, Messages.diffStatus(),
			Messages.files(), Messages.added(), Messages.deleted()};

	// {#, Dir, File, Status, Added, Deleted}
	private static final String[] DIFF_DETAIL_TABLE_LABELS = {null, Messages.dirPath(), Messages.fileName(),
			Messages.diffStatus(), Messages.added(), Messages.deleted()};

	// # of {Path, Status, Added, Deleted}
	private static final int DIFFCOUNT_RESULT_SIMPLE_SIZE = 4;

	/** constructor */
	private BuildResultCountTestUtil() { }

	public static String summaryStepLabelOf(final int index) {
		return messageToPageLabelPrefix(STEP_SUMMARY_TABLE_LABELS[index]);
	}

	public static int lengthOfSummaryStepLabels() {
		return STEP_SUMMARY_TABLE_LABELS.length;
	}

	public static String summaryDiffLabelOf(final int index) {
		return messageToPageLabelPrefix(DIFF_SUMMARY_TABLE_LABELS[index]);
	}

	public static int lengthOfSummaryDiffLabels() {
		return DIFF_SUMMARY_TABLE_LABELS.length;
	}

	private static final int JOBMAIN_STEP_LABELS_NUM = 5;

	public static String summaryLabelForJobMainOf(final int index) {
		String msg = null;

		if (index <= JOBMAIN_STEP_LABELS_NUM) { // index 1..5
			msg = summaryStepLabelOf(index + 1); // index:1 apply array[2]
		} else { // index 6..8
			msg = summaryDiffLabelOf(index - JOBMAIN_STEP_LABELS_NUM + 1); // index:6 apply array[2]
			if (index == JOBMAIN_STEP_LABELS_NUM + 1) { // index:6
				msg = Messages.diffFiles(); // not to be same as index:1
			}
		}
		return messageToPageLabelPrefix(msg);
	}

	public static int lengthOfSummaryStepLabelsForJobMain() {
		return lengthOfSummaryStepLabels() - 1;
	}

	public static int lengthOfSummaryDiffLabelsForJobMain() {
		return lengthOfSummaryDiffLabels() - 1;
	}

	public static String rowLabelOfCounted() {
		return Messages.counted();
	}

	public static String rowLabelOfUncounted() {
		return Messages.uncounted();
	}

	public static String rowLabelOfChanged() {
		return Messages.changed();
	}

	public static String rowLabelOfUnchanged() {
		return Messages.unchanged();
	}

	public static String rowLabelOfUnsupported() {
		return Messages.unsupported();
	}

	private static String messageToPageLabelPrefix(final String msg) {
		if (msg == null || msg.isEmpty()) {
			return msg;
		}
		String lastLetter = msg.substring(msg.length() - 1);
		if (lastLetter.equals("s")) {
			return msg.substring(0, msg.length() - 1);
		}
		return msg;
	}

	public static String detailStepLabelOf(final int index) {
		return messageToPageLabelPrefix(STEP_DETAIL_TABLE_LABELS[index]);
	}

	public static int lengthOfDetailStepLabels() {
		return STEP_DETAIL_TABLE_LABELS.length;
	}

	public static String detailDiffLabelOf(final int index) {
		return messageToPageLabelPrefix(DIFF_DETAIL_TABLE_LABELS[index]);
	}

	public static int lengthOfDetailDiffLabels() {
		return DIFF_DETAIL_TABLE_LABELS.length;
	}

	public static String contentWithoutTypeAndCategoryOf(final URL url) {
		String csv = contentOf(url);
		String[][] table = convertToTableArrayFrom(csv);
		String[][] woTable = new String[table.length][];
		for (int i = 0; i < table.length; i++) {
			List<String> strList = new ArrayList<String>();
			for (int j = 0; j < table[i].length; j++) {
				if (j != 1 && j != 2) {
					strList.add(table[i][j]);
				}
			}
			woTable[i] = strList.toArray(new String[strList.size()]);
		}
		return contentOf(woTable);
	}

	public static String[][] convertToTableArrayFromStepTableBody(final HtmlPage page, final HtmlTableBody tbody) {
		return convertToTableArrayFromTableBody(page, tbody,
				lengthOfDetailStepLabels(), STEPCOUNT_RESULT_SIMPLE_SIZE);
	}

	public static String[][] convertToTableArrayFromDiffTableBody(final HtmlPage page, final HtmlTableBody tbody) {
		return convertToTableArrayFromTableBody(page, tbody,
				lengthOfDetailDiffLabels(), DIFFCOUNT_RESULT_SIMPLE_SIZE);
	}

	private static String[][] convertToTableArrayFromTableBody(final HtmlPage page, final HtmlTableBody tbody,
			final int columns, final int arraySize) {
		String[][] strTable = null;
		List<String[]> arrayList = new ArrayList<String[]>();
		Iterable<DomNode> it = tbody.getChildren();
		for (DomNode node : it) {
			HtmlTableRow row = (HtmlTableRow) node;
			arrayList.add(convertToArrayFromTableRow(page, row, columns, arraySize));
		}
		if (!arrayList.isEmpty()) {
			strTable = new String[arrayList.size()][];
			int i = 0;
			for (String[] array : arrayList) {
				strTable[i] = array;
				i++;
			}
		}
		return strTable;
	}

	private static String[] convertToArrayFromTableRow(final HtmlPage page, final HtmlTableRow row,
			final int columns, final int arraySize) {
		String[] strArray = null;
		List<String> strList = new ArrayList<String>();
		for (int i = 1; i <= columns; i++) {
			String tdXpath = row.getCanonicalXPath() + "/td[" + i + "]";
			HtmlTableDataCell valueCell = page.getFirstByXPath(tdXpath);
			strList.add(valueCell.getTextContent());
		}
		if (strList.size() == columns) {
			strArray = new String[arraySize];
			strArray[0] = strList.get(1) + "/" + strList.get(2);
			for (int i = 1; i < arraySize; i++) {
				strArray[i] = strList.get(i + 2);
				if (strArray[i].equals("-")) {
					strArray[i] = null;
				}
			}
		}
		return strArray;
	}

}
