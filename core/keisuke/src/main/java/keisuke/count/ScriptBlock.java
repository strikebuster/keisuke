package keisuke.count;

/**
 * keisuke:追加クラス
 * Scriptletの開始終了とScript言語タイプ
 */
public class ScriptBlock extends AbstractBlock {
	
	// Scriptタグの文字比較は大文字小文字無視
	private boolean caseInsense = true;
	// Script言語指定文字列を保持する変数
	private String scriptLang = null;
	

	/**
	 * 開始記号文字列と終了記号文字列を指定してAreaScriptを生成します。
	 *
	 * @param start 開始文字列
	 * @param end   終了文字列
	 */
	public ScriptBlock(String start,String end){
		setStartString(start);
		setEndString(end);
	}
	
	/**
	 * 大文字小文字無視フラグを設定します。
	 *
	 * @param 大文字小文字無視フラグ
	 */
	public void setCaseInsense(boolean bool){
		this.caseInsense = bool;
	}

	/**
	 * 大文字小文字無視フラグのbool値を返します。
	 *
	 * @param なし
	 */
	public boolean checkCaseInsense(){
		return this.caseInsense;
	}

	/**
	 * Script言語を設定します。
	 *
	 * @param 言語
	 */
	public void setScriptLang(String lang){
		this.scriptLang = lang;
	}
	
	/**
	 * Script言語を取得します。
	 *
	 * @param なし
	 */
	public String getScriptLang(){
		return this.scriptLang;
	}
	
	/**
	 * 保持する終了記号が文字列中にあるか探索する
	 * ただしソースコードからすると外部リテラルの開始を意味する
	 *
	 * @line　 処理対象文字列
	 * @return 終了記号があった場合の終端インデックス、なければ-1
	 */
	public int searchEnd(String line){
		int pos = -1;
		String end = getEndString();		
		if (checkCaseInsense()) {
			pos = line.toUpperCase().indexOf(end.toUpperCase());
		} else {
			pos = line.indexOf(end);	
		}
		// ScriptBlockの終了記号は記号開始インデックスを返す
		return pos;
	}
	
	/**
	 * 保持する開始記号が文字列中にあるか探索する
	 * ただしソースコードからすると外部リテラルの終了を意味する
	 *
	 * @line　 処理対象文字列
	 * @return 開始記号があった場合の開始インデックス、なければ-1
	 */
	public int searchStart(String line) {
		int pos = -1;
		String start = getStartString();
		if (checkCaseInsense()) {
			pos = line.toUpperCase().indexOf(start.toUpperCase());
		} else {
			pos = line.indexOf(start);
		}
		return pos;
	}
}
