package keisuke.count.diff.renderer;

/**
 * jp.sf.amateras.stepcounter.diffcout.DiffFolderResultとの互換性を持つためのインタフェース
 * Excel形式の出力をするために利用するExcelテンプレートのファイル別集計シートは
 * このインタフェースを持つオブジェクトのデータを取り込める
 * テンプレートをオリジナルのStepCounterと共有可能にするために必要な互換性
 */
public interface DiffResultCompatible {

	/**
	 * 計測対象のファイル/ディレクトリパスを返す
	 * @return ファイル/ディレクトリパス
	 */
	String getPath();

	/**
	 * ソースファイルタイプを返す
	 * @return ソースファイルタイプ
	 */
	String sourceType();

	/**
	 * 差分変更ステータスを返す
	 * @return 差分変更ステータス値
	 */
	String getStatus();

	/**
	 * カテゴリを返す
	 * @return カテゴリ名
	 */
	String getCategory();

	/**
	 * 差分計測結果の増分行数を返す
	 * @return 増分行数
	 */
	long getAddCount();

	/**
	 * 差分計測結果の削減行数を返す
	 * @return 削減行数
	 */
	long getDelCount();

}
