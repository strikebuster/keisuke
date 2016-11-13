package keisuke.count.format;

import jp.sf.amateras.stepcounter.CountResult;
import keisuke.count.SCCommonDefine;

/**
 * カウント結果をCSV形式でフォーマットします。
 * keisuke: パッケージの変更とprivateメソッドをprotectedへ変更
 * 　　　　　出力定形文言のproperties化
 */

public class CSVFormatter extends AbstractFormatter {

//	private String fileHeader    = "ファイル";
//	private String stepHeader    = "実行";
//	private String nonHeader     = "空行";
//	private String commentHeader = "ｺﾒﾝﾄ";
//	private String typeHeader    = "種類";
//	private String sumHeader     = "合計";
	
	public byte[] format(CountResult[] results){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<results.length;i++){
			CountResult result = results[i];
			// 未対応の形式をフォーマット
			if(result.getFileType()==null){
				sb.append(result.getFileName());
				sb.append(",");
				//sb.append("未対応");
				sb.append(getMessageText(SCCommonDefine.MSG_COUNT_FMT_UNDEF));
				sb.append(",");
				sb.append(",");
				sb.append(",");
				sb.append(",");
				sb.append(",");
				sb.append("\n");
			// 正常にカウントされたものをフォーマット
			} else {
				sb.append(result.getFileName());
				sb.append(",");
				sb.append(result.getFileType());
				sb.append(",");
				sb.append(result.getCategory());
				sb.append(",");
				sb.append(result.getStep());
				sb.append(",");
				sb.append(result.getNon());
				sb.append(",");
				sb.append(result.getComment());
				sb.append(",");
				sb.append(result.getStep()+result.getNon()+result.getComment());
				sb.append("\n");
			}
		}
		return sb.toString().getBytes();
	}
}
