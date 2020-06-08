package keisuke.count.language;

import java.io.File;

import keisuke.count.NakedSourceCode;
import keisuke.count.StepCountResultForCount;
import keisuke.util.LogUtil;

import static keisuke.count.language.StepCounterConstant.*;

/**
 * 行数カウントと有効行抽出カットの結果格納クラス
 */
public class OperationResult {
	private OperationType operation;
	private String sourceType;
	private StepCountResultForCount countResult = null;
	private long executableStep = 0;
	private long commentStep = 0;
	private long blancStep = 0;
	private NakedSourceCode sourceCode = null;
	private StringBuffer buildingSource = null;

	/**
	 * ステップカウントまたはコメント除去ソース抽出の操作を指定するコンストラクター
	 * @param ope OperationTypeのCOUNTまたはCUTを指定
	 * @param type ソースタイプ文字列
	 */
	protected OperationResult(final OperationType ope, final String type) {
		this.operation = ope;
		this.sourceType = type;
		this.buildingSource = new StringBuffer();
	}

	/**
	 * 実行ステップ数に１加える
	 * OPE_CUT処理時にはコメントを除いたソースに引数の文字列を追記する
	 * @param line ソースの実行ステップ文字列
	 */
	protected void appendExecutableStep(final String line) {
		this.increaseExecutableStep();
		if (this.operation == OperationType.CUT) {
			if (line == null || line.isEmpty()) {
				return;
			}
			this.buildingSource.append(line);
		}
		/* DEBUG *
		if (this.sourceType.equals("Haskell")) {
			LogUtil.debugLog("type=" + this.sourceType + " cut:" + line);
		}
		/* */
	}

	/**
	 * 実行ステップ数に１加える
	 */
	private void increaseExecutableStep() {
		this.executableStep++;
	}

	/**
	 * コメントステップ数に１加える
	 */
	protected void increaseCommentStep() {
		this.commentStep++;
	}

	/**
	 * 空白ステップ数に１加える
	 */
	protected void increaseBlancStep() {
		this.blancStep++;
	}

	/**
	 * ステップカウント結果を持つStepCountResultForCountインスタンスをセットする
	 * @param result CountResultインスタンス
	 */
	protected void setCountResult(final StepCountResultForCount result) {
		this.countResult = result;
	}

	/**
	 * 保持しているステップカウント結果を元にStepCountResultForCountインスタンスを作成して保持する
	 * @param file ソースのFileインスタンス
	 * @param type ソースのファイルタイプ
	 * @param category ソースのカテゴリ
	 */
	protected void makeCountResult(final File file, final String type, final String category) {
		StepCountResultForCount result = new StepCountResultForCount(file, file.getName(), type,
				category, this.executableStep, this.blancStep, this.commentStep);
		this.setCountResult(result);
	}

	/**
	 * 保持しているステップカウント結果を返す
	 * @return ステップカウント結果
	 */
	protected StepCountResultForCount getCountResult() {
		return this.countResult;
	}

	/**
	 * コメントを除いたソースを持つNakedSourceCodeインスタンスをセットする
	 * @param source NakedSourceCodeインスタンス
	 */
	protected void setDiffSource(final NakedSourceCode source) {
		this.sourceCode = source;
	}

	/**
	 * 保持しているコメントを除いたソースを元にDiffSourceインスタンスを作成して保持する
	 * @param category ソースのカテゴリ
	 */
	protected void makeNakedSource(final String category) {
		NakedSourceCode code = new NakedSourceCode(this.buildingSource.toString(),
				category, IGNORE_NON);
		this.setDiffSource(code);
	}

	/**
	 * 保持しているコメントを除いたソースを返す
	 * @return コメントを除いたソース
	 */
	protected NakedSourceCode getNakedSource() {
		return this.sourceCode;
	}

	/**
	 * 保持しているステップカウント結果またはコメントを除いたソースを元に処理フラグによって
	 * 結果となるCountResultインスタンスまたはDiffSourceインスタンスを作成して保持する
	 * @param file ソースのFileインスタンス
	 * @param type ソースのファイルタイプ
	 * @param category ソースのカテゴリ
	 */
	protected void makeResult(final File file, final String type, final String category) {
		if (this.operation == OperationType.COUNT) {
			this.makeCountResult(file, type, category);
		} else if (this.operation == OperationType.CUT) {
			this.makeNakedSource(category);
		}
	}

	/**
	 * 解析対象ソースが無視対象の場合の結果を作成して保持する
	 * @param category ソースのカテゴリ
	 */
	protected void makeIgnoredResult(final String category) {
		if (this.operation == OperationType.COUNT) {
			this.setCountResult(null);
		} else if (this.operation == OperationType.CUT) {
			NakedSourceCode source = new NakedSourceCode(null, category, IGNORE_YES);
			this.setDiffSource(source);
		}
	}

	/**
	 * 別のインスタンスのカウント変数をコピーする
	 * CountResultインスタンスとDiffSourceインスタンスはコピーせずに
	 * nullに初期化する
	 * @param another コピー元のインスタンス
	 */
	protected void copyFrom(final OperationResult another) {
		if (another == null) {
			this.reset();
			return;
		}
		this.countResult = null;
		this.executableStep = another.executableStep;
		this.commentStep = another.commentStep;
		this.blancStep = another.blancStep;
		this.sourceCode = null;
		this.buildingSource = new StringBuffer(another.buildingSource);
	}

	/**
	 * インスタンス変数を初期化する
	 */
	protected void reset() {
		this.countResult = null;
		this.executableStep = 0;
		this.commentStep = 0;
		this.blancStep = 0;
		this.sourceCode = null;
		this.buildingSource = new StringBuffer();
	}

	/**
	 * LogUtilのインポートを正当化するためのダミー
	 */
	void noUsingDummy() {
		LogUtil.warningLog("This must not be called, because dummy. " + this.sourceType);
	}
}
