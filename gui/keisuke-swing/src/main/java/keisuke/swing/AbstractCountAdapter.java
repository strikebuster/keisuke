package keisuke.swing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import keisuke.count.AbstractCountMainProc;
import keisuke.util.StringUtil;

/**
 * Swing画面からStepCountProc/DiffCountProcを呼び出すためのAdapterの
 * 共通処理を持つ基底クラス
 */
public class AbstractCountAdapter {

	private AbstractCountMainProc countProc = null;

	protected AbstractCountAdapter() { }

	/**
	 * 行数計測機能の処理インスタンスを返す
	 * @return 行数計測機能の処理インスタンス
	 */
	protected AbstractCountMainProc countProc() {
		return this.countProc;
	}

	/**
	 * 行数計測機能の処理インスタンスを設定する
	 * @param proc 行数計測機能の処理インスタンス
	 */
	protected void setCountProc(final AbstractCountMainProc proc) {
		this.countProc = proc;
	}

	/**
	 * 言語定義XMLファイル名を設定する
	 * @param filename 定義XMLファイル名
	 */
	public void setXml(final String filename) {
		this.countProc.setXmlFileName(filename);
	}

	/**
	 * エンコードを設定する
	 * @param encoding エンコード文字列
	 */
	public void setEncoding(final String encoding) {
		this.countProc.setSourceEncoding(encoding);
	}

	/**
	 * フォーマット種類を設定する
	 * @param format フォーマット種類文字列
	 */
	public void setFormat(final String format) {
		this.countProc.setFormat(format);
	}

	/**
	 * 計測対象に対し計測を実行して結果のフォーマット済みテキストを返す
	 * @param files 計測対象ファイル名を指定する配列
	 * @return 計測結果テキスト
	 */
	public String getCountingResultAsText(final String[] files) {
		String outText = "";
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			this.countProc().doCountingAndWriting(files, stream);
			outText = stream.toString();
		} catch (IOException ioEx) {
			outText = ioEx.toString();
		} finally {
			try {
				stream.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return StringUtil.normalizeLineSeparator(outText);
	}

	/**
	 * 計測対象に対し計測を実行して結果データのバイト配列を返す
	 * 結果データはExcel形式
	 * @param files 計測対象ファイル名を指定する配列
	 * @return 計測結果バイト配列
	 */
	public byte[] getCountingResultAsBytes(final String[] files) {
		byte[] outData;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			this.countProc().doCountingAndWriting(files, stream);
			outData = stream.toByteArray();
		} catch (IOException ioEx) {
			outData = ioEx.toString().getBytes();
		} finally {
			try {
				stream.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return outData;
	}
}
