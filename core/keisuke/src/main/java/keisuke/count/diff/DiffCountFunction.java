package keisuke.count.diff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import keisuke.DiffStatusEnum;
import keisuke.count.NakedSourceCode;
import keisuke.count.StepCutter;
import keisuke.count.language.XmlDefinedStepCounterFactory;
import keisuke.count.util.FileNameUtil;
import keisuke.util.LogUtil;
import keisuke.util.StringUtil;


/**
 * 差分のカウント処理を行います。
 */
public class DiffCountFunction {

	private String encoding = null;
	private XmlDefinedStepCounterFactory factory = null;

	/**
	 * 言語定義XMLファイルと差分変更ステータスの表示文言定義を指定するコンストラクター
	 * @param encode ソースのエンコード名
	 * @param xmlfile 言語ルールのカスタマイズ定義XMLファイル名
	 */
	public DiffCountFunction(final String encode, final String xmlfile) {
		// エンコードの設定
		this.encoding = encode;
		// 言語カウンターのファクトリ生成
		this.factory = new XmlDefinedStepCounterFactory();
		if (xmlfile != null && !xmlfile.isEmpty()) {
			this.factory.appendCustomizeXml(xmlfile);
		}
	}

	/**
	 * 2つのディレクトリ配下のソースコードの差分をカウントします。
	 *
	 * @param oldRoot 変更前のソースツリーのルートディレクトリ
	 * @param newRoot 変更後のソースツリーのルートディレクトリ
	 * @return カウント結果
	 */
	public DiffFolderResult countDiffBetween(final File oldRoot, final File newRoot) {

		DiffFolderResult root = new DiffFolderResult(newRoot.getName(),
				DiffStatusEnum.MODIFIED,	// 暫定で変更と設定し、後で再評価する
				null);

		this.makeDiffResultAboutSubFolder(root, oldRoot, newRoot);

		return root;
	}

