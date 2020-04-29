package keisuke.count.step.format;

import static keisuke.count.step.format.FormatConstant.*;
import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import keisuke.MessageMap;
import keisuke.StepCountResult;
import keisuke.count.FormatEnum;
import keisuke.count.Formatter;
import keisuke.report.property.MessageDefine;

/**
 * ステップ計測結果を出力形式に整形する
 */
public abstract class AbstractFormatter implements Formatter<StepCountResult[]> {

	private FormatEnum formatEnum;
	private MessageMap messageMap;

	protected AbstractFormatter() { }

	/**
	 * コンストラクター
	 * フォーマット種類とメッセージ定義の設定を行う
	 * @param format FormatEnumインスタンス
	 */
	protected AbstractFormatter(final FormatEnum format) {
		this.formatEnum = format;
		this.messageMap = new MessageDefine(MSG_COUNT_FMT_PREFIX).getMessageMap();
	}

	/**
	 * フォーマッタが対応するFormatEnum列挙子の要素を返す
	 * @return FormatEnum列挙子の要素
	 */
	protected FormatEnum formatEnum() {
		return this.formatEnum;
	}
	/**
	 * ステップ計測結果の出力形式で使用する定形文言を返す
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
	 * ソースコードタイプを返す
	 * @param type 計測結果のソースコードタイプ
	 * @return ソースコードタイプ
	 */
	protected String getSourceType(final String type) {
		if (type == null) {
			return this.messageMap.get(MSG_COUNT_FMT_UNDEF);
		}
		return type;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * 引数はステップ計測結果の配列
	 */
	public abstract byte[] format(StepCountResult[] results);

	/** {@inheritDoc} */
	public boolean isText() {
		return this.formatEnum.isTextData();
	}

	/** {@inheritDoc} */
	public String textEncoding() {
		String encoding = this.formatEnum.stableEncoding();
		if (encoding != null && encoding.isEmpty()) {
			// 空文字列の場合は環境依存
			return SYSTEM_ENCODING;
		}
		return encoding;
	}
}
