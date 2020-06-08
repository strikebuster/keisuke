package keisuke.count.diff.renderer;

import keisuke.count.Formatter;
import keisuke.count.PathStyleEnum;
import keisuke.count.SortOrderEnum;
import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;

import keisuke.count.FormatEnum;

/**
 * 出力形式に応じたFormatterの実装クラスを生成するファクトリ
 */
public final class RendererFactory {

	private RendererFactory() { }

	/**
	 * 出力形式に応じたFormatterの実装インスタンスを返す
	 * @param format 出力形式
	 * @return 差分計測結果の出力形式に応じたFormatterインスタンス
	 */
	public static Formatter<DiffFolderResult> getFormatter(final FormatEnum format) {
		AbstractRenderer renderer = null;
		if (format == null) {
			renderer = new TextRenderer();
			renderer.setSortOrder(SortOrderEnum.NODE);
			return renderer;
		}
		if (format.equals(FormatEnum.TEXT)) {
			renderer = new TextRenderer();
			renderer.setSortOrder(SortOrderEnum.NODE);
		} else if (format.equals(FormatEnum.CSV)) {
			renderer = new CsvRenderer();
			renderer.setSortOrder(SortOrderEnum.OS);
		} else if (format.equals(FormatEnum.EXCEL)) {
			renderer = new ExcelRenderer(FormatEnum.EXCEL);
			renderer.setSortOrder(SortOrderEnum.OS);
		//} else if (format.equals(FormatEnum.EXCEL97)) {
		//	renderer = new ExcelRenderer(FormatEnum.EXCEL97);
		//	renderer.setSortOrder(SortOrderEnum.OS);
		} else if (format.equals(FormatEnum.JSON)) {
			renderer = new JsonRenderer();
			renderer.setSortOrder(SortOrderEnum.OS);
		} else if (format.equals(FormatEnum.XML)) {
			renderer = new XmlRenderer();
			renderer.setSortOrder(SortOrderEnum.NODE);
		} else if (format.equals(FormatEnum.HTML)) {
			renderer = new HtmlRenderer();
			renderer.setSortOrder(SortOrderEnum.NODE);
		}
		return renderer;
	}

	/**
	 * 出力形式に応じたFormatterの実装インスタンスを返す
	 * @param name 出力形式名称
	 * @return 差分計測結果の出力形式に応じたFormatterインスタンス
	 */
	@Deprecated
	public static Formatter<DiffFolderResult> getFormatter(final String name) {
		AbstractRenderer renderer = null;
		if (name != null && !name.isEmpty()) {
			if (name.equals(FormatEnum.TEXT.value())) {
				renderer = new TextRenderer();
				renderer.setSortOrder(SortOrderEnum.NODE);
			} else if (name.equals(FormatEnum.HTML.value())) {
				renderer = new HtmlRenderer();
				renderer.setSortOrder(SortOrderEnum.NODE);
			} else if (name.equals(FormatEnum.EXCEL.value())) {
				renderer = new ExcelRenderer(FormatEnum.EXCEL);
				renderer.setSortOrder(SortOrderEnum.OS);
			//} else if (name.equals(FormatEnum.EXCEL97.value())) {
			//	renderer = new ExcelRenderer(FormatEnum.EXCEL97);
			//	renderer.setSortOrder(SortOrderEnum.OS);
			} else if (name.equals(FormatEnum.CSV.value())) {
				renderer = new CsvRenderer();
				renderer.setSortOrder(SortOrderEnum.OS);
			} else if (name.equals(FormatEnum.JSON.value())) {
				renderer = new JsonRenderer();
				renderer.setSortOrder(SortOrderEnum.OS);
			} else if (name.equals(FormatEnum.XML.value())) {
				renderer = new XmlRenderer();
				renderer.setSortOrder(SortOrderEnum.NODE);
			}
		} else {
			renderer = new TextRenderer();
			renderer.setSortOrder(SortOrderEnum.NODE);
		}
		return renderer;
	}

	/**
	 * 出力形式に応じたFormatterの実装インスタンスを返す
	 * @param format 出力形式
	 * @param msgdef メッセージ定義インスタンス
	 * @return 差分計測結果の出力形式に応じたFormatterインスタンス
	 */
	public static Formatter<DiffFolderResult> getFormatter(final FormatEnum format, final MessageDefine msgdef) {
		AbstractRenderer renderer = (AbstractRenderer) getFormatter(format);
		if (renderer != null && msgdef != null) {
			renderer.setMessageMap(msgdef);
		}
		return renderer;
	}

	/**
	 * 出力形式に応じたFormatterの実装インスタンスを返す
	 * @param format 出力形式
	 * @param msgdef メッセージ定義インスタンス
	 * @param style パス表記スタイル
	 * @param order ソート順
	 * @return 差分計測結果の出力形式に応じたFormatterインスタンス
	 */
	public static Formatter<DiffFolderResult> getFormatter(final FormatEnum format, final MessageDefine msgdef,
			final PathStyleEnum style, final SortOrderEnum order) {
		AbstractRenderer renderer = (AbstractRenderer) getFormatter(format, msgdef);
		if (renderer != null && style != null) {
			renderer.setPathStyle(style);
		}
		if (renderer != null && order != null) {
			renderer.setSortOrder(order);
		}
		return renderer;
	}
}
