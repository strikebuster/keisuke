package keisuke;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class ExtensionClassifyFunc implements IfClassifyFunc {

	public String getClassifyName(String strpath) {
		if (strpath == null) {
			return null;
		}
		String strext = FilenameUtils.getExtension(strpath);
		if (strext == null) {
			return null;
		}
		return strext.toLowerCase();
	}
	
	public String getClassifyNameForReport(String classifyname) {
		return classifyname;
	}
	
	public List<String> getClassifyFixedList() {
		return null;
	}
}
