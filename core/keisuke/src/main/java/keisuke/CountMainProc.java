package keisuke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CountMainProc extends AbstractMainProc {
	
	private Map<String, CountElement> langMap = null;
	private int ignoreFiles = 0; // 計測対象外ファイル本数
	
	public CountMainProc() {
		createBindedFuncs(CommonDefine.COUNTPROC);
	}

	public void main(String[] args) {
		this.argMap = this.argFunc.makeMapOfArgs(args);
		if (this.argMap == null) {
			return;
		}
		//debugArgMap(argMap);
		String pfile = argMap.get(CommonDefine.OPT_PROP);
		PropertyDefine propDef = new PropertyDefine();
		if (pfile != null) {
			propDef.customizePropertyDefine(pfile);
		}
		this.columnMap = propDef.getCountProperties();
		this.messageMap = propDef.getMessageProperties();
		//debugColMap(columnMap);
		//debugMsgMap(messageMap);
		
		String ctype = this.argMap.get(CommonDefine.OPT_CLASS);
		if (ctype == null ||  !(ctype.equals(CommonDefine.OPTVAL_LANGUAGE)
				|| ctype.equals(CommonDefine.OPTVAL_LANGGROUP) || ctype.startsWith(CommonDefine.OPTVAL_FW))) {
			ctype = CommonDefine.OPTVAL_EXTENSION;
		}
		String xfile = this.argMap.get(CommonDefine.OPT_XML);
		if (xfile != null) {
			this.classifyType = ClassifyFuncFactory.createClassifyFunc(ctype, xfile);
		} else {
			this.classifyType = ClassifyFuncFactory.createClassifyFunc(ctype);
		}
		
		String infile = this.argMap.get(CountArgFunc.ARG_INPUT);
		//if (infile == null) {
		//	throw new RuntimeException("!! Input file is not specified.");
		//}
		
		prepareLangMap();
		aggregateCount(infile);
		reportCount();
		writeOutput();
	}
	
	private void prepareLangMap() {
		this.langMap = new TreeMap<String, CountElement>();
		List<String> list = this.classifyType.getClassifyFixedList();
		if (list == null || list.size() < 1) {
			return;
		}
		for (String key : list) {
			this.langMap.put(key, new CountElement(key));
		}
	}
	
	private void aggregateCount(String infile) {
		BufferedReader reader = null;
		String line = null;
		try {
			// 入力ファイル：stepcounterのCSV形式出力
			if (infile == null) {
				reader = new BufferedReader(new InputStreamReader(System.in));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(infile))));
			}
			int linectr = 0;
			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr++;
				line = line.trim();
				// 列要素分解
				String strArray[] = line.split(",");
				int salen = strArray.length;
				int skipcols = 0 ;
				String strpath = "";
				//String strext = "";
				String strcategory = "";
				String strexestep = "0";
				String strempstep = "0";
				String strcomstep = "0";
				String strsumstep = "0";
				if (salen > 7) {
					// StepCounterの出力でカテゴリ文字列内に","が含まれる場合の回避
					skipcols = salen - 7;
				}
				if (salen > 6) {
					strpath = strArray[0];
					// 拡張子の取得
					//strext = strlist[1];
					//strext = FilenameUtils.getExtension(strpath);
					strcategory = strArray[2];
					for  (int i = 0; i < skipcols; i++) {
						strcategory += "," + strArray[3+i];
					}
					//System.out.println("[DEBUG] CATEGORY :" + strcategory);
					strexestep = strArray[3+skipcols];
					strempstep = strArray[4+skipcols];
					strcomstep = strArray[5+skipcols];
					strsumstep = strArray[6+skipcols];
				} else {
					//System.out.println("[DEBUG] ignore line : " + line);
					this.ignoreFiles++;
					continue;
				}
				// 行数の数値化
				int numexe = -1;
				int numemp = -1;
				int numcom = -1;
				int numall = -1;
				try {
					if (! strexestep.isEmpty()) {
						numexe = Integer.parseInt(strexestep);
					}
					if (! strempstep.isEmpty()) {
						numemp = Integer.parseInt(strempstep);
					}
					if (! strcomstep.isEmpty()) {
						numcom = Integer.parseInt(strcomstep);
					}
					if (! strsumstep.isEmpty()) {
						numall = Integer.parseInt(strsumstep);
					}
				} catch (NumberFormatException e) {
					System.out.println("![WARN] Illegal format for number : " + line);
				}
				// 数値のない解析対象外ファイルはスキップ
			    if (numexe < 0 || numemp < 0 || numcom < 0 || numall < 0) {
			    	//System.out.println("[DEBUG] ignore line : " + line);
			    	this.ignoreFiles++;
			        continue;
			    }
			    // 対象言語分類の取得
			    String classify = this.classifyType.getClassifyName(strpath);
			    CountElement ce = new CountElement(classify, numexe, numemp, numcom, numall);
			    // 言語種類での集計
			    if ( this.langMap.containsKey(classify)) {
			        CountElement sumce = langMap.get(classify);
			        sumce.add(ce);
			        this.langMap.put(classify, sumce);
			    } else {
			    	// 新しい拡張子用にキーを追加
			    	this.langMap.put(classify, ce);
			    }
			}
			//System.out.println("[DEBUG] Input Lines = " + linectr);
		} catch (IOException e) {
			System.err.println("!! Read error : " + infile);
			throw new RuntimeException(e);
		} finally {
			if (reader != null && infile != null) try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	private void reportCount() {
		this.reportFunc.setColumnOrder(this.columnMap);
		StringBuilder sb = new StringBuilder();
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_COUNT_SUBJECT_HEAD));
		sb.append("\n");
		// 列タイトルの出力
		String line = this.reportFunc.getColumnTitles();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for ( String langkey : this.langMap.keySet() ) {
			CountElement elem = this.langMap.get(langkey);
			// 列毎の値を出力
			String langlabel = this.classifyType.getClassifyNameForReport(langkey);
			line = this.reportFunc.getColumnValues(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_COUNT_SUBJECT_UNSUPPORT));
		sb.append("\n");
		line = this.reportFunc.getOnlyFilesNumTitles();
		sb.append(line);
		sb.append("\n");
		sb.append("ALL , ");
		sb.append(this.ignoreFiles);
		sb.append("\n");
		// 出力結果の保管
		this.reportOutput = sb.toString();
	}

}
