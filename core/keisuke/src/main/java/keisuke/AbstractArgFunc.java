package keisuke;

import java.util.Map;

import org.apache.commons.cli.*;

public abstract class AbstractArgFunc implements IfArgFunc {

	protected String proctype = "";
	protected Options options = null;
	protected CommandLineParser parser = null;
	protected String usageStr = "";
	protected CommandLine commandline = null;
	
	protected AbstractArgFunc() {
		this.parser = new DefaultParser();
	}
	
	/** コマンドライン引数の解析 */
	public Map<String, String> makeMapOfArgs(String[] args) {
		Map<String, String> map = null;
		try {
			this.commandline = this.parser.parse(this.options, args);
			if (this.commandline.hasOption("?")) {
				showUsage();
				return null;
			}
			map = makeOwnArgMap();
		} catch (ParseException e) {
			showUsage();
			return null;
		}
		return map;
	}
	
	/** Proc毎のコマンドライン引数解析結果Map作成 */
	public abstract Map<String, String> makeOwnArgMap();

	/** オプション引数解析後の残りの引数の文字列配列を作成 */
	public String[] makeRestArgArray() {
		if (this.commandline == null) {
			return null;
		}
		return this.commandline.getArgs();
	}
	
	/** 使用方法メッセージ */
    public void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(this.usageStr, this.options, true);
    }

}
