package keisuke.swing.diffcount;

import keisuke.count.diff.DiffCountProc;
import keisuke.count.diff.DiffFolderResult;
import keisuke.report.property.MessageDefine;
import keisuke.swing.AbstractCountAdapter;

/**
 * 差分行数計測機能のSwing画面からDiffCountFuncを呼び出すためのAdapter
 *
 */
class DiffCountAdapter extends AbstractCountAdapter {

	DiffCountAdapter(final String encode, final String xmlname, final MessageDefine msgdef) {
		this.setCountProc(new DiffCountProc());
	}

	/**
	 * 格納済みの差分行数計測結果を返す。
	 * @return 差分行数計測結果
	 */
	DiffFolderResult getCountedResultAsRaw() {
		return ((DiffCountProc) this.countProc()).getResultAsRawData();
	}
}
