package keisuke.swing.stepcount;

/**
 * StepCount Swing画面用の定数定義
 */
final class StepCountGUIConstant {

	private StepCountGUIConstant() { }

	// GUI Component Name
	static final String MAIN_FRAME = "main_frame";
	static final String SOURCE_LIST = "source_list";
	static final String ADD_BUTTON = "add_button";
	static final String REMOVE_BUTTON = "remove_button";
	static final String FILE_CHOOSER = "src_choose";
	static final String SHOWDIR_CHECK = "showdir_box"; // not use since ver.2.0.0
	static final String HIDDEN_ADDING = "_hidden_add_text";

	// Table Column Index
	static final int COLUMNS_NUM	= 7;
	static final int INDEX_NAME		= 0;
	static final int INDEX_TYPE		= 1;
	static final int INDEX_CATEGORY	= 2;
	static final int INDEX_EXEC		= 3;
	static final int INDEX_BLANC	= 4;
	static final int INDEX_COMMENT	= 5;
	static final int INDEX_SUM		= 6;
	// Table Column Percentage of Width
	static final int HUNDRED_PERCENT	= 100;
	static final int PERCENTAGE_NAME	= 40;
	static final int PERCENTAGE_TYPE	= 10;
	static final int PERCENTAGE_CATEGORY = 10;
	static final int PERCENTAGE_EXEC 	= 10;
	static final int PERCENTAGE_BLANC 	= 10;
	static final int PERCENTAGE_COMMENT = 10;
	static final int PERCENTAGE_SUM 	= 10;
	static final int[] COL_WIDTH_PERCENTAGE = {PERCENTAGE_NAME, PERCENTAGE_TYPE, PERCENTAGE_CATEGORY,
				PERCENTAGE_EXEC, PERCENTAGE_BLANC, PERCENTAGE_COMMENT, PERCENTAGE_SUM};

}
