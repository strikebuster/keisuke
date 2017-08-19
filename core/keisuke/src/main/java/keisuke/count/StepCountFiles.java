package keisuke.count;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.StepCounter;
import jp.sf.amateras.stepcounter.Util;

/**
 * 言語定義XMLの内容にしたがって行数カウントする
 * origin: a part of jp.sf.amateras.stepcounter.Main
 */
class StepCountFiles {

	private XmlDefinedStepCounterFactory factory = null;
	private List<CountResult> resultsList;

	/**
	 * 引数なしのコンストラクタは不可
	 */
	//private StepCountFiles() { }

	/**
	 * XML定義ファイルを指定するコンストラクタ
	 * @param xmlfile 言語定義用のXML定義ファイル名
	 */
	StepCountFiles(final String xmlfile) {
		// 言語カウンターのファクトリ生成
		this.factory = new XmlDefinedStepCounterFactory();
		if (xmlfile != null) {
			this.factory.appendCustomizeXml(xmlfile);
		}
	}

	/**
	 * 指定されたカウント対象File配列に含まれるノード毎にカウントする
	 * ノードがディレクトリの場合は再帰的に含まれるノードをカウントする
	 * カウント結果はresultList()で取得できる
	 * @param files カウント対象File配列
	 * @throws IOException カウントのためにファイルを読み取る際に異常があれば発行
	 */
	void countEveryNodes(final File[] files) throws IOException {
		// １ファイル or １ディレクトリずつカウント
		ArrayList<CountResult> list = new ArrayList<CountResult>();
		for (int i = 0; i < files.length; i++) {
			CountResult[] results = countOneNode(files[i]);
			for (int j = 0; j < results.length; j++) {
				list.add(results[j]);
			}
		}
		this.resultsList = list;
	}

	/**
	 * カウント結果のListを返す
	 * List要素毎のファイル名はディレクトリパスは含まない
	 * @return カウント結果List
	 */
	List<CountResult> getResults() {
		return this.resultsList;
	}

	/**
	 * 引数で渡された１ファイルFileインスタンスをカウントする
	 * インスタンスがディレクトリの場合は配下のファイルを再帰的にカウントし、
	 * １ファイル毎のカウント結果の配列を返す
	 * @param file カウント対象ファイルまたはディレクトリ
	 * @return StepCountのカウント結果の配列
	 * @throws IOException 引数ファイルのカウント中に異常があった場合に発行
	 */
	private CountResult[] countOneNode(final File file) throws IOException {
		if (file.isDirectory()) {
			File[] childrenFiles = file.listFiles();
			ArrayList<CountResult> list = new ArrayList<CountResult>();
			for (int i = 0; i < childrenFiles.length; i++) {
				CountResult[] results = countOneNode(childrenFiles[i]);
				for (int j = 0; j < results.length; j++) {
					list.add(results[j]);
				}
			}
			return (CountResult[]) list.toArray(new CountResult[list.size()]);
		} else {
			StepCounter counter = this.factory.getCounter(file.getName());
			if (counter != null) {
				CountResult result = counter.count(file, Util.getFileEncoding(file));
				return new CountResult[] {result};
			} else {
				// 未対応の形式の場合は形式にnullを設定して返す
				return new CountResult[] {
						new CountResult(file, file.getName(), null, null, 0, 0, 0)
					};
			}
		}
	}

}
