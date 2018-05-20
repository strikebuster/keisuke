package keisuke.swing;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;

/**
 * ウィンドウの領域を分ける区切り線部品
 */
public class SeparatorUnit {

	private JSeparator separator;

	public SeparatorUnit() {
		this.separator = new JSeparator(SwingConstants.HORIZONTAL);
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したParallelGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのParallelGroup
	 */
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		//return layout.createParallelGroup(Alignment.CENTER).addComponent(this.separator);
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.separator));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(this.separator));
	}
}
