package keisuke.count.step.format;

import keisuke.StepCountResult;
import keisuke.count.FormatEnum;
import keisuke.count.Formatter;

/**
 * フォーマッタのインスタンスを作成するためのファクトリクラス。
 * 出力定形文言をpropertiesから設定して出力形式に応じたフォーマッタ
 * を作成する
 */
public final class FormatterFactory {

	private FormatterFactory() { }

	/**
	 * フォーマッタのインスタンスを生成します。
	 *
	 * @param format フォーマット
	 * @return フォーマッタのインスタンス
	 */
	public static Formatter<StepCountResult[]> getFormatter(final FormatEnum format) {
		// nullの場合はデフォルトフォーマット
		if (format == null) {
			return new TextFormatter();
		}
		// TEXTフォーマット
		if (format.equals(FormatEnum.TEXT)) {
			return new TextFormatter();

		// CSVフォーマット
		} else if (format.equals(FormatEnum.CSV)) {
			return new CSVFormatter();

		// XMLフォーマット
		} else if (format.equals(FormatEnum.XML)) {
			return new XmlFormatter();

		// JSONフォーマット
		} else if (format.equals(FormatEnum.JSON)) {
			return new JsonFormatter();

		// Excelフォーマット
		} else if (format.equals(FormatEnum.EXCEL)) {
			return new ExcelFormatter(FormatEnum.EXCEL);

		// Excel97フォーマット
		//} else if (format.equals(FormatEnum.EXCEL97)) {
		//	return new ExcelFormatter(FormatEnum.EXCEL97);

		// デフォルトフォーマット
		} else {
			return null;
		}
	}

	/**
	 * フォーマッタのインスタンスを生成します。
	 *
	 * @param format フォーマット
	 * @return フォーマッタのインスタンス
	 */
	@Deprecated
	public static Formatter<StepCountResult[]> getFormatter(final String format) {
		// nullの場合はデフォルトフォーマット
		if (format == null || format.isEmpty()) {
			return new TextFormatter();
		}
		String name = format.toLowerCase();
		// TEXTフォーマット
		if (name.equals(FormatEnum.TEXT.value())) {
			return new TextFormatter();

		// CSVフォーマット
		} else if (name.equals(FormatEnum.CSV.value())) {
			return new CSVFormatter();

		// XMLフォーマット
		} else if (name.equals(FormatEnum.XML.value())) {
			return new XmlFormatter();

		// JSONフォーマット
		} else if (name.equals(FormatEnum.JSON.value())) {
			return new JsonFormatter();

		// Excelフォーマット
		} else if (name.equals(FormatEnum.EXCEL.value())) {
			return new ExcelFormatter(FormatEnum.EXCEL);

		// Excel97フォーマット
		//} else if (name.equals(FormatEnum.EXCEL97.value())) {
		//	return new ExcelFormatter(FormatEnum.EXCEL97);

		// デフォルトフォーマット
		} else {
			return null;
		}
	}

}
