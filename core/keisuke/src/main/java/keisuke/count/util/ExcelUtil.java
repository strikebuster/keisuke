package keisuke.count.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import keisuke.util.LogUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excelに関するユーティリティメソッドを提供します。
 */
public class ExcelUtil {

	public ExcelUtil() { }

	/**
	 * jXLSを使用してExcelファイルを生成します。
	 * 引数で与えたテンプレートの入力ストリームはこのメソッド内でクローズされます。
	 * 生成したEXCELファイルの内容をバイト配列にして返す。
	 * @param in EXCELテンプレートの入力ストリーム
	 * @param data テンプレートに差し込むデータ
	 * @return 作成したEXCELファイルのバイト配列
	 * @throws Exception 外部ライブラリやStreamの処理で異常があった際に発行
	 */
	public byte[] makeExcelData(final InputStream in, final Map<String, Object> data)
			throws Exception {

		// net.sf.jxls 1.0.6
		XLSTransformer transformer = new XLSTransformer();

		// fix ClassNotFound exception when jxls runs on Web Application Server.
		//this.debugClassLoader();
		ClassLoader orgLoader = transformer.getConfiguration().getDigester().getClassLoader();
		//LogUtil.debugLog("digetster = "
		//		+ transformer.getConfiguration().getDigester().getClass().getName()
		//		+ " ClassLoader :");
		//this.debugClassLoader(orgLoader);
		ClassLoader loader = transformer.getClass().getClassLoader();
		//LogUtil.debugLog("transformer = " + transformer.getClass().getName()
		//		+ " ClassLoader :");
		//this.debugClassLoader(loader);
		transformer.getConfiguration().getDigester().setClassLoader(loader);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Workbook workbook = transformer.transformXLS(in, data);
			workbook.write(out);
		} catch (Exception ex) {
			LogUtil.errorLog("jxls or poi got an error.\n" + ex.toString());
			throw ex;
		} finally {
			transformer.getConfiguration().getDigester().setClassLoader(orgLoader);
			out.close();
		}
		return out.toByteArray();
	}

	@SuppressWarnings("unused")
	private void debugClassLoader() {
		LogUtil.debugLog(this.getClass().getName() + " ClassLoader :");
		debugClassLoader(this.getClass().getClassLoader());
	}

	private static void debugClassLoader(final ClassLoader classLoader) {
		ClassLoader loader = classLoader;
		int i = 0;
		while (loader != null) {
			LogUtil.debugLog("ClassLoader " + i + "th ancestor: " + loader.getClass().getName());
			loader = loader.getParent();
			i++;
		}
	}
}
