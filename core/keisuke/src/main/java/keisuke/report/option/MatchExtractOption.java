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
 * Class to deal command arguments for MatchProc.
 */
public class MatchExtractOption extends AbstractCommandOption {

	private static OptionValues pathStyle = new OptionValues();
	static {
		pathStyle.add(OPTVAL_BASE);
		pathStyle.add(OPTVAL_SUB);
	}

	protected MatchExtractOption() {
		super();
		this.setProcCommandName(ProcedureType.MATCH_PROC.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <mafile> <trfile>  [<outfile>]");
	}

	/** {@inheritDoc} */
	public OptionValues valuesAs(final String optname) {
		if (optname == null || optname.isEmpty()) {
			return null;
		} else if (optname.equals(OPT_PATH)) {
			return pathStyle;
		} else {
			return null;
		}
	}

	// MatchProcコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        options.addOption("o", OPT_OUT, true, "output file name for extracting");
        options.addOption("p", OPT_PATH, true, "path style of input files " + pathStyle.printList());
        this.setOptions(options);
    }

	// MatchProcコマンドライン引数の解析
	/** {@inheritDoc} */
	@Override
	public ArgumentMap makeOwnOptionMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_MASTER, null);
		map.put(ARG_TRANSACTION, null);
		map.put(ARG_OUTPUT, null);
		// 引数の解析
		if (this.commandline().hasOption(OPT_PATH)) {
			map.put(OPT_PATH, this.commandline().getOptionValue(OPT_PATH));
		}
		if (this.commandline().hasOption(OPT_OUT)) {
			map.put(OPT_OUT, this.commandline().getOptionValue(OPT_OUT));
		}
		String[] argArray = this.makeRestArgArray();
		if (argArray.length > 0) {
			map.put(ARG_MASTER, argArray[0]);
		}
		if (argArray.length > 1) {
			map.put(ARG_TRANSACTION, argArray[1]);
		}
		if (argArray.length > 2) {
			map.put(ARG_OUTPUT, argArray[2]);
		}
		if (argArray.length < 2) {
			this.showUsage();
		}
		return new ArgumentMapImpl(map);
	}
}
