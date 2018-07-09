package keisuke.count.diff.renderer;

import static keisuke.count.diff.renderer.RendererConstant.*;
import static keisuke.util.StringUtil.LINE_SEP;
import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.DateUtil;


/**
 * 差分カウントの結果をテキスト形式でレンダリングします。
 */
public class TextRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(this.getMessageText(MSG_DIFF_RND_TIME));
		sb.append("：");
		sb.append(DateUtil.formatDate(new Date()));
		sb.append(LINE_SEP);
		sb.append("--");
		sb.append(LINE_SEP);
		sb.append(this.getTextLineAbout(result, 0));
		try {
			return sb.toString().getBytes(SYSTEM_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 差分計測結果を表示用テキストに整形した文字列を返す
	 * @param result フォルダー/ファイル差分計測結果インスタンス
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected String getTextLineAbout(final AbstractDiffResultForCount result, final int nest) {
		if (result instanceof DiffFolderResult) {
			return this.getTextLineAbout((DiffFolderResult) result, nest);
		} else if (result instanceof DiffFileResult) {
			return this.getTextLineAbout((DiffFileResult) result, nest);
		}
		return "";
	}

	/**
	 * 差分計測結果を表示用テキストに整形した文字列を返す
	 * @param result フォルダー差分計測結果インスタンス
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected String getTextLineAbout(final DiffFolderResult result, final int nest) {
		//result.evaluateChildren(); // 子供を評価して自身の値を確定する
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(result.nodeName()).append("/");
		sb.append("[").append(this.getStatusLabelOf(result.status())).append("]");
		sb.append(" +").append(result.addedSteps());
		sb.append(" -").append(result.deletedSteps()).append(LINE_SEP);

		for (AbstractDiffResultForCount obj : result.getSortedChildren()) {
			sb.append(this.getTextLineAbout(obj, nest + 1));
		}
		return sb.toString();
	}

	/**
	 * 差分計測結果を表示用テキストに整形した文字列を返す
	 * @param result ファイル差分計測結果インスタンス
	 * @param nest ルートからの階層数
	 * @return 表示用テキスト
	 */
	protected String getTextLineAbout(final DiffFileResult result, final int nest) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(result.nodeName());
		sb.append("[").append(this.getStatusLabelOf(result.status())).append("]");
		sb.append(" +").append(result.addedSteps());
		sb.append(" -").append(result.deletedSteps());
		sb.append(LINE_SEP);
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
