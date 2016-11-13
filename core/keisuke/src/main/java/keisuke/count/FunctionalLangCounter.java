package keisuke.count;

/**
 * keisuke: 追加クラス
 * 関数型言語（コメント化式への対応）用のステップカウンタ
 */
public class FunctionalLangCounter extends DefaultStepCounter {
	
	/** 通常のPROGRAM言語のコンストラクタ */
	public FunctionalLangCounter() {
		super();
	}
	
	/** コメント化された式のチェックし、コメント部分除き有効な文字列を返す */
	@Override
	protected String dealProgramCode(ProgramLangRule pr, String line, boolean codeFlag) {
		// lineの内容はコメントの抜かれたソースコードのみからなる文字列
		CommentExpr expr = onCommentExpr;
		if ( expr == null ) {
			// コメント式ではないのですべて出力
			return line;
		} else if (codeFlag == false) {
			// コメント式でかつリテラルのみ
			return "";
		}
		// コメント式でコードがある行なので解析必要
		int pos = expr.searchEnd(line);
		if (pos < 0) {
			// コメント式の中
			return "";
		}
		// コメント式が終了
		onCommentExpr = null;
		if ( pos < line.length() ) {
			// 終了記号の右側の処理
			// コメント式の対象範囲外なので、dealProgramCode()を被せない
			return line.substring(pos);
		}
		return "";
	}
	
	/** 複数行ブロックコメントの開始から行末までの処理をする */
	@Override
	public String dealAreaCommentStart(ProgramLangRule pr, String line, AreaComment area) {
		if (area instanceof CommentExpr) {
			// コメント式
			CommentExpr expr = (CommentExpr)area;
			// コメント式開始の確認とネスト処理をする
			int pos = expr.validateCommentExpressionStart(line, onCommentExpr);
			if (onCommentExpr == null) {
				onCommentExpr = expr;
			}
			onAreaComment = null;
			// 開始記号より右のコメントの処理
			return removeCommentFromLeft(pr, line.substring(pos), 1);
		} else {
			// 通常の複数行コメント
			return super.dealAreaCommentStart(pr, line, area);
		}
	}
}
