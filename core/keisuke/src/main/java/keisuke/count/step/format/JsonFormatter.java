package keisuke.count.step.format;

//import static keisuke.count.step.format.FormatConstant.MSG_COUNT_FMT_UNDEF;
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
			sb.append("\t{ \"name\": \"");
			sb.append(EncodeUtil.unicodeEscape(result.filePath()));
			sb.append("\", ");
			// 未対応の形式をフォーマット
			if (result.sourceType() == null || result.sourceType().length() == 0) {
				//sb.append("\"type\": \"unknown\"");
				sb.append("\"type\": \"");
				//sb.append(this.getMessageText(MSG_COUNT_FMT_UNDEF));
				sb.append("unknown"); // stepcounter互換で固定
				sb.append("\"");
			// 正常にカウントされたものをフォーマット
			} else {
				sb.append("\"type\": \"");
				sb.append(EncodeUtil.unicodeEscape(result.sourceType()));
				sb.append("\", ");
				if (result.sourceCategory() != null && result.sourceCategory().length() > 0) {
					sb.append("\"category\": \"");
					sb.append(EncodeUtil.unicodeEscape(result.sourceCategory()));
					sb.append("\", ");
				}

				sb.append("\"step\": ");
				sb.append(Long.toString(result.execSteps()));
				sb.append(", \"none\": ");
				sb.append(Long.toString(result.blancSteps()));
				sb.append(", \"comment\": ");
				sb.append(Long.toString(result.commentSteps()));
				sb.append(", \"total\": ");
				sb.append(Long.toString(result.sumSteps()));
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
