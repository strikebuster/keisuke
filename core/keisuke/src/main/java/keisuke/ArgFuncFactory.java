package keisuke;

/**
 * Factory class to return instance which implements ArgFunc.
 * @author strikebuster
 */
final class ArgFuncFactory {
	private ArgFuncFactory() { }

	/**
	 * 各コマンド用のオプション解析クラスの生成
	 * @param type コマンドの種類を指定する定数文字列
	 * @return IfArgFunc 各コマンド用のオプション解析クラス
	 */
	static IfArgFunc createArgFunc(final String type) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.COUNTPROC)) {
			return new CountArgFunc();
		} else if (type.equals(CommonDefine.DIFFPROC)) {
			return new DiffArgFunc();
		} else if (type.equals(CommonDefine.MATCHPROC)) {
			return new MatchArgFunc();
		} else {
			return null;
		}
	}
}
