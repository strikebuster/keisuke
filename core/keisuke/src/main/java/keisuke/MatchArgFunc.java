package keisuke;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

public class MatchArgFunc extends AbstractArgFunc {
	public final static String ARG_MASTER = "mafile";
	public final static String ARG_TRANSACTION = "trfile";
	public final static String ARG_OUTPUT = "outfile";
	
	public MatchArgFunc() {
		this.proctype = CommonDefine.MATCHPROC;
		this.options = createMatchOptions();
		this.usageStr = proctype +  " <mafile> <trfile>  [<outfile>]";
	}
	

	/** MatchProc引数設定 */
	private Options createMatchOptions() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        return options;
    }
	
	/** MatchProcコマンドライン引数の解析 */
	@Override
	public Map<String, String> makeOwnArgMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_MASTER, null);
		map.put(ARG_TRANSACTION, null);
		map.put(ARG_OUTPUT, null);
		// 引数の解析
		String[] argArray = this.commandline.getArgs();
		if (argArray.length > 1) {         	
			map.put(ARG_MASTER, argArray[0]);
			map.put(ARG_TRANSACTION, argArray[1]);
			if (argArray.length > 2) {
				map.put(ARG_OUTPUT, argArray[2]);
			}
		} else {
			// 引数が不足
			showUsage();
			return null;
		}
        return map;
	}
}
