package keisuke.report.procedure;

import keisuke.MainProcedure;
import keisuke.report.ProcedureType;

/**
 * Factory class to create MainProcedure
 */
public final class MainProcFactory {

	private MainProcFactory() { }

	/**
	 * 各コマンド用のメイン処理クラスの生成
	 * @param type コマンドの種類を指定する列挙子の値
	 * @return 各コマンド用のメイン処理クラス
	 */
	public static MainProcedure create(final ProcedureType type) {
		if (type == null) {
			return null;
		} else if (type.equals(ProcedureType.COUNT_PROC)) {
			return new CountMainProc();
		} else if (type.equals(ProcedureType.DIFF_PROC)) {
			return new DiffMainProc();
		} else if (type.equals(ProcedureType.MATCH_PROC)) {
			return new MatchMainProc();
		} else {
			return null;
		}
	}
}
