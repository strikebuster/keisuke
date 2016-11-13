package keisuke.count.diff;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

import keisuke.AbstractArgFunc;
import keisuke.count.SCCommonDefine;

public class DiffCountArgFunc extends AbstractArgFunc {

	public DiffCountArgFunc() {
		this.proctype = SCCommonDefine.DIFFCOUNTPROC;
		this.options = createDiffCountOptions();
		this.usageStr = proctype +  " <newDir> <oldDir>";
	}
	
	/** DiffCount引数設定 */
	protected Options createDiffCountOptions() {
        Options options = new Options();
        options.addOption("?", SCCommonDefine.OPT_HELP, false, "show help");
        options.addOption("e", SCCommonDefine.OPT_ENCODE, true, "encoding name");
        options.addOption("o", SCCommonDefine.OPT_OUTPUT, true, "file name");
        options.addOption("f", SCCommonDefine.OPT_FORMAT, true, 
        		"'" + SCCommonDefine.OPTVAL_TEXT+ "' | '" + SCCommonDefine.OPTVAL_HTML + "' | '"
        				+ SCCommonDefine.OPTVAL_EXCEL + "'");
        options.addOption("x", SCCommonDefine.OPT_XML, true, "xml file name");
        return options;
    }
	
	/** StepCountコマンドライン引数の解析 */
	@Override
	public Map<String, String> makeOwnArgMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		// 引数の解析
		if (this.commandline.hasOption(SCCommonDefine.OPT_ENCODE)) {
			String encoding = this.commandline.getOptionValue(SCCommonDefine.OPT_ENCODE);
			map.put(SCCommonDefine.OPT_ENCODE, encoding);
		}
		if (this.commandline.hasOption(SCCommonDefine.OPT_OUTPUT)) {
			String fname = this.commandline.getOptionValue(SCCommonDefine.OPT_OUTPUT);
			map.put(SCCommonDefine.OPT_OUTPUT, fname);
		}
		if (this.commandline.hasOption(SCCommonDefine.OPT_FORMAT)) {
			String format = this.commandline.getOptionValue(SCCommonDefine.OPT_FORMAT);
			map.put(SCCommonDefine.OPT_FORMAT, format);
		}
		if (this.commandline.hasOption(SCCommonDefine.OPT_XML)) {
			String fname = this.commandline.getOptionValue(SCCommonDefine.OPT_XML);
			map.put(SCCommonDefine.OPT_XML, fname);
		}
		
        return map;
	}
	
}