	private void makeDiffResultAboutSubFolder(final DiffFolderResult parent, final File oldFolder,
			final File newFolder) {
		File[] oldFiles = null;
		if (oldFolder != null) {
			oldFiles = oldFolder.listFiles();
		}
		if (oldFiles == null) {
			oldFiles = new File[0];
		}
		List<File> oldList = new ArrayList<File>();
		Collections.addAll(oldList, oldFiles);
		Collections.sort(oldList);

		File[] newFiles = null;
		if (newFolder != null) {
			newFiles = newFolder.listFiles();
		}
		if (newFiles == null) {
			newFiles = new File[0];
		}
		List<File> newList = new ArrayList<File>();
		Collections.addAll(newList, newFiles);
		Collections.sort(newList);
		// SORT順はUNIXとWindowsで異なる、Windowsは大文字小文字区別なし

		int newidx = 0;
		int oldidx = 0;
		String newName = "";
		String oldName = "";
		AbstractDiffResultForCount result = null;
		while (newidx < newList.size()) {
			File oldFile = null;
			File newFile = newList.get(newidx);
			if (FileNameUtil.checkToBeIgnored(newFile)) {
				newidx++;
				continue;
			}
			newName = newFile.getName();
			//LogUtil.debugLog("NEW node : " + parent.filePath() + "/" + newName);
			boolean found = false;
			while (oldidx < oldList.size()) {
				oldFile = oldList.get(oldidx);
				if (FileNameUtil.checkToBeIgnored(oldFile)) {
					oldidx++;
					continue;
				}
				oldName = oldFile.getName();
				//LogUtil.debugLog("OLD node : " + parent.filePath() + "/" + oldName);
				if (FileNameUtil.compare(newName, oldName) == 0) { // 一致
					found = true;
					oldidx++;
					break;
				} else if (FileNameUtil.compare(newName, oldName) < 0) { // oldが大きい==oldにはない==新規
					// oldidxは進めずに次のnewidxと比較する
					break;
				} else { // oldが小さい==newにはない==削除
					//LogUtil.debugLog("OLD is REMOVED");
					result = createDiffResult(parent, oldFile, null, DiffStatusEnum.DROPED);
					parent.addChild(result);
					// ディレクトリの場合は再帰的に処理
					if (oldFile.isDirectory()) {
						makeDiffResultAboutSubFolder((DiffFolderResult) result, oldFile, null);
					}
				}
				oldidx++;
			}
			if (!found) {
				// oldになかったので「新規」
				//LogUtil.debugLog("NEW is ADDED");
				result = createDiffResult(parent, null, newFile, DiffStatusEnum.ADDED);
				parent.addChild(result);
			} else {
				// oldにあったのでファイルとディレクトリの組み合わせが合っていれば
				//「変更」または「変更なし」だが、配下の比較しないと分からないので
				// 暫定的に「変更」を設定し、後で再評価する
				//LogUtil.debugLog("NEW == OLD");
				if ((oldFile.isFile() && newFile.isFile())
						|| (oldFile.isDirectory() && newFile.isDirectory())) {
					result = createDiffResult(parent, oldFile, newFile, DiffStatusEnum.MODIFIED);
					parent.addChild(result);
				} else if (newFile.isDirectory()) {
					// oldがファイルでnewがディレクトリ
					result = createDiffResult(parent, null, newFile, DiffStatusEnum.ADDED);
					parent.addChild(result);

					AbstractDiffResultForCount result2 = createDiffResult(parent, oldFile, null,
							DiffStatusEnum.DROPED);
					parent.addChild(result2);
				} else {
					// newがファイルでoldがディレクトリ
					AbstractDiffResultForCount result2 = createDiffResult(parent, oldFile, null,
							DiffStatusEnum.DROPED);
					parent.addChild(result2);
					makeDiffResultAboutSubFolder((DiffFolderResult) result2, oldFile, null);

					result = createDiffResult(parent, null, newFile, DiffStatusEnum.ADDED);
					parent.addChild(result);

				}
			}
			// ディレクトリの場合は再帰的に処理
			if (newFile.isDirectory()) {
				if (found) {
					makeDiffResultAboutSubFolder((DiffFolderResult) result,
							new File(oldFolder, newName), newFile);
				} else {
					makeDiffResultAboutSubFolder((DiffFolderResult) result, null, newFile);
				}
			}
			newidx++;
		}
		while (oldidx < oldList.size()) {
			// newにはない==削除
			File oldFile = oldList.get(oldidx);
			if (FileNameUtil.checkToBeIgnored(oldFile)) {
				oldidx++;
				continue;
			}
			oldName = oldFile.getName();
			//LogUtil.debugLog("OLD node : " + parent.filePath() + "/" + oldName);
			//LogUtil.debugLog("OLD is REMOVED");
			result = createDiffResult(parent, oldFile, null, DiffStatusEnum.DROPED);
			parent.addChild(result);
			// ディレクトリの場合は再帰的に処理
			if (oldFile.isDirectory()) {
				this.makeDiffResultAboutSubFolder((DiffFolderResult) result, oldFile, null);
			}
			oldidx++;
		}

	}

