package keisuke.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.diffcount.DiffSource;


/**
 *  keisuke:追加クラス
 * 	Cobol用のステップカウンタ
 */
public class CobolCounter extends DefaultStepCounter {

	// 固定形式用定義
	// 固定形式コメント（7カラム目に特定文字）
	private List<String> fixLineComments = Arrays.asList("*", "/", "D", "$");
	// MicroFocus独自の固定形式コメント（1カラム目に*）
	private List<String> fixMFLineComments = Arrays.asList("*");
	
	// 自由形式用定義
	// 自由形式コメント
	private List<String> freeLineComments = Arrays.asList("*>", "^>>D ");
	// MicroFocus独自の自由形式コメント（左端の1文字に特定文字）
	private List<String> freeMFLineComments = Arrays.asList("*", "/", "D ", "$");
	
	// 共通定義
	// COBOLの文字列リテラル引用符
	private LiteralString cobolLiteralString = new LiteralString("'", "'", "''");
	// IBM独自の文字列リテラル引用符
	private LiteralString ibmCobolLiteralString = new LiteralString("\"", "\"", "\"\"");
	// 形式判定で利用するCOBOL文
	private static String COBOLFIXSTYLE = ">>SOURCE FORMAT IS FIXED";
	private static String COBOLFREESTYLE = ">>SOURCE FORMAT IS FREE";
	private static String COBOLFIRSTTOKEN = "IDENTIFICATION";
	
	private static int FIX_STYLE = 1;
	private static int FREE_STYLE = 2;
	private static int UNDEF_STYLE = 0;
	// 指定された形式を保持
	private int formatStyle = UNDEF_STYLE;

	/**
	 * コンストラクター
	 *
	 */
	public CobolCounter() {
		super();
		setFileType("COBOL");
		for (String ss : freeLineComments) {
			addLineComment(new LineComment(ss));
		}
		addLiteralString(cobolLiteralString);
		addLiteralString(ibmCobolLiteralString);
		setCaseInsenseTrue();
	}
	
