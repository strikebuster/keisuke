package keisuke.count.step;

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

	private boolean sortingOsOrder = false;
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
		if (xmlfile != null) {
			this.factory.appendCustomizeXml(xmlfile);
		}
	}

	/**
	 * ファイルリストのソート順を文字列順でなくOSファイル名順に設定する
	 */
	void setSortingOsOrder() {
		this.sortingOsOrder = true;
	}

	/**
	 * 指定されたカウント対象File配列に含まれるノード全てをカウントした結果のListを返す
	 * List要素毎のファイル名はディレクトリパスは含まないが、
	 * 指定されたノードを相対パスの基点とできるようにList要素に記録しておく
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
	 * 指定されたカウント対象File配列に含まれるノード毎にカウントする
	 * ノードがディレクトリの場合は再帰的に含まれるノードをカウントする
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
	 * 引数で渡された１ファイルFileインスタンスをカウントする
	 * インスタンスがディレクトリの場合は配下のファイルを再帰的にカウントし、
	 * １ファイル毎のカウント結果の配列を返す
	 * @param file カウント対象ファイルまたはディレクトリ
	 * @return StepCountのカウント結果の配列
	 * @throws IOException 引数ファイルのカウント中に異常があった場合に発行
	 */
	private List<StepCountResultForCount> countOneNode(final File file) throws IOException {
		if (file.isDirectory()) {
			if (FileNameUtil.checkToBeIgnored(file)) {
				return new ArrayList<StepCountResultForCount>();
			}
			if (this.sortingOsOrder) {
				return this.countEveryNodes(FileNameUtil.sortInOsOrder(file.listFiles()));
			} else {
				return this.countEveryNodes(FileNameUtil.sortInCodeOrder(file.listFiles()));
			}
		} else {
			ArrayList<StepCountResultForCount> list = new ArrayList<StepCountResultForCount>();
			StepCounter counter = this.factory.getCounter(file.getName());
			if (counter != null) {
				StepCountResultForCount result = counter.count(file, this.encoding);
				list.add(result);
			} else {
				// 未対応の形式の場合は形式にnullを設定して返す
				list.add(new StepCountResultForCount(file, file.getName(), null, null, 0, 0, 0));
			}
			return list;
		}
	}
}
