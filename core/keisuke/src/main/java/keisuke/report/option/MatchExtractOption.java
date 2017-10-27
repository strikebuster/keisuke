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
 * Class to deal command arguments for MatchProc.
 */
public class MatchExtractOption extends AbstractCommandOption {

	protected MatchExtractOption() {
		super();
		this.setProcCommandName(ProcedureType.MATCH_PROC.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <mafile> <trfile>  [<outfile>]");
	}

	// MatchProcコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", "help", false, "show help");
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
		String[] argArray = this.commandline().getArgs();
		if (argArray.length > 1) {
			map.put(ARG_MASTER, argArray[0]);
			map.put(ARG_TRANSACTION, argArray[1]);
			if (argArray.length > 2) {
				map.put(ARG_OUTPUT, argArray[2]);
			}
		} else {
			// 引数が不足
			this.showUsage();
			return null;
		}
		return new ArgumentMapImpl(map);
	}
}
