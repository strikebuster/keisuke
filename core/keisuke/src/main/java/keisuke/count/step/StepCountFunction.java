package keisuke.count.step;

import static keisuke.count.option.CountOptionConstant.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import keisuke.count.StepCountResultForCount;
import keisuke.count.StepCounter;
import keisuke.count.language.XmlDefinedStepCounterFactory;
import keisuke.count.util.FileNameUtil;
import keisuke.util.LogUtil;

/**
 * 言語定義XMLの内容にしたがって行数カウントする機能をもつクラス
 */
class StepCountFunction {

	private String sortingOrder = OPTVAL_SORT_ON;
	private String encoding = null;
	private XmlDefinedStepCounterFactory factory = null;

	/**
	 * 引数なしのコンストラクタは不可
	 */
	//private StepCountFunction() { }

	/**
	 * XML定義ファイルを指定するコンストラクタ
	 * @param encode ソースのエンコード名
	 * @param xmlfile 言語定義用のXML定義ファイル名
	 */
	StepCountFunction(final String encode, final String xmlfile) {
		// エンコードの設定
		this.encoding = encode;
		// 言語カウンターのファクトリ生成
		this.factory = new XmlDefinedStepCounterFactory();
		if (xmlfile != null && !xmlfile.isEmpty()) {
			this.factory.appendCustomizeXml(xmlfile);
		}
	}

	/**
	 * ファイルリストのソート順を文字列順でなくOSファイル名順に設定する
	 * @param order ソート順
	 */
	void setSortingOrder(final String order) {
		if (order == null || order.isEmpty()) {
			return;
		}
		this.sortingOrder = order;
	}

