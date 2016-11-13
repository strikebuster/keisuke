package keisuke.count;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.sf.amateras.stepcounter.CountResult;
import jp.sf.amateras.stepcounter.StepCounter;
import jp.sf.amateras.stepcounter.Util;

/**
 * keisuke: 追加クラス
 * StepCountクラスにメソッドcountAll()などを置いて呼ぶと
 * Junitでエラー発生するためクラス分割
 *
 */
public class StepCountFiles {

	private XmlDefinedStepCounterFactory factory = null;
	private File[] files;
	private boolean showDirectory = false;
	
	public StepCountFiles(String xmlfile) {
		// 言語カウンターのファクトリ生成
		this.factory = new XmlDefinedStepCounterFactory();
		if (xmlfile != null) {
			this.factory.appendCustomizeXml(xmlfile);
		}
	}
	
	public void setShowDirectoryTrue() {
		this.showDirectory = true;
	}
	
	/** keisuke: StepCount(元Main).executeCount()から計測処理を独立　*/
	public CountResult[] countAll(File[] fileArray) throws IOException {
		this.files = fileArray;
		// １ファイル or １ディレクトリずつカウント
		ArrayList<CountResult> list = new ArrayList<CountResult>();
		for(int i=0;i<this.files.length;i++){
			CountResult[] results = count(this.files[i]);
			for(int j=0;j<results.length;j++){
				list.add(results[j]);
			}
		}
		if (this.showDirectory) {
			for (CountResult result : list) {
				// 差分ディレクトリ付きのファイル名に上書きします。
				result.setFileName(getFileNameWithDir(result.getFile()));
			}
		}
		Collections.sort(list, new PathComparator());
		CountResult[] results = (CountResult[])list.toArray(new CountResult[list.size()]);
		
		return results;
	}
	
	public class PathComparator implements Comparator<CountResult>{
		public int compare(CountResult o1, CountResult o2) {
			return o1.getFileName().compareTo(o2.getFileName());
		}
	}

	/** １ファイルをカウント */
	private CountResult[] count(File file) throws IOException {
		if(file.isDirectory()){
			File[] files = file.listFiles();
			ArrayList<CountResult> list = new ArrayList<CountResult>();
			for(int i=0;i<files.length;i++){
				CountResult[] results = count(files[i]);
				for(int j=0;j<results.length;j++){
					list.add(results[j]);
				}
			}
			return (CountResult[])list.toArray(new CountResult[list.size()]);
		} else {
			StepCounter counter = this.factory.getCounter(file.getName());
			if(counter!=null){
				CountResult result = counter.count(file, Util.getFileEncoding(file));
				return new CountResult[]{result};
			} else {
				// 未対応の形式の場合は形式にnullを設定して返す
				return new CountResult[]{
					new CountResult(file, file.getName(), null, null, 0, 0, 0)
				};
			}
		}
	}

	/** ディレクトリ付きファイル名の出力形式を取得します。 */
	private String getFileNameWithDir(File file) throws IOException {
		if (file.isDirectory()) {
			return file.getName();
		}
		if (this.files == null || this.files.length == 0) {
			return file.getName();
		}
		// ファイルの正規パスを取得します。
		String filePath = file.getCanonicalPath();
		for (File f : this.files) {
			String parentPath = f.getCanonicalPath();
			if (filePath.contains(parentPath)) {
				// 引数の正規パスにファイルが含まれている場合、
				// 選択されたディレクトリからのパスとファイル名を返却します。
				StringBuilder builder = new StringBuilder();
				builder.append('/');
				builder.append(f.getName());
				builder.append(filePath.substring(parentPath.length()).replaceAll("\\\\", "/"));
				return builder.toString();
			}
		}
		return file.getName();
	}

}
