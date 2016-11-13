package keisuke;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class LanguageClassifyFunc extends AbstractLanguageDefine implements IfClassifyFunc {

	protected String classify = CommonDefine.OPTVAL_LANGUAGE;
	
	/** 言語分類（デフォルト）のコンストラクタ */
	public LanguageClassifyFunc() {
		this.xmlElemFactory = new XmlElementFactory();
		init();
	}
	
	/** 言語グループ分類の場合のコンストラクタ */
	public LanguageClassifyFunc(String str) {
		if (str != null && str.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			this.classify = str;
		}
		this.xmlElemFactory = new XmlElementFactory();
		init();
	}
	
	public String getClassifyName(String strpath) {
		if (strpath == null) {
			return null;
		}
		if (this.langExtMap == null) {
			System.err.println("![WARN] there is no language definition map.");
			return null;
		}
		// 拡張子の取得
		String strext = FilenameUtils.getExtension(strpath);
		if (strext == null) {
			return null;
		}
		// 小文字に変換し、ピリオドを付与
		strext = "." + strext.toLowerCase();
		// XML定義されていた設定をMapから取得
		LanguageElement le = this.langExtMap.get(strext);
		if (le == null) {
			return "Unsupport";
		} else if (this.classify.equals(CommonDefine.OPTVAL_LANGGROUP)) {
			return le.getGroup();
		} else {
			return le.getName();
		}
	}
	
	public String getClassifyNameForReport(String classifyname) {
		return classifyname;
	}
	
	public List<String> getClassifyFixedList() {
		return null;
	}

}
