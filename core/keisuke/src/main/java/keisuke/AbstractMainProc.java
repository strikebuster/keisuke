package keisuke;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public abstract class AbstractMainProc implements IfMainProc{
	protected String proctype = "";
	protected IfArgFunc argFunc = null;
	protected IfReportFunc reportFunc = null;
	protected IfClassifyFunc classifyType = null;
	public String reportOutput = "";
	public Map<String, String> argMap = null;
	public Map<String, ReportColumn> columnMap = null;
	public Map<String, String> messageMap = null;
	
	protected AbstractMainProc() {	}
	
	public void main(String[] args) {
	}
	
	protected void createBindedFuncs(String type) {
		this.proctype = type;
		this.argFunc = ArgFuncFactory.createArgFunc(this.proctype);
		this.reportFunc = ReportFuncFactory.createReportFunc(this.proctype);
	}
	
	public String getProctype() {
		return this.proctype;
	}
	
	protected void writeOutput() {
		BufferedWriter writer = null;
		try {
			// 出力ファイル：標準出力
			//writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fname))));
			writer = new BufferedWriter(new OutputStreamWriter(System.out));
			writer.write(this.reportOutput);
			writer.flush();
		} catch (IOException e) {
			System.err.println("!! Write error : <System.out>");
			throw new RuntimeException(e);
		} finally {
			//if (writer != null) try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	protected void debugArgMap(Map<String, String> argMap) {
		debugMap(argMap);
	}
	
	protected void debugColMap(Map<String, ReportColumn> colMap) {
		for ( String key : colMap.keySet() ) {
			ReportColumn repcol = colMap.get( key );
			int idx = repcol.getIndex();
			String title = repcol.getTitle();
			System.out.println("[DEBUG] " + key + ": [" + idx + "][" + title + "]" );
		}
	}
	
	protected void debugMsgMap(Map<String, String> msgMap) {
		debugMap(msgMap);
	}
	
	protected void debugMap(Map<String, String> map) {
		for ( String key : map.keySet() ) {
			String data = map.get( key );
			System.out.println("[DEBUG] " + key + ": " + data );
		}
	}
}
