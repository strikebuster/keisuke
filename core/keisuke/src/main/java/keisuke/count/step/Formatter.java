package keisuke.count.step;

import keisuke.StepCountResult;

/**
 * ステップ計測結果の出力用フォーマッター
 */
public interface Formatter {

	/**
	 * ステップ計測結果を出力用にフォーマットし、バイト配列にして返す
	 * @param result ステップ計測結果
	 * @return フォーマット後のバイト配列
	 */
	byte[] format(StepCountResult[] result);
}
