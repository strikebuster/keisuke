package keisuke.swing.stepcount;

import static keisuke.count.step.format.FormatConstant.*;
import static keisuke.swing.stepcount.StepCountGUIConstant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import keisuke.MessageMap;
import keisuke.StepCountResult;
import keisuke.report.property.MessageDefine;

/**
 * StepCountResult配列をTableModelの形式にマッピングしたクラス
 */
class StepCountTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private MessageMap messageMap = null;

	StepCountTableModel(final MessageDefine msgdef) {
		super();
		this.setupConfig(msgdef);
		super.setColumnIdentifiers(convertToVectorFrom(this.createColumnIdentifiers()));
	}

	/**
	 * 出力メッセージを設定する.
	 * @param msgdef メッセージ定義インスタンス
	 */
	void setupConfig(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.messageMap = msgdef.getMessageMap();
	}

	private List<String> createColumnIdentifiers() {
		List<String> columnIdentifiers = new ArrayList<String>();
		if (this.messageMap != null) {
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_PATH));
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_TYPE));
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_CATEGORY));
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_EXEC));
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_BLANC));
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_COMMENT));
			columnIdentifiers.add(this.messageMap.get(MSG_COUNT_FMT_SUM));
		} else {
			columnIdentifiers.add("FileName");
			columnIdentifiers.add("Type");
			columnIdentifiers.add("Category");
			columnIdentifiers.add("Exec");
			columnIdentifiers.add("Blanc");
			columnIdentifiers.add("Comment");
			columnIdentifiers.add("Sum");
		}
		return columnIdentifiers;
	}

	void buildTableFrom(final StepCountResult[] results) {
		this.resetTable();
		for (StepCountResult result : results) {
			Object[] objArray = new Object[COLUMNS_NUM];
			objArray[INDEX_NAME] = result.filePath();
			if (result.sourceType() != null) {
				objArray[INDEX_TYPE] = result.sourceType();
				objArray[INDEX_CATEGORY] = result.sourceCategory();
				objArray[INDEX_EXEC] = result.execSteps();
				objArray[INDEX_BLANC] = result.blancSteps();
				objArray[INDEX_COMMENT] = result.commentSteps();
				objArray[INDEX_SUM] = result.sumSteps();
			} else {
				objArray[INDEX_TYPE] = this.messageMap.get(MSG_COUNT_FMT_UNDEF);
			}
			super.addRow(objArray);
		}
	}

	void resetTable() {
		int size = super.getRowCount();
		for (int i = size - 1; i >= 0; i--) {
			super.removeRow(i);
		}
	}

	static Vector<String> convertToVectorFrom(final List<String> list) {
		Vector<String> vec = new Vector<String>();
		vec.addAll(list);
		return vec;
	}
}
