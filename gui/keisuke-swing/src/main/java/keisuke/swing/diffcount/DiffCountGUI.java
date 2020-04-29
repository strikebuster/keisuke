package keisuke.swing.diffcount;

import static keisuke.count.diff.DiffCountProc.MSG_DIFF_PREFIXES;
import static keisuke.swing.GUIConstant.MSG_EXCUSE_BINARY;

import java.awt.Container;
import java.io.UnsupportedEncodingException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import keisuke.report.property.MessageDefine;
import keisuke.swing.AbstractMainGUI;
import keisuke.swing.SeparatorUnit;

/**
 * DiffCount用のSwing部品の集約クラス
 *
 */
public class DiffCountGUI extends AbstractMainGUI {

	private DiffCountAdapter diffCounter;
	private RootDirectoriesComponent rootDirs;
	private SeparatorUnit separator1;
	private SeparatorUnit separator2;

	public DiffCountGUI() {
		super();
		this.setMessageDefine(new MessageDefine(MSG_DIFF_PREFIXES));
		this.diffCounter = new DiffCountAdapter(null, null, this.messageDefine());
		this.initComponents();
	}

	private void initComponents() {
		this.mainWindow().setTitle("DiffCount");

		this.rootDirs = new RootDirectoriesComponent(this);
		this.separator1 = new SeparatorUnit();
		this.setCommander(new DiffCommandComponent(this));
		this.separator2 = new SeparatorUnit();
		this.setResultView(new DiffResultViewComponent(this, this.messageDefine()));

		Container contentPane = this.mainWindow().getContentPane();
		contentPane.setPreferredSize(this.mainWindow().getSize());

		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(this.rootDirs.createGroupForVerticalGroup(contentPane))
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
						.addGroup(this.separator1.createGroupForVerticalGroup(contentPane)))
				.addGroup(this.commander().createGroupForVerticalGroup(contentPane))
				.addGroup(layout.createParallelGroup(Alignment.CENTER)
					.addGroup(this.separator2.createGroupForVerticalGroup(contentPane)))
				.addGroup(this.resultView().createGroupForVerticalGroup(contentPane)));
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(Alignment.CENTER)
	        		.addGroup(this.rootDirs.createGroupForHorizontalGroup(contentPane))
	        		.addGroup(this.separator1.createGroupForHorizontalGroup(contentPane))
	        		.addGroup(this.commander().createGroupForHorizontalGroup(contentPane))
	        		.addGroup(this.separator2.createGroupForHorizontalGroup(contentPane))
	        		.addGroup(this.resultView().createGroupForHorizontalGroup(contentPane))));

		this.mainWindow().pack();
		this.mainWindow().setLocationRelativeTo(null);
	}

	/**
	 * {@inheritDoc}
	 * 計測条件に変更があったときに呼び出され、
	 * 新旧ディレクトリ指定部品の状態更新処理を呼ぶ。
	 */
	protected void updateStatus() {
		this.rootDirs.updateStatus();
	}

	/**
	 * {@inheritDoc}
	 * 2つのディレクトリ配下のソースの差分行数を計測し表示する。
	 */
	protected void showCountingResult() {
		String xml = this.commander().xmlFileName();
		if (xml == null || xml.isEmpty()) {
			this.diffCounter.setXml(null);
		} else {
			this.diffCounter.setXml(xml);
		}
		String encoding = this.commander().encoding();
		if (encoding == null || encoding.isEmpty()) {
			this.diffCounter.setEncoding(null);
		} else {
			this.diffCounter.setEncoding(encoding);
		}
		this.diffCounter.setPath(((DiffCommandComponent) this.commander()).pathStyle());
		this.diffCounter.setSort(((DiffCommandComponent) this.commander()).sort());
		this.diffCounter.setFormat(this.commander().format());
		String[] dirs = new String[2];
		dirs[0] = this.rootDirs.oldDirName();
		dirs[1] = this.rootDirs.newDirName();
		this.setResultBytes(this.diffCounter.getCountingResultAsBytes(dirs));
		if (this.diffCounter.isTextAsOutput()) {
			String text = "";
			try {
				text = new String(this.resultBytes(), this.diffCounter.encodingAsOutput());
			} catch (UnsupportedEncodingException e) {
				text = e.toString();
			}
			((DiffResultViewComponent) this.resultView())
				.refreshWith(text, this.diffCounter.getCountedResultAsRaw());
		} else {
			((DiffResultViewComponent) this.resultView())
				.refreshWith(MSG_EXCUSE_BINARY, this.diffCounter.getCountedResultAsRaw());
		}
	}

}
