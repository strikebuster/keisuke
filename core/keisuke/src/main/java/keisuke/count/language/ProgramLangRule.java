package keisuke.count.language;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.LineComment;
import keisuke.count.syntax.LiteralString;
import keisuke.count.syntax.SyntaxRules;

/**
 * Program言語用のコメント記号等の解析ルール
 */
public class ProgramLangRule implements SyntaxRules {
	// 処理対象とするScript LanguageのType（Factoryで指定）
	private String langName = "UNDEF";

	// スキップ記号を正規表現で記述したPatternのリスト
	private List<Pattern> skipPatterns = new ArrayList<Pattern>();
	// 単一行コメント記号のリスト
	private List<LineComment> lineComments = new ArrayList<LineComment>();
	// 複数行コメント領域の開始・終了記号のリスト
	private List<AreaComment> areaComments = new ArrayList<AreaComment>();
	// リテラル文字列の開始・終了記号
	private List<LiteralString> literalStrings = new ArrayList<LiteralString>();

	// 旧世代言語で大文字小文字の区別がない比較をするフラグ
	private boolean caseInsense = false;

	// Python用の左端インデントをcut時に整形・保持するフラグ
	private boolean indentOpt = false;

	/** コンストラクタ */
	public ProgramLangRule() { }

	/**
	 * プログラミング言語名を設定する
	 * @param lang プログラミング言語名
	 */
	protected void setLanguageName(final String lang) {
		this.langName = lang;
	}

	/**	{@inheritDoc} */
	public String languageName() {
		return this.langName;
	}

	/**
	 * スキップするパターン（正規表現）を追加します
	 * @param pattern スキップする行の正規表現パターン
	 */
	protected void addSkipPattern(final String pattern) {
		this.skipPatterns.add(Pattern.compile(pattern));
	}

	/**	{@inheritDoc} */
	public List<Pattern> skipPatterns() {
		return this.skipPatterns;
	}

	/**
	 * 単一行コメントの記号定義を追加します
	 * @param linecomm 行コメント定義インスタンス
	 */
	protected void addLineComment(final LineComment linecomm) {
		this.lineComments.add(linecomm);
	}

	/**	{@inheritDoc} */
	public List<LineComment> lineComments() {
		return this.lineComments;
	}

	/**
	 * 複数行コメントの記号定義を追加します
	 * @param area ブロックコメント定義インスタンス
	 */
	protected void addAreaComment(final AreaComment area) {
		this.areaComments.add(area);
	}

	/**	{@inheritDoc} */
	public List<AreaComment> areaComments() {
		return this.areaComments;
	}

	/**
	 * リテラル文字列の記号定義を追加します
	 * @param literal リテラル定義インスタンス
	 */
	protected void addLiteralString(final LiteralString literal) {
		this.literalStrings.add(literal);
	}

	/**	{@inheritDoc} */
	public List<LiteralString> literalStrings() {
		return this.literalStrings;
	}

	/**
	 * 大文字小文字の区別のない言語か設定する
	 * @param bool 区別ない場合はtrueを指定する
	 */
	protected void setCaseInsense(final boolean bool) {
		this.caseInsense = bool;
	}

	/**	{@inheritDoc} */
	public boolean isCaseInsense() {
		return this.caseInsense;
	}

	/**
	 * この言語がインデントブロックを使うか設定する
	 * @param bool 使い場合はtrueを指定する
	 */
	protected void setUsingIndentBlock(final boolean bool) {
		this.indentOpt = bool;
	}

	/**	{@inheritDoc} */
	public boolean isUsingIndentBlock() {
		return this.indentOpt;
	}
}
