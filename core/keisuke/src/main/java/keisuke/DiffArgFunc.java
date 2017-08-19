package keisuke;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

/**
 * Class to deal command arguments for DiffProc.
 */
class DiffArgFunc extends AbstractArgFunc {

	protected static final String ARG_INPUT = "infile";
	protected static final String OPT_AOUT = "aout";
	protected static final String OPT_MOUT = "mout";
	protected static final String OPT_UNCHANGE = "unchange";
	protected static final String OPTVAL_TOTAL = "total";
	protected static final String OPTVAL_DETAIL = "detail";

	protected DiffArgFunc() {
		super();
		this.setProcType(CommonDefine.DIFFPROC);
		this.setOptions(this.createDiffOptions());
		this.setUsage(CommonDefine.DIFFPROC +  " <infile>");
	}

	// DiffProc引数設定
	private Options createDiffOptions() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        options.addOption("a", OPT_AOUT, true, "output file name for added files");
        options.addOption("m", OPT_MOUT, true, "output file name for modified files");
        options.addOption("u", OPT_UNCHANGE, true,
        		"operation about unchanged files '" + OPTVAL_DETAIL + "' | '" + OPTVAL_TOTAL + "'");
        options.addOption("c", CommonDefine.OPT_CLASS, true, "type to classify '"
        		+ CommonDefine.OPTVAL_EXTENSION + "' | '" + CommonDefine.OPTVAL_LANGUAGE + "' | '"
        		+ CommonDefine.OPTVAL_LANGGROUP + "' | '" + CommonDefine.OPTVAL_FW + "'<name>");
        options.addOption("p", CommonDefine.OPT_PROP, true, "properties file name");
        options.addOption("x", CommonDefine.OPT_XML, true, "xml file name");
        return options;
    }

	// DiffProcコマンドライン引数の解析
	/** {@inheritDoc} */
	@Override
	public Map<String, String> makeOwnArgMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_INPUT, null);
		map.put(OPT_AOUT, null);
		map.put(OPT_MOUT, null);
		// 引数の解析
		if (this.commandline().hasOption(CommonDefine.OPT_PROP)) {
			String fname = this.commandline().getOptionValue(CommonDefine.OPT_PROP);
			map.put(CommonDefine.OPT_PROP, fname);
		}
		if (this.commandline().hasOption(CommonDefine.OPT_XML)) {
			String fname = this.commandline().getOptionValue(CommonDefine.OPT_XML);
			map.put(CommonDefine.OPT_XML, fname);
		}
		if (this.commandline().hasOption(CommonDefine.OPT_CLASS)) {
			String operand = this.commandline().getOptionValue(CommonDefine.OPT_CLASS);
			map.put(CommonDefine.OPT_CLASS, operand);
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
        return map;
	}
}
