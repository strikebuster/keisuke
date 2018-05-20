package keisuke.swing;

import keisuke.OptionValues;

import static keisuke.count.option.CountOptionConstant.OPT_FORMAT;
import static keisuke.swing.GUIConstant.FORMAT_SELECT;

import keisuke.CommandOption;

/**
 * 出力フォーマット選択用部品
 */
public class SelectFormatUnit extends AbstractSelectUnit {

	private CommandComponent parent;
	private CommandOption comOption;

	public SelectFormatUnit(final CommandComponent owner, final CommandOption option) {
		this.parent = owner;
		this.comOption = option;
		super.initialize("format:", FORMAT_SELECT, this.defineValues());
	}

	private String[] defineValues() {
		OptionValues formatOpt = this.comOption.valuesAs(OPT_FORMAT);
		return formatOpt.getValuesAsArray();
	}

	/** {@inheritDoc} */
	protected void updateStatus() {
		this.parent.updateStatus();
	}

}
