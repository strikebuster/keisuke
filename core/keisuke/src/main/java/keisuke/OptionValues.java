package keisuke;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * コマンドライン引数オプションの値が選択の場合の選択肢を定義するクラス
 */
public class OptionValues {

	private List<String> valuesList = new ArrayList<String>();

	public OptionValues() {	}

	/**
	 * 選択肢の値を追加する
	 * 選択肢の1番目に追加された値はデフォルト値として扱う
	 * @param value 選択肢の値
	 */
	public void add(final String value) {
		this.valuesList.add(value);
	}

	/**
	 * 選択肢の後半に可変パラメータがある値を追加する
	 * 選択肢の1番目に追加された値はデフォルト値として扱う
	 * @param value 選択肢の値
	 * @param parameter 可変パラメータ名
	 */
	public void add(final String value, final String parameter) {
		String s = value + "<" + parameter + ">";
		this.valuesList.add(s);
	}

	/**
	 * 確認対象の値が選択肢に含まれるかをチェック
	 * @param value 確認対象の値
	 * @return 含まれていればtrue
	 */
	public boolean contains(final String value) {
		Iterator<String> it = this.valuesList.iterator();
		while (it.hasNext()) {
			String str = it.next();
			int pos = str.indexOf('<');
			if (pos > 0) {
				String prefix = str.substring(0, pos);
				if (value.startsWith(prefix)) {
					return true;
				}
			} else if (str.equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 選択肢のデフォルト値を返す
	 * @return デフォルト値
	 */
	public String getDefault() {
		if (this.valuesList.isEmpty()) {
			return null;
		}
		return this.valuesList.get(0);
	}

	/**
	 * 使用方法表示用に選択肢を列挙する文字列を返す
	 * @return 表示用文字列
	 */
	public String printList() {
		if (this.valuesList.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = this.valuesList.iterator();
		String value = it.next();
		sb.append(this.getQuotedString(value));
		while (it.hasNext()) {
			value = it.next();
			sb.append(" | ");
			sb.append(this.getQuotedString(value));
		}
		return sb.toString();
	}

	private String getQuotedString(final String value) {
		StringBuilder sb = new StringBuilder();
		sb.append('\'');
		int pos = value.indexOf('<');
		if (pos > 0) {
			sb.append(value.substring(0, pos)).append('\'');
			sb.append(value.substring(pos));
		} else {
			sb.append(value).append('\'');
		}
		return sb.toString();
	}

	/**
	 * オプションリストをString配列に格納して返す
	 * @return オプションリストを配列化したインスタンス
	 */
	public String[] getValuesAsArray() {
		return (String[]) this.valuesList.toArray(new String[this.valuesList.size()]);
	}
}
