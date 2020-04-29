package keisuke.swing.diffcount;

import static keisuke.count.option.CountOptionConstant.OPT_SORT;

import keisuke.CommandOption;
import keisuke.OptionValues;
import keisuke.count.option.DiffCountOption;
import keisuke.swing.AbstractSelectSortUnit;

/**
 * DiffCount用出力ソート順選択用部品
 */
public class SelectSortUnit extends AbstractSelectSortUnit {

	private static String[] sortValues;
	static {
		OptionValues sortOpt = (new DiffCountOption()).valuesAs(OPT_SORT);
		// GUIではpathの選択肢に空オプションを追加する
		String[] optArray = sortOpt.getValuesAsArray();
		int len = optArray.length;
		sortValues = new String[len + 1];
		for (int i = 0; i < len; i++) {
			sortValues[i + 1] = optArray[i];
		}
		sortValues[0] = "";
	}

	SelectSortUnit(final DiffCommandComponent owner, final CommandOption option) {
		super(owner, option);
	}

	/** {@inheritDoc} */
	@Override
	protected String[] defineValues() {
		return sortValues;
	}
}
