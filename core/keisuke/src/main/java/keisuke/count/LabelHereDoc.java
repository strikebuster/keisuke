package keisuke.count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * keisuke:追加クラス
 * ヒアドキュメント形式のリテラル文字列への対応
 */
public class LabelHereDoc extends LiteralString {
	
	// ラベル終了リテラル（ヒアドキュメント）用の変数
	public static String LABEL_MAGIC_WORD = "@?@";
	private boolean labelEnclose = false;
	private String label = "";
	
	/**
	 * 開始記号と終了記号を指定したコンストラクタ
	 *
	 * @param start 開始記号文字列
	 * @param end   終了記号文字列
	 */
	public LabelHereDoc(String start, String end) {
		setStartString(start); // Override
		setEndString(end);
	}
	
	/**
	 * リテラルの開始記号を設定します
	 *
	 * @param start 開始記号文字列
	 */
	@Override
	public void setStartString(String start){
		if (start == null) {
			this.start = "";
			return;
		}
		int pos = start.indexOf(LABEL_MAGIC_WORD);
		if (pos > 0) {
			// １文字以上の文字列の後ろに"@?@"があればラベル指定と判定
			this.labelEnclose = true;
		}
		super.setStartString(start);
	}
	/**
	 * リテラルの開始記号を取得します
	 *
	 * @param なし
	 */
	@Override
	public String getStartString(){
		int pos = -1;
		if (this.labelEnclose) {
			pos = this.start.indexOf(LABEL_MAGIC_WORD);
		}
		if (pos > 0) {
			// 可変部は事前に不明なのでprefixで判定するためにそれだけ返す
			return this.start.substring(0, pos);
		}
		return super.getStartString();
	}
	
	/**
	 * リテラルの終了記号を取得します
	 *
	 * @param なし
	 */
	@Override
	public String getEndString(){
		if (this.labelEnclose) {
			// 可変部前後に空白許容も想定しラベル終了リテラルではこのメソッドでなく
			// getEndRegxString()を使う
			return this.end.replace(LABEL_MAGIC_WORD, this.label);
		}
		return super.getEndString();
	}
	
	/**
	 * リテラルの終了ラベルを設定します
	 *
	 * @param label 終了ラベル文字列
	 */
	public void setLabelString(String label){
		if (label == null || label.length() == 0) {
			this.label = "";
		}
		this.label = label;
	}
	
	/**
	 * リテラルの終了ラベルを取得します
	 *
	 * @return 終了ラベル文字列
	 */
	public String getLabelString(){
		return this.label;
	}
	
	/** 
	 *  ラベル終了のヒアドキュメント形式
	 *  "@?@"の部分を抽出するPattern用正規表現文字列
	 *  　　固定部と可変ラベル部の間に空白を許可する言語も想定したパターン化
	 *  
	 *  その他
	 *  "@!@"や"@+@"の部分を抽出するPattern用正規表現文字列
	 *  親クラスで実装されている
	 *  
	 *  を返す
	 */
	@Override
	public String getStartRegxString(){
		int pos;
		String preStr = "";
		String postStr = "";
		StringBuilder sb = new StringBuilder();
		if (this.labelEnclose) {
			// prefix[ \t]*(["'`]?\w+["'`]?)[ \t]*postfix
			pos = this.start.indexOf(LABEL_MAGIC_WORD);
			preStr = this.start.substring(0,pos);
			pos += LABEL_MAGIC_WORD.length();
			if (pos < this.start.length()) {
				postStr = this.start.substring(pos, this.start.length()); 
			}	
			sb.append(Pattern.quote(preStr));
			sb.append("[ \\t]*[\"'`]?(\\w+)[\"'`]?");
			if (postStr.length() > 0) {
				sb.append("[ \\t]*");
				sb.append(Pattern.quote(postStr));
			}
			return sb.toString();
		}
		return super.getStartRegxString();
	}
	
	/** 
	 *  ラベル終了のヒアドキュメント形式
	 *  "@?@"の部分を抽出するPattern用正規表現文字列
	 *  　　固定部と可変ラベル部の間に空白を許可する言語も想定したパターン化
	 *  
	 * その他
	 *  ％記法の区切り文字までを抽出するPattern用正規表現文字列
	 *  親クラスで実装されている
	 *  
	 *  を返す
	 */
	@Override
	public String getEndRegxString(){
		StringBuilder sb = new StringBuilder();
		if (this.labelEnclose) {
			// prefix[ \t]*label[ \t]*postfix 
			String preStr = "";
			int pos = this.end.indexOf(LABEL_MAGIC_WORD);
			if (pos > 0) {
				// ラベルは行頭なので通常はないはずだが、拡張性のため
				preStr = this.end.substring(0,pos);
			}
			String postStr = "";
			pos += LABEL_MAGIC_WORD.length();
			if (pos < this.end.length()) {
				postStr = this.end.substring(pos, this.end.length()); 
			}
			if (preStr.length() > 0) {
				sb.append(Pattern.quote(preStr));
				sb.append("[ \\t]*");
			}
			sb.append(Pattern.quote(this.label));
			if (postStr.length() > 0) {
				sb.append("[ \\t]*");
				sb.append(Pattern.quote(postStr));
			}
			return sb.toString();
		}
		return super.getEndRegxString();
	}
	
	/**
	 * 保持する終了記号が文字列中にあるか探索する
	 *
	 * @line　 処理対象文字列
	 * @return 開始記号があった場合の開始インデックス、なければ-1
	 */
	@Override
	public int searchEnd(String line) {
		if (this.labelEnclose) {
			// ラベル終了リテラル
			String patternStr = getEndRegxString();
			//System.out.println("[DEBUG] HereDoc end pattern = " + patternStr);
			//System.out.println("[DEBUG] line = " + line);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				// リテラル終了
				int pos = matcher.end();
				return pos;
			}
			// リテラル終了していない
			return -1;
		}
		return super.searchEnd(line);
	}
}
