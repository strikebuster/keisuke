package keisuke.count.diff.renderer;

import java.io.IOException;
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
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		InputStream in = null;
		try {
			String xlsTemplate = XLS_PREFIX + LocaleUtil.getLocalePostfix() + XLS_EXTENSION;
			//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
			URL url = this.getClass().getResource(xlsTemplate);
			if (url == null) {
				xlsTemplate = XLS_PREFIX + XLS_EXTENSION;
			}
			//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
			in = this.getClass().getResourceAsStream(xlsTemplate);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(XLS_DATA_RESULT, this.listConvertedFrom(result));
			data.put(XLS_DATA_TOTAL_ADD, result.addedSteps());
			data.put(XLS_DATA_TOTAL_DELETE, result.deletedSteps());
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

	private List<DiffCountResultDto> listConvertedFrom(
			final DiffFolderResult folderResult) {
		return new ArrayList<DiffCountResultDto>(
				this.mapConvertedFrom(folderResult).values());
	}

	private Map<String, DiffCountResultDto> mapConvertedFrom(
			final DiffFolderResult folderResult) {
		Map<String, DiffCountResultDto> map = new TreeMap<String, DiffCountResultDto>();
		List<AbstractDiffResultForCount> children = folderResult.getChildren();
		for (AbstractDiffResultForCount child : children) {
			if (child instanceof DiffFolderResult) {
				Map<String, DiffCountResultDto> childMap = mapConvertedFrom((DiffFolderResult) child);
				map.putAll(childMap);
			} else if (child instanceof DiffFileResult) {
				DiffCountResultDto dto =
						new DiffCountResultDto((DiffFileResult) child, this.diffStatusLabels());
				map.put(dto.pathFromTop(), dto);
			}
		}
		return map;
	}

	/** {@inheritDoc} */
	public boolean isText() {
		return false;
	}

	/** {@inheritDoc} */
	public String textEncoding() {
		return null;
	}
}
