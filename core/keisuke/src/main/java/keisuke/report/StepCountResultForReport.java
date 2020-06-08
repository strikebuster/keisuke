package keisuke.report;

import static keisuke.report.property.PropertyConstant.*;

import keisuke.StepCountEnum;
import keisuke.StepCountResult;
import keisuke.util.LogUtil;

/**
 * ソースコードファイルのステップ数計測結果の集計レポート作成用の派生クラス
 */
public class StepCountResultForReport extends StepCountResult implements CountResultForReport {
	private static final long serialVersionUID = 1L; // since ver.2.0.1

	private static final int STEPCOUNT_COLUMN_NUM = StepCountEnum.values().length;
	private long sumSteps = 0;
	private long files = 0;
	private String classify = "";
	private String unsupportedLabel = "UNSUPPORTED";

	/**
	 * StepCount結果を集計分類ための分類キーで初期化するコンストラクタ
	 * @param classname 集計分類のキー名
	 */
	public StepCountResultForReport(final String classname) {
		this.setClassify(classname);
	}

	/**
	 * StepCounterの計測結果CSVファイルの１行を指定して値を設定するコンストラクタ
	 * @param line CSVファイルの１行
	 * @param label 未対応を示すラベル文字列
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public StepCountResultForReport(final String line, final String label) throws IllegalFormattedLineException {
		this.setUnsupportedLabel(label);
		this.load(line);
	}

	/**
	 * StepCount結果の対象ソースファイルの集計分類を設定する
	 * @param name 集計分類のキー名
	 */
	public void setClassify(final String name) {
		this.classify = name;
	}

	/**
	 * StepCount結果の対象ソースファイルの数値一式を設定する
	 * @param exe 実行行数
	 * @param blanc 空白行数
	 * @param comment コメント行数
	 * @param sum 合計行数
	 */
	protected void setValues(final long exe, final long blanc, final long comment, final long sum) {
		this.setSteps(exe, blanc, comment);
		this.sumSteps = sum;
		this.files = 1;
	}

	/**
	 * 未対応を示すラベル文字列を設定する
	 * @param label 未対応を示すラベル文字列
	 */
	protected void setUnsupportedLabel(final String label) {
		this.unsupportedLabel = label;
	}

	/**
	 * StepCounterの計測結果CSVファイルの１行を解析して、要素ごとの値を取り込む
	 * @param line CSVファイルの１行
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	private void load(final String line) throws IllegalFormattedLineException {
		if (line == null || line.isEmpty()) {
			LogUtil.warningLog("line is null or empty.");
			throw new IllegalFormattedLineException("null or empty.");
		}
		// 列要素分解
		String[] strArray = line.split(",", -1);
		int salen = strArray.length;
		int skipcols = 0;
		String strpath = "";
		String strtype = "";
		String strcategory = "";
		String strexestep = "0";
		String strempstep = "0";
		String strcomstep = "0";
		String strsumstep = "0";
		if (salen > STEPCOUNT_COLUMN_NUM) {
			// StepCounterの出力でカテゴリ文字列内に","が含まれる場合の回避
			skipcols = salen - STEPCOUNT_COLUMN_NUM;
		}
		if (salen > STEPCOUNT_COLUMN_NUM - 1) {
			// ファイルパスの取得
			strpath = strArray[StepCountEnum.FILE_PATH.index()];
			// ソースタイプの取得
			strtype = strArray[StepCountEnum.SOURCE_TYPE.index()];
			// カテゴリの取得
			StringBuilder sb = new StringBuilder(strArray[StepCountEnum.SOURCE_CATEGORY.index()]);
			for  (int i = 1; i <= skipcols; i++) {
				sb.append(",").append(strArray[StepCountEnum.SOURCE_CATEGORY.index() + i]);
			}
			strcategory = sb.toString();
			//LogUtil.debugLog("CATEGORY :" + strcategory);
			strexestep = strArray[StepCountEnum.EXEC_STEPS.index() + skipcols];
			strempstep = strArray[StepCountEnum.BLANC_STEPS.index() + skipcols];
			strcomstep = strArray[StepCountEnum.COMMENT_STEPS.index() + skipcols];
			strsumstep = strArray[StepCountEnum.SUM_STEPS.index() + skipcols];
		} else {
			LogUtil.warningLog("too short, ignore line : " + line);
			throw new IllegalFormattedLineException("too short.");
		}
		// 解析結果の数値以外を設定
	    this.setFilePath(strpath);
	    this.setSourceType(strtype);
	    this.setSourceCategory(strcategory);
	    // 未対応であれば数値解析不要
	    if (strtype.equals(this.unsupportedLabel)) {
	    	//LogUtil.debugLog("unsupported, ignore line : " + line);
	    	throw new IllegalFormattedLineException("unsupported target.");
	    }
		// 行数の数値化
		long numexe = -1;
		long numemp = -1;
		long numcom = -1;
		long numall = -1;
		try {
			if (!strexestep.isEmpty()) {
				numexe = Long.parseLong(strexestep);
			}
			if (!strempstep.isEmpty()) {
				numemp = Long.parseLong(strempstep);
			}
			if (!strcomstep.isEmpty()) {
				numcom = Long.parseLong(strcomstep);
			}
			if (!strsumstep.isEmpty()) {
				numall = Long.parseLong(strsumstep);
			}
		} catch (NumberFormatException e) {
			LogUtil.warningLog("illegal format for number in line : " + line);
		}
		// 数値のない解析対象外ファイルはスキップ
	    if (numexe < 0 || numemp < 0 || numcom < 0 || numall < 0) {
	    	LogUtil.warningLog("fail to parse integer, ignore line : " + line);
	    	throw new IllegalFormattedLineException("fail to parse integer.");
	    }
	    // 解析結果の数値を設定
	    this.setValues(numexe, numemp, numcom, numall);
	}

	/** {@inheritDoc} */
	public void add(final CountResultForReport result) {
		if (result == null || !(result instanceof StepCountResultForReport)) {
			return;
		}
		StepCountResultForReport another = (StepCountResultForReport) result;
		if (!this.classify.equals(another.classify)) {
			return;
		}
		this.setSteps(this.execSteps() + another.execSteps(),
				this.blancSteps() + another.blancSteps(),
				this.commentSteps() + another.commentSteps());
		this.sumSteps += another.sumSteps;
		this.files += another.files;
	}

	/** {@inheritDoc} */
	public long getValue(final String key) {
		if (key == null) {
			return -1;
		}
		long val;
		if (key.equals(CP_EXECSTEP)) {
			val = this.execSteps();
		} else if (key.equals(CP_BLANCSTEP)) {
			val = this.blancSteps();
		} else if (key.equals(CP_COMMENTSTEP)) {
			val = this.commentSteps();
		} else if (key.equals(CP_SUMSTEP)) {
			val = this.sumSteps;
		} else if (key.equals(CP_FILENUM)) {
			val = this.files;
		} else {
			val = -1;
		}
		return val;
	}
}
