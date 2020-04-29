package keisuke.count.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
//import keisuke.util.LogUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;

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

		// fix ClassNotFound exception when jxls runs on Web Application Server.
		ClassLoader orgLoader = transformer.getConfiguration().getDigester().getClassLoader();
		//LogUtil.debugLog("digetster ClassLoader = " + orgLoader.getClass().getName());
		ClassLoader loader = transformer.getClass().getClassLoader();
		//LogUtil.debugLog("transformer ClassLoader = " + loader.getClass().getName());
		transformer.getConfiguration().getDigester().setClassLoader(loader);

		Workbook workbook = transformer.transformXLS(in, data);
		transformer.getConfiguration().getDigester().setClassLoader(orgLoader);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);

		return out.toByteArray();
	}
}
