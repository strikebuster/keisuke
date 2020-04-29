package keisuke.count.option;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.Options;

import keisuke.ArgumentMap;
import keisuke.OptionValues;
import keisuke.count.CountType;
import keisuke.count.FormatEnum;
import keisuke.count.PathStyleEnum;
import keisuke.count.SortOrderEnum;
import keisuke.option.AbstractCommandOption;
import keisuke.option.ArgumentMapImpl;

import static keisuke.count.option.CountOptionConstant.*;

/**
 * Class to deal command arguments for DiffCount.
 */
public class DiffCountOption extends AbstractCommandOption {

	public static final FormatEnum[] DIFFCOUNT_FORMATS = {
			FormatEnum.TEXT,
			FormatEnum.CSV,
			FormatEnum.EXCEL,
			FormatEnum.XML,
			FormatEnum.JSON,
			FormatEnum.HTML
	};
	public static final PathStyleEnum[] DIFFCOUNT_PATHSTYLES = {
			PathStyleEnum.BASE,
			PathStyleEnum.SUB,
			PathStyleEnum.NO
	};
	public static final SortOrderEnum[] DIFFCOUNT_SORTORDERS = {
			SortOrderEnum.NODE, // default for compatible with ver.1.x wehn TEXT,XML,HTML format
			SortOrderEnum.OS, // default for compatible with ver.1.x when CSV,JSON,EXCEL format
			SortOrderEnum.ON
	};
	private static OptionValues format = new OptionValues();
	private static OptionValues path = new OptionValues();
	private static OptionValues sort = new OptionValues();
	static {
		for (FormatEnum obj : DIFFCOUNT_FORMATS) {
			format.add(obj.value());
		}
		for (PathStyleEnum obj : DIFFCOUNT_PATHSTYLES) {
			path.add(obj.value());
		}
		for (SortOrderEnum obj : DIFFCOUNT_SORTORDERS) {
			sort.add(obj.value());
		}
	}

	public DiffCountOption() {
		super();
		this.setProcCommandName(CountType.DIFF_COUNT.toString());
		this.configureOption();
		this.setUsage(this.procCommand() +  " <newDir> <oldDir>");
	}

	/** {@inheritDoc} */
	public OptionValues valuesAs(final String optname) {
		if (optname == null || optname.isEmpty()) {
			return null;
		} else if (optname.equals(OPT_FORMAT)) {
			return format;
		} else if (optname.equals(OPT_PATH)) {
			return path;
		} else if (optname.equals(OPT_SORT)) {
			return sort;
		} else {
			return null;
		}
	}

	// DiffCountコマンドライン引数の設定
	/** {@inheritDoc} */
	@Override
	protected void configureOption() {
        Options options = new Options();
        options.addOption("?", OPT_HELP, false, "show help");
        options.addOption("e", OPT_ENCODE, true, "encoding name");
        options.addOption("o", OPT_OUTPUT, true, "file name");
        options.addOption("f", OPT_FORMAT, true, "output format " + format.printList());
        options.addOption("p", OPT_PATH, true, "path style " + path.printList());
        options.addOption("S", OPT_SORT, true, "sorting order " + sort.printList());
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
			map.put(OPT_ENCODE, this.commandline().getOptionValue(OPT_ENCODE));
		}
		if (this.commandline().hasOption(OPT_OUTPUT)) {
			map.put(OPT_OUTPUT, this.commandline().getOptionValue(OPT_OUTPUT));
		}
		if (this.commandline().hasOption(OPT_FORMAT)) {
			map.put(OPT_FORMAT, this.commandline().getOptionValue(OPT_FORMAT));
		}
		if (this.commandline().hasOption(OPT_PATH)) {
			map.put(OPT_PATH, this.commandline().getOptionValue(OPT_PATH));
		}
		if (this.commandline().hasOption(OPT_SORT)) {
			map.put(OPT_SORT, this.commandline().getOptionValue(OPT_SORT));
		}
		if (this.commandline().hasOption(OPT_XML)) {
			map.put(OPT_XML, this.commandline().getOptionValue(OPT_XML));
		}
		// 引数の解析
		String[] argArray = this.makeRestArgArray();
		if (argArray.length > 0) {
			map.put(ARG_NEWDIR, argArray[0]);
		}
		if (argArray.length > 1) {
			map.put(ARG_OLDDIR, argArray[1]);
		}
		return new ArgumentMapImpl(map);
	}

}
