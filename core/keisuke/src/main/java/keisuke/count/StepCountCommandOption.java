package keisuke.count;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.Options;

import keisuke.ArgumentMap;
import keisuke.option.AbstractCommandOption;
import keisuke.option.ArgumentMapImpl;

/**
 * Class to deal command arguments for StepCount.
 */
public class StepCountCommandOption extends AbstractCommandOption {

	protected StepCountCommandOption() {
		super();
		this.setProcCommandName(SCCommonDefine.STEPCOUNTPROC);
		this.configureOption();
		this.setUsage(SCCommonDefine.STEPCOUNTPROC +  " <infile> [<infile2> ..]");
	}

	// StepCountコマンドライン引数の設定
	@Override
	/** {@inheritDoc} */
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", SCCommonDefine.OPT_HELP, false, "show help");
        options.addOption("e", SCCommonDefine.OPT_ENCODE, true, "encoding name");
        options.addOption("o", SCCommonDefine.OPT_OUTPUT, true, "file name");
        options.addOption("f", SCCommonDefine.OPT_FORMAT, true,
        		"'" + SCCommonDefine.OPTVAL_TEXT + "' | '" + SCCommonDefine.OPTVAL_CSV + "' | '"
        				+ SCCommonDefine.OPTVAL_EXCEL + "' | '" + SCCommonDefine.OPTVAL_XML + "' | '"
        				+ SCCommonDefine.OPTVAL_JSON + "'");
        options.addOption("s", SCCommonDefine.OPT_SHOWDIR, false, "show source files path");
        options.addOption("x", SCCommonDefine.OPT_XML, true, "xml file name");
        this.setOptions(options);
    }

	// StepCountコマンドライン引数の解析
	@Override
	/** {@inheritDoc} */
	public ArgumentMap makeOwnOptionMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		// 引数の解析
		if (this.commandline().hasOption(SCCommonDefine.OPT_ENCODE)) {
			String encoding = this.commandline().getOptionValue(SCCommonDefine.OPT_ENCODE);
			map.put(SCCommonDefine.OPT_ENCODE, encoding);
		}
		if (this.commandline().hasOption(SCCommonDefine.OPT_OUTPUT)) {
			String fname = this.commandline().getOptionValue(SCCommonDefine.OPT_OUTPUT);
			map.put(SCCommonDefine.OPT_OUTPUT, fname);
		}
		if (this.commandline().hasOption(SCCommonDefine.OPT_FORMAT)) {
			String format = this.commandline().getOptionValue(SCCommonDefine.OPT_FORMAT);
			map.put(SCCommonDefine.OPT_FORMAT, format);
		}
		if (this.commandline().hasOption(SCCommonDefine.OPT_SHOWDIR)) {
			map.put(SCCommonDefine.OPT_SHOWDIR, "true");
		}
		if (this.commandline().hasOption(SCCommonDefine.OPT_XML)) {
			String fname = this.commandline().getOptionValue(SCCommonDefine.OPT_XML);
			map.put(SCCommonDefine.OPT_XML, fname);
		}

		return new ArgumentMapImpl(map);
	}

}
