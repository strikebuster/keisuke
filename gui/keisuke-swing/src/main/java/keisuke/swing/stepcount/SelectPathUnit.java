package keisuke.swing.stepcount;

import static keisuke.count.option.CountOptionConstant.OPT_PATH;
import static keisuke.count.option.CountOptionConstant.OPTVAL_PATH_SHOWDIR;

import keisuke.CommandOption;
import keisuke.OptionValues;
import keisuke.count.option.StepCountOption;
import keisuke.swing.AbstractSelectPathUnit;

/**
 * StepCount用出力パス表記スタイル選択用部品
 */
public class SelectPathUnit extends AbstractSelectPathUnit {

	private static String[] pathValues;
	static {
		OptionValues pathOpt = (new StepCountOption()).valuesAs(OPT_PATH);
		// GUIではpathの選択肢にshowDirectoryオプションを追加する
		String[] optArray = pathOpt.getValuesAsArray();
		int len = optArray.length;
		pathValues = new String[len + 1];
		for (int i = 0; i < len; i++) {
			pathValues[i] = optArray[i];
		}
		pathValues[len] = OPTVAL_PATH_SHOWDIR;
	}

	SelectPathUnit(final CountCommandComponent owner, final CommandOption option) {
		super(owner, option);
	}

	/** {@inheritDoc} */
	@Override
	protected String[] defineValues() {
		return pathValues;
	}

}
