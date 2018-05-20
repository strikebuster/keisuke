package keisuke.count.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import keisuke.count.language.parse.ParseSyntaxRule;
import keisuke.count.language.parse.SyntaxStatus;
import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.LabelHereDoc;
import keisuke.count.syntax.LiteralString;
import keisuke.util.LogUtil;

/**
 * ラベル終了形式のヒアドキュメント構文のあるスクリプト言語用のステップカウンタ
 */
public class LabelHereDocScriptCounter extends GeneralStepCounter {

	/**
	 * 通常のPROGRAM言語のコンストラクタ
	 */
	public LabelHereDocScriptCounter() {
		super();
	}

	/**
	 * Scriptlet言語であることを指定したコンストラクタ
	 * @param sflag Scriptlet言語の場合にtrueを指定
	 */
	public LabelHereDocScriptCounter(final boolean sflag) {
		super(sflag);
	}

	/* リテラル文字列の開始から行末までの処理をする */
	@Override
	public String handleLiteralStringStart(
			final ProgramLangRule lang, final String line, final LiteralString literal) {
		StringBuilder sb = new StringBuilder();
		int pos = -1;
		if (literal instanceof LabelHereDoc) {
			LabelHereDoc heredoc = (LabelHereDoc) literal;
			// ラベル終了リテラルのラベル宣言を抽出
			String patternStr = heredoc.getStartRegxString();
			//LogUtil.debugLog("HereDoc start pattern = " + patternStr);
			//LogUtil.debugLog("line = " + line);
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				// ラベル読み取り
				String label = matcher.group(1);
				heredoc.setLabelString(label);
				//LogUtil.debugLog("label = " + label);
				pos = matcher.end();
				// 開始記号およびラベルの処理
				sb.append(this.handleValidCode(lang, line.substring(0, pos), false));
				// ラベル宣言より右の処理
				sb.append(this.removeCommentFromLeft(lang, line.substring(pos), 1));
				//右側の処理結果に不正なブロックコメント定義があった場合の備え
				ParseSyntaxRule rule = this.peekStatusAsSomeRule();
				if (rule != null && rule.status() == SyntaxStatus.IN_COMMENT) {
					// 文法的に正しくないがあったときのため
					LogUtil.warningLog("ignore blockcomment: "
							+ ((AreaComment) rule.subject()).getStartString());
					this.popStatusAsCommentRule();
				}
			} else {
				// ラベルが見つからない
				LogUtil.warningLog("here doc label is not found.");
				this.popStatusAsLiteralRule(); // pop(IN_LITERAL&LabelHereDoc)
				// 開始記号を処理
				pos = heredoc.getStartString().length();
				sb.append(this.handleValidCode(lang, line.substring(0, pos)));
				// 右側を処理、空でもremoveCommentFromLeft()を呼ぶ
				sb.append(this.removeCommentFromLeft(lang, line.substring(pos), 1));
			}
			return sb.toString();

		} else {
			// 通常の引用符リテラル
			return super.handleLiteralStringStart(lang, line, literal);
		}
	}
}
