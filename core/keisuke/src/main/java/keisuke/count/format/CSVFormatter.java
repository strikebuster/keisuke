package keisuke.count.format;

import static keisuke.count.format.FormatConstant.*;

import keisuke.StepCountResult;

/**
 * ステップ計測結果をCSV形式にフォーマットします。
 */

public class CSVFormatter extends AbstractFormatter {

	/** {@inheritDoc} */
	public byte[] format(final StepCountResult[] results) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < results.length; i++) {
			StepCountResult result = results[i];
			// 未対応の形式をフォーマット
			if (result.sourceType() == null) {
				sb.append(result.filePath());
				sb.append(",");
				sb.append(this.getMessageText(MSG_COUNT_FMT_UNDEF));
				sb.append(",");
				sb.append(",");
				sb.append(",");
				sb.append(",");
				sb.append(",");
				sb.append("\n");
			// 正常にカウントされたものをフォーマット
			} else {
				sb.append(result.filePath());
				sb.append(",");
				sb.append(result.sourceType());
				sb.append(",");
				sb.append(result.sourceCategory());
				sb.append(",");
				sb.append(result.execSteps());
				sb.append(",");
				sb.append(result.blancSteps());
				sb.append(",");
				sb.append(result.commentSteps());
				sb.append(",");
				sb.append(result.sumSteps());
				sb.append("\n");
			}
		}
		return sb.toString().getBytes();
	}
}
