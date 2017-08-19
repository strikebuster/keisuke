package keisuke.count.format;

import jp.sf.amateras.stepcounter.CountResult;
import keisuke.count.SCCommonDefine;

/**
 * ステップ計測結果をCSV形式にフォーマットします。
 */

public class CSVFormatter extends AbstractFormatter {

	/** {@inheritDoc} */
	public byte[] format(final CountResult[] results) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < results.length; i++) {
			CountResult result = results[i];
			// 未対応の形式をフォーマット
			if (result.getFileType() == null) {
				sb.append(result.getFileName());
				sb.append(",");
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
				sb.append(result.getStep() + result.getNon() + result.getComment());
				sb.append("\n");
			}
		}
		return sb.toString().getBytes();
	}
}
