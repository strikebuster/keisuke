package keisuke.count.language.parse;

import keisuke.count.language.ProgramLangRule;
import keisuke.count.syntax.ScriptBlock;

/**
 * ソースコード解析の現在処理中の状態を保持する
 */
public class ParseSourceCode {
	private SourceCodeStatus status;
	private ScriptBlock scriptBlock;
	private ProgramLangRule language;

	public ParseSourceCode(final SourceCodeStatus stat) {
		if (stat == null) {
			throw new RuntimeException("ScriptletStatus does not allow null.");
		}
		this.status = stat;
		this.scriptBlock = null;
		this.language = null;
	}

	public ParseSourceCode(final SourceCodeStatus stat,
			final ScriptBlock block, final ProgramLangRule lang) {
		if (stat == null) {
			throw new RuntimeException("ScriptletStatus does not allow null.");
		}
		this.status = stat;
		if (block == null) {
			throw new RuntimeException("ScriptBlock does not allow null.");
		}
		this.scriptBlock = block;
		if (lang == null) {
			throw new RuntimeException("ProgramLangRule does not allow null.");
		}
		this.language = lang;
	}

	/**
	 * 保持するSourceCodeStatusの値を返す
	 * @return SourceCodeStatusの値
	 */
	public SourceCodeStatus status() {
		return this.status;
	}

	/**
	 * 保持するScriptBlock要素を返す
	 * @return ScriptBlock要素
	 */
	public ScriptBlock scriptBlock() {
		return this.scriptBlock;
	}

	/**
	 * 保持するプログラ言語ルール定義を返す
	 * @return プログラ言語ルール定義
	 */
	public ProgramLangRule language() {
		return this.language;
	}
}
