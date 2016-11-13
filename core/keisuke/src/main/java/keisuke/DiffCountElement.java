package keisuke;

public class DiffCountElement implements ICountElement {
	
	public String classify = "";
	public int addSteps = 0;
	public int deleteSteps = 0;
	public int sumSteps = 0;
	public int files = 0;
	
	public DiffCountElement(String name) {
		this.classify = name;
		this.addSteps = 0;
		this.deleteSteps = 0;
		this.sumSteps = 0;
		this.files = 0;
	}
	
	public DiffCountElement(String name, int add, int del) {
		this.classify = name;
		this.addSteps = add;
		this.deleteSteps = del;
		this.sumSteps = add - del;
		this.files = 1;
	}
	
	public void add(ICountElement ice) {
		if (ice == null || !(ice instanceof DiffCountElement)) {
			return;
		}
		DiffCountElement de = (DiffCountElement)ice;
		if (! this.classify.equals(de.classify)) {
			return;
		}
		this.addSteps += de.addSteps;
		this.deleteSteps += de.deleteSteps;
		this.sumSteps += de.sumSteps;
		this.files += de.files;
	}
	
	public int getValue(String key) {
		if (key == null) {
			return -1;
		}
		int val;
		if (key.equals(CommonDefine.DP_ADDSTEP)) {
			val = this.addSteps;
		} else if (key.equals(CommonDefine.DP_DELSTEP)) {
			val = this.deleteSteps;
		} else if (key.equals(CommonDefine.DP_SUMSTEP)) {
			val = this.sumSteps;
		} else if (key.equals(CommonDefine.DP_FILENUM)) {
			val = this.files;
		} else {
			val = -1;
		}
		return val;
	}
}
