package keisuke;

import static keisuke.report.property.PropertyConstant.*;

/**
 * Account element for result of StepCount.
 */
public class StepCountResult implements CountResult {

	private static final int STEPCOUNT_COLUMN_NUM = StepCountEnum.values().length;
	private String filePath = "";
	private String fileExt = "";
	private String category = "";
	private int execSteps = 0;
	private int blancSteps = 0;
	private int commentSteps = 0;
	private int sumSteps = 0;
	private int files = 0;
	private String classify = "";

	/**
	 * StepCount結果を集計分類ための分類キーで初期化するコンストラクタ
	 * @param classname 集計分類のキー名
	 */
	public StepCountResult(final String classname) {
		this.classify = classname;
	}

	/**
	 * StepCounterの計測結果CSVファイルの１行を指定して値を設定するコンストラクタ
	 * @param line CSVファイルの１行
	 * @param index CSVファイルの行位置
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public StepCountResult(final String line, final int index) throws IllegalFormattedLineException {
		this.load(line);
	}

	/**
	 * StepCount結果の対象ソースファイルのパス名を返す
	 * @return パス名
	 */
	public String filePath() {
		return this.filePath;
	}

	/**
	 * StepCount結果の対象ソースファイルの拡張子を返す
	 * @return 拡張子
	 */
	public String fileExtension() {
		return this.fileExt;
	}

	/**
	 * StepCount結果の対象ソースファイルのカテゴリ指定を返す
	 * @return カテゴリ指定
	 */
	public String category() {
		return this.category;
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
	public void setValues(final int exe, final int blanc, final int comment, final int sum) {
		this.execSteps = exe;
		this.blancSteps = blanc;
		this.commentSteps = comment;
		this.sumSteps = sum;
		this.files = 1;
	}

	/**
	 * StepCounterの計測結果CSVファイルの１行を解析して、要素ごとの値を取り込む
	 * @param line CSVファイルの１行
	 * @throws IllegalFormattedLineException 解析エラー時に返す
	 */
	public void load(final String line) throws IllegalFormattedLineException {
		if (line == null || line.isEmpty()) {
			System.out.println("![WARN] line is null or empty.");
			throw new IllegalFormattedLineException("null or empty.");
		}
		// 列要素分解
		String[] strArray = line.split(",");
		int salen = strArray.length;
		int skipcols = 0;
		String strpath = "";
		String strext = "";
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
			strpath = strArray[StepCountEnum.FILE_PATH.index()];
			// 拡張子の取得
			strext = strArray[StepCountEnum.FILE_EXTENSION.index()];
			StringBuilder sb = new StringBuilder(strArray[StepCountEnum.FILE_CATEGORY.index()]);
			for  (int i = 1; i <= skipcols; i++) {
				sb.append(",").append(strArray[StepCountEnum.FILE_CATEGORY.index() + i]);
			}
			strcategory = sb.toString();
			//System.out.println("[DEBUG] CATEGORY :" + strcategory);
			strexestep = strArray[StepCountEnum.EXEC_STEPS.index() + skipcols];
			strempstep = strArray[StepCountEnum.BLANC_STEPS.index() + skipcols];
			strcomstep = strArray[StepCountEnum.COMMENT_STEPS.index() + skipcols];
			strsumstep = strArray[StepCountEnum.SUM_STEPS.index() + skipcols];
		} else {
			//System.out.println("[DEBUG] too short, ignore line : " + line);
			throw new IllegalFormattedLineException("too short.");
		}
		// 行数の数値化
		int numexe = -1;
		int numemp = -1;
		int numcom = -1;
		int numall = -1;
		try {
			if (!strexestep.isEmpty()) {
				numexe = Integer.parseInt(strexestep);
			}
			if (!strempstep.isEmpty()) {
				numemp = Integer.parseInt(strempstep);
			}
			if (!strcomstep.isEmpty()) {
				numcom = Integer.parseInt(strcomstep);
			}
			if (!strsumstep.isEmpty()) {
				numall = Integer.parseInt(strsumstep);
			}
		} catch (NumberFormatException e) {
			System.out.println("![WARN] Illegal format for number : " + line);
		}
		// 数値のない解析対象外ファイルはスキップ
	    if (numexe < 0 || numemp < 0 || numcom < 0 || numall < 0) {
	    	//System.out.println("[DEBUG] fail to parse integer, ignore line : " + line);
	    	throw new IllegalFormattedLineException("fail to parse integer.");
	    }
	    // 解析結果を設定
	    this.filePath = strpath;
	    this.fileExt = strext;
	    this.category = strcategory;
	    this.setValues(numexe, numemp, numcom, numall);
	}

	/** {@inheritDoc} */
	public void add(final CountResult result) {
		if (result == null || !(result instanceof StepCountResult)) {
			return;
		}
		StepCountResult another = (StepCountResult) result;
		if (!this.classify.equals(another.classify)) {
			return;
		}
		this.execSteps += another.execSteps;
		this.blancSteps += another.blancSteps;
		this.commentSteps += another.commentSteps;
		this.sumSteps += another.sumSteps;
		this.files += another.files;
	}

	/** {@inheritDoc} */
	public int getValue(final String key) {
		if (key == null) {
			return -1;
		}
		int val;
		if (key.equals(CP_EXECSTEP)) {
			val = this.execSteps;
		} else if (key.equals(CP_BLANCSTEP)) {
			val = this.blancSteps;
		} else if (key.equals(CP_COMMENTSTEP)) {
			val = this.commentSteps;
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
