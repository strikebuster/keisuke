package keisuke.swing.stepcount;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JListFixture;

import keisuke.count.util.FileNameUtil;

import static keisuke.swing.GUITestUtil.*;
import static keisuke.swing.stepcount.StepCountGUIConstant.HIDDEN_ADDING;

/**
 * Swing画面DiffCountテスト用ユーティリティ
 */
final class StepCountTestUtil {

	private StepCountTestUtil() { }

	/**
	 * 親Frameの子画面としてファイル選択ダイアログを開く操作を割り当てられたボタンを押し、
	 * 指定した基準ディレクトリの指定したファイルを選択した状態にする。
	 * @param frame 親のFrameFixture
	 * @param browseBtn FileChooserダイアログを開くボタンのJButtonFixture
	 * @param dialogName FileChooserダイアログの名前
	 * @param dirName 選択する基準ディレクトリ名
	 * @param fileNames 選択するファイル名配列
	 * @param list 選択したファイルパスを記録するJListFixture
	 */
	static void chooseSomeFiles(final FrameFixture frame,
			final JButtonFixture browseBtn, final String dialogName,
			final String dirName, final String[] fileNames,
			final JListFixture list) {

		if (usingDialog()) {
			chooseSomeFilesByUsingDialog(frame, browseBtn, dialogName, dirName, fileNames);
		} else {
			chooseSomeFilesByPuttingInList(frame, fileNames);
		}
	}

	static void chooseSomeFilesByUsingDialog(final FrameFixture frame,
			final JButtonFixture browseBtn, final String dialogName,
			final String dirName, final String[] fileNames) {

		sleep(SLEEPVERYLONGTIME); // next filechooser is very often dead
		browseBtn.click();
		sleep(SLEEPLONGTIME);
		JFileChooserFixture fileChooser = findFileChooserWithRetrying(frame,
				dialogName, RETRY_TIMES);
		sleep(SLEEPTIME);
		if (fileNames == null) {
			try {
				// name == "取消" を探すが "Cancel"のときComponentLookupException
				fileChooser.cancelButton().click();
			} catch (ComponentLookupException ex) {
				JButton button = (JButton) fileChooser.robot.finder()
						.find(new GenericTypeMatcher<JButton>(javax.swing.JButton.class) {
							@Override
							protected boolean isMatching(final JButton button) {
								return "Cancel".equals(button.getText());
							}
						});
				JButtonFixture buttonFixture = new JButtonFixture(fileChooser.robot, button);
				buttonFixture.click();
			}
		} else if (dirName == null) {
			fileChooser.focus().setCurrentDirectory(new File("."));
			sleep(SLEEPTIME);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < fileNames.length; i++) {
				sb.append('"').append(fileNames[i]).append('"');
				if (i < fileNames.length - 1) {
					sb.append(' ');
				}
			}
			fileChooser.fileNameTextBox().setText(sb.toString());
			sleep(SLEEPTIME);
			fileChooser.approveButton().focus().click();
		} else {
			File[] multiFiles = new File[fileNames.length];
			for (int i = 0; i < fileNames.length; i++) {
				multiFiles[i] = new File(fileNames[i]);
			}
			fileChooser.focus().setCurrentDirectory(new File(dirName));
			sleep(SLEEPTIME);
			fileChooser.focus().selectFiles(multiFiles);
			sleep(SLEEPTIME);
			fileChooser.approveButton().focus().click();
		}
		sleep(SLEEPLONGTIME);
	}

	private static void chooseSomeFilesByPuttingInList(final FrameFixture frame,
			final String[] fileNames) {

		sleep(SLEEPTIME);
		if (fileNames == null || fileNames.length == 0) {
			return;
		}
		/*
		File[] multiFiles = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			multiFiles[i] = new File(fileNames[i]);
		}
		multiFiles = sortPathInOSOrder(multiFiles);
		for (int i = 0; i < multiFiles.length; i++) {
			frame.textBox(HIDDEN_TEXT).setText(multiFiles[i].getAbsolutePath());
			System.out.println("[TEST] not to use file-choose-dialog: set directly file="
					+ multiFiles[i].getAbsolutePath());
		}
		*/
		String[] sortedNames;
		if (fileNames.length == 1) {
			sortedNames = fileNames;
		} else {
			sortedNames = sortInOSOrder(fileNames);
		}
		for (int i = 0; i < sortedNames.length; i++) {
			frame.textBox(HIDDEN_ADDING).setText(sortedNames[i]);
			System.out.println("[TEST] not to use file-choose-dialog: set directly file="
					+ sortedNames[i]);
		}
	}

	/**
	 * ファイル配列に対しソートを実行した結果の配列を返す
	 * OSの違いによる大文字小文字の扱いに対応したソートを行う
	 * ソートはファイルの絶対パスに対して行う
	 * cf. FileNameUtil#sortInOSOrder() はファイル名のみでソートする
	 * @param filearray ファイル配列
	 * @return ソート後のファイル配列
	 */
	private static File[] sortPathInOSOrder(final File[] filearray) {
		if (filearray == null) {
			return null;
		}
		Arrays.sort(filearray, new Comparator<File>() {
			public int compare(final File o1, final File o2) {
				return FileNameUtil.compareInOsOrder(o1.getAbsolutePath(), o2.getAbsolutePath());
			}
		});
		return filearray;
	}

	private static String[] sortInOSOrder(final String[] strarray) {
		if (strarray == null) {
			return null;
		}
		Arrays.sort(strarray, new Comparator<String>() {
			public int compare(final String o1, final String o2) {
				return FileNameUtil.compareInOsOrder(o1, o2);
			}
		});
		return strarray;
	}

	static String parentPathOf(final String path) {
		if (path == null) {
			return null;
		}
		int pos = path.lastIndexOf(File.separatorChar);
		if (pos <= 0) {
			return "";
		}
		return path.substring(0, pos);
	}

}
