package keisuke;

import static keisuke.report.property.PropertyConstant.*;

/**
 * Account element for result of DiffCount.
 */
public class DiffCountResult implements CountResult {

	private int nodeDepth = 0;
	private boolean isFile = false;
	private String nodeName = "";
	private String statusText = "";
	private int addSteps = 0;
	private int deleteSteps = 0;
	private int sumSteps = 0;
	private int files = 0;
	private String filePath = "";
	private String classify = "";
	private DiffStatusEnum status = null;

	/**
	 * DiffCount結果を集計分類ための分類キーで初期化するコンストラクタ
	 * @param name 集計分類のキー名
	 */
	public DiffCountResult(final String name) {
		this.classify = name;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの値一式を設定するコンストラクタ
	 * @param line 結果ファイルの１行
	 * @param index 結果ファイルの行位置
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public DiffCountResult(final String line, final int index) throws IllegalFormattedLineException {
		this.load(line);
	}

	/**
	 * ノードのトップからの階層の深さを返す
	 * @return 階層の深さ
	 */
	public int depth() {
		return this.nodeDepth;
	}

	/**
	 * DiffCount結果の対象ノードがファイルかチェックする
	 * @return ファイルならtrue
	 */
	public boolean isFile() {
		return this.isFile;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの名称を返す
	 * @return ノード名
	 */
	public String nodeName() {
		return this.nodeName;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの変更ステータス文字列を返す
	 * @return 変更ステータス文字列
	 */
	public String statusText() {
		return this.statusText;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリのパス名を返す
	 * @return パス名
	 */
	public String filePath() {
		return this.filePath;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリのパス名を設定する
	 * @param path パス名
	 */
	public void setFilePath(final String path) {
		this.filePath = path;
	}

	/**
	 * DiffCount結果の対象ソースファイルの集計分類を返す
	 * @return 集計分類のキー名
	 */
	public String classify() {
		return this.classify;
	}

	/**
	 * DiffCount結果の対象ソースファイルの集計分類を設定する
	 * @param name 集計分類のキー名
	 */
	public void setClassify(final String name) {
		this.classify = name;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの変更ステータスを返す
	 * @return 変更ステータス
	 */
	public DiffStatusEnum status() {
		return this.status;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの変更ステータスを設定する
	 * @param stat 変更ステータス
	 */
	public void setDiffStatus(final DiffStatusEnum stat) {
		this.status = stat;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの数値一式を設定する
	 * @param add diff追加行数(正の整数)
	 * @param del diff削除行数(0または負の整数）
	 */
	public void setValues(final int add, final int del) {
		this.addSteps = add;
		this.deleteSteps = del;
		this.sumSteps = add - del;
		this.files = 1;
	}

	/**
	 * DiffCounterの計測結果Textファイルの１行を解析して、要素ごとの値を取り込む
	 * @param line Textファイルの１行
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public void load(final String line) throws IllegalFormattedLineException {
		if (line == null || line.isEmpty()) {
			System.out.println("![WARN] line is null or empty.");
			throw new IllegalFormattedLineException("null or empty.");
		}
		// ノード名と階層の深さを取得
        int pos = line.indexOf("/["); // ディレクトリの"/"があるか
        if (pos < 0) {
        	this.isFile = true;
        }
        int pos2 = line.indexOf("[");
        if (pos2 < 0) {
        	System.out.println("![WARN] '[' not found in " + line);
        	throw new IllegalFormattedLineException();
        } else if (pos2 == 0) {
        	System.out.println("![WARN] node name not found before '[' in " + line);
        	throw new IllegalFormattedLineException("'[' not found.");
        }
        String strname = line.substring(0, pos2);
        this.nodeDepth = this.countHeadSpacesIn(strname); // 空白文字の数で階層の深さを測る
        this.nodeName = strname.substring(this.nodeDepth);

        // 変更ステータスを取得
        pos2++;
        int pos3 = line.indexOf("]", pos2);
        if (pos3 < 0) {
        	System.out.println("![WARN] ']' not found in " + line);
        	throw new IllegalFormattedLineException();
        } else if (pos3 == 0) {
        	System.out.println("![WARN] status not found between '[' and ']' in " + line);
        	throw new IllegalFormattedLineException("status not found.");
        }
        this.statusText = line.substring(pos2, pos3);

        // 差分の増減数を取得
        pos3++;
        String strwk = line.substring(pos3).trim();
        String[] strarray = strwk.split(" ");
        int numadd = 0;
        int numdel = 0;
        if (strarray.length >= 2) {
        	String stradd = strarray[0];
        	String strdel = strarray[1];
        	try {
        		numadd = Integer.parseInt(stradd);
        		numdel = Integer.parseInt(strdel);
        	} catch (NumberFormatException e) {
        		System.out.println("![WARN] NumberFormatException at " + line);
        		throw new IllegalFormattedLineException("fail to parse integer.");
        	}
        } else {
        	System.out.println("![WARN] '+m -n' not found in " + line);
        	throw new IllegalFormattedLineException("'+m -n' not found.");
        }
        // 解析結果の数値を設定
        this.setValues(numadd, numdel);

	}

	private int countHeadSpacesIn(final String line) {
		byte[] chararray = line.getBytes();
		int spcnt = 0;
        for (int i = 0; i < chararray.length; i++) {
        	if (chararray[i] == ' ') {
            	spcnt++;
        	} else {
            	break;
        	}
        }
        return spcnt;
	}

	/**
	 * Diff差分計測結果のステータスがUNCHANGEDかチェックする
	 * @return UNCHANGEDならtrue
	 */
	public boolean isUnchanged() {
		if (this.status.equals(DiffStatusEnum.UNCHANGED)) {
			return true;
		}
		return false;
	}

	/**
	 * Diff差分計測結果のステータスがUNSUPPORTEDかチェックする
	 * @return UNSUPPORTEDならtrue
	 */
	public boolean isUnsupported() {
		if (this.status.equals(DiffStatusEnum.UNSUPPORTED)) {
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	public void add(final CountResult result) {
		if (result == null || !(result instanceof DiffCountResult)) {
			return;
		}
		DiffCountResult another = (DiffCountResult) result;
		if (!this.classify.equals(another.classify)) {
			return;
		}
		this.addSteps += another.addSteps;
		this.deleteSteps += another.deleteSteps;
		this.sumSteps += another.sumSteps;
		this.files += another.files;
	}

	/** {@inheritDoc} */
	public int getValue(final String key) {
		if (key == null) {
			return 0;
		}
		int val;
		if (key.equals(DP_ADDSTEP)) {
			val = this.addSteps;
		} else if (key.equals(DP_DELSTEP)) {
			val = this.deleteSteps;
		} else if (key.equals(DP_SUMSTEP)) {
			val = this.sumSteps;
		} else if (key.equals(DP_FILENUM)) {
			val = this.files;
		} else {
			val = 0;
		}
		return val;
	}
}
