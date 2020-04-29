package keisuke.swing;

import static keisuke.count.option.CountOptionConstant.OPT_SORT;
import static keisuke.swing.GUIConstant.SORT_SELECT;

import keisuke.CommandOption;
import keisuke.OptionValues;

/**
 * 出力ソート順選択用部品の基底クラス
 */
public abstract class AbstractSelectSortUnit extends AbstractSelectUnit {

	private AbstractCommandComponent parent;
	private CommandOption comOption;

	public AbstractSelectSortUnit(final AbstractCommandComponent owner, final CommandOption option) {
		this.parent = owner;
		this.comOption = option;
		super.initialize("sort:", SORT_SELECT, this.defineValues());
	}

	/**
	 * ソートオプションの選択肢の配列を返す
	 * @return 選択肢の配列
	 */
	protected String[] defineValues() {
		OptionValues sortOpt = this.comOption.valuesAs(OPT_SORT);
		return sortOpt.getValuesAsArray();
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