	private AbstractDiffResultForCount createDiffResult(final DiffFolderResult parent,
			final File oldFile, final File newFile, final DiffStatusEnum status) {

		if (newFile != null && newFile.isFile()) {
			String fileName = newFile.getName();
			DiffFileResult diffResult = null;
			StepCutter cutter = this.factory.getCutter(fileName);
			if (cutter != null) {
				try {
					diffResult = this.createDiffFileResult(parent, oldFile, newFile,
							status, cutter);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			} else {
				// カッターが取得できなかった場合はサポート対象外とする
				diffResult = new DiffFileResult(fileName, DiffStatusEnum.UNSUPPORTED, parent);
			}
			return diffResult;

		} else if (oldFile != null && oldFile.isFile()) {
			String fileName = oldFile.getName();
			DiffFileResult diffResult = null;
			StepCutter cutter = this.factory.getCutter(fileName);
			if (cutter != null) {
				if (status == DiffStatusEnum.DROPED) {
					try {
						diffResult = this.createDiffFileResult(parent, oldFile, newFile,
								status, cutter);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			} else {
				// カッターが取得できなかった場合はサポート対象外とする
				diffResult = new DiffFileResult(fileName, DiffStatusEnum.UNSUPPORTED, parent);
			}
			return diffResult;

		} else if (newFile != null && newFile.isDirectory()) {
			DiffFolderResult diffResult = new DiffFolderResult(newFile.getName(), status, parent);
			return diffResult;

		} else if (oldFile != null && oldFile.isDirectory()) {
			DiffFolderResult diffResult = new DiffFolderResult(oldFile.getName(), status, parent);
			return diffResult;
		}

		return null;
	}

	private DiffFileResult createDiffFileResult(final DiffFolderResult parent,
			final File oldFile, final File newFile, final DiffStatusEnum status,
			final StepCutter cutter) throws IOException {

		DiffFileResult diffResult = null;
		// REMOVEDのときは対象はoldFileにする、そうでなければnewFile
		if (status == DiffStatusEnum.DROPED) {
			diffResult = new DiffFileResult(oldFile.getName(), status, parent);
		} else {
			diffResult = new DiffFileResult(newFile.getName(), status, parent);
		}
		diffResult.setSourceType(cutter.getFileType());

		if (status == DiffStatusEnum.ADDED) {
			// 新規ファイルの場合
			NakedSourceCode source = cutter.cut(newFile, this.encoding);
			/* DEBUG BEGIN *
			if (cutter.getFileType().equals("VBScript")) {
				LogUtil.debugLog("(NEW) " + newFile.getName() + "===begin===");
				LogUtil.debugLog("\n" + source.code());
				LogUtil.debugLog("(NEW) " + newFile.getName() + "===end===");
			}
			/* DEBUG END */
			if (source.isIgnored()) {
				return null;
			}
			diffResult.setSteps(StringUtil.splitArrayOfLinesFrom(source.code()).length, 0);
			diffResult.setSourceCategory(source.category());
		} else if (status == DiffStatusEnum.MODIFIED) {
			// 変更ファイルの場合
			NakedSourceCode oldSource = cutter.cut(oldFile, this.encoding);
			NakedSourceCode newSource = cutter.cut(newFile, this.encoding);
			/* DEBUG BEGIN *
			if (cutter.getFileType().equals("PHP")) {
				LogUtil.debugLog("<OLD> " + oldFile.getName() + "===begin===");
				LogUtil.debugLog("\n" + oldSource.code());
				LogUtil.debugLog("<OLD> " + oldFile.getName() + "===end===");
				LogUtil.debugLog("<NEW> " + newFile.getName() + "===begin===");
				LogUtil.debugLog("\n" + newSource.code());
				LogUtil.debugLog("<NEW> " + newFile.getName() + "===end===");
			}
			/* DEBUG END */
			if (newSource.isIgnored()) {
				return null;
			}

			DiffEngine engine = new DiffEngine(oldSource.code(),
					newSource.code());
			engine.doDiff();

			diffResult.setSteps(engine.countAddedSteps(), engine.countDeletedSteps());
			diffResult.setSourceCategory(newSource.category());

			if (engine.countAddedSteps() == 0 && engine.countDeletedSteps() == 0) {
				diffResult.setDiffStatus(DiffStatusEnum.UNCHANGED);
			}
		} else if (status == DiffStatusEnum.DROPED) {
			// 削除ファイルの場合
			NakedSourceCode source = cutter.cut(oldFile, this.encoding);
			/* DEBUG BEGIN *
			if (cutter.getFileType().equals("Text")) {
				LogUtil.debugLog("(OLD) " + oldFile.getName() + "===begin===");
				LogUtil.debugLog("\n" + source.code());
				LogUtil.debugLog("(OLD) " + oldFile.getName() + "===end===");
			}
			/* DEBUG END */
			if (source.isIgnored()) {
				return null;
			}
			diffResult.setSteps(0, StringUtil.splitArrayOfLinesFrom(source.code()).length);
			diffResult.setSourceCategory(source.category());
		} else {
			// 変更なしの場合だが、ここにはこないはず
			LogUtil.warningLog("illegal status of createDiffFileResult().");
		}

		return diffResult;
	}

}
