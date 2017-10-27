package keisuke.report.option;

import static keisuke.report.option.ReportOptionConstant.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

import keisuke.ArgumentMap;
import keisuke.option.AbstractCommandOption;
import keisuke.option.ArgumentMapImpl;
import keisuke.report.ProcedureType;

/**
 * Class to deal command arguments for DiffProc.
 */
public class DiffProcOption extends AbstractCommandOption {

	protected DiffProcOption() {
		super();
		this.setProcCommandName(ProcedureType.DIFF_PROC.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <infile>");
	}

	// DiffProcコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        options.addOption("a", OPT_AOUT, true, "output file name for added files");
        options.addOption("m", OPT_MOUT, true, "output file name for modified files");
        options.addOption("u", OPT_UNCHANGE, true,
        		"operation about unchanged files '" + OPTVAL_DETAIL + "' | '" + OPTVAL_TOTAL + "'");
        options.addOption("c", OPT_CLASS, true, "type to classify '"
        		+ OPTVAL_EXTENSION + "' | '" + OPTVAL_LANGUAGE + "' | '"
        		+ OPTVAL_LANGGROUP + "' | '" + OPTVAL_FW + "'<name>");
        options.addOption("p", OPT_PROP, true, "properties file name");
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
		if (this.commandline().hasOption(OPT_PROP)) {
			String fname = this.commandline().getOptionValue(OPT_PROP);
			map.put(OPT_PROP, fname);
		}
		if (this.commandline().hasOption(OPT_XML)) {
			String fname = this.commandline().getOptionValue(OPT_XML);
			map.put(OPT_XML, fname);
		}
		if (this.commandline().hasOption(OPT_CLASS)) {
			String operand = this.commandline().getOptionValue(OPT_CLASS);
			map.put(OPT_CLASS, operand);
		}
		if (this.commandline().hasOption(OPT_AOUT)) {
			String fname = this.commandline().getOptionValue(OPT_AOUT);
			map.put(OPT_AOUT, fname);
		}
		if (this.commandline().hasOption(OPT_MOUT)) {
			String fname = this.commandline().getOptionValue(OPT_MOUT);
			map.put(OPT_MOUT, fname);
		}
		if (this.commandline().hasOption(OPT_UNCHANGE)) {
			String operand = this.commandline().getOptionValue(OPT_UNCHANGE);
			map.put(OPT_UNCHANGE, operand);
		}

		String[] argArray = this.commandline().getArgs();
		if (argArray.length > 0) {
			map.put(ARG_INPUT, argArray[0]);
		}
		return new ArgumentMapImpl(map);
	}
}
