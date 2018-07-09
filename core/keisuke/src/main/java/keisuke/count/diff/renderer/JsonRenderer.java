package keisuke.count.diff.renderer;

import static keisuke.util.StringUtil.LINE_SEP;
//import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import java.io.UnsupportedEncodingException;

import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.EncodeUtil;

/**
 * 差分カウントの結果をJSON形式でレンダリングします。
 */
public class JsonRenderer extends AbstractRenderer {

	private boolean first = true;

	/** {@inheritDoc} */
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(this.getJsonLineAbout(result));
		sb.append(LINE_SEP);
		sb.append("]");
		sb.append(LINE_SEP);
		try {
			// JSON data encoding must be UTF-8
			return sb.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 差分計測結果を表示用JSONに整形した文字列を返す
	 * @param result フォルダー/ファイル差分計測結果インスタンス
	 * @return 表示用テキスト
	 */
	protected String getJsonLineAbout(final AbstractDiffResultForCount result) {
		if (result instanceof DiffFolderResult) {
			return this.getJsonLineAbout((DiffFolderResult) result);
		} else if (result instanceof DiffFileResult) {
			return this.getJsonLineAbout((DiffFileResult) result);
		}
		return "";
	}

	/**
	 * 差分計測結果を表示用JSONに整形した文字列を返す
	 * @param result フォルダー差分計測結果インスタンス
	 * @return 表示用テキスト
	 */
	protected String getJsonLineAbout(final DiffFolderResult result) {
		StringBuilder sb = new StringBuilder();
		for (AbstractDiffResultForCount obj : result.getChildren()) {
			sb.append(this.getJsonLineAbout(obj));
		}
		return sb.toString();
	}

	/**
	 * 差分計測結果を表示用JSONに整形した文字列を返す
	 * @param result ファイル差分計測結果インスタンス
	 * @return 表示用テキスト
	 */
	protected String getJsonLineAbout(final DiffFileResult result) {
		StringBuilder sb = new StringBuilder();
		if (this.first) {
			this.first = false;
		} else {
			sb.append(',');
		}
		sb.append(LINE_SEP);
		sb.append("\t{ \"name\": \"");
		sb.append(EncodeUtil.unicodeEscape(result.pathFromTop())).append("\", ");
		sb.append("\"type\": \"").append(EncodeUtil.unicodeEscape(result.sourceType())).append("\", ");
		if (result.sourceCategory() != null && result.sourceCategory().length() > 0) {
			sb.append("\"category\": \"")
				.append(EncodeUtil.unicodeEscape(result.sourceCategory())).append("\", ");
		}
		sb.append("\"status\": \"")
			.append(EncodeUtil.unicodeEscape(this.getStatusLabelOf(result.status()))).append("\", ");
		sb.append("\"added\": ").append(result.addedSteps()).append(", ");
		sb.append("\"deleted\": ").append(result.deletedSteps()).append(" }");
		return sb.toString();
	}

	/** {@inheritDoc} */
	public boolean isText() {
		return true;
	}

	/** {@inheritDoc} */
	public String textEncoding() {
		return "UTF-8";
	}
}
