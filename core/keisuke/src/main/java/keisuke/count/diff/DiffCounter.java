package keisuke.count.diff;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.sf.amateras.stepcounter.Util;
import jp.sf.amateras.stepcounter.diffcount.DiffSource;
import jp.sf.amateras.stepcounter.diffcount.diff.DiffEngine;
import jp.sf.amateras.stepcounter.diffcount.diff.IDiffHandler;
import keisuke.count.Cutter;
import keisuke.count.XmlDefinedStepCounterFactory;


/**
 * 差分のカウント処理を行います。
 */
public class DiffCounter {

	private XmlDefinedStepCounterFactory factory = null;
	private DiffStatusText registeredDiffStatusText = null;

	/**
	 * 言語定義XMLファイルと差分変更ステータスの表示文言定義を指定するコンストラクター
	 * @param xmlfile 言語ルールのカスタマイズ定義XMLファイル名
	 * @param diffStatusText 差分変更ステータスの表示文言定義インスタンス
	 */
	public DiffCounter(final String xmlfile, final DiffStatusText diffStatusText) {

		// 言語カウンターのファクトリ生成
		this.factory = new XmlDefinedStepCounterFactory();
		if (xmlfile != null) {
			this.factory.appendCustomizeXml(xmlfile);
		}
		this.registeredDiffStatusText = diffStatusText;
	}

	/**
	 * 2つのディレクトリ配下のソースコードの差分をカウントします。
	 *
	 * @param oldRoot 変更前のソースツリーのルートディレクトリ
	 * @param newRoot 変更後のソースツリーのルートディレクトリ
	 * @return カウント結果
	 */
	public DiffFolderResult count(final File oldRoot, final File newRoot) {

		DiffFolderResult root = new DiffFolderResult(null, this.registeredDiffStatusText);
		root.setName(newRoot.getName());

		diffFolder(root, oldRoot, newRoot);

		return root;
	}

	private void diffFolder(final DiffFolderResult parent, final File oldFolder,
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
		AbstractDiffResult result = null;
		while (newidx < newList.size()) {
			File oldFile = null;
			File newFile = newList.get(newidx);
			if (DiffCounterUtil.isIgnore(newFile)) {
				newidx++;
				continue;
			}
			newName = newFile.getName();
			//System.out.println("[DEBUG] NEW node : " + parent.getPath() + "/" + newName);
			boolean found = false;
			while (oldidx < oldList.size()) {
				oldFile = oldList.get(oldidx);
				if (DiffCounterUtil.isIgnore(oldFile)) {
					oldidx++;
					continue;
				}
				oldName = oldFile.getName();
				//System.out.println("[DEBUG] OLD node : " + parent.getPath() + "/" + oldName);
				if (FileNameCompare.compare(newName, oldName) == 0) { // 一致
					found = true;
					oldidx++;
					break;
				} else if (FileNameCompare.compare(newName, oldName) < 0) { // oldが大きい==oldにはない==新規
					// oldidxは進めずに次のnewidxと比較する
					break;
				} else { // oldが小さい==newにはない==削除
					//System.out.println("[DEBUG] OLD is REMOVED");
					result = createDiffResult(parent, oldFile, null,
							Util.getFileEncoding(oldFile), DiffStatus.REMOVED);
					parent.addChild(result);
					// ディレクトリの場合は再帰的に処理
					if (oldFile.isDirectory()) {
						diffFolder((DiffFolderResult) result, oldFile, null);
					}
				}
				oldidx++;
			}
			if (!found) {
				// oldになかったので「新規」
				//System.out.println("[DEBUG] NEW is ADDED");
				result = createDiffResult(parent, null, newFile,
						Util.getFileEncoding(newFile), DiffStatus.ADDED);
				parent.addChild(result);
			} else {
				// oldにあったので「変更」だが、ファイルとディレクトリの組み合わせ確認
				//System.out.println("[DEBUG] NEW == OLD");
				if ((oldFile.isFile() && newFile.isFile())
						|| (oldFile.isDirectory() && newFile.isDirectory())) {
					result = createDiffResult(parent, oldFile, newFile,
						Util.getFileEncoding(newFile), DiffStatus.MODIFIED);
					parent.addChild(result);
				} else if (newFile.isDirectory()) {
					// oldがファイルでnewがディレクトリ
					result = createDiffResult(parent, null, newFile,
							Util.getFileEncoding(newFile), DiffStatus.ADDED);
					parent.addChild(result);

					AbstractDiffResult result2 = createDiffResult(parent, oldFile, null,
							Util.getFileEncoding(oldFile), DiffStatus.REMOVED);
					parent.addChild(result2);
				} else {
					// newがファイルでoldがディレクトリ
					AbstractDiffResult result2 = createDiffResult(parent, oldFile, null,
							Util.getFileEncoding(oldFile), DiffStatus.REMOVED);
					parent.addChild(result2);
					diffFolder((DiffFolderResult) result2, oldFile, null);

					result = createDiffResult(parent, null, newFile,
							Util.getFileEncoding(newFile), DiffStatus.ADDED);
					parent.addChild(result);

				}
			}
			// ディレクトリの場合は再帰的に処理
			if (newFile.isDirectory()) {
				if (found) {
					diffFolder((DiffFolderResult) result, new File(oldFolder, newName), newFile);
				} else {
					diffFolder((DiffFolderResult) result, null, newFile);
				}
			}
			newidx++;
		}
		while (oldidx < oldList.size()) {
			// newにはない==削除
			File oldFile = oldList.get(oldidx);
			if (DiffCounterUtil.isIgnore(oldFile)) {
				oldidx++;
				continue;
			}
			oldName = oldFile.getName();
			//System.out.println("[DEBUG] OLD node : " + parent.getPath() + "/" + oldName);
			//System.out.println("[DEBUG] OLD is REMOVED");
			result = createDiffResult(parent, oldFile, null,
					Util.getFileEncoding(oldFile), DiffStatus.REMOVED);
			parent.addChild(result);
			// ディレクトリの場合は再帰的に処理
			if (oldFile.isDirectory()) {
				//diffFolder((DiffFolderResult)result, oldFile, new File(newFolder, oldName));
				diffFolder((DiffFolderResult) result, oldFile, null);
			}
			oldidx++;
		}

	}

