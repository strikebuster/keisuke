package keisuke.swing.diffcount;

import javax.swing.JLabel;

import keisuke.DiffStatusEnum;
import keisuke.DiffStatusLabels;
import keisuke.DiffStatusLabelsImpl;
import keisuke.MessageMap;
import keisuke.report.property.MessageDefine;

import static keisuke.count.diff.renderer.RendererConstant.MSG_DIFF_RND_DECREASE;
import static keisuke.count.diff.renderer.RendererConstant.MSG_DIFF_RND_INCREASE;
import static keisuke.count.diff.renderer.RendererConstant.MSG_DIFF_RND_STATUS;
import static keisuke.swing.diffcount.DiffCountGUIConstant.TOTAL_LABEL;

/**
 * Diff結果差分行数合計表示GUI部品
 */
class TotalSumElement {

	private MessageMap messageMap = null;
	private DiffStatusLabels diffStatLabels = null;
	private JLabel label;

	TotalSumElement(final MessageDefine msgdef) {
		if (msgdef != null) {
			this.messageMap = msgdef.getMessageMap();
			this.diffStatLabels = new DiffStatusLabelsImpl(msgdef);
		}
		this.label = new JLabel();
		this.label.setName(TOTAL_LABEL);
		this.resetTotal();
	}

	void resetTotal() {
		this.showTotal(null, 0, 0);
		this.label.setEnabled(false);
	}

	void showTotal(final DiffStatusEnum status, final long added, final long deleted) {
		StringBuffer sb = new StringBuffer();
		sb.append("Total: ");
		String statusTitle = "Status";
		String addedTitle = "Added Steps";
		String deletedTitle = "Deleted Steps";
		if (this.messageMap != null) {
			statusTitle = this.messageMap.get(MSG_DIFF_RND_STATUS);
			addedTitle = this.messageMap.get(MSG_DIFF_RND_INCREASE);
			deletedTitle = this.messageMap.get(MSG_DIFF_RND_DECREASE);
		}
		String statusText;
		if (status == null) {
			statusText = " ";
		} else if (this.diffStatLabels == null) {
			statusText = status.toString();
		} else {
			statusText = this.diffStatLabels.getLabelOf(status);
		}
		sb.append(statusTitle).append("[").append(statusText).append("] ");
		sb.append(addedTitle).append("[").append(added).append("] ");
		sb.append(deletedTitle).append("[").append(deleted).append("]");
		this.label.setText(sb.toString());
		this.label.setEnabled(true);
		//System.err.println("totalSum size=" + this.label.getPreferredSize().width);
	}

	JLabel label() {
		return this.label;
	}

}
