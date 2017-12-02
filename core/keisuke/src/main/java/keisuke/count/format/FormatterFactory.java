package keisuke.count.format;

import keisuke.count.Formatter;
import static keisuke.count.option.CountOptionConstant.*;

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
	public static Formatter getFormatter(final String format) {
		// nullの場合はデフォルトフォーマット
		if (format == null || format.isEmpty()) {
			return new TextFormatter();
		}
		String name = format.toLowerCase();
		// TEXTフォーマット
		if (name.equals(OPTVAL_TEXT)) {
			return new TextFormatter();

		// CSVフォーマット
		} else if (name.equals(OPTVAL_CSV)) {
			return new CSVFormatter();

		// XMLフォーマット
		} else if (name.equals(OPTVAL_XML)) {
			return new XmlFormatter();

		// JSONフォーマット
		} else if (name.equals(OPTVAL_JSON)) {
			return new JsonFormatter();

		// Excelフォーマット
		} else if (name.equals(OPTVAL_EXCEL)) {
			return new ExcelFormatter();

		// デフォルトフォーマット
		} else {
			return null;
		}
	}

}
