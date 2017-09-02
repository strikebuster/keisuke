package keisuke;

/**
 * 変更ステータスで仕分けるためのステータス毎のデータ格納クラス
 */
public class DiffCountResultsAssortedStatus implements CountResult {

	private static final int DIFF_STATUS_NUM = DiffStatusEnum.values().length;
	// 集計結果の仕分け対象ステータス数分の配列を作成
	// ステータス定義の最後の１要素は言語分類できないUNSUPPORTEDなので集計から除外
	private DiffCountResult[] sumOfResults = new DiffCountResult[DIFF_STATUS_NUM - 1];

	public DiffCountResultsAssortedStatus(final String classify) {
		for (int i = 0; i < DIFF_STATUS_NUM - 1; i++) {
			this.sumOfResults[i] = new DiffCountResult(classify);
		}
	}

	/**
	 * 変更ステータスを指定してその計測結果の集計値を格納するインスタンスを返す
	 * @param status 対象とする変更ステータス
	 * @return 集計値を格納するインスタンス
	 */
	public DiffCountResult sumOfResultFor(final DiffStatusEnum status) {
		return this.sumOfResults[status.index()];
	}

	/** {@inheritDoc} */
	public void add(final CountResult result) {
		if (result == null || !(result instanceof DiffCountResult)) {
			return;
		}
		DiffCountResult another = (DiffCountResult) result;
		DiffCountResult sumResult = this.sumOfResults[another.status().index()];
		if (!another.classify().equals(sumResult.classify())) {
			return;
		}
		sumResult.add(another);
	}

	/** {@inheritDoc} */
	public int getValue(final String key) {
		if (key == null) {
			return 0;
		}
		int val = 0;
		for (int i = 0; i < DIFF_STATUS_NUM - 1; i++) {
			val += this.sumOfResults[i].getValue(key);
		}
		return val;
	}
}
