package keisuke;

import java.util.regex.Pattern;

public class FwPatternElement implements Comparable<FwPatternElement> {

	private String name = null;
	private String patternString = null;
	private int patternLength = -1;
	private Pattern pattern = null;
	
	public FwPatternElement(FwSpecificElement fse, int idx) {
		if (fse == null) {
			return;
		}
		this.name = fse.getName();
		if (idx < 0 || idx >= fse.countPatternStrings()) {
			return;
		}
		this.patternString = fse.getPatternStrings().get(idx);
		if (this.patternString == null) {
			return;
		}
		this.patternLength = this.patternString.length();
		this.pattern = Pattern.compile(this.patternString);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Pattern getPattern() {
		return this.pattern;
	}
	
	public int getPatternLength() {
		return this.patternLength;
	}
	
	public int compareTo(FwPatternElement fpe) throws NullPointerException{
		if (fpe == null) {
			throw new NullPointerException();
		}
		return (this.patternLength - fpe.getPatternLength());
	}
	
	public String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("[DEBUG] FwPatternElement : name=" + this.name);
		sb.append("\n");
		sb.append("[DEBUG] FwPatternElement : patternStringn=" + this.patternString + " , length=" + this.patternLength) ;
		sb.append("\n");
		return sb.toString();
	}
}
