package keisuke.count.language.parse;

import keisuke.count.syntax.AbstractBlock;

/**
 * ソースコードの解析中のコード要素を保持する
 */
public class ParseSyntaxRule {
	private SyntaxStatus status;
	private AbstractBlock subject;

	public ParseSyntaxRule(final SyntaxStatus stat) {
		if (stat == null) {
			throw new RuntimeException("SyntaxStatus does not allow null.");
		}
		this.status = stat;
		this.subject = null;
	}

	public ParseSyntaxRule(final SyntaxStatus stat, final AbstractBlock obj) {
		if (stat == null) {
			throw new RuntimeException("SyntaxStatus does not allow null.");
		}
		this.status = stat;
		if (obj == null) {
			throw new RuntimeException("Object does not allow null.");
		}
		this.subject = obj;
	}

	/**
	 * 保持するSyntaxStatusの値を返す
	 * @return SyntaxStatusの値
	 */
	public SyntaxStatus status() {
		return this.status;
	}

	/**
	 * 保持する解析ルール要素を返す
	 * @return 解析ルール要素
	 */
	public AbstractBlock subject() {
		return this.subject;
	}
}
