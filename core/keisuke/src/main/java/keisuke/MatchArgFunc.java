package keisuke;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Options;

/**
 * Class to deal command arguments for MatchProc.
 */
class MatchArgFunc extends AbstractArgFunc {
	protected static final String ARG_MASTER = "mafile";
	protected static final String ARG_TRANSACTION = "trfile";
	protected static final String ARG_OUTPUT = "outfile";

	protected MatchArgFunc() {
		super();
		this.setProcType(CommonDefine.MATCHPROC);
		this.setOptions(this.createMatchOptions());
		this.setUsage(CommonDefine.MATCHPROC +  " <mafile> <trfile>  [<outfile>]");
	}


	// MatchProc引数設定
	private Options createMatchOptions() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
        return options;
    }

	// MatchProcコマンドライン引数の解析
	/** {@inheritDoc} */
	@Override
	public Map<String, String> makeOwnArgMap() {
		// 引数のデフォルト設定
		Map<String, String> map = new HashMap<String, String>();
		map.put(ARG_MASTER, null);
		map.put(ARG_TRANSACTION, null);
		map.put(ARG_OUTPUT, null);
		// 引数の解析
		String[] argArray = this.commandline().getArgs();
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
