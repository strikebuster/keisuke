package keisuke.count.diff.renderer;

import java.util.Date;

import keisuke.count.SCCommonDefine;
import keisuke.count.diff.AbstractDiffResult;
import keisuke.count.diff.DiffCounterUtil;
import keisuke.count.diff.DiffFolderResult;


/**
 * 差分カウントの結果をHTML形式でレンダリングします。
 */
public class HTMLRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] render(final DiffFolderResult result) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("<title>");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_TITLE));
		sb.append("</title>\n");
		sb.append("<script type=\"text/javascript\">\n");
		sb.append("function switchDir(dirId){\n");
		sb.append("  var trList = document.getElementsByTagName('tr');\n");
		sb.append("  for(var i=0;i<trList.length;i++){\n");
		sb.append("    var className = trList[i].className;\n");
		sb.append("    if(className != null && className.indexOf(dirId) == 0){\n");
		sb.append("      if(trList[i].style.display == 'none'){\n");
		sb.append("        if(className == dirId){\n");
		sb.append("          trList[i].style.display = '';\n");
		sb.append("        }\n");
		sb.append("      } else {\n");
		sb.append("        trList[i].style.display = 'none';\n");
		sb.append("      }\n");
		sb.append("    }\n");
		sb.append("  }\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append("function showAll(){\n");
		sb.append("  var trList = document.getElementsByTagName('tr');\n");
		sb.append("  for(var i=0;i<trList.length;i++){\n");
		sb.append("    if(trList[i].className != ''){\n");
		sb.append("      trList[i].style.display = '';\n");
		sb.append("    }\n");
		sb.append("  }\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append("function hideAll(){\n");
		sb.append("  var trList = document.getElementsByTagName('tr');\n");
		sb.append("  for(var i=0;i<trList.length;i++){\n");
		sb.append("    if(trList[i].className != ''){\n");
		sb.append("      trList[i].style.display = 'none';\n");
		sb.append("    }\n");
		sb.append("  }\n");
		sb.append("}\n");
		sb.append("</script>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");

		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_TIME));
		sb.append("：").append(DiffCounterUtil.formatDate(new Date())).append("\n");
		sb.append("<input type=\"button\" onclick=\"showAll();\" value=\"");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_EXPAND));
		sb.append("\">");
		sb.append("<input type=\"button\" onclick=\"hideAll();\" value=\"");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_HIDE));
		sb.append("\">");

		sb.append("<table border=\"1\" width=\"100%\">\n");

		sb.append("<tr><th width=\"80%\">");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_PATH));
		sb.append("</th><th width=\"10%\">");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_STATUS));
		sb.append("</th><th width=\"10%\">");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_INCREASE));
		sb.append("</th><th>");
		sb.append(this.messageMap().get(SCCommonDefine.MSG_DIFF_RND_DECREASE));
		sb.append("</th></tr>\n");

		for (AbstractDiffResult obj : result.getSortedChildren()) {
			renderLine(null, obj, sb, 0);
		}

		sb.append("</table>\n");

		sb.append("</body>\n");
		sb.append("</html>\n");

		return sb.toString().getBytes();
	}

	private void renderLine(final DiffFolderResult parent, final AbstractDiffResult obj,
			final StringBuilder sb, final int nest) {
		if (obj instanceof DiffFolderResult && nest == 0) {
			sb.append("<tr>");
		} else {
			sb.append("<tr class=\"").append(parent.hashCodeName()).append("\" style=\"display: none;\">");
		}

		sb.append("<td>");
		for (int i = 0; i < nest; i++) {
			sb.append("&nbsp;");
		}
		if (obj instanceof DiffFolderResult) {
			sb.append("<a href=\"javascript:switchDir('").append(obj.hashCodeName()).append("')\">");
			sb.append(obj.getName() + "/");
			sb.append("</a>");
		} else {
			sb.append(obj.getName());
		}
		sb.append("</td>");

		sb.append("<td>");
		sb.append(obj.getStatus());
		sb.append("</td>");

		sb.append("<td style=\"text-align: right;\">");
		sb.append(obj.getAddCount());
		sb.append("</td>");

		sb.append("<td style=\"text-align: right;\">");
		sb.append(obj.getDelCount());
		sb.append("</td>");

		sb.append("</tr>\n");

		if (obj instanceof DiffFolderResult) {
			DiffFolderResult folder = (DiffFolderResult) obj;
			for (AbstractDiffResult child : folder.getSortedChildren()) {
				renderLine(folder, child, sb, nest + 1);
			}
		}
	}

}
