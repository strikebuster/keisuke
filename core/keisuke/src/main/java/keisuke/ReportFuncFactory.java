package keisuke;

/**
 * Factory class to return instance which implements ReportFunc.
 * @author strikebuster
 *
 */
final class ReportFuncFactory {
	private ReportFuncFactory() { }

	/**
	 * 各コマンド用の出力結果編集クラスの生成
	 * @param type コマンドの種類を指定する定数文字列
	 * @return IfReportFunc 各コマンド用の出力結果編集クラス
	 */
	static IfReportFunc createReportFunc(final String type) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.COUNTPROC)) {
			return new CountReportFunc();
		} else if (type.equals(CommonDefine.DIFFPROC)) {
			//return new DiffReportFunc();
			return new CountReportFunc();
		} else {
			return null;
		}
	}
}
