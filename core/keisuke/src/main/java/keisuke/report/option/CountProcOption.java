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
 * Class to deal command arguments for CountProc.
 */
public class CountProcOption extends AbstractCommandOption {

	private static OptionValues classify = new OptionValues();
	static {
		classify.add(OPTVAL_EXTENSION);
		classify.add(OPTVAL_LANGUAGE);
		classify.add(OPTVAL_LANGGROUP);
		classify.add(OPTVAL_FW, "name");
	}

	protected CountProcOption() {
		super();
		this.setProcCommandName(ProcedureType.COUNT_PROC.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <infile>");
	}

	/** {@inheritDoc} */
	public OptionValues valuesAs(final String optname) {
		if (optname == null || optname.isEmpty()) {
			return null;
		} else if (optname.equals(OPT_CLASS)) {
			return classify;
		} else {
			return null;
		}
	}

	// CountProcコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
		Options options = new Options();
        options.addOption("?", "help", false, "show help");
        options.addOption("c", OPT_CLASS, true, "type to classify " + classify.printList());
        options.addOption("o", OPT_OUT, true, "output file name");
        options.addOption("p", OPT_PROP, true, "properties file name");
        options.addOption("x", OPT_XML, true, "xml file name");
		this.setOptions(options);
	}

	// CountProcコマンドライン引数の解析
	/** {@inheritDoc} */
	@Override
	public ArgumentMap makeOwnOptionMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_INPUT, null);
		// 引数の解析
		if (this.commandline().hasOption(OPT_OUT)) {
			map.put(OPT_OUT, this.commandline().getOptionValue(OPT_OUT));
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
		String[] argArray = this.makeRestArgArray();
		if (argArray.length > 0) {
			map.put(ARG_INPUT, argArray[0]);
		}
        return new ArgumentMapImpl(map);
	}
}
