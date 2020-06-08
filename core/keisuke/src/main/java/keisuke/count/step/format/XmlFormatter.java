package keisuke.count.step.format;

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
		sb.append("<?xml version=\"1.0\" encoding=\"").append(this.textEncoding())
			.append("\"?>").append(LINE_SEP);
		sb.append("<stepcounter>").append(LINE_SEP);
		for (StepCountResult result : results) {
			sb.append("\t<file ");
			sb.append("name=\"").append(EncodeUtil.xmlEscape(result.filePath())).append("\" ");
			sb.append("type=\"")
				.append(EncodeUtil.xmlEscape(this.getSourceType(result.sourceType()))).append("\" ");
			if (result.sourceCategory() != null && !result.sourceCategory().isEmpty()) {
				sb.append("category=\"")
					.append(EncodeUtil.xmlEscape(result.sourceCategory())).append("\" ");
			}
			if (!result.isUnsupported()) {
				sb.append("code=\"").append(Long.toString(result.execSteps())).append("\" ");
				sb.append("blank=\"").append(Long.toString(result.blancSteps())).append("\" ");
				sb.append("comment=\"").append(Long.toString(result.commentSteps())).append("\" ");
				sb.append("total=\"").append(Long.toString(result.sumSteps())).append("\" ");
			}
			sb.append("/>").append(LINE_SEP);
		}
		sb.append("</stepcounter>").append(LINE_SEP);
		try {
			return sb.toString().getBytes(this.textEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
