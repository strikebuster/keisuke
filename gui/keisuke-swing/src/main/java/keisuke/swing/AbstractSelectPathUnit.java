package keisuke.swing;

import static keisuke.count.option.CountOptionConstant.OPT_PATH;
import static keisuke.swing.GUIConstant.PATH_SELECT;

import keisuke.CommandOption;
import keisuke.OptionValues;

/**
 * 出力パス表記スタイル選択用部品の基底クラス
 */
public abstract class AbstractSelectPathUnit extends AbstractSelectUnit {

	private AbstractCommandComponent parent;
	private CommandOption comOption;

	public AbstractSelectPathUnit(final AbstractCommandComponent owner, final CommandOption option) {
		this.parent = owner;
		this.comOption = option;
		super.initialize("path style:", PATH_SELECT, this.defineValues());
	}

	/**
	 * パス表記オプションの選択肢の配列を返す
	 * @return 選択肢の配列
	 */
	protected String[] defineValues() {
		OptionValues pathOpt = this.comOption.valuesAs(OPT_PATH);
		return pathOpt.getValuesAsArray();
	}

	/** {@inheritDoc} */
	protected void updateStatus() {
		this.parent.updateStatus();
	}

	/**
	 * 親GUIコンポーネントを返す
	 * @return 親GUIコンポーネント
	 */
	protected AbstractCommandComponent parent() {
		return this.parent;
	}

}
