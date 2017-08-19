package keisuke.count.diff;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.Options;
import keisuke.AbstractArgFunc;
import keisuke.count.SCCommonDefine;

/**
 * Class to deal command arguments for DiffCount.
 * @author strikebuster
 *
 */
public class DiffCountArgFunc extends AbstractArgFunc {

	// keisuke.count.DiffCount でnew	するため、protectedではなくpublic
	public DiffCountArgFunc() {
		super();
		this.setProcType(SCCommonDefine.DIFFCOUNTPROC);
		this.setOptions(this.createDiffCountOptions());
		this.setUsage(SCCommonDefine.DIFFCOUNTPROC +  " <newDir> <oldDir>");
	}

	/** DiffCount引数設定
	 * @return Options DiffCount用のオプション定義
	 */
	protected Options createDiffCountOptions() {
        Options options = new Options();
        options.addOption("?", SCCommonDefine.OPT_HELP, false, "show help");
        options.addOption("e", SCCommonDefine.OPT_ENCODE, true, "encoding name");
        options.addOption("o", SCCommonDefine.OPT_OUTPUT, true, "file name");
        options.addOption("f", SCCommonDefine.OPT_FORMAT, true,
        		"'" + SCCommonDefine.OPTVAL_TEXT + "' | '" + SCCommonDefine.OPTVAL_HTML + "' | '"
        				+ SCCommonDefine.OPTVAL_EXCEL + "'");
        options.addOption("x", SCCommonDefine.OPT_XML, true, "xml file name");
        return options;
    }

	// StepCountコマンドライン引数の解析
	@Override
	/** {@inheritDoc} */
	public Map<String, String> makeOwnArgMap() {
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
		if (this.commandline().hasOption(SCCommonDefine.OPT_XML)) {
			String fname = this.commandline().getOptionValue(SCCommonDefine.OPT_XML);
			map.put(SCCommonDefine.OPT_XML, fname);
		}

        return map;
	}

}
