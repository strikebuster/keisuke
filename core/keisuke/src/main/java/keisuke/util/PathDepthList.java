package keisuke.util;

import java.util.ArrayList;

/**
 * パスの階層に沿って処理するときの現在位置を記録するクラス
 */
public class PathDepthList {
	private ArrayList<String> list;

	public PathDepthList() {
		this.list = new ArrayList<String>();
	}

	/**
	 * リストの深さを返す
	 * @return number of path depth
	 */
	public int depth() {
		return this.list.size();
	}

	/**
	 * リストの末尾にノード名を追加する
	 * @param node name of path node
	 */
	public void add(final String node) {
		this.list.add(node);
	}

	private void raise() {
		this.list.remove(this.depth() - 1);
	}

	/**
	 * リストの指定の深さまで上った位置にノードを追加する
	 * @param node name of node.
	 * @param position depth position where node will be placed in.
	 */
	public void setNodeIntoDepth(final String node, final int position) {
		//System.out.println("[DEBUG] Node = " + node + ", Position = "
		//		+ position + " : Depth = " + this.depth());
		if (node == null || node.isEmpty()) {
			return;
		}
		if (position < 0) {
			return;
		}
		int dirDepth = this.depth();
		if (position == 0) {
			if (dirDepth == 0) {
				// ルートディレクトリの追加
				this.add(node);
				return;
			} else {
				// ルート以外の場合、エラー
				throw new RuntimeException("illegal depth of node.");
			}
		}
		while (this.depth() > position) {
			this.raise();
		}
		this.add(node);
		//System.out.println("[DEBUG] Depth = " + dirDepth + " : " + this.toString());
	}

	/**
	 * 保持しているパスノードリストを連結してパス文字列を返す
	 * @return パス文字列
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String node : this.list) {
	        	sb.append(node);
	    }
		return sb.toString();
	}
}
