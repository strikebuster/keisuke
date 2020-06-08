package keisuke.count.diff.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import keisuke.count.FormatEnum;
import keisuke.count.SortOrderEnum;
import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.ExcelUtil;
import keisuke.util.LocaleUtil;
import keisuke.util.LogUtil;

/**
 * 差分カウントの結果をEXCEL形式でレンダリングします。
 */
public class ExcelRenderer extends AbstractRenderer {

	//private static final String XLS_PREFIX = "DiffExcelTemplate";
	private static final String XLS_PREFIX = "DiffExcelFormat";
	private static final String XLS_DATA_RESULT = "results";
	private static final String XLS_DATA_TOTAL_ADD = "totalAdd";
	private static final String XLS_DATA_TOTAL_DELETE = "totalDel";
	private String xlsExtension = "." + FormatEnum.EXCEL.fileExtension();

	ExcelRenderer(final FormatEnum formatEnum) {
		super();
		//if (formatEnum.equals(FormatEnum.EXCEL97)) {
		//	this.xlsExtension = "." + FormatEnum.EXCEL97.fileExtension();
		//}
	}

	/** {@inheritDoc} */
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		InputStream in = null;
		try {
			String xlsTemplate = XLS_PREFIX + LocaleUtil.getLocalePostfix() + this.xlsExtension;
			//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
			URL url = this.getClass().getResource(xlsTemplate);
			if (url == null) {
				xlsTemplate = XLS_PREFIX + this.xlsExtension;
				//LogUtil.debugLog("xlsTemplate = " + xlsTemplate);
			}
			in = this.getClass().getResourceAsStream(xlsTemplate);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(XLS_DATA_RESULT, this.listConvertedFrom(result));
			data.put(XLS_DATA_TOTAL_ADD, result.addedSteps());
			data.put(XLS_DATA_TOTAL_DELETE, result.deletedSteps());
			return new ExcelUtil().makeExcelData(in, data);
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

	/**
	 * 差分計測結果からEXCELレンダリング用DTOのリストを返す
	 * @param result フォルダー差分計測結果インスタンス
	 * @return EXCELレンダリング用DTOのリスト
	 */
	protected List<DiffCountResultDto> listConvertedFrom(final DiffFolderResult result) {
		List<DiffCountResultDto> list = new ArrayList<DiffCountResultDto>();
		List<AbstractDiffResultForCount> children = null;
		if (this.sortOrder() == SortOrderEnum.NODE) {
			children = result.getSortedChildren();
		} else {
			children = result.getChildren();
		}
		for (AbstractDiffResultForCount child : children) {
			if (child instanceof DiffFolderResult) {
				list.addAll(this.listConvertedFrom((DiffFolderResult) child));
			} else if (child instanceof DiffFileResult) {
				list.add(this.listConvertedFrom((DiffFileResult) child));
			}
		}
		return list;
	}

	/**
	 * 差分計測結果からEXCELレンダリング用DTOを返す
	 * @param result ファイル差分計測結果インスタンス
	 * @return EXCELレンダリング用DTO
	 */
	protected DiffCountResultDto listConvertedFrom(final DiffFileResult result) {
		// dto.filePath()のパス表記スタイルを有効に
		DiffCountResultDto dto =
				new DiffCountResultDto(result, this.diffStatusLabels(), this.pathStyle());
		// soutceTypeがnullのときの表記を設定
		if (result.sourceType() == null) {
			dto.setSourceType(this.getSourceType(result.sourceType()));
		}
		return dto;
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
