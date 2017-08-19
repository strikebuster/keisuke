package keisuke.count.format;

import jp.sf.amateras.stepcounter.format.JSONFormatter;
import jp.sf.amateras.stepcounter.format.ResultFormatter;
import jp.sf.amateras.stepcounter.format.XMLFormatter;

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
	public static ResultFormatter getFormatter(final String format) {
		// nullの場合はデフォルトフォーマット
		if (format == null) {
			return new DefaultFormatter();
		}
		String name = format.toLowerCase();
		// CSVフォーマット
		if (name.equals("csv")) {
			return new CSVFormatter();

		// XMLフォーマット
		} else if (name.equals("xml")) {
			return new XMLFormatter();

		// JSONフォーマット
		} else if (name.equals("json")) {
			return new JSONFormatter();

		// Excelフォーマット
		} else if (name.equals("excel")) {
			return new ExcelFormatter();

		// デフォルトフォーマット
		} else {
			return new DefaultFormatter();
		}
	}

}
