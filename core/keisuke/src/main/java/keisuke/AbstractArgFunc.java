package keisuke;

import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Abstract class to deal command options.
 * @author strikebuster
 *
 */
public abstract class AbstractArgFunc implements IfArgFunc {

	private String proctype = "";
	private Options options = null;
	private String usageStr = "";
	private CommandLine commandline = null;
	private CommandLineParser parser = null;

	protected AbstractArgFunc() {
		this.parser = new DefaultParser();
	}

	/**
	 * Set process type.
	 * @param type command type.
	 */
	protected void setProcType(final String type) {
		if (type == null) return;
		this.proctype = type;
	}

	/**
	 * Set option definitions.
	 * @param opt command option definition.
	 */
	protected void setOptions(final Options opt) {
		if (opt == null) return;
		this.options = opt;
	}

	/**
	 * Set usage message.
	 * @param msg command usage messge.
	 */
	protected void setUsage(final String msg) {
		if (msg == null) return;
		this.usageStr = msg;
	}

	/**
	 * Get commandline
	 * @return org.apache.commons.cli.CommandLine
	 */
	protected CommandLine commandline() {
		return this.commandline;
	}

	/** {@inheritDoc} */
	public Map<String, String> makeMapOfArgs(final String[] args) {
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

	/**
	 * Proc毎のコマンドライン引数解析結果オプションMap作成
	 * @return Map<String, String> Map<オプション名, オプション値>
	 */
	protected abstract Map<String, String> makeOwnArgMap();

	/**
	 * オプション引数解析後の残りの引数の文字列配列を作成
	 * @return String[] オプション解析後の残った引数の配列
	 */
	public String[] makeRestArgArray() {
		if (this.commandline == null) {
			return null;
		}
		return this.commandline.getArgs();
	}

	/**
	 * 使用方法メッセージ
	 */
    public void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(this.usageStr, this.options, true);
    }

}
