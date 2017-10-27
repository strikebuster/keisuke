package keisuke.count.diff.renderer;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.ExcelUtil;
import keisuke.count.util.LocaleUtil;
import keisuke.util.LogUtil;

/**
 * 差分カウントの結果をEXCEL形式でレンダリングします。
 */
public class ExcelRenderer extends AbstractRenderer {

	private static final String XLS_PREFIX = "DiffExcelFormat";
	private static final String XLS_EXTENSION = ".xls";
	private static final String XLS_DATA_RESULT = "results";
	private static final String XLS_DATA_TOTAL_ADD = "totalAdd";
	private static final String XLS_DATA_TOTAL_DELETE = "totalDel";

	/** {@inheritDoc} */
	public byte[] render(final DiffFolderResult result) {
		try {
			String xlsTemplate = XLS_PREFIX + LocaleUtil.getLocalePostfix() + XLS_EXTENSION;
			//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
			URL url = this.getClass().getResource(xlsTemplate);
			if (url == null) {
				xlsTemplate = XLS_PREFIX + XLS_EXTENSION;
			}
			//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
			InputStream in = ExcelRenderer.class.getResourceAsStream(xlsTemplate);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put(XLS_DATA_RESULT, listConvertedFrom(result));
			data.put(XLS_DATA_TOTAL_ADD, result.addedSteps());
			data.put(XLS_DATA_TOTAL_DELETE, result.deletedSteps());

			return ExcelUtil.makeExcelData(in, data);

		} catch (Exception ex) {
			LogUtil.errorLog("fail to make excel data");
			throw new RuntimeException(ex);
		}
	}

	private static List<DiffCountResultDto> listConvertedFrom(
			final DiffFolderResult folderResult) {
		return new ArrayList<DiffCountResultDto>(
				mapConvertedFrom(folderResult).values());
	}

	private static Map<String, DiffCountResultDto> mapConvertedFrom(
			final DiffFolderResult folderResult) {
		Map<String, DiffCountResultDto> map = new TreeMap<String, DiffCountResultDto>();
		List<AbstractDiffResultForCount> children = folderResult.getChildren();
		for (AbstractDiffResultForCount child : children) {
			if (child instanceof DiffFolderResult) {
				Map<String, DiffCountResultDto> childMap = mapConvertedFrom((DiffFolderResult) child);
				map.putAll(childMap);
			} else if (child instanceof DiffFileResult) {
				DiffCountResultDto dto = new DiffCountResultDto((DiffFileResult) child);
				map.put(dto.pathFromTop(), dto);
			}
		}
		return map;
	}
}
