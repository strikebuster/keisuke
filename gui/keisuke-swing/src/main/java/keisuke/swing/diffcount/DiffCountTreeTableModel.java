package keisuke.swing.diffcount;

import keisuke.DiffCountResult;
import keisuke.DiffStatusLabels;
import keisuke.DiffStatusLabelsImpl;
import keisuke.MessageMap;
import keisuke.count.diff.AbstractDiffResultForCount;
import keisuke.count.diff.DiffFileResult;
import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;

import static keisuke.count.diff.renderer.RendererConstant.*;
import static keisuke.swing.diffcount.DiffCountGUIConstant.*;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 * DiffCountResultの木構造をTreeTableModelにマッピングしたクラス
 *
 */
public class DiffCountTreeTableModel extends DefaultTreeTableModel {

	private MessageMap messageMap = null;
	private DiffStatusLabels diffStatLabels = null;

	DiffCountTreeTableModel(final MessageDefine msgdef) {
		super();
		this.setupConfig(msgdef);
		super.setColumnIdentifiers(this.createColumnIdentifiers());
	}

	/**
	 * 出力メッセージを設定する.
	 * @param msgdef メッセージ定義インスタンス
	 */
	protected final void setupConfig(final MessageDefine msgdef) {
		if (msgdef == null) {
			return;
		}
		this.messageMap = msgdef.getMessageMap();
		this.diffStatLabels = new DiffStatusLabelsImpl(msgdef);
	}

	private List<String> createColumnIdentifiers() {
		List<String> columnIdentifiers = new ArrayList<String>();
		if (this.messageMap != null) {
			columnIdentifiers.add(this.messageMap.get(MSG_DIFF_RND_PATH));
			columnIdentifiers.add(this.messageMap.get(MSG_DIFF_RND_STATUS));
			columnIdentifiers.add(this.messageMap.get(MSG_DIFF_RND_INCREASE));
			columnIdentifiers.add(this.messageMap.get(MSG_DIFF_RND_DECREASE));
		} else {
			columnIdentifiers.add("Name");
			columnIdentifiers.add("Status");
			columnIdentifiers.add("Added Steps");
			columnIdentifiers.add("Deleted Steps");
		}
		return columnIdentifiers;
	}

	/** {@inheritDoc} */
	@Override
	public Object getValueAt(final Object node, final int column) {
		DiffCountResult result = (DiffCountResult) ((DefaultMutableTreeTableNode) node).getUserObject();

		switch (column) {
		case INDEX_NAME:
			return result.nodeName();
		case INDEX_STATUS:
			if (this.diffStatLabels != null) {
				return this.diffStatLabels.getLabelOf(result.status());
			} else {
				return result.status().toString();
			}
		case INDEX_ADDED:
			return result.addedSteps();
		case INDEX_DELETEED:
			return result.deletedSteps();
		default:
			return null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isLeaf(final Object node) {
		DiffCountResult obj = (DiffCountResult) ((DefaultMutableTreeTableNode) node).getUserObject();
		return (obj instanceof DiffFileResult);
	}

	/**
	 * カウント結果をJXTreeTableで表示するためにDefaultMutableTreeTableNodeに変換します。
	 * @param folder 表示対象のルートフォルダー
	 * @return folder以下の木構造を変換したTreeTableNodeのルート
	 */
	private static DefaultMutableTreeTableNode treeTableNodeConvertedFrom(final DiffFolderResult folder) {
        DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(folder);
        for (AbstractDiffResultForCount child: folder.getChildren()) {
        	if (child instanceof DiffFolderResult) {
        		node.add(treeTableNodeConvertedFrom((DiffFolderResult) child));
        	} else {
                DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode(child);
        		node.add(childNode);
        	}
        }
        return node;
	}

	/**
	 * 差分行数計測結果のトップディレクトリノードをTreeTableの木構造に変換して
	 * TreeTableModelの根として設定する
	 * @param folder TreeTableの根となる差分行数計測結果のトップディレクトリノード
	 */
	protected void buildTreeOutOf(final DiffFolderResult folder) {
        this.setRoot(treeTableNodeConvertedFrom(folder));
	}
}
