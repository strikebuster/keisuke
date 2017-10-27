package keisuke.option;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import keisuke.ArgumentMap;
import keisuke.CommandOption;

/**
 * base class to deal command options.
 */
public abstract class AbstractCommandOption implements CommandOption {

	private String procname = "";
	private Options options = null;
	private String usageStr = "";
	private CommandLine commandline = null;
	private CommandLineParser parser = null;

	protected AbstractCommandOption() {
		this.parser = new DefaultParser();
	}

	/**
	 * Set process type.
	 * @param type command type.
	 */
	protected void setProcCommandName(final String type) {
		if (type == null) return;
		this.procname = type;
	}

	/**
	 * Get command name
	 * @return command name
	 */
	protected String procCommand() {
		return this.procname;
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
	 * @param msg command usage message.
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
	public ArgumentMap makeMapOfOptions(final String[] args) {
		ArgumentMap map = null;
		try {
			this.commandline = this.parser.parse(this.options, args);
			if (this.commandline.hasOption("?")) {
				this.showUsage();
				return null;
			}
			map = makeOwnOptionMap();
		} catch (ParseException e) {
			this.showUsage();
			return null;
		}
		return map;
	}

	/**
	 * Procedure種類ごとのコマンドライン引数オプション定義を構成する
	 */
	protected abstract void configureOption();

	/**
	 * Procedure種類毎のコマンドライン引数解析結果オプションMap作成
	 * @return Map<オプション名, オプション値>
	 */
	protected abstract ArgumentMap makeOwnOptionMap();

	/** {@inheritDoc} */
	public String[] makeRestArgArray() {
		if (this.commandline == null) {
			return null;
		}
		return this.commandline.getArgs();
	}

	/** {@inheritDoc} */
    public void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(this.usageStr, this.options, true);
    }

}
