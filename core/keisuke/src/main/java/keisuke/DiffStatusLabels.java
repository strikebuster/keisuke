package keisuke;

/**
 * DiffStatusEnumの差分変更ステータスに対応する表示文言を持つインタフェース
 */
public interface DiffStatusLabels {

	/**
	 * DiffStatusに対応する表示文言を返す
	 * @param status 差分変更ステータス
	 * @return ステータスに対応する表示文言
	 */
	String getLabelOf(DiffStatusEnum status);

	/**
	 * 表示文言に対応するDiffStatusEnumの値を返す
	 * @param text ステータスに対応する表示文言
	 * @return 差分変更ステータス
	 */
	DiffStatusEnum whichDiffStatusIs(String text);
}
