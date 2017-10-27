package keisuke.report;

import keisuke.DiffStatusEnum;

/**
 * 変更ステータスで仕分けるためのステータス毎のデータ格納クラス
 */
public class DiffCountResultsAssortedStatus implements CountResultForReport {

	private static final int DIFF_STATUS_NUM = DiffStatusEnum.values().length;
	// 集計結果の仕分け対象ステータス数分の配列を作成
	// ステータス定義の最後の１要素は言語分類できないUNSUPPORTEDなので集計から除外
	private DiffCountResultForReport[] sumOfResults = new DiffCountResultForReport[DIFF_STATUS_NUM - 1];

	public DiffCountResultsAssortedStatus(final String classify) {
		for (int i = 0; i < DIFF_STATUS_NUM - 1; i++) {
			this.sumOfResults[i] = new DiffCountResultForReport(classify);
		}
	}

	/**
	 * 変更ステータスを指定してその計測結果の集計値を格納するインスタンスを返す
	 * @param status 対象とする変更ステータス
	 * @return 集計値を格納するインスタンス
	 */
	public DiffCountResultForReport sumOfResultFor(final DiffStatusEnum status) {
		return this.sumOfResults[status.index()];
	}

	/** {@inheritDoc} */
	public void add(final CountResultForReport result) {
		if (result == null || !(result instanceof DiffCountResultForReport)) {
			return;
		}
		DiffCountResultForReport another = (DiffCountResultForReport) result;
		DiffCountResultForReport sumResult = this.sumOfResults[another.status().index()];
		if (!another.classify().equals(sumResult.classify())) {
			return;
		}
		sumResult.add(another);
	}

	/** {@inheritDoc} */
	public long getValue(final String key) {
		if (key == null) {
			return 0;
		}
		long val = 0;
		for (int i = 0; i < DIFF_STATUS_NUM - 1; i++) {
			val += this.sumOfResults[i].getValue(key);
		}
		return val;
	}
}
