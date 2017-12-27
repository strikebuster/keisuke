package keisuke.report;

import static keisuke.report.property.PropertyConstant.*;

import keisuke.DiffCountResult;
import keisuke.util.LogUtil;
import keisuke.util.NumberUtil;

/**
 * Account element for result of DiffCount.
 */
public class DiffCountResultForReport extends DiffCountResult implements CountResultForReport {

	private int nodeDepth = 0;
	private String statusText = "";
	private long sumSteps = 0;
	private long files = 0;
	private String classify = "";

	/**
	 * DiffCount結果を集計分類ための分類キーで初期化するコンストラクタ
	 * @param name 集計分類のキー名
	 */
	public DiffCountResultForReport(final String name) {
		super();
		this.classify = name;
	}

	/**
	 * DiffCount結果の対象ファイル｜ディレクトリの値一式を設定するコンストラクタ
	 * @param line 結果ファイルの１行
	 * @param index 結果ファイルの行位置
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public DiffCountResultForReport(final String line, final int index) throws IllegalFormattedLineException {
		super();
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
	 * DiffCount結果の対象ファイル｜ディレクトリの変更ステータス文字列を返す
	 * @return 変更ステータス文字列
	 */
	public String statusText() {
		return this.statusText;
	}

	private void addValues(final long add, final long del) {
		this.setSteps(this.addedSteps() + add, this.deletedSteps() + del);
	}

	/**
	 * DiffCounterの計測結果Textファイルの１行を解析して、要素ごとの値を取り込む
	 * @param line Textファイルの１行
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	private void load(final String line) throws IllegalFormattedLineException {
		if (line == null || line.isEmpty()) {
			LogUtil.warningLog("line is null or empty.");
			throw new IllegalFormattedLineException("null or empty.");
		}
		// ノード名と階層の深さを取得
        int pos = line.indexOf("/["); // ディレクトリの"/"があるか
        if (pos < 0) {
        	this.setIsFile(true);
        }
        int pos2 = line.indexOf("[");
        if (pos2 < 0) {
        	LogUtil.warningLog("'[' not found in " + line);
        	throw new IllegalFormattedLineException();
        } else if (pos2 == 0) {
        	LogUtil.warningLog("node name not found before '[' in " + line);
        	throw new IllegalFormattedLineException("'[' not found.");
        }
        String strname = line.substring(0, pos2);
        this.nodeDepth = this.countHeadSpacesIn(strname); // 空白文字の数で階層の深さを測る
        this.setNodeName(strname.substring(this.nodeDepth));

        // 変更ステータスを取得
        pos2++;
        int pos3 = line.indexOf("]", pos2);
        if (pos3 < 0) {
        	LogUtil.warningLog("']' not found in " + line);
        	throw new IllegalFormattedLineException();
        } else if (pos3 == 0) {
        	LogUtil.warningLog("status not found between '[' and ']' in " + line);
        	throw new IllegalFormattedLineException("status not found.");
        }
        this.statusText = line.substring(pos2, pos3);

        // 差分の増減数を取得
        pos3++;
        String strwk = line.substring(pos3).trim();
        String[] strarray = strwk.split(" ");
        long numadd = 0;
        long numdel = 0;
        if (strarray.length >= 2) {
        	String stradd = strarray[0];
        	String strdel = strarray[1];
        	try {
        		numadd = NumberUtil.parseLong(stradd);
        		numdel = NumberUtil.parseLong(strdel) * -1;
        	} catch (NumberFormatException e) {
        		LogUtil.warningLog("NumberFormatException at " + line);
        		throw new IllegalFormattedLineException("fail to parse integer.");
        	}
        } else {
        	LogUtil.warningLog("'+m -n' not found in " + line);
        	throw new IllegalFormattedLineException("'+m -n' not found.");
        }
        // 解析結果の数値を設定
        this.setSteps(numadd, numdel);
		this.sumSteps = numadd + numdel;
		this.files = 1;
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

	/** {@inheritDoc} */
	public void add(final CountResultForReport result) {
		if (result == null || !(result instanceof DiffCountResultForReport)) {
			return;
		}
		DiffCountResultForReport another = (DiffCountResultForReport) result;
		if (!this.classify.equals(another.classify)) {
			return;
		}
		this.addValues(another.addedSteps(), another.deletedSteps());
		this.sumSteps += another.sumSteps;
		this.files += another.files;
	}

	/** {@inheritDoc} */
	public long getValue(final String key) {
		if (key == null) {
			return 0;
		}
		long val;
		if (key.equals(DP_ADDSTEP)) {
			val = this.addedSteps();
		} else if (key.equals(DP_DELSTEP)) {
			val = this.deletedSteps();
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
