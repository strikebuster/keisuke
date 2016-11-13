package keisuke;

public class CountElement implements ICountElement {

	public String classify = "";
	public int execSteps = 0;
	public int blancSteps = 0;
	public int commentSteps = 0;
	public int sumSteps = 0;
	public int files = 0;
	
	public CountElement(String name) {
		this.classify = name;
	}
	
	public CountElement(String name, int exe, int blanc, int comment, int sum) {
		this.classify = name;
		this.execSteps = exe;
		this.blancSteps = blanc;
		this.commentSteps = comment;
		this.sumSteps = sum;
		this.files = 1;
	}
	
	public void add(ICountElement ice) {
		if (ice == null || !(ice instanceof CountElement)) {
			return;
		}
		CountElement ce = (CountElement)ice;
		if (! this.classify.equals(ce.classify)) {
			return;
		}
		this.execSteps += ce.execSteps;
		this.blancSteps += ce.blancSteps;
		this.commentSteps += ce.commentSteps;
		this.sumSteps += ce.sumSteps;
		this.files += ce.files;
	}
	
	public int getValue(String key) {
		if (key == null) {
			return -1;
		}
		int val;
		if (key.equals(CommonDefine.CP_EXECSTEP)) {
			val = this.execSteps;
		} else if (key.equals(CommonDefine.CP_BLANCSTEP)) {
			val = this.blancSteps;
		} else if (key.equals(CommonDefine.CP_COMMENTSTEP)) {
			val = this.commentSteps;
		} else if (key.equals(CommonDefine.CP_SUMSTEP)) {
			val = this.sumSteps;
		} else if (key.equals(CommonDefine.CP_FILENUM)) {
			val = this.files;
		} else {
			val = -1;
		}
		return val;
	}
}
