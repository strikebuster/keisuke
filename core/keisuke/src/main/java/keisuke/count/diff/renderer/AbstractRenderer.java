package keisuke.count.diff.renderer;

import static keisuke.count.diff.renderer.RendererConstant.MSG_DIFF_RND_UNDEF;

import keisuke.MessageMap;
import keisuke.count.Formatter;
import keisuke.count.PathStyleEnum;
import keisuke.count.SortOrderEnum;
import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;

import keisuke.DiffStatusEnum;
import keisuke.DiffStatusLabels;
import keisuke.DiffStatusLabelsImpl;

/**
 * 差分計測結果の出力整形をするI/Fを実装する抽象基底クラス
 */
public abstract class AbstractRenderer implements Formatter<DiffFolderResult> {

	private MessageMap messageMap = null;
	private DiffStatusLabels diffStatusLabels = null;
	private PathStyleEnum pathStyle = PathStyleEnum.BASE;
	private SortOrderEnum sortOrder = SortOrderEnum.OS;

	protected AbstractRenderer() {	}

	/**
	 * メッセージ定義インスタンスを設定する
	 * @param msgdef メッセージ定義インスタンス
	 */
	protected void setMessageMap(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.messageMap = msgdef.getMessageMap();
		this.diffStatusLabels = new DiffStatusLabelsImpl(msgdef);
	}

	/**
	 * DiffStatusLabelsインスタンスを返す
	 * @return DiffStatusLabelsインスタンス
	 */
	protected DiffStatusLabels diffStatusLabels() {
		return this.diffStatusLabels;
	}

	/**
	 * 差分計測結果の出力形式で使用する定形文言を返す
	 * @param key 定形文言の種類を示すキー
	 * @return 表示用定形文言
	 */
	protected String getMessageText(final String key) {
		if (key == null) {
			return "";
		}
		return this.messageMap.get(key);
	}

	/**
	 * パス表記スタイルを設定する
	 * @param style パス表記スタイル
	 */
	public void setPathStyle(final PathStyleEnum style) {
		this.pathStyle = style;
	}

	/**
	 * パス表記スタイルを返す
	 * @return パス表記スタイル
	 */
	protected PathStyleEnum pathStyle() {
		return this.pathStyle;
	}

	/**
	 * ソート順を設定する
	 * @param order ソート順
	 */
	public void setSortOrder(final SortOrderEnum order) {
		this.sortOrder = order;
	}

	/**
	 * ソート順を返す
	 * @return ソート順
	 */
	protected SortOrderEnum sortOrder() {
		return this.sortOrder;
	}

	/**
	 * 変更ステータスに対するメッセージ定義の文言を返す
	 * @param status 変更ステータス
	 * @return メッセージ定義の文言
	 */
	protected String getStatusLabelOf(final DiffStatusEnum status) {
		return this.diffStatusLabels.getLabelOf(status);
	}

	/**
	 * 変更ステータスに対するメッセージ定義の文言を返す
	 * ただしソースタイプが未対応の場合は変更ステータスに依らずUNSUPPORTEDの定義文言を返す
	 * @param status 変更ステータス
	 * @param unsupported 未対応フラグ
	 * @return メッセージ定義の文言
	 */
	protected String getStatusLabelOf(final DiffStatusEnum status, final boolean unsupported) {
		if (unsupported) {
			return this.diffStatusLabels.getLabelOf(DiffStatusEnum.UNSUPPORTED);
		}
		return this.diffStatusLabels.getLabelOf(status);
	}

	/**
	 * ソースコードタイプを返す
	 * @param type 計測結果のソースコードタイプ
	 * @return ソースコードタイプ
	 */
	protected String getSourceType(final String type) {
		if (type == null) {
			return this.messageMap.get(MSG_DIFF_RND_UNDEF);
		}
		return type;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * 引数は差分計測結果
	 */
	public abstract byte[] format(DiffFolderResult result);

	/** {@inheritDoc} */
	public abstract boolean isText();

	/** {@inheritDoc} */
	public abstract String textEncoding();
}
