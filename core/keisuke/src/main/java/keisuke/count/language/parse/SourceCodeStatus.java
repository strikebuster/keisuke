package keisuke.count.language.parse;

/**
 * Scriptletプログラムのようにソースファイルがリテラル部分とソースコード部分に
 * 分かれているときに、どちらの部分を処理中かの状態を定義
 */
public enum SourceCodeStatus {
	/** Scriptlet外部のリテラル定義のコード */
	EXTERNAL_LITERAL,

	/** Scriptletブロックの内部で解析対象のコード */
	IN_SCRIPTLET
}
