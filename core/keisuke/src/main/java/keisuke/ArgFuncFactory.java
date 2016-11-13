package keisuke;

public class ArgFuncFactory {

	public static IfArgFunc createArgFunc(String type) {
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
