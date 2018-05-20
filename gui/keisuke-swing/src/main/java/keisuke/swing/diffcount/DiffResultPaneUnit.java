package keisuke.swing.diffcount;

import static keisuke.swing.GUIConstant.RESULT_TABLE;
import static keisuke.swing.diffcount.DiffCountGUIConstant.*;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
//import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXTreeTable;

import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;
import keisuke.swing.ResultPaneUnit;
import keisuke.swing.ResultViewComponent;

/**
 * Diff結果表示用GUI部品
 */
class DiffResultPaneUnit extends ResultPaneUnit {

	private JXTreeTable contentsTable;
	private DiffCountTreeTableModel model;

	@SuppressWarnings("serial")
	DiffResultPaneUnit(final ResultViewComponent owner, final MessageDefine msgdef) {
		super(owner);
		// テーブル準備
		this.model = new DiffCountTreeTableModel(msgdef);
		this.contentsTable = new JXTreeTable(this.model) {
			@Override
		    public Component prepareRenderer(final TableCellRenderer renderer,
		    		final int row, final int column) {
				Component component;
				if (column == INDEX_STATUS) {
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(JLabel.CENTER);
					component = super.prepareRenderer(centerRenderer, row, column);
				} else if (column == INDEX_ADDED || column == INDEX_DELETEED) {
					DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
					rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
					component = super.prepareRenderer(rightRenderer, row, column);
				} else {
					component = super.prepareRenderer(renderer, row, column);
				}
		        return applyRenderer(component, getComponentAdapter(row, column));
			}
		};
		this.contentsTable.setName(RESULT_TABLE);
		this.contentsTable.setEditable(false);
		this.contentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		//this.adjustColumnsWidth();
	}

	void showTable(final DiffFolderResult result) {
		this.model.buildTreeOutOf(result);
		//this.adjustColumnsWidth();
		this.basePane().setViewportView(this.contentsTable);
	}

	/*
	 * カラム幅の設定を試みたが、TreeTableModelの内容が更新されると、
	 * カラム幅が同じになるように初期化されてしまう。
	 * 制御することがうまくできなかったので諦める
	 *
	void adjustColumnsWidth() {
		int saveMode = this.contentsTable.getAutoResizeMode();
		this.contentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int frameWidth = this.root.width();
		System.err.println("frame width=" + frameWidth);
	    int tableWidth = this.contentsTable.getWidth();
	    System.err.println("table width=" + tableWidth);
	    if (tableWidth == 0) {
	    	tableWidth = frameWidth;
	    }
	    TableColumnModel columnModel = this.contentsTable.getColumnModel();
	    int columnsTotalWidth = columnModel.getTotalColumnWidth();
	    System.err.println("before total=" + columnsTotalWidth);
	    int remainingWidth = columnsTotalWidth;
	    for (int i = 0; i < COLUMNS_NUM; i++) {
	    	TableColumn column = columnModel.getColumn(i);
	        int columnWidth = 0;
	        if (i + 1 == COLUMNS_NUM) {
	        	columnWidth = remainingWidth;
	        } else {
	        	columnWidth = Math.round(columnsTotalWidth * COL_WIDTH_PERCENTAGE[i] / HUNDRED_PERCENT);
	        }
	        remainingWidth -= columnWidth;
	        column.setPreferredWidth(columnWidth);
	        column.setWidth(columnWidth);
	        System.err.println("adjust! column[" + i + "]="
		    		+ column.getPreferredWidth());
	        System.err.println("actually! column[" + i + "]="
		    		+ column.getWidth());
	    }
	    System.err.println("adjust! total=" + tableWidth);
	    System.err.println("after total=" + columnModel.getTotalColumnWidth());
	    this.contentsTable.setAutoResizeMode(saveMode);
	}
	*/

	DiffCountTreeTableModel model() {
		return this.model;
	}

	void showAll() {
		this.contentsTable.expandAll();
	}

	void hideAll() {
		this.contentsTable.collapseAll();
	}

}
