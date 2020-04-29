package keisuke.count.step.format;

import static keisuke.util.StringUtil.LINE_SEP;

import java.io.UnsupportedEncodingException;

import keisuke.StepCountResult;
import keisuke.count.FormatEnum;
import keisuke.count.util.EncodeUtil;

/**
 * ステップ計測結果をCSV形式にフォーマットします。
 */

public class CSVFormatter extends AbstractFormatter {

	CSVFormatter() {
		super(FormatEnum.CSV);
	}

	/** {@inheritDoc} */
	public byte[] format(final StepCountResult[] results) {
		if (results == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < results.length; i++) {
			StepCountResult result = results[i];
			sb.append(result.filePath()).append(",");
			sb.append(this.getSourceType(result.sourceType())).append(",");
			sb.append(EncodeUtil.csvEscape(result.sourceCategory())).append(",");
			// 未対応の形式をフォーマット
			if (result.isUnsupported()) {
				sb.append(",,,");
			// 正常にカウントされたものをフォーマット
			} else {
				sb.append(Long.toString(result.execSteps())).append(",");
				sb.append(Long.toString(result.blancSteps())).append(",");
				sb.append(Long.toString(result.commentSteps())).append(",");
				sb.append(Long.toString(result.sumSteps()));
			}
			sb.append(LINE_SEP);
		}
		try {
			return sb.toString().getBytes(this.textEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
