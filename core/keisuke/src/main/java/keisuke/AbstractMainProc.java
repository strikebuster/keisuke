package keisuke;

import java.util.Map;

/**
 * Common base class for main procedure of command.
 * We have common variables for command options and arguments.
 */
public abstract class AbstractMainProc {

	private CommandOption commandOption = null;
	private ArgumentMap argMap = null;

	/** {@inheritDoc} */
	public abstract void main(String[] args);

	/**
	 * コマンドオプション処理のインスタンスを設定する
	 * @param comOpt コマンドオプション処理のインスタンス
	 */
	protected void setCommandOption(final CommandOption comOpt) {
		this.commandOption = comOpt;
	}

	/**
	 * コマンドオプション処理のインスタンスを返す
	 * @return コマンドオプション処理のインスタンス
	 */
	public CommandOption commandOption() {
		return this.commandOption;
	}

	/**
	 * 引数解析結果のマップを設定する
	 * @param map 引数解析結果のマップ
	 */
	protected void setArgMap(final ArgumentMap map) {
		this.argMap = map;
	}

	/**
	 * 引数解析結果のマップを返す
	 * @return 引数解析結果のマップ
	 */
	protected ArgumentMap argMap() {
		return this.argMap;
	}

	/**
	 * 引数解析結果のマップを返す
	 * テスト用に可視性がpublic
	 * @return 引数解析結果のマップの実体Map
	 */
	public Map<String, String> argMapEntity() {
		if (this.argMap == null) {
			return null;
		}
		return this.argMap.getMap();
	}
}
