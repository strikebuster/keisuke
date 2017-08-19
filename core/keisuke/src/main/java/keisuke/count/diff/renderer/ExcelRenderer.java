package keisuke.count.diff.renderer;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import keisuke.count.diff.DiffCounterUtil;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.format.ExcelFormatter;

/**
 * 差分カウントの結果をEXCEL形式でレンダリングします。
 */
public class ExcelRenderer extends AbstractRenderer {

	private static String xlsPrefix = "DiffExcelFormat";

	/** {@inheritDoc} */
	public byte[] render(final DiffFolderResult result) {
		try {
			String xlsTemplate = xlsPrefix + ExcelFormatter.getLocalePostfix() + ".xls";
			//System.out.println("[DEBUG] xlsTemplate = " + xlsTemplate);
			URL url = this.getClass().getResource(xlsTemplate);
			if (url == null) {
				xlsTemplate = xlsPrefix + ".xls";
			}
			//System.out.println("[DEBUG] xlsTemplate = " + xlsTemplate);
			InputStream in = ExcelRenderer.class.getResourceAsStream(xlsTemplate);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("results", DiffCounterUtil.convertToList(result));
			data.put("totalAdd", result.getAddCount());
			data.put("totalDel", result.getDelCount());

			return ExcelFormatter.makeExcelData(in, data);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/*
	 * jXLSを使用してExcelファイルを生成します。
	 * 引数で与えたテンプレートの入力ストリームはこのメソッド内でクローズされます。
	 * <p>
	 * TODO {@link ExcelFormatter}と共通化する
	 *
	private static byte[] merge(InputStream in, Map<String, Object> data) throws Exception {
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = transformer.transformXLS(in, data);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);

		return out.toByteArray();
	}
	*/

}
