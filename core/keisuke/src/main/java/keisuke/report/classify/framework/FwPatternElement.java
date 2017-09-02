package keisuke.report.classify.framework;

import java.util.regex.Pattern;

/**
 * Framework artifact type path pattern element node of XML to define framework.
 * It has a compiled regular expression pattern which means to match source file path
 * with artifact type specification.
 */
public class FwPatternElement implements Comparable<FwPatternElement> {

	private String name = null;
	private String patternString = null;
	private int patternLength = -1;
	private Pattern pattern = null;

	/**
	 * 解析対象の指定されたFramework成果物種類に対する指定された順番にあるパス定義を
	 * パターンマッチコンパイルされたPatternを設定するコンストラクタ
	 * @param fwSpecElem Framework成果物種類を定義するFwSpecificElement
	 * @param idx 指定の成果物種類における何番目のパターン定義か示す数
	 */
	protected FwPatternElement(final FwSpecificElement fwSpecElem, final int idx) {
		if (fwSpecElem == null) {
			return;
		}
		this.name = fwSpecElem.getName();
		if (idx < 0 || idx >= fwSpecElem.countPatternStrings()) {
			return;
		}
		this.patternString = fwSpecElem.getPatternStrings().get(idx);
		if (this.patternString == null) {
			return;
		}
		this.patternLength = this.patternString.length();
		this.pattern = Pattern.compile(this.patternString);
	}

	/**
	 * 解析対象のFramework成果物種類の名称を返す
	 * @return 成果物種類の名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 解析対象のFramework成果物種類のグループ名称を返す
	 * @return 成果物種類グループの名称
	 */
	protected Pattern getPattern() {
		return this.pattern;
	}

	/**
	 * 定義されたパターン文字列の長さを返す
	 * @return パターン文字列長
	 */
	protected int getPatternLength() {
		return this.patternLength;
	}

	/**
	 * このオブジェクトと指定されたオブジェクトの順序を比較します。
	 * 順序はこのオブジェクトのpatternLengthの値の大小で決定されます。
	 * このオブジェクトのpatternLengthが指定されたオブジェクトのものより小さい場合は負の整数、
	 * 等しい場合はゼロ、大きい場合は正の整数を返します。
	 * 順序が同じ（返り値がセロ）であってもequals()はtrueとならない点に注意すること。
	 * @param anotherElem 比較対象のFwPatternElementオブジェクト
	 * @return このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、
	 * 等しい場合はゼロ、大きい場合は正の整数
	 * @exception NullPointerException 指定されたオブジェクトが null の場合
	 */
	public int compareTo(final FwPatternElement anotherElem) throws NullPointerException {
		if (anotherElem == null) {
			throw new NullPointerException();
		}
		return (this.patternLength - anotherElem.getPatternLength());
	}

	/**
	 * DEBUG用Framework成果物種類定義の解析ログの文字列を作成する
	 * @return ログ文字列
	 */
	protected String debug() {
		StringBuilder sb = new StringBuilder();
		sb.append("[DEBUG] FwPatternElement : name=" + this.name);
		sb.append("\n");
		sb.append("[DEBUG] FwPatternElement : patternStringn=" + this.patternString
					+ " , length=" + this.patternLength);
		sb.append("\n");
		return sb.toString();
	}
}
