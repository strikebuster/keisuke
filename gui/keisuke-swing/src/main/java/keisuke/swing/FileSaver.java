package keisuke.swing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * データをファイルに保存するクラス
 */
class FileSaver {

	private OutputStream outputStream = null;
	private String message = null;

	/**
	 * 保存先のファイル名を指定してインスタンスを作成する
	 * @param filename ファイル名
	 */
	FileSaver(final String filename) {
		if (filename == null || filename.isEmpty()) {
			this.message = "illegal argument.";
			return;
		}
		try {
			this.outputStream = new FileOutputStream(new File(filename));
		} catch (Exception e) {
			System.err.println("[ERROR] can not open the file : " + filename);
			e.printStackTrace();
			this.message = "fail to open";
		}
	}

	/**
	 * 保存先のファイルを指定してインスタンスを作成する
	 * @param file ファイル
	 */
	FileSaver(final File file) {
		if (file == null) {
			this.message = "illegal argument.";
			return;
		}
		try {
			this.outputStream = new FileOutputStream(file);
		} catch (Exception e) {
			System.err.println("[ERROR] can not open the file : " + file.getPath());
			e.printStackTrace();
			this.message = "fail to open.";
		}
	}

	/**
	 * 保存データをファイルに書き出す
	 * @param data 保存データbyte配列
	 */
	void save(final byte[] data) {
		if (this.outputStream == null) {
			return;
		}
		if (data == null || data.length == 0) {
			this.message = "result is empty, cancel to save.";
			return;
		}
		try {
			this.outputStream.write(data);
			this.outputStream.flush();
			this.message = "finish saving.";
		} catch (IOException e) {
			System.err.println("[ERROR] file i/o error occured.");
			e.printStackTrace();
			this.message = "fail to write.";
		} finally {
			try {
				this.outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存データをファイルに書き出す
	 * @param text 保存データ文字列
	 */
	void save(final String text) {
		if (text == null) {
			this.save(new byte[] {});
		} else {
			this.save(text.getBytes());
		}
	}

	/**
	 * 保存実行結果メッセージを返す
	 * @return 保存実行結果メッセージ
	 */
	String getMessage() {
		return this.message;
	}
}
