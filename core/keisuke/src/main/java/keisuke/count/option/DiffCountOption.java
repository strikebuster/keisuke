package keisuke.count.option;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.Options;

import keisuke.ArgumentMap;
import keisuke.count.CountType;
import keisuke.option.AbstractCommandOption;
import keisuke.option.ArgumentMapImpl;
import static keisuke.count.option.CountOptionConstant.*;

/**
 * Class to deal command arguments for DiffCount.
 */
public class DiffCountOption extends AbstractCommandOption {

	public DiffCountOption() {
		super();
		this.setProcCommandName(CountType.DIFF_COUNT.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <newDir> <oldDir>");
	}

	// DiffCountコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", OPT_HELP, false, "show help");
        options.addOption("e", OPT_ENCODE, true, "encoding name");
        options.addOption("o", OPT_OUTPUT, true, "file name");
        options.addOption("f", OPT_FORMAT, true,
        		"'" + OPTVAL_TEXT + "' | '" + OPTVAL_HTML + "' | '"
        				+ OPTVAL_EXCEL + "'");
        options.addOption("x", OPT_XML, true, "xml file name");
        this.setOptions(options);
    }

	// DiffCountコマンドライン引数の解析
	/** {@inheritDoc} */
	@Override
	public ArgumentMap makeOwnOptionMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		// 引数の解析
		if (this.commandline().hasOption(OPT_ENCODE)) {
			String encoding = this.commandline().getOptionValue(OPT_ENCODE);
			map.put(OPT_ENCODE, encoding);
		}
		if (this.commandline().hasOption(OPT_OUTPUT)) {
			String fname = this.commandline().getOptionValue(OPT_OUTPUT);
			map.put(OPT_OUTPUT, fname);
		}
		if (this.commandline().hasOption(OPT_FORMAT)) {
			String format = this.commandline().getOptionValue(OPT_FORMAT);
			map.put(OPT_FORMAT, format);
		}
		if (this.commandline().hasOption(OPT_XML)) {
			String fname = this.commandline().getOptionValue(OPT_XML);
			map.put(OPT_XML, fname);
		}

		return new ArgumentMapImpl(map);
	}

}
