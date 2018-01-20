package keisuke.count.diff.renderer;

import java.util.Date;

import static keisuke.count.diff.renderer.RendererConstant.*;

import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.DateUtil;


/**
 * 差分カウントの結果をテキスト形式でレンダリングします。
 */
public class TextRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] render(final DiffFolderResult result) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getMessageText(MSG_DIFF_RND_TIME));
		sb.append("：");
		sb.append(DateUtil.formatDate(new Date())).append("\n");
		sb.append("--\n");
		sb.append(this.getTextLineAbout(result, 0));
		return sb.toString().getBytes();
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
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(result.nodeName()).append("/");
		sb.append("[").append(this.getStatusLabelOf(result.status())).append("]");
		sb.append(" +").append(result.addedSteps());
		sb.append(" -").append(result.deletedSteps()).append("\n");

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
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < nest; i++) {
			sb.append(" ");
		}
		sb.append(result.nodeName());
		sb.append("[").append(this.getStatusLabelOf(result.status())).append("]");
		sb.append(" +").append(result.addedSteps());
		sb.append(" -").append(result.deletedSteps());
		sb.append("\n");
		return sb.toString();
	}
}
