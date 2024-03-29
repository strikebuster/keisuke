package keisuke.swing;

import static keisuke.util.StringUtil.SYSTEM_ENCODING;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
//import java.io.UnsupportedEncodingException;

import keisuke.count.AbstractCountMainProc;
//import keisuke.util.StringUtil;

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
	 * ソート種類を設定する
	 * @param sort ソート種類文字列
	 */
	public void setSort(final String sort) {
		this.countProc.setSortOrder(sort);
	}

	/**
	 * パス表記スタイルを設定する
	 * @param path パス表記スタイル
	 */
	public void setPath(final String path) {
		this.countProc.setPathStyle(path);
	}

	/**
	 * フォーマットで指定する出力がテキスト形式か真偽を返す
	 * @return 出力がテキスト形式であればtrue
	 */
	public boolean isTextAsOutput() {
		return this.countProc.isTextAsOutput();
	}

	/**
	 * フォーマットで指定する出力のテキストの文字エンコードを返す
	 * @return 文字エンコード名
	 */
	public String encodingAsOutput() {
		String str = this.countProc.encodingAsOutput();
		if (str == null || str.isEmpty()) {
			return SYSTEM_ENCODING;
		}
		return str;
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
		} catch (IOException e) {
			outData = e.toString().getBytes();
		} finally {
			try {
				stream.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return outData;
	}

	/*
	 * 計測対象に対し計測を実行して結果のフォーマット済みテキストを返す
	 * @param files 計測対象ファイル名を指定する配列
	 * @return 計測結果テキスト
	 *
	public String getCountingResultAsText(final String[] files) {
		String outText = "";
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			this.countProc().doCountingAndWriting(files, stream);
			outText = stream.toString(this.encodingAsOutput());
		} catch (UnsupportedEncodingException e) {
			outText = e.toString();
		} catch (IOException e) {
			outText = e.toString();
		} finally {
			try {
				stream.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		//return StringUtil.normalizeLineSeparator(outText);
		return outText;
	}*/
}
