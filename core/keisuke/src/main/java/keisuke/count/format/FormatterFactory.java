package keisuke.count.format;

import jp.sf.amateras.stepcounter.format.JSONFormatter;
import jp.sf.amateras.stepcounter.format.ResultFormatter;
import jp.sf.amateras.stepcounter.format.XMLFormatter;

/**
 * フォーマッタのインスタンスを作成するためのファクトリクラス。
 * keisuke: パッケージの変更
 * 　　　　　出力定形文言に日本語が埋め込まれていたものをproperties化するため
 */
public class FormatterFactory {

	/**
	 * フォーマッタのインスタンスを生成します。
	 *
	 * @param format フォーマット
	 * @return フォーマッタのインスタンス
	 */
	public static ResultFormatter getFormatter(String format){
		// nullの場合はデフォルトフォーマット
		if(format==null){
			return new DefaultFormatter();
		}
		String name = format.toLowerCase();
		// CSVフォーマット
		if(name.equals("csv")){
			return new CSVFormatter();
			
		// XMLフォーマット
		} else if(name.equals("xml")){
			return new XMLFormatter();
			
		// JSONフォーマット
		} else if(name.equals("json")){
			return new JSONFormatter();
			
		// Excelフォーマット
		} else if(name.equals("excel")){
			return new ExcelFormatter();

		// デフォルトフォーマット
		} else {
			return new DefaultFormatter();
		}
	}

}
