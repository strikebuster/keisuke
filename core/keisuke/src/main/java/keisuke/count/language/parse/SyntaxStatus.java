package keisuke.count.language.parse;

/**
 * ソースコード内部の解析中のコード要素を示す処理状態定義
 */
public enum SyntaxStatus {
	/** 解析対象のソースコード内部処理中 */
	IN_PROGRAM,

	/** 有効行でなく解析対象外のリテラル定義処理中 */
	IN_LITERAL,

	/** 有効行でなく解析対象外のコメント記述処理中 */
	IN_COMMENT,

	/** 有効行でないが解析すべきコメント式の内部処理中 */
	IN_COMMENT_EXPR
}
