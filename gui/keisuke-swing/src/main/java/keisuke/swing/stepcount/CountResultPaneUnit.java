package keisuke.swing.stepcount;

import static keisuke.swing.GUIConstant.RESULT_TABLE;
import static keisuke.swing.stepcount.StepCountGUIConstant.*;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import keisuke.StepCountResult;
import keisuke.report.property.MessageDefine;
import keisuke.swing.ResultPaneUnit;
import keisuke.swing.ResultViewComponent;

/**
 * 計測結果テキスト表示用GUI部品
 */
class CountResultPaneUnit extends ResultPaneUnit {

	private JTable contentsTable;
	private StepCountTableModel model;

	CountResultPaneUnit(final ResultViewComponent owner, final MessageDefine msgdef) {
		super(owner);
		// テーブル準備
		this.model = new StepCountTableModel(msgdef);
		this.contentsTable = new JTable(this.model);
		this.contentsTable.setName(RESULT_TABLE);
		this.contentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		this.adjustColumnsWidth();
	}

	void adjustColumnsWidth() {
		int saveMode = this.contentsTable.getAutoResizeMode();
		this.contentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    TableColumnModel columnModel = this.contentsTable.getColumnModel();
	    int columnsTotalWidth = columnModel.getTotalColumnWidth();
	    //System.err.println("before total=" + columnsTotalWidth);
	    if (columnsTotalWidth == 0) {
	    	columnsTotalWidth = PANE_WIDTH;
	    }
	    int remainingWidth = columnsTotalWidth;
	    for (int i = 0; i < COLUMNS_NUM; i++) {
	    	TableColumn column = columnModel.getColumn(i);
	        int columnWidth = 0;
	        if (i + 1 == COLUMNS_NUM) {
	        	columnWidth = remainingWidth;
	        } else {
	        	columnWidth = Math.round((float) columnsTotalWidth * COL_WIDTH_PERCENTAGE[i] / HUNDRED_PERCENT);
	        }
	        remainingWidth -= columnWidth;
	        column.setPreferredWidth(columnWidth);
	        column.setWidth(columnWidth);
	       // System.err.println("adjust! column[" + i + "]="
		    //		+ column.getPreferredWidth());
	       // System.err.println("actually! column[" + i + "]="
		   // 		+ column.getWidth());
	    }
	    //System.err.println("after total=" + columnModel.getTotalColumnWidth());
	    this.contentsTable.setAutoResizeMode(saveMode);
	}

	void showTable(final StepCountResult[] results) {
		this.model.buildTableFrom(results);
		this.basePane().setViewportView(this.contentsTable);
	}

}
