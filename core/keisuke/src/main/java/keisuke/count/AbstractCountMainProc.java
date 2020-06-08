package keisuke.count;

import static keisuke.count.option.CountOptionConstant.OPT_FORMAT;
import static keisuke.count.option.CountOptionConstant.OPT_PATH;
import static keisuke.count.option.CountOptionConstant.OPT_SORT;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import keisuke.AbstractMainProc;

/**
 * Template class for main procedure of counting command.
 */
public abstract class AbstractCountMainProc extends AbstractMainProc {

	private OutputStream outputStream = System.out;
	private String srcEncoding = System.getProperty("file.encoding");
	private String xmlFileName = null;
	private String formatType = "";
	private FormatEnum formatEnum = null;
	private Formatter<?> formatter = null;
	private String pathStyleName = "";
	private PathStyleEnum pathStyleEnum = null;
	private String sortType = "";
	private SortOrderEnum sortOrderEnum = null;

	protected AbstractCountMainProc() {
		this.setDefaultFormat();
		this.setDefaultPathStyle();
		this.setDefaultSortOrder();
	}

	/**
	 * StepCount/DiffCountの主処理メソッド
	 * コマンドライン引数の処理、カウント処理、出力処理を呼び出す
	 * @param args 引数配列
	 */
	public void main(final String[] args) {
		// 引数処理
		// オプション解析
		this.setArgMap(this.commandOption().makeMapOfOptions(args));
		if (this.argMap() == null) {
			return;
		}
		// 引数で指定されたカウント対象を設定
		try {
			this.setFileArguments();
		} catch (IllegalArgumentException ex) {
			this.commandOption().showUsage();
			throw ex;
		}

		try {
			// 引数のオプション指定を設定
			this.setOptions();
			//カウント処理
			this.executeCounting();
			// 出力処理
			this.writeResults();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("Output file error.", fnfe);
		} catch (IOException ioe) {
			throw new RuntimeException("Count error.", ioe);
		} finally {
			if (this.outputStream != null && this.outputStream != System.out) {
				try {
					this.outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 出力形式オプションのデフォルト値を設定する
	 */
	protected abstract void setDefaultFormat();

	/**
	 * パス表記方法オプションのデフォルト値を設定する
	 */
	protected abstract void setDefaultPathStyle();

	/**
	 * ソート順オプションのデフォルト値を設定する
	 */
	protected abstract void setDefaultSortOrder();

	/**
	 * 計測対象のファイルまたはディレクトリを引数から設定する
	 * @throws IllegalArgumentException 引数指定に異常があると発行
	 */
	protected abstract void setFileArguments() throws IllegalArgumentException;

	/**
	 * 引数オプション解析Mapの設定内容からインスタンス変数の値を設定する
	 * @throws FileNotFoundException 出力先ファイルに異常があると発行
	 */
	protected abstract void setOptions() throws FileNotFoundException;

	/**
	 * 保持しているFile配列に対してカウントを実行します
	 * @throws IOException 出力時に異常があると発行
	 */
	protected abstract void executeCounting() throws IOException;

	/**
	 * カウント結果を指定された出力先へ出力フォーマットに変換して書き出す
	 * @throws IOException 出力時に異常があると発行
	 */
	protected abstract void writeResults() throws IOException;

	/**
	 * ソースファイルのエンコード名をセット
	 * @param encoding エンコード名
	 */
	public void setSourceEncoding(final String encoding) {
		this.srcEncoding = encoding;
	}

	/**
	 * 設定されたソースファイルのエンコード名を返す
	 * @return エンコード名
	 */
	protected String sourceEncoding() {
		return this.srcEncoding;
	}

	/**
	 * 言語定義XMLファイル名をセット
	 * @param name XMLファイル名
	 */
	public void setXmlFileName(final String name) {
		this.xmlFileName = name;
	}

	/**
	 * 設定された言語定義XMLファイル名を返す
	 * @return XMLファイル名
	 */
	protected String xmlFileName() {
		return this.xmlFileName;
	}

	/**
	 * 結果出力のフォーマットを設定します
	 * @param format 出力フォーマット
	 */
	public void setFormat(final String format) {
		if (format == null || format.isEmpty()) {
			return;
		}
		this.validateFormatOption(format);
		this.formatType = format;
		if (format.equals(FormatEnum.TEXT.value())) {
			this.formatEnum = FormatEnum.TEXT;
		} else if (format.equals(FormatEnum.CSV.value())) {
			this.formatEnum = FormatEnum.CSV;
		} else if (format.equals(FormatEnum.EXCEL.value())) {
			this.formatEnum = FormatEnum.EXCEL;
		//} else if (format.equals(FormatEnum.EXCEL97.value())) {
		//	this.formatEnum = FormatEnum.EXCEL97;
		} else if (format.equals(FormatEnum.HTML.value())) {
			this.formatEnum = FormatEnum.HTML;
		} else if (format.equals(FormatEnum.JSON.value())) {
			this.formatEnum = FormatEnum.JSON;
		} else if (format.equals(FormatEnum.XML.value())) {
			this.formatEnum = FormatEnum.XML;
		}
	}

	/**
	 * 結果出力のフォーマットを設定します
	 * @param format 出力フォーマット
	 */
	protected void setFormatEnum(final FormatEnum format) {
		this.formatEnum = format;
	}

	/**
	 * 結果出力のフォーマットを返す
	 * @return 出力フォーマット
	 */
	protected FormatEnum format() {
		return this.formatEnum;
	}

	/**
	 * 結果出力のフォーマット名称を返す
	 * @return 出力フォーマット名称
	 */
	protected String formatType() {
		return this.formatType;
	}

	/**
	 * フォーマットオプションの値としてチェックして不当な場合は例外を投げる
	 * @param format フォーマット名
	 * @throws IllegalArgumentException フォーマット名が不正の場合に発行
	 */
	protected void validateFormatOption(final String format) throws IllegalArgumentException {
		if (format == null || format.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_FORMAT).contains(format)) {
			throw new IllegalArgumentException(format + " is invalid format value.");
		}
	}

	/**
	 * 結果出力のパス表記方法を設定します
	 * @param style パス表記方法
	 */
	public void setPathStyle(final String style) {
		if (style == null || style.isEmpty()) {
			return;
		}
		if (style.equals(PathStyleEnum.SHOWDIR.value())) {
			this.pathStyleName = style;
			this.pathStyleEnum = PathStyleEnum.SHOWDIR;
			return;
		}
		this.validatePathOption(style);
		this.pathStyleName = style;
		if (style.equals(PathStyleEnum.BASE.value())) {
			this.pathStyleEnum = PathStyleEnum.BASE;
		} else if (style.equals(PathStyleEnum.SUB.value())) {
			this.pathStyleEnum = PathStyleEnum.SUB;
		} else if (style.equals(PathStyleEnum.NO.value())) {
			this.pathStyleEnum = PathStyleEnum.NO;
		}
	}

	/**
	 * パス表記方法の値を設定する
	 * @param style パス表記方法の値
	 */
	protected void setPathStyleEnum(final PathStyleEnum style) {
		this.pathStyleEnum = style;
	}

	/**
	 * 結果出力のパス表記方法を返す
	 * @return パス表記方法
	 */
	protected PathStyleEnum pathStyle() {
		return this.pathStyleEnum;
	}

	/**
	 * 結果出力のパス表記方法の名称を返す
	 * @return パス表記方法の名称
	 */
	protected String pathStyleName() {
		return this.pathStyleName;
	}

	/**
	 * 結果出力のバス表記にディレクトリを含むか真偽を返す
	 * @return ディレクトリを含む場合はtrue
	 */
	protected boolean isWithDirectory() {
		return this.pathStyleEnum.hasDirectory();
	}

	/**
	 * パス表記オプションの値としてチェックして不当な場合は例外を投げる
	 * @param path パス表記方法
	 * @throws IllegalArgumentException パス表記方法が不正の場合に発行
	 */
	protected void validatePathOption(final String path) throws IllegalArgumentException {
		if (path == null || path.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_PATH).contains(path)) {
			throw new IllegalArgumentException(path + " is invalid path value.");
		}
	}

	/**
	 * 結果出力のソート順をセットします
	 * @param order ソート順
	 */
	public void setSortOrder(final String order) {
		if (order == null || order.isEmpty()) {
			return;
		}
		this.validateSortOption(order);
		this.sortType = order;
		if (order.equals(SortOrderEnum.ON.value())) {
			this.sortOrderEnum = SortOrderEnum.ON;
		} else if (order.equals(SortOrderEnum.OS.value())) {
			this.sortOrderEnum = SortOrderEnum.OS;
		} else if (order.equals(SortOrderEnum.NODE.value())) {
			this.sortOrderEnum = SortOrderEnum.NODE;
		} else if (order.equals(SortOrderEnum.OFF.value())) {
			this.sortOrderEnum = SortOrderEnum.OFF;
		}
	}

	/**
	 * 結果出力のソート順の値をセットします
	 * @param order ソート順の値
	 */
	protected void setSortOrderEnum(final SortOrderEnum order) {
		this.sortOrderEnum = order;
	}

	/**
	 * 結果出力のソート順の値を返す
	 * @return ソート順の値
	 */
	protected SortOrderEnum sortOrder() {
		return this.sortOrderEnum;
	}

	/**
	 * 結果出力のソート順の名称を返す
	 * @return ソート順の名称
	 */
	protected String sortType() {
		return this.sortType;
	}

	/**
	 * ソートオプションの値としてチェックして不当な場合は例外を投げる
	 * @param sort ソート順
	 * @throws IllegalArgumentException ソート順が不正の場合に発行
	 */
	protected void validateSortOption(final String sort) throws IllegalArgumentException {
		if (sort == null || sort.isEmpty()) {
			return;
		}
		if (!this.commandOption().valuesAs(OPT_SORT).contains(sort)) {
			throw new IllegalArgumentException(sort + " is invalid sort value.");
		}
	}

	/**
	 * 出力ストリームを設定します
	 * @param output 出力ストリーム
	 */
	protected void setOutputStream(final OutputStream output) {
		this.outputStream = output;
	}

	/**
	 * 設定された出力ストリームを返す
	 * @return 出力ストリーム
	 */
	protected OutputStream outputStream() {
		return this.outputStream;
	}

	/**
	 * Formatterを設定します
	 * @param obj Formatterインスタンス
	 */
	protected void setFormatter(final Formatter<?> obj) {
		this.formatter = obj;
	}

	/**
	 * 設定されたFormatterインスタンスを返す
	 * @return Formatterインスタンス
	 */
	protected Formatter<?> formatter() {
		return this.formatter;
	}

	/**
	 * 設定されたフォーマットが返す出力がテキスト形式であるか真偽を返す.
	 * @return テキスト形式であれば	true
	 */
	public boolean isTextAsOutput() {
		if (this.formatter == null) {
			return false;
		}
		return this.formatter.isText();
	}

	/**
	 * 設定されたフォーマットがテキストの場合に文字エンコードを返す.
	 * テキスト形式でない場合はnullを返す
	 * @return エンコード名
	 */
	public String encodingAsOutput() {
		if (this.formatter == null) {
			return null;
		}
		return this.formatter.textEncoding();
	}

	/**
	 * オプションや計測対象ファイル名を設定後に、ステップ計測と結果出力を実行する
	 * 結果出力先がnull指定の場合は、結果出力は省略する
	 * @param filenames 計測対象ファイル名配列
	 * @param output 結果出力先
	 * @throws IOException ファイル入出力で異常があれば発行する
	 */
	public abstract void doCountingAndWriting(String[] filenames, OutputStream output)
			throws IOException;
}
