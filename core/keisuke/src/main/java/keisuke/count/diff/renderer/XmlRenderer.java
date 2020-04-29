package keisuke.count.diff.renderer;

import static keisuke.util.StringUtil.LINE_SEP;
import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import java.io.UnsupportedEncodingException;
import java.util.List;

import keisuke.count.SortOrderEnum;
import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.EncodeUtil;

/**
 * 差分カウントの結果をXML形式でレンダリングします。
 */
public class XmlRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"");
		sb.append(SYSTEM_ENCODING);
		sb.append("\"?>");
		sb.append(LINE_SEP);
		sb.append("<diffcounter>");
		sb.append(LINE_SEP);
		sb.append(this.getXmlLineAbout(result, 0));
		sb.append("</diffcounter>");
		sb.append(LINE_SEP);
		try {
			return sb.toString().getBytes(SYSTEM_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 差分計測結果を表示用XMLに整形した文字列を返す
	 * @param result フォルダー/ファイル差分計測結果インスタンス
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected String getXmlLineAbout(final AbstractDiffResultForCount result, final int nest) {
		if (result instanceof DiffFolderResult) {
			return this.getXmlLineAbout((DiffFolderResult) result, nest);
		} else if (result instanceof DiffFileResult) {
			return this.getXmlLineAbout((DiffFileResult) result, nest);
		}
		return "";
	}

	/**
	 * 差分計測結果を表示用XMLに整形した文字列を返す
	 * @param result フォルダー差分計測結果インスタンス
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected String getXmlLineAbout(final DiffFolderResult result, final int nest) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append("<directory ");
		sb.append("name=\"").append(EncodeUtil.xmlEscape(result.nodeName())).append("\" ");
		sb.append("status=\"").append(EncodeUtil.xmlEscape(this.getStatusLabelOf(result.status())))
			.append("\" ");
		sb.append("added=\"").append(result.addedSteps()).append("\" ");
		sb.append("deleted=\"").append(result.deletedSteps()).append("\" >").append(LINE_SEP);

		List<AbstractDiffResultForCount> children = null;
		if (this.sortOrder() == SortOrderEnum.NODE) {
			children = result.getSortedChildren();
		} else {
			children = result.getChildren();
		}
		for (AbstractDiffResultForCount obj : children) {
			sb.append(this.getXmlLineAbout(obj, nest + 1));
		}
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append("</directory>").append(LINE_SEP);
		return sb.toString();
	}

	/**
	 * 差分計測結果を表示用XMLに整形した文字列を返す
	 * @param result ファイル差分計測結果インスタンス
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected String getXmlLineAbout(final DiffFileResult result, final int nest) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append("<file ");
		sb.append("name=\"").append(EncodeUtil.xmlEscape(result.nodeName())).append("\" ");
		sb.append("type=\"").append(EncodeUtil.xmlEscape(this.getSourceType(result.sourceType())))
			.append("\" ");
		if (result.sourceCategory() != null && !result.sourceCategory().isEmpty()) {
			sb.append("category=\"").append(EncodeUtil.xmlEscape(result.sourceCategory())).append("\" ");
		}
		sb.append("status=\"").append(EncodeUtil.xmlEscape(this.getStatusLabelOf(result.status())))
			.append("\" ");
		if (!result.isUnsupported()) {
			sb.append("added=\"").append(result.addedSteps()).append("\" ");
			sb.append("deleted=\"").append(result.deletedSteps()).append("\" ");
		}
		sb.append("/>").append(LINE_SEP);
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
