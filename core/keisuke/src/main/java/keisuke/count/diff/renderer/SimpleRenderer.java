package keisuke.count.diff.renderer;

import java.util.Date;

import keisuke.count.SCCommonDefine;
import keisuke.count.diff.DiffCounterUtil;
import keisuke.count.diff.DiffFolderResult;


/**
 * 差分カウントの結果をテキスト形式でレンダリングします。
 * keisuke: パッケージの変更と固定文言のproperties化
 */
public class SimpleRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] render(final DiffFolderResult result) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.messageDefine().getMessage(SCCommonDefine.MSG_DIFF_RND_TIME));
		sb.append("：");
		sb.append(DiffCounterUtil.formatDate(new Date())).append("\n");
		sb.append("--\n");
		sb.append(result.toString());

		return sb.toString().getBytes();
	}

}
