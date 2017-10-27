package keisuke.count.format;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import keisuke.StepCountResult;
import keisuke.count.util.ExcelUtil;
import keisuke.count.util.LocaleUtil;
import keisuke.util.LogUtil;

import static keisuke.count.format.FormatConstant.*;

/**
 * ステップ計測結果をExcel形式にフォーマットします。
 */
public class ExcelFormatter extends AbstractFormatter {

	private static final String XLS_PREFIX = "ExcelFormatter";
	private static final String XLS_EXTENSION = ".xls";
	private static final String XLS_DATA_RESULT = "results";
	private static final String XLS_DATA_CATEGORY = "categories";

	/** {@inheritDoc} */
	public byte[] format(final StepCountResult[] results) {
		String xlsTemplate = XLS_PREFIX + LocaleUtil.getLocalePostfix() + XLS_EXTENSION;
		//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
		URL url = this.getClass().getResource(xlsTemplate);
		if (url == null) {
			xlsTemplate = XLS_PREFIX + XLS_EXTENSION;
		}
		//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
		InputStream in = null;
		try {
			in = this.getClass().getResourceAsStream(xlsTemplate);
			CountResultCompatible[] dtoArray = this.createResultDtoFrom(results);
			CategoryStepDtoCompatible[] categories = this.createCategoryDtoFrom(results);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(XLS_DATA_RESULT, dtoArray);
			data.put(XLS_DATA_CATEGORY, categories);

			return ExcelUtil.makeExcelData(in, data);

		} catch (Exception ex) {
			LogUtil.errorLog("fail to make excel data");
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

	// StepCountResultの配列をStepCountResultDtoの配列に変換する
	private StepCountResultDto[] createResultDtoFrom(final StepCountResult[] results) {
		StepCountResultDto[] dtoArray = new StepCountResultDto[results.length];
		for (int i = 0; i < results.length; i++) {
			StepCountResult result = results[i];
			if (result.sourceCategory() == null) {
				// カテゴリ・ファイルタイプが無指定の場合はnullから空文字に修正する。(fishplate対応)
				result.setSourceCategory("");
			}
			if (result.sourceType() == null) {
				result.setSourceType(this.getMessageText(MSG_COUNT_FMT_UNDEF));
			}
			dtoArray[i] = new StepCountResultDto(result);
		}
		return dtoArray;
	}

	// StepCountResultの配列からカテゴリ毎に集計したStepCountResultCategoryDtoの配列を作成する
	private StepCountResultCategoryDto[] createCategoryDtoFrom(final StepCountResult[] results) {
		List<StepCountResultCategoryDto> categories = new ArrayList<StepCountResultCategoryDto>();
		// 未指定のインスタンスを準備
		StepCountResultCategoryDto nonCategory = new StepCountResultCategoryDto();
		nonCategory.setCategory("");
		// 未指定の結果存在フラグ
		boolean useNonCategory = false;
		for (StepCountResult result : results) {
			StepCountResultCategoryDto categoryDto = null;
			if (result.sourceCategory() == null || result.sourceCategory().isEmpty()) {
				categoryDto = nonCategory;
				useNonCategory = true;
			} else {
				for (StepCountResultCategoryDto dto : categories) {
					if (dto.getCategory().equals(result.sourceCategory())) {
						categoryDto = dto;
						break;
					}
				}
				if (categoryDto == null) {
					categoryDto = new StepCountResultCategoryDto();
					categoryDto.setCategory(result.sourceCategory());
					categories.add(categoryDto);
				}
			}
			categoryDto.addResultSteps(result.execSteps(), result.blancSteps(), result.commentSteps());
		}
		if (useNonCategory) {
			categories.add(nonCategory);
		}
		Collections.sort(categories, new Comparator<CategoryStepDtoCompatible>() {
			public int compare(final CategoryStepDtoCompatible o1, final CategoryStepDtoCompatible o2) {
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
		return categories.toArray(new StepCountResultCategoryDto[categories.size()]);
	}

}
