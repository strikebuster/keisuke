package keisuke.count.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.sf.amateras.stepcounter.CategoryStepDto;
import jp.sf.amateras.stepcounter.CountResult;
import keisuke.count.SCCommonDefine;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * ステップ計測結果をExcel形式にフォーマットします。
 */
public class ExcelFormatter extends AbstractFormatter {

	private static final String XLS_PREFIX = "ExcelFormatter";

	/** {@inheritDoc} */
	public byte[] format(final CountResult[] results) {
		String xlsTemplate = XLS_PREFIX + getLocalePostfix() + ".xls";
		//System.out.println("[DEBUG] xlsTemplate = " + xlsTemplate);
		URL url = this.getClass().getResource(xlsTemplate);
		if (url == null) {
			xlsTemplate = XLS_PREFIX + ".xls";
		}
		//System.out.println("[DEBUG] xlsTemplate = " + xlsTemplate);
		InputStream in = null;
		try {
			in = this.getClass().getResourceAsStream(xlsTemplate);

			List<CategoryStepDto> categories = new ArrayList<CategoryStepDto>();
			CategoryStepDto nonCategory = new CategoryStepDto();
			nonCategory.setCategory("");
			boolean useNonCategory = false;
			for (CountResult result : results) {
				CategoryStepDto categoryDto = null;
				if (result.getCategory() == null || "".equals(result.getCategory())) {
					categoryDto = nonCategory;
					useNonCategory = true;
				} else {
					categoryDto = getCategoryDto(categories, result.getCategory());
				}
				categoryDto.setStep(categoryDto.getStep() + result.getStep());
				categoryDto.setNone(categoryDto.getNone() + result.getNon());
				categoryDto.setComment(categoryDto.getComment() + result.getComment());
			}
			if (useNonCategory) {
				categories.add(nonCategory);
			}

			Collections.sort(categories, new Comparator<CategoryStepDto>() {
				public int compare(final CategoryStepDto o1, final CategoryStepDto o2) {
					if (o1.getCategory().length() == 0
							&& o2.getCategory().length() == 0) {
						return 0;
					}
					if (o1.getCategory().length() == 0) {
						return 1;
					}
					if (o2.getCategory().length() == 0) {
						return -1;
					}
					return o1.getCategory().compareTo(o2.getCategory());
				}
			});

			// カテゴリ・ファイルタイプが無指定の場合はnullから空文字に修正する。(fishplate対応)
			for (CountResult result : results) {
				if (result.getCategory() == null) {
					result.setCategory("");
				}
				if (result.getFileType() == null) {
					result.setFileType(getMessageText(SCCommonDefine.MSG_COUNT_FMT_UNDEF));
				}
			}

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("results", results);
			data.put("categories", categories);

			return makeExcelData(in, data);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

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

	private static CategoryStepDto getCategoryDto(
			final List<CategoryStepDto> categoryResults, final String category) {
		for (CategoryStepDto categoryDto : categoryResults) {
			if (categoryDto.getCategory().equals(category)) {
				return categoryDto;
			}
		}

		CategoryStepDto categoryDto = new CategoryStepDto();
		categoryDto.setCategory(category);
		categoryResults.add(categoryDto);

		return categoryDto;
	}

	/**
	 * ロケールに対応するリソースファイル名用の接尾語を返す
	 * "_"とロケール言語　例）"_ja"
	 * @return ロケール言語接尾語
	 */
	public static String getLocalePostfix() {
		Locale locale = Locale.getDefault();
		return "_" + locale.getLanguage();
	}

}