	private AbstractDiffResult createDiffResult(final DiffFolderResult parent,
			final File oldFile, final File newFile, final String charset, final DiffStatus status) {

		if (newFile != null && newFile.isFile()) {
			String fileName = newFile.getName();
			DiffFileResult diffResult = null;
			//Cutter cutter = CutterFactory.getCutter(newFile);
			Cutter cutter = this.factory.getCutter(fileName);
			if (cutter != null) {
				try {
					diffResult = createDiffFileResult(parent, oldFile, newFile,
							charset, status, cutter);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			} else {
				// カッターが取得できなかった場合はサポート対象外とする
				diffResult = new DiffFileResult(parent, this.registeredDiffStatusText);
				diffResult.setName(fileName);
				diffResult.setDiffStatus(DiffStatus.UNSUPPORTED);
				diffResult.setAddCount(0);
				diffResult.setDelCount(0); // keisuke: 初期化漏れ対応で追加
				//diffResult.setFileType(CutterFactory.getFileType(newFile));
			}

			return diffResult;

		} else if (oldFile != null && oldFile.isFile()) {
			String fileName = oldFile.getName();
			DiffFileResult diffResult = null;
			//diffResult.setFileType(CutterFactory.getFileType(oldFile));
			//Cutter cutter = CutterFactory.getCutter(fileName);
			Cutter cutter = this.factory.getCutter(fileName);
			if (cutter != null) {
				if (status == DiffStatus.REMOVED) {
					try {
						diffResult = createDiffFileResult(parent, oldFile, newFile,
								charset, status, cutter);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			} else {
					// カッターが取得できなかった場合はサポート対象外とする
					diffResult = new DiffFileResult(parent, this.registeredDiffStatusText);
					diffResult.setName(fileName);
					diffResult.setDiffStatus(DiffStatus.UNSUPPORTED);
					diffResult.setAddCount(0);
					diffResult.setDelCount(0);
					//diffResult.setFileType(CutterFactory.getFileType(oldFile));
			}
			return diffResult;

		} else if (newFile != null && newFile.isDirectory()) {
			DiffFolderResult diffResult = new DiffFolderResult(parent, this.registeredDiffStatusText);
			diffResult.setName(newFile.getName());
			diffResult.setDiffStatus(status);
			return diffResult;

		} else if (oldFile != null && oldFile.isDirectory()) {
			DiffFolderResult diffResult = new DiffFolderResult(parent, this.registeredDiffStatusText);
			diffResult.setName(oldFile.getName());
			diffResult.setDiffStatus(status);
			return diffResult;
		}

		return null;
	}

	private DiffFileResult createDiffFileResult(final DiffFolderResult parent,
			final File oldFile, final File newFile, final String charset, final DiffStatus status,
			final Cutter cutter) throws IOException {

		DiffFileResult diffResult = new DiffFileResult(parent, this.registeredDiffStatusText);
		diffResult.setFileType(cutter.getFileType());
		// REMOVEDのときは対象はoldFileにする、そうでなければnewFile
		if (status == DiffStatus.REMOVED) {
			//diffResult.setFileType(CutterFactory.getFileType(oldFile));
			diffResult.setName(oldFile.getName());
		} else {
			//diffResult.setFileType(CutterFactory.getFileType(newFile));
			diffResult.setName(newFile.getName());
		}
		diffResult.setDiffStatus(status);

		if (status == DiffStatus.ADDED) {
			// 新規ファイルの場合
			DiffSource source = cutter.cut(newFile, charset);
			/* DEBUG BEGIN *
			if (cutter.getFileType().equals("VBScript")) {
				System.out.println("[DEBUG] (NEW) " + newFile.getName() + "===begin===");
				System.out.println(source.getSource());
				System.out.println("[DEBUG] (NEW) " + newFile.getName() + "===end===");
			}
			/* */
			if (source.isIgnore()) {
				return null;
			}
			diffResult.setAddCount(DiffCounterUtil.split(source.getSource()).length);
			diffResult.setCategory(source.getCategory());
		} else if (status == DiffStatus.MODIFIED) {
			// 変更ファイルの場合
			DiffSource oldSource = cutter.cut(oldFile, charset);
			DiffSource newSource = cutter.cut(newFile, charset);
			/* DEBUG BEGIN *
			if (cutter.getFileType().equals("PHP")) {
				System.out.println("[DEBUG] <OLD> " + oldFile.getName() + "===begin===");
				System.out.println(oldSource.getSource());
				System.out.println("[DEBUG] <OLD> " + oldFile.getName() + "===end===");
				System.out.println("[DEBUG] <NEW> " + newFile.getName() + "===begin===");
				System.out.println(newSource.getSource());
				System.out.println("[DEBUG] <NEW> " + newFile.getName() + "===end===");
			}
			/* */
			if (newSource.isIgnore()) {
				return null;
			}

			DiffCountHandler handler = new DiffCountHandler();
			DiffEngine engine = new DiffEngine(handler, oldSource.getSource(),
					newSource.getSource());
			engine.doDiff();

			diffResult.setAddCount(handler.getAddCount());
			diffResult.setDelCount(handler.getDelCount());
			diffResult.setCategory(newSource.getCategory());

			// keisuke: AddCount==0だけでは誤判定、&& DelCount==0 追加
			if (handler.getAddCount() == 0 && handler.getDelCount() == 0) {
				diffResult.setDiffStatus(DiffStatus.NONE);
			}
		} else if (status == DiffStatus.REMOVED) {
			// 削除ファイルの場合
			DiffSource source = cutter.cut(oldFile, charset);
			/* DEBUG BEGIN *
			if (cutter.getFileType().equals("EmbeddedRuby")) {
				System.out.println("[DEBUG] (OLD) " + oldFile.getName() + "===begin===");
				System.out.println(source.getSource());
				System.out.println("[DEBUG] (OLD) " + oldFile.getName() + "===end===");
			}
			/* */
			if (source.isIgnore()) {
				return null;
			}
			diffResult.setDelCount(DiffCounterUtil.split(source.getSource()).length);
			diffResult.setCategory(source.getCategory());
		} else {
			// 変更なしの場合だが、ここにはこないはず
			System.out.println("![WARN] illegal status of createDiffFileResult().");
		}

		return diffResult;
	}

	/**
	 * OSの違いによる大文字小文字の扱いに対応したファイル名比較のクラス
	 */
	private static class FileNameCompare {
		private static boolean ignoreCase = (new File("a")).compareTo(new File("A")) == 0 ? true : false;

		public static int compare(final String s1, final String s2) {
			if (ignoreCase) {
				return s1.compareToIgnoreCase(s2);
			} else {
				return s1.compareTo(s2);
			}
		}
	}

	/**
	 * 変更行数をカウントするための{@link IDiffHandler}実装クラスです。
	 */
	private static class DiffCountHandler implements IDiffHandler {

		/** 追加行数 */
		private int	addCount = 0;

		/** 削除行数 */
		private int	delCount = 0;

		/**
		 * {@inheritDoc}
		 */
		public void add(final String text) {
			this.addCount++;
		}

		/**
		 * {@inheritDoc}
		 */
		public void delete(final String text) {
			this.delCount++;
		}

		/**
		 * {@inheritDoc}
		 */
		public void match(final String text) { }

		/**
		 * 追加行数を取得します。
		 *
		 * @return 追加行数
		 */
		public int getAddCount() {
			return this.addCount;
		}

		/**
		 * 削除行数を取得します。
		 *
		 * @return 削除行数
		 */
		public int getDelCount() {
			return this.delCount;
		}
	}

}
