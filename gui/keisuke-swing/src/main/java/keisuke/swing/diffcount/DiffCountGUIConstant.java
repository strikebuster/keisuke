package keisuke.swing.diffcount;

/**
 * DiffCount Swing画面用の定数定義
 */
final class DiffCountGUIConstant {

	private DiffCountGUIConstant() { }

	// GUI Component Name
	static final String OLD_DIR_TEXTFIELD = "old_dir_text";
	static final String OLD_DIR_BUTTON = "old_dir_button";
	static final String NEW_DIR_TEXTFIELD = "new_dir_text";
	static final String NEW_DIR_BUTTON = "new_dir_button";
	static final String SHOW_BUTTON = "show_button";
	static final String HIDE_BUTTON = "hide_button";
	static final String TOTAL_LABEL = "total_label";
	static final String FILE_CHOOSER = "dir_choose";

	// Table Column Index
	static final int COLUMNS_NUM	= 4;
	static final int INDEX_NAME		= 0;
	static final int INDEX_STATUS	= 1;
	static final int INDEX_ADDED	= 2;
	static final int INDEX_DELETEED	= 3;
	/*
	// Table Column Percentage of Width
	static final int HUNDRED_PERCENT	= 100;
	static final int PERCENTAGE_NAME	= 60;
	static final int PERCENTAGE_STATUS	= 20;
	static final int PERCENTAGE_ADDED	= 10;
	static final int PERCENTAGE_DELETED = 10;
	static final int[] COL_WIDTH_PERCENTAGE =
		{PERCENTAGE_NAME, PERCENTAGE_STATUS, PERCENTAGE_ADDED, PERCENTAGE_DELETED};
	*/
}
