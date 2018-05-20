package keisuke.swing;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

/**
 * コンボボックス選択用部品の基底クラス
 */
public abstract class AbstractSelectUnit {

	private JLabel label;
	private JComboBox<String> comboBox;

	protected AbstractSelectUnit() { }

	/**
	 * 部品の初期化処理
	 * @param labeltext 部品ラベル文字列
	 * @param comboname コンボボックスの名称
	 * @param values コンボボックスの選択肢
	 */
	protected void initialize(final String labeltext, final String comboname, final String[] values) {
		this.label = new JLabel(labeltext);
		this.comboBox = new JComboBox<String>(values);
		this.comboBox.setName(comboname);
		this.comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				doActionOfSelect(event);
			}
		});
	}

	/**
	 * ラベルインスタンスを返す
	 * @return ラベルインスタンス
	 */
	protected JLabel label() {
		return this.label;
	}

	/**
	 * コンボボックスインスタンスを返す
	 * @return コンボボックスインスタンス
	 */
	protected JComboBox<String> comboBox() {
		return this.comboBox;
	}

	/**
	 * コンボボックスの選択値を返す
	 * @return コンボボックスの選択値
	 */
	public String selectedValue() {
		return (String) this.comboBox.getSelectedItem();
	}

	/**
	 * コンボボックスが選択された時のコールバック処理
	 * @param event 発生したイベント
	 */
	protected void doActionOfSelect(final ActionEvent event) {
		this.updateStatus();
	}

	/**
	 * ファイルが選択された時の状態更新処理.
	 */
	protected abstract void updateStatus();

	/**
	 * ContainerのGroupLayoutの中にGUI要素を垂直方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForVerticalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(this.label)
					.addComponent(this.comboBox));
	}

	/**
	 * ContainerのGroupLayoutの中にGUI要素を水平方向に配置したSequentialGroupを作成する
	 * @param container 配置先となるGroupLayoutの親Container
	 * @return 配置済みのSequentialGroup
	 */
	public GroupLayout.SequentialGroup createGroupForHorizontalGroup(final Container container) {
		GroupLayout layout = (GroupLayout) container.getLayout();
		return layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
				.addComponent(this.label))
			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
				.addComponent(this.comboBox));
	}
}
