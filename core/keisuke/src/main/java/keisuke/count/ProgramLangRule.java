package keisuke.count;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** 
 * keisuke:追加クラス
 * Program言語用のコメント記号等の設定値保持クラス
 */
public class ProgramLangRule {
	// 処理対象とするScript LanguageのType（Factoryで指定）
	private String langType = "UNDEF";
			
	// スキップ記号を正規表現で記述したPatternのリスト
	protected List<Pattern> skipPatterns = new ArrayList<Pattern>();
			
	// 単一行コメント記号のリスト
	protected List<LineComment> lineComments = new ArrayList<LineComment>();
	// 複数行コメント領域の開始・終了記号のリスト
	protected List<AreaComment> areaComments = new ArrayList<AreaComment>();
			
	// リテラル文字列の開始・終了記号
	protected List<LiteralString> literalStrings = new ArrayList<LiteralString>();
			
	// 旧世代言語で大文字小文字の区別がない比較をするフラグ
	protected boolean caseInsense = false;
			
	// Python用の左端インデントをcut時に整形・保持するフラグ
	protected boolean indentOpt = false;
	
	/** コンストラクタ */
	public ProgramLangRule() { }
	
	protected void setLangType(String langType) {
		this.langType = langType;
	}
			
	protected String getLangType(){
		return this.langType;
	}
	
	/** スキップするパターン（正規表現）を追加します */
	protected void addSkipPattern(String pattern){
		skipPatterns.add(Pattern.compile(pattern));
	}

	/** スキップするパターンを取得します */
	protected Pattern[] getSkipPatterns(){
		return skipPatterns.toArray(new Pattern[skipPatterns.size()]);
	}

	/** 単一行コメントの開始文字列を追加します */
	protected void addLineComment(LineComment linecomm){
		lineComments.add(linecomm);
	}

	/** 複数行コメントを追加します */
	protected void addAreaComment(AreaComment area){
		areaComments.add(area);
	}
	
	/** リテラル文字列を追加します */
	protected void addLiteralString(LiteralString literal){
		literalStrings.add(literal);
	}
	
	/** 大文字小文字無視フラグをTrueにします */
	protected void setCaseInsenseTrue(){
		this.caseInsense = true;
	}
	
	/** インデント保持フラグをTrueにします */
	protected void setIndentOptTrue(){
		this.indentOpt = true;
	}
}
