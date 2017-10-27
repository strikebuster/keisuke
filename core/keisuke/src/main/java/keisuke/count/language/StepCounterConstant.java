package keisuke.count.language;

/**
 * PROGRAM言語のStepCounterオブジェクトで利用する定数定義
 */
public final class StepCounterConstant {

	private StepCounterConstant() { }

	// 対象ソースファイルが無視対象か指定する定数定義
	protected static final boolean IGNORE_YES = true;
	protected static final boolean IGNORE_NON = false;

	// 処理対象の文字列が行頭を含むか指定する定数定義
	protected static final boolean LINE_HEAD = true;
	protected static final boolean LINE_NO_HEAD = false;

	// 有効行を取り出す際にリテラル以外の有効行文字が含まれるか示すフラグ
	protected static final boolean HAVING_VALID_STATEMENT = true;
	protected static final boolean HAVING_ONLY_LITERAL = false;

}
