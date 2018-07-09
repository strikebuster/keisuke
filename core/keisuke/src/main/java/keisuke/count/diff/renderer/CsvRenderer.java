package keisuke.count.diff.renderer;

import static keisuke.util.StringUtil.LINE_SEP;
import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import java.io.UnsupportedEncodingException;

import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.EncodeUtil;

/**
 * 差分カウントの結果をCSV形式でレンダリングします。
 */
public class CsvRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		try {
			return this.getCsvLineAbout(result).getBytes(SYSTEM_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 差分計測結果を表示用CSVに整形した文字列を返す
	 * @param result フォルダー/ファイル差分計測結果インスタンス
	 * @return 表示用テキスト
	 */
	protected String getCsvLineAbout(final AbstractDiffResultForCount result) {
		if (result instanceof DiffFolderResult) {
			return this.getCsvLineAbout((DiffFolderResult) result);
		} else if (result instanceof DiffFileResult) {
			return this.getCsvLineAbout((DiffFileResult) result);
		}
		return "";
	}

	/**
	 * 差分計測結果を表示用CSVに整形した文字列を返す
	 * @param result フォルダー差分計測結果インスタンス
	 * @return 表示用テキスト
	 */
	protected String getCsvLineAbout(final DiffFolderResult result) {
		StringBuffer sb = new StringBuffer();
		//sb.append(result.pathFromTop()).append("/,,,");
		//sb.append(this.getStatusLabelOf(result.status())).append(',');
		//sb.append(result.addedSteps()).append(',');
		//sb.append(result.deletedSteps()).append(LINE_SEP);
		//for (AbstractDiffResultForCount obj : result.getSortedChildren()) {
		for (AbstractDiffResultForCount obj : result.getChildren()) {
			sb.append(this.getCsvLineAbout(obj));
		}
		return sb.toString();
	}

	/**
	 * 差分計測結果を表示用CSVに整形した文字列を返す
	 * @param result ファイル差分計測結果インスタンス
	 * @return 表示用テキスト
	 */
	protected String getCsvLineAbout(final DiffFileResult result) {
		StringBuffer sb = new StringBuffer();
		sb.append(result.pathFromTop()).append(',');
		sb.append(result.sourceType()).append(',');
		sb.append(EncodeUtil.csvEscape(result.sourceCategory())).append(',');
		sb.append(this.getStatusLabelOf(result.status())).append(',');
		sb.append(result.addedSteps()).append(',');
		sb.append(result.deletedSteps()).append(LINE_SEP);
		return sb.toString();
	}

	/** {@inheritDoc} */
	public boolean isText() {
		return true;
	}

	/** {@inheritDoc} */
	public String textEncoding() {
		return SYSTEM_ENCODING;
	}
}
