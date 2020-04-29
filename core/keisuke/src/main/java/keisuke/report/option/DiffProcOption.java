package keisuke.report.option;

import static keisuke.report.option.ReportOptionConstant.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

import keisuke.ArgumentMap;
import keisuke.OptionValues;
import keisuke.option.AbstractCommandOption;
import keisuke.option.ArgumentMapImpl;
import keisuke.report.ProcedureType;

/**
 * Class to deal command arguments for DiffProc.
 */
public class DiffProcOption extends AbstractCommandOption {

	private static OptionValues classify = new OptionValues();
	private static OptionValues format = new OptionValues();
	private static OptionValues unchange = new OptionValues();
	static {
		classify.add(OPTVAL_EXTENSION);
		classify.add(OPTVAL_LANGUAGE);
		classify.add(OPTVAL_LANGGROUP);
		classify.add(OPTVAL_FW, "name");
		format.add(OPTVAL_TEXT);
		format.add(OPTVAL_CSV);
		unchange.add(OPTVAL_DETAIL);
		unchange.add(OPTVAL_TOTAL);
	}

	protected DiffProcOption() {
		super();
		this.setProcCommandName(ProcedureType.DIFF_PROC.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <infile>");
	}

	/** {@inheritDoc} */
	public OptionValues valuesAs(final String optname) {
		if (optname == null || optname.isEmpty()) {
			return null;
		} else if (optname.equals(OPT_CLASS)) {
			return classify;
		} else if (optname.equals(OPT_FORMAT)) {
			return format;
		} else if (optname.equals(OPT_UNCHANGE)) {
			return unchange;
		} else {
			return null;
		}
	}

	// DiffProcコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        options.addOption("a", OPT_AOUT, true, "output file name for listing added files");
        options.addOption("c", OPT_CLASS, true, "type to classify " + classify.printList());
        options.addOption("f", OPT_FORMAT, true, "format of input " + format.printList());
        options.addOption("m", OPT_MOUT, true, "output file name for listing modified files");
        options.addOption("o", OPT_OUT, true, "output file name for reporting");
        options.addOption("p", OPT_PROP, true, "properties file name");
        options.addOption("u", OPT_UNCHANGE, true,
        		"operation about unchanged files " + unchange.printList());
        options.addOption("x", OPT_XML, true, "xml file name");
        this.setOptions(options);
    }

	// DiffProcコマンドライン引数の解析
	/** {@inheritDoc} */
	@Override
	public ArgumentMap makeOwnOptionMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_INPUT, null);
		map.put(OPT_AOUT, null);
		map.put(OPT_MOUT, null);
		// 引数の解析
		if (this.commandline().hasOption(OPT_OUT)) {
			map.put(OPT_OUT, this.commandline().getOptionValue(OPT_OUT));
		}
		if (this.commandline().hasOption(OPT_AOUT)) {
			map.put(OPT_AOUT, this.commandline().getOptionValue(OPT_AOUT));
		}
		if (this.commandline().hasOption(OPT_MOUT)) {
			map.put(OPT_MOUT, this.commandline().getOptionValue(OPT_MOUT));
		}
		if (this.commandline().hasOption(OPT_PROP)) {
			map.put(OPT_PROP, this.commandline().getOptionValue(OPT_PROP));
		}
		if (this.commandline().hasOption(OPT_XML)) {
			map.put(OPT_XML, this.commandline().getOptionValue(OPT_XML));
		}
		if (this.commandline().hasOption(OPT_CLASS)) {
			map.put(OPT_CLASS, this.commandline().getOptionValue(OPT_CLASS));
		}
		if (this.commandline().hasOption(OPT_FORMAT)) {
			map.put(OPT_FORMAT, this.commandline().getOptionValue(OPT_FORMAT));
		}
		if (this.commandline().hasOption(OPT_UNCHANGE)) {
			map.put(OPT_UNCHANGE, this.commandline().getOptionValue(OPT_UNCHANGE));
		}

		String[] argArray = this.makeRestArgArray();
		if (argArray.length > 0) {
			map.put(ARG_INPUT, argArray[0]);
		}
		return new ArgumentMapImpl(map);
	}
}
