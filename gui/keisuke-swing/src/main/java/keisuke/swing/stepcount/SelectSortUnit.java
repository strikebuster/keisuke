package keisuke.swing.stepcount;

import keisuke.OptionValues;
import keisuke.count.option.StepCountOption;
import keisuke.swing.AbstractSelectUnit;

import static keisuke.count.option.CountOptionConstant.OPT_SORT;
import static keisuke.swing.stepcount.StepCountGUIConstant.SORT_SELECT;

/**
 * 出力ソート順選択用部品
 */
class SelectSortUnit extends AbstractSelectUnit {

	private CountCommandComponent parent;

	SelectSortUnit(final CountCommandComponent owner) {
		this.parent = owner;
		super.initialize("sort:", SORT_SELECT, this.defineValues());
	}

	private String[] defineValues() {
		OptionValues formatOpt = (new StepCountOption()).valuesAs(OPT_SORT);
		return formatOpt.getValuesAsArray();
	}

	/** {@inheritDoc} */
	protected void updateStatus() {
		this.parent.updateStatus();
	}

}
