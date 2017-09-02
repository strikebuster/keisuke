package keisuke.report.editor;

import keisuke.ProcedureType;
import keisuke.ReportEditor;

/**
 * Factory class to return instance which implements ReportEditor.
 */
public final class ReportEditorFactory {
	private ReportEditorFactory() { }

	/**
	 * 各コマンド用の出力結果編集クラスの生成
	 * @param type コマンドの種類を指定する定数文字列
	 * @return 各コマンド用の出力結果編集クラス
	 */
	public static ReportEditor create(final ProcedureType type) {
		if (type == null) {
			return null;
		} else if (type.equals(ProcedureType.COUNT_PROC)) {
			return new CommonReportEditor();
		} else if (type.equals(ProcedureType.DIFF_PROC)) {
			return new CommonReportEditor();
		} else {
			return null;
		}
	}
}
