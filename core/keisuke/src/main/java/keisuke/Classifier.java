package keisuke;

import java.util.List;

/**
 * Interface for classifier of source files.
 */
public interface Classifier {

	/**
	 * ソースファイルの分類を返す
	 * @param path target source file
	 * @return name of classifier
	 */
	String getClassifyName(String path);

	/**
	 * 分類識別子の出力レポート用の名称を返す
	 * @param classifyname name of classifier
	 * @return title for reporting
	 */
	String getClassifyNameForReport(String classifyname);

	/**
	 * 分類識別子がFrameworkの場合にソースの分類タイプが定義で固定されるので
	 * 分類タイプのリストを返す
	 * 分類識別子がFramework以外の時は固定されないのでnullを返す
	 * @return list of something
	 */
	List<String> getClassifyFixedList();

}
