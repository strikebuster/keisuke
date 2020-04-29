package keisuke.count.step.format;

import static keisuke.util.StringUtil.LINE_SEP;

import java.io.UnsupportedEncodingException;

import keisuke.StepCountResult;
import keisuke.count.FormatEnum;
import keisuke.count.util.EncodeUtil;

/**
 * カウント結果をJSON形式でフォーマットします。
 */
public class JsonFormatter extends AbstractFormatter {

	JsonFormatter() {
		super(FormatEnum.JSON);
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * stepcounter互換とするためキー名は固定で踏襲する
	 */
	public byte[] format(final StepCountResult[] results) {
		if (results == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		boolean first = true;
		for (StepCountResult result : results) {
			if (first) {
				first = false;
			} else {
				sb.append(',');
			}
			sb.append(LINE_SEP);
			sb.append("\t{ \"name\": \"")
				.append(EncodeUtil.unicodeEscape(result.filePath())).append("\", ");
			sb.append("\"type\": \"")
				.append(EncodeUtil.unicodeEscape(this.getSourceType(result.sourceType())))
				.append("\"");
			if (result.sourceCategory() != null && !result.sourceCategory().isEmpty()) {
				sb.append(", \"category\": \"")
					.append(EncodeUtil.unicodeEscape(result.sourceCategory())).append("\"");
			}
			if (!result.isUnsupported()) {
				sb.append(", \"step\": ").append(Long.toString(result.execSteps()));
				sb.append(", \"none\": ").append(Long.toString(result.blancSteps()));
				sb.append(", \"comment\": ").append(Long.toString(result.commentSteps()));
				sb.append(", \"total\": ").append(Long.toString(result.sumSteps()));
			}
			sb.append(" }");
		}
		sb.append(LINE_SEP + "]" + LINE_SEP);
		try {
			// JSON data encoding must be UTF-8
			return sb.toString().getBytes(this.textEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
