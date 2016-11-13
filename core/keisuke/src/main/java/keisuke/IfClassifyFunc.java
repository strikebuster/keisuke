package keisuke;

import java.util.List;

public interface IfClassifyFunc {

	public String getClassifyName(String path);
	
	public String getClassifyNameForReport(String classifyname);
	
	public List<String> getClassifyFixedList();
}