	@Override
	public CountAndCutSource countOrCut(int ope, File file, String charset) throws IOException {
		String charSetName = charset;
		if (charSetName == null) {
			// キャラクタセット無指定の場合は
			// プラットフォームデフォルトキャラクタセットを指定する。
			charSetName = Charset.defaultCharset().name();
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), charSetName));

		String category = "";
		boolean ignore = false;
		// 正式なカウンタ変数
		long step    = 0;
		long non     = 0;
		long comment = 0;
		StringBuilder sb = new StringBuilder();
		// 仮の固定形式カウンタ変数
		long step1    = 0;
		long non1     = 0;
		long comment1 = 0;
		StringBuilder sb1 = new StringBuilder();
		// 仮の自由形式カウンタ変数
		long step2    = 0;
		long non2     = 0;
		long comment2 = 0;
		StringBuilder sb2 = new StringBuilder();
		// リターン値格納変数
		CountAndCutSource ccsObj = new CountAndCutSource();
		
		try {
			String line     = null;
	
			//System.out.println("[DEBUG] ### Processing file=" + file.getPath() + " ###"); 
			while((line = reader.readLine())!=null){
				if(category.length() == 0){
					Matcher matcher = CATEGORY_PATTERN.matcher(line);
					if(matcher.find()){
						category = matcher.group(1);
					}
				}
				if(IGNORE_PATTERN.matcher(line).find()){
					if (ope == OPE_CUT) {
						DiffSource ds = new DiffSource(null, ignore, category);
						ccsObj.setDiffSource(ds);
					}
					return ccsObj;
				}
				// styleが判定できるまで両方の形式でカウントし、判定後はいずれかのみでカウント
				if (formatStyle != FREE_STYLE) {
					// 固定形式と想定したカウント
					String rtrimedLine = rtrim(line); // 右端だけトリム
					String indicatorArea = " "; // 標識領域の初期化
					String abAreaLine = ""; // A領域＋B領域の初期化
					int lineLength = rtrimedLine.length();
					if (lineLength > 0 && checkFixMFLineComment(rtrimedLine)) {
						// MF独自形式のコメント行
						if (formatStyle == UNDEF_STYLE) {
							comment1++;
						} else {
							comment++;
						}
						//System.out.println("[DEBUG] Cmf#["+rtrimedLine+"]");
					} else if(lineLength > 6){
						indicatorArea =  rtrimedLine.substring(6,7).toUpperCase();
						if(lineLength > 7){
							abAreaLine = rtrimedLine.substring(7,lineLength).trim();
						}
						if(checkFixLineComment(indicatorArea)){
							// コメント行
							if (formatStyle == UNDEF_STYLE) {
								comment1++;
							} else {
								comment++;
							}
							//System.out.println("[DEBUG] Com#["+rtrimedLine+"]");
						} else {
							if (formatStyle == UNDEF_STYLE) {
								step1++;
							} else {
								step++;
							}
							if (ope == OPE_CUT) {
								if (formatStyle == UNDEF_STYLE) {
									sb1.append(abAreaLine + "\n");
								} else {
									sb.append(abAreaLine + "\n");
								}
							}
							//System.out.println("[DEBUG] Exe#["+rtrimedLine+"]");
							// style未定なら確認する
							if (formatStyle == UNDEF_STYLE && checkFirstProgramStep(abAreaLine)) {
								formatStyle = FIX_STYLE;
								// 仮カウントしていた固定形式のカウンタ値を採用
								step = step1;
								non = non1;
								comment = comment1;
								sb = sb1;
								// 仮カウントしていた自由形式のカウンタをリセット
								step2    = 0;
								non2     = 0;
								comment2 = 0;
								sb2 = new StringBuilder();
								continue;
							} else if (checkFormatChangeToFree(abAreaLine)) {
								formatStyle = FREE_STYLE;
								continue;
							}
						}
					} else {
						if (formatStyle == UNDEF_STYLE) {
							non1++;
						} else {
							non++;
						}
						//System.out.println("[DEBUG] Non#["+rtrimedLine+"]");
					}
				} 
				if (formatStyle != FIX_STYLE) {
					// 自由形式と想定したカウント
					// MicroFocus独自記法のコメント行かチェック
					if ( checkFreeMFLineComment(line)) {
						// MicroFocus独自記法のコメント行
						if (formatStyle == UNDEF_STYLE) {
							comment2++;
						} else {
							comment++;
						}
						//System.out.println("[DEBUG] Cmf!["+line+"]");
					} else {
						//　ANSI記法のコメント行かチェック
						String trimedLine = line.trim(); // 両端トリム
						if (checkBlancLine(trimedLine)){
							// 空白行なので飛ばす
							if (formatStyle == UNDEF_STYLE) {
								non2++;
							} else {
								non++;
							}
							//System.out.println("[DEBUG] Non!["+trimedLine+"]");
						} else {
							// 空白行ではない
							String focusedLine = removeFreeLineComment(trimedLine).trim();
							if (focusedLine.length() > 0) {
								// 有効行が残っている
								if (formatStyle == UNDEF_STYLE) {
									step2++;
								} else {
									step++;
								}
								if (ope == OPE_CUT) {
									if (formatStyle == UNDEF_STYLE) {
										sb2.append(focusedLine + "\n");
									} else {
										sb.append(focusedLine + "\n");
									}
								}
								//System.out.println("[DEBUG] Exe!["+focusedLine+"]");
								// style未定なら確認する
								if (formatStyle == UNDEF_STYLE && checkFirstProgramStep(focusedLine)) {
									formatStyle = FREE_STYLE;
									// 仮カウントしていた自由形式のカウンタ値を採用
									step = step2;
									non = non2;
									comment = comment2;
									sb = sb2;
									// 仮カウントしていた固定形式のカウンタをリセット
									step1    = 0;
									non1     = 0;
									comment1 = 0;
									sb1 = new StringBuilder();
									continue;
								} else if (checkFormatChangeToFix(focusedLine)) {
									formatStyle = FIX_STYLE;
									continue;
								}
							} else {
								// コメントが含まれていた
								if (formatStyle == UNDEF_STYLE) {
									comment2++;
								} else {
									comment++;
								}
								//System.out.println("[DEBUG] Com!["+trimedLine+"]");
							}						
						}
					}
				}			
			} // end of while
		} finally {
			reader.close();
		}
		if (formatStyle == UNDEF_STYLE) {
			// 形式不明のまま終了した場合（登録集ファイルなど）固定形式とみなす
			step = step1;
			non = non1;
			comment = comment1;
			sb = sb1;
		}
		if (ope == OPE_COUNT) {
			CountResult cs = null;
			cs =  new CountResult(file, file.getName(), getFileType(), category, step, non, comment);
			ccsObj.setCountResult(cs);
		} else {		
			String str = sb.toString();
			DiffSource ds = new DiffSource(str, ignore, category);
			ccsObj.setDiffSource(ds);
		}
		return ccsObj;
	}

	
	/** 固定形式　コメント行チェック */
	private boolean checkFixLineComment(String indicator) {
		if(fixLineComments.contains(indicator)){
			return true;
		} else {
			return false;
		}
	}
	
	/** 固定形式　MicroFocus独自コメント行チェック */
	private boolean checkFixMFLineComment(String line) {
		for (String ss : fixMFLineComments) {
			int lineLength = line.length();
			int sslen = ss.length();
			if (lineLength > ss.length()) {
				String head = line.substring(0,sslen);
				if ( head.equals(ss)) {
					// コメント行にマッチした
					return true;
				}
			} 
		}
		return false;
	}
	
	/** 自由形式　MicroFocus独自コメント行チェック */
	private boolean checkFreeMFLineComment(String line) {
		for (String ss : freeMFLineComments) {
			int lineLength = line.length();
			int sslen = ss.length();
			if (lineLength > ss.length()) {
				// ">>D "に対し大文字化とタブはスペースに置換して比較
				String head = line.substring(0,sslen).toUpperCase().replace('\t', ' ');
				if ( head.equals(ss)) {
					// コメント行にマッチした
					return true;
				}
			} 
		}
		return false;
	}
	
	/** 自由形式　コメント除去した行文字列を抽出 */
	private String removeFreeLineComment(String line) {
		return removeComments(line);
	}

	/** COBOL先頭行の判定 */
	private boolean checkFirstProgramStep(String line) {
		if (line.toUpperCase().startsWith(COBOLFIRSTTOKEN)){
			return true;
		}else {
			return false;
		}
	}
	
	/** COBOL自由形式→固定形式命令のチェック */
	private boolean checkFormatChangeToFix(String line) {
		if (line.toUpperCase().startsWith(COBOLFIXSTYLE)){
			return true;
		}else {
			return false;
		}
	}
	
	/** COBOL固定形式→自由形式命令のチェック */
	private boolean checkFormatChangeToFree(String line) {
		if (line.toUpperCase().trim().startsWith(COBOLFREESTYLE)){
			return true;
		}else {
			return false;
		}
	}
	
	/** 右端のみトリム */
	private String rtrim(String str) {
		if (str == null) {
	        return null;
	    }
	    
	    char[] val = str.toCharArray();
	    int len = val.length;
	    
	    while ((0 < len) && (val[len - 1] <= ' ')) {
	        len--;
	    }
	    
	    return (len<val.length) ? str.substring(0,len):str;
	}
	
}
