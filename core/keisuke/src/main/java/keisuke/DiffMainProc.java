package keisuke;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DiffMainProc extends AbstractMainProc {
	
	private final static int DIFF_ADDEDIDX = 0; // 新規追加ファイルのグループ
	private final static int DIFF_MODIFIEDIDX = 1; // 修正ファイルのグループ
	private final static int DIFF_DROPEDIDX = 2; // 廃止ファイルのグループ
	private final static int DIFF_UNCHANGEDIDX= 3; // 変更なしファイルのグループ
	private final static int DIFF_UNSUPPORTEDIDX= 4; // 対象外ファイルのグループ
	private String statusArray[] = null;
	private Map<String, DiffCountElement[]> langMap = null;
	private boolean unchangeByLang = true;
	private int unchangeFiles = 0; // 変更なしファイルの合計本数
	private int ignoreFiles = 0; // 計測対象外ファイル本数
	private String aoutfile = null; // 新規ファイルリスト出力ファイル
	public String aoutText = null;
	private String moutfile = null; // 修正ファイルリスト出力ファイル
	public String moutText = null;
	
	public DiffMainProc() {
		createBindedFuncs(CommonDefine.DIFFPROC);
	}

	public void main(String[] args) {
		this.argMap = this.argFunc.makeMapOfArgs(args);
		if (this.argMap == null) {
			return;
		}
		//debugArgMap(argMap);
		String pfile = this.argMap.get(CommonDefine.OPT_PROP);
		PropertyDefine propDef = new PropertyDefine();
		if (pfile != null) {
			propDef.customizePropertyDefine(pfile);
		}
		this.columnMap = propDef.getDiffProperties();
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
		
		String infile = this.argMap.get(DiffArgFunc.ARG_INPUT);
		//if (infile == null) {
		//	throw new RuntimeException("!! Input file is not specified.");
		//}
		String aout = this.argMap.get(DiffArgFunc.OPT_AOUT);
		if (aout != null) {
			this.aoutfile = aout;
		}
		String mout = this.argMap.get(DiffArgFunc.OPT_MOUT);
		if (mout != null) {
			this.moutfile = mout;
		}
		String unch = this.argMap.get(DiffArgFunc.OPT_UNCHANGE);
		if (unch != null && ! unch.equals(DiffArgFunc.OPTVAL_DETAIL)) {
			this.unchangeByLang = false;
		}
		
		prepareLangMap();
		aggregateDiff(infile);
		reportDiff();
		writeOutputDiff();
		writeOutput();
	}
	
	private void prepareLangMap() {
		this.langMap = new TreeMap<String, DiffCountElement[]>();
		List<String> list = this.classifyType.getClassifyFixedList();
		if (list == null || list.size() < 1) {
			return;
		}
		for (String key : list) {
			this.langMap.put(key, createDiffCountElementArray(key));
		}
	}
	
	private void aggregateDiff(String infile) {
		BufferedReader reader = null;	
		StringBuilder sbaout = new StringBuilder();
		StringBuilder sbmout = new StringBuilder();
		int alinectr = 0;
		int mlinectr = 0;
		try {
			// DiffCount結果判定用の配列準備
			this.statusArray = createDiffCountStatusArray();
			// 入力ファイル：StepCounter.DiffCountのTXT形式出力
			if (infile == null) {
				reader = new BufferedReader(new InputStreamReader(System.in));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(infile))));
			}
			int linectr = 0;
			String line = null;
			// 無効な冒頭の行をスキップ
			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr += 1;
				if (line.equals("--")) {
					//System.out.println("[DEBUG] skip lines in diffcount output = " + linectr );
					break;
				}
			}
			int dirDepth = 0;
			List<String> pathNodeList = new ArrayList<String>();
			
			while ((line = reader.readLine()) != null) {
				// ファイル1行毎の処理
				linectr++;
				int pos = line.indexOf("/[");
				if (pos < 0) {
					// 	ファイルの処理
					// 変更なしファイルを言語別集計しないならループ内スキップ
			        if (this.unchangeByLang == false && line.indexOf("["+this.statusArray[DIFF_UNCHANGEDIDX]+"] +0 -0") >= 0) {
			        	this.unchangeFiles++;
			            //System.out.println("[DEBUG] Unchange: " + line);
			            continue;
			        } else if (line.indexOf("["+this.statusArray[DIFF_UNSUPPORTEDIDX]+"] +0 -0") >= 0) {
			        	// サポート対象外ファイルをループ内スキップ
			        	ignoreFiles++;
			            //System.out.println("[DEBUG] Unsupport: " + line);
			            continue;
			        }
			        // 空白文字の数で階層の深さを測る
					byte[] chararray= line.getBytes();
					int spcnt = 0;
			        for (int i=0; i<chararray.length; i++) {
			        	if (chararray[i] == ' ') {
			            	spcnt++;
			        	} else {
			            	break;
			        	}
			        }
			        // ファイル名と差分の増減数を取得
			        boolean errflag = false;
			        String fname = "";
			        String stat = "";
			        int numadd = 0;
			        int numdel = 0;
			        int pos2 = line.indexOf("[");
			        if (pos2 > 0) {
			        	fname = line.substring(0,pos2).trim();
			        	pos2++;
			        	int pos3 = line.indexOf("]", pos2);
			        	if (pos3 > 0) {
			        		stat = line.substring(pos2, pos3);
			        		pos3++;
			        		String strwk = line.substring(pos3).trim();
				        	String strarray[] = strwk.split(" ");
				        	if (strarray.length >= 2) {
				        		String stradd = strarray[0];
				        		String strdel = strarray[1];
				        		try {
				        			numadd = Integer.parseInt(stradd);
				        			numdel = Integer.parseInt(strdel);
				        		} catch (NumberFormatException nfe) {
				        			//System.out.println("[DEBUG] NumberFormatException at " + strwk);
				        			errflag = true;
				        		}
				        	} else {
				        		//System.out.println("[DEBUG] not found ' ' in " + strwk);
				        		errflag = true;
				        	}
			        	} else {
			        		//System.out.println("[DEBUG] not found ']' in " + line);
			        		errflag = true;
			        	}
			        } else {
			        	//System.out.println("[DEBUG] not found '[' in " + line);
		        		errflag = true;
		        	}
			        if (errflag) {
			        	// 行の解析ができなかった
			        	System.err.println("![WARN] fail to parse line = " + line);
			        	continue;
			        }
			        // ファイル名でパスリストを更新
			        if (spcnt == 0) {
			        	System.err.println("![WARN] illegal path depth = " + line);
			        } else {
			            for (int i=0; spcnt + i <= dirDepth; i++) {
			                pathNodeList.remove(pathNodeList.size() - 1);
			            }
			            pathNodeList.add(fname);
			        }
			        dirDepth = spcnt;
			        //System.out.println("[DEBUG] Depth = " + dirDepth + " : " + pathNodeList);
			        // 言語（拡張子）のマッチング処理
			        // パス文字列作成
			        String strpath= "";
			        for (String node : pathNodeList) {
			        	strpath += node;
			        }
			        //System.out.println("[DEBUG] path = " + strpath + " [" + stat + "] (add)" + numadd + " (del)" + numdel);
					int idx = whichDiffCountStatus(stat);
					if (idx == DIFF_ADDEDIDX) {
						if (this.aoutfile != null) {
							// 新規ファイルリスト出力ON
							sbaout.append("/"); //StepCounterの仕様に合わせ先頭に"/"
							sbaout.append(strpath);
							sbaout.append("\n");
							alinectr++;
						}		
					} else if (idx == DIFF_MODIFIEDIDX) {
						if (this.moutfile != null) {
							// 修正ファイルリスト出力ON
							sbmout.append("/"); //StepCounterの仕様に合わせ先頭に"/"
							sbmout.append(strpath);
							sbmout.append("\n");
							mlinectr++;
						}
					} else if (idx == DIFF_DROPEDIDX) {
						// 処理なし
					} else if (idx == DIFF_UNCHANGEDIDX) { // 変更なし				
						this.unchangeFiles++;
					} else if (idx == DIFF_UNSUPPORTEDIDX) { // サポート対象外
						this.ignoreFiles++;
						continue;
					} else {
						System.err.println("![WARN] unknown status in " + line);
						continue;
					}
					// 拡張子の取得
			        //String strext = FilenameUtils.getExtension(fname);
			        String classify = this.classifyType.getClassifyName(strpath);
					DiffCountElement dce = new DiffCountElement(classify, numadd, numdel);
					// 言語種類での集計
					DiffCountElement dcearray[];
					if ( ! this.langMap.containsKey(classify) ) {
						// 新しい拡張子用にキーを追加
						dcearray = createDiffCountElementArray(classify);
					} else {
						dcearray = this.langMap.get(classify);
					}	
					dcearray[idx].add(dce);
					this.langMap.put(classify, dcearray);
				} else {
					// ディレクトリの処理
					// 空白文字の数で階層の深さを測る
					byte[] chararray= line.getBytes();
					int spcnt = 0;
			        for (int i=0; i<chararray.length; i++) {
			        	if (chararray[i] == ' ') {
			            	spcnt++;
			        	} else {
			            	break;
			        	}
			        }
			        // ディレクトリ名を取得
			        int pos2 = pos + 1; // "/["の位置がposで"/"まで取り出す
			        String dname = line.substring(0,pos2).trim();
			        // ディレクトリ名でパスリストを更新
			        if (spcnt == 0) {
			            pathNodeList.add(dname);
			        } else {
			            for (int i=0; spcnt + i <= dirDepth; i++) {
			                pathNodeList.remove(pathNodeList.size() - 1);
			            }
			            pathNodeList.add(dname);
			        }
			        dirDepth = spcnt;
			        //System.out.println("[DEBUG] Depth = " + dirDepth + " : " + pathNodeList);
				}
			}
			this.aoutText = sbaout.toString();
			this.moutText = sbmout.toString();
			//System.out.println("[DEBUG] Input Lines = " + linectr);
			//System.out.println("[DEBUG] AOut Lines = " + alinectr);
			//System.out.println("[DEBUG] MOut Lines = " + mlinectr);
		} catch (IOException e) {
			System.err.println("!! Read error : " + infile);
			throw new RuntimeException(e);
		} finally {
			if (reader != null && infile != null) try { reader.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	private String[] createDiffCountStatusArray() {
		String array[] = new String[5];
		array[DIFF_ADDEDIDX] = this.messageMap.get(CommonDefine.MSG_DIFF_STATUS_ADD);
		array[DIFF_MODIFIEDIDX] = this.messageMap.get(CommonDefine.MSG_DIFF_STATUS_MODIFY);
		array[DIFF_DROPEDIDX] = this.messageMap.get(CommonDefine.MSG_DIFF_STATUS_DROP);
		array[DIFF_UNCHANGEDIDX] = this.messageMap.get(CommonDefine.MSG_DIFF_STATUS_UNCHANGE);
		array[DIFF_UNSUPPORTEDIDX] = this.messageMap.get(CommonDefine.MSG_DIFF_STATUS_UNSUPPORT);
		return array;
	}
	
	private int whichDiffCountStatus(String stat) {
		if (stat == null) {
			return -1;
		}
		for (int i=0; i < this.statusArray.length; i++) {
			if (stat.equals(this.statusArray[i])) {
				return i;
			}
		}
		return -1;
	}
	
	private DiffCountElement[] createDiffCountElementArray(String ext) {
		DiffCountElement array[] = new DiffCountElement[4];
		array[DIFF_ADDEDIDX] = new DiffCountElement(ext);
		array[DIFF_MODIFIEDIDX] = new DiffCountElement(ext);
		array[DIFF_DROPEDIDX] = new DiffCountElement(ext);
		array[DIFF_UNCHANGEDIDX] = new DiffCountElement(ext);
		return array;
	}
	
	private void reportDiff() {
		this.reportFunc.setColumnOrder(this.columnMap);
		StringBuilder sb = new StringBuilder();
		String line;
		// 追加ファイル
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_DIFF_SUBJECT_ADD));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportFunc.getColumnTitles();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for ( String langkey : this.langMap.keySet() ) {
			DiffCountElement array[] = this.langMap.get(langkey);
			DiffCountElement elem = array[DIFF_ADDEDIDX];
			// 列毎の値を出力
			String langlabel = this.classifyType.getClassifyNameForReport(langkey);
			line = this.reportFunc.getColumnValues(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}
		// 修正ファイル
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_DIFF_SUBJECT_MODIFY));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportFunc.getColumnTitles();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for ( String langkey : langMap.keySet() ) {
			DiffCountElement array[] = langMap.get(langkey);
			DiffCountElement elem = array[DIFF_MODIFIEDIDX];
			// 列毎の値を出力
			String langlabel = this.classifyType.getClassifyNameForReport(langkey);
			line = this.reportFunc.getColumnValues(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}
		
		// 廃止ファイル
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_DIFF_SUBJECT_DROP));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportFunc.getColumnTitles();
		sb.append(line);
		sb.append("\n");
		// 言語毎の集計結果の出力
		for ( String langkey : langMap.keySet() ) {
			DiffCountElement array[] = langMap.get(langkey);
			DiffCountElement elem = array[DIFF_DROPEDIDX];
			// 列毎の値を出力
			String langlabel = this.classifyType.getClassifyNameForReport(langkey);
			line = this.reportFunc.getColumnValues(langlabel, elem);
			sb.append(line);
			sb.append("\n");
		}
		
		// 修正なしファイル
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_DIFF_SUBJECT_UNCHANGE));
		sb.append("\n");
		// 列タイトルの出力
		line = this.reportFunc.getOnlyFilesNumTitles();
		sb.append(line);
		sb.append("\n");
		// オプションで出力レベルを指定されている
		if (this.unchangeByLang) {
			// 言語毎の集計結果の出力 -nochange detail
			for ( String langkey : langMap.keySet() ) {
				DiffCountElement array[] = langMap.get(langkey);
				DiffCountElement elem = array[DIFF_UNCHANGEDIDX];
				// 列毎の値を出力
				String langlabel = this.classifyType.getClassifyNameForReport(langkey);
				line = this.reportFunc.getOnlyFilesNumColumnValues(langlabel, elem);
				sb.append(line);
				sb.append("\n");
			}
		} else {
			// 言語区別なしで合計の出力 -nochange total
			sb.append("ALL , ");
			sb.append(this.unchangeFiles);
			//sb.append(" , 0 , 0 , 0\n");
			sb.append("\n");
		}
		// 計測対象外ファイル
		// 出力グループの表題
		sb.append(this.messageMap.get(CommonDefine.MSG_DIFF_SUBJECT_UNSUPPORT));
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
	
	private void writeOutputDiff() {
		if (this.aoutfile != null) {
			BufferedWriter writer = null;
			try {
				// 出力ファイル
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(this.aoutfile))));
				writer.write(this.aoutText);

			} catch (IOException e) {
				System.err.println("!! Write error : " + this.aoutfile);
				throw new RuntimeException(e);
			} finally {
				if (writer != null) try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
			}
		}
		if (this.moutfile != null) {
			BufferedWriter writer = null;
			try {
				// 出力ファイル
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(this.moutfile))));
				writer.write(this.moutText);

			} catch (IOException e) {
				System.err.println("!! Write error : " + this.moutfile);
				throw new RuntimeException(e);
			} finally {
				if (writer != null) try { writer.close(); } catch (IOException e) { e.printStackTrace(); }
			}
		}
	}
}
