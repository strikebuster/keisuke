package keisuke.count;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * keisuke:追加クラス
 * ラベル終了形式のヒアドキュメント構文のあるスクリプト言語用のステップカウンタ
 */
public class LabelHereDocScriptCounter extends DefaultStepCounter {

	/** 通常のPROGRAM言語のコンストラクタ */
	public LabelHereDocScriptCounter() {
		super();
	}
	
	/** Scriptlet言語のコンストラクタ */
	public LabelHereDocScriptCounter(boolean sflag) {
		super(sflag);
	}
	
	/** リテラル文字列の開始から行末までの処理をする */
	@Override
	public String dealLiteralStringStart(ProgramLangRule pr, String line, LiteralString literal) {
		StringBuilder sb = new StringBuilder();
		int pos = -1;
		if (literal instanceof LabelHereDoc) {
			LabelHereDoc heredoc = (LabelHereDoc)literal;
			// ラベル終了リテラルのラベル宣言を抽出
			String patternStr = heredoc.getStartRegxString();
			//System.out.println("[DEBUG] HereDoc start pattern = " + patternStr);
			//System.out.println("[DEBUG] line = " + line);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				// ラベル読み取り
				String label = matcher.group(1);
				heredoc.setLabelString(label);
				//System.out.println("[DEBUG] label = " + label);
				pos = matcher.end();
				// 開始記号およびラベルの処理
				sb.append(dealProgramCode(pr, line.substring(0,pos), false));
				// ラベル宣言より右の処理
				sb.append(removeCommentFromLeft(pr, line.substring(pos), 1));
				if ( onAreaComment != null ) {
					// 文法的に正しくないがあったときのため
					onAreaComment = null;
				}
			} else {
				// ラベルが見つからない
				onLiteralString = null;
				//System.out.println("[DEBUG] label = Not found !!");
				// 開始記号を処理
				pos = heredoc.getStartString().length();
				sb.append(dealProgramCode(pr, line.substring(0,pos)));
				// 右側を処理、空でもremoveCommentFromLeft()を呼ぶ
				sb.append(removeCommentFromLeft(pr, line.substring(pos), 1));
			}
			return sb.toString();
	
		} else {
			// 通常の引用符リテラル
			return super.dealLiteralStringStart(pr, line, literal);
		}
	}
}
