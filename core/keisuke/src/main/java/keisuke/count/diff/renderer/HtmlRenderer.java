package keisuke.count.diff.renderer;

import static keisuke.count.diff.renderer.RendererConstant.*;
import static keisuke.util.StringUtil.LINE_SEP;
import static keisuke.util.StringUtil.SYSTEM_ENCODING;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import keisuke.count.SortOrderEnum;
import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFolderResult;
import keisuke.count.util.EncodeUtil;
import keisuke.util.DateUtil;


/**
 * 差分カウントの結果をHTML形式でレンダリングします。
 */
public class HtmlRenderer extends AbstractRenderer {

	/** {@inheritDoc} */
	public byte[] format(final DiffFolderResult result) {
		if (result == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<html>").append(LINE_SEP);
		sb.append("<head>").append(LINE_SEP);
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"")
			.append(SYSTEM_ENCODING).append("\">").append(LINE_SEP);
		sb.append("<title>").append(this.getMessageText(MSG_DIFF_RND_TITLE))
			.append("</title>").append(LINE_SEP);
		sb.append("<script type=\"text/javascript\">").append(LINE_SEP);
		sb.append("function switchDir(dirId){").append(LINE_SEP);
		sb.append("  var trList = document.getElementsByTagName('tr');").append(LINE_SEP);
		sb.append("  for(var i=0;i<trList.length;i++){").append(LINE_SEP);
		sb.append("    var className = trList[i].className;").append(LINE_SEP);
		sb.append("    if(className != null && className.indexOf(dirId) == 0){").append(LINE_SEP);
		sb.append("      if(trList[i].style.display == 'none'){").append(LINE_SEP);
		sb.append("        if(className == dirId){").append(LINE_SEP);
		sb.append("          trList[i].style.display = '';").append(LINE_SEP);
		sb.append("        }").append(LINE_SEP);
		sb.append("      } else {").append(LINE_SEP);
		sb.append("        trList[i].style.display = 'none';").append(LINE_SEP);
		sb.append("      }").append(LINE_SEP);
		sb.append("    }").append(LINE_SEP);
		sb.append("  }").append(LINE_SEP);
		sb.append("}").append(LINE_SEP);
		sb.append(LINE_SEP);
		sb.append("function showAll(){").append(LINE_SEP);
		sb.append("  var trList = document.getElementsByTagName('tr');").append(LINE_SEP);
		sb.append("  for(var i=0;i<trList.length;i++){").append(LINE_SEP);
		sb.append("    if(trList[i].className != ''){").append(LINE_SEP);
		sb.append("      trList[i].style.display = '';").append(LINE_SEP);
		sb.append("    }").append(LINE_SEP);
		sb.append("  }").append(LINE_SEP);
		sb.append("}").append(LINE_SEP);
		sb.append(LINE_SEP);
		sb.append("function hideAll(){").append(LINE_SEP);
		sb.append("  var trList = document.getElementsByTagName('tr');").append(LINE_SEP);
		sb.append("  for(var i=0;i<trList.length;i++){").append(LINE_SEP);
		sb.append("    if(trList[i].className != ''){").append(LINE_SEP);
		sb.append("      trList[i].style.display = 'none';").append(LINE_SEP);
		sb.append("    }").append(LINE_SEP);
		sb.append("  }").append(LINE_SEP);
		sb.append("}").append(LINE_SEP);
		sb.append("</script>").append(LINE_SEP);
		sb.append("</head>").append(LINE_SEP);
		sb.append("<body>").append(LINE_SEP);
		sb.append(this.getMessageText(MSG_DIFF_RND_TIME));
		sb.append("：").append(DateUtil.formatDate(new Date())).append(LINE_SEP);
		sb.append("<input type=\"button\" onclick=\"showAll();\" value=\"");
		sb.append(this.getMessageText(MSG_DIFF_RND_EXPAND)).append("\">");
		sb.append("<input type=\"button\" onclick=\"hideAll();\" value=\"");
		sb.append(this.getMessageText(MSG_DIFF_RND_HIDE)).append("\">");
		sb.append("<table border=\"1\" width=\"100%\">").append(LINE_SEP);
		sb.append("<tr><th width=\"70%\">");
		sb.append(this.getMessageText(MSG_DIFF_RND_PATH));
		sb.append("</th><th width=\"10%\">");
		sb.append(this.getMessageText(MSG_DIFF_RND_STATUS));
		sb.append("</th><th width=\"10%\">");
		sb.append(this.getMessageText(MSG_DIFF_RND_INCREASE));
		sb.append("</th><th width=\"10%\">");
		sb.append(this.getMessageText(MSG_DIFF_RND_DECREASE));
		sb.append("</th></tr>").append(LINE_SEP);
		for (AbstractDiffResultForCount obj : result.getSortedChildren()) {
			sb.append(this.renderLine(null, obj, 0));
		}
		sb.append("</table>").append(LINE_SEP);
		sb.append("</body>").append(LINE_SEP);
		sb.append("</html>").append(LINE_SEP);
		try {
			return sb.toString().getBytes(SYSTEM_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String renderLine(final DiffFolderResult parent, final AbstractDiffResultForCount obj,
			final int nest) {
		StringBuffer sb = new StringBuffer();
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
			sb.append(EncodeUtil.xmlEscape(obj.nodeName() + "/"));
			sb.append("</a>");
		} else {
			sb.append(EncodeUtil.xmlEscape(obj.nodeName()));
		}
		sb.append("</td>");

		sb.append("<td>");
		sb.append(EncodeUtil.xmlEscape(this.getStatusLabelOf(obj.status(), obj.isUnsupported())));
		sb.append("</td>");

		sb.append("<td style=\"text-align: right;\">");
		if (obj.isUnsupported()) {
			sb.append('-');
		} else {
			sb.append(obj.addedSteps());
		}
		sb.append("</td>");

		sb.append("<td style=\"text-align: right;\">");
		if (obj.isUnsupported()) {
			sb.append('-');
		} else {
			sb.append(obj.deletedSteps());
		}
		sb.append("</td>");

		sb.append("</tr>").append(LINE_SEP);

		if (obj instanceof DiffFolderResult) {
			DiffFolderResult folder = (DiffFolderResult) obj;
			List<AbstractDiffResultForCount> children = null;
			if (this.sortOrder() == SortOrderEnum.NODE) {
				children = folder.getSortedChildren();
			} else {
				children = folder.getChildren();
			}
			for (AbstractDiffResultForCount child : children) {
				sb.append(this.renderLine(folder, child, nest + 1));
			}
		}
		return sb.toString();
	}

	/** {@inheritDoc} */
	public boolean isText() {
		return true;
	}

	/** {@inheritDoc} */
	public String textEncoding() {
		return SYSTEM_ENCODING;
	}
}
