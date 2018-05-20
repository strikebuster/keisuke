package keisuke.count.language;

import keisuke.count.language.parse.ParseSyntaxRule;
import keisuke.count.syntax.AreaComment;
import keisuke.count.syntax.CommentExpr;
import keisuke.util.LogUtil;

/**
 * 関数型言語（コメント化式への対応）用のステップカウンタ
 */
public class FunctionalLangCounter extends GeneralStepCounter {

	/**
	 * コンストラクタ
	 */
	public FunctionalLangCounter() {
		super();
	}

	/* コメント化された式のチェックし、コメント部分除き有効な文字列を返す */
	@Override
	protected String handleValidCode(final ProgramLangRule lang, final String line, final boolean validCodeFlag) {
		// lineの内容はコメントの抜かれたソースコードのみからなる文字列
		if (!this.isInsideOfCommentExpressionRule()) {
			// コメント式ではないのですべて出力
			return line;
		}
		//コメント式の内部に解析すべき本来は有効行だったコードが含まれるか
		if (!validCodeFlag) {
			// リテラルのみ
			return "";
		}
		// コメント式でコードがある行なので解析必要
		CommentExpr expr = (CommentExpr) this.peekStatusAsCommentExpressionRule().subject();
		int pos = expr.searchEndingRightBlacket(line);
		if (pos < 0) {
			// コメント式の中
			return "";
		}
		// コメント式が終了
		this.popStatusAsCommentExpressionRule();
		if (pos < line.length()) {
			// 終了記号の右側の処理
			// コメント式の対象範囲外なので、dealProgramCode()を被せない
			return line.substring(pos);
		}
		// コメント式の右側は空
		return "";
	}

	/* 複数行ブロックコメントの開始から行末までの処理をする */
	@Override
	public String handleAreaCommentStart(final ProgramLangRule lang, final String line, final AreaComment area) {
		if (area instanceof CommentExpr) {
			// コメント式
			CommentExpr expr = (CommentExpr) area;
			// ブロックコメントの取り消し
			this.popStatusAsCommentRule();
			// コメント式開始の確認とネスト処理をする
			CommentExpr parentCommentExpr = null;
			if (this.isInCommentExpressionRule()) { // 既にコメント式の中
				ParseSyntaxRule rule = this.peekStatusAsCommentExpressionRule();
				parentCommentExpr = (CommentExpr) rule.subject();
			}
			int pos = expr.validateCommentExpressionStart(line, parentCommentExpr);
			if (parentCommentExpr == null) {
				//ネスト内部でないコメント式なので改めてスタックへ登録
				this.pushNewStatusAsCommentExpressionRuleWith(expr);
				//LogUtil.debugLog("Comment expr: " + expr.getStartString());
			}
			// 開始記号より右のコメントの処理
			return this.removeCommentFromLeft(lang, line.substring(pos), 1);
		} else {
			// 通常の複数行コメント
			return super.handleAreaCommentStart(lang, line, area);
		}
	}

	/**
	 * LogUtilのインポートを正当化するためのダミー
	 */
	static void noUsingDummy() {
		LogUtil.warningLog("This must not be called, because dummy");
	}
}
