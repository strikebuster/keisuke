package keisuke;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

public class FrameworkClassifyFunc implements IfClassifyFunc {

	private final static String xmlfile = "/keisuke/framework.xml";
	private String classify = null;
	public List<FwSpecificElement> fwSpecificList = null;
	private List<FwPatternElement> fwPatternList = null;
	
	public FrameworkClassifyFunc(String fwname) {
		this.classify = fwname;
		try {
			URL urlfile = this.getClass().getResource(xmlfile);
			String uriStr = urlfile.toURI().toString();
			init(uriStr);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public FrameworkClassifyFunc(String fwname, String fname) {
		this.classify = fwname;
		init(fname);
	}
	
	private void init(String xfile) {
		XmlFrameworkDefine xmlDef = new XmlFrameworkDefine(xfile);
		this.fwSpecificList = xmlDef.createFwSpecificList(this.classify);
		if (this.fwSpecificList == null) {
			return;
		}
		//debugFwSpecificList();
		createFwPatternList();
		//debugFwPatternList();
	}
	
	private void createFwPatternList() {
		// this.fwSpecificList -> this.fwPatternList
		this.fwPatternList = new ArrayList<FwPatternElement>();
		for (FwSpecificElement fse : this.fwSpecificList) {
			int size = fse.getPatternStrings().size();
			for (int i = 0; i < size; i++) {
				FwPatternElement fpe = new FwPatternElement(fse, i);
				this.fwPatternList.add(fpe);
			}
		}
		Collections.sort(this.fwPatternList);
		Collections.reverse(this.fwPatternList);
	}
	
	public String getClassifyName(String strpath) {
		if (strpath == null) {
			return null;
		}
		FwPatternElement fpe = searchFwPatternElement(strpath);
		if (fpe == null) {
			return attachNumberingName(999, "Others");
		} else {
			return fpe.getName();
		}
	}
	
	public String getClassifyNameForReport(String classifyname) {
		return detachNumberingName(classifyname);
	}
	
	public List<String> getClassifyFixedList() {
		if (this.fwSpecificList == null || this.fwSpecificList.isEmpty()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (FwSpecificElement fse : this.fwSpecificList) {
			list.add(fse.getName());
		}
		return list;
	}
	
	private FwPatternElement searchFwPatternElement(String path) {
		if (path == null) {
			return null;
		}
		if (this.fwPatternList == null) {
			return null;
		}
		for (FwPatternElement fpe : this.fwPatternList) {
			Matcher ma = fpe.getPattern().matcher(path);
			if (ma.matches()) {
				return fpe;
			}
		}
		return null;
	}
	
	private static String attachNumberingName(int num, String name) {
		return (String.format("%03d", num) + "#" + name);
	}
	
	private static String detachNumberingName(String numname) {
		if (numname == null) {
			return null;
		}
		int pos = numname.indexOf('#');
		if (pos < 0 || pos == numname.length() - 1) {
			return numname;
		}
		return numname.substring(pos+1);
	}
	
	public void debugFwSpecificList() {
		System.out.println("[DEBUG] fw:" + this.classify + ", fwSpecificList contains ");
		if (this.fwSpecificList == null) {
			System.out.println("[DEBUG] null ");
			return;
		}
		for (FwSpecificElement fse : this.fwSpecificList) {
			System.out.println(fse.debug());
		}
	}
	
	public void debugFwPatternList() {
		System.out.println("[DEBUG] fw:" + this.classify + ", fwPatternList contains ");
		if (this.fwPatternList == null) {
			System.out.println("[DEBUG] null ");
			return;
		}
		for (FwPatternElement fpe : this.fwPatternList) {
			System.out.println(fpe.debug());
		}
	}
}
