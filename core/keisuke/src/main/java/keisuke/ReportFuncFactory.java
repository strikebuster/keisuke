package keisuke;

public class ReportFuncFactory {
	public static IfReportFunc createReportFunc(String type) {
		if (type == null) {
			return null;
		} else if (type.equals(CommonDefine.COUNTPROC)) {
			return new CountReportFunc();
		} else if (type.equals(CommonDefine.DIFFPROC)) {
			//return new DiffReportFunc();
			return new CountReportFunc();
		} else {
			return null;
		}
	}
}
