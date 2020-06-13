package keisuke.report;

import static keisuke.report.option.ReportOptionConstant.OPTVAL_CSV;
import static keisuke.report.property.PropertyConstant.*;

import keisuke.DiffCountEnum;
import keisuke.DiffCountResult;
import keisuke.util.LogUtil;
import keisuke.util.NumberUtil;

/**
 * Account element for result of DiffCount.
 */
public class DiffCountResultForReport extends DiffCountResult implements CountResultForReport {
	private static final long serialVersionUID = 1L; // since ver.2.0.1

	private static final int DIFFCOUNT_COLUMN_NUM = DiffCountEnum.values().length;

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
	 * @param format 結果ファイルのフォーマット形式
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public DiffCountResultForReport(final String line, final String format) throws IllegalFormattedLineException {
		super();
		if (OPTVAL_CSV.equals(format)) {
			// StepCounter.DiffCountのCSV形式出力
			this.loadCsv(line);
		} else {
			// StepCounter.DiffCountのTEXT形式出力
			this.loadText(line);
		}
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

	private void setStatusText(final String status) {
		this.statusText = status;
	}

	private void addValues(final long add, final long del) {
		this.setSteps(this.addedSteps() + add, this.deletedSteps() + del);
	}

	/**
	 * DiffCounterの計測結果CSVファイルの１行を解析して、要素ごとの値を取り込む
	 * @param line CSVファイルの１行
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	private void loadCsv(final String line) throws IllegalFormattedLineException {
		if (line == null || line.isEmpty()) {
			LogUtil.warningLog("line is null or empty.");
			throw new IllegalFormattedLineException("null or empty.");
		}
		this.setIsFile(true);
		// 列要素分解
		String[] strArray = line.split(",", -1);
		int salen = strArray.length;
		int skipcols = 0;
		String strPath = "";
		@SuppressWarnings("unused")
		String strType = "";
		@SuppressWarnings("unused")
		String strCategory = "";
		String strStatus = "";
		String strAddStep = "0";
		String strDelStep = "0";
		if (salen > DIFFCOUNT_COLUMN_NUM) {
			// DiffCount -f csvの出力でカテゴリ文字列内に","が含まれる場合の回避
			skipcols = salen - DIFFCOUNT_COLUMN_NUM;
		}
		if (salen > DIFFCOUNT_COLUMN_NUM - 1) {
			// ファイルパスの取得
			strPath = strArray[DiffCountEnum.FILE_PATH.index()];
			// ソースタイプの取得
			strType = strArray[DiffCountEnum.SOURCE_TYPE.index()];
			// カテゴリの取得
			StringBuilder sb = new StringBuilder(strArray[DiffCountEnum.SOURCE_CATEGORY.index()]);
			for  (int i = 1; i <= skipcols; i++) {
				sb.append(",").append(strArray[DiffCountEnum.SOURCE_CATEGORY.index() + i]);
			}
			strCategory = sb.toString();
			//LogUtil.debugLog("CATEGORY :" + strCategory);
			strStatus = strArray[DiffCountEnum.DIFF_STATUS.index() + skipcols];
			strAddStep = strArray[DiffCountEnum.ADDED_STEPS.index() + skipcols];
			strDelStep = strArray[DiffCountEnum.DELETED_STEPS.index() + skipcols];
		} else {
			LogUtil.warningLog("too short, ignore line : " + line);
			throw new IllegalFormattedLineException("too short.");
		}
		// 解析結果の数値以外を設定
		// ファイルパスの設定
	    this.setFilePath(strPath);
	    // ファイル名の設定
	    int pos = strPath.lastIndexOf('/');
	    this.setNodeName(strPath.substring(pos));
	    // ソースタイプ、カテゴリは無視
	    // 変更ステータスの設定
	    this.setStatusText(strStatus);
	    // 差分の増減数を取得
	    long numadd = 0;
	    long numdel = 0;
	    try {
	    	if (!strAddStep.trim().isEmpty()) {
	    		numadd = NumberUtil.parseLong(strAddStep);
	    	}
	    	if (!strDelStep.trim().isEmpty()) {
	    		numdel = NumberUtil.parseLong(strDelStep);
	    	}
	    } catch (NumberFormatException e) {
	    	LogUtil.warningLog("NumberFormatException at " + line);
	    	throw new IllegalFormattedLineException("fail to parse integer.");
	    }
	    // 数値が負の場合は異常値なのでスキップ
	    if (numadd < 0 || numdel < 0) {
	    	LogUtil.warningLog("unexpected minus integer, ignore line : " + line);
	    	throw new IllegalFormattedLineException("unexpected minus integer.");
	    }
	    // 解析結果の数値を設定
	    this.setSteps(numadd, numdel);
	    this.sumSteps = numadd + numdel;
	    this.files = 1;
	}

	/**
	 * DiffCounterの計測結果Textファイルの１行を解析して、要素ごとの値を取り込む
	 * @param line Textファイルの１行
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	private void loadText(final String line) throws IllegalFormattedLineException {
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
		this.setStatusText(line.substring(pos2, pos3));

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
