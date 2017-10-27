package keisuke.count.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * Excelに関するユーティリティメソッドを提供します。
 */
public final class ExcelUtil {

	private ExcelUtil() { }

	/**
	 * jXLSを使用してExcelファイルを生成します。
	 * 引数で与えたテンプレートの入力ストリームはこのメソッド内でクローズされます。
	 * 生成したEXCELファイルの内容をバイト配列にして返す。
	 * @param in EXCELテンプレートの入力ストリーム
	 * @param data テンプレートに差し込むデータ
	 * @return 作成したEXCELファイルのバイト配列
	 * @throws Exception 外部ライブラリやStreamの処理で異常があった際に発行
	 */
	public static byte[] makeExcelData(final InputStream in, final Map<String, Object> data) throws Exception {
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = transformer.transformXLS(in, data);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);

		return out.toByteArray();
	}
}
