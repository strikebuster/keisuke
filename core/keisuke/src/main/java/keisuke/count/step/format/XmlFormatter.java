package keisuke.count.step.format;

//import static keisuke.count.step.format.FormatConstant.MSG_COUNT_FMT_UNDEF;
import static keisuke.util.StringUtil.LINE_SEP;

import java.io.UnsupportedEncodingException;

import keisuke.StepCountResult;
import keisuke.count.FormatEnum;
import keisuke.count.util.EncodeUtil;

/**
 * カウント結果をXML形式でフォーマットします。
 */
public class XmlFormatter extends AbstractFormatter {

	XmlFormatter() {
		super(FormatEnum.XML);
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
		sb.append("<?xml version=\"1.0\" encoding=\"");
		sb.append(this.textEncoding());
		sb.append("\"?>");
		sb.append(LINE_SEP);
		sb.append("<stepcounter>");
		sb.append(LINE_SEP);
		for (StepCountResult result : results) {
			sb.append("\t<file ");
			sb.append("name=\"");
			sb.append(EncodeUtil.xmlEscape(result.filePath()));
			sb.append("\" ");
			// 未対応の形式をフォーマット
			if (result.sourceType() == null || result.sourceType().isEmpty()) {
				sb.append("type=\"");
				//sb.append(this.getMessageText(MSG_COUNT_FMT_UNDEF));
				sb.append("unknown"); // stepcounter互換で固定
				sb.append("\" ");
			// 正常にカウントされたものをフォーマット
			} else {
				sb.append("type=\"");
				sb.append(EncodeUtil.xmlEscape(result.sourceType()));
				sb.append("\" ");
				if (result.sourceCategory() != null && !result.sourceCategory().isEmpty()) {
					sb.append("category=\"");
					sb.append(EncodeUtil.xmlEscape(result.sourceCategory()));
					sb.append("\" ");
				}
				sb.append("step=\"");
				sb.append(Long.toString(result.execSteps()));
				sb.append("\" ");
				sb.append("none=\"");
				sb.append(Long.toString(result.blancSteps()));
				sb.append("\" ");
				sb.append("comment=\"");
				sb.append(Long.toString(result.commentSteps()));
				sb.append("\" ");
				sb.append("total=\"");
				sb.append(Long.toString(result.sumSteps()));
				sb.append("\" ");
			}
			sb.append("/>");
			sb.append(LINE_SEP);
		}
		sb.append("</stepcounter>");
		sb.append(LINE_SEP);
		try {
			return sb.toString().getBytes(this.textEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
