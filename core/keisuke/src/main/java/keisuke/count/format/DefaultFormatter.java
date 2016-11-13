package keisuke.count.format;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.Util;
import keisuke.count.SCCommonDefine;

/**
 * カウント結果をフォーマットして出力するクラスです。
 * keisuke: パッケージの変更とprivateメソッドをprotectedへ変更
 * 　　　　　出力定形文言のproperties化
 */

public class DefaultFormatter extends AbstractFormatter {
	
	private final static int pathMinLen = 40;
	private final static int typeMaxLen = 12;
	private final static int categoryMaxLen = 20;
	private final static int execMaxLen = 6;
	private final static int blancMaxLen = 6;
	private final static int commentMaxLen = 6;
	private final static int sumMaxLen = 6;

	public byte[] format(CountResult[] results){

		long sumStep    = 0;
		long sumComment = 0;
		long sumNone    = 0;

		int maxFileLength = getFileLength(results);
		// ヘッダをフォーマット
		StringBuffer sb = new StringBuffer();
		//sb.append(fillOrCut("ファイル", maxFileLength));
		//sb.append("種類  カテゴリ            実行  空行  ｺﾒﾝﾄ  合計  ");
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_PATH), maxFileLength));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_TYPE), typeMaxLen));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_CATEGORY),categoryMaxLen));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_EXEC), execMaxLen));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_BLANC), blancMaxLen));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_COMMENT), commentMaxLen));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_SUM), sumMaxLen));	
		sb.append("\n");
		//sb.append(makeHyphen(maxFileLength));
		//sb.append("--------------------------------------------------");
		sb.append(makeHyphen(maxFileLength + typeMaxLen + categoryMaxLen
				+ execMaxLen + blancMaxLen + commentMaxLen + sumMaxLen));
		sb.append("\n");
		// １行ずつ処理を行う
		for(int i=0;i<results.length;i++){
			CountResult result = results[i];
			// 未対応のカウント結果をフォーマット
			if(result.getFileType()==null){
				sb.append(fillOrCut(result.getFileName(), maxFileLength));
				//sb.append("未対応");
				sb.append(getMessageText(SCCommonDefine.MSG_COUNT_FMT_UNDEF));
				sb.append("\n");
			// 正常にカウントされた結果をフォーマット
			} else {
//				String fileName = result.getFileName();
//				String fileType = result.getFileType();
				String step     = String.valueOf(result.getStep());
				String non      = String.valueOf(result.getNon());
				String comment  = String.valueOf(result.getComment());
				String sum      = String.valueOf(result.getStep() + result.getNon() + result.getComment());

				sb.append(fillOrCut(result.getFileName(), maxFileLength));
				sb.append(fillOrCut(result.getFileType(), typeMaxLen));
				sb.append(fillOrCut(result.getCategory(), categoryMaxLen));
				sb.append(leftFillOrCut(step    , execMaxLen));
				sb.append(leftFillOrCut(non     , blancMaxLen));
				sb.append(leftFillOrCut(comment , commentMaxLen));
				sb.append(leftFillOrCut(sum     , sumMaxLen));
				sb.append("\n");

				sumStep    += result.getStep();
				sumComment += result.getComment();
				sumNone    += result.getNon();
			}
		}
		// 合計行をフォーマット
		//sb.append(makeHyphen(maxFileLength));
		//sb.append("--------------------------------------------------");
		sb.append(makeHyphen(maxFileLength + typeMaxLen + categoryMaxLen
				+ execMaxLen + blancMaxLen + commentMaxLen + sumMaxLen));
		sb.append("\n");
		//sb.append(fillOrCut("合計", maxFileLength));
		sb.append(fillOrCut(getMessageText(SCCommonDefine.MSG_COUNT_FMT_TOTAL), maxFileLength));
		sb.append(makeSpace(typeMaxLen));
		sb.append(makeSpace(categoryMaxLen));
		sb.append(leftFillOrCut(String.valueOf(sumStep)   ,execMaxLen));
		sb.append(leftFillOrCut(String.valueOf(sumNone)   ,blancMaxLen));
		sb.append(leftFillOrCut(String.valueOf(sumComment),commentMaxLen));
		sb.append(leftFillOrCut(String.valueOf(sumStep + sumNone + sumComment),sumMaxLen));
		sb.append("\n");

		return sb.toString().getBytes();
	}

	/** ファイル名の長さに合わせた最大長を取得します（最小40） */
	protected int getFileLength(CountResult[] results) {
		int fileLength = pathMinLen;
		if (results == null || results.length == 0) {
			return fileLength;
		}
		for (CountResult result : results) {
			String fileName = result.getFileName();
			
			if (fileName != null) {
				int len = getDisplayWidth(fileName);
				if (fileLength < len) fileLength = len;
			}
		}
		return fileLength;
	}
	
	/** テキストの表示幅を計算します */
	protected int getDisplayWidth(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		}

		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			
			// ASCII・ヨーロッパ文字および HALFWIDTH のみ半角と判断
			if (c <= 0x00FF || (c >= 0xFF61 && c <= 0xFFDC) || (c >= 0xFFE8 && c <= 0xFFEE)) {
				len += 1;
			} else {
				len += 2;
			}
		}
		return len;
	}
	
	/** 指定された長さの半角スペースを作成します */
	protected String makeSpace(int width){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<width;i++){
			sb.append(" ");
		}
		return sb.toString();
	}

	/** 指定された長さの半角ハイフンを作成します */
	protected String makeHyphen(int width){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<width;i++){
			sb.append('-');
		}
		return sb.toString();
	}

	/**
	 * 文字列が指定の長さ未満であれば右側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 */
	protected String fillOrCut(String str,int width){
		int length = getDisplayWidth(str);
		if(length==width){
			return str;
		} else if(length < width){
			return str + makeSpace(width - length);
		} else {
			return Util.substring(str,width);
		}
	}

	/**
	 * 文字列が指定の長さ未満であれば左側をスペースで埋め、
	 * 指定の長さ以上であれば右側を切り落とします。
	 */
	protected String leftFillOrCut(String str,int width){
		int length = Util.getByteLength(str);
		if(length==width){
			return str;
		} else if(length < width){
			return makeSpace(width - length) + str;
		} else {
			return Util.substring(str,width);
		}
	}
}
