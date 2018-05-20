package keisuke.count.step.format;

/**
 * jp.sf.amateras.stepcounter.CountResultとの互換性を持つためのインタフェース
 * Excel形式の出力をするために利用するExcelテンプレートのファイル別集計シートは
 * このインタフェースを持つオブジェクトのデータを取り込める
 * テンプレートをオリジナルのStepCounterと共有可能にするために必要な互換性
 */
public interface CountResultCompatible {

	/**
	 * 計測対象のファイル名（パス）を返す
	 * @return ファイル名
	 */
	String getFileName();

	/**
	 * 計測対象のファイルのソースコードタイプを返す
	 * @return ソースコードタイプ
	 */
	String getFileType();

	/**
	 * 計測対象のファイルに指定されたカテゴリ文字列を返す
	 * @return カテゴリ文字列
	 */
	String getCategory();

	/**
	 * 計測対象のソースコードの実行ステップ数を返す
	 * @return 実行スッテプ数
	 */
	long getStep();

	/**
	 * 計測対象のソースコードの空白ステップ数を返す
	 * @return 空白ステップ数
	 */
	long getNon();

	/**
	 * 計測対象のソースコードのコメントステップ数を返す
	 * @return コメントステップ数
	 */
	long getComment();
}
