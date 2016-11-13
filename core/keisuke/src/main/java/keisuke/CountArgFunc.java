package keisuke;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

public class CountArgFunc extends AbstractArgFunc {
	public final static String ARG_INPUT = "infile";
	
	public CountArgFunc() {
		this.proctype = CommonDefine.COUNTPROC;
		this.options = createCountOptions();
		this.usageStr = proctype +  " <infile>";
	}
	
	/** CountProc引数設定 */
	protected Options createCountOptions() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        options.addOption("c", CommonDefine.OPT_CLASS, true, "type to classify '"
        		+ CommonDefine.OPTVAL_EXTENSION + "' | '" + CommonDefine.OPTVAL_LANGUAGE + "' | '"
        		+ CommonDefine.OPTVAL_LANGGROUP + "' | '" + CommonDefine.OPTVAL_FW + "'<name>");
        options.addOption("p", CommonDefine.OPT_PROP, true, "properties file name");
        options.addOption("x", CommonDefine.OPT_XML, true, "xml file name");
        return options;
    }
	
	/** CountProcコマンドライン引数の解析 */
	@Override
	public Map<String, String> makeOwnArgMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_INPUT, null);
		// 引数の解析
		if (this.commandline.hasOption(CommonDefine.OPT_PROP)) {
			String fname = this.commandline.getOptionValue(CommonDefine.OPT_PROP);
			map.put(CommonDefine.OPT_PROP, fname);
		}
		if (this.commandline.hasOption(CommonDefine.OPT_XML)) {
			String fname = this.commandline.getOptionValue(CommonDefine.OPT_XML);
			map.put(CommonDefine.OPT_XML, fname);
		}
		if (this.commandline.hasOption(CommonDefine.OPT_CLASS)) {
			String operand = this.commandline.getOptionValue(CommonDefine.OPT_CLASS);
			map.put(CommonDefine.OPT_CLASS, operand);
		}
		String[] argArray = this.commandline.getArgs();
		if (argArray.length > 0) {
			map.put(ARG_INPUT, argArray[0]);
		}
        return map;
	}
}
