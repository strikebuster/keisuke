package keisuke.count.step.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import keisuke.StepCountResult;
import keisuke.count.step.Formatter;
import keisuke.count.util.EncodeUtil;

/**
 * カウント結果をJSON形式でフォーマットします。
 */
public class JsonFormatter implements Formatter {

	/** {@inheritDoc} */
	public byte[] format(final StepCountResult[] results) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			writer.append("[");
			boolean first = true;
			for (StepCountResult result : results) {
				if (first) {
					writer.append("\n");
				} else {
					writer.append(",\n");
				}
				writer.append("\t{ ");
				writer.append("\"name\": \"");
				writer.append(EncodeUtil.unicodeEscape(result.filePath()));
				writer.append("\", ");
				// 未対応の形式をフォーマット
				if (result.sourceType() == null || result.sourceType().length() == 0) {
					writer.append("\"type\": \"unknown\"");
				// 正常にカウントされたものをフォーマット
				} else {
					writer.append("\"type\": \"");
					writer.append(EncodeUtil.unicodeEscape(result.sourceType()));
					writer.append("\", ");
					if (result.sourceCategory() != null && result.sourceCategory().length() > 0) {
						writer.append("\"category\": \"");
						writer.append(EncodeUtil.unicodeEscape(result.sourceCategory()));
						writer.append("\", ");
					}

					writer.append("\"step\": ")
						.append(Long.toString(result.execSteps()))
						.append(", ");
					writer.append("\"none\": ")
						.append(Long.toString(result.blancSteps()))
						.append(", ");
					writer.append("\"comment\": ")
						.append(Long.toString(result.commentSteps()))
						.append(", ");
					writer.append("\"total\": ")
						.append(Long.toString(result.sumSteps()))
						.append(" ");
				}
				writer.append("}");

				first = false;
			}
			writer.write("\n]\n");
			writer.flush();
		} catch (IOException e) {
			// not happen
			e.printStackTrace();
		}
		return out.toByteArray();
	}

}
