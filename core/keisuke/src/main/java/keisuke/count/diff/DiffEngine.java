package keisuke.count.diff;

import java.util.List;

import difflib.ChangeDelta;
import difflib.Chunk;
import difflib.DeleteDelta;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.InsertDelta;
import difflib.Patch;
import keisuke.util.StringUtil;

/**
 * 指定された２つの文字列のDiff差分を抽出する
 */
public class DiffEngine {

	private DiffCountHandler handler;
	private List<String> text1;
	private List<String> text2;

	/**
	 * コンストラクタ。
	 * @param string1 変更前の文字列（nullの場合は空文字列として扱います）
	 * @param string2 変更後の文字列（nullの場合は空文字列として扱います）
	 */
	public DiffEngine(final String string1, final String string2) {
		this.handler = new DiffCountHandler();
		if (string1 == null) {
			this.text1 = StringUtil.splitListOfLinesFrom("");
		} else {
			this.text1 = StringUtil.splitListOfLinesFrom(string1);
		}
		if (string2 == null) {
			this.text2 = StringUtil.splitListOfLinesFrom("");
		} else {
			this.text2 = StringUtil.splitListOfLinesFrom(string2);
		}
	}

	/**
	 * 差分取得処理を実行します。
	 */
	public void doDiff() {
		int linePos1 = 0;
		int linePos2 = 0;

		Patch<String> patch = DiffUtils.diff(this.text1, this.text2);
		for (Delta<String> delta : (List<Delta<String>>) patch.getDeltas()) {
			Chunk<String> orgChunk = delta.getOriginal();
			Chunk<String> revChunk = delta.getRevised();

			while (linePos1 != orgChunk.getPosition()) {
				this.handler.match();
				linePos1++;
			}
			//linePos1  = orgChunk.getPosition();
			linePos2 = revChunk.getPosition();
			if (delta instanceof InsertDelta) {
				while (linePos2 <= revChunk.last()) {
					this.handler.add();
					linePos2++;
				}
			} else if (delta instanceof DeleteDelta) {
				while (linePos1 <= orgChunk.last()) {
					this.handler.delete();
					linePos1++;
				}
			} else if (delta instanceof ChangeDelta) {
				while (linePos1 <= orgChunk.last()) {
					this.handler.delete();
					linePos1++;
				}
				while (linePos2 <= revChunk.last()) {
					this.handler.add();
					linePos2++;
				}
			}
			linePos1 = orgChunk.last() + 1;
			linePos2 = revChunk.last() + 1;
		}

		while (this.text2.size() > linePos2) {
			this.handler.match();
			linePos2++;
		}
	}

	/**
	 * 追加された行数を返す
	 * @return 追加行数
	 */
	public long countAddedSteps() {
		return this.handler.getAddedSteps();
	}

	/**
	 * 削除された行数を返す
	 * @return 削除行数
	 */
	public long countDeletedSteps() {
		return this.handler.getDeletedSteps();
	}

	/**
	 * 変更行数をカウントするためのクラス
	 */
	private static class DiffCountHandler {

		private long addedSteps = 0;
		private long deletedSteps = 0;

		/**
		 * 行が追加されているときの処理をする
		 */
		void add() {
			this.addedSteps++;
		}

		/**
		 * 行が削除されているときの処理をする
		 */
		void delete() {
			this.deletedSteps++;
		}

		/**
		 * 行が変更されていないときの処理をする
		 */
		void match() { }

		/**
		 * 追加された行数を返す
		 * @return 追加行数
		 */
		long getAddedSteps() {
			return this.addedSteps;
		}

		/**
		 * 削除された行数を返す
		 * @return 削除行数
		 */
		long getDeletedSteps() {
			return this.deletedSteps;
		}
	}
}