	/**
	 * 指定されたカウント対象File配列に含まれるノード全てをカウントした結果のListを返す.
	 * カウント結果にはディレクトリパスを含まないファイル名とともに
	 * 指定されたノードを相対パスの基点とした相対パスも記録しておく.
	 * @param paths カウント対象パス名の配列
	 * @return カウント結果List
	 * @throws IOException カウントのためにファイルを読み取る際に異常があれば発行
	 */
	List<StepCountResultForCount> countAll(final String[] paths) throws IOException {
		if (paths == null) {
			return null;
		}
		ArrayList<StepCountResultForCount> list = new ArrayList<StepCountResultForCount>();
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			String fullpath = null;
			File file = null;
			try {
				file = new File(path);
				fullpath = file.getCanonicalPath().replace('\\', '/');
			} catch (Exception e) {
				LogUtil.warningLog("fail to access '" + path + "', so skipping.");
				continue;
			}
			// １ファイル or １ディレクトリずつカウント
			List<StepCountResultForCount> results = countOneNode(file);
			for (StepCountResultForCount result : results) {
				// showDirectory用にパス情報を記録しておく
				if (file.isDirectory()) {
					// 基点ディレクトリを記録
					result.setBaseDirPath(fullpath);
				} else {
					// ファイル指定パスを記録
					result.setSpecifiedFilePath(path.replace('\\', '/'));
				}
				list.add(result);
			}
		}
		return list;
	}

	/**
	 * 指定されたカウント対象File配列に含まれるノード毎にカウントする.
	 * ノードがディレクトリの場合は再帰的に含まれるノードをカウントする.
	 * @param files カウント対象File配列
	 * @return StepCountのカウント結果の配列
	 * @throws IOException カウントのためにファイルを読み取る際に異常があれば発行
	 */
	private List<StepCountResultForCount> countEveryNodes(final File[] files) throws IOException {
		ArrayList<StepCountResultForCount> list = new ArrayList<StepCountResultForCount>();
		// １ファイル or １ディレクトリずつカウント
		for (int i = 0; i < files.length; i++) {
			List<StepCountResultForCount> results = countOneNode(files[i]);
			for (StepCountResultForCount result : results) {
				list.add(result);
			}
		}
		return list;
	}

	/**
	 * 引数で渡された１ノードであるFileインスタンスをカウントする.
	 * インスタンスがディレクトリの場合は配下のファイルを再帰的にカウントし、
	 * １ファイル毎のカウント結果の配列を返す.
	 * @param file カウント対象ファイルまたはディレクトリ
	 * @return StepCountのカウント結果の配列
	 * @throws IOException 引数ファイルのカウント中に異常があった場合に発行
	 */
	private List<StepCountResultForCount> countOneNode(final File file) throws IOException {
		if (file.isDirectory()) {
			if (FileNameUtil.checkToBeIgnored(file)) {
				return new ArrayList<StepCountResultForCount>();
			}
			if (this.sortingOrder.equals(OPTVAL_SORT_ON)) {
				return this.countEveryNodes(FileNameUtil.sortInCodeOrder(file.listFiles()));
			} else if (this.sortingOrder.equals(OPTVAL_SORT_OS)) {
				return this.countEveryNodes(FileNameUtil.sortInOsOrder(file.listFiles()));
			} else {
				//listFiles()の順番は不明なのでOS順にする
				//return this.countEveryNodes(file.listFiles());
				return this.countEveryNodes(FileNameUtil.sortInOsOrder(file.listFiles()));
			}
		} else {
			ArrayList<StepCountResultForCount> list = new ArrayList<StepCountResultForCount>();
			list.add(this.countOneFile(file));
			return list;
		}
	}

	/**
	 * 引数で渡された１ファイルであるFileインスタンスをカウントする.
	 * @param file カウント対象ファイルまたはディレクトリ
	 * @return StepCountのカウント結果
	 * @throws IOException 引数ファイルのカウント中に異常があった場合に発行
	 */
	private StepCountResultForCount countOneFile(final File file) throws IOException {
		StepCounter counter = this.factory.getCounter(file.getName());
		if (counter != null) {
			return counter.count(file, this.encoding);
		} else {
			// 未対応の形式の場合は形式にnullを設定して返す
			return new StepCountResultForCount(file, file.getName(), null, null, 0, 0, 0);
		}
    }

	/**
	 * このメソッドはAntタスクI/F向けのクラスから呼び出すことを想定している.
	 * 指定されたカウント対象ファイルパス名配列のファイル全てをカウントした結果のListを返す.
	 * ファイルパスは指定された基点ディレクトリからの相対パスで指定される.
	 * カウント結果にはディレクトリパスは含まないファイル名とともに
	 * 指定された基点ディレクトリからの相対パスも記録しておく.
	 *
	 * @param baseDir カウント対象ファイルパスの基点ディレクトリ
	 * @param filePaths カウント対象ファイルパスの配列
	 * @param showDir カウント結果のファイル名にパスを付ける場合はtrueを指定する
	 * @param isCategory 基点ディレクトリ名をカテゴリとして記録する場合はtrueを指定する
	 * @return カウント結果List
	 */
	List<StepCountResultForCount> countFileSet(final File baseDir, final String[] filePaths,
			final boolean showDir, final boolean isCategory) {
		if (filePaths == null) {
			return null;
		}
		if (baseDir == null) {
			LogUtil.errorLog("countFileSet: baseDir must be not null.");
			throw new IllegalArgumentException("no baseDir.");
		}
		String baseFullPath = null;
		try {
			if (showDir) {
				baseFullPath = baseDir.getCanonicalPath().replace('\\', '/');
			}
		} catch (IOException e) {
            throw new RuntimeException("I/O Error: " + baseDir, e);
		}

		ArrayList<StepCountResultForCount> list = new ArrayList<StepCountResultForCount>();
		for (int i = 0; i < filePaths.length; i++) {
			File file = null;
			try {
				file = new File(baseDir, filePaths[i]);
				StepCountResultForCount result = this.countOneFile(file);
				if (showDir) {
					// 基点ディレクトリを記録
					result.setBaseDirPath(baseFullPath);
					// baseDirディレクトリからのファイルパスに上書きします。
					result.setFilePathAsSubPathFromBase();
				}
				if (isCategory) {
					result.setSourceCategory(baseDir.getName());
				}
				list.add(result);
				//LogUtil.debugLog(result.filePath());
			} catch (IOException e) {
				throw new RuntimeException("I/O Error: " + file, e);
			}
		}
		return list;
	}
}
