package keisuke;

import java.util.Map;

public interface IfArgFunc {
	/** コマンドライン引数の解析 */
	public Map<String, String> makeMapOfArgs(String[] args) ;

}
