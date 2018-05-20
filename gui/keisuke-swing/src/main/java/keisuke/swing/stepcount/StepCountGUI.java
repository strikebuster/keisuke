package keisuke.swing.stepcount;

import static keisuke.count.step.format.FormatConstant.MSG_COUNT_FMT_PREFIX;
import static keisuke.count.option.CountOptionConstant.OPTVAL_EXCEL;
import static keisuke.swing.GUIConstant.MSG_EXCUSE_BINARY;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import keisuke.report.property.MessageDefine;
import keisuke.swing.AbstractMainGUI;

/**
 * StepCount用のSwing部品の集約クラス
 *
 */
public final class StepCountGUI extends AbstractMainGUI {

	private StepCountAdapter stepCounter;
	private SourceListComponent sources;

	public StepCountGUI() {
		super();
		this.setMessageDefine(new MessageDefine(MSG_COUNT_FMT_PREFIX));
		this.stepCounter = new StepCountAdapter();
		this.initComponents();
	}

	private void initComponents() {
		this.mainWindow().setTitle("StepCount");

		this.sources = new SourceListComponent(this);
		this.setCommander(new CountCommandComponent(this));
		this.setResultView(new CountResultViewComponent(this, this.messageDefine()));

		Container contentPane = this.mainWindow().getContentPane();
		contentPane.setPreferredSize(this.mainWindow().getSize());
		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(this.sources.createGroupForVerticalGroup(contentPane)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(this.commander().createGroupForVerticalGroup(contentPane)))
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(this.resultView().createGroupForVerticalGroup(contentPane))));
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(Alignment.CENTER)
	        		.addGroup(this.sources.createGroupForHorizontalGroup(contentPane))
	        		.addGroup(this.commander().createGroupForHorizontalGroup(contentPane))
	        		.addGroup(this.resultView().createGroupForHorizontalGroup(contentPane))));

		this.mainWindow().pack();
		this.mainWindow().setLocationRelativeTo(null);
	}

	/**
	 * {@inheritDoc}
	 * 計測条件に変更があったときに呼び出され、
	 * ソースリストの状態更新処理を呼ぶ。
	 */
	protected void updateStatus() {
		this.sources.updateStatus();
	}

	/**
	 * {@inheritDoc}
	 * ソースリストに挙げられたファイルまたはディレクトリの
	 * ステップ数計測を実行して結果を表示する。
	 */
	protected void showCountingResult() {
		String xml = this.commander().xmlFileName();
		if (xml != null && !xml.isEmpty()) {
			this.stepCounter.setXml(xml);
		}
		String encoding = this.commander().encoding();
		if (encoding != null && !encoding.isEmpty()) {
			this.stepCounter.setEncoding(encoding);
		}
		this.stepCounter.setShowDirectory(((CountCommandComponent) this.commander()).showDirectory());
		this.stepCounter.setSort(((CountCommandComponent) this.commander()).sort());
		String format = this.commander().format();
		this.stepCounter.setFormat(format);
		if (format.equals(OPTVAL_EXCEL)) {
			this.setResultBytes(this.stepCounter.getCountingResultAsBytes(this.sources.getPathOfSources()));
			((CountResultViewComponent) this.resultView())
				.refreshWith(MSG_EXCUSE_BINARY, this.stepCounter.getCountedResultAsRaw());
		} else {
			String text = this.stepCounter.getCountingResultAsText(this.sources.getPathOfSources());
			this.setResultBytes(text.getBytes());
			((CountResultViewComponent) this.resultView())
				.refreshWith(text, this.stepCounter.getCountedResultAsRaw());
		}
	}

}
