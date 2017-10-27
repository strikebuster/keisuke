package keisuke.count;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import keisuke.count.language.XmlDefinedStepCounterFactory;
import keisuke.count.util.FileNameUtil;

/**
 * 言語定義XMLの内容にしたがって行数カウントする機能をもつクラス
 */
class StepCountFunction {

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
	 * 指定されたカウント対象File配列に含まれるノード全てをカウントした結果のListを返す
	 * List要素毎のファイル名はディレクトリパスは含まない
	 * @param files カウント対象File配列
	 * @return カウント結果List
	 * @throws IOException カウントのためにファイルを読み取る際に異常があれば発行
	 */
	List<StepCountResultForCount> countAll(final File[] files) throws IOException {
		return this.countEveryNodes(files);
	}

	/**
	 * 指定されたカウント対象File配列に含まれるノード毎にカウントする
	 * ノードがディレクトリの場合は再帰的に含まれるノードをカウントする
	 * @param files カウント対象File配列
	 * @return StepCountのカウント結果の配列
	 * @throws IOException カウントのためにファイルを読み取る際に異常があれば発行
	 */
	private List<StepCountResultForCount> countEveryNodes(final File[] files) throws IOException {
		// １ファイル or １ディレクトリずつカウント
		ArrayList<StepCountResultForCount> list = new ArrayList<StepCountResultForCount>();
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
			return this.countEveryNodes(file.listFiles());
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
