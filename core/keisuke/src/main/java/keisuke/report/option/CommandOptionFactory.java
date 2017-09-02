package keisuke.report.option;

import keisuke.CommandOption;
import keisuke.ProcedureType;

/**
 * Factory class to return an instance which implements CommandOption.
 */
public final class CommandOptionFactory {

	private CommandOptionFactory() { }

	/**
	 * 各コマンド用のオプション解析クラスの生成
	 * @param type コマンドの種類を指定する定数文字列
	 * @return 各コマンド用のオプション解析クラス
	 */
	public static CommandOption create(final ProcedureType type) {
		if (type == null) {
			return null;
		} else if (type.equals(ProcedureType.COUNT_PROC)) {
			return new CountCommandOption();
		} else if (type.equals(ProcedureType.DIFF_PROC)) {
			return new DiffCommandOption();
		} else if (type.equals(ProcedureType.MATCH_PROC)) {
			return new MatchCommandOption();
		} else {
			return null;
		}
	}
}
